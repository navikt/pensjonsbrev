package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.hentSignatur
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.letter.alleFritekstFelterErRedigert
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.sql.Connection
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

data class GeneriskRedigerbarBrevdata(
    override val pesysData: FagsystemBrevdata,
    override val saksbehandlerValg: SaksbehandlerValgBrevdata,
) : RedigerbarBrevdata<SaksbehandlerValgBrevdata, FagsystemBrevdata>

sealed class BrevredigeringException(override val message: String) : Exception() {
    class KanIkkeReservereBrevredigeringException(message: String, val response: Api.ReservasjonResponse) :
        BrevredigeringException(message)

    class ArkivertBrevException(val brevId: BrevId, val journalpostId: JournalpostId) :
        BrevredigeringException("Brev med id $brevId er allerede arkivert i journalpost $journalpostId")

    class BrevIkkeKlartTilSendingException(message: String) : BrevredigeringException(message)
    class NyereVersjonFinsException(message: String) : BrevredigeringException(message)
    class BrevmalFinnesIkke(message: String) : BrevredigeringException(message)
}

interface HentBrevService {
    fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo>
}

class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
    private val penService: PenService,
) : HentBrevService {
    companion object {
        val RESERVASJON_TIMEOUT = 10.minutes.toJavaDuration()
    }

    private val brevreservasjonPolicy = BrevreservasjonPolicy()


    /**
     * Slett brev med id.
     * @return `true` om brevet ble slettet, false om brevet ikke eksisterer,
     */
    fun slettBrev(saksId: SaksId, brevId: BrevId): Boolean {
        return transaction {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
            if (brev != null) {
                brev.delete()
                true
            } else {
                false
            }
        }
    }

    override fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo> =
        transaction {
            BrevredigeringEntity.find { BrevredigeringTable.saksId inList saksIder }
                .map { it.toBrevInfo(brevreservasjonPolicy) }
        }

     suspend fun sendBrev(saksId: SaksId, brevId: BrevId): Pen.BestillBrevResponse? {
        val (brev, document) = transaction {
            BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
                .let { it?.toDto(brevreservasjonPolicy, null) to it?.document }
        }

        return if (brev != null && document != null) {
            if (!brev.info.laastForRedigering) {
                throw BrevIkkeKlartTilSendingException("Brev må være markert som klar til sending")
            }
            brev.validerErFerdigRedigert()
            if (document.redigertBrevHash != brev.redigertBrevHash) {
                throw NyereVersjonFinsException("Det finnes en nyere versjon av brevet enn den som er generert til PDF")
            }

            val template = brevbakerService.getRedigerbarTemplate(brev.info.brevkode)

            if (template == null) {
                throw BrevmalFinnesIkke("Mangler TemplateDescription for ${brev.info.brevkode}")
            } else {
                validerVedtaksbrevAttestert(brev, template.metadata.brevtype)
                penService.sendbrev(
                    sendRedigerbartBrevRequest = Pen.SendRedigerbartBrevRequest(
                        dokumentDato = document.dokumentDato,
                        saksId = brev.info.saksId,
                        enhetId = brev.info.avsenderEnhetId,
                        templateDescription = template,
                        brevkode = brev.info.brevkode,
                        pdf = document.pdf,
                        eksternReferanseId = "skribenten:${brev.info.id.id}",
                        mottaker = brev.info.mottaker?.toPen(),
                    ),
                    distribuer = brev.info.distribusjonstype == Distribusjonstype.SENTRALPRINT,
                ).also {
                    transaction {
                        if (it.journalpostId != null) {
                            if (it.error == null) {
                                BrevredigeringEntity[brevId].delete()
                            } else {
                                BrevredigeringEntity[brevId].journalpostId = it.journalpostId
                            }
                        }
                    }
                }
            }
        } else null
    }

    private fun validerVedtaksbrevAttestert(brev: Dto.Brevredigering, brevtype: LetterMetadata.Brevtype) {
        if (brevtype == LetterMetadata.Brevtype.VEDTAKSBREV && brev.info.attestertAv == null) {
            throw BrevIkkeKlartTilSendingException("Brev med id ${brev.info.id} er ikke attestert.")
        }
    }

    suspend fun tilbakestill(brevId: BrevId): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId) {
            val modelSpec = brevbakerService.getModelSpecification(brevDto.info.brevkode)

            if (modelSpec != null) {
                val tilbakestiltValg = brevDto.saksbehandlerValg.tilbakestill(modelSpec)
                val rendretBrev = rendreBrev(
                    brev = brevDto,
                    saksbehandlerValg = tilbakestiltValg
                )
                transaction {
                    brevDb.apply {
                        saksbehandlerValg = tilbakestiltValg
                        redigertBrev = rendretBrev.markup.toEdit()
                    }.toDto(brevreservasjonPolicy, rendretBrev.letterDataUsage)
                }
            } else {
                throw BrevmalFinnesIkke("Finner ikke brevmal for brevkode ${brevDto.info.brevkode}")
            }
        }

    private suspend fun <T> hentBrevMedReservasjon(
        brevId: BrevId,
        saksId: SaksId? = null,
        block: suspend ReservertBrevScope.() -> T
    ): T? {
        val principal = PrincipalInContext.require()

        return transaction(transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ) {
            BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
                ?.apply {
                    if (redigeresAv == null || redigeresAv == principal.navIdent || erReservasjonUtloept()) {
                        redigeresAv = principal.navIdent
                        sistReservert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                    }
                }?.let { ReservertBrevScope(it) }
        }?.let { reservertBrevScope ->
            val redigeresAv = reservertBrevScope.brevDto.info.redigeresAv

            if (reservertBrevScope.brevDto.info.journalpostId != null) {
                throw ArkivertBrevException(
                    reservertBrevScope.brevDto.info.id,
                    journalpostId = reservertBrevScope.brevDto.info.journalpostId
                )
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
        brev: Dto.Brevredigering,
        saksbehandlerValg: SaksbehandlerValg? = null,
        signaturSignerende: String? = null,
        signaturAttestant: String? = null,
        annenMottaker: String? = null,
    ): LetterMarkupWithDataUsage {
        val pesysData = penService.hentPesysBrevdata(
            saksId = brev.info.saksId,
            vedtaksId = brev.info.vedtaksId,
            brevkode = brev.info.brevkode,
            avsenderEnhetsId = brev.info.avsenderEnhetId,
        )
        return brevbakerService.renderMarkup(
            brevkode = brev.info.brevkode,
            spraak = brev.info.spraak,
            brevdata = GeneriskRedigerbarBrevdata(
                pesysData = pesysData.brevdata,
                saksbehandlerValg = saksbehandlerValg ?: brev.saksbehandlerValg,
            ),
            felles = pesysData.felles.medSignerendeSaksbehandlere(
                SignerendeSaksbehandlere(
                    saksbehandler = signaturSignerende ?: brev.redigertBrev.signatur.saksbehandlerNavn
                        ?: PrincipalInContext.require().hentSignatur(navansattService),
                    attesterendeSaksbehandler = signaturAttestant ?: brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn
                )
            ).medAnnenMottakerNavn(annenMottakerNavn = annenMottaker ?: brev.redigertBrev.sakspart.annenMottakerNavn)
        )
    }

    private inner class ReservertBrevScope(val brevDb: BrevredigeringEntity) {
        val brevDto = brevDb.toDto(brevreservasjonPolicy, null)
    }
}

private fun Dto.Brevredigering.validerErFerdigRedigert(): Boolean =
    redigertBrev.alleFritekstFelterErRedigert() || throw BrevIkkeKlartTilSendingException("Brevet inneholder fritekst-felter som ikke er endret")

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

private fun BrevredigeringEntity.erReservasjonUtloept(): Boolean =
    sistReservert?.plus(RESERVASJON_TIMEOUT)?.isBefore(Instant.now()) == true
