package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetalingSelectors.etterbetalingPeriodeValg_safe
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetalingSelectors.frivilligSkattetrekk_safe
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetalingSelectors.inneholderKrav_safe
import no.nav.pensjon.etterlatte.maler.EtterbetalingPeriodeValg
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon

object BarnepensjonFellesFraser {
    object FyllInn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "(utfall jamfør tekstbibliotek)",
                    Nynorsk to "(utfall jamfør tekstbibliotek)",
                    English to "(utfall jamfør tekstbibliotek)",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § <riktig paragrafhenvisning>.",
                    Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova § <riktig paragrafhenvisning>.",
                    English to "This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections <riktig paragrafhenvisning>.",
                )
            }
        }
    }

    object DuHarRettTilAaKlage : OutlinePhrase<LangBokmalNynorskEnglish>() {
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
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen " +
                            "du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen " +
                            "du fekk vedtaket. Klaga må vere skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                    English to "If you believe the decision is incorrect, " +
                            "you may appeal the decision within six weeks from the date you received the decision. " +
                            "The appeal must be in writing. " +
                            "You can find the form and information online: ${Constants.Engelsk.KLAGE_URL}."
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
                    Bokmal to "Du har som hovedregel rett til å se dokumentene i saken etter" +
                            " forvaltningsloven § 18. Hvis du ønsker innsyn, må du kontakte oss på " +
                            "telefon eller per post.",
                    Nynorsk to "Etter føresegnene i forvaltingslova § 18 har du som hovudregel rett til " +
                            "å sjå dokumenta i saka di. Kontakt oss på telefon eller per post dersom du ønskjer innsyn.",
                    English to "As a general rule, you have the right to see the documents in your case " +
                            "pursuant to the provisions of Section 18 of the Public Administration Act. " +
                            "If you want access, you can contact us by phone or mail."
                )
            }
        }
    }

    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must report any changes",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for utbetalingen av barnepensjon, eller retten til å få barnepensjon. I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Du pliktar å melde frå til oss om endringar som har innverknad på utbetalinga av eller retten på barnepensjon. I vedlegget «Rettane og pliktene dine» ser du kva endringar du må seie frå om.",
                    English to "You are obligated to notify us of any changes that affect the payment of a children's pension, or the right to receive a children's pension. You will see which changes you must report in the attachment Your Rights and Obligations.",
                )
            }
        }
    }

    data class HarDuSpoersmaal(
        val brukerUnder18Aar: Expression<Boolean>,
        val bosattUtland: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Any questions?",
                )
            }

            showIf(brukerUnder18Aar) {
                paragraph {
                    text(
                        Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon ",
                        Nynorsk to "Du finn meir informasjon på ${Constants.BARNEPENSJON_URL}. Dersom du ikkje finn svar på spørsmålet ditt der, kan du ringje oss på telefon ",
                        English to "For more information, visit us online: ${Constants.Engelsk.BARNEPENSJON_URL}. If you cannot find the answer to your question, you can call us by phone at "
                    )
                    kontakttelefonPensjon(bosattUtland)
                    text(
                        Bokmal to " hverdager mellom klokken 09.00-15.00. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                        Nynorsk to ", kvardagar mellom klokka 09.00–15.00. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret til barnet.",
                        English to ", Monday to Friday between 09:00 AM and 03:00 PM. If you provide your child's national identity number, we can more easily provide you with quick and good help."
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. Du kan også kontakte oss på telefon ",
                        Nynorsk to "Du finn meir informasjon på ${Constants.BARNEPENSJON_URL}. Du kan skrive til eller chatte med oss på ${Constants.KONTAKT_URL}. Alternativt kan du ringje oss på telefon ",
                        English to "You can find answers to your questions online: ${Constants.Engelsk.BARNEPENSJON_URL}. Feel free to chat with us or write to us here: ${Constants.Engelsk.KONTAKT_URL}. You can also contact us by phone at "
                    )
                    kontakttelefonPensjon(bosattUtland)
                    text(
                        Bokmal to ", hverdager klokken 09.00-15.00. Om du oppgir fødselsnummeret ditt, kan vi lettere gi deg rask og god hjelp.",
                        Nynorsk to " kvardagar mellom klokka 09.00–15.00. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret ditt.",
                        English to ", Monday to Friday between 9:00 AM and 3:00 PM. If you provide your national identity number, we can more easily provide you with quick and good help."
                    )
                }
            }
        }
    }

    object HvorLengeKanDuFaaBarnepensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hvor lenge kan du få barnepensjon?",
                    Nynorsk to "Kor lenge kan du få barnepensjon?",
                    English to "How long will you receive the children’s pension?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du er innvilget barnepensjon til og med den kalendermåneden du fyller 20 år, så lenge du oppfyller vilkårene.",
                    Nynorsk to "Under føresetnad av at du innfrir vilkåra, kan du få barnepensjon fram til og med kalendermånaden du fyller 20 år.",
                    English to "You will receive the children’s pension until and including the calendar month in which you turn 20, as long as you satisfy the conditions.",
                )
            }
        }
    }

    data class UtbetalingAvBarnepensjon(
        val etterbetaling: Expression<BarnepensjonEtterbetaling?>,
        val brukerUnder18Aar: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Utbetaling av barnepensjon",
                    Nynorsk to "Utbetaling av barnepensjon",
                    English to "Payment of the children's pension",
                )
            }
            paragraph {
                text(
                    Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. Du finner utbetalingsdatoer på ${Constants.UTBETALINGSDATOER_URL}.",
                    Nynorsk to "Pensjonen blir utbetalt innan den 20. kvar månad. Du finn utbetalingsdatoane på ${Constants.UTBETALINGSDATOER_URL}.",
                    English to "The pension is paid by the 20th of each month. You can find payout dates online: ${Constants.Engelsk.UTBETALINGSDATOER_URL}.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon er skattepliktig, men ikke trekkpliktig.",
                    Nynorsk to "Barnepensjon er skattepliktig, men ikkje trekkpliktig.",
                    English to "Child pension is taxable, but not subject to withholding tax.",
                )
            }
            showIf(etterbetaling.notNull()) {
                paragraph {
                    text(
                        Bokmal to "Du får etterbetalt pensjon. Vanligvis vil du få denne i løpet av tre uker. ",
                        Nynorsk to "Du får etterbetalt pensjon. Vanlegvis vil du få denne i løpet av tre veker. ",
                        English to
                            "You will receive a back payment on your pension. " +
                            "You will usually receive this back payment within three weeks. ",
                    )
                    showIf(etterbetaling.inneholderKrav_safe.equalTo(true)) {
                        text(
                            Bokmal to
                                "Hvis det er lagt inn krav i etterbetalingen kan denne bli forsinket. " +
                                "Beløpet som er trukket fra etterbetalingen vil gå frem av utbetalingsmeldingen.",
                            Nynorsk to
                                "Dersom det er lagt inn krav i etterbetalinga, kan denne bli forseinka. " +
                                "Beløpet som er trekt frå etterbetalinga, vil gå fram av utbetalingsmeldinga.",
                            English to
                                "If a claim has been submitted against your back payment, the payment to you may be delayed. " +
                                "Deductions from the back payment will be stated in the disbursement notice.",
                        )
                    }
                }
                showIf(etterbetaling.frivilligSkattetrekk_safe.equalTo(true)) {
                    paragraph {
                        text(
                            Bokmal to
                                "Du har tidligere oppgitt frivillig skattetrekk på barnepensjonen. " +
                                "Det samme skattetrekket vil bli brukt på etterbetalingen.",
                            Nynorsk to
                                "Du har tidlegare oppgitt frivillig skattetrekk på barnepensjonen. " +
                                "Det same skattetrekket vil bli brukt på etterbetalinga.",
                            English to
                                "You have previously registered a voluntary tax deduction on your children’s pension. " +
                                "The same tax deduction will be applied to the back payment. ",
                        )
                    }
                } orShow {
                    showIf(etterbetaling.etterbetalingPeriodeValg_safe.equalTo(EtterbetalingPeriodeValg.UNDER_3_MND)) {
                        paragraph {
                            text(
                                Bokmal to
                                    "Vær oppmerksom på at det ikke blir trukket skatt av etterbetalingen fordi det " +
                                    "ikke er registrert frivillig skattetrekk.",
                                Nynorsk to
                                    "Ver merksam på at det ikkje blir trekt skatt av etterbetalinga, då det " +
                                    "ikkje er registrert frivillig skattetrekk.",
                                English to
                                    "Please note that no tax is deducted from the back payment because no voluntary " +
                                    "tax deduction has been registered.",
                            )
                        }
                    }
                    showIf(etterbetaling.etterbetalingPeriodeValg_safe.equalTo(EtterbetalingPeriodeValg.FRA_3_MND)) {
                        paragraph {
                            text(
                                Bokmal to
                                    "Det er ikke registrert frivillig skattetrekk for utbetaling av barnepensjonen. " +
                                    "Vi må få beskjed innen tre uker om du ønsker at vi skal trekke skatt på etterbetalingen. " +
                                    "Dette sikrer at skatten blir riktig og gir mindre risiko for restskatt.",
                                Nynorsk to
                                    "Det er ikkje registrert frivillig skattetrekk for utbetaling av barnepensjonen. " +
                                    "Vi må få beskjed innan tre veker dersom du vil at vi skal trekkje skatt på etterbetalinga. " +
                                    "Dette sikrar at skatten blir rett, og gir mindre risiko for restskatt.",
                                English to
                                    "There is no registered voluntary tax deduction on the payment of your children's pension. " +
                                    "We must be notified within three weeks if you want us to deduct tax from the back payment. " +
                                    "This ensures that your tax payment is correct and minimises the risk of back taxes.",
                            )
                        }
                    }
                }
            }
            showIf(brukerUnder18Aar) {
                paragraph {
                    text(
                        Bokmal to
                            "Du kan lese mer om frivillig skattetrekk og skatt i vedlegget " +
                            "“Informasjon til deg som handler på vegne av barnet”.",
                        Nynorsk to
                            "Du kan lese meir om frivillig skattetrekk og skatt i vedlegget " +
                            "«Informasjon til deg som handlar på vegner av barnet». ",
                        English to
                            "You can read more about voluntary withholding tax and tax in the appendix, " +
                            "Information for those acting on Behalf of the Child.",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to
                            "Du kan lese mer om frivillig skattetrekk og skatt i vedlegget " +
                            "“Informasjon til deg som mottar barnepensjon”.",
                        Nynorsk to
                            "Du kan lese meir om skattetrekk i vedlegget " +
                            "«Informasjon til deg som får barnepensjon».",
                        English to
                            "You can read more about voluntary withholding tax and tax in the appendix, " +
                            "Information to recipients of children’s pensions.",
                    )
                }
            }
        }
    }
}
