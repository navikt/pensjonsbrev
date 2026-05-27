package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createVedtakAfpEtteroppgjoerEtterbetalingAutoDto(): VedtakAfpEtteroppgjoerEtterbetalingAutoDto =
    VedtakAfpEtteroppgjoerEtterbetalingAutoDto(
        oppgjoersAar = Year(2024),
        forlitebetalt = Kroner(28_500),
        pensjonsgivendeInntekt = Kroner(412_500),
        inntektFoerUttak = Kroner(85_000),
        inntektEtterOpphoer = Kroner(120_000),
        inntektIAfpPerioden = Kroner(207_500),
        forventetPensjonsgivendeInntektBeregnet = Kroner(250_000),
        fullAfp = Kroner(220_000),
        fradragberegnetai = Kroner(63_500),
        korrigertAfp = Kroner(156_500),
        tpiberegnet = Kroner(300_000),
        utbetaltAfp = Kroner(128_000),
        uttaksdato = LocalDate.of(2024, 3, 1),
        opphorsdato = LocalDate.of(2024, 10, 31),
        periode = VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode.UTTAK_OG_OPPHOER_I_AARET,
    )
