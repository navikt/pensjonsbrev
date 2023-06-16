package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDto
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

//TBU3323
data class Innledning(
    val etteroppgjoerResultat: ForhaandsvarselEtteroppgjoerDto.EtteroppgjoerResultat,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Hvert år sjekker NAV inntekten din for å se om du har fått utbetalt riktig beløp i uføretrygd året før. Uføretrygden din er beregnet etter nye opplysninger om inntekt fra Skatteetaten.",
                Nynorsk to "Kvart år sjekkar NAV inntekta di for å sjå om du fekk utbetalt rett beløp i uføretrygd året før. Uføretrygda di blir rekna ut etter nye opplysningar om inntekt frå Skatteetaten.",
                English to "Every year, NAV checks your income to make sure that you have received the correct amount of disability benefit in the previous year. We use updated income information from the Tax Office to calculate your benefit"
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Vår beregning viser at du har fått <Avviksbelop> kroner for mye utbetalt.".expr(),
                Nynorsk to "Utrekninga vår viser at du har fått utbetalt <Avviksbelop> kroner for mykje.".expr(),
                English to "Our calculations show that you have received an overpayment of NOK <Aaviksbelop>.".expr()
            )
        }
    }
}

object SjekkBeregning : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        title1 {
            text(
                Bokmal to "Sjekk beregningen og meld fra hvis noe er feil",
                Nynorsk to "Sjekk utrekninga og meld frå dersom noko er feil",
                English to "Check the calculation and inform us of any errors"
            )
        }
        paragraph {
            text(
                Bokmal to "Dette brevet er et forhåndsvarsel, slik at du kan sjekke at beregningene i vedlegg 1 er korrekte, og melde fra til oss hvis noe er feil eller mangler.",
                Nynorsk to "Dette brevet er eit førehandsvarsel, og du har såleis høve til å sjekke at utrekningane i vedlegg 1 er korrekte, og melde frå til oss dersom noko er feil eller manglar.",
                English to "We are writing to inform you in advance before we make a final decision. Please review the calculations in Appendix 1 carefully. Check if there are any mistakes or missing information. If you find any errors, let us know as soon as possible."
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis vi ikke hører fra deg innen 3 uker, tar vi utgangspunkt i at beregningene våre er korrekte, og sender saken videre til Skatteetaten. Beregningen blir gjort om til et vedtak etter 4 uker fra du mottok dette brevet. Du vil ikke motta et nytt vedtak.",
                Nynorsk to "Dersom vi ikkje høyrer frå deg innan 3 veker, tek vi utgangspunkt i at utrekningane våre er korrekte, og sender saka vidare til Skatteetaten. Utrekninga blir gjort om til vedtak 4 veker etter at du har fått dette brevet. Du får ikkje eit nytt vedtak.",
                English to "If we do not hear from you within 3 weeks, we will assume that our calculations are correct and proceed to forward the case to the Tax Office. The calculation will be converted to an official decision 4 weeks from the date you received this letter. You will not receive a new decision letter."
            )
        }
    }
}

object HvordanDuBetaleTilbake : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du vil få informasjon om hvordan du kan betale tilbake etter 4 uker",
                Nynorsk to "Etter 4 veker får du informasjon om korleis du kan betale tilbake",
                English to "You will receive information about how you can repay after 4 weeks"
            )
        }
        paragraph {
            text(
                Bokmal to "Du vil få informasjon fra Skatteetaten etter 4 uker om når og hvordan du kan betale tilbake pengene. Før du kan få svar på spørsmål om saken din eller kan betale tilbake, må du ha mottatt betalingsinformasjon fra Skatteetaten.",
                Nynorsk to "Når det har gått 4 veker, vil Skatteetaten sende deg informasjon om når og korleis du kan betale tilbake pengane. Før du kan få svar på spørsmål om saka di eller betale tilbake, må du ha fått betalingsinformasjon frå Skatteetaten.",
                English to "After 4 weeks, you will receive information from the Tax Office about how and when to repay the overpaid amount. You will receive information from the Tax Office for contribution and repayment demands after 4 weeks, about when and how you can repay the money. Before you can receive a response to questions regarding your case, or repay any amounts, you must have received payment information from the Tax Office."
            )
        }
        paragraph {
            text(
                Bokmal to "Fordi du har betalt skatt av det du har fått for mye utbetalt, vil vi trekke fra skatt fra beløpet du skal betale tilbake. I betalingsinformasjonen fra Skatteetaten står det hvor mye du faktisk skal betale tilbake.",
                Nynorsk to "Ettersom du har betalt skatt av det du har fått for mykje utbetalt, vil vi trekkje frå skatten frå beløpet du skal betale tilbake. I betalingsinformasjonen frå Skatteetaten står det kor mykje du faktisk skal betale tilbake.",
                English to "As you have paid tax on the amount you have been overpaid, we will deduct tax from the amount you have to repay. The payment information from the Tax Office will state how much you must actually repay."
            )
        }
    }
}

// Hvis bruker har hatt inntekt over 80% av OIFU
data class InntektOverInntektstak(
val periodeFom: Expression<LocalDate>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du har tjent over 80 prosent av inntekten du hadde før du ble ufør",
                Nynorsk to "Du har tent over 80 prosent av inntekta du hadde før du blei ufør",
                English to "You have earned over 80 percent of the income you had before you received disability benefit"
            )
        }
        paragraph {
            text(
                Bokmal to "Det er mulig at du ikke trenger å betale tilbake hele/deler av beløpet du har fått for mye utbetalt. Dette vil vi vurdere. Dette forutsetter at inntekten din i starten av året var under inntektsgrensen, jf. folketrygdloven § 4-1. Er dette aktuelt for deg, vil du få et eget brev.",
                Nynorsk to "Det er mogleg at du ikkje treng å betale tilbake heile/delar av beløpet du har fått for mykje utbetalt. Dette vil vi vurdere. Føresetnaden er at inntekta di i starten av året var under inntektsgrensa, jf. folketrygdlova § 4-1. Dersom dette gjeld deg, vil du få eit eige brev.",
                English to "It may be the case that you do not need to repay the whole/part of the amount that you have been overpaid. We will assess this. This is on the premise that your income at the beginning of the year was below the income threshold, ref. National Insurance Act Section 4-1. If this is relevant to you, you will receive a separate letter."
            )
        }
    }

}
