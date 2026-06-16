package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createVedtakAfpEtteroppgjoerIngenEndringDto(): VedtakAfpEtteroppgjoerIngenEndringDto =
    VedtakAfpEtteroppgjoerIngenEndringDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerIngenEndringDto.PesysData(
            oppgjoersAar = Year(2024),
            pensjonsgivendeInntekt = Kroner(280_000),
            inntektFoerUttak = Kroner(40_000),
            inntektEtterOpphoer = Kroner(0),
            inntektIAfpPerioden = Kroner(240_000),
            forventetPensjonsgivendeInntektBeregnet = Kroner(250_000),
            avvik = Kroner(10_000),
            uttaksdato = LocalDate.of(2024, 3, 1),
            opphorsdato = null,
            medlemAvApotekerordningen = true,
            toleranseBeloep = Kroner(3000),
            periode = VedtakAfpEtteroppgjoerIngenEndringDto.Periode.UTTAK_I_AARET,
        ),
    )

fun createVedtakAfpEtteroppgjoerEtterbetalingDto(): VedtakAfpEtteroppgjoerEtterbetalingDto =
    VedtakAfpEtteroppgjoerEtterbetalingDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerEtterbetalingDto.PesysData(
            oppgjoersAar = Year(2024),
            forlitebetalt = Kroner(14_000),
            pensjonsgivendeInntekt = Kroner(220_000),
            inntektFoerUttak = Kroner(40_000),
            inntektEtterOpphoer = Kroner(0),
            inntektIAfpPerioden = Kroner(180_000),
            forventetPensjonsgivendeInntektBeregnet = Kroner(250_000),
            fullAfp = Kroner(200_000),
            fradragBeregnetArbeidsInntekt = Kroner(50_000),
            tidligereArbeidsInntektBeregnet = Kroner(250_000),
            korrigertAfp = Kroner(150_000),
            utbetaltAfp = Kroner(135_000),
            uttaksdato = LocalDate.of(2024, 3, 1),
            opphorsdato = null,
            medlemAvApotekerordningen = true,
            toleranseBeloep = Kroner(33_240),
            periode = VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode.UTTAK_I_AARET,

        ),
    )

fun createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto(): VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto =
    VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto.PesysData(
            oppgjoersAar = Year(2024),
            pgi = Kroner(280_000),
            scenario = VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto.Scenario.IKKE_AFP_FULL_INNTEKT,
        ),
    )
