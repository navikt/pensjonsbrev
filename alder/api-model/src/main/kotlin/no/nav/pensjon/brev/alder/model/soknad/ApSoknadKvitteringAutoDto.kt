package no.nav.pensjon.brev.alder.model.soknad

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import java.time.LocalDate

/**
 * DTO for kvittering av innsendt søknad om alderspensjon (brevkode AP_SOKNAD_KVITTERING).
 *
 * Strukturen og feltene speiler 1-1 den gamle XSL-baserte kvitteringen
 * (`alderspensjon.xsl` + `KeyTextGetter` + `PdfPopulator`) i
 * pensjon-selvbetjening-soknad-alder-backend.
 *
 * Forretningslogikken (hvilke rader/seksjoner som vises, navne-/fnr-oppslag med
 * sensitivitet, betegnelse på ektefelle/partner/samboer osv.) gjøres i mapperen
 * i søknad-backend. Denne malen rendrer kun det som ligger i modellen, slik at
 * malen holdes fri for forgreningslogikk.
 *
 * Avvik fra den gamle PDF-en (bevisst):
 * - Fotnoter/«Informasjon»-listen er utelatt (hjelpetekster, ikke kjernedata).
 * - Måned-år vises med liten forbokstav («januar 2025») iht. brevbaker-konvensjon.
 */
data class ApSoknadKvitteringAutoDto(
    val innledning: Innledning,
    val personopplysninger: Personopplysninger,
    val familieforhold: Familieforhold,
    val utland: Utland,
    val afpPrivat: AfpPrivat,
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
        val erUtenlandsk: Boolean,
        val statsborgerskapLand: String?,
        val erFlyktning: Boolean?,
        val kontonummer: String?,
    )

    data class Familieforhold(
        val sivilstand: String,
        val omsorgForBarnUnder7: Boolean?,
        val avdoed: Avdoed?,
        val samboer: Samboer?,
        val harSamboerSpoersmaal: HarSamboerSpoersmaal?,
        val eps: Eps?,
    )

    data class Avdoed(
        val navn: String,
        val foedselsnummer: String?,
    )

    data class Samboer(
        val navn: String,
        val foedselsnummer: String?,
        val samboerskapOpphoertDato: LocalDate?,
    )

    data class HarSamboerSpoersmaal(
        val erNySamboer: Boolean,
        val svar: Boolean,
    )

    data class Eps(
        val betegnelse: String,
        val betegnelseGenitiv: String,
        val betegnelseGenitivStor: String,
        val navnOgFoedselsnummer: EpsNavnOgFoedselsnummer?,
        val samboerFraDato: LocalDate?,
        val giftOgBarn: GiftOgBarn?,
        val leverVarigAdskilt: Boolean?,
        val pensjonOgInntekt: PensjonOgInntekt?,
    )

    data class EpsNavnOgFoedselsnummer(
        val navn: String,
        val foedselsnummer: String?,
    )

    data class GiftOgBarn(
        val tidligereGift: Boolean,
        val harFellesBarn: Boolean,
    )

    data class PensjonOgInntekt(
        val mottarAfp: Boolean,
        val harAnnenInntekt: Boolean,
        val sumInntekt: Int?,
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
        val utlandsId: String?,
        val tilleggsinformasjon: String?,
    )

    data class AfpPrivat(
        val soektAfpPrivat: Boolean,
        val detaljer: AfpPrivatDetaljer?,
    )

    data class AfpPrivatDetaljer(
        val arbeidsgiverNavn: String?,
        val arbeidsgiverAdresse: List<String>,
        val arbeidsgiverOrgnr: String?,
        val omsorgForBarnUnder7: Boolean?,
        val ansattDato: LocalDate?,
        val ansattforholdOpphoert: Boolean?,
        val opphoer: AfpOpphoer?,
        val ansattType: String?,
        val redusertStillingSiste3Ar: Boolean?,
        val stillingUnder20Etter53Ar: Boolean?,
        val sykemeldtMerEnn26Siste3Ar: Boolean?,
        val permittertSiste3Ar: Boolean?,
        val permisjonSiste3Ar: Boolean?,
        val inntektUtenArbeidsplikt: Boolean?,
        val naeringsvirkEierandel20: Boolean?,
        val arbeidetUtlandEtter53: Boolean?,
        val samtykkeEpost: Boolean?,
        val epost: String?,
    )

    data class AfpOpphoer(
        val sisteDagArbeid: LocalDate?,
        val opphoerArsak: String?,
    )
}
