package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createVedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto(): VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto =
    VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto.PesysData(
            oppgjoersAar = Year(2024),
            forlitebetalt = Kroner(28_500),
            pensjonsgivendeInntekt = Kroner(412_500),
            inntektFoerUttak = Kroner(85_000),
            inntektEtterOpphoer = Kroner(120_000),
            inntektIAfpPerioden = Kroner(207_500),
            avvik = Kroner(35_000),
            fullAfp = Kroner(220_000),
            fradragBeregnetArbeidsInntekt = Kroner(63_500),
            korrigertAfp = Kroner(156_500),
            tidligereArbeidsInntektBeregnet = Kroner(300_000),
            utbetaltAfp = Kroner(128_000),
            uttaksdato = LocalDate.of(2024, 3, 1),
            opphorsdato = LocalDate.of(2024, 10, 31),
            scenario = VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto.Scenario.IFU_OG_IEO_OVERSTYRT,
            periode = VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto.NyPensjonsberegningPeriode.UTTAK_OG_OPPHOR_I_AARET,
            medlemAvApotekerordningen = false,
            toleranseBeloep = Kroner(15001)
        )
    )
