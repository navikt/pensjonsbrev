package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brev.skribenten.services.ServiceResult.Ok
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
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
class ArkivertBrevException(val brevId: Long, val journalpostId: Long) :
    RuntimeException("Brev med id $brevId er allerede arkivert i journalpost $journalpostId")

class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
    private val penService: PenService,
) {
    companion object {
        val RESERVASJON_TIMEOUT = 10.minutes.toJavaDuration()
    }

    suspend fun opprettBrev(
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        avsenderEnhetsId: String?,
        saksbehandlerValg: SaksbehandlerValg,
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
    ): ServiceResult<Dto.Brevredigering> =
        harTilgangTilEnhet(avsenderEnhetsId) {
            val principal = PrincipalInContext.require()
            val signerendeSaksbehandler = navansattService.hentNavansatt(principal.navIdent.id)
                ?.let { "${it.fornavn} ${it.etternavn}" }
                ?: principal.fullName

            rendreBrev(
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
                        opprettetAvNavIdent = principal.navIdent
                        this.brevkode = brevkode
                        this.spraak = spraak
                        this.avsenderEnhetId = avsenderEnhetsId
                        this.saksbehandlerValg = saksbehandlerValg
                        laastForRedigering = false
                        distribusjonstype = Distribusjonstype.SENTRALPRINT
                        redigeresAvNavIdent = principal.navIdent.takeIf { reserverForRedigering }
                        sistReservert = Instant.now().truncatedTo(ChronoUnit.MILLIS).takeIf { reserverForRedigering }
                        opprettet = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        redigertBrev = letter.toEdit()
                        sistRedigertAvNavIdent = principal.navIdent
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
        saksId: Long?,
        brevId: Long,
        nyeSaksbehandlerValg: SaksbehandlerValg?,
        nyttRedigertbrev: Edit.Letter?,
        frigiReservasjon: Boolean = false,
    ): ServiceResult<Dto.Brevredigering>? =
        hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
            rendreBrev(
                brevkode = brevDto.info.brevkode,
                spraak = brevDto.info.spraak,
                saksId = brevDto.info.saksId,
                saksbehandlerValg = nyeSaksbehandlerValg ?: brevDto.saksbehandlerValg,
                avsenderEnhetsId = brevDto.info.avsenderEnhetId,
                signaturSignerende = brevDto.info.signaturSignerende,
            ).map { rendretBrev ->
                val principal = PrincipalInContext.require()
                transaction {
                    brevDb.apply {
                        redigertBrev = (nyttRedigertbrev ?: brevDto.redigertBrev).updateEditedLetter(rendretBrev)
                        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        saksbehandlerValg = nyeSaksbehandlerValg ?: brevDto.saksbehandlerValg
                        sistRedigertAvNavIdent = principal.navIdent
                        if (frigiReservasjon) {
                            redigeresAvNavIdent = null
                        }
                    }.toDto()
                }
            }
        }

    suspend fun delvisOppdaterBrev(
        saksId: Long,
        brevId: Long,
        laastForRedigering: Boolean? = null,
        distribusjonstype: Distribusjonstype? = null,
        mottaker: Dto.Mottaker? = null,
    ): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
            transaction {
                brevDb.apply {
                    laastForRedigering?.also { this.laastForRedigering = it }
                    distribusjonstype?.also { this.distribusjonstype = it }
                    mottaker?.also { this.mottaker?.oppdater(mottaker) ?: Mottaker.new(brevId) { oppdater(mottaker) } }
                }.also { Brevredigering.reload(it, true) }.toDto()
            }
        }

    suspend fun oppdaterSignatur(brevId: Long, signaturSignerende: String): ServiceResult<Dto.Brevredigering>? =
        hentBrevMedReservasjon(brevId = brevId) {
            rendreBrev(
                brevkode = brevDto.info.brevkode,
                spraak = brevDto.info.spraak,
                saksId = brevDto.info.saksId,
                saksbehandlerValg = brevDto.saksbehandlerValg,
                avsenderEnhetsId = brevDto.info.avsenderEnhetId,
                signaturSignerende = signaturSignerende,
            ).map { rendretBrev ->
                transaction {
                    brevDb.apply {
                        redigertBrev = brevDto.redigertBrev.updateEditedLetter(rendretBrev)
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

    suspend fun hentBrev(saksId: Long, brevId: Long, reserverForRedigering: Boolean = false): ServiceResult<Dto.Brevredigering>? =
        if (reserverForRedigering) {
            hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
                rendreBrev(
                    brevkode = brevDto.info.brevkode,
                    spraak = brevDto.info.spraak,
                    saksId = brevDto.info.saksId,
                    saksbehandlerValg = brevDto.saksbehandlerValg,
                    avsenderEnhetsId = brevDto.info.avsenderEnhetId,
                    signaturSignerende = brevDto.info.signaturSignerende,
                ).map { rendretBrev ->
                    transaction {
                        brevDb.apply { redigertBrev = brevDto.redigertBrev.updateEditedLetter(rendretBrev) }.toDto()
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

    suspend fun fornyReservasjon(brevId: Long): Api.ReservasjonResponse? =
        hentBrevMedReservasjon(brevId = brevId) {
            val principal = PrincipalInContext.require()

            Api.ReservasjonResponse(
                vellykket = true,
                reservertAv = Api.NavAnsatt(id = principal.navIdent, navn = principal.fullName),
                timestamp = brevDto.info.sistReservert ?: Instant.now(),
                expiresIn = RESERVASJON_TIMEOUT,
                redigertBrevHash = brevDto.redigertBrevHash,
            )
        }

    suspend fun hentEllerOpprettPdf(saksId: Long, brevId: Long): ServiceResult<ByteArray>? {
        val (brevredigering, document) = transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId).let { it?.toDto() to it?.document?.firstOrNull()?.toDto() }
        }

        return brevredigering?.let {
            val currentHash = brevredigering.redigertBrevHash

            if (document != null && document.redigertBrevHash == currentHash) {
                Ok(document.pdf)
            } else {
                opprettPdf(brevredigering, currentHash)
            }
        }
    }

    suspend fun sendBrev(saksId: Long, brevId: Long): ServiceResult<Pen.BestillBrevResponse>? {
        val (brev, document) = transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId).let { it?.toDto() to it?.document?.firstOrNull()?.toDto() }
        }

        return if (brev != null && document != null) {
            val template = brevbakerService.getRedigerbarTemplate(brev.info.brevkode)

            if (template == null) {
                ServiceResult.Error(
                    "Mangler TemplateDescription for ${brev.info.brevkode}",
                    HttpStatusCode.InternalServerError
                )
            } else {
                penService.sendbrev(
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
                    transaction {
                        if (it.journalpostId != null) {
                            if (it.error == null) {
                                Brevredigering[brevId].delete()
                            } else {
                                Brevredigering[brevId].journalpostId = it.journalpostId
                            }
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

    suspend fun tilbakestill(brevId: Long): ServiceResult<Dto.Brevredigering>? =
        hentBrevMedReservasjon(brevId = brevId) {
            brevbakerService.getModelSpecification(brevDto.info.brevkode)
                .map { brevDto.saksbehandlerValg.tilbakestill(it) }
                .then { tilbakestiltValg ->
                    rendreBrev(
                        brevkode = brevDto.info.brevkode,
                        spraak = brevDto.info.spraak,
                        saksId = brevDto.info.saksId,
                        saksbehandlerValg = tilbakestiltValg,
                        avsenderEnhetsId = brevDto.info.avsenderEnhetId,
                        signaturSignerende = brevDto.info.signaturSignerende,
                    ).map { rendretBrev ->
                        transaction {
                            brevDb.apply {
                                saksbehandlerValg = tilbakestiltValg
                                redigertBrev = rendretBrev.toEdit()
                            }.toDto()
                        }
                    }
                }
        }

    private suspend fun <T> hentBrevMedReservasjon(brevId: Long, saksId: Long? = null, block: suspend ReservertBrevScope.() -> T): T? {
        val principal = PrincipalInContext.require()

        return transaction(Connection.TRANSACTION_REPEATABLE_READ) {
            Brevredigering.findByIdAndSaksId(brevId, saksId)
                ?.apply {
                    if (redigeresAvNavIdent == null || redigeresAvNavIdent == principal.navIdent || erReservasjonUtloept()) {
                        redigeresAvNavIdent = principal.navIdent
                        sistReservert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                    }
                }?.let { ReservertBrevScope(it) }
        }?.let { reservertBrevScope ->
            val redigeresAv = reservertBrevScope.brevDto.info.redigeresAv

            if (reservertBrevScope.brevDto.info.journalpostId != null) {
                throw ArkivertBrevException(reservertBrevScope.brevDto.info.id, journalpostId = reservertBrevScope.brevDto.info.journalpostId)
            } else if (redigeresAv == principal.navIdent) {
                reservertBrevScope.block()
            } else throw KanIkkeReservereBrevredigeringException(
                message = "Brev er allerede reservert av: ${reservertBrevScope.brevDto.info.redigeresAv}",
                response = Api.ReservasjonResponse(
                    vellykket = false,
                    reservertAv = redigeresAv?.let {
                        Api.NavAnsatt(
                            id = redigeresAv,
                            navn = navansattService.hentNavansatt(redigeresAv.id)?.navn
                        )
                    } ?: Api.NavAnsatt(id = NavIdent("INGEN"), "INGEN"),
                    timestamp = reservertBrevScope.brevDto.info.sistReservert ?: Instant.now(),
                    expiresIn = RESERVASJON_TIMEOUT,
                    redigertBrevHash = reservertBrevScope.brevDto.redigertBrevHash,
                )
            )
        }
    }

    private suspend fun rendreBrev(
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        saksId: Long,
        saksbehandlerValg: BrevbakerBrevdata,
        avsenderEnhetsId: String?,
        signaturSignerende: String,
        attesterendeSaksbehandler: NavIdent? = null,
    ): ServiceResult<LetterMarkup> = coroutineScope {
        val signaturAttesterende = async { attesterendeSaksbehandler?.let { navansattService.hentNavansatt(it.id) } }

        penService.hentPesysBrevdata(saksId = saksId, brevkode = brevkode, avsenderEnhetsId = avsenderEnhetsId)
            .then { pesysData ->
                brevbakerService.renderMarkup(
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

    private suspend fun <T> harTilgangTilEnhet(enhetsId: String?, then: suspend () -> ServiceResult<T>): ServiceResult<T> {
        return if (enhetsId == null) {
            then()
        } else {
            navansattService.harTilgangTilEnhet(PrincipalInContext.require().navIdent.id, enhetsId)
                .then { harTilgang ->
                    if (harTilgang) {
                        then()
                    } else ServiceResult.Error("Mangler tilgang til NavEnhet $enhetsId", HttpStatusCode.Forbidden)
                }
        }
    }

    private suspend fun opprettPdf(brevredigering: Dto.Brevredigering, redigertBrevHash: EditLetterHash): ServiceResult<ByteArray> {
        return penService.hentPesysBrevdata(
            saksId = brevredigering.info.saksId,
            brevkode = brevredigering.info.brevkode,
            avsenderEnhetsId = brevredigering.info.avsenderEnhetId
        ).then { pesysData ->
            brevbakerService.renderPdf(
                brevkode = brevredigering.info.brevkode,
                spraak = brevredigering.info.spraak,
                brevdata = GeneriskRedigerbarBrevdata(
                    pesysData = pesysData.brevdata,
                    saksbehandlerValg = brevredigering.saksbehandlerValg,
                ),
                felles = pesysData.felles.copy(signerendeSaksbehandlere = SignerendeSaksbehandlere(brevredigering.info.signaturSignerende)),
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

}

private fun SaksbehandlerValg.tilbakestill(modelSpec: TemplateModelSpecification): SaksbehandlerValg {
    val saksbehandlerValgSpec = modelSpec.types[modelSpec.letterModelTypeName]?.get("saksbehandlerValg")
        ?.let { if (it is TemplateModelSpecification.FieldType.Object) it.typeName else null }
        ?.let { modelSpec.types[it] }

    return if (saksbehandlerValgSpec != null) {
        SaksbehandlerValg().apply {
            putAll(this@tilbakestill)
            saksbehandlerValgSpec.entries.forEach {
                val fieldType = it.value
                if (fieldType.nullable) {
                    put(it.key, null)
                } else if (fieldType is TemplateModelSpecification.FieldType.Scalar && fieldType.kind == TemplateModelSpecification.FieldType.Scalar.Kind.BOOLEAN) {
                    put(it.key, false)
                }
            }
        }
    } else this
}

private class ReservertBrevScope(val brevDb: Brevredigering) {
    val brevDto = brevDb.toDto()
}

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
        journalpostId = journalpostId,
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

private fun Brevredigering.erReservasjonUtloept(): Boolean =
    sistReservert?.plus(RESERVASJON_TIMEOUT)?.isBefore(Instant.now()) == true