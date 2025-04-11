package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDto
import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDtoSelectors.erEOSLand
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELFO
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELSENORGE
import no.nav.pensjon.brev.maler.fraser.common.Constants.MEDLEMSKAP_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.TELEFON_HELSE
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

// V00009 i metaforce
@TemplateModelHelpers
val vedleggInformasjonOmMedlemskapOgHelserettigheter =
    createAttachment<LangBokmalEnglish, InformasjonOmMedlemskapOgHelserettigheterDto>(
        title = newText(
            Bokmal to "Informasjon om medlemskap og rett til helsetjenester",
            English to "Information about membership and entitlement to health services",
        ),
        includeSakspart = false
    ) {
        showIf(erEOSLand) {
            title1 {
                text(
                    Bokmal to "- for alderspensjonister som flytter til et EØS-land og er omfattet av EØS-avtalens regler om trygd",
                    English to "- for retirement pensioners who move to an EEA country and are covered by the EEA Agreement's social security rules"
                )
            }
            title2 {
                text(
                    Bokmal to "Medlemskap i folketrygden",
                    English to "Membership in the National Insurance Scheme"
                )
            }
            paragraph {
                text(
                    Bokmal to "Selv om du betaler skatt til Norge, er du ikke automatisk medlem i folketrygden. Du kan lese mer om dette på $MEDLEMSKAP_URL.",
                    English to "Even if you pay tax to Norway, you are not automatically a member of the National Insurance Scheme. You can read more about this at $MEDLEMSKAP_URL."
                )
            }

            title2 {
                text(
                    Bokmal to "Rett til helsetjenester",
                    English to "Right to health services"
                )
            }
            paragraph {
                text(
                    Bokmal to "Pensjonister og deres forsørgede familiemedlemmer som flytter til et annet EØS-land har rett til å få dekket utgifter til helsetjenester i det landet de bosetter seg.",
                    English to "Pensioners and their dependent family members who move to another EEA country are entitled to reimbursement of healthcare expenses in the country in which they take up residence."
                )
            }
            paragraph {
                text(
                    Bokmal to "Dette gjelder dersom du har pensjon eller uføretrygd fra folketrygden, Statens Pensjonskasse, Pensjonstrygden for sjømenn, Pensjonstrygden for fiskere, Pensjonstrygden for skogsarbeidere og Pensjonsordning for sykepleiere.",
                    English to "This applies if you have a pension or disability benefit from the National Insurance Scheme, the Norwegian Public Service Pension Fund, the Pension Insurance for Seamen Scheme, the Pension Insurance for Fishermen Scheme, the Pension Insurance for Forest Workers Scheme and the Pension Scheme for Nurses."
                )
            }
            paragraph {
                text(
                    Bokmal to "For å ha rett til helsetjenester må du bruke blankett E121/S1. Blanketten får du hos $HELFO.",
                    English to "To be entitled to health services, you must use form E121/S1. The form can be obtained at $HELFO."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må kontakte $HELFO dersom du har spørsmål om helsetrygdkort, blankett E 121/S1 og helsetjenester mens du oppholder deg i utlandet.",
                    English to "You must contact $HELFO if you have any questions about health benefit cards, form E 121/S1 and health services while you are abroad."
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Bokmal to "Internett: $HELSENORGE.",
                            English to "Internet: $HELSENORGE."
                        )
                    }
                    item {
                        text(
                            Bokmal to "Telefon: $TELEFON_HELSE",
                            English to "Telephone: $TELEFON_HELSE"
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Blankett E 121/S1 brukes ikke innen Norden.",
                    English to "Form E 121/S1 is not used within the Nordic region."
                )
            }

            title2 {
                text(
                    Bokmal to "Frivillig medlemskap i folketrygden",
                    English to "Voluntary membership in the National Insurance Scheme"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan, på nærmere bestemte vilkår, bli frivillig medlem i folketrygdens helsedel dersom du søker om det. Du kan lese mer om dette på $NAV_URL.",
                    English to "You may, on further specified terms, become a voluntary member of the National Insurance Scheme if you apply for it. You can read more about this at $NAV_URL."
                )
            }

            title2 {
                text(
                    Bokmal to "Trygdeavgift",
                    English to "National insurance contributions"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du skal som hovedregel betale trygdeavgift til Norge av pensjonen din også etter at du har flyttet. Bakgrunnen for dette er at det normalt er Norge som er ansvarlige for dine utgifter til helsetjenester selv om du ikke lenger bor her.",
                    English to "As a general rule, you will pay national insurance contributions to Norway on your pension even after you have moved. The reason for this is that it is normally Norway that is responsible for your healthcare expenses even if you no longer live here."
                )
            }
            paragraph {
                text(
                    Bokmal to "Trygdeavgift skal du imidlertid kun betale til ett land av gangen. Dersom du også betaler slik avgift i bostedslandet, ber vi deg ta dette opp med trygdemyndighetene der.",
                    English to "However, you will only pay national insurance contributions to one country at a time. If you also pay such a contribution in your country of residence, please take this up with the social security authorities there."
                )
            }

        }.orShow {
            title1 {
                text(
                    Bokmal to "- for alderspensjonister som bosetter seg utenfor EØS-området",
                    English to "- for retirement pensioners who take up residence outside the EEA"
                )
            }
            title2 {
                text(
                    Bokmal to "Medlemskap i folketrygden",
                    English to "Membership in the National Insurance Scheme"
                )
            }
            paragraph {
                text(
                    Bokmal to "Selv om du betaler skatt til Norge, er du ikke automatisk medlem i folketrygden.",
                    English to "Even if you pay tax to Norway, you are not automatically a member of the National Insurance Scheme."
                )
            }
            paragraph {
                text(
                    Bokmal to "Er du ikke medlem, får du ikke dekket utgiftene du har til helsetjenester, verken når du oppholder deg i utlandet eller når du er i Norge.",
                    English to "If you are not a member, your healthcare expenses will not be reimbursed, either while staying abroad or while you are in Norway."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan søke om frivillig medlemskap i folketrygdens helsedel. Du finner søknadsskjema på $MEDLEMSKAP_URL.",
                    English to "You can apply for voluntary membership of the National Insurance Scheme’s health component. You can find the application form at $MEDLEMSKAP_URL."
                )
            }

            title2 {
                text(
                    Bokmal to "Trygdeavgift",
                    English to "National insurance contributions"
                )
            }
            paragraph {
                text(
                    Bokmal to "En pensjonist som blir frivillig medlem må betale avgift til Nav.",
                    English to "A pensioner who becomes a voluntary member must pay a contribution to Nav."
                )
            }
            paragraph {
                text(
                    Bokmal to "I 2020 er avgiftssatsen 9,1 prosent av pensjonen dersom du ikke betaler skatt til Norge. Dersom du betaler skatt til Norge, er avgiftssatsen 2,3 prosent.",
                    English to "In 2020, the tax rate is 9.1 per cent of your pension if you do not pay tax to Norway. If you pay tax to Norway, the tax rate is 2.3 per cent."
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom du betaler kildeskatt skal du betale en avgift på 7,4 prosent av avgiftsgrunnlaget.",
                    English to "If you pay withholding tax, you must pay a contribution of 7.4 per cent of the contribution basis."
                )
            }

            title2 {
                text(
                    Bokmal to "Spørsmål om helsetjenester",
                    English to "Questions about health services"
                )
            }
            paragraph {
                text(
                    Bokmal to "Har du spørsmål om helsetjenester må du kontakte $HELFO.",
                    English to "If you have any questions about health services, please contact $HELFO."
                )
                list {
                    item {
                        text(
                            Bokmal to "Internett: $HELSENORGE.",
                            English to "Internet: $HELSENORGE."
                        )
                    }
                    item {
                        text(
                            Bokmal to "Telefon: $TELEFON_HELSE",
                            English to "Telephone: $TELEFON_HELSE"
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Kontaktinformasjon NAV Medlemskap og avgift // Postadresse: Postboks 6600 Etterstad. // N-0607 Oslo",
                    English to "Contact information NAV Membership and contribution // Postal address: P.O. Box 6600 Etterstad. // N-0607 Oslo"
                )
            }
            paragraph {
                text(
                    Bokmal to "Telefon: +47 21 07 37 00 // Fax: +47 21 06 91 01",
                    English to "Phone: +47 21 07 37 00 // Fax: +47 21 06 91 01"
                )
            }
            paragraph {
                text(
                    Bokmal to NAV_URL,
                    English to NAV_URL
                )
            }
        }
    }