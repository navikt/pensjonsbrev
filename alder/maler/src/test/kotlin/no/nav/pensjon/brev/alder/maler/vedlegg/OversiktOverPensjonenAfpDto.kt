package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.Endringsaarsaker
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.Periode
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.PeriodeBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.SistePeriode
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpDto.YtelsesKomponent
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createOversiktOverPensjonenAfpDto() =
    OversiktOverPensjonenAfpDto(
        perioder = listOf(
            Periode(
                virkDatoFom = LocalDate.of(2026, 3, 1),
                virkDatoTom = LocalDate.of(2026, 4, 30),
                grunnbeloep = Kroner(124_028),
                framtidigArligInntekt = Kroner(50_000),
                endringsaarsaker = Endringsaarsaker(
                    endringIVedtak = false,
                    endringIRegelEllerSats = false,
                    endringIFamilieforhold = false,
                    endringIInntekt = false,
                    endringIInstitusjonsopphold = false,
                    endringIFasteUtgifterInstitusjon = false,
                    aldersovergang = false,
                    endringIUttaksgrad = true,
                    endringIOpptjeningsgrunnlag = false,
                ),
                beregning = PeriodeBeregning(
                    brutto = Kroner(20_000),
                    netto = Kroner(18_000),
                    grunnpensjon = YtelsesKomponent(Kroner(12_000), Kroner(11_000)),
                    tilleggspensjon = YtelsesKomponent(Kroner(6_000), Kroner(5_500)),
                    saertillegg = null,
                    minstenivaatilleggIndividuelt = null,
                    afpTillegg = YtelsesKomponent(Kroner(2_000), Kroner(1_500)),
                    ektefelletillegg = null,
                    fasteUtgifterInstitusjon = null,
                    familietillegg = null,
                ),
            ),
            Periode(
                virkDatoFom = LocalDate.of(2026, 5, 1),
                virkDatoTom = LocalDate.of(2026, 6, 30),
                grunnbeloep = Kroner(128_116),
                framtidigArligInntekt = Kroner(50_000),
                endringsaarsaker = Endringsaarsaker(
                    endringIVedtak = false,
                    endringIRegelEllerSats = true,
                    endringIFamilieforhold = false,
                    endringIInntekt = false,
                    endringIInstitusjonsopphold = false,
                    endringIFasteUtgifterInstitusjon = false,
                    aldersovergang = false,
                    endringIUttaksgrad = false,
                    endringIOpptjeningsgrunnlag = false,
                ),
                beregning = PeriodeBeregning(
                    brutto = Kroner(20_500),
                    netto = Kroner(20_500),
                    grunnpensjon = YtelsesKomponent(Kroner(12_300), Kroner(12_300)),
                    tilleggspensjon = YtelsesKomponent(Kroner(6_200), Kroner(6_200)),
                    saertillegg = null,
                    minstenivaatilleggIndividuelt = null,
                    afpTillegg = YtelsesKomponent(Kroner(2_000), Kroner(2_000)),
                    ektefelletillegg = null,
                    fasteUtgifterInstitusjon = null,
                    familietillegg = null,
                ),
            ),
            Periode(
                virkDatoFom = LocalDate.of(2026, 7, 1),
                virkDatoTom = LocalDate.of(2026, 8, 31),
                grunnbeloep = Kroner(128_116),
                framtidigArligInntekt = Kroner(60_000),
                endringsaarsaker = Endringsaarsaker(
                    endringIVedtak = false,
                    endringIRegelEllerSats = false,
                    endringIFamilieforhold = false,
                    endringIInntekt = true,
                    endringIInstitusjonsopphold = false,
                    endringIFasteUtgifterInstitusjon = true,
                    aldersovergang = false,
                    endringIUttaksgrad = false,
                    endringIOpptjeningsgrunnlag = true,
                ),
                beregning = PeriodeBeregning(
                    brutto = Kroner(22_000),
                    netto = Kroner(19_500),
                    grunnpensjon = YtelsesKomponent(Kroner(12_500), Kroner(11_500)),
                    tilleggspensjon = YtelsesKomponent(Kroner(6_500), Kroner(6_000)),
                    saertillegg = YtelsesKomponent(Kroner(500), Kroner(450)),
                    minstenivaatilleggIndividuelt = null,
                    afpTillegg = YtelsesKomponent(Kroner(2_000), Kroner(1_550)),
                    ektefelletillegg = null,
                    fasteUtgifterInstitusjon = YtelsesKomponent(Kroner(500), Kroner(0)),
                    familietillegg = null,
                ),
            ),
        ),
        sistePeriode = SistePeriode(
            virkDatoFom = LocalDate.of(2026, 9, 1),
            grunnbeloep = Kroner(128_116),
            framtidigArligInntekt = Kroner(50_000),
            endringsaarsaker = Endringsaarsaker(
                endringIVedtak = false,
                endringIRegelEllerSats = true,
                endringIFamilieforhold = false,
                endringIInntekt = false,
                endringIInstitusjonsopphold = false,
                endringIFasteUtgifterInstitusjon = false,
                aldersovergang = false,
                endringIUttaksgrad = false,
                endringIOpptjeningsgrunnlag = false,
            ),
            beregning = PeriodeBeregning(
                brutto = Kroner(21_000),
                netto = Kroner(21_000),
                grunnpensjon = YtelsesKomponent(Kroner(12_500), Kroner(12_500)),
                tilleggspensjon = YtelsesKomponent(Kroner(6_500), Kroner(6_500)),
                saertillegg = null,
                minstenivaatilleggIndividuelt = null,
                afpTillegg = YtelsesKomponent(Kroner(2_000), Kroner(2_000)),
                ektefelletillegg = null,
                fasteUtgifterInstitusjon = null,
                familietillegg = null,
            ),
        ),
    )
