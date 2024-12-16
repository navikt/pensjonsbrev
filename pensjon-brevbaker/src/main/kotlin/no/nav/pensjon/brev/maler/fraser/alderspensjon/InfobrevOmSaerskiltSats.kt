package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

// infoSoerSatsEPS60AFP_001, infoSoerSatsEPS60AP_001
data class InfoSaerskiltSatsEPS60(
    val sakstype: Expression<Sakstype>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(sakstype.isOneOf(Sakstype.AFP, Sakstype.ALDER)) {
            paragraph {
                val pensjonsytelseAFP = sakstype.equalTo(Sakstype.AFP)
                textExpr(
                    Bokmal to "Hvis du forsørger ektefelle/partner/samboer over 60 år som har en inntekt lavere enn 1 G, ".expr() +
                            "har du rett til minste pensjonsnivå etter særskilt sats. Da kan du ha rett til ".expr() +
                            ifElse(pensjonsytelseAFP, ifTrue = "AFP", ifFalse = "alderspensjon") + ".".expr(),
                    Nynorsk to "Dersom du forsørgjer ektefelle/partnar/sambuar over 60 år som har ei inntekt lågare enn 1 G, ".expr() +
                            "har du rett til minste pensjonsnivå etter særskilt sats. Då kan du ha rett til ".expr() +
                            ifElse(pensjonsytelseAFP, ifTrue = "AFP", ifFalse = "alderspensjon") + ".".expr(),
                    English to "If you support a spouse/partner/cohabitant over 60 years old who has an income lower than 1 G, ".expr() +
                    "you are entitled to the minimum pension level at a special rate. You may then be entitled to ".expr() +
                            ifElse(pensjonsytelseAFP, ifTrue = "contractual early retirement", ifFalse = "retirement") + " pension.".expr()

                )
            }
        }
    }
}

// grunnenTilViSkriverInfoSaerSats_001
data class InfoSaerskiltSatsEPS60Grunnen(
    val sakstype: Expression<Sakstype>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Dette er grunnen til at vi skriver til deg:",
                Nynorsk to "Dette er grunnen til at vi skriv til deg:",
                English to "This is why we are writing to you"
            )
        }
        paragraph {
            val pensjonsytelseAFP = sakstype.equalTo(Sakstype.AFP)
            textExpr(
                Bokmal to ifElse(pensjonsytelseAFP, ifTrue = "AFPen", ifFalse = "Alderspensjonen") +
                        " du får utbetalt i dag er beregnet ut ifra at ektefellen/partneren/samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (2 G).".expr(),
                Nynorsk to ifElse(pensjonsytelseAFP, ifTrue = "AFPen", ifFalse = "Alderspensjonen") +
                        " du får utbetalt i dag er rekna ut frå at ektefellen/partnaren/sambuaren din har ei inntekt som er lågare enn to gonger folketrygda sitt grunnbeløp (2 G).".expr(),
                English to "The ".expr() + ifElse(pensjonsytelseAFP, ifTrue = "contractual early retirement", ifFalse = "retirement") +
                        " pension you receive today is calculated based on your spouse/partner/cohabitant having an income lower than twice the National Insurance basic amount (2 G).".expr()
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis denne inntekten også er under 1 G, må du dokumentere dette.",
                Nynorsk to "Dersom denne inntekta også er under 1 G, må du dokumentere dette.",
                English to "If this income is also below 1 G, you must document this."
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan lese mer om grunnbeløp på ${Constants.GRUNNBELOEP_URL}.",
                Nynorsk to "Du kan lese meir om grunnbeløp på ${Constants.GRUNNBELOEP_URL}.",
                English to "You can read more about the basic amount at ${Constants.GRUNNBELOEP_URL}."
            )
        }
    }
}

// detteMaaDuGjøreInfoSaerSats_001
data class InfoSaerskiltSatsEPS60DetteMaaDuGjoere(
    val sakstype: Expression<Sakstype>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Dette må du gjøre:",
                Nynorsk to "Dette må du gjere:",
                English to "What you need to do"
            )
        }
        paragraph {
            text(
                Bokmal to "Du må sende oss dokumentasjon på all inntekt. Med inntekt menes:",
                Nynorsk to "Du må sende oss dokumentasjon på all inntekt. Med inntekt meinar vi:",
                English to "You must send us documentation of all income. Income includes"
            )
            list {
                item {
                    text(
                        Bokmal to "arbeidsinntekt i Norge, og eventuelt andre land",
                        Nynorsk to "arbeidsinntekt i Noreg, og eventuelt andre land",
                        English to "employment income in Norway, and possibly other countries"
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt fra andre private og offentlige pensjonsordninger",
                        Nynorsk to "inntekt frå andre private og offentlege pensjonsordningar",
                        English to "income from other private and public pension schemes"
                    )
                }
                item {
                    text(
                        Bokmal to "pensjoner fra andre land",
                        Nynorsk to "pensjonar frå andre land",
                        English to "pensions from other countries"
                    )
                }
                item {
                    text(
                        Bokmal to "ytelser fra Nav, blant annet sykepenger og arbeidsavklaringspenger (APP)",
                        Nynorsk to "ytingar frå Nav, mellom anna sjukepengar og arbeidsavklaringspengar (AAP)",
                        English to "benefits from Nav, including sickness benefits and work assessment allowance (AAP)"
                    )
                }
                item {
                    text(
                        Bokmal to "kapitalinntekt",
                        Nynorsk to "kapitalinntekt",
                        English to "capital income"
                    )
                }
                item {
                    text(
                        Bokmal to "livrente",
                        Nynorsk to "livrente",
                        English to "income annuities"
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Som dokumentasjon kan du sende kopi av skatteoppgjøret for siste år. " +
                        "Vi godtar også bekreftelse fra regnskapsfører, årsoppgave fra bank eller kopier av lønns- og trekkoppgaver.",
                Nynorsk to "Som dokumentasjon kan du sende kopi av skatteoppgjeret for siste år. " +
                        "Vi godtek også bekrefting frå rekneskapsførar, årsoppgåve frå bank eller kopiar av løns- og trekkoppgåver.",
                English to "As documentation, you can send a copy of the tax settlement for the last year. " +
                "We also accept confirmation from an accountant, an annual statement from the bank, or copies of salary and deduction statements."
            )
        }
        paragraph {
            text(
                Bokmal to "Husk å merke forsendelsen med både ditt og din ektefelle/partner/samboer sitt navn og fødselsnummer.",
                Nynorsk to "Hugs å merke forsendelsen med både ditt og din ektefelle/partnar/sambuar sitt namn og fødselsnummer.",
                English to "Remember to mark the documents with both your and your spouse/partner/cohabitant's name and Norwegian social security number."
            )
        }
        paragraph {
            text(
                Bokmal to "Du bør sende inn dokumentasjonen innen 14 dager fra du mottar dette brevet, til:",
                Nynorsk to "Du bør sende inn dokumentasjonen innan 14 dagar frå du mottar dette brevet, til:",
                English to "You should submit the documentation within 14 days from receiving this letter, to:"
            )
            newline()
            text(
                Bokmal to "Nav Familie- og pensjonsytelser",
                Nynorsk to "Nav Familie- og pensjonsytingar",
                English to "Nav Familie- og pensjonsytelser"
            )
            newline()
            text(
                Bokmal to "PB 6600 Etterstad",
                Nynorsk to "PB 6600 Etterstad",
                English to "PB 6600 Etterstad"
            )
            newline()
            text(
                Bokmal to "0607 OSLO",
                Nynorsk to "0607 OSLO",
                English to "0607 OSLO, NORWAY"
            )
        }
        paragraph {
            val pensjonsytelseAFP = sakstype.equalTo(Sakstype.AFP)
            textExpr(
                Bokmal to ifElse(pensjonsytelseAFP, ifTrue = "AFPen", ifFalse = "Alderspensjonen") +
                        " din blir vurdert på nytt etter vi har mottatt dokumentasjon.".expr(),
                Nynorsk to ifElse(pensjonsytelseAFP, ifTrue = "AFPen", ifFalse = "Alderspensjonen") +
                        " din blir vurdert på nytt etter vi har mottatt dokumentasjon.".expr(),
                English to "Your ".expr() + ifElse(pensjonsytelseAFP, ifTrue = "contractual early retirement", ifFalse = "retirement") +
                " pension will be reassessed after we have received the documentation."
            )
        }
    }
}

