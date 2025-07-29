package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON_GJENLEVENDE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON_GJENLEVENDE_URL_EN
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_ENG_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocAlderspensjonGjtVarselBrev : AutobrevTemplate<EmptyBrevdata> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ADHOC_2025_VARSELBREV_GJT_KAP_20

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har beregnet for høyt gjenlevendetillegg",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Gjenlevendetillegget i alderspensjonen din blir mindre",
                Nynorsk to "Attlevandetillegget i alderspensjonen din blir mindre",
                English to "The survivor's supplement in your retirement pension will be reduced"
            )
        }
        outline {
            title1 {
                text(
                    Bokmal to "Forhåndsvarsel",
                    Nynorsk to "Førehandsvarsel",
                    English to "Prior notification"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi har oppdaget en feil i beregningen av gjenlevendetillegget i pensjonen din. " +
                            "Det har vært satt for høyt, og vi skal nå rette opp i dette. " +
                            "Du vil få et nytt vedtak med informasjon om det riktige beløpet når justeringen er gjort.",
                    Nynorsk to "Vi har oppdaga ein feil i berekninga av attlevandetillegget i pensjonen din. " +
                            "Det har vore sett for høgt, og vi skal no rette opp i dette. " +
                            "Du vil få eit nytt vedtak med informasjon om det rette beløpet når justeringa er gjord.",
                    English to "We have discovered an error in the calculation of the survivor's supplement in your pension. " +
                            "It was set too high, and we will now rectify this. You will receive a new decision with " +
                            "information about the correct amount when the adjustment has been made.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Nav vil ikke kreve tilbake det du har fått for mye utbetalt.",
                    Nynorsk to "Nav vil ikkje krevje tilbake det du har fått for mykje utbetalt.",
                    English to "Nav will not claim repayment of the amount that was overpaid.",
                )
            }

            title1 {
                text(
                    Bokmal to "Hva har skjedd?",
                    Nynorsk to "Kva har skjedd?",
                    English to "What has happened?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du får et gjenlevendetillegg i den delen av alderspensjonen din som beregnes etter nye " +
                            "regler (kapittel 20 i folketrygdloven). Du har tidligere fått et vedtak med informasjon om " +
                            "at dette tillegget gradvis skal fases ut. Det skjer ved at tillegget reduseres like mye som " +
                            "alderspensjonen din øker ved den årlige reguleringen. Gjenlevendetillegget vil derfor bli " +
                            "lavere hvert år og til slutt bli helt borte.",
                    Nynorsk to "Du får eit attlevandetillegg i den delen av alderspensjonen din som blir berekna etter " +
                            "nye reglar (kapittel 20 i folketrygdlova). Du har tidlegare fått eit vedtak med informasjon " +
                            "om at dette tillegget gradvis skal fasast ut. Det skjer ved at tillegget blir redusert like " +
                            "mykje som alderspensjonen din aukar ved den årlege reguleringa. Attlevandetillegget vil difor " +
                            "bli lågare kvart år og til slutt bli heilt borte.",
                    English to "You receive a survivor's supplement in the part of your retirement pension that is " +
                            "calculated according to new rules (chapter 20 of the National Insurance Act). " +
                            "You have previously received a decision with information that this supplement will be " +
                            "gradually phased out. This is done by reducing the supplement by an amount equal to the " +
                            "annual increase in your retirement pension. The survivor's supplement will therefore become " +
                            "lower each year and will eventually disappear completely. ",
                )
            }
            paragraph {
                text(
                    Bokmal to "I reguleringen mai 2024 ble tillegget ditt redusert, men ved en feil har det senere økt " +
                            "igjen. Det betyr at du har fått for mye utbetalt pensjon. Vi vil nå rette opp feilen og " +
                            "justere pensjonen din til riktig beløp.",
                    Nynorsk to "I reguleringa mai 2024 vart tillegget ditt redusert, men ved ein feil har det seinare " +
                            "auka att. Det betyr at du har fått for mykje utbetalt pensjon. Vi vil no rette opp feilen " +
                            "og justere pensjonen din til rett beløp.",
                    English to "In the adjustment in May 2024, your supplement was reduced, but due to an error it has " +
                            "later increased again. This means that you have been paid too much pension. We will now " +
                            "correct the error and adjust your pension to the correct amount.",
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål eller vil uttale deg?",
                    Nynorsk to "Har du spørsmål eller vil uttale deg?",
                    English to "If you have any questions or would like to make a statement, please let us know",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du har rett til å uttale deg i saken. Fristen for å uttale deg er 14 dager etter at du " +
                            "har fått dette brevet. Uttalelsen bør være skriftlig. Du kan skrive til oss på $KONTAKT_URL",
                    Nynorsk to "Du har rett til å uttale deg i saka. Fristen for å uttale deg er 14 dagar etter at du " +
                            "har fått dette brevet. Fråsegna bør vere skriftleg. Du kan skrive til oss på $KONTAKT_URL",
                    English to "You have the right to submit a statement on your case. The deadline for commenting is " +
                            "14 days after you receive this letter. You can submit a statement by logging in to $KONTAKT_URL",
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis vi ikke hører noe fra deg, vil saken bli behandlet med de opplysningene vi har informert om ovenfor.",
                    Nynorsk to "Om vi ikkje høyrer frå deg, vil saka bli behandla med dei opplysningane vi har informert om ovanfor.",
                    English to "If we do not hear from you, the case will be processed with the information we have provided above.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon om gjenlevendetillegg på $ALDERSPENSJON_GJENLEVENDE_URL. " +
                            "På $KONTAKT_URL kan du chatte eller skrive til oss. Hvis du ikke finner svar på $NAV_URL, " +
                            "kan du ringe oss på telefon 55 55 33 34, hverdager kl. 09:00-15:00.",
                    Nynorsk to "Du finn meir informasjon om attlevandetillegg på $ALDERSPENSJON_GJENLEVENDE_URL. " +
                            "På $KONTAKT_URL kan du chatte eller skrive til oss. Om du ikkje finn svar på $NAV_URL, " +
                            "kan du ringje oss på telefon 55 55 33 34, kvardagar kl. 09:00-15:00.",
                    English to "You can find more information about survivor's pension at $ALDERSPENSJON_GJENLEVENDE_URL_EN. " +
                            "At $KONTAKT_ENG_URL you can chat or write to us. If you do not find the answer at $NAV_URL, " +
                            "you can call us at +47 55 55 33 34, weekdays 09:00–15:00.",
                )
            }
        }
    }
}
