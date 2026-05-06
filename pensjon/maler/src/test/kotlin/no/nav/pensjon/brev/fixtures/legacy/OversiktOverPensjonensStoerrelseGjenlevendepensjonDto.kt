package no.nav.pensjon.brev.fixtures.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.Beregning
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.BeregningPeriode
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.BeregningYtelser
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.EndringAarsak
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.Komponent
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.KomponentValgfri
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.PesysData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createOversiktOverPensjonensStoerrelseGjenlevendepensjonDto() =
    OversiktOverPensjonensStoerrelseGjenlevendepensjonDto(
        pesysData = PesysData(
            virkningFom = LocalDate.of(2026, 1, 1),
            beregningPerioder = listOf(
                BeregningPeriode(
                    virkDatoFom = LocalDate.of(2026, 1, 1),
                    virkDatoTom = LocalDate.of(2026, 4, 30),
                    grunnbeloep = Kroner(124_028),
                    forventetAarligInntekt = Kroner(250_000),
                    aarsaker = listOf(EndringAarsak.UTTAKSGRAD, EndringAarsak.OPPTJENING),
                    ytelser = BeregningYtelser(
                        brutto = Kroner(20_000),
                        netto = Kroner(15_000),
                        grunnpensjon = Komponent(brutto = Kroner(10_500), netto = Kroner(8_000)),
                        tilleggspensjon = KomponentValgfri(innvilget = true, brutto = Kroner(8_000), netto = Kroner(6_000)),
                        saertillegg = KomponentValgfri(innvilget = false, brutto = Kroner(0), netto = Kroner(0)),
                        fasteUtgifter = KomponentValgfri(innvilget = false, brutto = Kroner(0), netto = Kroner(0)),
                        familietillegg = KomponentValgfri(innvilget = true, brutto = Kroner(1_500), netto = Kroner(1_000)),
                    ),
                ),
                BeregningPeriode(
                    virkDatoFom = LocalDate.of(2026, 5, 1),
                    virkDatoTom = LocalDate.of(2026, 12, 31),
                    grunnbeloep = Kroner(126_500),
                    forventetAarligInntekt = Kroner(260_000),
                    aarsaker = listOf(EndringAarsak.FASTE_UTGIFTER_INSTITUSJONSOPPHOLD),
                    ytelser = BeregningYtelser(
                        brutto = Kroner(16_000),
                        netto = Kroner(16_000),
                        grunnpensjon = Komponent(brutto = Kroner(10_700), netto = Kroner(10_700)),
                        tilleggspensjon = KomponentValgfri(innvilget = true, brutto = Kroner(4_300), netto = Kroner(4_300)),
                        saertillegg = KomponentValgfri(innvilget = false, brutto = Kroner(0), netto = Kroner(0)),
                        fasteUtgifter = KomponentValgfri(innvilget = true, brutto = Kroner(1_000), netto = Kroner(1_000)),
                        familietillegg = KomponentValgfri(innvilget = false, brutto = Kroner(0), netto = Kroner(0)),
                    ),
                ),
            ),
            beregning = Beregning(
                virkDatoFom = LocalDate.of(2026, 1, 1),
                grunnbeloep = Kroner(124_028),
                forventetAarligInntekt = Kroner(250_000),
                aarsaker = listOf(EndringAarsak.OPPTJENING, EndringAarsak.UTTAKSGRAD),
                ytelser = BeregningYtelser(
                    brutto = Kroner(20_000),
                    netto = Kroner(15_000),
                    grunnpensjon = Komponent(brutto = Kroner(10_500), netto = Kroner(8_000)),
                    tilleggspensjon = KomponentValgfri(innvilget = true, brutto = Kroner(8_000), netto = Kroner(6_000)),
                    saertillegg = KomponentValgfri(innvilget = true, brutto = Kroner(500), netto = Kroner(400)),
                    fasteUtgifter = KomponentValgfri(innvilget = false, brutto = Kroner(0), netto = Kroner(0)),
                    familietillegg = KomponentValgfri(innvilget = true, brutto = Kroner(1_000), netto = Kroner(600)),
                ),
            ),
        ),
    )

