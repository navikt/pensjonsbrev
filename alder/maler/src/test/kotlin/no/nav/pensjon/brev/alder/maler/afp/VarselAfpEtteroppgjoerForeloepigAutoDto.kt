package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.AfpPeriode
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createVarselAfpEtteroppgjoerForeloepigAutoDto(): VarselAfpEtteroppgjoerForeloepigAutoDto =
    VarselAfpEtteroppgjoerForeloepigAutoDto(
        oppgjoersAar = Year(2024),
        formyebetalt = Kroner(38_500),
        uttaksdato = LocalDate.of(2024, 3, 1),
        opphorsdato = LocalDate.of(2024, 9, 30),
        pensjonsgivendeInntekt = Kroner(412_500),
        inntektFoerUttak = Kroner(120_000),
        inntektEtterOpphoer = Kroner(85_000),
        inntektIAfpPerioden = Kroner(207_500),
        forventetInntekt = Kroner(150_000),
        fullAfp = Kroner(220_000),
        fradragBeregnetArbeidsInntekt = Kroner(63_500),
        korrigertAfp = Kroner(156_500),
        tidligereArbeidsInntektBeregnet = Kroner(300_000),
        utbetaltAfp = Kroner(195_000),
        periode = AfpPeriode.UTTAK_OG_OPPHOER_I_AARET,
        toleranseBeloep = Kroner(32100),
    )
