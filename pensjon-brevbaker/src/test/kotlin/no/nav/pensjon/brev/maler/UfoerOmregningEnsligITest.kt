package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.Kroner
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
                        grunnlag = BarnetilleggGjeldende.Grunnlag().copy(
                            erIkkeUtbetaltpgaTak = true,
                            erRedusertMotTak = true,
                        ),
                        saerkullsbarn = BarnetilleggGjeldende.Saerkullsbarn().copy(
                            avkortningsbeloepAar = Kroner(1000),
                            beloep = Kroner(1001),
                            beloepAar = Kroner(1002),
                            beloepAarFoerAvkort = Kroner(1003),
                            erRedusertMotinntekt = true,
                            fribeloep = Kroner(1005),
                            fribeloepEllerInntektErPeriodisert = false,
                            inntektBruktIAvkortning = Kroner(1007),
                            inntektOverFribeloep = Kroner(1008),
                            inntektstak = Kroner(1009),
                            justeringsbeloepAar = Kroner(10010),
                        )
                    ),
                    minsteytelseGjeldende_sats = 10.00,
                    ungUfoerGjeldende_erUnder20Aar = true,
                ),
                maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(),
                minsteytelseVedvirk_sats = null,
                orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterUfoereDto(),
                avdoed = UfoerOmregningEnsligDto.Avdoed(
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
                    totalUfoereMaanedligBeloep = Kroner(100000),
                    erInntektsavkortet = true,
                ),
                inntektFoerUfoerhetVedVirk = UfoerOmregningEnsligDto.InntektFoerUfoerhetVedVirk(
                    oppjustertBeloep = Kroner(100000),
                    beloep = Kroner(200000),
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
