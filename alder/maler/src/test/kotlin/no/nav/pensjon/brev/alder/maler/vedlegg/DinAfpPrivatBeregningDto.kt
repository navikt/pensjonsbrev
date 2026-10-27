package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.vedlegg.DinAfpPrivatBeregningDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createDinAfpPrivatBeregningDto() =
    DinAfpPrivatBeregningDto(
        bosattINorge = true,
        totalPensjon = Kroner(50000),
        livsvarigBrutto = Kroner(44300),
        kronetilleggBrutto = Kroner(2411),
        kompensasjonstilleggBrutto = Kroner(5509),
        opptjening = Kroner(520000),
        forholdstallUttak = 11.5,
        justeringsbeloep = Kroner(1830),
        referansebeloep = Kroner(11122),
        kompensasjonstilleggForholdstall = 11.7,
        etterbetaling = listOf(
            DinAfpPrivatBeregningDto.Etterbetaling(
                virkningFom = LocalDate.of(2026, 3, 1),
                virkningTom = LocalDate.of(2026, 4, 30),
                etterbetalingAfpLivsvarig = Kroner(1230),
                etterbetalingKronetillegg = Kroner(1830),
                etterbetalingKompensasjonstillegg = Kroner(1130),
                etterbetalingAfpSum = Kroner(1860),
            ),
            DinAfpPrivatBeregningDto.Etterbetaling(
                virkningFom = LocalDate.of(2026, 5, 1),
                virkningTom = LocalDate.of(2026, 6, 30),
                etterbetalingAfpLivsvarig = Kroner(930),
                etterbetalingKronetillegg = Kroner(1220),
                etterbetalingKompensasjonstillegg = Kroner(730),
                etterbetalingAfpSum = Kroner(2830),
            ))
        )
