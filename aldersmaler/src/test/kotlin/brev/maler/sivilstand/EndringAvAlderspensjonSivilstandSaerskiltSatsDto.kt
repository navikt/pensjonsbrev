package brev.maler.sivilstand

import brev.maler.vedlegg.createMaanedligPensjonFoerSkatt
import brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.KravArsakType
import no.nav.pensjon.brev.model.alder.MetaforceSivilstand
import no.nav.pensjon.brev.model.alder.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() =
    EndringAvAlderspensjonSivilstandSaerskiltSatsDto(
        saksbehandlerValg =
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto.SaksbehandlerValg(
                eps = EndringAvAlderspensjonSivilstandSaerskiltSatsDto.SaksbehandlerValg.EPS.epsHarInntektOver1G,
                aarligKontrollEPS = false,
                feilutbetaling = false,
                etterbetaling = true
            ),
        pesysData =
            EndringAvAlderspensjonSivilstandSaerskiltSatsDto.PesysData(
                alderspensjonVedVirk =
                    EndringAvAlderspensjonSivilstandSaerskiltSatsDto.AlderspensjonVedVirk(
                        innvilgetFor67 = false,
                        minstenivaaIndividuellInnvilget = true,
                        ufoereKombinertMedAlder = false,
                        uttaksgrad = 100,
                        saertilleggInnvilget = true,
                    ),
                beregnetPensjonPerManedVedVirk =
                    EndringAvAlderspensjonSivilstandSaerskiltSatsDto.BeregnetPensjonPerManedVedVirk(
                        grunnbelop = Kroner(124028),
                        totalPensjon = Kroner(340000),
                    ),
                kravAarsak = KravArsakType.SIVILSTANDSENDRING,
                kravVirkDatoFom = LocalDate.of(2025, 6, 1),
                regelverkType = AlderspensjonRegelverkType.AP2011,
                saerskiltSatsErBrukt = false,
                sivilstand = MetaforceSivilstand.GIFT,
                beloepEndring = BeloepEndring.UENDRET,
                maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
                maanedligPensjonFoerSkattAP2025Dto =
                    MaanedligPensjonFoerSkattAP2025Dto(
                        beregnetPensjonPerManedGjeldende =
                            MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
                                inntektspensjon = Kroner(1000),
                                totalPensjon = Kroner(2000),
                                garantipensjon = Kroner(1000),
                                minstenivaIndividuell = Kroner(1000),
                                virkDatoFom = LocalDate.now(),
                                virkDatoTom = null,
                            ),
                        beregnetPensjonperManed = listOf(),
                        kravVirkFom = LocalDate.now(),
                    ),
                orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            ),
    )