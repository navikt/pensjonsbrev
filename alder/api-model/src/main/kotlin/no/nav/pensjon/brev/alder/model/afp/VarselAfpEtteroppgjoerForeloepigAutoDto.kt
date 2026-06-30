package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

data class VarselAfpEtteroppgjoerForeloepigAutoDto(
    val oppgjoersAar: Year,
    val formyebetalt: Kroner,
    val uttaksdato: LocalDate,
    val opphorsdato: LocalDate?,
    val pensjonsgivendeInntekt: Kroner,
    val inntektFoerUttak: Kroner,
    val inntektEtterOpphoer: Kroner,
    val inntektIAfpPerioden: Kroner,
    val forventetInntekt: Kroner,
    val fullAfp: Kroner,
    val fradragBeregnetArbeidsInntekt: Kroner,
    val korrigertAfp: Kroner,
    val tidligereArbeidsInntektBeregnet: Kroner,
    val utbetaltAfp: Kroner,
    val periode: AfpPeriode,
    val toleranseBeloep: Kroner,
) : AutobrevData