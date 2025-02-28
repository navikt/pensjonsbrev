package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.model.LetterMetadataPensjon
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocAlderspensjonFraFolketrygden : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ADHOC_2024_REGLERENDRET_GJR_AP_MNTINDV
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadataPensjon(
            displayTitle = "Alderspensjon fra folketrygden",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Alderspensjon fra folketrygden",
                Nynorsk to "Alderspensjon frå folketrygda",
                English to "Retirement pension from the National Insurance Scheme"
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for gjenlevenderett i alderspensjonen fra 1. januar 2024.",
                    Nynorsk to "Stortinget har vedtatt nye reglar for attlevanderett i alderspensjonen frå 1. januar 2024.",
                    English to "The Norwegian Parliament has decided new rules regarding survivor's rights in the retirement pension from the 1st of January 2024."
                )
            }
            paragraph {
                text(
                    Bokmal to "Reglene har ingen praktisk betydning for deg. Samlet pensjon før skatt er den samme som før. "
                            + "Vi har bare endret metoden for beregningen av alderspensjonen din.",
                    Nynorsk to "Reglane har ingen praktisk tyding for deg. Samla pensjon før skatt er den same som før. "
                            + "Vi har berre endra metoden for utrekninga av alderspensjonen din.",
                    English to "The rules have no practical significance for you. Your total pension amount before tax is the same as before. "
                            + "We have only changed the method for calculating your retirement pension."
                )
            }
            title1 {
                text(
                    Bokmal to "Endret metode for beregning",
                    Nynorsk to "Endra metode for utrekning",
                    English to "Changed method for calculation"
                )
            }
            paragraph {
                text(
                    Bokmal to "Metoden for beregningen av alderspensjonen din gjør at du nå får et gjenlevendetillegg. "
                            + "Dette erstatter det som før het «minstenivåtillegg individuelt».",
                    Nynorsk to "Metoden for utrekninga av alderspensjonen din gjer at du no får eit attlevandetillegg. "
                            + "Dette erstattar det som før heitte «minstenivåtillegg individuelt».",
                    English to "The method for calculating your retirement pension means that you now receive a survivor’s supplement. "
                            + "This replaces what was previously called ‘minimum pension supplement’."
                )
            }
            paragraph {
                text(
                    Bokmal to "Alderspensjonen reguleres hvert år med gjennomsnittet av pris- og lønnsveksten.",
                    Nynorsk to "Alderspensjonen blir regulert kvart år med gjennomsnittet av pris- og lønnsveksten.",
                    English to "Retirement pension is adjusted annually by the average of consumer price and wage growth."
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegget vil ikke bli regulert. For at du ikke skal tape på dette, "
                            + "vil du ved reguleringen få et nytt minstenivåtillegg individuelt.",
                    Nynorsk to "Attlevandetillegget vil ikkje bli regulert. For at du ikkje skal tape på dette, "
                            + "vil du ved reguleringa få eit nytt minstenivåtillegg individuelt.",
                    English to "The survivor’s supplement will not be adjusted. To ensure that you do not lose out on this, "
                            + "you will receive a new individual minimum pension supplement at the time of adjustment."
                )
            }
            title1 {
                text(
                    Bokmal to "Lurer du på hva du har utbetalt?",
                    Nynorsk to "Lurer du på kva du har fått utbetalt?",
                    English to "Would you like to know how much you will receive?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan logge deg på ${Constants.UTBETALINGSOVERSIKT_URL} for å se utbetalingene dine.",
                    Nynorsk to "Du kan logge deg på ${Constants.UTBETALINGSOVERSIKT_URL} for å sjå utbetalingane dine.",
                    English to "Log in to ${Constants.UTBETALINGSOVERSIKT_URL} to see your payments.",
                )
            }
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "Notify Nav about changes",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i familiesituasjon eller planlegger opphold i et annet land, kan det påvirke utbetalingen din. "
                            + "I slike tilfeller må du derfor straks melde fra til Nav. Se hva du må melde fra om på ${Constants.PENSJON_ENDRING_URL}.",
                    Nynorsk to "Viss du får endringar i familiesituasjon eller planlegg opphald i eit anna land, kan det påverke utbetalinga di. "
                            + "I slike tilfelle må du derfor straks melde frå til Nav. Sjå kva du må melde frå om på ${Constants.PENSJON_ENDRING_URL}.",
                    English to "If you have changes in your family situation or you plan to live abroad, this may influence your benefits. "
                            + "You are obliged to notify Nav as soon as you are aware of any of these changes. "
                            + "You can find out what you are required to report at ${Constants.PENSJON_ENDRING_URL}."
                )
            }
            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på ${Constants.REGULERING_ALDERSPENSJON_URL}."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.REGULERING_ALDERSPENSJON_URL}."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Viss du ikkje finner svar på $NAV_URL, kan du ringje oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to "You can find more information at ${Constants.REGULERING_ALDERSPENSJON_URL}."
                            + " At ${Constants.KONTAKT_URL}, you can chat or write to us."
                            + " If you do not find the answer at $NAV_URL, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                )
            }
        }
    }
}