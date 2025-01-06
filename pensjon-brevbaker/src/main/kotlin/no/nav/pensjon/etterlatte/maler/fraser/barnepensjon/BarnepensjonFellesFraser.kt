package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon

object BarnepensjonFellesFraser {

    object DuHarTidligereAvslagViHarFaattNyeOplysninger : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Du har tidligere fått et foreløpig avslag på søknaden din om barnepensjon fordi du ikke hadde rett på pensjonen kun vurdert etter nasjonale regler. Avslaget var gitt i påvente av opplysninger fra utenlandske trygdemyndigheter.",
                    Nynorsk to "Du har tidlegare fått et foreløpig avslag på søknaden din om barnepensjon fordi du ikkje hadde rett på pensjonen berre vurdert etter nasjonale regler. Avslaget var gitt i påvente av opplysningar frå utanlandske trygdemyndigheiter.",
                    English to "You previously received a preliminary rejection of your application for children`s pension because you were assessed only according to national rules, which did not entitle you to the pension. The rejection was issued pending information from foreign social security authorities.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi har nå mottatt opplysninger fra utenlandske trygdemyndigheter, som gjør at du har rett på pensjonen etter EØS/avtalelandreglene heller.",
                    Nynorsk to "Vi har no mottatt opplysningar frå utanlandske trygdemyndigheiter, som gjer at du ikkje har rett på pensjonen vurdert etter EØS/avtalelandreglane heller.",
                    English to "We have now received information from foreign social security authorities, which means you are entitled to the pension under the EEA/agreement country rules either.",
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
                    English to "You are obligated to notify us of any changes that affect the payment of a children's pension, or the right to receive a children's pension. You will see which changes you must report in the attachment: Your rights and obligations.",
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
        val migrertYrkesskade: Expression<Boolean>,
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
                }.orShow {
                    text(
                        Bokmal to "20",
                        Nynorsk to "20",
                        English to "20",
                    )
                }
                text(
                    Bokmal to " år, så lenge du oppfyller vilkårene.",
                    Nynorsk to " år.",
                    English to ", as long as you satisfy the conditions.",
                )
            }
        }
    }

    data class UtbetalingAvBarnepensjon(
        val erEtterbetaling: Expression<Boolean>,
        val bosattUtland: Expression<Boolean>,
        val frivilligSkattetrekk: Expression<Boolean>
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

            showIf(bosattUtland) {
                paragraph {
                    text(
                        Bokmal to "Det trekkes 17 prosent skatt på alle utbetalinger av barnepensjon, så lenge det " +
                            "ikke foreligger vedtak om skattefritak.",
                        Nynorsk to "Dersom det ikkje føreligg vedtak om skattefritak, vert det trekt 17 prosent i " +
                            "skatt på all utbetaling av barnepensjon.",
                        English to "A tax of 17 percent is deducted from all payments of children's pension unless " +
                            "a tax exemption has been granted.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Hvis du mener at du ikke er skattepliktig til Norge, må du søke om fritak hos " +
                            "Skatteetaten. For at vi skal unngå å trekke skatt av barnepensjonen, må du sende vedtak " +
                            "om fritak til ${Constants.Utland.POSTADRESSE}. " +
                            "Har du allerede gjort dette, vil det ikke bli trukket skatt av barnepensjonen din. ",
                        Nynorsk to "Meiner du at du ikkje er skattepliktig til Noreg, må du søkje om fritak hos " +
                            "Skatteetaten. Du må sende kopi av vedtak om fritak til ${Constants.Utland.POSTADRESSE}, for at " +
                            "vi ikkje skal trekkje skatt av barnepensjonen. Har du allereie sendt oss vedtak om " +
                            "skattefritak, blir det ikkje trekt skatt på barnepensjonen.",
                        English to "If you believe you are not liable to pay tax in Norway, you must apply for an " +
                            "exemption from the Norwegian Tax Administration. To ensure we do not deduct tax from " +
                            "your children's pension, you must send the exemption decision to ${Constants.Utland.POSTADRESSE}. " +
                            "If you have already done so, no tax will be deducted from your children's pension. ",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "Det trekkes 17 prosent skatt på alle utbetalinger av barnepensjon, inkludert etterbetalinger.",
                        Nynorsk to "Det vert trekt 17 prosent i skatt på all utbetaling av barnepensjon. Også etterbetalingar.",
                        English to "17 percent tax is deducted from all payments of children's pension, including back payments.",
                    )
                }
            }

            showIf(frivilligSkattetrekk){
                paragraph {
                    text(
                        Bokmal to
                                "Du har bedt om ekstra skattetrekk utover 17 prosent. Det trekkes derfor ekstra skatt fra barnepensjonen din. Du kan se på utbetalingsmeldingen hvor mye som er trukket.",
                        Nynorsk to
                                "Du har bede om ekstra skattetrekk utover 17 prosent. Det blir derfor trekt ekstra skatt frå barnepensjonen din. Du kan sjå på utbetalingsmeldinga kor mykje som er trekt.",
                        English to
                                "You have requested additional tax deductions exceeding 17 percent. Additional tax is therefore deducted from your child's pension. You can see how much has been deducted on the disbursement notice.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to
                                "Du må melde fra ved hvert årsskifte dersom du ønsker å fortsette med ekstra skattetrekk.",
                        Nynorsk to
                                "Du må melda frå ved kvart årsskifte dersom du ønskjer å halda fram med ekstra skattetrekk.",
                        English to
                                "You must report at the end of each year if you wish to continue with the additional tax deductions.",
                    )
                }

            }

            paragraph {
                text(
                    Bokmal to
                            "Du kan lese mer om skattetrekk på ${Constants.BP_SKATTETREKK} og ${Constants.BP_SKATTEETATEN}.",
                    Nynorsk to
                            "Du kan lese meir om skattetrekk på ${Constants.BP_SKATTETREKK} og ${Constants.BP_SKATTEETATEN}.",
                    English to
                            "Read more about tax deductions at ${Constants.BP_SKATTETREKK} and ${Constants.BP_SKATTEETATEN}.",
                )
            }

            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Bokmal to "Du får etterbetalt pensjon. Vanligvis vil du få denne i løpet av tre uker. Hvis det er lagt inn krav i etterbetalingen kan denne bli forsinket. Beløpet som er trukket fra etterbetalingen vil gå frem av utbetalingsmeldingen.",
                        Nynorsk to "Du får etterbetalt pensjon. Vanlegvis vil du få denne i løpet av tre veker. Dersom det er lagt inn krav i etterbetalinga, kan denne bli forseinka. Beløpet som er trekt frå etterbetalinga, vil gå fram av utbetalingsmeldinga.",
                        English to
                            "You will receive a back payment on your pension. You will usually receive this back payment within three weeks. If a claim has been submitted against your back payment, the payment to you may be delayed. Deductions from the back payment will be stated in the disbursement notice.",
                    )
                }
            }
        }
    }
}
