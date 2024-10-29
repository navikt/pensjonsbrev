package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object InnledningPraktiskInformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Du vil motta betalingsinformasjon fra Skatteetaten på én av følgende måter:",
                Nynorsk to "Du får betalingsinformasjon frå Skatteetaten på éin av følgjande måtar:",
                English to "You will receive payment information from the Norwegian Tax Administration in one of the following ways:"
            )
            list {
                item {
                    text(
                        Bokmal to "Du kan få faktura rett i nettbanken. Da må du åpne fakturaen i nettbanken for å lese informasjon om tilbakebetaling.",
                        Nynorsk to "Som faktura rett i nettbanken. Du må då opne fakturaen i nettbanken for å lese informasjonen om tilbakebetaling.",
                        English to "You can receive an invoice directly in your online banking. You need to open the invoice in your online banking to read the repayment information."
                    )
                }
                item {
                    text(
                        Bokmal to "Hvis du ikke har takket ja til eFaktura i nettbanken din, får du fakturaen med betalingsinformasjon i Altinn.",
                        Nynorsk to "Dersom du ikkje har takka ja til eFaktura i nettbanken, får du fakturaen med betalingsinformasjon i Altinn.",
                        English to "If you have not accepted eFaktura in your online banking, you will receive an invoice containing payment information in Altinn."
                    )
                }
                item {
                    text(
                        Bokmal to "Har du reservert deg mot digital post, får du fakturaen i posten.",
                        Nynorsk to "Har du reservert deg mot digital post, får du fakturaen i posten.",
                        English to "If you have opted out of digital mail, you will receive an invoice by regular post."
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
                Nynorsk to "Du kan betale tilbake heile beløpet med ein gong, eller du kan be om å få dele opp beløpet. Hugs at du ikkje kan betale tilbake før du har fått brevet frå Skatteetaten. Dersom du ikkje betaler, kan Skatteetaten trekkje deg i løn etter lova om innkrevjing av bidrag § 11.",
                English to "You can repay the full amount in one payment, or you can request to pay in instalments. Remember that you cannot repay until you have received the letter from the Norwegian Tax Administration. If you fail to make the payment, the Norwegian Tax Administration has the right to make deductions in your wages, ref. Maintenance Recovery Act Section 11."
            )
        }
        title2 {
            text(
                Bokmal to "1. Du kan betale hele beløpet med en gang",
                Nynorsk to "1. Du kan betale heile beløpet med ein gong",
                English to "1. You can pay the full amount at once"
            )
        }
        paragraph {
            text(
                Bokmal to "Du betaler hele beløpet i faktura fra Skatteetaten.",
                Nynorsk to "Du betaler heile beløpet på fakturaen frå Skatteetaten.",
                English to "You pay the full amount on the invoice from the Norwegian Tax Administration."
            )
        }
        title2 {
            text(
                Bokmal to "2. Du kan betale i avdrag",
                Nynorsk to "2. Du kan betale i avdrag",
                English to "2. You can pay back in instalments"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan avtale å betale tilbake beløpet i avdrag. Ta kontakt med Skatteetaten hvis du ønsker å betale i avdrag, eller hvis du ønsker å øke eller redusere trekkprosenten.",
                Nynorsk to "Du kan avtale å betale tilbake beløpet i avdrag. Kontakt Skatteetaten dersom du ønskjer å betale i avdrag, eller dersom du ønskjer å auke eller redusere trekkprosenten.",
                English to "You can arrange to pay back the amount in instalments. Please contact the Norwegian Tax Administration if you wish to pay in instalments, or if you wish to increase or reduce the deduction percentage."
            )
        }
        title2 {
            text(
                Bokmal to "3. Du kan få trekk i støtte fra Nav",
                Nynorsk to "3. Du kan få trekk i støtte frå Nav",
                English to "3. You can have deductions made from benefits from Nav"
            )
        }
        paragraph {
            text(
                Bokmal to "Betaler du ikke hele beløpet, og du fortsatt får uføretrygd eller annen pengestøtte fra Nav, kan Skatteetaten automatisk trekke fra denne støtten. Normalt trekker vi 10 prosent av innvilget uføretrygd frem til hele beløpet er betalt. Vil du betale ned raskere, kan du be oss om å trekke mer enn 10 prosent.",
                Nynorsk to "Viss du ikkje betaler heile beløpet, og du framleis får uføretrygd eller anna pengestøtte frå Nav, kan Skatteetaten automatisk trekkje frå denne støtta. Vi trekkjer normalt sett 10 prosent av innvilga uføretrygd fram til heile beløpet er betalt. Dersom du ønskjer å betale ned raskare, kan du be oss om å trekkje meir enn 10 prosent.",
                English to "If you do not pay the full amount and you are still receiving disability benefits or other financial support from Nav, the Norwegian Tax Administration can automatically deduct from this support. Normally we deduct 10 percent of granted disability benefit until the entire amount is paid. If you wish to pay off faster, you can ask us to deduct more than 10 percent."
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
                English to "If you already have deductions from a previous post-settlement, you can have two deductions."
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
                English to "Income that can be excluded from the post-settlement"
            )
        }
        paragraph {
            text(
                Bokmal to "Har du inntekter som kan holdes utenfor etteroppgjøret som ikke allerede er tatt med i beregningen, må du sende oss dokumentasjon på det i posten innen 3 uker. Eksempler på dette er:",
                Nynorsk to "Dersom du har inntekter som kan haldast utanfor etteroppgjeret som ikkje allereie er teke med i berekningen, må du sende oss dokumentasjon på desse i posten innan 3 veker. Døme på slike inntekter er:",
                English to "If you have income that can be excluded from the settlement and has not already been considered, you must send us documentation within 3 weeks. Examples of this include:"
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
                English to "Examples of documentation for income that can be excluded from the settlement:"
            )
            list {
                item {
                    text(
                        Bokmal to "lønnsslipp",
                        Nynorsk to "lønsslipp ",
                        English to "wage slips"
                    )
                }
                item {
                    text(
                        Bokmal to "dokumentasjon fra regnskapsfører",
                        Nynorsk to "dokumentasjon frå rekneskapsførar",
                        English to "documentation from an accountant"
                    )
                }
                item {
                    text(
                        Bokmal to "kopi av vedtak for erstatning",
                        Nynorsk to "kopi av vedtak om erstatning",
                        English to "copy of the compensation decision"
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Du trenger ikke å klage på vedtaket hvis du kun skal be om at inntekt skal holdes utenfor etteroppgjøret.",
                Nynorsk to "Du treng ikkje å klage på vedtaket dersom du berre skal be om at inntekt blir halden utanfor etteroppgjeret.",
                English to "You do not need to appeal against the decision if you are only requesting that income be excluded from the settlement."
            )
        }
    }
}

object EttersendeDokumentasjon :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Hvordan ettersende dokumentasjon",
                Nynorsk to "Ettersende dokumentasjon",
                English to "Submitting documentation"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan sende oss dokumentasjon både digitalt og på papir.",
                Nynorsk to "Du kan sende oss dokumentasjon både digitalt og på papir.",
                English to "You have the option to submit documentation to us digitally or by post."
            )
        }
        title2 {
            text(
                Bokmal to "Digital ettersendelse",
                Nynorsk to "Digital ettersending",
                English to "Digital submission"
            )
        }
        paragraph {
            text(
                Bokmal to "For å sende dokumentasjon digitalt, gå inn på ${Constants.UFOERE_ETTERSENDE_URL}.",
                Nynorsk to "For å sende dokumentasjon digitalt, går du inn på ${Constants.UFOERE_ETTERSENDE_URL}.",
                English to "To digitally submit information, please visit ${Constants.UFOERE_ETTERSENDE_URL}."
            )
        }
        title2 {
            text(
                Bokmal to "Ettersendelse på papir",
                Nynorsk to "Ettersending på papir",
                English to "Paper submission"
            )
        }
        paragraph {
            text(
                Bokmal to "Når du sender inn dokumenter i posten, må du sende med en førsteside for innsending. Den finner du ved å gå inn på ${Constants.UFOERE_ETTERSENDE_POST_URL}.",
                Nynorsk to "Når du sender dokument i posten, må du leggje ved ei førsteside for innsending. Du finn denne på ${Constants.UFOERE_ETTERSENDE_POST_URL}.",
                English to "When sending documents by regular post, you must include a cover page. You can find the cover page at ${Constants.UFOERE_ETTERSENDE_POST_URL}."
            )
        }
        paragraph {
            text(
                Bokmal to "Når vi har mottatt dokumentasjon fra deg, vil vi vurdere om inntekten skal holdes utenfor. Du får et nytt brev når vi har gjort et nytt etteroppgjør.",
                Nynorsk to "Når vi har fått dokumentasjon frå deg, vil vi vurdere om inntekta skal haldast utanfor. Du får eit nytt brev når vi har utført eit nytt etteroppgjer.",
                English to "Once we have received your documentation, we will assess whether the income should be excluded. You will receive a new letter once we have conducted a new settlement."
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan lese mer om etteroppgjør på ${Constants.ETTEROPPGJOR_URL}.",
                Nynorsk to "Du kan lese meir om etteroppgjer på ${Constants.ETTEROPPGJOR_URL}.",
                English to "For further information on post-settlement, please visit ${Constants.ETTEROPPGJOR_URL}."
            )
        }
    }
}





