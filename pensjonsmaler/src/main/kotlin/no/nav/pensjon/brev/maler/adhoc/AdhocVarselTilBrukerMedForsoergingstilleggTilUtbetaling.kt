package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants.FORSOERGINGSTILLEGG_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_OPEN_HOURS
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_2024_UTBET_FT_VARSEL_OPPH
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
                bokmal { + "Du får ikke lenger forsørgingstillegg fra 1. januar 2025" },
                nynorsk { + "Du får ikkje lenger forsørgingstillegg frå 1. januar 2025" },
                english { + "You will no longer receive a dependant supplement from 1 January 2025" }
            )
        }
        outline {
            title1 {
                text(
                    bokmal { + "Forhåndsvarsel" },
                    nynorsk { + "Førehandsvarsel" },
                    english { + "Notice" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Reglene for forsørgingstillegg i alderspensjoner er endret, og tillegget er gradvis faset ut." },
                    nynorsk { + "Reglane for forsørgingstillegg i alderspensjonar er endra, og tillegget er gradvis fasa ut." },
                    english { + "The regulations for dependant supplement to retirement pension has been changed, and the supplement is being gradually phased out." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Fra januar 2025 vil ingen lenger få utbetalt forsørgingstillegg. Dette gjelder både barnetillegg og ektefelletillegg. Dette følger av folketrygdloven § 3- 24 tredje avsnitt og § 3-25 andre avsnitt." },
                    nynorsk { + "Frå januar 2025 vil ingen lenger få utbetalt forsørgingstillegg. Dette gjeld både barnetillegg og ektefelletillegg. Dette følger av folketrygdlova § 3- 24 tredje avsnitt og § 3-25 andre avsnitt." },
                    english { + "From January 2025, no one will no longer be paid a dependant supplement. This applies to both child supplements and spouse supplements. This follows from sections 3-24, third paragraph, and 3-25, second paragraph of the National Insurance Act." }
                )
            }

            title1 {
                text(
                    bokmal { + "Hva vil skje i saken din?" },
                    nynorsk { + "Kva vil skje i saka di?" },
                    english { + "What will happen in your case?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "I desember 2024 får du et vedtak fra oss. I vedtaket ser du hva du får i alderpensjon før skatt fra januar 2025.  Du vil ikke lenger få utbetalt forsørgingstillegg. Resten av alderspensjonen din blir ikke endret på grunn av dette." },
                    nynorsk { + "I desember 2024 får du eit vedtak frå oss. I vedtaket ser du kva du får i alderspensjon før skatt frå januar 2025.  Du vil ikkje lenger få utbetalt forsørgingstillegg. Resten av alderspensjonen din blir ikkje endra på grunn av dette." },
                    english { + "In December 2024, you will receive a written decision from us. In the decision, you will see what you will receive in retirement pension before tax from January 2025. You will no longer receive a dependant supplement. The rest of your pension will not be affected by this." }
                )
            }
            paragraph {
                text(
                    bokmal { + "På $FORSOERGINGSTILLEGG_URL finner du mer informasjon om reglene." },
                    nynorsk { + "På $FORSOERGINGSTILLEGG_URL finn du meir informasjon om reglane." },
                    english { + "More information is available at: $FORSOERGINGSTILLEGG_URL" }
                )
            }

            title1 {
                text(
                    bokmal { + "Har du spørsmål eller vil uttale deg?" },
                    nynorsk { + "Har du spørsmål eller vil uttale deg?" },
                    english { + "Do you have any questions, or would you like to comment?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å komme med en uttalelse i saken. Fristen for å uttale deg er 14 dager etter at du har fått dette brevet. Uttalelsen bør være skriftlig. Du kan skrive til oss på $KONTAKT_URL" },
                    nynorsk { + "Du har rett til å komme med ei fråsegn i saka. Fristen for å uttale deg er 14 dagar etter at du har fått dette brevet. Uttalen bør vere skriftleg. Du kan skrive til oss på $KONTAKT_URL" },
                    english { + "You have the right to make a statement in the case. You must do this within 14 days of receiving this letter. The statement should be in writing. At $KONTAKT_URL you can write to us." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis vi ikke hører noe fra deg, vil saken bli behandlet med de opplysningene vi har informert om ovenfor." },
                    nynorsk { + "Viss vi ikkje høyrer noko frå deg, vil saka bli behandla med dei opplysningane vi har informert om ovanfor." },
                    english { + "If we don't hear from you, the case will be processed with the information we have provided above." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du har spørsmål om saken din, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, hverdager $NAV_KONTAKTSENTER_AAPNINGSTID." },
                    nynorsk { + "Viss du har spørsmål om saka di, kan du ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, kvardagar $NAV_KONTAKTSENTER_AAPNINGSTID." },
                    english { + "If you have questions about your case, you can call us at +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON, weekdays $NAV_KONTAKTSENTER_OPEN_HOURS." }
                )
            }
        }
    }
}