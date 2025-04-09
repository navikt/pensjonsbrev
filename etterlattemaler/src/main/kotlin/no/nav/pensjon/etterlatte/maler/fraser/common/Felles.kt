package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Felles {
    object BlankTekst : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    object HjelpFraAndreForvaltningsloven12 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hjelp fra andre - forvaltningsloven § 12",
                    Nynorsk to "Hjelp frå andre – forvaltingslova § 12",
                    English to "Help from others – Section 12 of the Public Administration Act",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, " +
                            "rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som " +
                            "hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne " +
                            "skjemaet du finner på ${Constants.FULLMAKT_URL}.",
                    Nynorsk to "Du har under heile saksbehandlinga høve til å be om hjelp frå til dømes advokat, " +
                            "rettshjelpar, organisasjonar du er medlem av, eller andre myndige personar. " +
                            "Dersom personen som hjelper deg, ikkje er advokat, " +
                            "må du gi vedkomande ei skriftleg fullmakt. " +
                            "Bruk gjerne skjemaet du finn på ${Constants.FULLMAKT_URL}.",
                    English to "You can ask for help from others throughout case processing, such as an attorney, " +
                            "legal aid, an organization of which you are a member or another person of legal age. " +
                            "If the person helping you is not an attorney, " +
                            "you must give this person a written power of attorney. " +
                            "Feel free to use the form you find here:  ${Constants.Engelsk.FULLMAKT_URL}.",
                )
            }
        }
    }

    object SlikUttalerDuDegOmstillingsstoenad : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Slik uttaler du deg",
                    Nynorsk to "Slik uttaler du deg",
                    English to "How to provide a statement",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Du kan også sende " +
                            "uttalelsen din til oss i posten. Adressen finner du på ${Constants.ETTERSENDELSE_URL}.",
                    Nynorsk to "Du kan sende svar til oss ved å logge inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Alternativt kan du " +
                            "sende oss svar i posten. Adressa finn du på ${Constants.ETTERSENDELSE_URL}.",
                    English to "You can send us a statement regarding the matter by logging in to: " +
                            "${Constants.Engelsk.BESKJED_TIL_NAV_URL}. You can also " +
                            "send us your statement by post. The address can be found at: ${Constants.Engelsk.ETTERSENDELSE_URL}.",
                )
            }
        }
    }

    object SlikUttalerDuDegBarnepensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Slik uttaler du deg",
                    Nynorsk to "Slik uttaler du deg",
                    English to "How to provide a statement",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Du kan også sende " +
                            "uttalelsen din til oss i posten. Adressen finner du på ${Constants.BP_MELDE}.",
                    Nynorsk to "Du kan sende svar til oss ved å logge inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL}. Alternativt kan du " +
                            "sende oss svar i posten. Adressa finn du på ${Constants.BP_MELDE}.",
                    English to "You can send us a statement regarding the matter by logging in to: " +
                            "${Constants.Engelsk.BESKJED_TIL_NAV_URL}. You can also " +
                            "send us your statement by post. The address can be found at: ${Constants.BP_MELDE}.",
                )
            }
        }
    }

    object HvaSkjerVidereIDinSak : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hva skjer videre i din sak",
                    Nynorsk to "Kva som skjer vidare i saka",
                    English to "What will happen further in your case",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når fristen for uttale er gått ut, vil vi gjøre et vedtak og sende det " +
                            "til deg. Hvis du må betale tilbake hele eller deler av beløpet, gir vi beskjed i " +
                            "vedtaket om hvordan du betaler tilbake.",
                    Nynorsk to "Når svarfristen har gått ut, fattar vi eit vedtak og sender det til deg. " +
                            "Dersom du må betale tilbake heile eller delar av beløpet, forklarer vi i vedtaket " +
                            "korleis du betaler tilbake.",
                    English to "When the deadline for providing statements has expired, we will make a decision " +
                            "and send it to you. If you are required to repay the whole or part of the amount, we will " +
                            "inform you in the decision of how to make repayments.",
                )
            }
        }
    }

    object HvordanMelderDuFra : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hvordan melder du fra?",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Meld fra til oss ved å benytte endringsskjema på ${Constants.OMS_MELD_INN_ENDRING_URL}. " +
                            "Du må sende dokumentasjon på ${Constants.ETTERSENDE_OMS_URL}.",
                    Nynorsk to "",
                    English to "",
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
                    Bokmal to "Hvordan sende opplysninger til oss?",
                    Nynorsk to "Korleis melder du frå om endringar?",
                    English to "How to submit information to Nav?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan benytte endringsskjema ved å logge deg på på ${Constants.OMS_MELD_INN_ENDRING_URL}. " +
                        "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside ${Constants.NAV_URL}, " +
                        "kan du kontakte oss på telefon.",
                    Nynorsk to "Du kan bruke endringsskjema ved å logge på ${Constants.OMS_MELD_INN_ENDRING_URL}. " +
                        "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår ${Constants.NAV_URL}, " +
                        "kan du kontakte oss på telefon.",
                    English to "You can log in to our website and use change form on ${Constants.OMS_MELD_INN_ENDRING_URL}. " +
                        "If you do not have BankID or another option to log in to our website, ${Constants.NAV_URL}, " +
                        "you can also call us.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Skal du sende oss noe per post må du bruke adressen",
                    Nynorsk to "Skal du sende oss noko per post, bruker du adressa",
                    English to "If you are submitting anything through the mail, you must use the following address:",
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
                        Bokmal to "Nav familie- og pensjonsytelser",
                        Nynorsk to "Nav familie- og pensjonsytelser",
                        English to "Nav familie- og pensjonsytelser",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Postboks 6600 Etterstad",
                        Nynorsk to "Postboks 6600 Etterstad",
                        English to "Postboks 6600 Etterstad",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "0607 Oslo",
                        Nynorsk to "0607 Oslo",
                        English to "0607 Oslo",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Norge/Norway",
                        Nynorsk to "Norge/Norway",
                        English to "Norway",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Nav skanning",
                        Nynorsk to "Nav skanning",
                        English to "Nav skanning",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Postboks 1400",
                        Nynorsk to "Postboks 1400",
                        English to "Postboks 1400",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "0109 Oslo",
                        Nynorsk to "0109 Oslo",
                        English to "0109 Oslo",
                    )
                }
            }
        }
    }
}
