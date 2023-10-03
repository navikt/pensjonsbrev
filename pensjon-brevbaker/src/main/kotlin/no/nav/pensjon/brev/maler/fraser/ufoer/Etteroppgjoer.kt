package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year


//TBU3323
data class Innledning(
    val totaltAvvik: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Hvert år sjekker NAV inntekten din for å se om du har fått utbetalt riktig beløp i uføretrygd året før. Uføretrygden din er beregnet etter nye opplysninger om inntekt fra Skatteetaten.",
                Nynorsk to "Kvart år sjekkar NAV inntekta di for å sjå om du fekk utbetalt rett beløp i uføretrygd året før. Uføretrygda di blir rekna ut etter nye opplysningar om inntekt frå Skatteetaten.",
                English to "Every year, NAV checks your income information in your tax settlement to see whether you have received the correct amount of disability benefit in the previous year. We use updated income information from the Norwegian Tax Administration to calculate your benefit."
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Vår beregning viser at du har fått ".expr() + totaltAvvik.absoluteValue().format() + " kroner for mye utbetalt.",
                Nynorsk to "Utrekninga vår viser at du har fått utbetalt ".expr() + totaltAvvik.absoluteValue().format() + " kroner for mykje.",
                English to "Our calculations show that you have received an overpayment of NOK ".expr() + totaltAvvik.absoluteValue().format() + "."
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
                Bokmal to "Dette brevet er et forhåndsvarsel, slik at du kan sjekke at beregningene i vedlegg «Opplysninger om etteroppgjøret» er korrekte, og melde fra til oss hvis noe er feil eller mangler.",
                Nynorsk to "Dette brevet er eit førehandsvarsel, og du har såleis høve til å sjekke at utrekningane i vedlegg «Opplysningar om etteroppgjer» er korrekte, og melde frå til oss dersom noko er feil eller manglar.",
                English to "This letter is an advance notice regarding the calculations provided in the appendix «Information about the settlement». Please review the calculations carefully and inform us of any errors or missing information."
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis vi ikke hører fra deg innen 3 uker, tar vi utgangspunkt i at beregningene våre er korrekte, og sender saken videre til Skatteetaten. Beregningen blir gjort om til et vedtak etter 4 uker fra du mottok dette brevet. Du vil ikke motta et nytt vedtak.",
                Nynorsk to "Dersom vi ikkje høyrer frå deg innan 3 veker, tek vi utgangspunkt i at utrekningane våre er korrekte, og sender saka vidare til Skatteetaten. Utrekninga blir gjort om til vedtak 4 veker etter at du har fått dette brevet. Du får ikkje eit nytt vedtak.",
                English to "If we do not hear from you within 3 weeks, we will assume that our calculations are correct and proceed to forward the case to the Norwegian Tax Administration. The calculation will be converted to an official decision 4 weeks from the date you received this letter. You will not receive a new decision letter."
            )
        }
    }
}

object HvordanDuBetalerTilbake : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du vil få informasjon om hvordan du kan betale tilbake etter 4 uker",
                Nynorsk to "Etter 4 veker får du informasjon om korleis du kan betale tilbake",
                English to "You will receive payment instructions after 4 weeks"
            )
        }
        paragraph {
            text(
                Bokmal to "Du vil få informasjon fra Skatteetaten etter 4 uker om når og hvordan du kan betale tilbake pengene. Før du kan få svar på spørsmål om saken din eller kan betale tilbake, må du ha mottatt betalingsinformasjon fra Skatteetaten.",
                Nynorsk to "Når det har gått 4 veker, vil Skatteetaten sende deg informasjon om når og korleis du kan betale tilbake pengane. Før du kan få svar på spørsmål om saka di eller betale tilbake, må du ha fått betalingsinformasjon frå Skatteetaten.",
                English to "After 4 weeks, you will receive information from the Norwegian Tax Administration about how and when to repay the overpaid amount. Please note that you must receive this information before we can answer any questions regarding your case, or for you to start repaying the amount owed."
            )
        }
        paragraph {
            text(
                Bokmal to "Fordi du har betalt skatt av det du har fått for mye utbetalt, vil vi trekke fra skatt fra beløpet du skal betale tilbake. I betalingsinformasjonen du får fra Skatteetaten står det hvor mye du faktisk skal betale tilbake.",
                Nynorsk to "Ettersom du har betalt skatt av det du har fått for mykje utbetalt, vil vi trekkje frå skatten frå beløpet du skal betale tilbake. I betalingsinformasjonen frå Skatteetaten står det kor mykje du faktisk skal betale tilbake.",
                English to "As you have paid tax on the overpaid amount, tax will be deducted from the amount you are required to repay. The payment information provided by the Norwegian Tax Administration will specify the exact amount you are required to repay."
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan lese mer om tilbakebetaling i vedlegget «Praktisk informasjon om etteroppgjør».",
                Nynorsk to "Du kan lese meir om tilbakebetaling i vedlegget «Praktisk informasjon om etteroppgjer».",
                English to "You can read more about repayment in the appendix «Practical information about post-settlement»."
            )
        }
    }
}

data class InntektOverInntektstak(
    val periode: Expression<Year>,
    val oppjustertInntektFoerUfoerhet: Expression<Kroner>,
    val pensjonsgivendeInntektBruktIBeregningen: Expression<Kroner>,
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
                English to "It is possible that you may not need to repay the entire or part of the amount that you have been overpaid. We will assess this based on whether your income at the start of the year was below the specified threshold, as specified in Section 4-1 of the National Insurance Act. If this applies to your situation, you will receive a separate letter."
            )
        }
        paragraph {
            textExpr(
                Bokmal to "I ".expr() + periode.format() + " var 80 prosent av inntekten din før du ble ufør, "+ oppjustertInntektFoerUfoerhet.format() + " kroner. ",
                Nynorsk to "I ".expr() + periode.format() + " var 80 prosent av inntekta di før du blei ufør, "+ oppjustertInntektFoerUfoerhet.format() + " kroner. ",
                English to "In ".expr() + periode.format() + ", 80 percent of your pre-disability benefit income was NOK "+ oppjustertInntektFoerUfoerhet.format() + ". "
            )
            textExpr(
                Bokmal to "Du tjente ".expr() + pensjonsgivendeInntektBruktIBeregningen.format() + " kroner i "+ periode.format() + ".",
                Nynorsk to "Du tente ".expr() + pensjonsgivendeInntektBruktIBeregningen.format() + " kroner i "+ periode.format() + ".",
                English to "You earned NOK ".expr() + pensjonsgivendeInntektBruktIBeregningen.format() + " in "+ periode.format() + "."
            )
        }
    }
}

object SoekOmNyInntektsgrense : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Søke om ny inntektsgrense",
                Nynorsk to "Søkje om ny inntektsgrense",
                English to "Applying for a new income threshold"
            )
        }
        paragraph {
            text(
                Bokmal to "Er du arbeidstaker og har gradert uføretrygd, kan du søke om ny inntektsgrense. Dette gjelder hvis du har hatt høy lønnsøkning, uten at det skyldes overtidsjobbing, ekstravakter eller høyere stillingsprosent.",
                Nynorsk to "Dersom du er arbeidstakar og har gradert uføretrygd, kan du søkje om ny inntektsgrense. Dette gjeld viss du har hatt høg lønsauke, utan at det skuldast overtidsjobbing, ekstravakter eller høgare stillingsprosent.",
                English to "If you are an employee receiving partial disability benefits, you can apply for a new income threshold. This applies if your wage has significantly increased, unrelated to working overtime, extra shifts, or a higher job percentage.",
            )
        }
    }
}


object FlereVedtakOmEtteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "For deg som har fått flere vedtak om etteroppgjør for samme år",
                Nynorsk to "For deg som har fått fleire vedtak om etteroppgjer for same år",
                English to "Multiple post-settlements for the same year"
            )
        }
        paragraph {
            text(
                Bokmal to "I noen tilfeller gjør vi flere etteroppgjør for samme år. Alle oppgjør er gyldige. Vi sammenligner dem for å se om du må betale tilbake, eller om du har penger til gode.",
                Nynorsk to "I enkelte tilfelle utfører vi fleire etteroppgjer for same år. Alle oppgjer er gyldige. Vi samanliknar dei for å sjå om du må betale tilbake, eller om du har pengar til gode.",
                English to "In some cases, we issue multiple post-settlements for the same year. All settlements are valid and apply. We compare them to see if you need to repay or if you are owed a refund."
            )
        }
    }
}

object MeldeFraOmEndringerEtteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du må melde fra om endringer",
                Nynorsk to "Hugs å melde frå om endringar",
                English to "Update your income information"
            )
        }
        paragraph {
            text(
                Bokmal to "For at du skal få utbetalt riktig uføretrygd fremover, er det viktig at du oppdaterer inntekten din. Dette gjør du på ${Constants.INNTEKTSPLANLEGGEREN_URL}. I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                Nynorsk to "For at du skal få utbetalt rett uføretrygd framover, er det viktig at du oppdaterer inntekta di. Dette gjer du på ${Constants.INNTEKTSPLANLEGGEREN_URL}. I vedlegget «Rettane og pliktene dine» ser du kva endringar du må seie frå om.",
                English to "To ensure that you receive the correct amount of disability benefit in the future, it is important that you update your income information. You can do this at ${Constants.INNTEKTSPLANLEGGEREN_URL}. In the appendix «Your rights and obligations», you can find the changes you need to report."
            )
        }
    }
}

object FristerOpplysningerKlage : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Frister for å sende inn nye opplysninger og klage",
                Nynorsk to "Fristar for å sende inn nye opplysningar og klage",
                English to "Deadlines for submitting new information and appeals"
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du mener at beregningene i vedlegg «Opplysninger om etteroppgjøret» er feil, må du melde fra til oss innen 3 uker fra du fikk dette brevet. Du vil da få en ny vurdering og et nytt vedtak.",
                Nynorsk to "Dersom du meiner at utrekningane i vedlegg «Opplysningar om etteroppgjer» er feil, må du melde frå til oss innan 3 veker frå du fekk dette brevet. Du vil då få ei ny vurdering og eit nytt vedtak.",
                English to "If you believe that the calculations in appendix «Information about the settlement» are incorrect, please notify us within 3 weeks from the date you received this letter. You will then receive a new assessment and a new decision."
            )
        }
        paragraph {
            text(
                Bokmal to "Klagefristen på vedtaket er 6 uker etter at saken er sendt til Skatteetaten. Du finner skjema og informasjon om hvordan du klager på ${Constants.KLAGE_URL}. Du må som hovedregel begynne å betale tilbake selv om du klager på vedtaket, se forvaltningsloven § 42.",
                Nynorsk to "Klagefristen på vedtaket er 6 veker etter at saka er send til Skatteetaten. Du finn skjema og informasjon om korleis du klagar, på ${Constants.KLAGE_URL}. Du må som hovudregel byrje å betale tilbake sjølv om du klagar på vedtaket, jf. forvaltingslova § 42.",
                English to "The deadline for appealing against the decision is 6 weeks after the case has been forwarded to the Norwegian Tax Administration. You can find forms and information about how to appeal at ${Constants.KLAGE_URL}. As a rule, you are required to start repaying even if you appeal against the decision, as stated in section 42 of the Public Administration Act."
            )
        }
    }
}

object HarDuSpoersmaalEtteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Har du spørsmål?",
                Nynorsk to "Har du spørsmål?",
                English to "Do you have questions?"
            )
        }
        paragraph {
            text(
                Bokmal to "Du finner mer informasjon på ${Constants.ETTEROPPGJOR_URL}. På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON}, hverdager ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                Nynorsk to "Du finn meir informasjon på ${Constants.ETTEROPPGJOR_URL}. Du kan chatte med eller skrive til oss på ${Constants.KONTAKT_URL}. Dersom du ikkje finn svar på ${Constants.NAV_URL}, kan du ringje oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON}, kvardagar ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                English to "You can find more information at ${Constants.ETTEROPPGJOR_URL}. At ${Constants.KONTAKT_URL} you can chat or write to us. If you cannot find answers at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON}, weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
            )
        }
    }
}

