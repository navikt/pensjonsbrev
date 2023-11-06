package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBP
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBPSelectors.aarTrygdetid
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBPSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBPSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBPSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBPSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBPSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.Trygdetidstabell

@TemplateModelHelpers
val beregningAvBarnepensjon = createAttachment(
    title = newText(
        Bokmal to "Beregning av barnepensjon",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false
) {
    slikHarViBeregnetPensjonenDin(
        beregningsperioder = beregningsperioder,
        grunnbeloep = grunnbeloep,
        antallBarn = antallBarn
    )
    trygdetid(aarTrygdetid, trygdetidsperioder)
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.slikHarViBeregnetPensjonenDin(
    beregningsperioder: Expression<List<Beregningsperiode>>,
    grunnbeloep: Expression<Kroner>,
    antallBarn: Expression<Int>
) {
    title2 {
        text(
            Bokmal to "Slik har vi beregnet barnepensjonen din",
            Nynorsk to "",
            English to "",
        )
    }
    showIf(antallBarn.greaterThan(1)) {
        paragraph {
            textExpr(
                Bokmal to "NAV gjør en samlet beregning av pensjon for barn som oppdras sammen. ".expr() +
                        "For denne beregningen har vi lagt til grunn at dere er " + antallBarn.format() +
                        " barn som oppdras sammen.",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
        paragraph {
            text(
                Bokmal to "Barnepensjon utgjør 40 prosent av folketrygdens grunnbeløp (G) for det " +
                        "første barnet i søskenflokken. For hvert av de andre barna legges det til 25 prosent av G. " +
                        "Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna. " +
                        "Pensjonen fordeles på 12 utbetalinger i året.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Folketrygdens grunnbeløp er per i dag ".expr() + grunnbeloep.format() + " kroner. " +
                        "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis juni hvert år.",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    } orShow {
        paragraph {
            textExpr(
                Bokmal to "Barnepensjonen utgjør 40 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året. ".expr() +
                        "Folketrygdens grunnbeløp er per i dag " + grunnbeloep.format() + " kroner.",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
        paragraph {
            text(
                Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis juni hvert år.",
                Nynorsk to "",
                English to "",
            )
        }
    }
    includePhrase(Beregningsperiodetabell(beregningsperioder))
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.trygdetid(
    aarTrygdetid: Expression<Int>,
    trygdetidsperioder: Expression<List<Trygdetidsperiode>>
) {

    title2 {
        text(
            Bokmal to "Trygdetid",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        textExpr(
            Bokmal to "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. ".expr() +
                    "Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden etter fylte 16 år. " +
                    "Når avdøde var under 67 år ved dødsfallet blir det beregnet framtidig trygdetid. " +
                    "Det er vanligvis fram til og med det året avdøde ville ha fylt 66 år. " +
                    "Avdødes samledes trygdetid er " + aarTrygdetid.format() + " år",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
    konverterElementerTilBrevbakerformat(innhold)

    showIf(trygdetidsperioder.isNotEmpty()) {
        includePhrase(Trygdetidstabell(trygdetidsperioder))
        paragraph {
            text(
                Bokmal to "Tabellen viser når avdøde har vært medlem av folketrygden og når avdøde har bodd og/eller arbeidet i land som Norge har trygdeavtale med.",
                Nynorsk to "",
                English to "",
            )
        }
    }

}