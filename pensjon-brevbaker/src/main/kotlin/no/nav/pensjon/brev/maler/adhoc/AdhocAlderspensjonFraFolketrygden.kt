package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.reflect.jvm.internal.impl.resolve.scopes.MemberScope.Empty

object AdhocAlderspensjonFraFolketrygden : AutobrevTemplate<EmptyBrevdata> {
    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_AP_ADHOC_2024_REGLERENDRET_GJR_AP_MNTINDV
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
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
            title1 {
                text(
                    Bokmal to "Hva betyr dette for deg?",
                    Nynorsk to "Kva betyr dette for deg?",
                    English to "What does this mean for you?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi har endret metoden for beregningen av alderspensjonen din. Dette gjør at du nå får et gjenlevendetillegg. Dette erstatter det som før het «minstenivåtillegg individuelt».",
                    Nynorsk to "Vi har endra metoden for berekninga av alderspensjonen din. Dette gjer at du no får eit attlevandetillegg. Dette erstattar det som før heitte «minstenivåtillegg individuelt».",
                    English to "We have changed the method for calculating your retirement pension. This means that you now receive a survivor’s supplement. This replaces what was previously called ‘minimum pension supplement’."
                )
            }
            paragraph {
                text(
                    Bokmal to "Samlet pensjon før skatt er den samme som før.",
                    Nynorsk to "Samla pensjon før skatt er den same som før.",
                    English to "The total pension before tax is the same as before."
                )
            }
            paragraph {
                text(
                    Bokmal to "Alderspensjonen reguleres hvert år med gjennomsnittet av pris- og lønnsveksten.",
                    Nynorsk to "Alderspensjonen blir regulert kvart år med gjennomsnittet av pris- og lønnsveksten.",
                    English to "The retirement pension is adjusted each year by the average of consumer price and wage growth."
                )
            }
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "Notify NAV about changes",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i familiesituasjon eller planlegger opphold i et annet land, kan det påvirke utbetalingen din. I slike tilfeller må du derfor straks melde fra til NAV. Se hva du må melde fra om på nav.no/pensjon-endring.",
                    Nynorsk to "Viss du får endringar i familiesituasjon eller planlegg opphald i eit anna land, kan det påverke utbetalinga di. I slike tilfelle må du derfor straks melde frå til NAV. Sjå kva du må melde frå om på nav.no/pensjon-endring. ",
                    English to "If you have changes in your family situation or you plan to live abroad, this may influence your benefits. You are obliged to notify NAV as soon as you are aware of any of these changes. You can find out what you are required to report at nav.no/pensjon-endring."
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
                    Bokmal to "Du finner mer informasjon på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON},"
                            + " hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}."
                            + " Om du ikkje finn svar på $NAV_URL, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON},"
                            + " kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to "You can find more information at ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}."
                            + " At ${Constants.KONTAKT_URL}, you can chat or write to us."
                            + " If you do not find the answer at $NAV_URL, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON},"
                            + " weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                )
            }
        }
    }
}