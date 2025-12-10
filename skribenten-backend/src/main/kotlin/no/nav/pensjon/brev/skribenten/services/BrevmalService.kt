package no.nav.pensjon.brev.skribenten.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.LetterMetadata
import no.nav.pensjon.brev.skribenten.services.PenService.KravStoettetAvDatabyggerResult
import org.slf4j.LoggerFactory

private val ekskluderteBrev = hashSetOf("PE_IY_05_301", "PE_BA_01_108", "PE_GP_01_010", "PE_AP_04_922", "PE_IY_03_169")

class BrevmalService(
    private val penService: PenService,
    private val brevmetadataService: BrevmetadataService,
    private val brevbakerService: BrevbakerService,
) {
    private val logger = LoggerFactory.getLogger(BrevmalService::class.java)

    suspend fun hentBrevmaler(includeEblanketter: Boolean): List<Api.Brevmal> =
        hentAlleMaler(includeEblanketter).toList()

    suspend fun hentBrevmalerForSak(sakType: Sakstype, includeEblanketter: Boolean): List<Api.Brevmal> =
        hentMaler(sakType, includeEblanketter)
            .filter { it.isForSakskontekst }
            .map { it.toApi() }
            .toList()

    suspend fun hentBrevmalerForVedtak(sakstype: Sakstype, includeEblanketter: Boolean, vedtaksId: String): List<Api.Brevmal> {
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
    private suspend fun brevdataByggerStoettedeVedtak(vedtaksId: String): KravStoettetAvDatabyggerResult =
        penService.hentIsKravStoettetAvDatabygger(vedtaksId) ?: KravStoettetAvDatabyggerResult()

    private suspend fun Sequence<LetterMetadata>.filterIsRelevantRegelverk(sakstype: Sakstype, vedtaksId: String): Sequence<LetterMetadata> {
        val erKravPaaGammeltRegelverk = if (sakstype == Sakstype.ALDER) {
            penService.hentIsKravPaaGammeltRegelverk(vedtaksId)
                ?: false.also { logger.warn("Feltet \"erKravPaaGammeltRegelverk\" fra vedtak er null, antar false") }
        } else null

        return filter { it.isRelevantRegelverk(sakstype, erKravPaaGammeltRegelverk) }
    }

    private suspend fun hentMaler(sakstype: Sakstype, includeEblanketter: Boolean): Sequence<LetterMetadata> =
        withContext(Dispatchers.IO) {
            val brevbaker = async { hentBrevakerMaler().asSequence().filter { it.sakstyper.contains(sakstype) }.map { LetterMetadata.Brevbaker(it) } }
            val legacy = async { brevmetadataService.getBrevmalerForSakstype(sakstype).asSequence().map { LetterMetadata.Legacy(it, sakstype) } }
            val eblanketter = async {
                if (includeEblanketter) brevmetadataService.getEblanketter().asSequence().map { LetterMetadata.Legacy(it, sakstype) } else emptySequence()
            }

            return@withContext (brevbaker.await() + legacy.await() + eblanketter.await())
                .filter { it.isRedigerbart }
                .filter { it.brevkode !in ekskluderteBrev }
        }

    private suspend fun hentAlleMaler(includeEblanketter: Boolean): Sequence<Api.Brevmal> =
        withContext(Dispatchers.IO) {
            val brevbaker = async { hentBrevakerMaler() }
            val legacy = async {
                brevmetadataService.getAllBrev().asSequence()
                    .filter { includeEblanketter || it.dokumentkategori != BrevdataDto.DokumentkategoriCode.E_BLANKETT }
            }

            // NB: setter sakstype til GENRL for legacy brev her siden vi ikke har sakstype info når vi henter alle maler,
            //     det blir forkastet før funksjonen returnerer.
            return@withContext brevbaker.await().asSequence().map { LetterMetadata.Brevbaker(it) } +
                    legacy.await().map { LetterMetadata.Legacy(it, Sakstype.GENRL) }
        }.filter { it.isRedigerbart }
            .filter { it.brevkode !in ekskluderteBrev }
            .map { it.toApi() }

    private suspend fun hentBrevakerMaler(): List<TemplateDescription.Redigerbar> =
        brevbakerService.getTemplates() ?: emptyList()

}
