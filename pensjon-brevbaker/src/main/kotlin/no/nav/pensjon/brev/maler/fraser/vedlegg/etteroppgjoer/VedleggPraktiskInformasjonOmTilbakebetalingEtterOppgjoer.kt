package no.nav.pensjon.brev.maler.fraser.vedlegg.etteroppgjoer

import no.nav.pensjon.brev.api.prodAutobrevTemplates
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

object InnledningPraktiskInformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Du vil motta betalingsinformasjon fra NAV Innkreving på én av følgende måter:",
                Nynorsk to "Du får betalingsinformasjon frå NAV Innkrevjing på éin av følgjande måtar:",
                English to "You will receive payment information from NAV Collection Agency via one of the following methods:"
            )
            list {
                item {
                    text(
                        Bokmal to "Du kan få faktura rett i nettbanken. Da må du åpne fakturaen i nettbanken for å lese informasjon om tilbakebetaling.",
                        Nynorsk to "Som faktura rett i nettbanken. Du må då opne fakturaen i nettbanken for å lese informasjonen om tilbakebetaling.",
                        English to "You can receive an invoice directly in your online bank account. In such case, you must open the invoice in your online bank account to read information concerning repayment."
                    )
                }
                item {
                    text(
                        Bokmal to "Hvis du ikke har takket ja til eFaktura i nettbanken din, får du brevet med betalingsinformasjon i Altinn.",
                        Nynorsk to "Dersom du ikkje har takka ja til eFaktura i nettbanken, får du brevet med betalingsinformasjon i Altinn.",
                        English to "If you have not accepted eFaktura in your online bank account, you will receive a letter containing payment information in Altinn."
                    )
                }
                item {
                    text(
                        Bokmal to "Har du reservert deg mot digital post, får du brevet i posten.",
                        Nynorsk to "Har du reservert deg mot digital post, får du brevet i posten.",
                        English to "If you have opted out of digital mail, you will receive a letter by regular post."
                    )
                }
            }
        }
    }
}

object SlikBetalerDuTilbake :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Slik betaler du tilbake",
                Nynorsk to "Slik betaler du tilbake",
                English to "How to repay"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan betale tilbake hele beløpet med en gang, eller du kan be om å få dele opp beløpet. Husk at du ikke kan betale tilbake før du har fått brevet fra Skatteetaten. Hvis du ikke betaler, har Skatteetaten mulighet til å trekke deg i lønn etter bidragsinnkrevingsloven § 11.",
                Nynorsk to "Du kan betale tilbake heile beløpet med ein gong, eller du kan be om å få dele opp beløpet. Hugs at du ikkje kan betale tilbake før du har fått brevet frå Innkrevingssentralen for bidrag og tilbakebetalingskrav. Dersom du ikkje betaler, kan Innkrevingssentralen trekkje deg i løn etter lova om innkrevjing av bidrag § 11.",
                English to "You can repay the entire sum in one payment, or you can request to pay in instalments. Remember: you cannot repay before you have received a letter from the National Collections Agency for maintenance and repayment claims. If you fail to repay, the Norwegian Tax Administration has the right to make deductions in your wages, ref. Maintenance Recovery Act Section 11."
            )
        }
        title2 {
            text(
                Bokmal to "1. Du kan betale hele beløpet med en gang",
                Nynorsk to "1. Du kan betale heile beløpet med ein gong",
                English to "1. You can pay the entire sum in one payment"
            )
        }
        paragraph {
            text(
                Bokmal to "Du betaler hele beløpet i faktura fra Skatteetaten for bidrag og tilbakebetalingskrav.",
                Nynorsk to "Du betaler heile beløpet på fakturaen frå Innkrevingssentralen for bidrag og tilbakebetalingskrav.",
                English to "You pay the entire sum in the invoice from the Norwegian Tax Administration for maintenance and repayment claims."
            )
        }
        title2 {
            text(
                Bokmal to "2. Du kan betale i avdrag",
                Nynorsk to "2. Du kan betale i avdrag",
                English to "2. You can pay in instalments"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan avtale å betale tilbake beløpet i avdrag. Ta kontakt med Innkrevingssentralen for bidrag og tilbakebetalingskrav hvis du ønsker å betale i avdrag, eller hvis du ønsker å øke eller redusere trekkprosenten.",
                Nynorsk to "Du kan avtale å betale tilbake beløpet i avdrag. Kontakt Innkrevingssentralen for bidrag og tilbakebetalingskrav dersom du ønskjer å betale i avdrag, eller dersom du ønskjer å auke eller redusere trekkprosenten.",
                English to "You can agree to pay back the sum in instalments. Please contact the National Collections Agency for maintenance and repayment claims if you wish to pay in instalments, or if you wish to increase or reduce the deduction percentage."
            )
        }
        title2 {
            text(
                Bokmal to "3. Du kan få trekk i støtte fra NAV",
                Nynorsk to "3. Du kan få trekk i støtte frå NAV",
                English to "3. You can have deductions made from benefits from NAV "
            )
        }
        paragraph {
            text(
                Bokmal to "Betaler du ikke hele beløpet, og du fortsatt får uføretrygd eller annen pengestøtte fra NAV, kan Skatteetaten automatisk trekke fra denne støtten. Normalt trekker vi 10 prosent av innvilget uføretrygd frem til hele beløpet er betalt. Vil du betale ned raskere, kan du be oss om å trekke mer enn 10 prosent.",
                Nynorsk to "Viss du ikkje betaler heile beløpet, og du framleis får uføretrygd eller anna pengestøtte frå NAV, kan Skatteetaten automatisk trekkje frå denne støtta. Vi trekkjer normalt sett 10 prosent av innvilga uføretrygd fram til heile beløpet er betalt. Dersom du ønskjer å betale ned raskare, kan du be oss om å trekkje meir enn 10 prosent.",
                English to "If you do not repay the entire sum and you continue to receive disability benefit or other financial support from NAV, the National Tax Administration can automatically make deductions from these benefits. Normally we deduct 10 percent of granted disability benefit until the entire sum is paid. If you wish to repay more quickly, you can ask us to deduct more than 10 percent."
            )
        }
        paragraph {
            text(
                Bokmal to "Trekket blir gjort etter at skatten er trukket fra, men før eventuelle andre trekk.",
                Nynorsk to "Trekket blir gjort etter at skatten er trekt frå, men før eventuelle andre trekk.",
                English to "Deductions will be made after tax is deducted, but before any other deductions."
            )
        }
        paragraph {
            text(
                Bokmal to "Har du allerede trekk fra forrige etteroppgjør, kan du få to trekk.",
                Nynorsk to "Viss du allereie har trekk frå førre etteroppgjer, kan du få to trekk.",
                English to "If you already have deductions from a previous settlement, you can have two deductions."
            )
        }
    }
}

object InntekterSomKanHoldesUtenforEtteroppgjoeret :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Inntekter som kan holdes utenfor etteroppgjøret",
                Nynorsk to "Inntekter som kan haldast utanfor etteroppgjeret",
                English to "Income that is kept outside of the settlement"
            )
        }
        paragraph {
            text(
                Bokmal to "Har du inntekter som kan holdes utenfor etteroppgjøret, må du sende oss dokumentasjon på det i posten innen 3 uker. Eksempler på dette er:",
                Nynorsk to "Dersom du har inntekter som kan haldast utanfor etteroppgjeret, må du sende oss dokumentasjon på desse i posten innan 3 veker. Døme på slike inntekter er:",
                English to "If you have income that can be kept outside of the settlement, you must send us documentation of this income by regular post, within 3 weeks. Examples of this are:"
            )
        }
        paragraph {
            list {
                item {
                    text(
                        Bokmal to "Inntekt fra arbeid eller utbetaling fra en virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel feriepenger.",
                        Nynorsk to "Inntekt frå arbeid eller utbetaling frå verksemd som blei heilt avslutta før du fekk innvilga uføretrygd (t.d. feriepengar).",
                        English to "Income from work or payments from an enterprise that was fully concluded before you were granted disability benefit, for example holiday pay."
                    )
                }
                item {
                    text(
                        Bokmal to "Erstatningsoppgjør etter skadeerstatningsloven § 3-1 (også voldsoffererstatning), yrkesskadeloven § 13, og pasientskadeloven § 4 første ledd.",
                        Nynorsk to "Erstatningsoppgjer etter lova om skadeserstatning § 3-1 (også valdsoffererstatning), yrkesskadelova § 13, og pasientskadelova § 4 første ledd.",
                        English to "Compensation payments made according to the Compensation Act Section 3-1 (including compensation for victims of violent crime), Occupational Injury Act Section 13, and the Patient Injury Act Section 4 par. 1."
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Eksempler på dokumentasjon for inntekter som kan holdes utenfor etteroppgjøret:",
                Nynorsk to "Døme på dokumentasjon for inntekter som kan haldast utanfor etteroppgjeret:",
                English to "Examples of documentation of incomes that can be kept outside of the settlement:"
            )
            list {
                item {
                    text(
                        Bokmal to "Lønnsslip",
                        Nynorsk to "Lønsslipp ",
                        English to "Wage slips"
                    )
                }
                item {
                    text(
                        Bokmal to "Dokumentasjon fra regnskapsfører",
                        Nynorsk to "Dokumentasjon frå rekneskapsførar",
                        English to "Documentation from an accountant"
                    )
                }
                item {
                    text(
                        Bokmal to "Kopi av vedtak for erstatning ",
                        Nynorsk to "Kopi av vedtak om erstatning",
                        English to "Copy of the decision regarding compensation"
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Du trenger ikke å klage på vedtaket hvis du kun skal be om at inntekt skal holdes utenfor etteroppgjøret.",
                Nynorsk to "Du treng ikkje å klage på vedtaket dersom du berre skal be om at inntekt blir halden utanfor etteroppgjeret.",
                English to "You do not need to appeal against the decision if you are only requesting that income shall be kept outside of the settlement."
            )
        }
    }
}

object EttersendeDokumentasjon :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Ettersende dokumentasjon",
                Nynorsk to "Ettersende dokumentasjon",
                English to "Forwarding documentation"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan sende oss dokumentasjon både digitalt og på papir.",
                Nynorsk to "Du kan sende oss dokumentasjon både digitalt og på papir.",
                English to "You can submit documentation to us both digitally and on paper."
            )
        }
        title2 {
            text(
                Bokmal to "Digital ettersendelse",
                Nynorsk to "Digital ettersending",
                English to "Sending digitally"
            )
        }
        paragraph {
            text(
                Bokmal to "For å sende dokumentasjon digitalt, gå inn på nav.no/uføre-ettersende.",
                Nynorsk to "For å sende dokumentasjon digitalt går du inn på nav.no/uføre-ettersende.",
                English to "To submit information digitally, go to: nav.no/uføre-ettersende."
            )
        }
        title2 {
            text(
                Bokmal to "Ettersendelse på papir",
                Nynorsk to "Ettersending på papir",
                English to "Sending paper documentation "
            )
        }
        paragraph {
            text(
                Bokmal to "Når du sender inn dokumenter i posten, må du sende med en førsteside for innsending. Den finner du ved å gå inn på nav.no/uføre-ettersende-post.",
                Nynorsk to "Når du sender dokument i posten, må du leggje ved ei førsteside for innsending. Du finn denne på nav.no/uføre-ettersende-post.",
                English to "When you send in documents by regular post, you must include a cover letter. You can find this at: nav.no/uføre-ettersende-post."
            )
        }
        paragraph {
            text(
                Bokmal to "Når vi har mottatt dokumentasjon fra deg, vil vi vurdere om inntekten skal holdes utenfor. Du får et nytt brev når vi har gjort et nytt etteroppgjør.",
                Nynorsk to "Når vi har fått dokumentasjon frå deg, vil vi vurdere om inntekta skal haldast utanfor. Du får eit nytt brev når vi har utført eit nytt etteroppgjer.",
                English to "When you send in documents by regular post, you must include a cover letter. You can find this at: nav.no/uføre-ettersende-post."
            )
        }
        paragraph {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan lese mer om etteroppgjør på nav.no/etteroppgjor.",
                Nynorsk to "Du kan lese meir om etteroppgjer på nav.no/etteroppgjor.",
                English to "Du kan lese mer om etteroppgjør på nav.no/etteroppgjor."
            )
        }
    }
}





