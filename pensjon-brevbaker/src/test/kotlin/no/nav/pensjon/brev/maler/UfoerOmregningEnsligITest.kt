package no.nav.pensjon.brev.maler

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.vedlegg.OrienteringOmRettigheterParamDto
import no.nav.pensjon.brev.maler.vedlegg.opplysningerBruktIBeregning.BarnetilleggGjeldende
import no.nav.pensjon.brev.maler.vedlegg.opplysningerBruktIBeregning.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg.MaanedligeUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg.UfoeretrygdPerMaaned
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Tag(TestTags.PDF_BYGGER)
class UfoerOmregningEnsligITest {

    @Test
    fun test() {
        val avkortetUfoeretrygdPeriode =
            UfoeretrygdPerMaaned(
                annetBelop = 1500,
                barnetillegg = 2500,
                dekningFasteUtgifter = 1500,
                garantitilleggNordisk27 = 9000,
                grunnbeloep = 91540,
                ordinaerUTBeloep = 15000,
                totalUTBeloep = 5500,
                virkningFraOgMed = LocalDate.of(2017, 2, 3),
                virkningTilOgMed = LocalDate.of(2017, 5, 5),
                avkortning = UfoeretrygdPerMaaned.Avkortning(
                    barnetilleggFoerAvkort = 2800,
                    garantitilleggNordisk27FoerAvkort = 9999,
                    ordinaerUTBeloepFoerAvkort = 12000,
                    totalUTBeloepFoerAvkort = 6600,
                )
            )

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
                maanedligeUfoeretrygdFoerSkattDto = MaanedligeUfoeretrygdFoerSkattDto().copy(
                    ufoeretrygdPerioder = listOf(
                        avkortetUfoeretrygdPeriode,
                        avkortetUfoeretrygdPeriode.copy(avkortning = null)
                    )
                ),
                orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterParamDto().copy(
                    eps_bor_sammen_med_bruker_gjeldende = true,
                    eps_institusjon_gjeldende = Institusjon.INGEN,
                    har_barnetillegg_felles_barn_vedvirk = false,
                    har_barnetillegg_for_saerkullsbarn_vedvirk = false,
                    har_ektefelletillegg_vedvirk = true,
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
