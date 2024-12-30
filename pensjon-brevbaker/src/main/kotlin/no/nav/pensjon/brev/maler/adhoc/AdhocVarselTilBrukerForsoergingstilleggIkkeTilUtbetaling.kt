package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants.FORSOERGINGSTILLEGG_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_2024_IKKEUTBET_FT_VARSEL_OPPH
    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Forsørgingstillegg til alderspensjon blir faset ut",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Du får ikke lenger forsørgingstillegg fra 1. januar 2025",
                Nynorsk to "Du får ikkje lenger forsørgingstillegg frå 1. januar 2025",
                English to "You will no longer receive a dependant supplement from 1 January 2025"
            )
        }
        outline {
            title1 {
                text(
                    Bokmal to "Forhåndsvarsel",
                    Nynorsk to "Førehandsvarsel",
                    English to "Notice",
                )
            }
            paragraph {
                text(
                    Bokmal to "Reglene for forsørgingstillegg i alderspensjoner er endret, og tillegget er gradvis faset ut.",
                    Nynorsk to "Reglane for forsørgingstillegg i alderspensjonar er endra, og tillegget er gradvis fasa ut.",
                    English to "The regulations for dependant supplement to retirement pension has been changed, and the supplement is being gradually phased out."
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra januar 2025 vil ingen lenger få utbetalt forsørgingstillegg. Dette gjelder både barnetillegg og ektefelletillegg. Dette følger av folketrygdloven § 3- 24 tredje avsnitt og § 3-25 andre avsnitt.",
                    Nynorsk to "Frå januar 2025 vil ingen lenger få utbetalt forsørgingstillegg. Dette gjeld både barnetillegg og ektefelletillegg. Dette følger av folketrygdlova § 3- 24 tredje avsnitt og § 3-25 andre avsnitt.",
                    English to "From January 2025, no one will no longer be paid a dependant supplement. This applies to both child supplements and spouse supplements. This follows from sections 3-24, third paragraph, and 3-25, second paragraph of the National Insurance Act."
                )
            }

            title1 {
                text(
                    Bokmal to "Hva vil skje i saken din?",
                    Nynorsk to "Kva vil skje i saka di?",
                    English to "What will happen in your case?"
                )
            }
            paragraph {
                text(
                    Bokmal to "I desember 2024 får du et vedtak fra oss. I vedtaket ser du hva du får i alderspensjon før skatt fra 2025.",
                    Nynorsk to "I desember 2024 får du eit vedtak frå oss. I vedtaket ser du kva du får i alderspensjon før skatt frå januar 2025.",
                    English to "In December 2024, you will receive a written decision from us. In the decision, you can see what you will receive in retirement pension before tax from January 2025."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har for tiden ikke utbetalt forsørgingstillegg fordi den samlede inntekten er for høy. Endringen fra 1. januar 2025 vil derfor ikke ha noen innvirkning på utbetalingen din.",
                    Nynorsk to "Du har for tida ikkje utbetalt forsørgingstillegg fordi den samla inntekta er for høg. Endringa frå 1. januar 2025 vil derfor ikkje ha noko å seie for utbetalinga di.",
                    English to "At present the supplement is not paid out to you, as your total income is too high. Therefore, the change from 1 January 2025 will not have any impact on your payout."
                )
            }
            paragraph {
                text(
                    Bokmal to "På $FORSOERGINGSTILLEGG_URL finner du mer informasjon om reglene.",
                    Nynorsk to "På $FORSOERGINGSTILLEGG_URL finn du meir informasjon om reglane.",
                    English to "More information is available at: $FORSOERGINGSTILLEGG_URL"
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål eller vil uttale deg?",
                    Nynorsk to "Har du spørsmål eller vil uttale deg?",
                    English to "Do you have any questions, or would you like to comment?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å komme med en uttalelse i saken. Fristen for å uttale deg er 14 dager etter at du har fått dette brevet. Uttalelsen bør være skriftlig. Du kan skrive til oss på $KONTAKT_URL",
                    Nynorsk to "Du har rett til å komme med ei fråsegn i saka. Fristen for å uttale deg er 14 dagar etter at du har fått dette brevet. Uttalen bør vere skriftleg. Du kan skrive til oss på $KONTAKT_URL",
                    English to "You have the right to make a statement in the case. You must do this within 14 days of receiving this letter. The statement should be in writing. At $KONTAKT_URL you can write to us."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis vi ikke hører noe fra deg, vil saken bli behandlet med de opplysningene vi har informert om ovenfor.",
                    Nynorsk to "Viss vi ikkje høyrer noko frå deg, vil saka bli behandla med dei opplysningane vi har informert om ovanfor.",
                    English to "If we don't hear from you, the case will be processed with the information we have provided above."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner frem på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, hverdager 09.00-15.00.",
                    Nynorsk to "Viss du har spørsmål om saka di, kan du ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, kvardagar 09.00-15.00.",
                    English to "If you have questions about your case, you can call us at +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON, weekdays 09:00-15:00."
                )
            }
        }
    }
}