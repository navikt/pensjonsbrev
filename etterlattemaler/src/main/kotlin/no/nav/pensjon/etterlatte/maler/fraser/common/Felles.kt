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
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                    english { +"You have the right to appeal" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du mener at vedtaket er feil, kan du klage innen seks uker fra den dagen du " +
                            "mottok vedtaket. Kontoret som har fattet vedtaket vil da vurdere saken din på nytt. " +
                            "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til Nav klageinstans " +
                            "for ny vurdering og avgjørelse. Dersom du heller ikke får gjennomslag hos klageinstansen, " +
                            "kan du anke saken inn for Trygderetten." },
                    nynorsk { +"Dersom du meiner vedtaket er feil, kan du klage innan seks veker frå den " +
                            "datoen du fekk vedtaket. Kontoret som har fatta vedtaket, vil då vurdere saka di på " +
                            "nytt. Dersom du ikkje får medhald i klaga di, blir ho sendt vidare til Nav klageinstans " +
                            "for ny vurdering og avgjerd. Dersom du heller ikkje får medhald hos klageinstansen, kan " +
                            "du anke saka inn for Trygderetten." },
                    english { +"If you believe the decision is wrong, you can appeal within six weeks from " +
                            "the date you received the decision. The office that made the decision will then review " +
                            "your case again. If your appeal is not approved, it will be forwarded to the Nav Appeals " +
                            "Instance for a new review and decision. If you do not receive approval there either, you " +
                            "can bring your case before the National Insurance Court." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Du finner skjema og informasjon på ${Constants.KLAGE_URL}. Nav kan veilede deg " +
                            "på telefon om hvordan du sender en klage. Nav-kontoret ditt kan også hjelpe deg med å " +
                            "skrive en klage." },
                    nynorsk { +"Du finn skjema og informasjon på ${Constants.KLAGE_URL}. Nav kan rettleie deg " +
                            "på telefon om korleis du sender ei klage. Nav-kontoret ditt kan òg hjelpe deg med å " +
                            "skrive ei klage." },
                    english { +"You can find forms and information at ${Constants.Engelsk.KLAGE_URL}. Nav " +
                            "can guide you by phone on how to submit an appeal. Your local Nav office can also help " +
                            "you write an appeal." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Kontakt oss på telefon ${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE} " +
                            "hvis du trenger hjelp. Hvis du sender klagen i posten, må du signere klagen." },
                    nynorsk { +"Kontakt oss på telefon ${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE} " +
                            "dersom du treng hjelp. Viss du sender klaga i posten, må du signere ho." },
                    english { +"Contact us by phone at ${Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE} " +
                            "if you need assistance. If you send your appeal by post, you must sign it." }
                )
            }

            paragraph {
                text(
                    bokmal { +"Mer informasjon om klagerettigheter finner du på ${Constants.KLAGERETTIGHETER_URL}." },
                    nynorsk { +"Meir informasjon om klagerettar finn du på ${Constants.KLAGERETTIGHETER_URL}." },
                    english { +"More information about appeal rights can be found at ${Constants.KLAGERETTIGHETER_URL}." }
                )
            }
        }
    }
}
