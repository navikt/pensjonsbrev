package brev.maler.sivilstand

import brev.maler.vedlegg.createMaanedligPensjonFoerSkatt
import brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.KravArsakType
import no.nav.pensjon.brev.model.alder.MetaforceSivilstand
import no.nav.pensjon.brev.model.alder.sivilstand.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvAlderspensjonSivilstandDto() =
    EndringAvAlderspensjonSivilstandDto(
        saksbehandlerValg =
            EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg(
                feilutbetaling = false,
                sivilstandsendringsaarsak = EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg.Sivilstandsendringsaarsak.annet,
                etterbetaling = true
            ),
        pesysData =
            EndringAvAlderspensjonSivilstandDto.PesysData(
                alderspensjonVedVirk =
                    EndringAvAlderspensjonSivilstandDto.AlderspensjonVedVirk(
                        garantipensjonInnvilget = false,
                        innvilgetFor67 = false,
                        minstenivaaIndividuellInnvilget = true,
                        minstenivaaPensjonsistParInnvilget = false,
                        pensjonstilleggInnvilget = false,
                        saertilleggInnvilget = false,
                        ufoereKombinertMedAlder = false,
                        uttaksgrad = 100,
                    ),
                beregnetPensjonPerManedVedVirk =
                    EndringAvAlderspensjonSivilstandDto.BeregnetPensjonPerManedVedVirk(
                        grunnpensjon = Kroner(320000),
                        totalPensjon = Kroner(340000),
                    ),
                epsVedVirk =
                    EndringAvAlderspensjonSivilstandDto.EpsVedVirk(
                        borSammenMedBruker = true,
                        harInntektOver2G = false,
                        mottarOmstillingsstonad = false,
                        mottarPensjon = false,
                    ),
                kravAarsak = KravArsakType.SIVILSTANDSENDRING,
                kravVirkDatoFom = LocalDate.of(2025, 6, 1),
                regelverkType = AlderspensjonRegelverkType.AP2011,
                sivilstand = MetaforceSivilstand.GIFT,
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
                beloepEndring = BeloepEndring.UENDRET,
            ),
    )
