package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvAlderspensjonSivilstandDto() =
    EndringAvAlderspensjonSivilstandDto(
        saksbehandlerValg =
            EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg(
                feilutbetaling = false,
                fraFlyttet = false,
                giftBorIkkeSammen = false,
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
                beloepEndring = BeloepEndring.UENDRET,
            ),
    )
