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
                        English to "For more information, visit us online: ${Constants.Engelsk.BARNEPENSJON_URL}. If you cannot find the answer to your question, you can call us by phone ("
                    )
                    kontakttelefonPensjon(bosattUtland)
                    text(
                        Bokmal to " hverdager mellom kl. 09.00-15.00. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                        Nynorsk to ", kvardagar mellom kl. 09.00–15.00. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret til barnet.",
                        English to ") weekdays between 09.00-15.00. If you provide your child's national identity number, we can more easily provide you with quick and good help."
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon ",
                        Nynorsk to "Du finn meir informasjon på ${Constants.BARNEPENSJON_URL}. Dersom du ikkje finn svar på spørsmålet ditt der, kan du ringje oss på telefon ",
                        English to "For more information, visit us online: ${Constants.Engelsk.BARNEPENSJON_URL}. If you cannot find the answer to your question, you can call us by phone ("
                    )
                    kontakttelefonPensjon(bosattUtland)
                    text(
                        Bokmal to " hverdager mellom kl. 09.00-15.00. Om du oppgir fødselsnummeret ditt, kan vi lettere gi deg rask og god hjelp.",
                        Nynorsk to ", kvardagar mellom kl. 09.00–15.00. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret ditt.",
                        English to ") weekdays between 09.00-15.00. If you provide your national identity number, we can more easily provide you with quick and good help."
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

            showIf(brukerUnder18Aar) {
                paragraph {
                    text(
                        Bokmal to "Barnepensjon er skattepliktig, men ikke trekkpliktig. Du kan lese mer om skattetrekk i vedlegget “Informasjon til deg som handler på vegne av barnet”.",
                        Nynorsk to "Barnepensjon er skattepliktig, men ikkje trekkpliktig. Du kan lese meir om skattetrekk i vedlegget “Informasjon til deg som handlar på vegne av barnet”.",
                        English to "Child pension is taxable, but not subject to withholding tax. You can read more about tax deductions in the attachment “Information for those acting on behalf of the child”.",
                        )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "Barnepensjon er skattepliktig, men ikke trekkpliktig. Du kan lese mer om skattetrekk i vedlegget “Informasjon til deg som mottar barnepensjon”.",
                        Nynorsk to "Barnepensjon er skattepliktig, men ikkje trekkpliktig. Du kan lese meir om skattetrekk i vedlegget “Informasjon til deg som får barnepensjon”.",
                        English to "Child pension is taxable, but not subject to withholding tax. You can read more about tax deductions in the attachment “Information to recipients of children’s pensions”.",
                    )
                }
                showIf(etterbetaling.inneholderKrav_safe.equalTo(true)) {
                    paragraph {
                        text(
                            Bokmal to
                                "Hvis det er lagt inn krav i etterbetalingen kan denne bli forsinket. " +
                                "Beløpet som er trukket fra etterbetalingen vil gå frem av utbetalingsmeldingen.",
                            Nynorsk to "todo",
                            English to "todo",
                        )
                    }
                }
                title2 {
                    text(
                        Bokmal to "Skatt på etterbetaling",
                        Nynorsk to "todo",
                        English to "todo",
                    )
                }
                showIf(etterbetaling.frivilligSkattetrekk_safe.equalTo(true)) {
                    paragraph {
                        text(
                            Bokmal to
                                "Du har tidligere oppgitt frivillig skattetrekk på barnepensjonen. " +
                                "Det samme skattetrekket vil bli brukt på etterbetalingen.",
                            Nynorsk to "todo",
                            English to "todo",
                        )
                    }
                } orShow {
                    showIf(etterbetaling.etterbetalingPeriodeValg_safe.equalTo(EtterbetalingPeriodeValg.UNDER_3_MND)) {
                        paragraph {
                            text(
                                Bokmal to
                                        "Vær oppmerksom på at det ikke blir trukket skatt av etterbetalingen fordi det " +
                                        "ikke er registrert frivillig skattetrekk.",
                                Nynorsk to "todo",
                                English to "todo",
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
                                Nynorsk to "todo",
                                English to "todo",
                            )
                        }
                    }
                }
                showIf(brukerUnder18Aar) {
                    paragraph {
                        text(
                            Bokmal to
                                    "Du kan lese mer om frivillig skattetrekk og skatt i vedlegget " +
                                    "“Informasjon til deg som handler på vegne av barnet”.",
                            Nynorsk to "todo",
                            English to "todo",
                        )
                    }
                } orShow {
                    paragraph {
                        text(
                            Bokmal to
                                    "Du kan lese mer om frivillig skattetrekk og skatt i vedlegget " +
                                    "“Informasjon til deg som mottar barnepensjon”. ",
                            Nynorsk to "todo",
                            English to "todo",
                        )
                    }
                }
            }
        }
    }
}
