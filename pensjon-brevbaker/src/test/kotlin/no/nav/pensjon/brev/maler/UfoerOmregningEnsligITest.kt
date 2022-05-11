package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
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
                opplysningerBruktIBeregningUT = OpplysningerBruktIBeregningUTDto().copy(
                    barnetilleggGjeldende = BarnetilleggGjeldende().copy(
                        saerkullsbarn = BarnetilleggGjeldende.Saerkullsbarn().copy(
                            erRedusertMotinntekt = true,
                            erEndret = true
                        )
                    ),
                    minsteytelseGjeldende_sats = 10.00,
                    ungUforGjeldende_erUnder20Ar = true,
                ),
                maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(),
                orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterUfoereDto().copy(
                    eps_borSammenMedBrukerGjeldende = true,
                    instutisjon_epsInstitusjonGjeldende = Institusjon.INGEN,
                    barnetilleggVedvirk_innvilgetBarnetillegFellesbarn = false,
                    barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn = false,
                    ektefelletilleggVedvirk_innvilgetEktefelletillegg = true,
                    saktype = Sakstype.ALDER,
                ),
                avdod = UfoerOmregningEnsligDto.Avdod(
                    sivilstand = Sivilstand.PARTNER,
                    navn = "Avdod person",
                    ektefelletilleggOpphoert = false,
                    harFellesBarnUtenBarnetillegg = false,
                ),
                bruker = UfoerOmregningEnsligDto.Bruker(
                    borINorge = true,
                    borIAvtaleLand = false,
                ),
            ),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UT_DOD_ENSLIG_AUTO_BOKMAL", it) }
    }
}
