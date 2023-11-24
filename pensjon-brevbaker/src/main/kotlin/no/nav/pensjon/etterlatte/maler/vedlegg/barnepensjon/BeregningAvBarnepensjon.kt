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
        Nynorsk to "Utrekning av barnepensjon",
        English to "Calculation of Children’s Pension",
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
            Nynorsk to "Slik har vi rekna ut barnepensjonen din",
            English to "This is how we calculated your children’s pension",
        )
    }
    showIf(antallBarn.greaterThan(1)) {
        paragraph {
            textExpr(
                Bokmal to "NAV gjør en samlet beregning av pensjon for barn som oppdras sammen. ".expr() +
                        "For denne beregningen har vi lagt til grunn at dere er " + antallBarn.format() +
                        " barn som oppdras sammen.",
                Nynorsk to "NAV gjer ei samla utrekning av pensjon for barn som blir oppdregne saman. ".expr() +
                        "For denne utrekninga har vi lagt til grunn at de er " + antallBarn.format() + " barn som blir oppdregne saman.".expr(),
                English to "NAV makes a combined pension calculation for children who are raised together. ".expr() +
                        "For this calculation, we have determined that your family has " + antallBarn.format() + " children being raised together.",
            )
        }
        paragraph {
            text(
                Bokmal to "Barnepensjon utgjør 40 prosent av folketrygdens grunnbeløp (G) for det " +
                        "første barnet i søskenflokken. For hvert av de andre barna legges det til 25 prosent av G. " +
                        "Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna. " +
                        "Pensjonen fordeles på 12 utbetalinger i året.",
                Nynorsk to "Barnepensjon utgjer 40 prosent av grunnbeløpet i folketrygda (G) for det " +
                        "første barnet i syskenflokken. For kvart av dei andre barna blir det lagt til 25 prosent av G. " +
                        "Summen blir delt på talet på barn, og pensjonen blir utbetalt med likt beløp til kvart av barna. " +
                        "Pensjonen blir fordelt på 12 utbetalingar i året.",
                English to "The children’s pension amounts to 40 percent of the national insurance basic amount (G)" +
                        " for the first child in the sibling group. " +
                        "25 percent of G is added for each of the other children. " +
                        "This sum is divided by the total number of children, " +
                        "and the pension is paid in equal amounts to each of the children. " +
                        "The pension is distributed into 12 disbursements a year.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Folketrygdens grunnbeløp er per i dag ".expr() + grunnbeloep.format() + " kroner. " +
                        "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis juni hvert år.",
                Nynorsk to "Grunnbeløpet i folketrygda er per i dag ".expr() + grunnbeloep.format() + " kroner. " +
                        "Grunnbeløpet blir regulert 1. mai kvart år. Auken blir vanlegvis etterbetalt i juni kvart år.",
                English to "The national insurance basic amount currently amounts to NOK ".expr() + grunnbeloep.format() + " kroner. " +
                        "The basic amount is adjusted on 1 May each year. You will receive payment of any increase in June of each year.",
            )
        }
    } orShow {
        paragraph {
            textExpr(
                Bokmal to "Barnepensjonen utgjør 40 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året. ".expr() +
                        "Folketrygdens grunnbeløp er per i dag " + grunnbeloep.format() + " kroner.",
                Nynorsk to "Barnepensjonen utgjer 40 prosent av grunnbeløpet i folketrygda (G) og blir fordelt på 12 utbetalingar i året. ".expr() +
                        "Grunnbeløpet i folketrygda er per i dag " + grunnbeloep.format() + " kroner",
                English to "A children’s pension amounts to 40 percent of the national insurance basic amount (G), ".expr() +
                        "paid in 12 disbursements a year. " +
                        "The national insurance basic amount currently amounts to NOK " + grunnbeloep.format() + ". "
            )
        }
        paragraph {
            text(
                Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis juni hvert år.",
                Nynorsk to "Grunnbeløpet blir regulert 1. mai kvart år. Auken blir vanlegvis etterbetalt i juni kvart år.",
                English to "The basic amount is adjusted on 1 May each year. You will receive payment of any increase in June of each year.",
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
            Nynorsk to "Trygdetid",
            English to "Period of national insurance coverage",
        )
    }
    paragraph {
        textExpr(
            Bokmal to "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. ".expr() +
                    "Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden etter fylte 16 år. " +
                    "Når avdøde var under 67 år ved dødsfallet blir det beregnet framtidig trygdetid. " +
                    "Det er vanligvis fram til og med det året avdøde ville ha fylt 66 år. " +
                    "Avdødes samledes trygdetid er " + aarTrygdetid.format() + " år",
            Nynorsk to "For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. ".expr() +
                    "Trygdetida svarer til talet på år avdøde var medlem i folketrygda etter fylte 16 år. " +
                    "Dersom personen døde før fylte 67 år, blir det rekna ut framtidig trygdetid. " +
                    "Det er vanlegvis fram til og med det året avdøde ville ha fylt 66 år. " +
                    "Avdøde har ei samla trygdetid på " + aarTrygdetid.format() + ". ",
            English to "To be entitled to a full pension, the deceased must have accumulated at least 40 years ".expr() +
                    " of national insurance coverage. " +
                    "The period of national insurance coverage equals the number of years the deceased " +
                    "has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. " +
                    "If the deceased was less than 67 years old at the time of death, " +
                    "a calculation is made for what would have remained of the future period of national insurance coverage. " +
                    "This is usually calculated up to the year in which the deceased would have turned 66. " +
                    "The deceased's total calculated period of national insurance coverage is " + aarTrygdetid.format() + ". ",
        )
    }
    konverterElementerTilBrevbakerformat(innhold)

    showIf(trygdetidsperioder.isNotEmpty()) {
        includePhrase(Trygdetidstabell(trygdetidsperioder))
        paragraph {
            text(
                Bokmal to "Tabellen viser når avdøde har vært medlem av folketrygden og når avdøde har bodd og/eller arbeidet i land som Norge har trygdeavtale med.",
                Nynorsk to "Tabellen viser når avdøde var medlem i folketrygda, og når avdøde budde og/eller arbeidde i land som Noreg har trygdeavtale med.",
                English to "This table shows the period in which the deceased was a member of the National Insurance Scheme and when the deceased has lived and/or worked in a country with which Norway has a national insurance agreement.",
            )
        }
    }

}