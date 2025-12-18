package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class OmstillingsstoenadForhaandsvarselFraser {

    object ForhaandsvarselRedigerbart : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Som følge av vedtaket som er gjort har du fått <beløp> kroner for " +
                            "mye utbetalt i omstillingsstønad fra og med <dato> til og med <dato>." },
                    nynorsk { +"Som følgje av vedtaket som er gjort, har du fått utbetalt <beløp> " +
                            "kroner for mykje i omstillingsstønad frå og med <dato> til og med <dato>." },
                    english { +"As a result of a decision that has been made you have received NOK " +
                            "<beløp> overpaid in adjustment allowance from and including <dato> up to and including <dato>." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Før vi avgjør om du skal betale tilbake, har du rett til å uttale deg. " +
                            "Dette må du gjøre innen 14 dager etter at du har fått dette varselet." },
                    nynorsk { +"Før vi avgjer om du skal betale tilbake, har du rett til å uttale deg. " +
                            "Dette må du gjere innan 14 dagar etter at du har fått dette varselet." },
                    english { +"Before we decide whether you need to repay any amount, you have the " +
                            "right to state your views. You must do this within 14 days of receiving this notice." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Dette er kun et varsel om at vi vurderer å kreve tilbake det " +
                            "feilutbetalte beløpet. Det er ikke et vedtak om tilbakekreving." },
                    nynorsk { +"Dette er berre eit varsel om at vi vurderer å krevje tilbake det " +
                            "feilutbetalte beløpet. Det er ikkje eit vedtak om at pengane blir kravde tilbake." },
                    english { +"This is only an advance notice that we are considering repayment of " +
                            "the incorrectly paid amount. It is not a formal decision demanding repayment." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Dersom vi vedtar at du må betale tilbake hele eller deler av det " +
                            "feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake." },
                    nynorsk { +"Dersom vi vedtek at du må betale tilbake heile eller delar av det " +
                            "feilutbetalte beløpet, vil vi trekkje frå skatt på beløpet vi krev tilbake." },
                    english { +"If we make a formal decision that you must repay all or some of the " +
                            "incorrectly paid amount, we will deduct the tax payable on the amount we require to " +
                            "be repaid. " },
                )
            }

            title2 {
                text(
                    bokmal { +"Dette har skjedd" },
                    nynorsk { +"Dette har skjedd" },
                    english { +"The following has occurred:" },
                )
            }
            paragraph {
                text(
                    bokmal { +"<Fritekst. Eks.: Vi innvilget omstillingsstønad til deg selv om " +
                            "vilkårene ikke var oppfylt. Dette har medført en feilutbetaling til deg. > " },
                    nynorsk { +"<Fritekst. Døme: Du fekk innvilga omstillingsstønad frå oss trass " +
                            "i at vilkåra ikkje var oppfylte. Dette har ført til at du har fått ei feilutbetaling. >" },
                    english { +"<Fritekst. Eks.: We granted you an adjustment allowance even though " +
                            "some of the conditions were not met. This has resulted in an incorrect payment to you. >" },
                )
            }

            title2 {
                text(
                    bokmal { +"Dette legger vi vekt på i vurderingen vår" },
                    nynorsk { +"Dette legg vi vekt på i vurderinga vår" },
                    english { +"We will place emphasis on the following in our assessment:" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis Nav har forårsaket feilutbetalingen, kan vi kreve tilbake " +
                            "feilutbetalt beløp hvis du forstod eller burde forstått at utbetalingen du fikk, " +
                            "skyldtes en feil. Vi legger blant annet vekt på" },
                    nynorsk { +"Dersom Nav har forårsaka feilutbetalinga, kan vi krevje tilbake " +
                            "feilutbetalt beløp dersom du forstod eller burde ha forstått at utbetalinga du fekk, " +
                            "skuldast ein feil. Vi legg vekt på mellom anna" },
                    english { +"If Nav has caused an incorrect amount to be paid, we can demand " +
                            "repayment if you understood, or should have understood, that the payment you received " +
                            "was due to an error. We will place emphasis on, among other things" },
                )
                list {
                    item {
                        text(
                            bokmal { +"hvilken informasjon du har fått fra oss, og kvaliteten på denne" },
                            nynorsk { +"kva og kor god informasjon du har fått frå oss" },
                            english { +"which information you have received from us and the quality of " +
                                    "the information " },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvordan du har opptrådt i forbindelse med saken din" },
                            nynorsk { +"korleis du har opptredd i samband med saka" },
                            english { +"how you have conducted yourself in connection with your case" },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Vi kan også kreve tilbake feilutbetalt beløp hvis feilutbetalingen " +
                            "skyldes at du uaktsomt har gitt mangelfulle eller feil opplysninger. Vi legger blant " +
                            "annet vekt på " },
                    nynorsk { +"Vi kan også krevje tilbake feilutbetalt beløp dersom feilutbetalinga " +
                            "skuldast at du aktlaust har gitt mangelfulle eller feil opplysningar. Vi legg vekt på " +
                            "mellom anna" },
                    english { +"We can also demand repayment of incorrectly paid amounts if this has " +
                            "been caused by you negligently providing inadequate or incorrect information. We will " +
                            "place emphasis on, among other things" },
                )
                list {
                    item {
                        text(
                            bokmal { +"om du har fått god informasjon fra oss om dine rettigheter og plikter" },
                            nynorsk { +"om du har fått god informasjon frå oss om rettane og pliktene dine" },
                            english { +"whether you have received good information from us regarding " +
                                    "your rights and obligations" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"om du har gitt riktig informasjon til oss" },
                            nynorsk { +"om du har gitt oss rett informasjon" },
                            english { +"whether you have provided us with correct information" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"om du har gitt all nødvendig informasjon til oss i rett tid" },
                            nynorsk { +"om du har gitt all nødvendig informasjon i rett tid" },
                            english { +"whether you have provided us with all necessary information " +
                                    "within stipulated deadlines" },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Vi vil vurdere om det er særlige grunner til å redusere kravet. " +
                            "Da legger vi vekt på" },
                    nynorsk { +"Vi vil vurdere om det er særlege grunnar til å redusere kravet. " +
                            "Her legg vi vekt på" },
                    english { +"We will evaluate whether there are special grounds to reduce the " +
                            "repayment demand. In such case, we will place emphasis on" },
                )
                list {
                    item {
                        text(
                            bokmal { +"hvor aktsom du har vært" },
                            nynorsk { +"kor aktsam du har vore" },
                            english { +"how diligent you have been" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvor mye vi kan bebreides" },
                            nynorsk { +"i kor stor grad vi er å skulde" },
                            english { +"how much we are to blame " },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvor lenge det er siden feilutbetalingen skjedde" },
                            nynorsk { +"kor lenge det er sidan feilutbetalinga" },
                            english { +"the length of time that has passed since the incorrect " +
                                    "payment was made" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"størrelsen på det feilutbetalte beløpet" },
                            nynorsk { +"kor stort det feilutbetalte beløpet er" },
                            english { +"the amount involved" },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Dette går fram av folketrygdloven § 22-15 og rettspraksis." },
                    nynorsk { +"Dette går fram av folketrygdlova § 22-15 og rettspraksis." },
                    english { +"This as stated in the National Insurance Act Section 22-15 and " +
                            "legal practice." },
                )
            }

            title2 {
                text(
                    bokmal { +"Foreløpig vurdering" },
                    nynorsk { +"Førebels vurdering" },
                    english { +"Provisional assessment" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vår foreløpige vurdering er at du < forstod eller burde forstått at " +
                            "beløpet du fikk, skyldtes en feil. // ikke forstod eller burde forstått at beløpet du " +
                            "fikk, skyldtes en feil. // har gitt oss mangelfulle opplysninger // har gitt oss " +
                            "feil opplysninger // har gitt oss nødvendige opplysninger for sent ///, selv om du " +
                            "har fått god informasjon fra oss. //, fordi du ikke har fått god nok informasjon " +
                            "fra oss.> " },
                    nynorsk { +"Vurderinga vår så langt er at du < forstod eller burde ha forstått " +
                            "at beløpet du fekk, skuldast ein feil. // ikkje forstod eller kunne forventast å " +
                            "forstå at beløpet du fekk, skuldast ein feil. // har gitt oss mangelfulle " +
                            "opplysningar // har gitt oss feil opplysningar // ga oss dei nødvendige opplysningane " +
                            "for seint ///, sjølv om du har fått god informasjon frå oss. //, fordi du ikkje fekk " +
                            "god nok informasjon frå oss.> " },
                    english { +"Our provisional assessment is that you < understood or should have " +
                            "understood that the amount you received was due to an error. // not understood or " +
                            "should have understood that the amount you received was due to an error. // have given " +
                            "us insufficient information // have given us incorrect information // have given us " +
                            "the necessary information to late ///, even after receiving adequate information from " +
                            "us. //, because you have not received adequate information from us. > " },
                )
            }
        }
    }

}