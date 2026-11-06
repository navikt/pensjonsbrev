@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.fagsystem

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RendretBrev
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertMarkup
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertVedleggMarkup
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage as MarkupV2WithDataUsage
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataDto
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevmetadataService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient.KravStoettetAvDatabyggerResult
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.RedigerbareVedleggTitler
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import org.slf4j.LoggerFactory

private val ekskluderteBrev = hashSetOf("PE_IY_05_301", "PE_BA_01_108", "PE_GP_01_010", "PE_AP_04_922", "PE_IY_03_169")

class BrevmalService(
    private val brevbakerService: BrevbakerService,
    private val penClient: PenClient,
    private val brevmetadataService: BrevmetadataService,
) {
    private val logger = LoggerFactory.getLogger(BrevmalService::class.java)

    suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        saksbehandlerValg: SaksbehandlervalgMap,
        pesysData: BrevdataResponse.Data
    ): LetterMarkupWithDataUsage =
        brevbakerService.renderMarkup(
            brevkode = brevkode,
            spraak = spraak,
            felles = pesysData.felles,
            fagsystemBrevdata = pesysData.brevdata,
            saksbehandlervalg = saksbehandlerValg.somSaksbehandlervalgIDSL()
        )

    suspend fun renderMarkupV2(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        saksbehandlerValg: SaksbehandlervalgMap,
        pesysData: BrevdataResponse.Data
    ): MarkupV2WithDataUsage =
        brevbakerService.renderMarkupV2(
            brevkode = brevkode,
            spraak = spraak,
            felles = pesysData.felles,
            fagsystemBrevdata = pesysData.brevdata,
            saksbehandlervalg = saksbehandlerValg.somSaksbehandlervalgIDSL()
        )

    suspend fun renderMarkup(
        markupVersjon: MarkupVersjon,
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        saksbehandlerValg: SaksbehandlervalgMap,
        pesysData: BrevdataResponse.Data
    ): RendretBrev = when (markupVersjon) {
        MarkupVersjon.V1 -> renderMarkup(brevkode, spraak, saksbehandlerValg, pesysData)
            .let { RendretBrev(VersjonertMarkup.V1(it.markup), it.letterDataUsage) }

        MarkupVersjon.V2 -> renderMarkupV2(brevkode, spraak, saksbehandlerValg, pesysData)
            .let { rendret ->
                RendretBrev(
                    markup = VersjonertMarkup.V2(rendret.markup),
                    letterDataUsage = rendret.letterDataUsage.map { LetterMarkupWithDataUsageImpl.PropertyImpl(it.typeName, it.propertyName) }.toSet(),
                )
            }
    }

    suspend fun renderMarkup(brev: Brevredigering, pesysData: BrevdataResponse.Data): RendretBrev =
        renderMarkup(
            markupVersjon = brev.markupVersjon,
            brevkode = brev.brevkode,
            spraak = brev.spraak,
            saksbehandlerValg = brev.saksbehandlerValg,
            pesysData = pesysData,
        )

    suspend fun hentRedigerbareVedleggTitler(brev: Brevredigering, pesysData: BrevdataResponse.Data): RedigerbareVedleggTitler? =
        brevbakerService.hentRedigerbareVedleggTitler(
            brevkode = brev.brevkode,
            spraak = brev.spraak,
            fagsystemBrevdata = pesysData.brevdata,
            saksbehandlervalg = brev.saksbehandlerValg.somSaksbehandlervalgIDSL(),
            felles = pesysData.felles,
        )

    suspend fun harRedigerbareVedlegg(brevkode: Brevkode.Redigerbart): Boolean =
        brevbakerService.harRedigerbareVedlegg(brevkode)

    suspend fun renderRedigerbartVedlegg(brev: Brevredigering, pesysData: BrevdataResponse.Data, vedleggId: VedleggId): VersjonertVedleggMarkup? =
        when (brev.markupVersjon) {
            MarkupVersjon.V1 -> brevbakerService.renderRedigerbartVedlegg(
                brevkode = brev.brevkode,
                spraak = brev.spraak,
                fagsystemBrevdata = pesysData.brevdata,
                saksbehandlervalg = brev.saksbehandlerValg.somSaksbehandlervalgIDSL(),
                felles = pesysData.felles,
                vedleggId = vedleggId,
            )?.let { VersjonertVedleggMarkup.V1(it) }

            MarkupVersjon.V2 -> brevbakerService.renderRedigerbartVedleggV2(
                brevkode = brev.brevkode,
                spraak = brev.spraak,
                fagsystemBrevdata = pesysData.brevdata,
                saksbehandlervalg = brev.saksbehandlerValg.somSaksbehandlervalgIDSL(),
                felles = pesysData.felles,
                vedleggId = vedleggId,
            )?.let { VersjonertVedleggMarkup.V2(it) }
        }

    suspend fun renderRedigerteVedlegg(brev: Brevredigering, pesysData: BrevdataResponse.Data): Map<VedleggId, VersjonertVedleggMarkup> =
        brev.redigerteVedlegg.mapNotNull { lagret ->
            renderRedigerbartVedlegg(brev, pesysData, lagret.vedleggId)?.let { lagret.vedleggId to it }
        }.toMap()

    suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart): TemplateDescription.Redigerbar? =
        brevbakerService.getRedigerbarTemplate(brevkode)

    suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): TemplateModelSpecification? =
        brevbakerService.getModelSpecification(brevkode)

    suspend fun getAlltidValgbareVedlegg(brevkode: Brevkode.Redigerbart): Set<AlltidValgbartVedleggBrevkode> =
        brevbakerService.getAlltidValgbareVedlegg(brevkode)

    suspend fun getTemplates(): List<TemplateDescription.Redigerbar>? =
        brevbakerService.getTemplates()

    suspend fun hentBrevmaler(includeEblanketter: Boolean): List<Api.Brevmal> =
        hentAlleMaler(includeEblanketter).toList()

    suspend fun hentBrevmalerForSak(sakType: Sakstype, includeEblanketter: Boolean): List<Api.Brevmal> =
        hentMaler(sakType, includeEblanketter)
            .filter { it.isForSakskontekst }
            .map { it.toApi() }
            .toList()

    suspend fun hentBrevmalerForVedtak(sakstype: Sakstype, includeEblanketter: Boolean, vedtaksId: VedtaksId): List<Api.Brevmal> {
        // Finner hvilke brev som skal filtreres vekk basert på om vi har en brevdatabygger i PEN som sier at den ikke støttes.
        // Denne logikken skal på sikt reverteres slik at PEN gir en liste med brevmaler som støttes for et et gitt vedtak.
        val ikkeStoettedeBrevkoder = brevdataByggerStoettedeVedtak(vedtaksId).kravStoettet.filterValues { !it }.keys

        return hentMaler(sakstype, includeEblanketter)
            .filter { it.isForVedtakskontekst }
            .filter { it.brevkode !in ikkeStoettedeBrevkoder }
            .filterIsRelevantRegelverk(sakstype, vedtaksId)
            .map { it.toApi() }
            .toList()
    }

    // TODO rename vekk fra "krav..." når alle tolkninger er over i spring component data-bygger
    private suspend fun brevdataByggerStoettedeVedtak(vedtaksId: VedtaksId): KravStoettetAvDatabyggerResult =
        penClient.hentIsKravStoettetAvDatabygger(vedtaksId) ?: KravStoettetAvDatabyggerResult()

    private suspend fun Sequence<LetterMetadata>.filterIsRelevantRegelverk(sakstype: Sakstype, vedtaksId: VedtaksId): Sequence<LetterMetadata> {
        val erKravPaaGammeltRegelverk = if (sakstype == Sakstype("ALDER")) {
            penClient.hentIsKravPaaGammeltRegelverk(vedtaksId)
                ?: false.also { logger.warn("Feltet \"erKravPaaGammeltRegelverk\" fra vedtak er null, antar false") }
        } else null

        return filter { it.isRelevantRegelverk(sakstype, erKravPaaGammeltRegelverk) }
    }

    private suspend fun hentMaler(sakstype: Sakstype, includeEblanketter: Boolean): Sequence<LetterMetadata> {
        val visEblanketter = includeEblanketter && Features.eblanketter.isEnabled()
        return withContext(Dispatchers.IO) {
            val brevbaker = async { hentBrevbakerMaler().asSequence().filter { it.sakstyper.map { type -> type.kode }.contains(sakstype.kode) }.map { LetterMetadata.Brevbaker(it) } }
            val legacy = async { brevmetadataService.getBrevmalerForSakstype(sakstype).asSequence().map { LetterMetadata.Legacy(it, sakstype) } }
            val eblanketter = async {
                if (visEblanketter) brevmetadataService.getEblanketter().asSequence().map { LetterMetadata.Legacy(it, sakstype) } else emptySequence()
            }

            val visAutobrev = Features.visAutobrev.isEnabled()

            return@withContext (brevbaker.await() + legacy.await() + eblanketter.await())
                .filter { visAutobrev || it.isRedigerbart }
                .filter { it.brevkode !in ekskluderteBrev }
        }
    }

    private suspend fun hentAlleMaler(includeEblanketter: Boolean): Sequence<Api.Brevmal> {
        val visEblanketter = includeEblanketter && Features.eblanketter.isEnabled()
        val visAutobrev = Features.visAutobrev.isEnabled()

        return withContext(Dispatchers.IO) {
            val brevbaker = async { hentBrevbakerMaler() }
            val legacy = async {
                brevmetadataService.getAllBrev().asSequence()
                    .filter { visEblanketter || it.dokumentkategori != BrevdataDto.DokumentkategoriCode.E_BLANKETT }
            }

            // NB: setter sakstype til GENRL for legacy brev her siden vi ikke har sakstype info når vi henter alle maler,
            //     det blir forkastet før funksjonen returnerer.
            return@withContext brevbaker.await().asSequence().map { LetterMetadata.Brevbaker(it) } +
                    legacy.await().map { LetterMetadata.Legacy(it, Pen.sakstypeForLegacybrev) }
        }.filter { visAutobrev || it.isRedigerbart }
            .filter { it.brevkode !in ekskluderteBrev }
            .map { it.toApi() }
    }

    private suspend fun hentBrevbakerMaler(): List<TemplateDescription.Redigerbar> =
        getTemplates() ?: emptyList()

}