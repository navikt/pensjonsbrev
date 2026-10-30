package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Felles {
    object BlankTekst : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"" },
                    nynorsk { +"" },
                    english { +"" },
                )
            }
        }
    }

    object HjelpFraAndreForvaltningsloven12 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Hjelp fra andre - forvaltningsloven § 12" },
                    nynorsk { +"Hjelp frå andre – forvaltingslova § 12" },
                    english { +"Help from others – Section 12 of the Public Administration Act" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, " +
                            "rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som " +
                            "hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne " +
                            "skjemaet du finner på ${Constants.FULLMAKT_URL}." },
                    nynorsk { +"Du har under heile saksbehandlinga høve til å be om hjelp frå til dømes advokat, " +
                            "rettshjelpar, organisasjonar du er medlem av, eller andre myndige personar. " +
                            "Dersom personen som hjelper deg, ikkje er advokat, " +
                            "må du gi vedkomande ei skriftleg fullmakt. " +
                            "Bruk gjerne skjemaet du finn på ${Constants.FULLMAKT_URL}." },
                    english { +"You can ask for help from others throughout case processing, such as an attorney, " +
                            "legal aid, an organization of which you are a member or another person of legal age. " +
                            "If the person helping you is not an attorney, " +
                            "you must give this person a written power of attorney. " +
                            "Feel free to use the form you find here:  ${Constants.Engelsk.FULLMAKT_URL}." },
                )
            }
        }
    }

    object SlikUttalerDuDegOmstillingsstoenad : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Slik uttaler du deg" },
                    nynorsk { +"Slik uttaler du deg" },
                    english { +"How to provide a statement" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan sende uttalelsen din ved å logge deg inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Du kan også sende " +
                            "uttalelsen din til oss i posten. Adressen finner du på ${Constants.ETTERSENDELSE_URL}." },
                    nynorsk { +"Du kan sende svar til oss ved å logge inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Alternativt kan du " +
                            "sende oss svar i posten. Adressa finn du på ${Constants.ETTERSENDELSE_URL}." },
                    english { +"You can send us a statement regarding the matter by logging in to: " +
                            "${Constants.Engelsk.BESKJED_TIL_NAV_URL}. You can also " +
                            "send us your statement by post. The address can be found at: ${Constants.Engelsk.ETTERSENDELSE_URL}." },
                )
            }
        }
    }

    object SlikUttalerDuDegBarnepensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Slik uttaler du deg" },
                    nynorsk { +"Slik uttaler du deg" },
                    english { +"How to provide a statement" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan sende uttalelsen din ved å logge deg inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Du kan også sende " +
                            "uttalelsen din til oss i posten. Adressen finner du på ${Constants.BP_MELDE}." },
                    nynorsk { +"Du kan sende svar til oss ved å logge inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Alternativt kan du " +
                            "sende oss svar i posten. Adressa finn du på ${Constants.BP_MELDE}." },
                    english { +"You can send us a statement regarding the matter by logging in to: " +
                            "${Constants.Engelsk.BESKJED_TIL_NAV_URL}. You can also " +
                            "send us your statement by post. The address can be found at: ${Constants.BP_MELDE}." },
                )
            }
        }
    }

    object HvaSkjerVidereIDinSak : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Hva skjer videre i din sak" },
                    nynorsk { +"Kva som skjer vidare i saka" },
                    english { +"What will happen further in your case" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Når fristen for uttale er gått ut, vil vi gjøre et vedtak og sende det " +
                            "til deg. Hvis du må betale tilbake hele eller deler av beløpet, gir vi beskjed i " +
                            "vedtaket om hvordan du betaler tilbake." },
                    nynorsk { +"Når svarfristen har gått ut, fattar vi eit vedtak og sender det til deg. " +
                            "Dersom du må betale tilbake heile eller delar av beløpet, forklarer vi i vedtaket " +
                            "korleis du betaler tilbake." },
                    english { +"When the deadline for providing statements has expired, we will make a decision " +
                            "and send it to you. If you are required to repay the whole or part of the amount, we will " +
                            "inform you in the decision of how to make repayments." },
                )
            }
        }
    }

    object HvordanMelderDuFra : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Hvordan melder du fra?" },
                    nynorsk { +"Slik melder du frå" },
                    english { +"How to notify us " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Meld fra til oss ved å benytte endringsskjema eller ettersende dokumentasjon på ${Constants.ETTERSENDE_OMS_URL}." },
                    nynorsk { +"Meld frå til oss ved å fylle ut endringsskjemaet eller ettersende dokumentasjon på ${Constants.ETTERSENDE_OMS_URL}." },
                    english { +"You can notify us by using the amendment form, and/or you can forward documentation at: ${Constants.ETTERSENDE_OMS_URL}." },
                )
            }
        }
    }

    data class HvordanSendeOpplysninger(
        val utland: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Hvordan sende opplysninger til oss?" },
                    nynorsk { +"Korleis melder du frå om endringar?" },
                    english { +"How to submit information to Nav?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan benytte endringsskjema ved å logge deg på på ${Constants.OMS_MELD_INN_ENDRING_URL}. " +
                        "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside ${Constants.NAV_URL}, " +
                        "kan du kontakte oss på telefon." },
                    nynorsk { +"Du kan bruke endringsskjema ved å logge på ${Constants.OMS_MELD_INN_ENDRING_URL}. " +
                        "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår ${Constants.NAV_URL}, " +
                        "kan du kontakte oss på telefon." },
                    english { +"You can log in to our website and use change form on ${Constants.OMS_MELD_INN_ENDRING_URL}. " +
                        "If you do not have BankID or another option to log in to our website, ${Constants.NAV_URL}, " +
                        "you can also call us." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Skal du sende oss noe per post må du bruke adressen" },
                    nynorsk { +"Skal du sende oss noko per post, bruker du adressa" },
                    english { +"If you are submitting anything through the mail, you must use the following address:" },
                )
            }
            includePhrase(AdresseMedMellomrom(utland))
        }
    }

    data class AdresseMedMellomrom(
        val utland: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(utland) {
                paragraph {
                    text(
                        bokmal { +"Nav familie- og pensjonsytelser" },
                        nynorsk { +"Nav familie- og pensjonsytelser" },
                        english { +"Nav familie- og pensjonsytelser" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Postboks 6600 Etterstad" },
                        nynorsk { +"Postboks 6600 Etterstad" },
                        english { +"Postboks 6600 Etterstad" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"0607 Oslo" },
                        nynorsk { +"0607 Oslo" },
                        english { +"0607 Oslo" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Norge/Norway" },
                        nynorsk { +"Norge/Norway" },
                        english { +"Norway" },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Nav skanning" },
                        nynorsk { +"Nav skanning" },
                        english { +"Nav skanning" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Postboks 1400" },
                        nynorsk { +"Postboks 1400" },
                        english { +"Postboks 1400" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"0109 Oslo" },
                        nynorsk { +"0109 Oslo" },
                        english { +"0109 Oslo" },
                    )
                }
            }
        }
    }

    object DuHarRettTilAaKlage : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du kan klage på vedtaket" },
                    nynorsk { +"Du kan klage på vedtaket" },
                    english { +"You have the right to appeal" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du mener vedtaket er feil, har du rett til å klage. Du må sende klagen innen " +
                            "6 uker fra den datoen vedtaket har kommet fram til deg. Dette følger av folketrygdloven " +
                            "§ 21-12. Du finner mer informasjon om hvordan du kan klage på ${Constants.KLAGE_URL}." },
                    nynorsk { +"Dersom du meiner vedtaket er feil, har du rett til å klage. Du må sende klaga innan " +
                            "6 veker frå den datoen du fekk vedtaket. Dette følgjer av folketrygdlova § 21-12. Du finn " +
                            "meir informasjon om korleis du kan klage på ${Constants.KLAGE_URL}." },
                    english { +"If you believe the decision is incorrect, you have the right to appeal. You must " +
                            "submit your appeal within 6 weeks from the date you received the decision. This is set " +
                            "out in Section 21-12 of the National Insurance Act. You can find more information on how " +
                            "to appeal at ${Constants.Engelsk.KLAGE_URL}." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Nav kan veilede deg på telefon hvis du trenger hjelp til å sende en klage. Du kan " +
                            "også få hjelp av Nav-kontoret ditt til å skrive en klage. Hvis du velger å sende klagen " +
                            "på papir, må du huske å signere den." },
                    nynorsk { +"Nav kan rettleie deg på telefon dersom du treng hjelp til å sende ei klage. Du kan " +
                            "òg få hjelp av Nav-kontoret ditt til å skrive ei klage. Dersom du vel å sende klaga på " +
                            "papir, må du hugse å signere ho." },
                    english { +"Nav can advise you over the phone if you need help submitting an appeal. You can " +
                            "also get help from your local Nav office to write an appeal. If you choose to submit " +
                            "your appeal on paper, you must remember to sign it." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Når du klager, vil den Nav-enheten som har skrevet vedtaket, vurdere saken din på " +
                            "nytt. Hvis du får medhold i klagen din, mottar du et nytt vedtak. Hvis du ikke får " +
                            "medhold, sender vi klagen videre til Nav klageinstans. Du får brev når saken er sendt " +
                            "dit. I brevet står det hva som skjer videre. Før en sak kan bringes inn for domstolene, " +
                            "må du ha brukt muligheten til å klage og anke. Dette følger av folketrygdloven § 21-12." },
                    nynorsk { +"Når du klagar, vil den Nav-eininga som har skrive vedtaket, vurdere saka di på " +
                            "nytt. Dersom du får medhald i klaga di, får du eit nytt vedtak. Dersom du ikkje får " +
                            "medhald, sender vi klaga vidare til Nav klageinstans. Du får brev når saka er send dit. " +
                            "I brevet står det kva som skjer vidare. Før ei sak kan bringast inn for domstolane, må " +
                            "du ha nytta høvet til å klage og anke. Det følgjer av folketrygdlova § 21-12." },
                    english { +"When you lodge an appeal, the Nav unit that issued the decision will reassess " +
                            "your case. If your appeal is upheld, you will receive a new decision. If your appeal is " +
                            "unsuccessful, we will forward it to the Nav Appeals (Nav klageinstans). You will receive " +
                            "a letter once the case has been forwarded there. The letter will explain the next " +
                            "steps. Before a case can be brought before the courts, you must have exhausted the " +
                            "options to appeal and lodge a further appeal. This is in accordance with Section 21-12 " +
                            "of the National Insurance Act." }
                )
            }
        }
    }

    object DuHarRettTilInnsynISakenDin : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du har rett til innsyn i saken din" },
                    nynorsk { +"Du har rett til innsyn i saka di" },
                    english { +"You have the right to access documents" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til å se dokumentene i saken din. Dette følger av forvaltningsloven " +
                            "§ 18. Du har tilgang til mange av dokumentene dine digitalt på ${Constants.NAV_URL}. " +
                            "Hvis det er dokumenter du ikke finner, kan du be oss om innsyn i disse. Du kan lese mer " +
                            "om retten til innsyn på ${Constants.INNSYNSKRAV_URL}." },
                    nynorsk { +"Du har rett til å sjå dokumenta i saka di. Dette følgjer av forvaltningslova " +
                            "§ 18. Du har tilgang til mange av dokumenta dine digitalt på ${Constants.NAV_URL}. " +
                            "Dersom det er dokument du ikkje finn, kan du be oss om innsyn i desse. Du kan lese meir " +
                            "om retten til innsyn på ${Constants.INNSYNSKRAV_URL}." },
                    english { +"You have the right to view the documents relating to your case. This is set out " +
                            "in Section 18 of the Public Administration Act. You can access many of your documents " +
                            "digitally at ${Constants.NAV_URL}. If there are any documents you cannot find, you can " +
                            "ask us to grant you access to them. You can read more about the right of access at " +
                            "${Constants.INNSYNSKRAV_URL}." }
                )
            }
        }
    }

    object DuHarRettigheterKnyttetTilPersonopplysninger : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du har rettigheter knyttet til personopplysningene dine" },
                    nynorsk { +"Du har rettar knytte til personopplysningane dine" },
                    english { +"You have rights regarding your personal data" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du finner informasjon om hvordan Nav behandler personopplysningene dine, og hvilke " +
                            "rettigheter du har, på ${Constants.PERSONVERNERKLAERING_URL}." },
                    nynorsk { +"Du finn informasjon om korleis Nav behandlar personopplysningane dine, og kva " +
                            "rettar du har, på ${Constants.PERSONVERNERKLAERING_URL}." },
                    english { +"You can find information about how Nav processes your personal data, and what " +
                            "rights you have, at ${Constants.PERSONVERNERKLAERING_URL}." }
                )
            }
        }
    }

    object DuHarRettTilHjelpFraAndre : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du har rett til å få hjelp fra andre" },
                    nynorsk { +"Du har rett til hjelp frå andre" },
                    english { +"You have the right to receive assistance from others" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til å få hjelp i kontakten din med Nav, for eksempel fra en advokat, " +
                            "rettshjelper, en organisasjon du er medlem av, eller en myndig person over 18 år. Dette " +
                            "følger av forvaltningsloven § 12. Hvis den som hjelper deg ikke er advokat, må du gi " +
                            "denne personen skriftlig fullmakt. Bruk skjemaet du finner på ${Constants.FULLMAKT_URL}." },
                    nynorsk { +"Du har rett til å få hjelp i kontakten med Nav, til dømes frå ein advokat, " +
                            "rettshjelpar, ein organisasjon du er medlem av, eller ein myndig person over 18 år. " +
                            "Dette følgjer av forvaltningslova § 12. Dersom personen som hjelper deg, ikkje er " +
                            "advokat, må du gi vedkomande ei skriftleg fullmakt. Bruk gjerne skjemaet du finn på " +
                            "${Constants.FULLMAKT_URL}." },
                    english { +"You have the right to receive assistance in your dealings with Nav, for example " +
                            "from a solicitor, a legal adviser, an organisation of which you are a member, or a " +
                            "person over the age of 18. This is in accordance with Section 12 of the Public " +
                            "Administration Act. If the person assisting you is not a solicitor, you must provide " +
                            "that person with a written power of attorney. Use the form available at " +
                            "${Constants.Engelsk.FULLMAKT_URL}." }
                )
            }
        }
    }

    object DuHarRettTilVeiledning : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du har rett til å få veiledning fra Nav" },
                    nynorsk { +"Du har rett på rettleiing frå Nav" },
                    english { +"Guidance from Nav" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både " +
                            "før, under og etter saksbehandlingen. Dette følger av forvaltningsloven § 11." },
                    nynorsk { +"Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, under " +
                            "og etter saksbehandlinga. Det følgjer av forvaltningslova § 11." },
                    english { +"We have a duty to advise you of your rights and obligations in your case – before, " +
                            "during and after the case has been processed. This is in accordance with Section 11 of " +
                            "the Public Administration Act." }
                )
            }
        }
    }
}
