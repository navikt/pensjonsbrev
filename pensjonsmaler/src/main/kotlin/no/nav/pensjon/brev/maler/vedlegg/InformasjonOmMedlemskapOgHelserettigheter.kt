package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDto
import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDtoSelectors.erEOSLand
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELFO
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELSENORGE
import no.nav.pensjon.brev.maler.fraser.common.Constants.MEDLEMSKAP_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.TELEFON_HELSE
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

// V00009 i metaforce
@TemplateModelHelpers
val vedleggInformasjonOmMedlemskapOgHelserettigheter =
    createAttachment<LangBokmalNynorskEnglish, InformasjonOmMedlemskapOgHelserettigheterDto>(
        title = newText(
            Bokmal to "Informasjon om medlemskap og rett til helsetjenester",
            Nynorsk to "Informasjon om medlemskap og rett til helsetenester",
            English to "Information about membership and entitlement to health services",
        ),
        includeSakspart = false
    ) {
        showIf(erEOSLand) {
            title1 {
                text(
                    bokmal { + "- for alderspensjonister som flytter til et EØS-land og er omfattet av EØS-avtalens regler om trygd" },
                    nynorsk { + "- for alderspensjonistar som flyttar til eit EØS-land og er omfatta av reglane om trygd i EØS-avtalen" },
                    english { + "- for retirement pensioners who move to an EEA country and are covered by the EEA Agreement's social security rules" }
                )
            }
            title2 {
                text(
                    bokmal { + "Medlemskap i folketrygden" },
                    nynorsk { + "Medlemskap i folketrygda" },
                    english { + "Membership in the National Insurance Scheme" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Selv om du betaler skatt til Norge, er du ikke automatisk medlem i folketrygden. Du kan lese mer om dette på $MEDLEMSKAP_URL." },
                    nynorsk { + "Sjølv om du betalar skatt til Noreg, er du ikkje automatisk medlem i folketrygda. Du kan lese meir om dette på $MEDLEMSKAP_URL" },
                    english { + "Even if you pay tax to Norway, you are not automatically a member of the National Insurance Scheme. You can read more about this at $MEDLEMSKAP_URL." }
                )
            }

            title2 {
                text(
                    bokmal { + "Rett til helsetjenester" },
                    nynorsk { + "Rett til helsetenester" },
                    english { + "Right to health services" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Pensjonister og deres forsørgede familiemedlemmer som flytter til et annet EØS-land har rett til å få dekket utgifter til helsetjenester i det landet de bosetter seg." },
                    nynorsk { + "Pensjonistar og deira forsørgde familiemedlemmer som flyttar til eit anna EØS-land har rett til å få dekt utgifter til helsetenester i det landet dei buset seg i." },
                    english { + "Pensioners and their dependent family members who move to another EEA country are entitled to reimbursement of healthcare expenses in the country in which they take up residence." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Dette gjelder dersom du har pensjon eller uføretrygd fra folketrygden, Statens Pensjonskasse, Pensjonstrygden for sjømenn, Pensjonstrygden for fiskere, Pensjonstrygden for skogsarbeidere og Pensjonsordning for sykepleiere." },
                    nynorsk { + "Dette gjeld dersom du har pensjon eller uføretrygd frå folketrygda, Statens pensjonskasse, Pensjonstrygda for sjømenn, Pensjonstrygda for fiskarar, Pensjonstrygda for skogsarbeidarar og Pensjonsordninga for sjukepleiarar." },
                    english { + "This applies if you have a pension or disability benefit from the National Insurance Scheme, the Norwegian Public Service Pension Fund, the Pension Insurance for Seamen Scheme, the Pension Insurance for Fishermen Scheme, the Pension Insurance for Forest Workers Scheme and the Pension Scheme for Nurses." }
                )
            }
            paragraph {
                text(
                    bokmal { + "For å ha rett til helsetjenester må du bruke blankett E121/S1. Blanketten får du hos $HELFO." },
                    nynorsk { + "For å ha rett til helsetenester må du bruke blankett E121/S1. Blanketten får du hjå $HELFO." },
                    english { + "To be entitled to health services, you must use form E121/S1. The form can be obtained at $HELFO." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du må kontakte $HELFO dersom du har spørsmål om helsetrygdkort, blankett E 121/S1 og helsetjenester mens du oppholder deg i utlandet." },
                    nynorsk { + "Du må kontakte $HELFO dersom du har spørsmål om helsetrygdkort, blankett E 121/S1 og helsetenester medan du oppheld deg i utlandet." },
                    english { + "You must contact $HELFO if you have any questions about health benefit cards, form E 121/S1 and health services while you are abroad." }
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { + "Internett: $HELSENORGE." },
                            nynorsk { + "Internett: $HELSENORGE." },
                            english { + "Internet: $HELSENORGE." }
                        )
                    }
                    item {
                        text(
                            bokmal { + "Telefon: $TELEFON_HELSE" },
                            nynorsk { + "Telefon: $TELEFON_HELSE." },
                            english { + "Telephone: $TELEFON_HELSE" }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "Blankett E 121/S1 brukes ikke innen Norden." },
                    nynorsk { + "Blankett E 121/S1 blir ikkje brukt i Norden." },
                    english { + "Form E 121/S1 is not used within the Nordic region." }
                )
            }

            title2 {
                text(
                    bokmal { + "Frivillig medlemskap i folketrygden" },
                    nynorsk { + "Frivillig medlemskap i folketrygda." },
                    english { + "Voluntary membership in the National Insurance Scheme" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan, på nærmere bestemte vilkår, bli frivillig medlem i folketrygdens helsedel dersom du søker om det. Du kan lese mer om dette på $NAV_URL." },
                    nynorsk { + "Du kan, på nærare fastsette vilkår, bli frivillig medlem i helsedelen av folketrygda dersom du søkjer om det. Du kan lese meir om dette på $NAV_URL." },
                    english { + "You may, on further specified terms, become a voluntary member of the National Insurance Scheme if you apply for it. You can read more about this at $NAV_URL." }
                )
            }

            title2 {
                text(
                    bokmal { + "Trygdeavgift" },
                    nynorsk { + "Trygdeavgift" },
                    english { + "National insurance contributions" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du skal som hovedregel betale trygdeavgift til Norge av pensjonen din også etter at du har flyttet. Bakgrunnen for dette er at det normalt er Norge som er ansvarlige for dine utgifter til helsetjenester selv om du ikke lenger bor her." },
                    nynorsk { + "Som hovudregel skal du betale trygdeavgift til Noreg av pensjonen din også etter at du har flytta. Bakgrunnen for dette er at det vanlegvis er Noreg som har ansvaret for utgiftene dine til helsetenester sjølv om du ikkje bur her lenger." },
                    english { + "As a general rule, you will pay national insurance contributions to Norway on your pension even after you have moved. The reason for this is that it is normally Norway that is responsible for your healthcare expenses even if you no longer live here." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Trygdeavgift skal du imidlertid kun betale til ett land av gangen. Dersom du også betaler slik avgift i bostedslandet, ber vi deg ta dette opp med trygdemyndighetene der." },
                    nynorsk { + "Trygdeavgift skal du kun betale til eitt land av gongen. Dersom du også betalar slik avgift i bustadslandet, ber vi deg ta dette opp med trygdemyndigheitene der." },
                    english { + "However, you will only pay national insurance contributions to one country at a time. If you also pay such a contribution in your country of residence, please take this up with the social security authorities there." }
                )
            }

        }.orShow {
            title1 {
                text(
                    bokmal { + "- for alderspensjonister som bosetter seg utenfor EØS-området" },
                    nynorsk { + "- for alderspensjonistar som buset seg utanfor EØS-området." },
                    english { + "- for retirement pensioners who take up residence outside the EEA" }
                )
            }
            title2 {
                text(
                    bokmal { + "Medlemskap i folketrygden" },
                    nynorsk { + "Medlemskap i folketrygda" },
                    english { + "Membership in the National Insurance Scheme" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Selv om du betaler skatt til Norge, er du ikke automatisk medlem i folketrygden." },
                    nynorsk { + "Sjølv om du betalar skatt til Noreg, er du ikkje automatisk medlem i folketrygda." },
                    english { + "Even if you pay tax to Norway, you are not automatically a member of the National Insurance Scheme." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Er du ikke medlem, får du ikke dekket utgiftene du har til helsetjenester, verken når du oppholder deg i utlandet eller når du er i Norge." },
                    nynorsk { + "Er du ikkje medlem, får du ikkje dekt utgiftene dine til helsetenester, verken når du oppheld deg i utlandet eller når du er i Noreg." },
                    english { + "If you are not a member, your healthcare expenses will not be reimbursed, either while staying abroad or while you are in Norway." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan søke om frivillig medlemskap i folketrygdens helsedel. Du finner søknadsskjema på $MEDLEMSKAP_URL." },
                    nynorsk { + "Du kan søkje om frivillig medlemskap i helsedelen av folketrygda. Du finn søknadsskjema på $MEDLEMSKAP_URL." },
                    english { + "You can apply for voluntary membership of the National Insurance Scheme’s health component. You can find the application form at $MEDLEMSKAP_URL." }
                )
            }

            title2 {
                text(
                    bokmal { + "Trygdeavgift" },
                    nynorsk { + "Trygdeavgift" },
                    english { + "National insurance contributions" }
                )
            }
            paragraph {
                text(
                    bokmal { + "En pensjonist som blir frivillig medlem må betale avgift til Nav." },
                    nynorsk { + "Ein pensjonist som blir frivillig medlem må betale avgift til Nav." },
                    english { + "A pensioner who becomes a voluntary member must pay a contribution to Nav." }
                )
            }
            paragraph {
                text(
                    bokmal { + "I 2020 er avgiftssatsen 9,1 prosent av pensjonen dersom du ikke betaler skatt til Norge. Dersom du betaler skatt til Norge, er avgiftssatsen 2,3 prosent." },
                    nynorsk { + "I 2020 er avgiftssatsen 9,1 prosent av pensjonen dersom du ikkje betalar skatt til Noreg. Dersom du betalar skatt til Noreg, er avgiftssatsen 2,3 prosent." },
                    english { + "In 2020, the tax rate is 9.1 per cent of your pension if you do not pay tax to Norway. If you pay tax to Norway, the tax rate is 2.3 per cent." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Dersom du betaler kildeskatt skal du betale en avgift på 7,4 prosent av avgiftsgrunnlaget." },
                    nynorsk { + "Dersom du betalar kjeldeskatt, skal du betale ei avgift på 7,4 prosent av avgiftsgrunnlaget." },
                    english { + "If you pay withholding tax, you must pay a contribution of 7.4 per cent of the contribution basis." }
                )
            }

            title2 {
                text(
                    bokmal { + "Spørsmål om helsetjenester" },
                    nynorsk { + "Spørsmål om helsetenester" },
                    english { + "Questions about health services" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du spørsmål om helsetjenester må du kontakte $HELFO." },
                    nynorsk { + "Har du spørsmål om helsetenester, må du kontakte $HELFO." },
                    english { + "If you have any questions about health services, please contact $HELFO." }
                )
                list {
                    item {
                        text(
                            bokmal { + "Internett: $HELSENORGE." },
                            nynorsk { + "Internett: $HELSENORGE." },
                            english { + "Internet: $HELSENORGE." }
                        )
                    }
                    item {
                        text(
                            bokmal { + "Telefon: $TELEFON_HELSE" },
                            nynorsk { + "Telefon: $TELEFON_HELSE" },
                            english { + "Telephone: $TELEFON_HELSE" }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "Kontaktinformasjon NAV Medlemskap og avgift // Postadresse: Postboks 6600 Etterstad. // N-0607 Oslo" },
                    nynorsk { + "Kontaktinformasjon NAV Medlemskap og avgift // Postadresse: Postboks 6600 Etterstad. // N-0607 Oslo" },
                    english { + "Contact information NAV Membership and contribution // Postal address: P.O. Box 6600 Etterstad. // N-0607 Oslo" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Telefon: +47 21 07 37 00 // Fax: +47 21 06 91 01" },
                    nynorsk { + "Telefon: +47 21 07 37 00 // Fax: +47 21 06 91 01" },
                    english { + "Phone: +47 21 07 37 00 // Fax: +47 21 06 91 01" }
                )
            }
            paragraph {
                text(
                    bokmal { + NAV_URL },
                    nynorsk { + NAV_URL },
                    english { + NAV_URL }
                )
            }
        }
    }