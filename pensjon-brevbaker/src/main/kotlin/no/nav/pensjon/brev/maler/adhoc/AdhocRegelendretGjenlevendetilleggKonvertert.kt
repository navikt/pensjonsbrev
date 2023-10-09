package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_UFORETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.GJENLEVENDETILLEGG_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.INNTEKTSPLANLEGGEREN_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFORETRYGD_ENDRING_URL
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocRegelendretGjenlevendetilleggKonvertert : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Brevkode.AutoBrev.UT_2023_INFO_REGLERENDRET_GJT_KONVERTERT2015
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nye regler for gjenlevendetillegg til uføretrygden din",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Gjenlevendetillegget til uføretrygden din blir ikke lenger regulert",
                Nynorsk to "Attlevandetillegget til uføretrygda di blir ikkje lenger regulert",
                English to "The survivor supplement in your disability benefit will no longer be adjusted annually"
            )
        }

        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for gjenlevendetillegg til uføretrygden fra 1. januar 2024.",
                    Nynorsk to "Stortinget har vedtatt nye reglar for attlevandetillegg til uføretrygda frå 1. januar 2024.",
                    English to "The Norwegian Parliament has decided new rules regarding survivor supplement from 1 January 2024."
                )
            }
            title1 {
                text(
                    Bokmal to "Hva betyr endringene for deg?",
                    Nynorsk to "Kva betyr endringane for deg?",
                    English to "What do the changes mean for you?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du får fortsatt gjenlevendetillegg til uføretrygden din. Du beholder gjenlevendetillegget du har rett til i desember 2023.",
                    Nynorsk to "Du får framleis attlevandetillegg til uføretrygda di. Du beheld attlevandetillegget du har rett til i desember 2023.",
                    English to "You will continue to receive the survivor supplement in your disability benefit. You will still receive the survivor supplement determined for December 2023."
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegget vil ikke lenger bli regulert årlig ved endringer i grunnbeløpet. Uføretrygden vil fortsatt bli regulert årlig.",
                    Nynorsk to "Attlevandetillegget vil ikkje lenger bli regulert årleg ved endringar i grunnbeløpet. Uføretrygda vil framleis bli regulert årleg.",
                    English to "The survivor supplement will no longer be adjusted annually along with the changes to the basic amount of the National Insurance Scheme (G). The disability benefit will continue to be adjusted annually."
                )
            }
            paragraph {
                text(
                    Bokmal to "Beløpet du får utbetalt vil fortsatt bli redusert hvis du har inntekt over inntektsgrensen for uføretrygden din.",
                    Nynorsk to "Beløpet du får utbetalt vil framleis bli redusert viss du har inntekt over inntektsgrensa for uføretrygda di.",
                    English to "The amount you receive will still be reduced if you earn an income above the income limit for receiving disability benefit."
                )
            }
            title1 {
                text(
                    Bokmal to "Lurer du på hva du har utbetalt?",
                    Nynorsk to "Lurar du på kva du har utbetalt?",
                    English to "Would you like to know how much you will receive?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan logge deg på $DIN_UFORETRYGD_URL for å se utbetalingene dine.",
                    Nynorsk to "Du kan logge deg på $DIN_UFORETRYGD_URL for å sjå utbetalingane dine.",
                    English to "Log in to $DIN_UFORETRYGD_URL to see your payments."
                )
            }
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "Notify NAV about changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du mister gjenlevendetillegget hvis du gifter deg igjen eller får barn med en samboer.",
                    Nynorsk to "Du mistar attlevandetillegget viss du giftar deg att eller får barn med ein sambuar.",
                    English to "You will lose the survivor supplement if you remarry or have children with a cohabitee."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger opphold i et annet land, kan det påvirke utbetalingen din. I slike tilfeller må du derfor straks melde fra til NAV. Se hva du må melde fra om på $UFORETRYGD_ENDRING_URL.",
                    Nynorsk to "Viss du får endringar i inntekt, familiesituasjon, jobbsituasjon eller planlegg opphald i eit anna land, kan det verke inn på utbetalinga di. I slike tilfelle må du derfor straks melde frå til NAV. Sjå kva du må melde frå om på $UFORETRYGD_ENDRING_URL.",
                    English to "If you have experienced a change in income, changes to your family or job situation, or plan to live abroad, this may influence your benefits. You are obligated to notify NAV as soon as you are aware of any of these changes. You can find out what you are required to report at $UFORETRYGD_ENDRING_URL."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan oppdatere inntekten din på $INNTEKTSPLANLEGGEREN_URL.",
                    Nynorsk to "Du kan oppdatere inntekta di på $INNTEKTSPLANLEGGEREN_URL.",
                    English to "You can update your income at $INNTEKTSPLANLEGGEREN_URL."
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
                    Bokmal to "Du finner mer informasjon på $GJENLEVENDETILLEGG_URL.",
                    Nynorsk to "Du finn meir informasjon på $GJENLEVENDETILLEGG_URL.",
                    English to "You can find more information at $GJENLEVENDETILLEGG_URL."
                )
                newline()
                text(
                    Bokmal to "På $KONTAKT_URL kan du chatte eller skrive til oss.",
                    Nynorsk to "På $KONTAKT_URL kan du chatte eller skrive til oss.",
                    English to "At $KONTAKT_URL you can chat or write to us."
                )
                newline()
                text(
                    Bokmal to "Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon ${NAV_KONTAKTSENTER_TELEFON}, hverdager $NAV_KONTAKTSENTER_AAPNINGSTID.",
                    Nynorsk to "Om du ikkje finn svar på $NAV_URL, kan du ringe oss på telefon ${NAV_KONTAKTSENTER_TELEFON}, kvardagar $NAV_KONTAKTSENTER_AAPNINGSTID.",
                    English to "If you do not find the answer at $NAV_URL, you can call us at: +47 ${NAV_KONTAKTSENTER_TELEFON}, weekdays $NAV_KONTAKTSENTER_AAPNINGSTID."
                )
            }
        }
    }


}