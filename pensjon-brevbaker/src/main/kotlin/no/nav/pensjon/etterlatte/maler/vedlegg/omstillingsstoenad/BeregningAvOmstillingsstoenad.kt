package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.etterlatte.maler.Beregningsinfo
import no.nav.pensjon.etterlatte.maler.BeregningsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.BeregningsinfoSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BeregningsinfoSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.NyBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.NyBeregningsperiodeSelectors.stoenadFoerReduksjon
import no.nav.pensjon.etterlatte.maler.NyBeregningsperiodeSelectors.trygdetid
import no.nav.pensjon.etterlatte.maler.NyBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.land
import no.nav.pensjon.etterlatte.maler.TrygdetidsperiodeSelectors.opptjeningsperiode

@TemplateModelHelpers
val beregningAvOmstillingsstoenad = createAttachment<LangBokmalNynorskEnglish, Beregningsinfo>(
    title = newText(
        Bokmal to "Beregning av omstillingsstønad",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false
) {

    title2 {
        text(
            Bokmal to "Slik har vi beregnet omstillingsstønaden din",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        textExpr(
            Bokmal to "En uredusert omstillingsstønad beregnes utfra 2,25 ganger folketrygdens ".expr() +
                    "grunnbeløp (G). Dagens verdi av grunnbeløpet er " + grunnbeloep.format() +  " kroner. For at du " +
                    "skal få full stønad må avdødes trygdetid være minst 40 år. Trygdetiden tilsvarer det antall " +
                    "år den avdøde har vært medlem i folketrygden etter fylte 16 år.",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
    paragraph {
        text(
            Bokmal to "Stønaden til en gjenlevende ektefelle reduseres på grunnlag av arbeidsinntekt som " +
                    "er mer enn halvparten av grunnbeløpet. Ytelsen reduseres med 45 prosent av inntekt over " +
                    "halvparten av grunnbeløpet. Stønaden skal ikke reduseres hvis arbeidsinntekt på årsbasis " +
                    "er mindre enn halvparten av grunnbeløpet.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Dette får du i omstillingsstønad før skatt",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        table(
            header = {
                column(2) {
                    text(Bokmal to "Din inntekt før skatt",
                        Nynorsk to "",
                        English to "",
                    )
                }
                column(1) {
                    text(Bokmal to "Trygdetid",
                        Nynorsk to "",
                        English to "",
                    )
                }
                column(2) {
                    text(Bokmal to "Stønad før reduksjon",
                    Nynorsk to "",
                    English to "",
                    )
                }
                column(2) {
                    text(Bokmal to "Brutto utbetaling per måned",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        ) {
            forEach(beregningsperioder) {
                row {
                    cell { includePhrase(Felles.KronerText(it.inntekt)) }
                    cell { textExpr(
                        Bokmal to it.trygdetid.format(),
                        Nynorsk to "".expr(),
                        English to "".expr(),
                        )
                    }
                    cell { includePhrase(Felles.KronerText(it.stoenadFoerReduksjon)) }
                    cell { includePhrase(Felles.KronerText(it.utbetaltBeloep)) }
                }
            }
        }
        text(
            Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen blir vanligvis etterbetalt i juni.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Trygdetid",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        text(
            Bokmal to "Her kommer det tekst",
            Nynorsk to "",
            English to "",
        )
        table(
            header = {
                column(2) {
                    text(Bokmal to "Periode",
                        Nynorsk to "",
                        English to "",
                    )
                }
                column(1) {
                    text(Bokmal to "Land",
                        Nynorsk to "",
                        English to "",
                    )
                }
                column(2) {
                    text(Bokmal to "Opptjeningsperiode",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        ) {
            forEach(trygdetidsperioder) {
                row {
                    cell { includePhrase(PeriodeITabell(it.datoFOM, it.datoTOM)) }
                    cell { textExpr(
                        Bokmal to it.land,
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                    }
                    cell { textExpr(
                        Bokmal to it.opptjeningsperiode,
                        Nynorsk to "".expr(),
                        English to "".expr(),
                        )
                    }
                }
            }
        }
    }
}
