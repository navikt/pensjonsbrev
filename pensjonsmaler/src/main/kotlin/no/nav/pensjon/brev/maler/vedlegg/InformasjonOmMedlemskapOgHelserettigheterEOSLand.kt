package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.common.Constants.HELFO
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELSENORGE
import no.nav.pensjon.brev.maler.fraser.common.Constants.MEDLEMSKAP_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.TELEFON_HELSE
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

// V00009 i metaforce
@TemplateModelHelpers
val vedleggInformasjonOmMedlemskapOgHelserettigheterEOES =
    createAttachment<LangBokmalNynorskEnglish, Unit>(
        title = newText(
            Bokmal to "Informasjon om medlemskap og rett til helsetjenester - for alderspensjonister som flytter til et EØS-land og er omfattet av EØS-avtalens regler om trygd",
            Nynorsk to "Informasjon om medlemskap og rett til helsetenester - for alderspensjonistar som flyttar til eit EØS-land og er omfatta av reglane om trygd i EØS-avtalen",
            English to "Information about membership and entitlement to health services - for retirement pensioners who move to an EEA country and are covered by the EEA Agreement's social security rules",
        ),
        includeSakspart = false
    ) {
        title2 {
            text(
                bokmal { +"Medlemskap i folketrygden" },
                nynorsk { +"Medlemskap i folketrygda" },
                english { +"Membership in the National Insurance Scheme" }
            )
        }
        paragraph {
            text(
                bokmal { +"Selv om du betaler skatt til Norge, er du ikke automatisk medlem i folketrygden. Du kan lese mer om dette på $MEDLEMSKAP_URL." },
                nynorsk { +"Sjølv om du betalar skatt til Noreg, er du ikkje automatisk medlem i folketrygda. Du kan lese meir om dette på $MEDLEMSKAP_URL" },
                english { +"Even if you pay tax to Norway, you are not automatically a member of the National Insurance Scheme. You can read more about this at $MEDLEMSKAP_URL." }
            )
        }

        title2 {
            text(
                bokmal { +"Rett til helsetjenester" },
                nynorsk { +"Rett til helsetenester" },
                english { +"Right to health services" }
            )
        }
        paragraph {
            text(
                bokmal { +"Pensjonister og deres forsørgede familiemedlemmer som flytter til et annet EØS-land har rett til å få dekket utgifter til helsetjenester i det landet de bosetter seg." },
                nynorsk { +"Pensjonistar og deira forsørgde familiemedlemmer som flyttar til eit anna EØS-land har rett til å få dekt utgifter til helsetenester i det landet dei buset seg i." },
                english { +"Pensioners and their dependent family members who move to another EEA country are entitled to reimbursement of healthcare expenses in the country in which they take up residence." }
            )
        }
        paragraph {
            text(
                bokmal { +"Dette gjelder dersom du har pensjon eller uføretrygd fra folketrygden, Statens Pensjonskasse, Pensjonstrygden for sjømenn, Pensjonstrygden for fiskere, Pensjonstrygden for skogsarbeidere og Pensjonsordning for sykepleiere." },
                nynorsk { +"Dette gjeld dersom du har pensjon eller uføretrygd frå folketrygda, Statens pensjonskasse, Pensjonstrygda for sjømenn, Pensjonstrygda for fiskarar, Pensjonstrygda for skogsarbeidarar og Pensjonsordninga for sjukepleiarar." },
                english { +"This applies if you have a pension or disability benefit from the National Insurance Scheme, the Norwegian Public Service Pension Fund, the Pension Insurance for Seamen Scheme, the Pension Insurance for Fishermen Scheme, the Pension Insurance for Forest Workers Scheme and the Pension Scheme for Nurses." }
            )
        }
        paragraph {
            text(
                bokmal { +"For å ha rett til helsetjenester må du bruke blankett E121/S1. Blanketten får du hos $HELFO." },
                nynorsk { +"For å ha rett til helsetenester må du bruke blankett E121/S1. Blanketten får du hjå $HELFO." },
                english { +"To be entitled to health services, you must use form E121/S1. The form can be obtained at $HELFO." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du må kontakte $HELFO dersom du har spørsmål om helsetrygdkort, blankett E 121/S1 og helsetjenester mens du oppholder deg i utlandet." },
                nynorsk { +"Du må kontakte $HELFO dersom du har spørsmål om helsetrygdkort, blankett E 121/S1 og helsetenester medan du oppheld deg i utlandet." },
                english { +"You must contact $HELFO if you have any questions about health benefit cards, form E 121/S1 and health services while you are abroad." }
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { +"Internett: $HELSENORGE." },
                        nynorsk { +"Internett: $HELSENORGE." },
                        english { +"Internet: $HELSENORGE." }
                    )
                }
                item {
                    text(
                        bokmal { +"Telefon: $TELEFON_HELSE" },
                        nynorsk { +"Telefon: $TELEFON_HELSE." },
                        english { +"Telephone: $TELEFON_HELSE" }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { +"Blankett E 121/S1 brukes ikke innen Norden." },
                nynorsk { +"Blankett E 121/S1 blir ikkje brukt i Norden." },
                english { +"Form E 121/S1 is not used within the Nordic region." }
            )
        }

        title2 {
            text(
                bokmal { +"Frivillig medlemskap i folketrygden" },
                nynorsk { +"Frivillig medlemskap i folketrygda." },
                english { +"Voluntary membership in the National Insurance Scheme" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan, på nærmere bestemte vilkår, bli frivillig medlem i folketrygdens helsedel dersom du søker om det. Du kan lese mer om dette på $NAV_URL." },
                nynorsk { +"Du kan, på nærare fastsette vilkår, bli frivillig medlem i helsedelen av folketrygda dersom du søkjer om det. Du kan lese meir om dette på $NAV_URL." },
                english { +"You may, on further specified terms, become a voluntary member of the National Insurance Scheme if you apply for it. You can read more about this at $NAV_URL." }
            )
        }

        title2 {
            text(
                bokmal { +"Trygdeavgift" },
                nynorsk { +"Trygdeavgift" },
                english { +"National insurance contributions" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du skal som hovedregel betale trygdeavgift til Norge av pensjonen din også etter at du har flyttet. Bakgrunnen for dette er at det normalt er Norge som er ansvarlige for dine utgifter til helsetjenester selv om du ikke lenger bor her." },
                nynorsk { +"Som hovudregel skal du betale trygdeavgift til Noreg av pensjonen din også etter at du har flytta. Bakgrunnen for dette er at det vanlegvis er Noreg som har ansvaret for utgiftene dine til helsetenester sjølv om du ikkje bur her lenger." },
                english { +"As a general rule, you will pay national insurance contributions to Norway on your pension even after you have moved. The reason for this is that it is normally Norway that is responsible for your healthcare expenses even if you no longer live here." }
            )
        }
        paragraph {
            text(
                bokmal { +"Trygdeavgift skal du imidlertid kun betale til ett land av gangen. Dersom du også betaler slik avgift i bostedslandet, ber vi deg ta dette opp med trygdemyndighetene der." },
                nynorsk { +"Trygdeavgift skal du kun betale til eitt land av gongen. Dersom du også betalar slik avgift i bustadslandet, ber vi deg ta dette opp med trygdemyndigheitene der." },
                english { +"However, you will only pay national insurance contributions to one country at a time. If you also pay such a contribution in your country of residence, please take this up with the social security authorities there." }
            )
        }

    }