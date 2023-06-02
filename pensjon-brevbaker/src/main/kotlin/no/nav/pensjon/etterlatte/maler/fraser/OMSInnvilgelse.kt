package no.nav.pensjon.etterlatte.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.BeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.BeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.BeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.BeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

object OMSInnvilgelse {

    data class Vedtak(
        val virkningsdato: Expression<LocalDate>,
        val avdoedNavn: Expression<String>,
        val doedsdato: Expression<LocalDate>,
    ) :
        OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = doedsdato.format()
                textExpr(
                    Language.Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". " +
                            "Du vil ikke få utbetalt omstillingsstønad fordi inntekten din er høyere enn " +
                            "grensen for å få utbetalt stønaden.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Omstillingsstønad innvilges vanligvis for inntil tre år.",
                )
            }
        }
    }

    data class BeregningOgUtbetaling(
        val grunnbeloep: Expression<Kroner>,
        val beregningsperioder: Expression<List<OMSInnvilgelseDTO.Beregningsperiode>>
    ) : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Beregning og utbetaling"
                )
            }
            title2 {
                text(
                    Language.Bokmal to "Slik har vi beregnet omstillingsstønaden din"
                )
            }
            paragraph {
                val formatertGrunnbeloep = grunnbeloep.format()
                textExpr(
                    Language.Bokmal to "Omstillingsstønad beregnes utfra 2,25 ganger folketrygdens grunnbeløp ".expr() +
                            "(G). Dagens verdi av grunnbeløpet er " + formatertGrunnbeloep + " kroner. " +
                            "Stønaden reduseres etter inntekten din og avdødes trygdetid.",
                )
            }
            paragraph {
                table(
                    header = {
                        column(2) {
                            text(Language.Bokmal to "Periode")
                        }
                        column(1) {
                            text(Language.Bokmal to "Din inntekt")
                        }
                        column(2) {
                            text(Language.Bokmal to "Dette får du i omstillingsstønad per måned")
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
                    Language.Bokmal to "Tabellen viser at du ikke får utbetalt omstillingsstønad fordi " +
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
                    Language.Bokmal to datoFOM.format(true) + " - " + datoTOM.format(true)
                )
            } orShow {
                textExpr(
                    Language.Bokmal to datoFOM.format(true) + " - "
                )
            }
    }

    data class Beregningsgrunnlag(
        val inntekt: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Beregningsgrunnlag"
                )
            }
            paragraph {
                val formatertInntekt = inntekt.format()
                textExpr(
                    Language.Bokmal to "Du har opplyst i søknaden at du har en arbeidsinntekt på ".expr() +
                            formatertInntekt + " i inneværende kalenderår. Dette beløpet er høyere enn grensen " +
                            "for å få utbetalt omstillingsstønad. Du kan lese mer om beløpsgrense og hvordan " +
                            "vi beregner inntekt på ${Constants.OMS_HVORMYE_URL}.",
                )
            }
        }
    }

    object Utbetaling : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Utbetaling",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. " +
                            "Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}."
                )
            }
        }
    }

    data class EtterbetalingOgSkatt(
        val virkningsdato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Etterbetaling og skatt"
                )
            }
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                textExpr(
                    Language.Bokmal to "Du får etterbetalt pensjon fra".expr() + formatertVirkningsdato + ". " +
                            "Det trekkes vanligvis skatt av etterbetaling.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Har du mottatt andre ytelser fra NAV eller andre, som for eksempel " +
                            "tjenestepensjonsordninger, kan det bli beregnet fradrag i etterbetalingen. Hvis " +
                            "Skatteetaten eller andre ordninger har krav i etterbetalingen kan utbetalingen bli " +
                            "forsinket. Hvis du får fradrag i etterbetalingen, vil det gå frem av " +
                            "utbetalingsmeldingen din.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Gjelder etterbetalingen tidligere år trekker NAV skatt etter " +
                            "Skatteetatens standardsatser. Du kan lese mer om satsene på " +
                            "${Constants.SKATTETREKK_ETTERBETALING_URL}",
                )
            }
        }
    }

    object Regulering : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Regulering",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen i stønaden " +
                            "din blir vanligvis etterbetalt i juni. Du kan lese mer om regulering på " +
                            "${Constants.OMS_REGULERING_URL}."
                )
            }
        }
    }

    object Aktivitetsplikt : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Aktivitetsplikt",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Formålet med omstillingsstønad er å sikre inntekt for gjenlevende og " +
                            "gi hjelp til selvhjelp, slik at de etter en omstillingsperiode etter dødsfallet " +
                            "kan bli i stand til å forsørge seg selv ved eget arbeid."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Det første halvåret etter dødsfallet stilles det ikke krav til at " +
                            "den gjenlevende er i arbeid eller arbeidsrettet aktivitet. Etter seks måneder er det " +
                            "et vilkår for å fortsatt ha rett til omstillingsstønad at den gjenlevende er i minst " +
                            "50 prosent aktivitet."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Når det er gått ett år etter dødsfallet, kan det stilles krav om " +
                            "at den gjenlevende er i arbeid eller arbeidsrettet aktivitet på full tid."
                )
            }
        }
    }

    object Inntektsendring : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Inntektsendring",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du må si ifra til oss hvis årsinntekten din endrer seg og blir " +
                            "lavere enn <MAKSBELOEP>. Da må vi vurdere om du kan ha rett til " +
                            "utbetaling av omstillingsstønad."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du sier ifra om endringer i inntekt ved å skrive en beskjed til " +
                            "oss på ${Constants.SKRIVTILOSS_URL} eller sende informasjon til " +
                            "NAV Familie- og pensjonsytelser, Postboks 6600, 0607 OSLO."
                )
            }
        }
    }

    object Etteroppgjoer : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Etteroppgjør",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Selv om du ikke har utbetalt omstillingsstønad vil vi hver høst " +
                            "sjekke inntektsopplysningene i skatteoppgjøret ditt for å se om du har " +
                            "fått utbetalt riktig beløp i stønad året før."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom du ikke har hatt utbetalt omstillingsstønad i kalenderåret vi " +
                            "sjekker vil du ikke motta brev om etteroppgjør fra oss. Viser skatteoppgjøret at du har " +
                            "hatt en annen inntekt enn den inntekten vi brukte da vi beregnet omstillingstønaden din, " +
                            "vil vi gjøre en ny beregning. Dette kalles etteroppgjør."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du har fått for lite utbetalt, får du en etterbetaling. " +
                            "Har du fått for mye utbetalt, må du betale tilbake."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Mer informasjon om hvordan vi har behandlet vedtaket",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 17-2 til 17-9 og 22-12/22-13."
                )
            }
        }
    }

}