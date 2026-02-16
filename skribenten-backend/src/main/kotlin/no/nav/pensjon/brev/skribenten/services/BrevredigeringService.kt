package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.letter.*
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import org.jetbrains.exposed.v1.core.eq
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

    class ArkivertBrevException(val brevId: Long, val journalpostId: Long) :
        BrevredigeringException("Brev med id $brevId er allerede arkivert i journalpost $journalpostId")

    class BrevIkkeKlartTilSendingException(message: String) : BrevredigeringException(message)
    class NyereVersjonFinsException(message: String) : BrevredigeringException(message)
    class HarIkkeAttestantrolleException(message: String) : BrevredigeringException(message)
    class KanIkkeAttestereEgetBrevException(message: String) : BrevredigeringException(message)
    class AlleredeAttestertException(message: String) : BrevredigeringException(message)
    class BrevmalFinnesIkke(message: String) : BrevredigeringException(message)
    class IkkeTilgangTilEnhetException(message: String) : BrevredigeringException(message)
}

interface HentBrevService {
    fun hentBrevForAlleSaker(saksIder: Set<SaksId>): List<Dto.BrevInfo>
}

class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
    private val penService: PenService,
    private val p1Service: P1Service,
) : HentBrevService {
    companion object {
        val RESERVASJON_TIMEOUT = 10.minutes.toJavaDuration()
    }

    suspend fun delvisOppdaterBrev(
        saksId: SaksId,
        brevId: Long,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>? = null,
    ): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
            transaction {
                if (alltidValgbareVedlegg != null) {
                    if (brevDb.valgteVedlegg?.valgteVedlegg != alltidValgbareVedlegg) {
                        brevDb.document.singleOrNull()?.delete()
                    }
                    brevDb.valgteVedlegg?.oppdater(alltidValgbareVedlegg) ?: (ValgteVedlegg.new(brevId) { oppdater(alltidValgbareVedlegg) })
                }

                brevDb.redigeresAv = null

                BrevredigeringEntity.reload(brevDb, true)?.toDto(null)
            }
        }

    /**
     * Slett brev med id.
     * @return `true` om brevet ble slettet, false om brevet ikke eksisterer,
     */
    fun slettBrev(saksId: SaksId, brevId: Long): Boolean {
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

    suspend fun hentEllerOpprettPdf(
        saksId: SaksId, brevId: Long
    ): Api.PdfResponse? {
        val (brevredigering, document) = transaction {
            BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
                .let { it?.toDto(null) to it?.document?.firstOrNull()?.toDto() }
        }
        return brevredigering?.let {
            val pesysBrevdata = penService.hentPesysBrevdata(
                saksId = brevredigering.info.saksId,
                vedtaksId = brevredigering.info.vedtaksId,
                brevkode = brevredigering.info.brevkode,
                avsenderEnhetsId = brevredigering.info.avsenderEnhetId
            ).withP1DataIfP1(brevredigering.info, p1Service)

            val nyBrevdataHash = Hash.read(pesysBrevdata)

            // dokumentDato er en del av pesysBrevdata.felles, så vi trenger ikke sjekke den eksplisitt her
            if (document != null && document.redigertBrevHash == brevredigering.redigertBrevHash && nyBrevdataHash == document.brevdataHash) {
                Api.PdfResponse(pdf = document.pdf, rendretBrevErEndret = false)
            } else {
                // render markup to check if letterMarkup has changed due to changed brevdata
                val rendretBrevErEndret = brevbakerService.renderMarkup(
                    brevkode = brevredigering.info.brevkode,
                    spraak = brevredigering.info.spraak,
                    brevdata = GeneriskRedigerbarBrevdata(
                        pesysData = pesysBrevdata.brevdata,
                        saksbehandlerValg = brevredigering.saksbehandlerValg,
                    ),
                    felles = pesysBrevdata.felles
                        .medSignerendeSaksbehandlere(brevredigering.redigertBrev.signatur)
                        .medAnnenMottakerNavn(brevredigering.redigertBrev.sakspart.annenMottakerNavn)
                ).let {
                    // sjekker kun blocks her fordi det er eneste situasjonen hvor vi ønsker å informere bruker om å se over endringer
                    brevredigering.redigertBrev.updateEditedLetter(it.markup).blocks != brevredigering.redigertBrev.blocks
                }

                Api.PdfResponse(
                    pdf = opprettPdf(brevredigering, pesysBrevdata, nyBrevdataHash),
                    rendretBrevErEndret = rendretBrevErEndret
                )
            }
        }
    }

    suspend fun attester(
        saksId: SaksId,
        brevId: Long,
        nyeSaksbehandlerValg: SaksbehandlerValg?,
        nyttRedigertbrev: Edit.Letter?,
        frigiReservasjon: Boolean = false,
    ): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
            val principal = PrincipalInContext.require()
            brevDto.validerErFerdigRedigert()
            brevDto.validerKanAttestere(principal)

            // TODO: burde vi sjekke om brevet er et vedtaksbrev før vi gjennomfører attestering?

            val signaturAttestant = brevDto.redigertBrev.signatur.attesterendeSaksbehandlerNavn ?: principalSignatur()

            val rendretBrev = rendreBrev(
                brev = brevDto,
                saksbehandlerValg = nyeSaksbehandlerValg,
                signaturAttestant = signaturAttestant,
            )
            transaction {
                brevDb.apply {
                    redigertBrev = (nyttRedigertbrev ?: brevDto.redigertBrev).updateEditedLetter(rendretBrev.markup)
                    sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                    saksbehandlerValg = nyeSaksbehandlerValg ?: brevDto.saksbehandlerValg
                    sistRedigertAv = principal.navIdent
                    this.attestertAvNavIdent = principal.navIdent
                    if (frigiReservasjon) {
                        redigeresAv = null
                    }
                }.toDto(rendretBrev.letterDataUsage)
            }
        }

    suspend fun sendBrev(saksId: SaksId, brevId: Long): Pen.BestillBrevResponse? {
        val (brev, document) = transaction {
            BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
                .let { it?.toDto(null) to it?.document?.firstOrNull()?.toDto() }
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
                        eksternReferanseId = "skribenten:${brev.info.id}",
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

    suspend fun tilbakestill(brevId: Long): Dto.Brevredigering? =
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
                    }.toDto(rendretBrev.letterDataUsage)
                }
            } else {
                throw BrevmalFinnesIkke("Finner ikke brevmal for brevkode ${brevDto.info.brevkode}")
            }
        }

    private suspend fun <T> hentBrevMedReservasjon(
        brevId: Long,
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
                    ?: principalSignatur(),
                    attesterendeSaksbehandler = signaturAttestant ?: brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn
                )
            ).medAnnenMottakerNavn(annenMottakerNavn = annenMottaker ?: brev.redigertBrev.sakspart.annenMottakerNavn)
        )
    }

    private suspend fun opprettPdf(
        brevredigering: Dto.Brevredigering,
        pesysData: BrevdataResponse.Data,
        brevdataHash: Hash<BrevdataResponse.Data>,
    ): ByteArray {
        val pdf = brevbakerService.renderPdf(
            brevkode = brevredigering.info.brevkode,
            spraak = brevredigering.info.spraak,
            brevdata = GeneriskRedigerbarBrevdata(
                pesysData = pesysData.brevdata,
                saksbehandlerValg = brevredigering.saksbehandlerValg,
            ),
            // Brevbaker bruker signaturer fra redigertBrev, men felles er nødvendig fordi den kan brukes i vedlegg.
            felles = pesysData.felles
                .medAnnenMottakerNavn(brevredigering.redigertBrev.sakspart.annenMottakerNavn)
                .medSignerendeSaksbehandlere(brevredigering.redigertBrev.signatur),
            redigertBrev = brevredigering.redigertBrev.withSakspart(dokumentDato = pesysData.felles.dokumentDato)
                .toMarkup(),
            alltidValgbareVedlegg = brevredigering.valgteVedlegg ?: emptyList(),
        )

        return transaction {
            val update: Document.() -> Unit = {
                this.brevredigering = BrevredigeringEntity[brevredigering.info.id]
                this.pdf = pdf.file
                this.dokumentDato = pesysData.felles.dokumentDato
                this.redigertBrevHash = brevredigering.redigertBrevHash
                this.brevdataHash = brevdataHash
            }
            Document.findSingleByAndUpdate(DocumentTable.brevredigering eq brevredigering.info.id, update)?.pdf
                ?: Document.new(update).pdf
        }
    }

    private fun ValgteVedlegg?.oppdater(valgte: List<AlltidValgbartVedleggKode>?) {
        if (this == null) {
            return
        }
        if (valgte.isNullOrEmpty()) {
            delete()
        } else {
            valgteVedlegg = valgte
        }
    }

    private suspend fun principalSignatur(): String =
        PrincipalInContext.require().let { principal ->
            navansattService.hentNavansatt(principal.navIdent.id)?.let { "${it.fornavn} ${it.etternavn}" }
                ?: principal.fullName
        }
}

private suspend fun BrevdataResponse.Data.withP1DataIfP1(
    brevinfo: Dto.BrevInfo,
    p1Service: P1Service
): BrevdataResponse.Data =
    p1Service.patchMedP1DataOmP1(
        this,
        brevkode = brevinfo.brevkode,
        brevId = brevinfo.id,
        saksId = brevinfo.saksId
    )


private fun Felles.medSignerendeSaksbehandlere(signatur: LetterMarkup.Signatur): Felles =
    signatur.saksbehandlerNavn?.let {
        medSignerendeSaksbehandlere(
            SignerendeSaksbehandlere(
                saksbehandler = it,
                attesterendeSaksbehandler = signatur.attesterendeSaksbehandlerNavn
            )
        )
    } ?: this

private fun Dto.Brevredigering.validerErFerdigRedigert(): Boolean =
    redigertBrev.alleFritekstFelterErRedigert() || throw BrevIkkeKlartTilSendingException("Brevet inneholder fritekst-felter som ikke er endret")

private fun Dto.Brevredigering.validerKanAttestere(userPrincipal: UserPrincipal) {
    if (!userPrincipal.isAttestant()) {
        throw HarIkkeAttestantrolleException(
            "Bruker ${userPrincipal.navIdent} har ikke attestantrolle, brev ${info.id}",
        )
    }
    if (userPrincipal.navIdent == info.opprettetAv) {
        throw KanIkkeAttestereEgetBrevException(
            "Bruker ${userPrincipal.navIdent} prøver å attestere sitt eget brev, brev ${info.id}",
        )
    }
    if (info.attestertAv != null && info.attestertAv != userPrincipal.navIdent) {
        throw AlleredeAttestertException("Brev ${info.id} er allerede attestert av ${info.attestertAv}")
    }
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

private class ReservertBrevScope(val brevDb: BrevredigeringEntity) {
    val brevDto = brevDb.toDto(null)
}


private fun Document.toDto(): Dto.Document =
    Dto.Document(
        brevredigeringId = brevredigering.id.value,
        dokumentDato = dokumentDato,
        pdf = pdf,
        redigertBrevHash = redigertBrevHash,
        brevdataHash = brevdataHash
    )

private fun BrevredigeringEntity.erReservasjonUtloept(): Boolean =
    sistReservert?.plus(RESERVASJON_TIMEOUT)?.isBefore(Instant.now()) == true
