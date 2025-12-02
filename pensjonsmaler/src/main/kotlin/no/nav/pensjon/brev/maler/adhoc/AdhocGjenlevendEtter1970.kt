package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.ARBEID_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocGjenlevendEtter1970 : AutobrevTemplate<EmptyAutobrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_ADHOC_2024_VEDTAK_GJENLEVENDETTER1970
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - gjenlevendepensjonen din opphører fra 1. januar 2027",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Gjenlevendepensjonen din opphører fra 1. januar 2027" },
                nynorsk { + "Attlevandepensjonen din blir stansa frå 1. januar 2027" },
                english { + "Your survivor's pension terminates 1 January 2027" }
            )
        }
        outline {
            title1 {
                text(
                    bokmal { + "Vedtak" },
                    nynorsk { + "Vedtak" },
                    english { + "Decision" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Tidligere har vi informert deg om at gjenlevendepensjonen din fra folketrygden blir tidsbegrenset. "
                            + "Det betyr at du fortsatt får gjenlevendepensjon i tre år regnet fra 1. januar 2024." },
                    nynorsk { + "Tidlegare har vi informert deg om at attlevandepensjonen din frå folketrygda blir tidsavgrensa. "
                            + "Det betyr at du framleis får attlevandepensjon i tre år rekna frå 1. januar 2024." },
                    english { + "As we have previously informed you, your survivor’s pension through the National Insurance Act will be time limited. "
                            + "This means you will still receive the pension for three more years as of 1 January 2024." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Retten din til gjenlevendepensjon vil opphøre fra og med 1. januar 2027. Siste mulige utbetaling blir i desember 2026." },
                    nynorsk { + "Retten din til attlevandepensjon blir stansa frå og med 1. januar 2027. Siste moglege utbetaling blir i desember 2026." },
                    english { + "Your entitlement to survivor’s pension terminates 1 January 2027. The last payment will be in December 2026." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 17 A-3 første avsnitt første setning." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 17 A-3 første avsnitt første setning." },
                    english { + "This decision was made pursuant to the provisions of § 17 A-3, first paragraph, first sentence of the National Insurance Act." }
                )
            }
            title1 {
                text(
                    bokmal { + "Kan stønadsperioden forlenges?" },
                    nynorsk { + "Kan stønadsperioden bli forlenga?" },
                    english { + "Can the benefit period be extended?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan søke om å få pensjonen din forlenget med opptil to år. "
                            + "For å få utvidet stønadsperioden må du være under nødvendig utdanning eller ha behov for hjelp til å få jobb fra 1. januar 2027." },
                    nynorsk { + "Du kan søkje om å få pensjonen din forlenga med inntil to år. "
                            + "For å få utvida stønadsperioden må du vere under nødvendig utdanning eller ha behov for hjelp til å få jobb frå 1. januar 2027." },
                    english { + "You can apply to have your pension extended by up to two years. "
                            + "You can qualify for an extended benefit period, if you are still undertaking a necessary education or require help to find employment as of 1 January 2027." }
                )
            }
            title1 {
                text(
                    bokmal { + "Trenger du hjelp til å få jobb eller jobbe mer?" },
                    nynorsk { + "Treng du hjelp til å få jobb eller jobbe meir?" },
                    english { + "Do you need help getting a job or more work?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Nav tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. "
                            + "Du kan finne jobbsøkertips og lenke til ledige jobber på $ARBEID_URL." },
                    nynorsk { + "Nav tilbyr ulike tenester og støtteordningar for deg som treng hjelp til å få jobb. "
                            + "Du kan finne jobbsøkjartips og lenkje til ledige jobbar på $ARBEID_URL." },
                    english { + "The Norwegian Labour and Welfare Administration (Nav) offers various services and support schemes for those who need help finding employment. "
                            + "You can find job search tips and a hyperlink to job vacancies online: $ARBEID_URL." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan få veiledning og støtte fra Nav. Vi kan fortelle deg om ulike muligheter i arbeidsmarkedet "
                            + "eller snakke med deg om nødvendig utdanning eller andre tiltak som kan hjelpe deg med å komme i arbeid." },
                    nynorsk { + "Du kan få rettleiing frå Nav. Vi kan fortelje deg om ulike mogelegheiter i arbeidsmarknaden "
                            + "eller snakke med deg om nødvendig utdanning eller andre tiltak som kan hjelpe deg med å kome i arbeid." },
                    english { + "You can receive guidance from Nav. We can inform you about various opportunities in the job market "
                            + "or discuss necessary education or other measures that can help you find employment." }
                )
            }
            title1 {
                text(
                    bokmal { + "Har du helseutfordringer?" },
                    nynorsk { + "Har du helseutfordringar?" },
                    english { + "Do you have health issues?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan undersøke mulighetene for andre ytelser eller støtteordninger ved ditt lokale Nav-kontor og på $HELSE_URL." },
                    nynorsk { + "Du kan undersøkje om du kan få andre ytingar eller støtteordningar ved det lokale Nav-kontoret ditt og på $HELSE_URL." },
                    english { + "If you have health issues, you can check if you are eligible for other benefits or support schemes through your local Nav office or see $HELSE_URL." }
                )
            }
            title1 {
                text(
                    bokmal { + "Meld fra om endringer" },
                    nynorsk { + "Meld frå om endringar" },
                    english { + "Notify Nav about changes" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du mister retten til gjenlevendepensjon hvis du gifter deg. Du mister også retten "
                            + "hvis du flytter sammen med en du har barn med eller tidligere har vært gift med." },
                    nynorsk { + "Du mistar attlevandepensjonen din om du giftar deg. Du mistar også retten "
                            + "om du flyttar saman med ein du har barn med eller tidlegare har vore gift med." },
                    english { + "You lose the survivor's pension if you marry, "
                            + "or you live with someone you have children with or were previously married to." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Andre endringer som kan ha betydning for hva du får utbetalt:" },
                    nynorsk { + "Andre endringar kan verka inn på kva du får utbetalt:" },
                    english { + "Other changes that may affect your payments:" }
                )
                list {
                    item {
                        text(
                            bokmal { + "endret arbeidsinntekt" },
                            nynorsk { + "endra arbeidsinntekt" },
                            english { + "your income or job situation changes" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "planlegger å flytte til et annet land" },
                            nynorsk { + "planar om å flytte til eit anna land" },
                            english { + "you move to another country" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "blir samboer eller flytter fra samboer" },
                            nynorsk { + "blir sambuar eller flytter frå sambuar" },
                            english { + "you either become a cohabitant or you move away from your cohabitant" }
                        )
                    }
                    item {
                        text(
                            bokmal { + "hvis samboerens inntektsforhold endrer seg." },
                            nynorsk { + "sambuaren si inntekt endrar seg." },
                            english { + "the income of your cohabitant changes" }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "I slike tilfeller må du derfor straks melde fra til Nav." },
                    nynorsk { + "Meld i så fall frå straks til Nav." },
                    english { + "You are obligated to notify Nav as soon as you are aware of any of these changes." }
                )
            }
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
            includePhrase(Felles.HarDuSpoersmaal(Constants.GJENLEVENDEPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }
}