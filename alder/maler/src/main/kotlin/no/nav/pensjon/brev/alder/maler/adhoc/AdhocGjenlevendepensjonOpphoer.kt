package no.nav.pensjon.brev.alder.maler.adhoc

import no.nav.pensjon.brev.alder.maler.felles.Constants.ALDERSPENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.GJENLEVENDEPENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KONTAKT_ENG_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KONTAKT_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SOSIALHJELP_URL
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
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
object AdhocGjenlevendepensjonOpphoer : AutobrevTemplate<EmptyAutobrevdata> {
    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_ADHOC_2026_OPPHOER_GJP
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Feil informasjon om når gjenlevendepensjonen stopper ",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ){
        title {
            text(
                bokmal { +"Feil informasjon om når gjenlevendepensjonen stopper" },
                nynorsk { +"Feil informasjon om når attlevandepensjonen stansar" },
                english { +"Incorrect information about when the survivor's pension stops" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Du har tidligere fått brev om at retten til gjenlevendepensjon opphører fra 1. januar 2027." },
                    nynorsk { +"Du har tidlegare fått brev om at retten til attlevandepensjon stansar frå 1. januar 2027." },
                    english { +"You have previously received a letter stating that your entitlement to survivor's pension ends on 1 January 2027." },
                )
            }

            paragraph {
                text(
                    bokmal { +"I høst fikk du et nytt brev fra oss med informasjon om alderspensjon. " +
                            "I dette brevet sto det at gjenlevendepensjonen din opphører fra og med måneden etter at du fyller 67 år. " +
                            "Det riktige er at gjenlevendepensjonen stopper 1. januar 2027. Vi beklager feilen." },
                    nynorsk { +"I haust fekk du eit nytt brev frå oss med informasjon om alderspensjon. " +
                            "I dette brevet stod det at attlevandepensjonen din opphøyrer frå og med månaden etter at du fyller 67 år. " +
                            "Det rette er at attlevandepensjonen stoppar 1. januar 2027. Vi ber om orsak for feilen." },
                    english { +"This autumn, you received another letter from us with information about your retirement pension. " +
                            "In that letter, it stated that your survivor's pension would stop from the month after you turn 67. " +
                            "The correct information is that the survivor's pension stops on 1 January 2027. We are sorry for the error." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du finner mer informasjon om utfasing av gjenlevendepensjon og mulig forlengelse på $GJENLEVENDEPENSJON_URL." },
                    nynorsk { +"Du finn meir informasjon om utfasing av attlevandepensjon og mogleg forlenging på $GJENLEVENDEPENSJON_URL." },
                    english { +"You can find more information about the phasing out of survivor's pension and possible extensions at $GJENLEVENDEPENSJON_URL." },
                )
            }

            title2 {
                text(
                    bokmal { +"Du må søke om alderspensjon" },
                    nynorsk { +"Du må søkje om alderspensjon" },
                    english { +"You must apply for retirement pension" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Som hovedregel har du rett til å ta ut alderspensjon fra måneden etter at du fyller 67 år. " +
                            "Du kan ha rett til å ta ut alderspensjon tidligere." },
                    nynorsk { +"Som hovudregel har du rett til å ta ut alderspensjon frå månaden etter at du fyller 67 år. " +
                            "Du kan ha rett til å ta ut alderspensjon tidlegare." },
                    english { +"As a rule, you are entitled to take out retirement pension from the month after you turn 67. " +
                            "You may also be entitled to take out retirement pension earlier." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du må selv søke om alderspensjon på $DIN_PENSJON_URL. " +
                            "Du kan søke om alderspensjon 4 måneder før det tidspunktet du ønsker at utbetalingen skal starte. " +
                            "Du finner mer informasjon på $ALDERSPENSJON_URL." },
                    nynorsk { +"Du må sjølv søkje om alderspensjon på $DIN_PENSJON_URL. " +
                            "Du kan søkje om alderspensjon 4 månader før det tidspunktet du ønskjer at utbetalinga skal starte. " +
                            "Du finn meir informasjon på $ALDERSPENSJON_URL." },
                    english { +"You must apply for retirement pension yourself at $DIN_PENSJON_URL. " +
                            "You can apply for retirement pension up to 4 months before the date you want the payment to start. " +
                            "You can find more information at $ALDERSPENSJON_URL." },
                )
            }

            title2 {
                text(
                    bokmal { +"Økonomisk sosialhjelp" },
                    nynorsk { +"Økonomisk sosialhjelp" },
                    english { +"Financial social assistance" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Når alle andre inntektsmuligheter er utnyttet fullt ut, men ikke strekker til, kan du ha rett til økonomisk sosialhjelp. " +
                            "Du kan lese mer på $SOSIALHJELP_URL." },
                    nynorsk { +"Når alle andre inntektsmoglegheiter er nytta fullt ut, men ikkje strekkjer til, kan du ha rett til økonomisk sosialhjelp. " +
                            "Du kan lese meir på $SOSIALHJELP_URL." },
                    english { +"If all other income opportunities have been used to the fullest extent but are not sufficient, you may be entitled to financial social assistance. " +
                            "You can read more at $SOSIALHJELP_URL." },
                )
            }

            title2 {
                text(
                    bokmal { +"Har du spørsmål?" },
                    nynorsk { +"Har du spørsmål?" },
                    english { +"Do you have any questions?" },
                )
            }

            paragraph {
                text(
                    bokmal { +"På $KONTAKT_URL kan du chatte eller skrive til oss. " +
                            "Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON hverdager kl. 09.00-15.00." },
                    nynorsk { +"På $KONTAKT_URL kan du chatte eller skrive til oss. " +
                            "Om du ikkje finn svar på $NAV_URL, kan du ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON kvardagar kl. 09.00-15.00." },
                    english { +"At $KONTAKT_ENG_URL you can chat with us or write to us. " +
                            "If you cannot find the answer on $NAV_URL, you can call us on telephone +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON on weekdays between 09.00 and 15.00." },
                )
            }
        }
    }
}