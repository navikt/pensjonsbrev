package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.OverstyrtMottaker.*
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.ServiceResult.Ok
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
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
    private val penService: PenService,
    private val navansattService: NavansattService,
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
        mottaker: Api.OverstyrtMottaker? = null,
    ): ServiceResult<Api.BrevResponse> =
        harTilgangTilEnhet(call, avsenderEnhetsId) {
            val signerendeSaksbehandler = navansattService.hentNavansatt(call, call.principal().navIdent)?.navn ?: call.principal().fullName

            rendreBrev(
                call = call,
                brevkode = brevkode,
                spraak = spraak,
                saksId = sak.saksId,
                saksbehandlerValg = saksbehandlerValg,
                avsenderEnhetsId = avsenderEnhetsId,
                signaturSignerende = signerendeSaksbehandler,
            ).map { letter ->
                val template = brevbakerService.getRedigerbarTemplate(call, brevkode)

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
                        redigeresAvNavIdent = if (reserverForRedigering) call.principal().navIdent() else null
                        opprettet = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        redigertBrev = letter.toEdit()
                        sistRedigertAvNavIdent = call.principal().navIdent()
                        signaturSignerende = signerendeSaksbehandler
                    }.also {
                        if (mottaker != null) {
                            Mottaker.new(it.id.value) { oppdater(mottaker) }
                        }
                    }.mapBrev(template)
                }.oppdaterMedAnsattNavn(call)
            }
        }

    suspend fun oppdaterBrev(
        call: ApplicationCall,
        saksId: Long?,
        brevId: Long,
        nyeSaksbehandlerValg: BrevbakerBrevdata?,
        nyttRedigertbrev: Edit.Letter?,
    ): ServiceResult<Api.BrevResponse>? =
        hentBrevMedReservasjon(call = call, brevId = brevId, saksId = saksId) { brev ->
            rendreBrev(
                call = call,
                brevkode = brev.brevkode,
                spraak = brev.spraak,
                saksId = brev.saksId,
                saksbehandlerValg = nyeSaksbehandlerValg ?: brev.saksbehandlerValg,
                avsenderEnhetsId = brev.avsenderEnhetId,
                signaturSignerende = brev.signaturSignerende,
            ).map { (nyttRedigertbrev ?: brev.redigertBrev).updateEditedLetter(it) }
                .map { oppdatertBrev ->
                    val template = brevbakerService.getRedigerbarTemplate(call, brev.brevkode)

                    transaction {
                        brev.apply {
                            redigertBrev = oppdatertBrev
                            sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                            saksbehandlerValg = nyeSaksbehandlerValg ?: brev.saksbehandlerValg
                            sistRedigertAvNavIdent = call.principal().navIdent()
                        }.mapBrev(template)
                    }.oppdaterMedAnsattNavn(call)
                }
        }

    suspend fun delvisOppdaterBrev(call: ApplicationCall, saksId: Long, brevId: Long, patch: Api.DelvisOppdaterBrevRequest): Api.BrevResponse? =
        newSuspendedTransaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId)?.apply {
                patch.laastForRedigering?.also { laastForRedigering = it }
                patch.distribusjonstype?.also { distribusjonstype = it }
                if (patch.mottaker != null) {
                    mottaker?.oppdater(patch.mottaker) ?: Mottaker.new(brevId) { oppdater(patch.mottaker) }
                }
            }?.also { Brevredigering.reload(it, true) }?.let {
                it.mapBrev(brevbakerService.getRedigerbarTemplate(call, it.brevkode)).oppdaterMedAnsattNavn(call)
            }
        }

    suspend fun oppdaterSignatur(call: ApplicationCall, brevId: Long, signaturSignerende: String): ServiceResult<Api.BrevResponse>? =
        hentBrevMedReservasjon(call = call, brevId = brevId) { brev ->
            rendreBrev(
                call = call,
                brevkode = brev.brevkode,
                spraak = brev.spraak,
                saksId = brev.saksId,
                saksbehandlerValg = brev.saksbehandlerValg,
                avsenderEnhetsId = brev.avsenderEnhetId,
                signaturSignerende = signaturSignerende,
            ).map { brev.redigertBrev.updateEditedLetter(it) }
                .map {
                    val template = brevbakerService.getRedigerbarTemplate(call, brev.brevkode)

                    transaction {
                        brev.apply {
                            redigertBrev = it
                            this.signaturSignerende = signaturSignerende
                        }.mapBrev(template)
                    }.oppdaterMedAnsattNavn(call)
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

    suspend fun hentBrev(call: ApplicationCall, saksId: Long, brevId: Long, reserverForRedigering: Boolean = false): ServiceResult<Api.BrevResponse>? =
        if (reserverForRedigering) {
            hentBrevMedReservasjon(call = call, brevId = brevId, saksId = saksId) { brev ->
                rendreBrev(
                    call = call,
                    brevkode = brev.brevkode,
                    spraak = brev.spraak,
                    saksId = brev.saksId,
                    saksbehandlerValg = brev.saksbehandlerValg,
                    avsenderEnhetsId = brev.avsenderEnhetId,
                    signaturSignerende = brev.signaturSignerende,
                ).map { brev.redigertBrev.updateEditedLetter(it) }
                    .map {
                        val template = brevbakerService.getRedigerbarTemplate(call, brev.brevkode)
                        transaction { brev.apply { redigertBrev = it }.mapBrev(template) }.oppdaterMedAnsattNavn(call)
                    }
            }
        } else {
            val template = transaction { Brevredigering.findByIdAndSaksId(brevId, saksId)?.brevkode }?.let { brevbakerService.getRedigerbarTemplate(call, it) }
            transaction { Brevredigering.findByIdAndSaksId(brevId, saksId)?.mapBrev(template) }?.let { Ok(it.oppdaterMedAnsattNavn(call)) }
        }

    suspend fun hentBrevForSak(call: ApplicationCall, saksId: Long): List<Api.BrevInfo> =
        newSuspendedTransaction {
            Brevredigering.find { BrevredigeringTable.saksId eq saksId }.map {
                mapBrevInfo(it, brevbakerService.getRedigerbarTemplate(call, it.brevkode))
                    .oppdaterMedAnsattNavn(call )
            }
        }

    suspend fun fornyReservasjon(call: ApplicationCall, brevId: Long): Api.ReservasjonResponse? =
        hentBrevMedReservasjon(call = call, brevId = brevId) { brev ->
            Api.ReservasjonResponse(
                vellykket = true,
                reservertAv = Api.NavAnsatt(id = call.principal().navIdent(), navn = call.principal().fullName),
                timestamp = brev.sistReservert ?: Instant.now(),
                expiresIn = RESERVASJON_TIMEOUT,
                redigertBrevHash = brev.redigertBrevHash,
            )
        }

    private suspend fun <T> hentBrevMedReservasjon(call: ApplicationCall, brevId: Long, saksId: Long? = null, block: suspend (Brevredigering) -> T): T? =
        transaction(Connection.TRANSACTION_REPEATABLE_READ) {
            Brevredigering.findByIdAndSaksId(brevId, saksId)
                ?.apply {
                    if (redigeresAvNavIdent == null || redigeresAvNavIdent == call.principal().navIdent() || erReservasjonUtloept()) {
                        redigeresAvNavIdent = call.principal().navIdent()
                        sistReservert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                    }
                }
        }?.let { brev ->
            if (brev.redigeresAvNavIdent == call.principal().navIdent()) {
                block(brev)
            } else throw KanIkkeReservereBrevredigeringException(
                message = "Brev er allerede reservert av: ${brev.redigeresAvNavIdent}",
                response = Api.ReservasjonResponse(
                    vellykket = false,
                    reservertAv = brev.redigeresAvNavIdent!!.let { Api.NavAnsatt(id = it, navn = navansattService.hentNavansatt(call, it.id)?.navn) },
                    timestamp = brev.sistReservert ?: Instant.now(),
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

    private suspend fun opprettPdf(call: ApplicationCall, brevredigering: Brevredigering, redigertBrevHash: EditLetterHash): ServiceResult<ByteArray> {
        return penService.hentPesysBrevdata(
            call = call,
            saksId = brevredigering.saksId,
            brevkode = brevredigering.brevkode,
            avsenderEnhetsId = brevredigering.avsenderEnhetId
        ).then { pesysData ->
            brevbakerService.renderPdf(
                call = call,
                brevkode = brevredigering.brevkode,
                spraak = brevredigering.spraak,
                brevdata = GeneriskRedigerbarBrevdata(
                    pesysData = pesysData.brevdata,
                    saksbehandlerValg = brevredigering.saksbehandlerValg,
                ),
                felles = pesysData.felles,
                redigertBrev = brevredigering.redigertBrev.toMarkup()
            ).map {
                transaction {
                    val update: Document.() -> Unit = {
                        this.brevredigering = brevredigering
                        pdf = ExposedBlob(it.file)
                        dokumentDato = pesysData.felles.dokumentDato
                        this.redigertBrevHash = redigertBrevHash
                    }
                    Document.findSingleByAndUpdate(DocumentTable.brevredigering eq brevredigering.id, update)?.pdf?.bytes
                        ?: Document.new(update).pdf.bytes
                }
            }
        }
    }

    suspend fun hentEllerOpprettPdf(call: ApplicationCall, saksId: Long, brevId: Long): ServiceResult<ByteArray>? {
        val (brevredigering, document) = transaction { Brevredigering.findByIdAndSaksId(brevId, saksId).let { it to it?.document?.firstOrNull() } }

        return brevredigering?.let {
            val currentHash = brevredigering.redigertBrevHash

            if (document != null && document.redigertBrevHash == currentHash) {
                Ok(document.pdf.bytes)
            } else {
                opprettPdf(call, brevredigering, currentHash)
            }
        }
    }

    suspend fun sendBrev(call: ApplicationCall, saksId: Long, brevId: Long): ServiceResult<Pen.BestillBrevResponse>? {
        val brevkode = transaction { Brevredigering.findByIdAndSaksId(brevId, saksId)?.takeIf { !it.document.empty() }?.brevkode }

        return if (brevkode != null) {
            val template = brevbakerService.getRedigerbarTemplate(call, brevkode)

            if (template == null) {
                ServiceResult.Error(
                    "Mangler TemplateDescription for $brevkode",
                    HttpStatusCode.InternalServerError
                )
            } else {
                val (request, distribusjonstype) = transaction {
                    val brev = Brevredigering.findByIdAndSaksId(brevId, saksId)!!
                    val document = brev.document.firstOrNull()!!

                    Pen.SendRedigerbartBrevRequest(
                        dokumentDato = document.dokumentDato,
                        saksId = brev.saksId,
                        enhetId = brev.avsenderEnhetId,
                        templateDescription = template,
                        brevkode = brev.brevkode,
                        pdf = document.pdf.bytes,
                        eksternReferanseId = "skribenten:${brev.id}",
                        mottaker = brev.mottaker?.toPen(),
                    ) to brev.distribusjonstype
                }
                penService.sendbrev(
                    call,
                    request,
                    distribuer = distribusjonstype == Distribusjonstype.SENTRALPRINT,
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

    private fun Mottaker.oppdater(mottaker: Api.OverstyrtMottaker?) = mottaker.let {
        when (it) {
            is NorskAdresse -> norskAdresse(it.navn, it.postnummer, it.poststed, it.adresselinje1, it.adresselinje2, it.adresselinje3)
            is Samhandler -> samhandler(it.tssId)
            is UtenlandskAdresse -> utenlandskAdresse(
                it.navn,
                it.postnummer,
                it.poststed,
                it.adresselinje1,
                it.adresselinje2,
                it.adresselinje3,
                it.landkode
            )

            null -> delete()
        }
    }

    private fun Brevredigering.mapBrev(template: TemplateDescription?): Api.BrevResponse =
        Api.BrevResponse(
            info = mapBrevInfo(this, template),
            redigertBrev = redigertBrev,
            redigertBrevHash = redigertBrevHash,
            saksbehandlerValg = saksbehandlerValg,
        )

    private fun mapBrevInfo(brev: Brevredigering, template: TemplateDescription?): Api.BrevInfo = with(brev) {
        val redigeresAv = if (erReservasjonUtloept()) null else redigeresAvNavIdent
        Api.BrevInfo(
            id = id.value,
            opprettetAv = Api.NavAnsatt(opprettetAvNavIdent, null),
            opprettet = opprettet,
            sistredigertAv = Api.NavAnsatt(sistRedigertAvNavIdent, null),
            sistredigert = sistredigert,
            brevkode = brevkode,
            brevtittel = template?.metadata?.displayTitle ?: brevkode.name,
            status = when {
                laastForRedigering -> Api.BrevStatus.Klar
                redigeresAv != null -> Api.BrevStatus.UnderRedigering(Api.NavAnsatt(redigeresAv, null))
                else -> Api.BrevStatus.Kladd
            },
            distribusjonstype = distribusjonstype,
            mottaker = mottaker?.toApi(),
        )
    }

    private suspend fun Api.BrevResponse.oppdaterMedAnsattNavn(call: ApplicationCall): Api.BrevResponse =
        copy(info = info.oppdaterMedAnsattNavn(call))

    private suspend fun Api.BrevInfo.oppdaterMedAnsattNavn(call: ApplicationCall): Api.BrevInfo =
        copy(
            opprettetAv = opprettetAv.oppdaterMedNavn(call),
            sistredigertAv = sistredigertAv.oppdaterMedNavn(call),
            status = if (status is Api.BrevStatus.UnderRedigering) {
                Api.BrevStatus.UnderRedigering(status.redigeresAv.oppdaterMedNavn(call))
            } else status
        )
    private suspend fun Api.NavAnsatt.oppdaterMedNavn(call: ApplicationCall): Api.NavAnsatt =
        Api.NavAnsatt(id, navansattService.hentNavansatt(call, id.id)?.navn)

    private fun Brevredigering.erReservasjonUtloept(): Boolean =
        sistReservert?.plus(RESERVASJON_TIMEOUT)?.isBefore(Instant.now()) == true

    fun fjernOverstyrtMottaker(brevId: Long, saksId: Long): Boolean =
        transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId)
                ?.also { it.mottaker?.delete() }
                ?.let { true }
                ?: false
        }

}
