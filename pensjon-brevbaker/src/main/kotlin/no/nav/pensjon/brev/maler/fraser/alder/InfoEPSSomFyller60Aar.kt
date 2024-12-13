package no.nav.pensjon.brev.maler.fraser.alder

import no.nav.pensjon.brev.api.model.Sakstype
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
                    Nynorsk to "".expr(),
                    English to "".expr()

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
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            val pensjonsytelseAFP = sakstype.equalTo(Sakstype.AFP)
            textExpr(
                Bokmal to ifElse(pensjonsytelseAFP, ifTrue = "AFPen", ifFalse = "Alderspensjonen") +
                        " du får utbetalt i dag er beregnet ut ifra at ektefellen/partneren/samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (2 G).".expr(),
                Nynorsk to "".expr(),
                English to "".expr()
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis denne inntekten også er under 1 G, må du dokumentere dette.",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan lese mer om grunnbeløp på nav.no/grunnbeløp.",
                Nynorsk to "",
                English to ""
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
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du må sende oss dokumentasjon på all inntekt. Med inntekt menes:",
                Nynorsk to "",
                English to ""
            )
            list {
                item {
                    text(
                        Bokmal to "arbeidsinntekt i Norge, og eventuelt andre land",
                        Nynorsk to "",
                        English to ""
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt fra andre private og offentlige pensjonsordninger",
                        Nynorsk to "",
                        English to ""
                    )
                }
                item {
                    text(
                        Bokmal to "pensjoner fra andre land",
                        Nynorsk to "",
                        English to ""
                    )
                }
                item {
                    text(
                        Bokmal to "ytelser fra NAV, blant annet sykepenger og arbeidsavklaringspenger (APP)",
                        Nynorsk to "",
                        English to ""
                    )
                }
                item {
                    text(
                        Bokmal to "kapitalinntekt",
                        Nynorsk to "",
                        English to ""
                    )
                }
                item {
                    text(
                        Bokmal to "livrente",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Som dokumentasjon kan du sende kopi av skatteoppgjøret for siste år. " +
                        "Vi godtar også bekreftelse fra regnskapsfører, årsoppgave fra bank eller kopier av lønns- og trekkoppgaver.",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Husk å merke forsendelsen med både ditt og din ektefelle/partner/samboer sitt navn og fødselsnummer.",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du bør sende inn dokumentasjonen innen 14 dager fra du mottar dette brevet, til:",
                Nynorsk to "",
                English to ""
            )
            newline()
            text(
                Bokmal to "NAV Familie- og pensjonsytelser",
                Nynorsk to "NAV Familie- og pensjonsytelser",
                English to "NAV Familie- og pensjonsytelser"
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
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    }
}

