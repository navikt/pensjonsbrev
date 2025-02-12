package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class OmstillingsstoenadForhaandsvarselFraser {
    object ForhaandsvarselRedigerbart : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Som følge av vedtaket som er gjort har du fått <beløp> kroner for " +
                        "mye utbetalt i omstillingsstønad fra og med <dato> til og med <dato>.",
                    Language.Nynorsk to "Som følgje av vedtaket som er gjort, har du fått utbetalt <beløp> " +
                        "kroner for mykje i omstillingsstønad frå og med <dato> til og med <dato>.",
                    Language.English to "As a result of a decision that has been made you have received NOK " +
                        "<beløp> overpaid in adjustment allowance from and including <dato> up to and including <dato>.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Før vi avgjør om du skal betale tilbake, har du rett til å uttale deg. " +
                        "Dette må du gjøre innen 14 dager etter at du har fått dette varselet.",
                    Language.Nynorsk to "Før vi avgjer om du skal betale tilbake, har du rett til å uttale deg. " +
                        "Dette må du gjere innan 14 dagar etter at du har fått dette varselet.",
                    Language.English to "Before we decide whether you need to repay any amount, you have the " +
                        "right to state your views. You must do this within 14 days of receiving this notice.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dette er kun et varsel om at vi vurderer å kreve tilbake det " +
                        "feilutbetalte beløpet. Det er ikke et vedtak om tilbakekreving.",
                    Language.Nynorsk to "Dette er berre eit varsel om at vi vurderer å krevje tilbake det " +
                        "feilutbetalte beløpet. Det er ikkje eit vedtak om at pengane blir kravde tilbake.",
                    Language.English to "This is only an advance notice that we are considering repayment of " +
                        "the incorrectly paid amount. It is not a formal decision demanding repayment.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom vi vedtar at du må betale tilbake hele eller deler av det " +
                        "feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake.",
                    Language.Nynorsk to "Dersom vi vedtek at du må betale tilbake heile eller delar av det " +
                        "feilutbetalte beløpet, vil vi trekkje frå skatt på beløpet vi krev tilbake.",
                    Language.English to "If we make a formal decision that you must repay all or some of the " +
                        "incorrectly paid amount, we will deduct the tax payable on the amount we require to " +
                        "be repaid. ",
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Dette har skjedd",
                    Language.Nynorsk to "Dette har skjedd",
                    Language.English to "The following has occurred:",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "<Fritekst. Eks.: Vi innvilget omstillingsstønad til deg selv om " +
                        "vilkårene ikke var oppfylt. Dette har medført en feilutbetaling til deg. > ",
                    Language.Nynorsk to "<Fritekst. Døme: Du fekk innvilga omstillingsstønad frå oss trass " +
                        "i at vilkåra ikkje var oppfylte. Dette har ført til at du har fått ei feilutbetaling. >",
                    Language.English to "<Fritekst. Eks.: We granted you an adjustment allowance even though " +
                        "some of the conditions were not met. This has resulted in an incorrect payment to you. >",
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Dette legger vi vekt på i vurderingen vår",
                    Language.Nynorsk to "Dette legg vi vekt på i vurderinga vår",
                    Language.English to "We will place emphasis on the following in our assessment:",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis Nav har forårsaket feilutbetalingen, kan vi kreve tilbake " +
                        "feilutbetalt beløp hvis du forstod eller burde forstått at utbetalingen du fikk, " +
                        "skyldtes en feil. Vi legger blant annet vekt på",
                    Language.Nynorsk to "Dersom Nav har forårsaka feilutbetalinga, kan vi krevje tilbake " +
                        "feilutbetalt beløp dersom du forstod eller burde ha forstått at utbetalinga du fekk, " +
                        "skuldast ein feil. Vi legg vekt på mellom anna",
                    Language.English to "If Nav has caused an incorrect amount to be paid, we can demand " +
                        "repayment if you understood, or should have understood, that the payment you received " +
                        "was due to an error. We will place emphasis on, among other things",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "hvilken informasjon du har fått fra oss, og kvaliteten på denne",
                            Language.Nynorsk to "kva og kor god informasjon du har fått frå oss",
                            Language.English to "which information you have received from us and the quality of " +
                                "the information ",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "hvordan du har opptrådt i forbindelse med saken din",
                            Language.Nynorsk to "korleis du har opptredd i samband med saka",
                            Language.English to "how you have conducted yourself in connection with your case",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Language.Bokmal to "Vi kan også kreve tilbake feilutbetalt beløp hvis feilutbetalingen " +
                        "skyldes at du uaktsomt har gitt mangelfulle eller feil opplysninger. Vi legger blant " +
                        "annet vekt på ",
                    Language.Nynorsk to "Vi kan også krevje tilbake feilutbetalt beløp dersom feilutbetalinga " +
                        "skuldast at du aktlaust har gitt mangelfulle eller feil opplysningar. Vi legg vekt på " +
                        "mellom anna",
                    Language.English to "We can also demand repayment of incorrectly paid amounts if this has " +
                        "been caused by you negligently providing inadequate or incorrect information. We will " +
                        "place emphasis on, among other things",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "om du har fått god informasjon fra oss om dine rettigheter og plikter",
                            Language.Nynorsk to "om du har fått god informasjon frå oss om rettane og pliktene dine",
                            Language.English to "whether you have received good information from us regarding " +
                                "your rights and obligations",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "om du har gitt riktig informasjon til oss",
                            Language.Nynorsk to "om du har gitt oss rett informasjon",
                            Language.English to "whether you have provided us with correct information",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "om du har gitt all nødvendig informasjon til oss i rett tid",
                            Language.Nynorsk to "om du har gitt all nødvendig informasjon i rett tid",
                            Language.English to "whether you have provided us with all necessary information " +
                                "within stipulated deadlines",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Language.Bokmal to "Vi vil vurdere om det er særlige grunner til å redusere kravet. " +
                        "Da legger vi vekt på",
                    Language.Nynorsk to "Vi vil vurdere om det er særlege grunnar til å redusere kravet. " +
                        "Her legg vi vekt på",
                    Language.English to "We will evaluate whether there are special grounds to reduce the " +
                        "repayment demand. In such case, we will place emphasis on",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "hvor aktsom du har vært",
                            Language.Nynorsk to "kor aktsam du har vore",
                            Language.English to "how diligent you have been",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "hvor mye vi kan bebreides",
                            Language.Nynorsk to "i kor stor grad vi er å skulde",
                            Language.English to "how much we are to blame ",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "hvor lenge det er siden feilutbetalingen skjedde",
                            Language.Nynorsk to "kor lenge det er sidan feilutbetalinga",
                            Language.English to "the length of time that has passed since the incorrect " +
                                "payment was made",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "størrelsen på det feilutbetalte beløpet",
                            Language.Nynorsk to "kor stort det feilutbetalte beløpet er",
                            Language.English to "the amount involved",
                        )
                    }
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Dette går fram av folketrygdloven § 22-15 og rettspraksis.",
                    Language.Nynorsk to "Dette går fram av folketrygdlova § 22-15 og rettspraksis.",
                    Language.English to "This as stated in the National Insurance Act Section 22-15 and " +
                        "legal practice.",
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Foreløpig vurdering",
                    Language.Nynorsk to "Førebels vurdering",
                    Language.English to "Provisional assessment",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Vår foreløpige vurdering er at du < forstod eller burde forstått at " +
                        "beløpet du fikk, skyldtes en feil. // ikke forstod eller burde forstått at beløpet du " +
                        "fikk, skyldtes en feil. // har gitt oss mangelfulle opplysninger // har gitt oss " +
                        "feil opplysninger // har gitt oss nødvendige opplysninger for sent ///, selv om du " +
                        "har fått god informasjon fra oss. //, fordi du ikke har fått god nok informasjon " +
                        "fra oss.> ",
                    Language.Nynorsk to "Vurderinga vår så langt er at du < forstod eller burde ha forstått " +
                        "at beløpet du fekk, skuldast ein feil. // ikkje forstod eller kunne forventast å " +
                        "forstå at beløpet du fekk, skuldast ein feil. // har gitt oss mangelfulle " +
                        "opplysningar // har gitt oss feil opplysningar // ga oss dei nødvendige opplysningane " +
                        "for seint ///, sjølv om du har fått god informasjon frå oss. //, fordi du ikkje fekk " +
                        "god nok informasjon frå oss.> ",
                    Language.English to "Our provisional assessment is that you < understood or should have " +
                        "understood that the amount you received was due to an error. // not understood or " +
                        "should have understood that the amount you received was due to an error. // have given " +
                        "us insufficient information // have given us incorrect information // have given us " +
                        "the necessary information to late ///, even after receiving adequate information from " +
                        "us. //, because you have not received adequate information from us. > ",
                )
            }
        }
    }
}
