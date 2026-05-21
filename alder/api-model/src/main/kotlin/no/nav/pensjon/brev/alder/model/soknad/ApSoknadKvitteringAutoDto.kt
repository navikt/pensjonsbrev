package no.nav.pensjon.brev.alder.model.soknad

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import java.time.LocalDate

/**
 * DTO for kvittering av innsendt søknad om alderspensjon.
 *
 * Inneholder alle data som vises i søknadskvitteringen — strukturert
 * etter de samme seksjonene som den gamle XSL-baserte kvitteringen:
 * Innledning, Personopplysninger, Familieforhold, Utland, AFP Privat.
 */
data class ApSoknadKvitteringAutoDto(
    val innledning: Innledning,
    val personopplysninger: Personopplysninger,
    val familieforhold: Familieforhold,
    val utland: Utland,
    val afpPrivat: AfpPrivat?,
) : AutobrevData {

    data class Innledning(
        val iverksettelsesdato: LocalDate,
        val uttaksgrad: Int,
        val erNyttRegelverk: Boolean,
    )

    data class Personopplysninger(
        val navn: String,
        val foedselsnummer: String,
        val adresselinjer: List<String>,
        val telefon: String?,
        val statsborgerskap: String,
        val erUtenlandsk: Boolean,
        val erFlyktning: Boolean?,
        val kontonummer: String?,
    )

    data class Familieforhold(
        val sivilstand: String,
        val omsorgForBarnUnder7: Boolean?,
        val eps: EpsInfo?,
        val avdoed: RelasjonInfo?,
        val samboer: SamboerInfo?,
    )

    data class EpsInfo(
        val type: String,
        val navn: String?,
        val foedselsnummer: String?,
        val mottarPensjon: Boolean?,
        val harAnnenInntekt: Boolean?,
        val sumInntekt: Int?,
        val arbeidsinntekt: Int?,
        val kapitalinntekt: Int?,
        val pensjonsinntekt: Int?,
        val leverVarigAdskilt: Boolean?,
    )

    data class RelasjonInfo(
        val navn: String?,
        val foedselsnummer: String?,
    )

    data class SamboerInfo(
        val navn: String?,
        val foedselsnummer: String?,
        val samboerFraDato: LocalDate?,
        val samboerOpphortDato: LocalDate?,
        val tidligereGift: Boolean?,
        val fellesBarn: Boolean?,
    )

    data class Utland(
        val harBoddArbeidetUtland: Boolean,
        val opphold: List<Utenlandsopphold>,
    )

    data class Utenlandsopphold(
        val land: String,
        val bodd: Boolean,
        val arbeidet: Boolean,
        val startDato: LocalDate?,
        val sluttDato: LocalDate?,
        val pensjonsordning: String?,
    )

    data class AfpPrivat(
        val soktAfpPrivat: Boolean,
        val arbeidsgiverNavn: String?,
        val arbeidsgiverOrgnr: String?,
        val ansattDato: LocalDate?,
        val ansattforholdOpphoert: Boolean?,
        val sisteDagArbeid: LocalDate?,
        val opphoerArsak: String?,
        val permisjonSiste3Ar: Boolean?,
        val redusertStillingSiste3Ar: Boolean?,
        val inntektUtenArbeidsplikt: Boolean?,
        val naeringsvirkEierandel20: Boolean?,
        val stillingUnder20Etter53Ar: Boolean?,
        val sykemeldtMerEnn26Siste3Ar: Boolean?,
        val permittertSiste3Ar: Boolean?,
        val arbeidetUtlandEtter53: Boolean?,
        val omsorgForBarnUnder7: Boolean?,
        val samtykkeEpost: Boolean?,
        val epost: String?,
    )
}
