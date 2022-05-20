package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
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
                        grunnlag = BarnetilleggGjeldende.Grunnlag().copy(
                            erIkkeUtbetaltpgaTak = true,
                            erRedusertMotTak = true,
                        ),
                        saerkullsbarn = BarnetilleggGjeldende.Saerkullsbarn().copy(
                            avkortningsbelopAr = 1000,
                            belop = 1001,
                            belopAr = 1002,
                            belopArForAvkort = 1003,
                            erRedusertMotinntekt = true,
                            fribelop = 1005,
                            fribelopEllerInntektErPeriodisert = false,
                            inntektBruktIAvkortning = 1007,
                            inntektOverFribelop = 1008,
                            inntektstak = 1009,
                            justeringsbelopAr = 10010,
                        )
                    ),
                    minsteytelseGjeldende_sats = 10.00,
                    ungUforGjeldende_erUnder20Ar = true,
                ),
                maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(),
                minsteytelseVedvirk_sats = null,
                orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterUfoereDto(),
                dineRettigheterOgMulighetTilAaKlage = DineRettigheterOgMulighetTilAaKlageDto(),
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
                ufoeretrygdVedVirk = UfoerOmregningEnsligDto.UfoeretrygdVedVirk(
                    kompensasjonsgrad = 50.5,
                    totalUforeMaanedligBeloep = 100000,
                    erInntektsavkortet = true,
                ),
                inntektFoerUfoerhetVedVirk = UfoerOmregningEnsligDto.InntektFoerUfoerhetVedVirk(
                    oppjustertBeloep = 100000,
                    beloep = 200000,
                    erMinsteinntekt = false,
                    erSannsynligEndret = true
                ),
                barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt = true,
                barnetilleggVedVirk = null,

            ),
            Language.Bokmal,
            Fixtures.fellesAuto,
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("UT_DOD_ENSLIG_AUTO_BOKMAL", it) }
    }
}
