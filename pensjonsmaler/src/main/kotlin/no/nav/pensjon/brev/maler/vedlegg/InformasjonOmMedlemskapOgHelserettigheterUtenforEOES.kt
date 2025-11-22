package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
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
val vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggData>(
        title = newText(
            Bokmal to "Informasjon om medlemskap og rett til helsetjenester - for alderspensjonister som bosetter seg utenfor EØS-området",
            Nynorsk to "Informasjon om medlemskap og rett til helsetenester - for alderspensjonistar som buset seg utanfor EØS-området",
            English to "Information about membership and entitlement to health services - for retirement pensioners who take up residence outside the EEA",
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
                bokmal { +"Selv om du betaler skatt til Norge, er du ikke automatisk medlem i folketrygden." },
                nynorsk { +"Sjølv om du betalar skatt til Noreg, er du ikkje automatisk medlem i folketrygda." },
                english { +"Even if you pay tax to Norway, you are not automatically a member of the National Insurance Scheme." }
            )
        }
        paragraph {
            text(
                bokmal { +"Er du ikke medlem, får du ikke dekket utgiftene du har til helsetjenester, verken når du oppholder deg i utlandet eller når du er i Norge." },
                nynorsk { +"Er du ikkje medlem, får du ikkje dekt utgiftene dine til helsetenester, verken når du oppheld deg i utlandet eller når du er i Noreg." },
                english { +"If you are not a member, your healthcare expenses will not be reimbursed, either while staying abroad or while you are in Norway." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan søke om frivillig medlemskap i folketrygdens helsedel. Du finner søknadsskjema på $MEDLEMSKAP_URL." },
                nynorsk { +"Du kan søkje om frivillig medlemskap i helsedelen av folketrygda. Du finn søknadsskjema på $MEDLEMSKAP_URL." },
                english { +"You can apply for voluntary membership of the National Insurance Scheme’s health component. You can find the application form at $MEDLEMSKAP_URL." }
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
                bokmal { +"En pensjonist som blir frivillig medlem må betale avgift til Nav." },
                nynorsk { +"Ein pensjonist som blir frivillig medlem må betale avgift til Nav." },
                english { +"A pensioner who becomes a voluntary member must pay a contribution to Nav." }
            )
        }
        paragraph {
            text(
                bokmal { +"I 2020 er avgiftssatsen 9,1 prosent av pensjonen dersom du ikke betaler skatt til Norge. Dersom du betaler skatt til Norge, er avgiftssatsen 2,3 prosent." },
                nynorsk { +"I 2020 er avgiftssatsen 9,1 prosent av pensjonen dersom du ikkje betalar skatt til Noreg. Dersom du betalar skatt til Noreg, er avgiftssatsen 2,3 prosent." },
                english { +"In 2020, the tax rate is 9.1 per cent of your pension if you do not pay tax to Norway. If you pay tax to Norway, the tax rate is 2.3 per cent." }
            )
        }
        paragraph {
            text(
                bokmal { +"Dersom du betaler kildeskatt skal du betale en avgift på 7,4 prosent av avgiftsgrunnlaget." },
                nynorsk { +"Dersom du betalar kjeldeskatt, skal du betale ei avgift på 7,4 prosent av avgiftsgrunnlaget." },
                english { +"If you pay withholding tax, you must pay a contribution of 7.4 per cent of the contribution basis." }
            )
        }

        title2 {
            text(
                bokmal { +"Spørsmål om helsetjenester" },
                nynorsk { +"Spørsmål om helsetenester" },
                english { +"Questions about health services" }
            )
        }
        paragraph {
            text(
                bokmal { +"Har du spørsmål om helsetjenester må du kontakte $HELFO." },
                nynorsk { +"Har du spørsmål om helsetenester, må du kontakte $HELFO." },
                english { +"If you have any questions about health services, please contact $HELFO." }
            )
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
                        nynorsk { +"Telefon: $TELEFON_HELSE" },
                        english { +"Telephone: $TELEFON_HELSE" }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { +"Kontaktinformasjon NAV Medlemskap og avgift // Postadresse: Postboks 6600 Etterstad. // N-0607 Oslo" },
                nynorsk { +"Kontaktinformasjon NAV Medlemskap og avgift // Postadresse: Postboks 6600 Etterstad. // N-0607 Oslo" },
                english { +"Contact information NAV Membership and contribution // Postal address: P.O. Box 6600 Etterstad. // N-0607 Oslo" }
            )
        }
        paragraph {
            text(
                bokmal { +"Telefon: +47 21 07 37 00 // Fax: +47 21 06 91 01" },
                nynorsk { +"Telefon: +47 21 07 37 00 // Fax: +47 21 06 91 01" },
                english { +"Phone: +47 21 07 37 00 // Fax: +47 21 06 91 01" }
            )
        }
        paragraph {
            text(
                bokmal { +NAV_URL },
                nynorsk { +NAV_URL },
                english { +NAV_URL }
            )
        }
    }