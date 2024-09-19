package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.ServiceResult.Ok
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

data class GeneriskRedigerbarBrevdata(
    override val pesysData: BrevbakerBrevdata,
    override val saksbehandlerValg: BrevbakerBrevdata,
) : RedigerbarBrevdata<BrevbakerBrevdata, BrevbakerBrevdata>

class KanIkkeReservereBrevredigeringException(override val message: String, val response: Api.ReservasjonResponse) : RuntimeException(message)

class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
    private val penService: PenService,
) {
    companion object {
        val RESERVASJON_TIMEOUT = 10.minutes.toJavaDuration()
    }

    suspend fun opprettBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        avsenderEnhetsId: String?,
        saksbehandlerValg: BrevbakerBrevdata,
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
    ): ServiceResult<Dto.Brevredigering> =
        harTilgangTilEnhet(call, avsenderEnhetsId) {
            val signerendeSaksbehandler = navansattService.hentNavansatt(call, call.principal().navIdent)
                ?.let { "${it.fornavn} ${it.etternavn}" }
                ?: call.principal().fullName

            rendreBrev(
                call = call,
                brevkode = brevkode,
                spraak = spraak,
                saksId = sak.saksId,
                saksbehandlerValg = saksbehandlerValg,
                avsenderEnhetsId = avsenderEnhetsId,
                signaturSignerende = signerendeSaksbehandler,
            ).map { letter ->
                transaction {
                    Brevredigering.new {
                        saksId = sak.saksId
                        opprettetAvNavIdent = call.principal().navIdent()
                        this.brevkode = brevkode
                        this.spraak = spraak
                        this.avsenderEnhetId = avsenderEnhetsId
                        this.saksbehandlerValg = saksbehandlerValg
                        laastForRedigering = false
                        distribusjonstype = Distribusjonstype.SENTRALPRINT
                        redigeresAvNavIdent = call.principal().navIdent().takeIf { reserverForRedigering }
                        sistReservert = Instant.now().truncatedTo(ChronoUnit.MILLIS).takeIf { reserverForRedigering }
                        opprettet = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        redigertBrev = letter.toEdit()
                        sistRedigertAvNavIdent = call.principal().navIdent()
                        signaturSignerende = signerendeSaksbehandler
                    }.also {
                        if (mottaker != null) {
                            Mottaker.new(it.id.value) { oppdater(mottaker) }
                        }
                    }.toDto()
                }
            }
        }

    suspend fun oppdaterBrev(
        call: ApplicationCall,
        saksId: Long?,
        brevId: Long,
        nyeSaksbehandlerValg: BrevbakerBrevdata?,
        nyttRedigertbrev: Edit.Letter?,
    ): ServiceResult<Dto.Brevredigering>? =
        hentBrevMedReservasjon(call = call, brevId = brevId, saksId = saksId) { brev ->
            rendreBrev(
                call = call,
                brevkode = brev.info.brevkode,
                spraak = brev.info.spraak,
                saksId = brev.info.saksId,
                saksbehandlerValg = nyeSaksbehandlerValg ?: brev.saksbehandlerValg,
                avsenderEnhetsId = brev.info.avsenderEnhetId,
                signaturSignerende = brev.info.signaturSignerende,
            ).map { rendretBrev ->
                    transaction {
                        Brevredigering[brevId].apply {
                            redigertBrev = (nyttRedigertbrev ?: brev.redigertBrev).updateEditedLetter(rendretBrev)
                            sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                            saksbehandlerValg = nyeSaksbehandlerValg ?: brev.saksbehandlerValg
                            sistRedigertAvNavIdent = call.principal().navIdent()
                        }.toDto()
                    }
                }
        }

    fun delvisOppdaterBrev(
        saksId: Long,
        brevId: Long,
        laastForRedigering: Boolean? = null,
        distribusjonstype: Distribusjonstype? = null,
        mottaker: Dto.Mottaker? = null,
    ): Dto.Brevredigering? =
        transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId)?.apply {
                laastForRedigering?.also { this.laastForRedigering = it }
                distribusjonstype?.also { this.distribusjonstype = it }
                mottaker?.also { this.mottaker?.oppdater(mottaker) ?: Mottaker.new(brevId) { oppdater(mottaker) } }
            }?.also { Brevredigering.reload(it, true) }?.toDto()
        }

    suspend fun oppdaterSignatur(call: ApplicationCall, brevId: Long, signaturSignerende: String): ServiceResult<Dto.Brevredigering>? =
        hentBrevMedReservasjon(call = call, brevId = brevId) { brev ->
            rendreBrev(
                call = call,
                brevkode = brev.info.brevkode,
                spraak = brev.info.spraak,
                saksId = brev.info.saksId,
                saksbehandlerValg = brev.saksbehandlerValg,
                avsenderEnhetsId = brev.info.avsenderEnhetId,
                signaturSignerende = signaturSignerende,
            ).map { rendretBrev ->
                    transaction {
                        Brevredigering[brevId].apply {
                            redigertBrev = brev.redigertBrev.updateEditedLetter(rendretBrev)
                            this.signaturSignerende = signaturSignerende
                        }.toDto()
                    }
                }
        }

    /**
     * Slett brev med id.
     * @return `true` om brevet ble slettet, false om brevet ikke eksisterer,
     */
    fun slettBrev(saksId: Long, brevId: Long): Boolean {
        return transaction {
            val brev = Brevredigering.findByIdAndSaksId(brevId, saksId)
            if (brev != null) {
                brev.delete()
                true
            } else {
                false
            }
        }
    }

    suspend fun hentBrev(call: ApplicationCall, saksId: Long, brevId: Long, reserverForRedigering: Boolean = false): ServiceResult<Dto.Brevredigering>? =
        if (reserverForRedigering) {
            hentBrevMedReservasjon(call = call, brevId = brevId, saksId = saksId) { brev ->
                rendreBrev(
                    call = call,
                    brevkode = brev.info.brevkode,
                    spraak = brev.info.spraak,
                    saksId = brev.info.saksId,
                    saksbehandlerValg = brev.saksbehandlerValg,
                    avsenderEnhetsId = brev.info.avsenderEnhetId,
                    signaturSignerende = brev.info.signaturSignerende,
                ).map { rendretBrev ->
                        transaction {
                            Brevredigering[brevId].apply { redigertBrev = brev.redigertBrev.updateEditedLetter(rendretBrev) }.toDto()
                        }
                    }
            }
        } else {
            transaction { Brevredigering.findByIdAndSaksId(brevId, saksId)?.toDto() }
                ?.let { Ok(it) }
        }

    fun hentBrevForSak(saksId: Long): List<Dto.BrevInfo> =
        transaction {
            Brevredigering.find { BrevredigeringTable.saksId eq saksId }
                .map { it.toBrevInfo() }
        }

    suspend fun fornyReservasjon(call: ApplicationCall, brevId: Long): Api.ReservasjonResponse? =
        hentBrevMedReservasjon(call = call, brevId = brevId) { brev ->
            Api.ReservasjonResponse(
                vellykket = true,
                reservertAv = Api.NavAnsatt(id = call.principal().navIdent(), navn = call.principal().fullName),
                timestamp = brev.info.sistReservert ?: Instant.now(),
                expiresIn = RESERVASJON_TIMEOUT,
                redigertBrevHash = brev.redigertBrevHash,
            )
        }

    suspend fun hentEllerOpprettPdf(call: ApplicationCall, saksId: Long, brevId: Long): ServiceResult<ByteArray>? {
        val (brevredigering, document) = transaction { Brevredigering.findByIdAndSaksId(brevId, saksId).let { it?.toDto() to it?.document?.firstOrNull()?.toDto() } }

        return brevredigering?.let {
            val currentHash = brevredigering.redigertBrevHash

            if (document != null && document.redigertBrevHash == currentHash) {
                Ok(document.pdf)
            } else {
                opprettPdf(call, brevredigering, currentHash)
            }
        }
    }

    suspend fun sendBrev(call: ApplicationCall, saksId: Long, brevId: Long): ServiceResult<Pen.BestillBrevResponse>? {
        val (brev, document) = transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId).let { it?.toDto() to it?.document?.firstOrNull()?.toDto() }
        }

        return if (brev != null && document != null) {
            val template = brevbakerService.getRedigerbarTemplate(call, brev.info.brevkode)

            if (template == null) {
                ServiceResult.Error(
                    "Mangler TemplateDescription for ${brev.info.brevkode}",
                    HttpStatusCode.InternalServerError
                )
            } else {
                penService.sendbrev(
                    call = call,
                    sendRedigerbartBrevRequest = Pen.SendRedigerbartBrevRequest(
                        dokumentDato = document.dokumentDato,
                        saksId = brev.info.saksId,
                        enhetId = brev.info.avsenderEnhetId,
                        templateDescription = template,
                        brevkode = brev.info.brevkode,
                        pdf = document.pdf,
                        eksternReferanseId = "skribenten:${brev.info.id}",
                        mottaker = brev.info.mottaker?.toPen(),
                    ),
                    distribuer = brev.info.distribusjonstype == Distribusjonstype.SENTRALPRINT,
                ).onOk {
                    if (it.journalpostId != null) {
                        transaction {
                            Brevredigering[brevId].delete()
                        }
                    }
                }
            }
        } else null
    }

    fun fjernOverstyrtMottaker(brevId: Long, saksId: Long): Boolean =
        transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId)?.also { it.mottaker?.delete() } != null
        }

    private suspend fun <T> hentBrevMedReservasjon(call: ApplicationCall, brevId: Long, saksId: Long? = null, block: suspend (Dto.Brevredigering) -> T): T? =
        transaction(Connection.TRANSACTION_REPEATABLE_READ) {
            Brevredigering.findByIdAndSaksId(brevId, saksId)
                ?.apply {
                    if (redigeresAvNavIdent == null || redigeresAvNavIdent == call.principal().navIdent() || erReservasjonUtloept()) {
                        redigeresAvNavIdent = call.principal().navIdent()
                        sistReservert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                    }
                }?.toDto()
        }?.let { brev ->
            if (brev.info.redigeresAv == call.principal().navIdent()) {
                block(brev)
            } else throw KanIkkeReservereBrevredigeringException(
                message = "Brev er allerede reservert av: ${brev.info.redigeresAv}",
                response = Api.ReservasjonResponse(
                    vellykket = false,
                    reservertAv = brev.info.redigeresAv!!.let { Api.NavAnsatt(id = it, navn = navansattService.hentNavansatt(call, it.id)?.navn) },
                    timestamp = brev.info.sistReservert ?: Instant.now(),
                    expiresIn = RESERVASJON_TIMEOUT,
                    redigertBrevHash = brev.redigertBrevHash,
                )
            )
        }

    private suspend fun rendreBrev(
        call: ApplicationCall,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        saksId: Long,
        saksbehandlerValg: BrevbakerBrevdata,
        avsenderEnhetsId: String?,
        signaturSignerende: String,
        attesterendeSaksbehandler: NavIdent? = null,
    ): ServiceResult<LetterMarkup> = coroutineScope {
        val signaturAttesterende = async { attesterendeSaksbehandler?.let { navansattService.hentNavansatt(call, it.id) } }

        penService.hentPesysBrevdata(call = call, saksId = saksId, brevkode = brevkode, avsenderEnhetsId = avsenderEnhetsId)
            .then { pesysData ->
                brevbakerService.renderMarkup(
                    call = call,
                    brevkode = brevkode,
                    spraak = spraak,
                    brevdata = GeneriskRedigerbarBrevdata(
                        pesysData = pesysData.brevdata,
                        saksbehandlerValg = saksbehandlerValg,
                    ),
                    felles = pesysData.felles.copy(signerendeSaksbehandlere = SignerendeSaksbehandlere(signaturSignerende, signaturAttesterende.await()?.navn))
                )
            }
    }

    private suspend fun <T> harTilgangTilEnhet(call: ApplicationCall, enhetsId: String?, then: suspend () -> ServiceResult<T>): ServiceResult<T> {
        return if (enhetsId == null) {
            then()
        } else {
            navansattService.harTilgangTilEnhet(call, call.principal().navIdent, enhetsId)
                .then { harTilgang ->
                    if (harTilgang) {
                        then()
                    } else ServiceResult.Error("Mangler tilgang til NavEnhet $enhetsId", HttpStatusCode.Forbidden)
                }
        }
    }

    private suspend fun opprettPdf(call: ApplicationCall, brevredigering: Dto.Brevredigering, redigertBrevHash: EditLetterHash): ServiceResult<ByteArray> {
        return penService.hentPesysBrevdata(
            call = call,
            saksId = brevredigering.info.saksId,
            brevkode = brevredigering.info.brevkode,
            avsenderEnhetsId = brevredigering.info.avsenderEnhetId
        ).then { pesysData ->
            brevbakerService.renderPdf(
                call = call,
                brevkode = brevredigering.info.brevkode,
                spraak = brevredigering.info.spraak,
                brevdata = GeneriskRedigerbarBrevdata(
                    pesysData = pesysData.brevdata,
                    saksbehandlerValg = brevredigering.saksbehandlerValg,
                ),
                felles = pesysData.felles,
                redigertBrev = brevredigering.redigertBrev.toMarkup()
            ).map {
                transaction {
                    val update: Document.() -> Unit = {
                        this.brevredigering = Brevredigering[brevredigering.info.id]
                        pdf = ExposedBlob(it.file)
                        dokumentDato = pesysData.felles.dokumentDato
                        this.redigertBrevHash = redigertBrevHash
                    }
                    Document.findSingleByAndUpdate(DocumentTable.brevredigering eq brevredigering.info.id, update)?.pdf?.bytes
                        ?: Document.new(update).pdf.bytes
                }
            }
        }
    }

    private fun Mottaker.oppdater(mottaker: Dto.Mottaker?) =
        if (mottaker != null) {
            type = mottaker.type
            tssId = mottaker.tssId
            navn = mottaker.navn
            postnummer = mottaker.postnummer
            poststed = mottaker.poststed
            adresselinje1 = mottaker.adresselinje1
            adresselinje2 = mottaker.adresselinje2
            adresselinje3 = mottaker.adresselinje3
            landkode = mottaker.landkode
        } else delete()

    private fun Brevredigering.erReservasjonUtloept(): Boolean =
        sistReservert?.plus(RESERVASJON_TIMEOUT)?.isBefore(Instant.now()) == true

    private fun Brevredigering.toDto(): Dto.Brevredigering =
        Dto.Brevredigering(
            info = toBrevInfo(),
            redigertBrev = redigertBrev,
            redigertBrevHash = redigertBrevHash,
            saksbehandlerValg = saksbehandlerValg,
        )

    private fun Brevredigering.toBrevInfo(): Dto.BrevInfo =
        Dto.BrevInfo(
            id = id.value,
            saksId = saksId,
            opprettetAv = opprettetAvNavIdent,
            opprettet = opprettet,
            sistredigertAv = sistRedigertAvNavIdent,
            sistredigert = sistredigert,
            redigeresAv = redigeresAvNavIdent.takeIf { !erReservasjonUtloept() },
            brevkode = brevkode,
            laastForRedigering = laastForRedigering,
            distribusjonstype = distribusjonstype,
            mottaker = mottaker?.toDto(),
            avsenderEnhetId = avsenderEnhetId,
            spraak = spraak,
            sistReservert = sistReservert,
            signaturSignerende = signaturSignerende,
        )

    private fun Mottaker.toDto(): Dto.Mottaker =
        when (type) {
            MottakerType.SAMHANDLER -> Dto.Mottaker.samhandler(tssId!!)
            MottakerType.NORSK_ADRESSE -> Dto.Mottaker.norskAdresse(
                navn = navn!!,
                postnummer = postnummer!!,
                poststed = poststed!!,
                adresselinje1 = adresselinje1,
                adresselinje2 = adresselinje2,
                adresselinje3 = adresselinje3,
            )

            MottakerType.UTENLANDSK_ADRESSE -> Dto.Mottaker.utenlandskAdresse(
                navn = navn!!,
                postnummer = postnummer,
                poststed = poststed,
                adresselinje1 = adresselinje1!!,
                adresselinje2 = adresselinje2,
                adresselinje3 = adresselinje3,
                landkode = landkode!!
            )
        }

    private fun Document.toDto(): Dto.Document =
        Dto.Document(
            brevredigeringId = brevredigering.id.value,
            dokumentDato = dokumentDato,
            pdf = pdf.bytes,
            redigertBrevHash = redigertBrevHash
        )
}

