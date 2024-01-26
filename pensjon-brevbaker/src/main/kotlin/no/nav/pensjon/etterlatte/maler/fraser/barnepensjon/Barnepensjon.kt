package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon
import java.time.LocalDate

object Barnepensjon {

    data class Foerstegangsbehandlingsvedtak(
        val virkningsdato: Expression<LocalDate>,
        val avdoedNavn: Expression<String>,
        val doedsdato: Expression<LocalDate>,
        val beloep: Expression<Kroner>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = doedsdato.format()
                textExpr(
                    Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". " +
                            "Du får " + beloep.format() + " kroner hver måned før skatt. Barnepensjonen utbetales til og med den " +
                            "kalendermåneden du fyller 18 år. Vedtaket er gjort etter folketrygdloven kapittel 18 og 22.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
    }

    object BeregningOgUtbetalingOverskrift : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Beregning og utbetaling",
                )
            }
        }
    }

    object SlikHarViBeregnetPensjonenDinTittel : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Slik har vi beregnet pensjonen din",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class SlikHarViBeregnetPensjonenDin(
        val beregningsperioder: Expression<List<Beregningsperiode>>,
        val soeskenjustering: Expression<Boolean>,
        val antallBarn: Expression<Int>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            includePhrase(SlikHarViBeregnetPensjonenDinTittel)
            showIf(soeskenjustering) {
                paragraph {
                    textExpr(
                        Bokmal to "Det gjøres en samlet beregning av pensjon for barn som oppdras sammen. ".expr() +
                                "For denne beregningen har vi lagt til grunn at dere er " + antallBarn.format() +
                                " barn som oppdras sammen.",
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjon utgjør 40 prosent av folketrygdens grunnbeløp (G) for det " +
                                "første barnet i søskenflokken. For hvert av de øvrige barna legges det til 25 prosent av G. " +
                                "Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna. " +
                                "Pensjonen fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to ""
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "Barnepensjonen utgjør 40 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }
        }
    }

    data class BeregnetPensjonTabell(
        val beregningsperioder: Expression<List<Beregningsperiode>>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                table(
                    header = {
                        column(2) {
                            text(Bokmal to "Periode", Nynorsk to "", English to "")
                        }
                        column(1) {
                            text(Bokmal to "Grunnbeløp (G)", Nynorsk to "", English to "")
                        }
                        column(2) {
                            text(Bokmal to "Brutto utbetaling per måned", Nynorsk to "", English to "")
                        }
                    }
                ) {
                    forEach(beregningsperioder) {
                        row {
                            cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                            cell { includePhrase(Felles.KronerText(it.grunnbeloep)) }
                            cell { includePhrase(Felles.KronerText(it.utbetaltBeloep)) }
                        }
                    }
                }
                text(
                    Bokmal to "Tabellen viser hvor mye du får i barnepensjon før skatt.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }

    }

    data class PeriodeITabell(val datoFOM: Expression<LocalDate>, val datoTOM: Expression<LocalDate?>) :
        TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            ifNotNull(datoTOM) { datoTOM ->
                textExpr(
                    Bokmal to datoFOM.format(true) + " - " + datoTOM.format(true),
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            } orShow {
                textExpr(
                    Bokmal to datoFOM.format(true) + " - ",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
    }

    object Utbetaling : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Utbetaling",
                )
            }
            paragraph {
                text(
                    Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. " +
                            "Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}."
                )
            }
            paragraph {
                text(
                    Bokmal to "Har du rett til etterbetaling, vil du vanligvis få dette i løpet av tre uker."
                )
            }
        }
    }

    object Regulering : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Regulering",
                )
            }
            paragraph {
                text(
                    Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen blir vanligvis etterbetalt i juni."
                )
            }
        }
    }

    object InformasjonTilDegOverskrift : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Informasjon til deg som handler på vegne av barnet",
                )
            }
        }
    }

    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Meld fra om endringer",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må melde fra med en gang det skjer viktige endringer i barnets liv, som ",
                    Nynorsk to "",
                    English to "",
                )
                list {
                    item {
                        text(
                            Bokmal to "endringer av nåværende familie- eller omsorgsforhold",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                    item {
                        text(
                            Bokmal to "flytting eller opphold i et annet land over tid",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                    item { text(Bokmal to "varig opphold i institusjon", Nynorsk to "", English to "") }
                }
                text(
                    Bokmal to "Du er ansvarlig for å holde deg orientert om bevegelser på kontoen for utbetaling av barnepensjon, " +
                            "og du må straks melde fra om eventuelle feil til NAV. Er det utbetalt for mye barnepensjon fordi " +
                            "NAV ikke har fått beskjed, må pengene vanligvis betales tilbake.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    object EndringAvKontonummer : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Endring av kontonummer",
                )
            }
            paragraph {
                text(
                    Bokmal to "Ved endring av kontonummer for utbetaling av barnepensjon til barn under 18 år, " +
                            "kan du som forelder sende melding via ${Constants.SKRIVTILOSS_URL} eller sende skjema for melding om nytt " +
                            "kontonummer per post. Du må da legge ved kopi av gyldig legitimasjon."
                )
            }
            paragraph {
                text(
                    Bokmal to "Oppnevnt verge må melde om endring via post. Det må legges ved kopi av egen legitimasjon og vergefullmakt."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}."
                )
            }
        }
    }

    object SkattetrekkPaaBarnepensjonRevurdering : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Skattetrekk på barnepensjon",
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon er skattepliktig. " +
                            "Du kan lese mer om frivillig skattetrekk på ${Constants.SKATTETREKK_PENGESTOETTE_URL}."
                )
            }
            paragraph {
                text(
                    Bokmal to "Ved etterbetaling som gjelder tidligere år trekker NAV skatt etter "
                            + "Skatteetatens standardsatser. Du kan lese mer om satsene "
                            + "på ${Constants.SKATTETREKK_ETTERBETALING_URL}."
                )
            }
        }
    }

    object SkattetrekkPaaBarnepensjon : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Skattetrekk på barnepensjon",
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten å få " +
                            "beskjed om dette. Hvis du opplyste om ønsket skattetrekk i søknaden, har vi registrert " +
                            "dette for i år. Du må selv sjekke om dette skattetrekket overføres ved årsskiftet. " +
                            "Du kan lese mer om frivillig skattetrekk på ${Constants.SKATTETREKK_PENGESTOETTE_URL}."
                )
            }
            paragraph {
                text(
                    Bokmal to "Ved etterbetaling som gjelder tidligere år trekker NAV skatt etter " +
                            "Skatteetatens standardsatser. Du kan lese mer om satsene " +
                            "på ${Constants.SKATTETREKK_ETTERBETALING_URL}."
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

    object HarDuSpoersmaal : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "TODO engelsk"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan finne svar på ${Constants.BARNEPENSJON_URL}. Du kan også kontakte " +
                            "oss på telefon ${Constants.KONTAKTTELEFON_PENSJON}. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg " +
                            "rask og god hjelp.",
                    Nynorsk to "TODO nynorsk",
                    English to "TODO engelsk"
                )
            }
        }
    }

    data class HarDuSpoersmaalNy(
        val brukerUnder18Aar: Expression<Boolean>,
        val bosattUtland: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Any questions?"
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
                        Bokmal to " hverdager 9-15. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                        Nynorsk to ", kvardagar 9–15. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret til barnet.",
                        English to ") weekdays 9-15. If you provide your child's national identity number, we can more easily provide you with quick and good help."
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
                        Bokmal to " hverdager 9-15. Om du oppgir fødselsnummeret ditt, kan vi lettere gi deg rask og god hjelp.",
                        Nynorsk to ", kvardagar 9–15. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret ditt.",
                        English to ") weekdays 9-15. If you provide your child's national identity number, we can more easily provide you with quick and good help."
                    )
                }
            }
        }
    }

    object Feilutbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Feilutbetaling",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Siden pensjonen din er opphørt tilbake i tid, medfører dette at du har fått utbetalt for mye i pensjon i denne perioden. Du vil få eget forhåndsvarsel om eventuell tilbakekreving av det feilutbetalte beløpet.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }

    data class BarnepensjonenDinErDerforOpphoert(val virkningsdato: Expression<LocalDate>) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din er derfor opphørt fra ".expr() + virkningsdato.format(),
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
        }
    }

    object TilOgMedKalendermaaneden18Aar : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Barnepensjonen din utbetales til og med den kalendermåneden du fyller 18 år.",
                )
            }
        }
    }

}