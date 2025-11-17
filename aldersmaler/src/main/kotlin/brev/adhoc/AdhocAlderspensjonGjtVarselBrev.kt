package brev.adhoc

import brev.felles.Constants.ALDERSPENSJON_GJENLEVENDE_URL
import brev.felles.Constants.ALDERSPENSJON_GJENLEVENDE_URL_EN
import brev.felles.Constants.KONTAKT_ENG_URL
import brev.felles.Constants.KONTAKT_URL
import brev.felles.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import brev.felles.Constants.NAV_KONTAKTSENTER_OPEN_HOURS
import brev.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import brev.felles.Constants.NAV_URL
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocAlderspensjonGjtVarselBrev : AutobrevTemplate<EmptyAutobrevdata> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_ADHOC_2025_VARSELBREV_GJT_KAP_20

    override val template = createTemplate(
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
                bokmal { + "Gjenlevendetillegget i alderspensjonen din blir mindre" },
                nynorsk { + "Attlevandetillegget i alderspensjonen din blir mindre" },
                english { + "The survivor's supplement in your retirement pension will be reduced" }
            )
        }
        outline {
            title1 {
                text(
                    bokmal { + "Forhåndsvarsel" },
                    nynorsk { + "Førehandsvarsel" },
                    english { + "Prior notification" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har oppdaget en feil i beregningen av gjenlevendetillegget i pensjonen din. " +
                            "Det har vært satt for høyt, og vi skal nå rette opp i dette. " +
                            "Du vil få et nytt vedtak med informasjon om det riktige beløpet når justeringen er gjort." },
                    nynorsk { + "Vi har oppdaga ein feil i berekninga av attlevandetillegget i pensjonen din. " +
                            "Det har vore sett for høgt, og vi skal no rette opp i dette. " +
                            "Du vil få eit nytt vedtak med informasjon om det rette beløpet når justeringa er gjord." },
                    english { + "We have discovered an error in the calculation of the survivor's supplement in your pension. " +
                            "It was set too high, and we will now rectify this. You will receive a new decision with " +
                            "information about the correct amount when the adjustment has been made." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Nav vil ikke kreve tilbake det du har fått for mye utbetalt." },
                    nynorsk { + "Nav vil ikkje krevje tilbake det du har fått for mykje utbetalt." },
                    english { + "Nav will not claim repayment of the amount that was overpaid." },
                )
            }

            title1 {
                text(
                    bokmal { + "Hva har skjedd?" },
                    nynorsk { + "Kva har skjedd?" },
                    english { + "What has happened?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du får et gjenlevendetillegg i den delen av alderspensjonen din som beregnes etter nye " +
                            "regler (kapittel 20 i folketrygdloven). Du har tidligere fått et vedtak med informasjon om " +
                            "at dette tillegget gradvis skal fases ut. Det skjer ved at tillegget reduseres like mye som " +
                            "alderspensjonen din øker ved den årlige reguleringen. Gjenlevendetillegget vil derfor bli " +
                            "lavere hvert år og til slutt bli helt borte." },
                    nynorsk { + "Du får eit attlevandetillegg i den delen av alderspensjonen din som blir berekna etter " +
                            "nye reglar (kapittel 20 i folketrygdlova). Du har tidlegare fått eit vedtak med informasjon " +
                            "om at dette tillegget gradvis skal fasast ut. Det skjer ved at tillegget blir redusert like " +
                            "mykje som alderspensjonen din aukar ved den årlege reguleringa. Attlevandetillegget vil difor " +
                            "bli lågare kvart år og til slutt bli heilt borte." },
                    english { + "You receive a survivor's supplement in the part of your retirement pension that is " +
                            "calculated according to new rules (chapter 20 of the National Insurance Act). " +
                            "You have previously received a decision with information that this supplement will be " +
                            "gradually phased out. This is done by reducing the supplement by an amount equal to the " +
                            "annual increase in your retirement pension. The survivor's supplement will therefore become " +
                            "lower each year and will eventually disappear completely. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "I reguleringen mai 2024 ble tillegget ditt redusert, men ved en feil har det senere økt " +
                            "igjen. Det betyr at du har fått for mye utbetalt pensjon. Vi vil nå rette opp feilen og " +
                            "justere pensjonen din til riktig beløp." },
                    nynorsk { + "I reguleringa mai 2024 vart tillegget ditt redusert, men ved ein feil har det seinare " +
                            "auka att. Det betyr at du har fått for mykje utbetalt pensjon. Vi vil no rette opp feilen " +
                            "og justere pensjonen din til rett beløp." },
                    english { + "In the adjustment in May 2024, your supplement was reduced, but due to an error it has " +
                            "later increased again. This means that you have been paid too much pension. We will now " +
                            "correct the error and adjust your pension to the correct amount." },
                )
            }

            title1 {
                text(
                    bokmal { + "Har du spørsmål eller vil uttale deg?" },
                    nynorsk { + "Har du spørsmål eller vil uttale deg?" },
                    english { + "If you have any questions or would like to make a statement, please let us know" },
                )
            }

            paragraph {
                text(
                    bokmal { + "Du har rett til å uttale deg i saken. Fristen for å uttale deg er 14 dager etter at du " +
                            "har fått dette brevet. Uttalelsen bør være skriftlig. Du kan skrive til oss på $KONTAKT_URL" },
                    nynorsk { + "Du har rett til å uttale deg i saka. Fristen for å uttale deg er 14 dagar etter at du " +
                            "har fått dette brevet. Fråsegna bør vere skriftleg. Du kan skrive til oss på $KONTAKT_URL" },
                    english { + "You have the right to submit a statement on your case. The deadline for commenting is " +
                            "14 days after you receive this letter. You can submit a statement by logging in to $KONTAKT_URL" },
                )
            }


            paragraph {
                text(
                    bokmal { + "Hvis vi ikke hører noe fra deg, vil saken bli behandlet med de opplysningene vi har informert om ovenfor." },
                    nynorsk { + "Om vi ikkje høyrer frå deg, vil saka bli behandla med dei opplysningane vi har informert om ovanfor." },
                    english { + "If we do not hear from you, the case will be processed with the information we have provided above." },
                )
            }

            paragraph {
                text(
                    bokmal { + "Du finner mer informasjon om gjenlevendetillegg på $ALDERSPENSJON_GJENLEVENDE_URL. " +
                            "På $KONTAKT_URL kan du chatte eller skrive til oss. Hvis du ikke finner svar på $NAV_URL, " +
                            "kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, hverdager kl. $NAV_KONTAKTSENTER_AAPNINGSTID." },
                    nynorsk { + "Du finn meir informasjon om attlevandetillegg på $ALDERSPENSJON_GJENLEVENDE_URL. " +
                            "På $KONTAKT_URL kan du chatte eller skrive til oss. Om du ikkje finn svar på $NAV_URL, " +
                            "kan du ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, kvardagar kl. $NAV_KONTAKTSENTER_AAPNINGSTID." },
                    english { + "You can find more information about survivor's pension at $ALDERSPENSJON_GJENLEVENDE_URL_EN. " +
                            "At $KONTAKT_ENG_URL you can chat or write to us. If you do not find the answer at $NAV_URL, " +
                            "you can call us at +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON, weekdays $NAV_KONTAKTSENTER_OPEN_HOURS." },
                )
            }
        }
    }
}
