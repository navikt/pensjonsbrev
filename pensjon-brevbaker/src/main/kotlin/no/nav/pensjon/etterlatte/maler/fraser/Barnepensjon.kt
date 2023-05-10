package no.nav.pensjon.etterlatte.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTO
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

object Barnepensjon {

    data class Vedtak(
        val virkningsdato: Expression<LocalDate>,
        val avdoedNavn: Expression<String>,
        val doedsdato: Expression<LocalDate>,
        val beloep: Expression<Kroner>,
    ) :
        OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() =
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = doedsdato.format()
                textExpr(
                    Language.Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". " +
                            "Du får " + beloep.format() + " kroner hver måned før skatt. Barnepensjonen utbetales til og med den " +
                            "kalendermåneden du fyller 18 år. Vedtaket er gjort etter folketrygdloven kapittel 18 og 22.",
                )
            }
    }

    object UtbetalingOverskrift : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Utbetaling",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Etterbetaling av pensjon vil vanligvis bli utbetalt i løpet av tre uker. "
                )
            }
        }
    }

    data class SlikHarViBeregnetPensjonenDin(
        val beregningsperioder: Expression<List<BarnepensjonVedtakDTO.Beregningsperiode>>,
        val soeskenjustering: Expression<Boolean>,
        val antallBarn: Expression<Int>
    ) :
        OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Slik har vi beregnet pensjonen din",
                )
            }
            showIf(soeskenjustering) {
                paragraph {
                    textExpr(
                        Language.Bokmal to "Det gjøres en samlet beregning av pensjon for barn som oppdras sammen. ".expr() +
                                "For denne beregningen har vi lagt til grunn at dere er " + antallBarn.format() +
                                " barn som oppdras sammen.",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjon utgjør 40 prosent av folketrygdens grunnbeløp (G) for det " +
                                "første barnet i søskenflokken. For hvert av de øvrige barna legges det til 25 prosent av G. " +
                                "Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna. " +
                                "Pensjonen fordeles på 12 utbetalinger i året."
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjonen utgjør 40 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året."
                    )
                }
            }

            title1 {
                text(
                    Language.Bokmal to "Opplysninger om beregning av pensjonen din",
                )
            }
            paragraph {
                table(
                    header = {
                        column(1) {
                            text(Language.Bokmal to "Periode")
                        }
                        column(1) {
                            text(Language.Bokmal to "Beregning gjelder")
                        }
                        column(1) {
                            text(Language.Bokmal to "Grunnbeløp (G)")
                        }
                        column(1) {
                            text(Language.Bokmal to "Brutto utbetaling per måned")
                        }
                    }
                ) {
                    forEach(beregningsperioder) {
                        row {
                            cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                            cell {
                                textExpr(
                                    Language.Bokmal to it.antallBarn.format() + " barn".expr(),
                                )
                            }
                            cell { includePhrase(Felles.KronerText(it.grunnbeloep)) }
                            cell { includePhrase(Felles.KronerText(it.utbetaltBeloep)) }
                        }
                    }
                }
                text(
                    Language.Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år.  Økningen blir vanligvis etterbetalt i juni."
                )
            }
        }
    }

    data class PeriodeITabell(val datoFOM: Expression<LocalDate>, val datoTOM: Expression<LocalDate?>) :
        TextOnlyPhrase<LangBokmal>() {
        override fun TextOnlyScope<LangBokmal, Unit>.template() =
            ifNotNull(datoTOM) { datoTOM ->
                textExpr(
                    Language.Bokmal to datoFOM.format(true) + " - " + datoTOM.format(true)
                )
            } orShow {
                textExpr(
                    Language.Bokmal to datoFOM.format(true) + " - "
                )
            }
    }

    object MeldFraOmEndringer : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Meld fra om endringer",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du må melde fra med en gang det skjer viktige endringer i barnets liv, som"
                )
                list {
                    item { text(Language.Bokmal to "endringer av nåværende familie- eller omsorgsforhold") }
                    item { text(Language.Bokmal to "flytting eller opphold i et annet land over tid") }
                    item { text(Language.Bokmal to "varig opphold i institusjon") }
                }
                text(
                    Language.Bokmal to "Du er ansvarlig for å holde deg orientert om bevegelser på kontoen for utbetaling av barnepensjon, " +
                            "og du må straks melde fra om eventuelle feil til NAV. Er det utbetalt for mye barnepensjon fordi " +
                            "NAV ikke har fått beskjed, må pengene vanligvis betales tilbake."
                )
            }
        }
    }

    object EndringAvKontonummer : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Endring av kontonummer",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Ved endring av kontonummer for utbetaling av barnepensjon til barn under 18 år, " +
                            "kan du som forelder sende melding via ${Constants.SKRIVTILOSS_URL} eller sende skjema for melding om nytt " +
                            "kontonummer per post. Du må da legge ved kopi av gyldig legitimasjon."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Oppnevnt verge må melde om endring via post. Det må legges ved kopi av egen legitimasjon og vergefullmakt."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}."
                )
            }
        }
    }

    object SkattetrekkPaaBarnepensjon : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Skattetrekk på barnepensjon",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt for inneværende år " +
                            "uten å få beskjed om dette. Du kan lese mer om frivillig skattetrekk på ${Constants.SKATTETREKK_PENGESTOETTE_URL}."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Ved etterbetaling som gjelder tidligere år trekker NAV skatt etter Skatteetatens standardsatser. " +
                            "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}."
                )
            }
        }
    }

    object DuHarRettTilAaKlage : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen " +
                            "du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}."
                )
            }
        }
    }

    object DuHarRettTilInnsyn : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Du har rett til innsyn",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din etter" +
                            " bestemmelsene i forvaltningsloven § 18. Hvis du ønsker innsyn, må du kontakte oss på " +
                            "telefon eller per post."
                )
            }
        }
    }

    object HarDuSpoersmaal : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan finne svar på ${Constants.NAV_URL}. Du kan også kontakte oss på telefon 55 55 33 34." +
                            " Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp."
                )
            }
        }
    }

}