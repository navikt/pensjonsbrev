package no.nav.pensjon.brev.skribenten.services

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype
import org.slf4j.LoggerFactory

private val ekskluderteBrev = hashSetOf("PE_IY_05_301", "PE_BA_01_108", "PE_GP_01_010", "PE_AP_04_922", "PE_IY_03_169", "PE_IY_03_171", "PE_IY_03_172", "PE_IY_03_173")

class BrevmalService(
    private val penService: PenService,
    private val brevmetadataService: BrevmetadataService,
    private val brevbakerService: BrevbakerService,
) {
    private val logger = LoggerFactory.getLogger(BrevmalService::class.java)

    suspend fun hentBrevmalerForSak(sakType: Sakstype, includeEblanketter: Boolean): List<Api.Brevmal> =
        hentMaler(sakType, includeEblanketter)
            .filter { it.isForSakskontekst }
            .map { it.toApi() }
            .toList()

    suspend fun hentBrevmalerForVedtak(sakstype: Sakstype, includeEblanketter: Boolean, vedtaksId: String): List<Api.Brevmal> =
        hentMaler(sakstype, includeEblanketter)
            .filter { it.isForVedtakskontekst }
            .filterIsRelevantRegelverk(sakstype, vedtaksId)
            .map { it.toApi() }
            .toList()


    private suspend fun Sequence<LetterMetadata>.filterIsRelevantRegelverk(sakstype: Sakstype, vedtaksId: String): Sequence<LetterMetadata> {
        val erKravPaaGammeltRegelverk = if (sakstype == Sakstype.ALDER) {
            penService.hentIsKravPaaGammeltRegelverk(vedtaksId)
                .catch { message, httpStatusCode ->
                    logger.error("Feil ved henting av felt \"erKravPaaGammeltRegelverk\" fra vedtak. Status: $httpStatusCode, message: $message")
                    false
                }
        } else null

        return filter { it.isRelevantRegelverk(sakstype, erKravPaaGammeltRegelverk) }
    }

    private suspend fun hentMaler(sakstype: Sakstype, includeEblanketter: Boolean): Sequence<LetterMetadata> = coroutineScope {
        val brevbaker = async { hentBrevakerMaler().asSequence().filter { it.sakstyper.contains(sakstype) }.map { LetterMetadata.Brevbaker(it) } }
        val legacy = async { brevmetadataService.getBrevmalerForSakstype(sakstype).asSequence().map { LetterMetadata.Legacy(it, sakstype) } }
        val eblanketter = async {
            if (includeEblanketter) brevmetadataService.getEblanketter().asSequence().map { LetterMetadata.Legacy(it, sakstype) } else emptySequence()
        }

        return@coroutineScope (brevbaker.await() + legacy.await() + eblanketter.await())
            .filter { it.isRedigerbart }
            .filter { it.brevkode !in ekskluderteBrev }
    }

    private suspend fun hentBrevakerMaler(): List<TemplateDescription.Redigerbar> =
        if (Features.brevbakerbrev.isEnabled()) {
            brevbakerService.getTemplates()
                .map { maler ->
                    if (Features.attestant.isEnabled()) maler else maler.filter { it.metadata.brevtype != Brevtype.VEDTAKSBREV }
                }
                .catch { message, statusCode ->
                    logger.error("Kunne ikke hente brevmaler fra brevbaker: $message - $statusCode")
                    emptyList()
                }
        } else emptyList()
}
