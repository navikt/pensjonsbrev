package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.brevbaker.PDFByggerService
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.typst.TypstCompilerService
import org.slf4j.LoggerFactory

/**
 * PDFByggerService implementation that delegates to either LaTeX or Typst compiler
 * based on Unleash feature toggles.
 *
 * The toggle selection is based on:
 * - `isAutobrev = true` → uses [RendererToggles.typstRendererAutobrev] toggle
 * - `isAutobrev = false` → redigerbar brev → uses [RendererToggles.typstRendererRedigerbar] toggle
 */
class PDFByggerServiceImpl(
    private val latexCompilerService: LaTeXCompilerService,
    private val typstCompilerService: TypstCompilerService,
) : PDFByggerService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun producePDF(pdfRequest: PDFRequest, path: String, shouldRetry: Boolean): PDFCompilationOutput {
        val useTypst = if (shouldRetry) {
            isToggleEnabled(RendererToggles.typstRendererAutobrev)
        } else {
            isToggleEnabled(RendererToggles.typstRendererRedigerbar)
        }

        return if (useTypst) {
            logger.debug("Using Typst renderer for {} brev", if (shouldRetry) "auto" else "redigerbar")
            typstCompilerService.producePDF(pdfRequest, TypstCompilerService.PATH, shouldRetry)
        } else {
            logger.debug("Using LaTeX renderer for {} brev", if (shouldRetry) "auto" else "redigerbar")
            latexCompilerService.producePDF(pdfRequest, path, shouldRetry)
        }
    }

    private fun isToggleEnabled(toggle: FeatureToggle): Boolean =
        FeatureToggleSingleton.isInitialized && FeatureToggleSingleton.isEnabled(toggle)

}

/**
 * Feature toggles for selecting PDF renderer (Typst vs LaTeX).
 */
object RendererToggles {
    val typstRendererRedigerbar = FeatureToggle("typstRendererRedigerbar")
    val typstRendererAutobrev = FeatureToggle("typstRendererAutobrev")

    val all = listOf(typstRendererRedigerbar, typstRendererAutobrev)
}


