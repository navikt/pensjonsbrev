package no.nav.pensjon.etterlatte.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.AvkortetBeregningsperiode
import no.nav.pensjon.etterlatte.maler.AvkortetBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.AvkortetBeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.AvkortetBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.AvkortetBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.Etterbetalingsperiode
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

object OMSInnvilgelse {

    data class Vedtak(
        val virkningsdato: Expression<LocalDate>,
        val avdoedNavn: Expression<String>,
        val doedsdato: Expression<LocalDate>,
        val etterbetalingsperioder: Expression<List<Etterbetalingsperiode>>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = doedsdato.format()
                textExpr(
                    Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". " +
                            "Du vil ikke få utbetalt omstillingsstønad fordi inntekten din er høyere enn " +
                            "grensen for å få utbetalt stønaden.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Omstillingsstønad innvilges vanligvis for inntil tre år. Stønaden reduseres " +
                            "på grunnlag av arbeidsinntekten din. Se hvordan stønaden din er beregnet under " +
                            "beregning av omstillingsstønaden. Der går det også frem hvilken inntekt stønaden " +
                            "din er redusert etter.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                showIf (etterbetalingsperioder.isEmpty()) {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingstid i folketrygdloven " +
                                "§ 17-2, § 17-3, § 17-4, § 17-5, § 17-6, § 17-9 og § 22-12.",
                        Nynorsk to "",
                        English to "",
                    )
                } orShow {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingstid i folketrygdloven " +
                                "§ 17-2, § 17-3, § 17-4, § 17-5, § 17-6, § 17-9, § 22-12 og § 22-13.",
                        Nynorsk to "",
                        English to "",
                    )
                }

            }
        }
    }

    data class BeregningOgUtbetaling(
        val grunnbeloep: Expression<Kroner>,
        val beregningsperioder: Expression<List<AvkortetBeregningsperiode>>
    ) : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Beregning og utbetaling"
                )
            }
            title2 {
                text(
                    Bokmal to "Slik har vi beregnet omstillingsstønaden din"
                )
            }
            paragraph {
                val formatertGrunnbeloep = grunnbeloep.format()
                textExpr(
                    Bokmal to "Omstillingsstønad beregnes utfra 2,25 ganger folketrygdens grunnbeløp ".expr() +
                            "(G). Dagens verdi av grunnbeløpet er " + formatertGrunnbeloep + " kroner. " +
                            "Stønaden reduseres etter inntekten din og avdødes trygdetid.",
                )
            }
            paragraph {
                table(
                    header = {
                        column(2) {
                            text(Bokmal to "Periode")
                        }
                        column(1) {
                            text(Bokmal to "Din inntekt")
                        }
                        column(2) {
                            text(Bokmal to "Dette får du i omstillingsstønad per måned")
                        }
                    }
                ) {
                    forEach(beregningsperioder) {
                        row {
                            cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                            cell { includePhrase(Felles.KronerText(it.inntekt)) }
                            cell { includePhrase(Felles.KronerText(it.utbetaltBeloep)) }
                        }
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Tabellen viser at du ikke får utbetalt omstillingsstønad fordi " +
                            "inntekten din er høyere enn grensen for å få utbetalt stønaden.",
                )
            }
        }
    }

    data class PeriodeITabell(val datoFOM: Expression<LocalDate>, val datoTOM: Expression<LocalDate?>) :
        TextOnlyPhrase<LangBokmal>() {
        override fun TextOnlyScope<LangBokmal, Unit>.template() =
            ifNotNull(datoTOM) { datoTOM ->
                textExpr(
                    Bokmal to datoFOM.format(true) + " - " + datoTOM.format(true)
                )
            } orShow {
                textExpr(
                    Bokmal to datoFOM.format(true) + " - "
                )
            }
    }

    data class Beregningsgrunnlag(
        val inntekt: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Beregningsgrunnlag",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                val formatertInntekt = inntekt.format()
                textExpr(
                    Bokmal to "Du har opplyst i søknaden at du har en arbeidsinntekt på ".expr() +
                            formatertInntekt + " i inneværende kalenderår. Dette beløpet er høyere enn grensen " +
                            "for å få utbetalt omstillingsstønad. Du kan lese mer om beløpsgrense og hvordan " +
                            "vi beregner inntekt på ${Constants.OMS_HVORMYE_URL}.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }

    object Utbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Utbetaling",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. " +
                            "Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class EtterbetalingOgSkatt(
        val virkningsdato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Etterbetaling og skatt",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                textExpr(
                    Bokmal to "Du får etterbetalt pensjon fra".expr() + formatertVirkningsdato + ". " +
                            "Det trekkes vanligvis skatt av etterbetaling.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Har du mottatt andre ytelser fra NAV eller andre, som for eksempel " +
                            "tjenestepensjonsordninger, kan det bli beregnet fradrag i etterbetalingen. Hvis " +
                            "Skatteetaten eller andre ordninger har krav i etterbetalingen kan utbetalingen bli " +
                            "forsinket. Hvis du får fradrag i etterbetalingen, vil det gå frem av " +
                            "utbetalingsmeldingen din.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjelder etterbetalingen tidligere år trekker NAV skatt etter " +
                            "Skatteetatens standardsatser. Du kan lese mer om satsene på " +
                            "${Constants.SKATTETREKK_ETTERBETALING_URL}",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    object Regulering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Regulering",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen i stønaden " +
                            "din blir vanligvis etterbetalt i juni. Du kan lese mer om regulering på " +
                            "${Constants.OMS_REGULERING_URL}.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    object Aktivitetsplikt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må være i aktivitet",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Formålet med omstillingsstønad er å sikre inntekt for gjenlevende og " +
                            "gi hjelp til selvhjelp, slik at de etter en omstillingsperiode etter dødsfallet " +
                            "kan bli i stand til å forsørge seg selv ved eget arbeid.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Det første halvåret etter dødsfallet stilles det ikke krav til at " +
                            "den gjenlevende er i arbeid eller arbeidsrettet aktivitet. Etter seks måneder er det " +
                            "et vilkår for å fortsatt ha rett til omstillingsstønad at den gjenlevende er i minst " +
                            "50 prosent aktivitet.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når det er gått ett år etter dødsfallet, kan det stilles krav om " +
                            "at den gjenlevende er i arbeid eller arbeidsrettet aktivitet på full tid.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    object Inntektsendring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra hvis inntekten din endrer seg",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "For at du skal motta korrekt omstillingsstønad, er det viktig at du informerer " +
                            "oss hvis inntekten din endrer seg. Vi vil justere omstillingsstønaden fra måneden etter " +
                            "at du har gitt beskjed, og beregne inntekten din basert på det du har tjent så langt i år. " +
                            "Du kan lese mer om inntektsendring i vedlegget «Informasjon til deg som mottar overgangsstønad».",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan også finne mer informasjon om hvordan vi beregner inntekten din på " +
                            "${Constants.OMS_HVORMYE_URL}.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    object Etteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Etteroppgjør",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hver høst sjekker NAV inntektsopplysningene i skatteoppgjøret ditt for å se " +
                            "om du har fått utbetalt riktig beløp i omstillingsstønad året før. Hvis du har fått " +
                            "for lite utbetalt, får du en etterbetaling. Har du fått for mye utbetalt, må du betale " +
                            "tilbake. Du kan finne mer informasjon om etteroppgjør på ${Constants.OMS_ETTEROPPGJOER_URL}.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

}