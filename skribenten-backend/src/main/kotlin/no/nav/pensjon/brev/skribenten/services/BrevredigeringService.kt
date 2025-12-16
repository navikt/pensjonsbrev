package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.letter.*
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
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
    class BrevLaastForRedigeringException(message: String) : BrevredigeringException(message)
    class HarIkkeAttestantrolleException(message: String) : BrevredigeringException(message)
    class KanIkkeAttestereEgetBrevException(message: String) : BrevredigeringException(message)
    class KanIkkeAttestereException(message: String) : BrevredigeringException(message)
    class AlleredeAttestertException(message: String) : BrevredigeringException(message)
    class BrevmalFinnesIkke(message: String) : BrevredigeringException(message)
    class VedtaksbrevKreverVedtaksId(message: String) : BrevredigeringException(message)
    class IkkeTilgangTilEnhetException(message: String) : BrevredigeringException(message)
}

interface HentBrevService {
    fun hentBrevForAlleSaker(saksIder: Set<Long>): List<Dto.BrevInfo>
}

class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
    private val penService: PenService,
    private val samhandlerService: SamhandlerService,
    private val p1Service: P1Service,
) : HentBrevService {
    companion object {
        val RESERVASJON_TIMEOUT = 10.minutes.toJavaDuration()
    }

    suspend fun opprettBrev(
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        avsenderEnhetsId: String?,
        saksbehandlerValg: SaksbehandlerValg,
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
        vedtaksId: Long?,
    ): Dto.Brevredigering =
        harTilgangTilEnhet(avsenderEnhetsId) {
            val principal = PrincipalInContext.require()
            val signerendeSaksbehandler = principalSignatur()
            val annenMottakerNavn = mottaker?.fetchNavn()
            val vedtaksIdOmVedtaksbrev = beholdOgKrevVedtaksIdOmVedtaksbrev(vedtaksId, brevkode)

            val rendretBrev = rendreBrev(
                brevkode = brevkode,
                spraak = spraak,
                saksId = sak.saksId,
                vedtaksId = vedtaksIdOmVedtaksbrev,
                saksbehandlerValg = saksbehandlerValg,
                avsenderEnhetsId = avsenderEnhetsId,
                signaturSignerende = signerendeSaksbehandler,
                annenMottakerNavn = annenMottakerNavn
            )
            transaction {
                Brevredigering.new {
                    saksId = sak.saksId
                    this.vedtaksId = vedtaksIdOmVedtaksbrev
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
                    sistRedigertAvNavIdent = principal.navIdent
                    redigertBrev = rendretBrev.markup.toEdit()
                }.also {
                    if (mottaker != null) {
                        Mottaker.new(it.id.value) { oppdater(mottaker) }
                    }
                }.toDto(rendretBrev.letterDataUsage)
            }
        }

    suspend fun oppdaterBrev(
        saksId: Long?,
        brevId: Long,
        nyeSaksbehandlerValg: SaksbehandlerValg?,
        nyttRedigertbrev: Edit.Letter?,
        frigiReservasjon: Boolean = false,
    ): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
            if (!brevDto.info.laastForRedigering || PrincipalInContext.require().isAttestant()) {
                val rendretBrev = rendreBrev(
                    brev = brevDto,
                    saksbehandlerValg = nyeSaksbehandlerValg ?: brevDto.saksbehandlerValg,
                    signaturSignerende = nyttRedigertbrev?.signatur?.saksbehandlerNavn,
                    signaturAttestant = nyttRedigertbrev?.signatur?.attesterendeSaksbehandlerNavn,
                )
                val principal = PrincipalInContext.require()

                transaction {
                    brevDb.apply {
                        redigertBrev =
                            (nyttRedigertbrev ?: brevDto.redigertBrev).updateEditedLetter(rendretBrev.markup)
                        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        saksbehandlerValg = nyeSaksbehandlerValg ?: brevDto.saksbehandlerValg
                        sistRedigertAvNavIdent = principal.navIdent
                        if (frigiReservasjon) {
                            redigeresAvNavIdent = null
                        }
                    }.toDto(rendretBrev.letterDataUsage)
                }
            } else {
                throw BrevLaastForRedigeringException("Kan ikke oppdatere brev markert som 'klar til sending'/'klar til attestering'.")
            }
        }

    suspend fun delvisOppdaterBrev(
        saksId: Long,
        brevId: Long,
        laastForRedigering: Boolean? = null,
        distribusjonstype: Distribusjonstype? = null,
        mottaker: Dto.Mottaker? = null,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>? = null,
    ): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
            // Før brevet kan markeres som `laastForRedigering` (klar til sending) så må det valideres at brevet faktisk er klar til sending.
            if (laastForRedigering == true) {
                brevDto.validerErFerdigRedigert()
            }

            val annenMottakerNavn = mottaker?.fetchNavn()

            transaction {
                if (laastForRedigering == false) {
                    brevDb.laastForRedigering = laastForRedigering
                    brevDb.attestertAvNavIdent = null
                } else if (laastForRedigering == true) {
                    brevDb.laastForRedigering = laastForRedigering
                }
                brevDb.distribusjonstype = distribusjonstype ?: brevDb.distribusjonstype
                if (mottaker != null) {
                    brevDb.mottaker?.oppdater(mottaker) ?: Mottaker.new(brevId) { oppdater(mottaker) }
                    brevDb.oppdaterMedAnnenMottakerNavn(annenMottakerNavn)
                }

                if (alltidValgbareVedlegg != null) {
                    brevDb.valgteVedlegg?.oppdater(alltidValgbareVedlegg) ?: ValgteVedlegg.new(brevId) { oppdater(alltidValgbareVedlegg) }
                }

                brevDb.redigeresAvNavIdent = null

                Brevredigering.reload(brevDb, true)?.toDto(null)
            }
        }

    private fun Brevredigering.oppdaterMedAnnenMottakerNavn(annenMottaker: String?) {
        redigertBrev = redigertBrev.withSakspart(annenMottakerNavn = annenMottaker)
    }

    private suspend fun Dto.Mottaker.fetchNavn(): String? =
        when (type) {
            MottakerType.SAMHANDLER -> tssId?.let { samhandlerService.hentSamhandlerNavn(it) }
            MottakerType.NORSK_ADRESSE, MottakerType.UTENLANDSK_ADRESSE ->
                if (manueltAdressertTil == Dto.Mottaker.ManueltAdressertTil.ANNEN) navn else null
        }

    suspend fun oppdaterSignatur(brevId: Long, signaturSignerende: String): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId) {
            val rendretBrev = rendreBrev(brev = brevDto, signaturSignerende = signaturSignerende)

            transaction {
                brevDb.apply {
                    redigertBrev = brevDto.redigertBrev.updateEditedLetter(rendretBrev.markup)
                }.toDto(rendretBrev.letterDataUsage)
            }
        }

    suspend fun oppdaterSignaturAttestant(brevId: Long, signaturAttestant: String): Dto.Brevredigering? =
        hentBrevMedReservasjon(brevId = brevId) {
            val rendretBrev = rendreBrev(brev = brevDto, signaturAttestant = signaturAttestant)

            transaction {
                brevDb.apply {
                    redigertBrev = brevDto.redigertBrev.updateEditedLetter(rendretBrev.markup)
                }.toDto(rendretBrev.letterDataUsage)
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

    fun hentBrevInfo(brevId: Long): Dto.BrevInfo? =
        transaction { Brevredigering.findById(brevId)?.toBrevInfo() }

    suspend fun hentBrev(
        saksId: Long,
        brevId: Long,
        reserverForRedigering: Boolean = false,
    ): Dto.Brevredigering? =
        if (reserverForRedigering) {
            hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
                val rendretBrev = rendreBrev(brev = brevDto)

                transaction {
                    brevDb.apply {
                        redigertBrev = brevDto.redigertBrev.updateEditedLetter(rendretBrev.markup)
                    }.toDto(rendretBrev.letterDataUsage)
                }
            }
        } else {
            transaction { Brevredigering.findByIdAndSaksId(brevId, saksId)?.toDto(null) }
        }

    suspend fun hentBrevAttestering(
        saksId: Long,
        brevId: Long,
        reserverForRedigering: Boolean = false,
    ): Dto.Brevredigering? =
        if (reserverForRedigering) {
            hentBrevMedReservasjon(brevId = brevId, saksId = saksId) {
                brevDto.validerKanAttestere(PrincipalInContext.require())

                val signaturAttestant =
                    brevDto.redigertBrev.signatur.attesterendeSaksbehandlerNavn ?: principalSignatur()

                val rendretBrev = rendreBrev(brev = brevDto, signaturAttestant = signaturAttestant)

                transaction {
                    brevDb.apply {
                        redigertBrev = brevDto.redigertBrev.updateEditedLetter(rendretBrev.markup)
                    }.toDto(rendretBrev.letterDataUsage)
                }
            }
        } else {
            transaction { Brevredigering.findByIdAndSaksId(brevId, saksId)?.toDto(null) }
        }

    fun hentBrevForSak(saksId: Long): List<Dto.BrevInfo> =
        transaction {
            Brevredigering.find { BrevredigeringTable.saksId eq saksId }
                .map { it.toBrevInfo() }
        }

    override fun hentBrevForAlleSaker(saksIder: Set<Long>): List<Dto.BrevInfo> =
        transaction {
            Brevredigering.find { BrevredigeringTable.saksId inList saksIder }
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
        saksId: Long, brevId: Long
    ): Api.PdfResponse? {
        val (brevredigering, document) = transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId)
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
        saksId: Long,
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
                    sistRedigertAvNavIdent = principal.navIdent
                    this.attestertAvNavIdent = principal.navIdent
                    if (frigiReservasjon) {
                        redigeresAvNavIdent = null
                    }
                }.toDto(rendretBrev.letterDataUsage)
            }
        }

    suspend fun sendBrev(saksId: Long, brevId: Long): Pen.BestillBrevResponse? {
        val (brev, document) = transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId)
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

    private fun validerVedtaksbrevAttestert(brev: Dto.Brevredigering, brevtype: LetterMetadata.Brevtype) {
        if (brevtype == LetterMetadata.Brevtype.VEDTAKSBREV && brev.info.attestertAv == null) {
            throw BrevIkkeKlartTilSendingException("Brev med id ${brev.info.id} er ikke attestert.")
        }
    }

    fun fjernOverstyrtMottaker(brevId: Long, saksId: Long): Boolean =
        transaction {
            Brevredigering.findByIdAndSaksId(brevId, saksId)?.also {
                it.mottaker?.delete()
                it.oppdaterMedAnnenMottakerNavn(null)
            } != null

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
        saksId: Long? = null,
        block: suspend ReservertBrevScope.() -> T
    ): T? {
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
    ) =
        rendreBrev(
            brevkode = brev.info.brevkode,
            spraak = brev.info.spraak,
            saksId = brev.info.saksId,
            vedtaksId = brev.info.vedtaksId,
            saksbehandlerValg = saksbehandlerValg ?: brev.saksbehandlerValg,
            avsenderEnhetsId = brev.info.avsenderEnhetId,
            signaturSignerende = signaturSignerende ?: brev.redigertBrev.signatur.saksbehandlerNavn
            ?: principalSignatur(),
            signaturAttestant = signaturAttestant ?: brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn,
            annenMottakerNavn = annenMottaker ?: brev.redigertBrev.sakspart.annenMottakerNavn,
        )

    private suspend fun rendreBrev(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        saksId: Long,
        vedtaksId: Long?,
        saksbehandlerValg: SaksbehandlerValgBrevdata,
        avsenderEnhetsId: String?,
        signaturSignerende: String,
        signaturAttestant: String? = null,
        annenMottakerNavn: String? = null,
    ): LetterMarkupWithDataUsage {
        val pesysData = penService.hentPesysBrevdata(
            saksId = saksId,
            vedtaksId = vedtaksId,
            brevkode = brevkode,
            avsenderEnhetsId = avsenderEnhetsId,
        )
        return brevbakerService.renderMarkup(
            brevkode = brevkode,
            spraak = spraak,
            brevdata = GeneriskRedigerbarBrevdata(
                pesysData = pesysData.brevdata,
                saksbehandlerValg = saksbehandlerValg,
            ),
            felles = pesysData.felles.medSignerendeSaksbehandlere(
                SignerendeSaksbehandlere(
                    saksbehandler = signaturSignerende,
                    attesterendeSaksbehandler = signaturAttestant
                )
            ).medAnnenMottakerNavn(annenMottakerNavn)
        )
    }

    private suspend fun <T> harTilgangTilEnhet(enhetsId: String?, then: suspend () -> T): T {
        val ident = PrincipalInContext.require().navIdent.id

        return if (enhetsId == null || navansattService.harTilgangTilEnhet(ident, enhetsId)) {
            then()
        } else {
            throw IkkeTilgangTilEnhetException("Mangler tilgang til NavEnhet $enhetsId")
        }
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
                this.brevredigering = Brevredigering[brevredigering.info.id]
                this.pdf = pdf.file
                this.dokumentDato = pesysData.felles.dokumentDato
                this.redigertBrevHash = brevredigering.redigertBrevHash
                this.brevdataHash = brevdataHash
            }
            Document.findSingleByAndUpdate(DocumentTable.brevredigering eq brevredigering.info.id, update)?.pdf
                ?: Document.new(update).pdf
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
            manueltAdressertTil = mottaker.manueltAdressertTil
        } else delete()

    private fun ValgteVedlegg?.oppdater(valgte: List<AlltidValgbartVedleggKode>?) {
        if (this == null) {
            return
        }
        if (valgte == null || valgte.isEmpty()) {
            delete()
        } else {
            valgteVedlegg = valgte
        }
    }

    /**
     * Krever vedtaksId om brevet er vedtaksbrev, men forkaster om ikke.
     */
    private suspend fun beholdOgKrevVedtaksIdOmVedtaksbrev(vedtaksId: Long?, brevkode: Brevkode.Redigerbart): Long? {
        val template = brevbakerService.getRedigerbarTemplate(brevkode)

        return if (template?.metadata?.brevtype == LetterMetadata.Brevtype.VEDTAKSBREV) {
            vedtaksId
                ?: throw VedtaksbrevKreverVedtaksId("Kan ikke opprette brev for vedtaksmal ${brevkode.kode()}: mangler vedtaksId")
        } else {
            null
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
    redigertBrev.klarTilSending() || throw BrevIkkeKlartTilSendingException("Brevet inneholder fritekst-felter som ikke er endret")

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

private class ReservertBrevScope(val brevDb: Brevredigering) {
    val brevDto = brevDb.toDto(null)
}

private fun Brevredigering.toDto(coverage: Set<LetterMarkupWithDataUsage.Property>?): Dto.Brevredigering =
    Dto.Brevredigering(
        info = toBrevInfo(),
        redigertBrev = redigertBrev,
        redigertBrevHash = redigertBrevHash,
        saksbehandlerValg = saksbehandlerValg,
        propertyUsage = coverage,
        valgteVedlegg = valgteVedlegg?.valgteVedlegg
    )

private fun Brevredigering.toBrevInfo(): Dto.BrevInfo =
    Dto.BrevInfo(
        id = id.value,
        saksId = saksId,
        vedtaksId = vedtaksId,
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
        journalpostId = journalpostId,
        attestertAv = attestertAvNavIdent,
        status = when {
            journalpostId != null -> Dto.BrevStatus.ARKIVERT
            laastForRedigering && isVedtaksbrev ->
                if (attestertAvNavIdent != null) {
                    Dto.BrevStatus.KLAR
                } else {
                    Dto.BrevStatus.ATTESTERING
                }

            laastForRedigering -> Dto.BrevStatus.KLAR

            else -> Dto.BrevStatus.KLADD
        }
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
            manueltAdressertTil = manueltAdressertTil,
        )

        MottakerType.UTENLANDSK_ADRESSE -> Dto.Mottaker.utenlandskAdresse(
            navn = navn!!,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            landkode = landkode!!,
            manueltAdressertTil = manueltAdressertTil,
        )
    }

private fun Document.toDto(): Dto.Document =
    Dto.Document(
        brevredigeringId = brevredigering.id.value,
        dokumentDato = dokumentDato,
        pdf = pdf,
        redigertBrevHash = redigertBrevHash,
        brevdataHash = brevdataHash
    )

private fun Brevredigering.erReservasjonUtloept(): Boolean =
    sistReservert?.plus(RESERVASJON_TIMEOUT)?.isBefore(Instant.now()) == true
