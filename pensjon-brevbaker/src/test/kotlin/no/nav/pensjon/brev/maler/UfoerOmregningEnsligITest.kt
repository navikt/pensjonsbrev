package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class UfoerOmregningEnsligITest {

    @Test
    fun test() {
        Letter(
            UfoerOmregningEnslig.template,
            UfoerOmregningEnsligDto().copy(
                    sivilstand = Sivilstand.PARTNER,
                    bor_i_norge = true,
                    institusjon_gjeldende = Institusjon.INGEN,
                    eps_bor_sammen_med_bruker_gjeldende = true,
                    eps_institusjon_gjeldende = Institusjon.INGEN,
                    har_barnetillegg_felles_barn_vedvirk = false,
                    har_barnetillegg_for_saerkullsbarn_vedvirk = false,
                    har_ektefelletillegg_vedvirk = true,
                    saktype = Sakstype.ALDER,

                        ),
                        Language.Bokmal,
                        Fixtures.fellesAuto,
                        ).render()
                        .let { PdfCompilationInput(it.base64EncodedFiles()) }
                            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
                            .also { writeTestPDF("UT_DOD_ENSLIG_AUTO_BOKMAL", it) }
    }
}
