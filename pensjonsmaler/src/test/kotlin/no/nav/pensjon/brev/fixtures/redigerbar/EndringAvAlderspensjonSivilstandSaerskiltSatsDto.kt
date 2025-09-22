package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvAlderspensjonSivilstandSaerskiltSatsDto() =
    EndringAvAlderspensjonSivilstandSaerskiltSatsDto(
            saksbehandlerValg =
                EndringAvAlderspensjonSivilstandSaerskiltSatsDto.SaksbehandlerValg(
                    aarligKontrollEPS = false,
                    endringPensjon = false,
                    epsAvkallPaaEgenAlderspenspensjon = false,
                    epsAvkallPaaEgenUfoeretrygd = false,
                    epsHarInntektOver1G = true,
                    epsHarRettTilFullAlderspensjon = false,
                    epsIkkeFylt62Aar = false,
                    epsIkkeRettTilFullAlderspensjon = false,
                    epsTarUtAlderspensjon = false,
                    epsTarUtAlderspensjonIStatligSektor = false,
                    epsTarUtUfoeretrygd = false,
                    feilutbetaling = false,
                    beloepEndring = BeloepEndring.UENDRET,
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
                            grunnpensjon = Kroner(320000),
                            totalPensjon = Kroner(340000),
                        ),
                    kravAarsak = KravArsakType.SIVILSTANDSENDRING,
                    kravVirkDatoFom = LocalDate.of(2025, 6, 1),
                    regelverkType = AlderspensjonRegelverkType.AP2011,
                    saerskiltSatsErBrukt = false,
                    sivilstand = MetaforceSivilstand.GIFT,
                    vedtakEtterbetaling = false,
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