package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocGjenlevendEtter1970 : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_ADHOC_2024_VEDTAK_GJENLEVENDETTER1970
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - gjenlevendepensjonen din opphører fra 1. januar 2027",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Gjenlevendepensjonen din opphører fra 1. januar 2027",
                Nynorsk to "Attlevandepensjonen din blir stansa frå 1. januar 2027",
                English to "Your survivor's pension terminates 1 January 2027"
            )
        }
        outline {
            title1 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "Vedtak",
                    English to "Decision"
                )
            }
            paragraph {
                text(
                    Bokmal to "Tidligere har vi informert deg om at gjenlevendepensjonen din fra folketrygden blir tidsbegrenset. "
                            + "Det betyr at du fortsatt får gjenlevendepensjon i tre år regnet fra 1. januar 2024.",
                    Nynorsk to "Tidlegare har vi informert deg om at attlevandepensjonen din frå folketrygda blir tidsavgrensa. "
                            + "Det betyr at du framleis får attlevandepensjon i tre år rekna frå 1. januar 2024.",
                    English to "As we have previously informed you, your survivor’s pension through the National Insurance Act will be time limited. "
                            + "This means you will still receive the pension for three more years as of 1 January 2024."
                )
            }
            paragraph {
                text(
                    Bokmal to "Retten din til gjenlevendepensjon vil opphøre fra og med 1. januar 2027. Siste mulige utbetaling blir i desember 2026.",
                    Nynorsk to "Retten din til attlevandepensjon blir stansa frå og med 1. januar 2027. Siste moglege utbetaling blir i desember 2026.",
                    English to "Your entitlement to survivor’s pension terminates 1 January 2027. The last payment will be in December 2026."
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 17 A-3 første avsnitt første setning.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 17 A-3 første avsnitt første setning.",
                    English to "This decision was made pursuant to the provisions of § 17 A-3, first paragraph, first sentence of the National Insurance Act."
                )
            }
            title1 {
                text(
                    Bokmal to "Kan stønadsperioden forlenges?",
                    Nynorsk to "Kan stønadsperioden bli forlenga?",
                    English to "Can the benefit period be extended?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan søke om å få pensjonen din forlenget med opptil to år. "
                            + "For å få utvidet stønadsperioden må du være under nødvendig utdanning eller ha behov for hjelp til å få jobb fra 1. januar 2027.",
                    Nynorsk to "Du kan søkje om å få pensjonen din forlenga med inntil to år. "
                            + "For å få utvida stønadsperioden må du vere under nødvendig utdanning eller ha behov for hjelp til å få jobb frå 1. januar 2027.",
                    English to "You can apply to have your pension extended by up to two years. "
                            + "You can qualify for an extended benefit period, if you are still undertaking a necessary education or require help to find employment as of 1 January 2027."
                )
            }
            title1 {
                text(
                    Bokmal to "Trenger du hjelp til å få jobb eller jobbe mer?",
                    Nynorsk to "Treng du hjelp til å få jobb eller jobbe meir?",
                    English to "Do you need help getting a job or more work?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Nav tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. "
                            + "Du kan finne jobbsøkertips og lenke til ledige jobber på nav.no/arbeid.",
                    Nynorsk to "Nav tilbyr ulike tenester og støtteordningar for deg som treng hjelp til å få jobb. "
                            + "Du kan finne jobbsøkjartips og lenkje til ledige jobbar på nav.no/arbeid.",
                    English to "The Norwegian Labour and Welfare Administration (Nav) offers various services and support schemes for those who need help finding employment. "
                            + "You can find job search tips and a hyperlink to job vacancies online: nav.no/arbeid."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan få veiledning og støtte fra Nav. Vi kan fortelle deg om ulike muligheter i arbeidsmarkedet "
                            + "eller snakke med deg om nødvendig utdanning eller andre tiltak som kan hjelpe deg med å komme i arbeid.",
                    Nynorsk to "Du kan få rettleiing frå Nav. Vi kan fortelje deg om ulike mogelegheiter i arbeidsmarknaden "
                            + "eller snakke med deg om nødvendig utdanning eller andre tiltak som kan hjelpe deg med å kome i arbeid.",
                    English to "You can receive guidance from Nav. We can inform you about various opportunities in the job market "
                            + "or discuss necessary education or other measures that can help you find employment."
                )
            }
            title1 {
                text(
                    Bokmal to "Har du helseutfordringer?",
                    Nynorsk to "Har du helseutfordringar?",
                    English to "Do you have health issues?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan undersøke mulighetene for andre ytelser eller støtteordninger ved ditt lokale Nav-kontor og på nav.no/helse.",
                    Nynorsk to "Du kan undersøkje om du kan få andre ytingar eller støtteordningar ved det lokale Nav-kontoret ditt og på nav.no/helse.",
                    English to "If you have health issues, you can check if you are eligible for other benefits or support schemes through your local Nav office or see nav.no/helse."
                )
            }
            title1 {
                text(
                    Bokmal to "Meld fra om endringer",
                    Nynorsk to "Meld frå om endringar",
                    English to "Notify Nav about changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du mister retten til gjenlevendepensjon hvis du gifter deg. Du mister også retten "
                            + "hvis du flytter sammen med en du har barn med eller tidligere har vært gift med.",
                    Nynorsk to "Du mistar attlevandepensjonen din om du giftar deg. Du mistar også retten "
                            + "om du flyttar saman med ein du har barn med eller tidlegare har vore gift med.",
                    English to "You lose the survivor's pension if you marry, "
                            + "or you live with someone you have children with or were previously married to."
                )
            }
            paragraph {
                text(
                    Bokmal to "Andre endringer som kan ha betydning for hva du får utbetalt:",
                    Nynorsk to "Andre endringar kan verka inn på kva du får utbetalt:",
                    English to "Other changes that may affect your payments:"
                )
                list {
                    item {
                        text(
                            Bokmal to "endret arbeidsinntekt",
                            Nynorsk to "endra arbeidsinntekt",
                            English to "your income or job situation changes"
                        )
                    }
                    item {
                        text(
                            Bokmal to "planlegger å flytte til et annet land",
                            Nynorsk to "planar om å flytte til eit anna land",
                            English to "you move to another country"
                        )
                    }
                    item {
                        text(
                            Bokmal to "blir samboer eller flytter fra samboer",
                            Nynorsk to "blir sambuar eller flytter frå sambuar",
                            English to "you either become a cohabitant or you move away from your cohabitant"
                        )
                    }
                    item {
                        text(
                            Bokmal to "hvis samboerens inntektsforhold endrer seg.",
                            Nynorsk to "sambuaren si inntekt endrar seg.",
                            English to "the income of your cohabitant changes"
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "I slike tilfeller må du derfor straks melde fra til Nav.",
                    Nynorsk to "Meld i så fall frå straks til Nav.",
                    English to "You are obligated to notify Nav as soon as you are aware of any of these changes."
                )
            }
            title1 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right of appeal"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. "
                            + "Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage. "
                            + "I vedlegget får du vite mer om hvordan du går fram.",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. "
                            + "Klaga skal vera skriftleg. Du finn skjema og informasjon på nav.no/klage. "
                            + "I vedlegget får du vite meir om korleis du går fram.\n",
                    English to "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. "
                            + "In the attachment “Your rights and how to appeal”, you can find out more about how to proceed. "
                            + "You will find forms and information at nav.no/klage."
                )
            }
            title1 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn",
                    English to "You have the right to access your file"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram.",
                    English to "You are entitled to see your case documents. Refer to the attachment «Your rights and how to appeal» for information about how to proceed."
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
                    Bokmal to "Du finner mer informasjon på ${Constants.GJENLEVENDEPENSJON_URL}. "
                            + "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. "
                            + "Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, "
                            + "hverdager kl. 09.00-15.00.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.GJENLEVENDEPENSJON_URL}. "
                            + "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. "
                            + "Om du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, "
                            + "kvardagar kl. 09.00-15.00.",
                    English to "You can find more information at ${Constants.GJENLEVENDEPENSJON_URL}. "
                            + "At ${Constants.KONTAKT_URL} you can chat or write to us. "
                            + "If you do not find the answer at ${Constants.NAV_URL}, you can call us at +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, "
                            + "Monday to Friday from 09:00 to 15:00."
                )
            }
        }
        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }
}