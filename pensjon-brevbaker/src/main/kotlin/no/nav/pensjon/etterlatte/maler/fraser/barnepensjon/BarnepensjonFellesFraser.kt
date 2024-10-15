package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetalingSelectors.etterbetalingPeriodeValg_safe
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetalingSelectors.inneholderKrav_safe
import no.nav.pensjon.etterlatte.maler.EtterbetalingPeriodeValg
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon
import java.time.LocalDate

object BarnepensjonFellesFraser {

    data class DuHarTidligereAvslagMenErNaaInvilget(
        var virkningstidspunkt: Expression<LocalDate>,
        var avdoed: Expression<Avdoed>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningstidspunkt.format()
            val formatertDoedsdato = avdoed.doedsdato.format()

            paragraph {
                text(
                    Bokmal to "Du har tidligere fått et foreløpig avslag på søknaden din om barnepensjon fordi du ikke hadde rett på pensjonen kun vurdert etter nasjonale regler. Avslaget var gitt i påvente av opplysninger fra utenlandske trygdemyndigheter.",
                    Nynorsk to "Du har tidlegare fått et foreløpig avslag på søknaden din om barnepensjon fordi du ikkje hadde rett på pensjonen berre vurdert etter nasjonale regler. Avslaget var gitt i påvente av opplysningar frå utanlandske trygdemyndigheiter.",
                    English to "You previously received a preliminary rejection of your application for children`s pension because you were assessed only according to national rules, which did not entitle you to the pension. The rejection was issued pending information from foreign social security authorities.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi har nå mottatt opplysninger fra utenlandske trygdemyndigheter, som gjør at du har rett på pensjonen etter EØS-reglene.",
                    Nynorsk to "Vi har no mottatt opplysningar frå utanlandske trygdemyndigheiter, som gjer at du ikkje har rett på pensjonen vurdert etter EØS-reglene heller.",
                    English to "We have now received information from foreign social security authorities, which means you are entitled to the pension under the EEA rules.",
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoed.navn + " døde " + formatertDoedsdato + ".",
                    Nynorsk to "Du har fått innvilga omstillingsstønad frå ".expr() + formatertVirkningsdato +
                            ", ettersom " + avdoed.navn + " døydde " + formatertDoedsdato + ".",
                    English to "You have been granted adjustment allowance starting ".expr() +
                            formatertVirkningsdato + " because " + avdoed.navn + " died on " + formatertDoedsdato + ".",
                )
            }

        }
    }



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
                    English to "You are obligated to notify us of any changes that affect the payment of a children's pension, or the right to receive a children's pension. You will see which changes you must report in the attachment: Your Rights and Obligations.",
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
                    English to "Do you have any questions?",
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
                        Bokmal to ", hverdager mellom klokken 09.00-15.00. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
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
                        Nynorsk to ", kvardagar mellom klokka 09.00–15.00. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret ditt.",
                        English to ", Monday to Friday between 9:00 AM and 3:00 PM. If you provide your national identity number, we can more easily provide you with quick and good help."
                    )
                }
            }
        }
    }

    data class HvorLengeKanDuFaaBarnepensjon(
        val migrertYrkesskade: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
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
                    Bokmal to "Du er innvilget barnepensjon til og med den kalendermåneden du fyller ",
                    Nynorsk to "Under føresetnad av at du innfrir vilkåra, kan du få barnepensjon fram til og med kalendermånaden du fyller ",
                    English to "You will receive the children’s pension until and including the calendar month in which you turn 20 ",
                )
                showIf(migrertYrkesskade) {
                    text(
                        Bokmal to "21",
                        Nynorsk to "21",
                        English to "21",
                    )
                }.orShow { text(
                    Bokmal to "20",
                    Nynorsk to "20",
                    English to "20",
                ) }
                text(
                    Bokmal to " år, så lenge du oppfyller vilkårene.",
                    Nynorsk to " år.",
                    English to ", as long as you satisfy the conditions.",
                )
            }
        }
    }

    data class UtbetalingAvBarnepensjon(
        val etterbetaling: Expression<BarnepensjonEtterbetaling?>,
        val frivilligSkattetrekk: Expression<Boolean>,
        val bosattUtland: Expression<Boolean>,
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

            showIf(bosattUtland.not()) {
                paragraph {
                    text(
                        Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt " +
                            "beskjed om det. Du kan legge til et frivillig skattetrekk som en prosentandel av pensjonen " +
                            "eller som et fast beløp. Dette sikrer at skatten blir riktig og gir mindre risiko for restskatt.",
                        Nynorsk to "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt av beløpet utan at " +
                            "det er avtalt. Du kan leggje til eit frivillig skattetrekk anten som prosentdel av pensjonen " +
                            "eller som fast beløp. Dette sikrar at skatten blir rett, og gir mindre risiko for restskatt.",
                        English to "A children’s pension is taxable, but we do not deduction tax from the amount unless " +
                            "we have agreed with you to do so. You can add a voluntary tax deduction as a percentage of " +
                            "your pension or as a fixed amount. This ensures that your tax payment is correct, " +
                            "and it minimises the risk of back taxes.",
                    )
                }
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
                showIf(frivilligSkattetrekk.equalTo(true)) {
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
            }.orShow {
                showIf(frivilligSkattetrekk.equalTo(true)) {
                    paragraph {
                        text(
                            Bokmal to
                                "Du har oppgitt frivillig skattetrekk på barnepensjonen. Dette videreføres " +
                                "inntil du melder fra om endring.",
                            Nynorsk to
                                "Du har oppgitt frivillig skattetrekk på barnepensjonen. Dette vert vidareført inntil " +
                                "du melde frå om endring.",
                            English to
                                "You have registered a voluntary tax deduction on your children’s pension. " +
                                "This will continue until you notify us the change.",
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            Bokmal to
                                "For å unngå eventuell restskatt, anbefaler vi å legge til et frivillig skattetrekk " +
                                "på barnepensjonen. Ta kontakt med Skatteetaten dersom du har spørsmål om skattetrekk.",
                            Nynorsk to
                                "Vi anbefaler deg å leggje inn eit frivillig skattetrekk på barnepensjonen for å " +
                                "unngå restskatt. Ta kontakt med Skatteetaten dersom du har spørsmål om skattetrekk.",
                            English to
                                "To avoid any underpaid tax, we recommend adding a voluntary tax deduction to the " +
                                "children's pension. Contact the Tax Administration if you have questions about tax deductions.",
                        )
                    }
                }
            }

            showIf(bosattUtland) {
                paragraph {
                    text(
                        Bokmal to
                            "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. " +
                            "Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikke er skattemessig " +
                            "bosatt i Norge. Les mer om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                        Nynorsk to
                            "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt utan at du har gitt beskjed om det. " +
                            "Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikkje er skattemessig " +
                            "busett i Noreg. Les meir om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                        English to
                            "Children’s pension is taxable; however, we do not deduct tax if you do not notify us to do so. " +
                            "The Tax Administration will respond to any queries regarding tax on pensions for those " +
                            "who are resident in Norway for tax purposes. Read more about tax at: ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to
                            "Du kan lese mer om skattetrekk på ${Constants.BP_SKATTETREKK}.",
                        Nynorsk to
                            "Du kan lese meir om skattetrekk på ${Constants.BP_SKATTETREKK}.",
                        English to
                            "Read more about tax deductions at ${Constants.BP_SKATTETREKK}.",
                    )
                }
            }
        }
    }
}
