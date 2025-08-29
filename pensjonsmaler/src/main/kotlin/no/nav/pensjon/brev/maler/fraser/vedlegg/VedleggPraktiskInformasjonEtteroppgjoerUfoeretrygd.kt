package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object InnledningPraktiskInformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Du vil motta betalingsinformasjon fra Skatteetaten på én av følgende måter:" },
                nynorsk { + "Du får betalingsinformasjon frå Skatteetaten på éin av følgjande måtar:" },
                english { + "You will receive payment information from the Norwegian Tax Administration in one of the following ways:" }
            )
            list {
                item {
                    text(
                        bokmal { + "Du kan få faktura rett i nettbanken. Da må du åpne fakturaen i nettbanken for å lese informasjon om tilbakebetaling." },
                        nynorsk { + "Som faktura rett i nettbanken. Du må då opne fakturaen i nettbanken for å lese informasjonen om tilbakebetaling." },
                        english { + "You can receive an invoice directly in your online banking. You need to open the invoice in your online banking to read the repayment information." }
                    )
                }
                item {
                    text(
                        bokmal { + "Hvis du ikke har takket ja til eFaktura i nettbanken din, får du fakturaen med betalingsinformasjon i Altinn." },
                        nynorsk { + "Dersom du ikkje har takka ja til eFaktura i nettbanken, får du fakturaen med betalingsinformasjon i Altinn." },
                        english { + "If you have not accepted eFaktura in your online banking, you will receive an invoice containing payment information in Altinn." }
                    )
                }
                item {
                    text(
                        bokmal { + "Har du reservert deg mot digital post, får du fakturaen i posten." },
                        nynorsk { + "Har du reservert deg mot digital post, får du fakturaen i posten." },
                        english { + "If you have opted out of digital mail, you will receive an invoice by regular post." }
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
                bokmal { + "Slik betaler du tilbake" },
                nynorsk { + "Slik betaler du tilbake" },
                english { + "How to repay" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du kan betale tilbake hele beløpet med en gang, eller du kan be om å få dele opp beløpet. Husk at du ikke kan betale tilbake før du har fått brevet fra Skatteetaten. Hvis du ikke betaler, har Skatteetaten mulighet til å trekke deg i lønn etter bidragsinnkrevingsloven § 11." },
                nynorsk { + "Du kan betale tilbake heile beløpet med ein gong, eller du kan be om å få dele opp beløpet. Hugs at du ikkje kan betale tilbake før du har fått brevet frå Skatteetaten. Dersom du ikkje betaler, kan Skatteetaten trekkje deg i løn etter lova om innkrevjing av bidrag § 11." },
                english { + "You can repay the full amount in one payment, or you can request to pay in instalments. Remember that you cannot repay until you have received the letter from the Norwegian Tax Administration. If you fail to make the payment, the Norwegian Tax Administration has the right to make deductions in your wages, ref. Maintenance Recovery Act Section 11." }
            )
        }
        title2 {
            text(
                bokmal { + "1. Du kan betale hele beløpet med en gang" },
                nynorsk { + "1. Du kan betale heile beløpet med ein gong" },
                english { + "1. You can pay the full amount at once" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du betaler hele beløpet i faktura fra Skatteetaten." },
                nynorsk { + "Du betaler heile beløpet på fakturaen frå Skatteetaten." },
                english { + "You pay the full amount on the invoice from the Norwegian Tax Administration." }
            )
        }
        title2 {
            text(
                bokmal { + "2. Du kan betale i avdrag" },
                nynorsk { + "2. Du kan betale i avdrag" },
                english { + "2. You can pay back in instalments" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du kan avtale å betale tilbake beløpet i avdrag. Ta kontakt med Skatteetaten hvis du ønsker å betale i avdrag, eller hvis du ønsker å øke eller redusere trekkprosenten." },
                nynorsk { + "Du kan avtale å betale tilbake beløpet i avdrag. Kontakt Skatteetaten dersom du ønskjer å betale i avdrag, eller dersom du ønskjer å auke eller redusere trekkprosenten." },
                english { + "You can arrange to pay back the amount in instalments. Please contact the Norwegian Tax Administration if you wish to pay in instalments, or if you wish to increase or reduce the deduction percentage." }
            )
        }
        title2 {
            text(
                bokmal { + "3. Du kan få trekk i støtte fra Nav" },
                nynorsk { + "3. Du kan få trekk i støtte frå Nav" },
                english { + "3. You can have deductions made from benefits from Nav" }
            )
        }
        paragraph {
            text(
                bokmal { + "Betaler du ikke hele beløpet, og du fortsatt får uføretrygd eller annen pengestøtte fra Nav, kan Skatteetaten automatisk trekke fra denne støtten. Normalt trekker vi 10 prosent av innvilget uføretrygd frem til hele beløpet er betalt. Vil du betale ned raskere, kan du be oss om å trekke mer enn 10 prosent." },
                nynorsk { + "Viss du ikkje betaler heile beløpet, og du framleis får uføretrygd eller anna pengestøtte frå Nav, kan Skatteetaten automatisk trekkje frå denne støtta. Vi trekkjer normalt sett 10 prosent av innvilga uføretrygd fram til heile beløpet er betalt. Dersom du ønskjer å betale ned raskare, kan du be oss om å trekkje meir enn 10 prosent." },
                english { + "If you do not pay the full amount and you are still receiving disability benefits or other financial support from Nav, the Norwegian Tax Administration can automatically deduct from this support. Normally we deduct 10 percent of granted disability benefit until the entire amount is paid. If you wish to pay off faster, you can ask us to deduct more than 10 percent." }
            )
        }
        paragraph {
            text(
                bokmal { + "Trekket blir gjort etter at skatten er trukket fra, men før eventuelle andre trekk." },
                nynorsk { + "Trekket blir gjort etter at skatten er trekt frå, men før eventuelle andre trekk." },
                english { + "Deductions will be made after tax is deducted, but before any other deductions." }
            )
        }
        paragraph {
            text(
                bokmal { + "Har du allerede trekk fra forrige etteroppgjør, kan du få to trekk." },
                nynorsk { + "Viss du allereie har trekk frå førre etteroppgjer, kan du få to trekk." },
                english { + "If you already have deductions from a previous post-settlement, you can have two deductions." }
            )
        }
    }
}

object InntekterSomKanHoldesUtenforEtteroppgjoeret :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Inntekter som kan holdes utenfor etteroppgjøret" },
                nynorsk { + "Inntekter som kan haldast utanfor etteroppgjeret" },
                english { + "Income that can be excluded from the post-settlement" }
            )
        }
        paragraph {
            text(
                bokmal { + "Har du inntekter som kan holdes utenfor etteroppgjøret som ikke allerede er tatt med i beregningen, må du sende oss dokumentasjon på det i posten innen 3 uker. Eksempler på dette er:" },
                nynorsk { + "Dersom du har inntekter som kan haldast utanfor etteroppgjeret som ikkje allereie er teke med i berekningen, må du sende oss dokumentasjon på desse i posten innan 3 veker. Døme på slike inntekter er:" },
                english { + "If you have income that can be excluded from the settlement and has not already been considered, you must send us documentation within 3 weeks. Examples of this include:" }
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { + "Inntekt fra arbeid eller utbetaling fra en virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel feriepenger." },
                        nynorsk { + "Inntekt frå arbeid eller utbetaling frå verksemd som blei heilt avslutta før du fekk innvilga uføretrygd (t.d. feriepengar)." },
                        english { + "Income from work or payments from an enterprise that was fully concluded before you were granted disability benefit, for example holiday pay." }
                    )
                }
                item {
                    text(
                        bokmal { + "Erstatningsoppgjør etter skadeerstatningsloven § 3-1 (også voldsoffererstatning), yrkesskadeloven § 13, og pasientskadeloven § 4 første ledd." },
                        nynorsk { + "Erstatningsoppgjer etter lova om skadeserstatning § 3-1 (også valdsoffererstatning), yrkesskadelova § 13, og pasientskadelova § 4 første ledd." },
                        english { + "Compensation payments made according to the Compensation Act Section 3-1 (including compensation for victims of violent crime), Occupational Injury Act Section 13, and the Patient Injury Act Section 4 par. 1." }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { + "Eksempler på dokumentasjon for inntekter som kan holdes utenfor etteroppgjøret:" },
                nynorsk { + "Døme på dokumentasjon for inntekter som kan haldast utanfor etteroppgjeret:" },
                english { + "Examples of documentation for income that can be excluded from the settlement:" }
            )
            list {
                item {
                    text(
                        bokmal { + "lønnsslipp" },
                        nynorsk { + "lønsslipp " },
                        english { + "wage slips" }
                    )
                }
                item {
                    text(
                        bokmal { + "dokumentasjon fra regnskapsfører" },
                        nynorsk { + "dokumentasjon frå rekneskapsførar" },
                        english { + "documentation from an accountant" }
                    )
                }
                item {
                    text(
                        bokmal { + "kopi av vedtak for erstatning" },
                        nynorsk { + "kopi av vedtak om erstatning" },
                        english { + "copy of the compensation decision" }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { + "Du trenger ikke å klage på vedtaket hvis du kun skal be om at inntekt skal holdes utenfor etteroppgjøret." },
                nynorsk { + "Du treng ikkje å klage på vedtaket dersom du berre skal be om at inntekt blir halden utanfor etteroppgjeret." },
                english { + "You do not need to appeal against the decision if you are only requesting that income be excluded from the settlement." }
            )
        }
    }
}

object EttersendeDokumentasjon :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Hvordan ettersende dokumentasjon" },
                nynorsk { + "Ettersende dokumentasjon" },
                english { + "Submitting documentation" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du kan sende oss dokumentasjon både digitalt og på papir." },
                nynorsk { + "Du kan sende oss dokumentasjon både digitalt og på papir." },
                english { + "You have the option to submit documentation to us digitally or by post." }
            )
        }
        title2 {
            text(
                bokmal { + "Digital ettersendelse" },
                nynorsk { + "Digital ettersending" },
                english { + "Digital submission" }
            )
        }
        paragraph {
            text(
                bokmal { + "For å sende dokumentasjon digitalt, gå inn på ${Constants.UFOERE_ETTERSENDE_URL}." },
                nynorsk { + "For å sende dokumentasjon digitalt, går du inn på ${Constants.UFOERE_ETTERSENDE_URL}." },
                english { + "To digitally submit information, please visit ${Constants.UFOERE_ETTERSENDE_URL}." }
            )
        }
        title2 {
            text(
                bokmal { + "Ettersendelse på papir" },
                nynorsk { + "Ettersending på papir" },
                english { + "Paper submission" }
            )
        }
        paragraph {
            text(
                bokmal { + "Når du sender inn dokumenter i posten, må du sende med en førsteside for innsending. Den finner du ved å gå inn på ${Constants.UFOERE_ETTERSENDE_POST_URL}." },
                nynorsk { + "Når du sender dokument i posten, må du leggje ved ei førsteside for innsending. Du finn denne på ${Constants.UFOERE_ETTERSENDE_POST_URL}." },
                english { + "When sending documents by regular post, you must include a cover page. You can find the cover page at ${Constants.UFOERE_ETTERSENDE_POST_URL}." }
            )
        }
        paragraph {
            text(
                bokmal { + "Når vi har mottatt dokumentasjon fra deg, vil vi vurdere om inntekten skal holdes utenfor. Du får et nytt brev når vi har gjort et nytt etteroppgjør." },
                nynorsk { + "Når vi har fått dokumentasjon frå deg, vil vi vurdere om inntekta skal haldast utanfor. Du får eit nytt brev når vi har utført eit nytt etteroppgjer." },
                english { + "Once we have received your documentation, we will assess whether the income should be excluded. You will receive a new letter once we have conducted a new settlement." }
            )
        }
        paragraph {
            text(
                bokmal { + "Du kan lese mer om etteroppgjør på ${Constants.ETTEROPPGJOR_URL}." },
                nynorsk { + "Du kan lese meir om etteroppgjer på ${Constants.ETTEROPPGJOR_URL}." },
                english { +  "For further information on post-settlement, please visit ${Constants.ETTEROPPGJOR_URL}." }
            )
        }
    }
}





