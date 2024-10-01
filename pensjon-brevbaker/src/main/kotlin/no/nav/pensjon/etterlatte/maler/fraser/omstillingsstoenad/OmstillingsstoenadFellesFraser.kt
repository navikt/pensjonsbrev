package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

object OmstillingsstoenadFellesFraser {

    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must report any changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for " +
                            "utbetalingen av omstillingsstønaden din, eller retten til å få omstillingsstønad. " +
                            "I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Du pliktar å melde frå til oss om endringar som har innverknad på utbetalinga " +
                            "av eller retten på omstillingsstønad. I vedlegget «Dine rettar og plikter» ser du kva " +
                            "endringar du må seie frå om. ",
                    English to "You are obligated to notify us of any changes that affect the payment of " +
                            "the adjustment allowance, or the right to receive the allowance. You will see " +
                            "which changes you must report in the attachment Your Rights and Obligations."
                )
            }
        }
    }

    object DuHarRettTilAaKlage  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema " +
                            "og informasjon på ${Constants.KLAGE_URL}.",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den " +
                            "datoen du fekk vedtaket. Klaga må vere skriftleg. Du finn skjema og informasjon " +
                            "på ${Constants.KLAGE_URL}.",
                    English to "If you believe the decision is incorrect, you may appeal the decision within " +
                            "six weeks from the date you received the decision. The appeal must be in writing. " +
                            "You can find the form and information online: ${Constants.Engelsk.KLAGE_URL}."
                )
            }
        }
    }

    object DuHarRettTilAaKlageAvslagOpphoer  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Du kan lese mer om hvordan du klager i " +
                            "vedlegget «Informasjon om klage og anke».",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den " +
                            "datoen du fekk vedtaket. I vedlegget «Informasjon om klage og anke» kan du lese " +
                            "meir om korleis du klagar.",
                    English to "If you believe the decision is incorrect, you may appeal the decision within " +
                            "six weeks from the date you received the decision. You can read more about how to " +
                            "appeal in the attachment Information on Complaints and Appeals.  "
                )
            }
        }
    }

    object DuHarRettTilInnsyn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn",
                    English to "You have the right to access documents"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din " +
                            "etter bestemmelsene i forvaltningsloven § 18. Hvis du ønsker innsyn, " +
                            "må du kontakte oss på telefon eller per post.",
                    Nynorsk to "Etter føresegnene i forvaltingslova § 18 har du som hovudregel rett til å " +
                            "sjå dokumenta i saka di. Kontakt oss på telefon eller per post dersom du ønskjer innsyn.",
                    English to "As a general rule, you have the right to see the documents in your case " +
                            "pursuant to the provisions of Section 18 of the Public Administration Act. " +
                            "If you want access, you can contact us by phone or mail. "
                )
            }
        }
    }

    object HarDuSpoersmaal : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Any questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan finne svar på ${Constants.OMS_URL}. På ${Constants.KONTAKT_URL} kan du " +
                            "chatte eller skrive til oss. Du kan også kontakte oss på telefon " +
                            "${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE}, hverdager mellom klokken 09.00-15.00. " +
                            "Hvis du oppgir fødselsnummer, kan vi lettere gi deg rask og god hjelp.",
                    Nynorsk to "Du kan finne svar på ${Constants.OMS_URL}. Du kan skrive til eller chatte " +
                            "med oss på ${Constants.KONTAKT_URL}. Alternativt kan du ringje oss på telefon " +
                            "${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE}, kvardagar mellom klokka 09.00–15.00. " +
                            "Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummer.",
                    English to "You can find answers to your questions online: ${Constants.OMS_URL}. " +
                            "Feel free to chat with us or write to us here: ${Constants.Engelsk.KONTAKT_URL}. You can also contact " +
                            "us by phone at ${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE}, Monday to Friday between 9:00 AM and 3:00 PM. " +
                            "If you provide your national identity number, we can more easily provide you with quick and good help."
                )
            }
        }
    }

    object Etteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Etteroppgjør",
                    Nynorsk to "Etteroppgjer",
                    English to "Final settlement",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hver høst sjekker NAV inntektsopplysningene i skatteoppgjøret ditt for å se " +
                            "om du har fått utbetalt riktig beløp i omstillingsstønad året før. Hvis du har fått " +
                            "for lite utbetalt, får du en etterbetaling. Har du fått for mye utbetalt, må du betale " +
                            "tilbake. Du kan finne mer informasjon om etteroppgjør på " +
                            "${Constants.OMS_ETTEROPPGJOER_URL}.",
                    Nynorsk to "Kvar haust sjekkar NAV inntektsopplysningane i skatteoppgjeret ditt for å sjå " +
                            "om du fekk utbetalt rett beløp i omstillingsstønad året før. Dersom du fekk utbetalt " +
                            "for lite, får du ei etterbetaling. Dersom du fekk utbetalt meir enn du hadde rett på, " +
                            "må du betale tilbake. Du kan lese meir om etteroppgjer på " +
                            "${Constants.OMS_ETTEROPPGJOER_URL}.",
                    English to "Each autumn, NAV checks income data from your tax return to verify the correct " +
                            "amount of adjustment allowance for the previous year. If you received less than you " +
                            "are owed, you will receive Back Pay. If you received more than you were owed, you must " +
                            "repay the excess to NAV. You can find more information about final settlements online: " +
                            "${Constants.OMS_ETTEROPPGJOER_URL}.",
                )
            }
        }
    }

    data class HvorLengerKanDuFaaOmstillingsstoenad(
        val beregning: Expression<OmstillingsstoenadBeregning>,
        val omsRettUtenTidsbegrensning: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hvor lenge kan du få omstillingsstønaden?",
                    Nynorsk to "Kor lenge kan du få omstillingsstønad?",
                    English to "How long can you receive adjustment allowance?",
                )
            }
            showIf(omsRettUtenTidsbegrensning) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kommer inn under unntaksreglene for varighet av stønaden, fordi du har hatt ".expr() +
                                "lav eller ingen inntekt de siste fem årene før " +
                                ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallstidspunktet") +
                                ". Du får omstillingsstønad frem til du fyller 67 år, så lenge du oppfyller vilkårene.",
                        Nynorsk to "Du kjem inn under unntaksreglane for lengde på stønad, då du var utan ".expr() +
                                "inntekt eller hadde låg inntekt dei siste fem åra før " +
                                ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                                ". Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad fram til du " +
                                "fyller 67 år. ",
                        English to "If any of the rules for exemption from the duration of allowance apply to ".expr() +
                                "you, because you had a low income or no income in the last five years before the " +
                                ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                ". You are eligible for the adjustment allowance until you turn " +
                                "67 as long as you meet the conditions.",
                    )
                }
            }.orShow {
                showIf(beregning.sisteBeregningsperiode.utbetaltBeloep.greaterThan(0)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får omstillingsstønad frem til det er gått tre år ".expr() +
                                    ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "fra datoen for dødsfallet") + ", så " +
                                    "lenge du oppfyller vilkårene. Rett til omstillingsstønad faller uansett bort når du fyller 67 år.",
                            Nynorsk to "Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad ".expr() +
                                    "fram til det har gått tre år frå datoen " +
                                    ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "for dødsfallet") +
                                    ". Rett til omstillingsstønad fell uansett bort når du fyller 67 år.",
                            English to "You are eligible for adjustment allowance for three years from the ".expr() +
                                    ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                    ", as long as you meet the conditions for receiving the allowance. " +
                                    "The right to adjustment allowance ceases when you reach the age of 67.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Stønaden kan forlenges med inntil to år hvis du tar utdanning som er nødvendig " +
                                    "og hensiktsmessig.",
                            Nynorsk to "Stønaden kan bli forlenga med inntil to år dersom du tek utdanning " +
                                    "som er nødvendig og føremålstenleg.",
                            English to "The allowance can be extended for two years if you begin an education or " +
                                    "vocational training that is necessary and suitable.",
                        )
                    }
                }.orShow {
                    paragraph {
                        textExpr(
                            Bokmal to "Du er innvilget omstillingsstønad frem til det er gått tre år ".expr() +
                                    ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "fra datoen for dødsfallet") +
                                    ", så lenge du oppfyller vilkårene. Om det skjer endringer " +
                                    "i inntekten din kan dette gjør at du likevel vil få utbetalt stønad i denne perioden. ",
                            Nynorsk to "Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad ".expr() +
                                    "i tre år frå datoen " + ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "for dødsfallet") +
                                    ". Dersom inntekta di skulle endre seg, kan " +
                                    "dette gjere at du likevel får utbetalt stønad i denne perioden.",
                            English to "You are eligible for adjustment allowance for three years from the ".expr() +
                                    ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                    ", as long as you meet the conditions for receiving the allowance. " +
                                    "Changes to your income may make you eligible for allowance in this period. ",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Les mer om hvor lenge du kan få på " + Constants.OMS_HVORLENGE_URL + ".",
                    Nynorsk to "Les meir på " + Constants.OMS_HVORLENGE_URL + " om kor lenge du kan få stønad.",
                    English to "Read more about the duration of allowance online: " + Constants.OMS_HVORLENGE_URL + ".",
                )
            }
        }
    }

    object SpesieltOmInntektsendring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Spesielt om endring av inntekten din",
                    Nynorsk to "Spesielt om endring av inntekta di",
                    English to "Special information about changes to your income",
                )
            }
            paragraph {
                text(
                    Bokmal to "For at du skal motta korrekt omstillingsstønad, er det viktig at du informerer " +
                            "oss hvis inntekten din endrer seg. Vi justerer omstillingsstønaden fra måneden etter " +
                            "at du har gitt beskjed. Du kan lese mer om inntektsendring i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad».",
                    Nynorsk to "For at du skal få rett omstillingsstønad, er det viktig at du informerer oss " +
                            "dersom inntekta di endrar seg. Vi justerer omstillingsstønaden frå månaden etter at du " +
                            "har gitt beskjed. Du kan lese meir om inntektsendring i vedlegget «Informasjon til deg " +
                            "som får omstillingsstønad».",
                    English to "To receive the correct amount of the adjustment allowance, you are obligated " +
                            "to inform us about any changes to your income. We will adjust the adjustment " +
                            "allowance starting the month after you reported the change. You can read more " +
                            "about income reporting in the attachment Information to Adjustment Allowance Recipients.",
                )
            }
        }
    }

    object Inntektsendring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra hvis inntekten din endrer seg",
                    Nynorsk to "Meld frå dersom inntekta di endrar seg",
                    English to "You must report any changes to your income",
                )
            }
            paragraph {
                text(
                    Bokmal to "For at du skal motta korrekt omstillingsstønad, er det viktig at du informerer " +
                            "oss hvis inntekten din endrer seg. Vi justerer omstillingsstønaden fra måneden etter " +
                            "at du har gitt beskjed. Du kan lese mer om inntektsendring i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad».",
                    Nynorsk to "For at du skal få rett omstillingsstønad, er det viktig at du informerer oss " +
                            "dersom inntekta di endrar seg. Vi justerer omstillingsstønaden frå månaden etter at du " +
                            "har gitt beskjed. Du kan lese meir om inntektsendring i vedlegget «Informasjon til deg " +
                            "som får omstillingsstønad».",
                    English to "To receive the correct amount of adjustment allowance, you are obligated to " +
                            "inform us about any changes to your income. We will adjust the adjustment allowance " +
                            "starting the month after you reported the change. You can read more about income " +
                            "reporting in the attachment Information to Recipients of Adjustment Allowance. ",
                )
            }
        }
    }

}