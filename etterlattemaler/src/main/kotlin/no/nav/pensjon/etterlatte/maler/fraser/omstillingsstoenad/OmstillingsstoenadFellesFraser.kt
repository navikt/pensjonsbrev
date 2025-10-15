package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
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
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.bosattUtland

object OmstillingsstoenadFellesFraser {

    object Utbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Utbetaling av omstillingsstønad" },
                    nynorsk { +"Utbetaling av omstillingsstønad" },
                    english { +"Payment of adjustment allowance" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Omstillingsstønad blir utbetalt innen den 20. i hver måned. Du finner " +
                            "utbetalingsdatoer på ${Constants.UTBETALING_URL}. Utbetalingen kan bli forsinket hvis " +
                            "den skal samordnes med ytelser du mottar fra Nav eller andre, som for eksempel " +
                            "tjenestepensjonsordninger." },
                    nynorsk { +"Omstillingsstønad blir utbetalt innan den 20. i kvar månad. Du finn " +
                            "utbetalingsdatoane på ${Constants.UTBETALING_URL}. Utbetalinga kan bli forseinka dersom " +
                            "ho skal samordnast med ytingar du får frå Nav eller andre (t.d. tenestepensjonsordningar)." },
                    english { +"The adjustment allowance is paid on or before the 20th of each month. " +
                            "You can find payout dates online: ${Constants.UTBETALING_URL}. The payment may be delayed " +
                            "if it is to be  coordinated with benefits you receive from Nav or others, such as a " +
                            "pension from an occupational pension scheme." },
                )
            }
        }
    }

    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du må melde fra om endringer" },
                    nynorsk { +"Du må melde frå om endringar" },
                    english { +"You must report any changes" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har plikt til å melde fra til oss om endringer som har betydning for " +
                            "utbetalingen av omstillingsstønaden din, eller retten til å få omstillingsstønad. " +
                            "I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om." },
                    nynorsk { +"Du pliktar å melde frå til oss om endringar som har innverknad på utbetalinga " +
                            "av eller retten på omstillingsstønad. I vedlegget «Dine rettar og plikter» ser du kva " +
                            "endringar du må seie frå om. " },
                    english { +"You are obligated to notify us of any changes that affect the payment of " +
                            "the adjustment allowance, or the right to receive the allowance. You will see " +
                            "which changes you must report in the attachment: Your rights and obligations." }
                )
            }
        }
    }

    object DuHarRettTilAaKlage  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                    english { +"You have the right to appeal" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du mener at vedtaket er feil, kan du sende inn en klage. Du må klage innen seks uker fra den dagen du mottok vedtaket. " },
                    nynorsk { +"Dersom du meiner at vedtaket er feil, kan du sende inn ei klage. Du må klage innan seks veker frå den dagen du fekk vedtaket." },
                    english { +"If you believe the decision is incorrect, you can submit an appeal. You must appeal within six weeks from the day you received the decision." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan lese mer om hvordan du klager i vedlegget \"Informasjon om klage og anke\" eller på ${Constants.KLAGE_URL}." },
                    nynorsk { +"Du kan lese meir om korleis du klagar i vedlegget \"Informasjon om klage og anke\" eller på ${Constants.KLAGE_URL}." },
                    english { +"You can read more about how to appeal in the attachment \"Information about appeals\" or at ${Constants.Engelsk.KLAGE_URL}." }
                )
            }
        }
    }

    object DuHarRettTilAaKlageAvslagOpphoer  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                    english { +"You have the right to appeal" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Du kan lese mer om hvordan du klager i " +
                            "vedlegget «Informasjon om klage og anke»." },
                    nynorsk { +"Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den " +
                            "datoen du fekk vedtaket. I vedlegget «Informasjon om klage og anke» kan du lese " +
                            "meir om korleis du klagar." },
                    english { +"If you believe the decision is incorrect, you may appeal the decision within " +
                            "six weeks from the date you received the decision. You can read more about how to " +
                            "appeal in the attachment Information on Complaints and Appeals.  " }
                )
            }
        }
    }

    object DuHarRettTilInnsyn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du har rett til innsyn" },
                    nynorsk { +"Du har rett til innsyn" },
                    english { +"You have the right to access documents" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har som hovedregel rett til å se dokumentene i saken din " +
                            "etter bestemmelsene i forvaltningsloven § 18. Hvis du ønsker innsyn, " +
                            "må du kontakte oss på telefon eller per post." },
                    nynorsk { +"Etter føresegnene i forvaltingslova § 18 har du som hovudregel rett til å " +
                            "sjå dokumenta i saka di. Kontakt oss på telefon eller per post dersom du ønskjer innsyn." },
                    english { +"As a general rule, you have the right to see the documents in your case " +
                            "pursuant to the provisions of Section 18 of the Public Administration Act. " +
                            "If you want access, you can contact us by phone or mail. " }
                )
            }
        }
    }

    object HarDuSpoersmaal : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Har du spørsmål?" },
                    nynorsk { +"Har du spørsmål?" },
                    english { +"Do you have any questions?" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan finne svar på ${Constants.OMS_URL}. På ${Constants.KONTAKT_URL} kan du " +
                            "chatte eller skrive til oss. Du kan også kontakte oss på telefon " +
                            "${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE}, hverdager mellom klokken 09.00-15.00. " +
                            "Hvis du oppgir fødselsnummer, kan vi lettere gi deg rask og god hjelp." },
                    nynorsk { +"Du kan finne svar på ${Constants.OMS_URL}. Du kan skrive til eller chatte " +
                            "med oss på ${Constants.KONTAKT_URL}. Alternativt kan du ringje oss på telefon " +
                            "${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE}, kvardagar mellom klokka 09.00–15.00. " +
                            "Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummer." },
                    english { +"You can find answers to your questions online: ${Constants.OMS_URL}. " +
                            "Feel free to chat with us or write to us here: ${Constants.Engelsk.KONTAKT_URL}. You can also contact " +
                            "us by phone at ${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE}, Monday to Friday between 9:00 AM and 3:00 PM. " +
                            "If you provide your national identity number, we can more easily provide you with quick and good help." }
                )
            }
        }
    }

    object Etteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Etteroppgjør" },
                    nynorsk { +"Etteroppgjer" },
                    english { +"Final settlement" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hver høst sjekker Nav inntektsopplysningene i skatteoppgjøret ditt for å se " +
                            "om du har fått utbetalt riktig beløp i omstillingsstønad året før. Hvis du har fått " +
                            "for lite utbetalt, kan du få en etterbetaling. Har du fått for mye utbetalt, kan du måtte betale " +
                            "tilbake. Du kan finne mer informasjon om etteroppgjør på " +
                            "${Constants.OMS_ETTEROPPGJOER_URL}." },
                    nynorsk { +"Kvar haust sjekkar Nav inntektsopplysningane i skatteoppgjeret ditt for å sjå " +
                            "om du fekk utbetalt rett beløp i omstillingsstønad året før. Dersom du fekk utbetalt " +
                            "for lite, kan du få ei etterbetaling. Dersom du fekk utbetalt meir enn du hadde rett på, " +
                            "kan du betale tilbake. Du kan lese meir om etteroppgjer på " +
                            "${Constants.OMS_ETTEROPPGJOER_URL}." },
                    english { +"Each autumn, Nav checks income data from your tax return to verify the correct " +
                            "amount of adjustment allowance for the previous year. If you received less than you " +
                            "are owed, you may receive Back Pay. If you received more than you were owed, you may have to " +
                            "repay the excess to Nav. You can find more information about final settlements online: " +
                            "${Constants.OMS_ETTEROPPGJOER_URL}." },
                )
            }
        }
    }

    data class HarDuIkkeBankID(
        val bosattUtland: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(bosattUtland) {
                paragraph {
                    text(
                        bokmal { +"Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til Nav familie- og pensjonsytelser, Postboks 6600 Etterstad, 0607 Oslo, Norway." },
                        nynorsk { +"Dersom du ikkje har BankID eller andre måtar å logge deg på heimesida vår, nav.no, må du sende brev til Nav familie- og pensjonsytelser, Postboks 6600 Etterstad, 0607 Oslo, Norway." },
                        english { +"If you do not have BankID or other means of logging into our home page nav.no, you must send a letter to Nav familie- og pensjonsytelser, P.O. Box 6600 Etterstad, 0607 Oslo, Norway." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til Nav skanning, Postboks 1400, 0109 OSLO." },
                        nynorsk { +"Dersom du ikkje har BankID eller andre måtar å logge deg på heimesida vår, nav.no, må du sende brev til Nav skanning, Postboks 1400, 0109 OSLO." },
                        english { +"If you do not have BankID or other means of logging into our home page nav.no, you must send a letter to Nav skanning, P.O. Box 1400, 0109 OSLO." },
                    )
                }
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
                    bokmal { +"Hvor lenge kan du få omstillingsstønaden?" },
                    nynorsk { +"Kor lenge kan du få omstillingsstønad?" },
                    english { +"How long can you receive adjustment allowance?" },
                )
            }
            showIf(omsRettUtenTidsbegrensning) {
                paragraph {
                    text(
                        bokmal { +"Du kommer inn under unntaksreglene for varighet av stønaden, fordi du har hatt " +
                                "lav eller ingen inntekt de siste fem årene før " +
                                ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallstidspunktet") +
                                ". Du får omstillingsstønad frem til du fyller 67 år, så lenge du oppfyller vilkårene." },
                        nynorsk { +"Du kjem inn under unntaksreglane for lengde på stønad, då du var utan " +
                                "inntekt eller hadde låg inntekt dei siste fem åra før " +
                                ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                                ". Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad fram til du " +
                                "fyller 67 år. " },
                        english { +"If any of the rules for exemption from the duration of allowance apply to " +
                                "you, because you had a low income or no income in the last five years before the " +
                                ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                ". You are eligible for the adjustment allowance until you turn " +
                                "67 as long as you meet the conditions." },
                    )
                }
            }.orShow {
                showIf(beregning.sisteBeregningsperiode.utbetaltBeloep.greaterThan(0)) {
                    paragraph {
                        text(
                            bokmal { +"Du får omstillingsstønad frem til det er gått tre år " +
                                    ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "fra datoen for dødsfallet") + ", så " +
                                    "lenge du oppfyller vilkårene. Rett til omstillingsstønad faller uansett bort når du fyller 67 år." },
                            nynorsk { +"Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad " +
                                    "fram til det har gått tre år frå datoen " +
                                    ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "for dødsfallet") +
                                    ". Rett til omstillingsstønad fell uansett bort når du fyller 67 år." },
                            english { +"You are eligible for adjustment allowance for three years from the " +
                                    ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                    ", as long as you meet the conditions for receiving the allowance. " +
                                    "The right to adjustment allowance ceases when you reach the age of 67." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Stønaden kan forlenges med inntil to år hvis du tar utdanning som er nødvendig " +
                                    "og hensiktsmessig." },
                            nynorsk { +"Stønaden kan bli forlenga med inntil to år dersom du tek utdanning " +
                                    "som er nødvendig og føremålstenleg." },
                            english { +"The allowance can be extended for two years if you begin an education or " +
                                    "vocational training that is necessary and suitable." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Du er innvilget omstillingsstønad frem til det er gått tre år " +
                                    ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "fra datoen for dødsfallet") +
                                    ", så lenge du oppfyller vilkårene. Om det skjer endringer " +
                                    "i inntekten din kan dette gjør at du likevel vil få utbetalt stønad i denne perioden. " },
                            nynorsk { +"Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad " +
                                    "i tre år frå datoen " + ifElse(tidligereFamiliepleier, "siden pleieforholdet opphørte", "for dødsfallet") +
                                    ". Dersom inntekta di skulle endre seg, kan " +
                                    "dette gjere at du likevel får utbetalt stønad i denne perioden." },
                            english { +"You are eligible for adjustment allowance for three years from the " +
                                    ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                    ", as long as you meet the conditions for receiving the allowance. " +
                                    "Changes to your income may make you eligible for allowance in this period. " },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Les mer om hvor lenge du kan få på " + Constants.OMS_HVORLENGE_URL + "." },
                    nynorsk { +"Les meir på " + Constants.OMS_HVORLENGE_URL + " om kor lenge du kan få stønad." },
                    english { +"Read more about the duration of allowance online: " + Constants.OMS_HVORLENGE_URL + "." },
                )
            }
        }
    }

    object FyllInn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"(Utfall jamfør tekstbibliotek)" },
                    nynorsk { +"(Utfall jamfør tekstbibliotek)" },
                    english { +"(Utfall jamfør tekstbibliotek)" },
                )
            }
            paragraph {
                text(
                    bokmal { +
                            "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § <riktig paragrafhenvisning>." },
                    nynorsk { +
                            "Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova § <riktig paragrafhenvisning>." },
                    english { +
                            "This decision has been made pursuant to the provisions regarding adjustment allowance in the National Insurance Act – sections § <riktig paragrafhenvisning>." },
                )
            }
        }
    }

    object SpesieltOmInntektsendring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Spesielt om endring av inntekten din" },
                    nynorsk { +"Spesielt om endring av inntekta di" },
                    english { +"Special information about changes to your income" },
                )
            }
            paragraph {
                text(
                    bokmal { +"For at du skal motta korrekt omstillingsstønad, er det viktig at du informerer " +
                            "oss hvis inntekten din endrer seg. Vi justerer omstillingsstønaden fra måneden etter " +
                            "at du har gitt beskjed. Du kan lese mer om inntektsendring i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad»." },
                    nynorsk { +"For at du skal få rett omstillingsstønad, er det viktig at du informerer oss " +
                            "dersom inntekta di endrar seg. Vi justerer omstillingsstønaden frå månaden etter at du " +
                            "har gitt beskjed. Du kan lese meir om inntektsendring i vedlegget «Informasjon til deg " +
                            "som får omstillingsstønad»." },
                    english { +"To receive the correct amount of the adjustment allowance, you are obligated " +
                            "to inform us about any changes to your income. We will adjust the adjustment " +
                            "allowance starting the month after you reported the change. You can read more " +
                            "about income reporting in the attachment: Information for recipients of adjustment allowance." },
                )
            }
        }
    }

    object Inntektsendring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du må melde fra hvis inntekten din endrer seg" },
                    nynorsk { +"Meld frå dersom inntekta di endrar seg" },
                    english { +"You must report any changes to your income" },
                )
            }
            paragraph {
                text(
                    bokmal { +"For at du skal motta korrekt omstillingsstønad, er det viktig at du informerer " +
                            "oss hvis inntekten din endrer seg. Vi justerer omstillingsstønaden fra måneden etter " +
                            "at du har gitt beskjed. Du kan lese mer om inntektsendring i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad»." },
                    nynorsk { +"For at du skal få rett omstillingsstønad, er det viktig at du informerer oss " +
                            "dersom inntekta di endrar seg. Vi justerer omstillingsstønaden frå månaden etter at du " +
                            "har gitt beskjed. Du kan lese meir om inntektsendring i vedlegget «Informasjon til deg " +
                            "som får omstillingsstønad»." },
                    english { +"To receive the correct amount of adjustment allowance, you are obligated to " +
                            "inform us about any changes to your income. We will adjust the adjustment allowance " +
                            "starting the month after you reported the change. You can read more about income " +
                            "reporting in the attachment Information to recipients of adjustment allowance. " },
                )
            }
        }
    }

}