package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.multiply
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.innhold
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.trygdetid
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.aarsinntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.fratrekkInnAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.relevantMaanederInnAar
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.restanse
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregnetTrygdetidAar
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeAnvendt
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeFraGrunnlag
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.mindreEnnFireFemtedelerAvOpptjeningstiden
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.formatBroek
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.Trygdetidstabell

@TemplateModelHelpers
val beregningAvOmstillingsstoenad = createAttachment(
    title = newText(
        Bokmal to "Beregning av omstillingsstønad",
        Nynorsk to "Utrekning av omstillingsstønad",
        English to "Calculation of Adjustment Allowance",
    ),
    includeSakspart = false
) {
    beregning()
    trygdetid(trygdetid)
    perioderMedRegistrertTrygdetid(trygdetid)
    meldFraTilNav()
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, OmstillingsstoenadBeregning>.beregning() {
    val grunnbeloep = sisteBeregningsperiode.grunnbeloep
    val aarsinntekt = sisteBeregningsperiode.aarsinntekt
    val fratrekkInnAar = sisteBeregningsperiode.fratrekkInnAar
    val gjenvaerendeMaaneder = sisteBeregningsperiode.relevantMaanederInnAar

    paragraph {
        textExpr(
            Bokmal to "Full omstillingsstønad beregnes utfra 2,25 ganger folketrygdens ".expr() +
                    "grunnbeløp (G). Dagens verdi av grunnbeløpet er " + grunnbeloep.format() + " kroner. " +
                    "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis i juni hvert år.",
            Nynorsk to "Full omstillingsstønad blir rekna ut etter 2,25 gongar grunnbeløpet i ".expr() +
                    "folketrygda (G). Dagens verdi av grunnbeløpet er " + grunnbeloep.format() + " kroner. " +
                    "Grunnbeløpet blir regulert 1. mai kvart år. Auken blir vanlegvis etterbetalt i " +
                    "juni kvart år.",
            English to "Full adjustment allowance are calculated based on 2.25 × the national ".expr() +
                    "insurance basic amount (G). The current value of the basic amount is NOK " + grunnbeloep.format() +
                    ". The basic amount is adjusted on 1 May each year. You will receive payment of any increase in " +
                    "June of each year.",
        )
    }
    paragraph {
        text(
            Bokmal to "For at du skal få full stønad må avdødes trygdetid være minst 40 år. Er trygdetiden " +
                    "mindre enn 40 år vil stønaden avkortes.",
            Nynorsk to "For at du skal få full stønad må avdøde ha hatt ei trygdetid på minst 40 år. Dersom " +
                    "trygdetida er mindre enn 40 år, blir stønaden avkorta.",
            English to "To receive the maximum allowance, the deceased's contribution time to the national " +
                    "insurance scheme must be at least 40 years. If the contribution time is less than 40 years, " +
                    "the allowance is reduced.",
        )
    }
    paragraph {
        text(
            Bokmal to "Inntekten din avgjør hvor mye du kan få. Stønaden reduseres med 45 prosent av " +
                    "arbeidsinntekt eller tilsvarende inntekt som er over halvparten av grunnbeløpet.",
            Nynorsk to "Inntekta di avgjer kor mykje du kan få. Stønaden blir redusert med 45 prosent av " +
                    "arbeidsinntekt eller tilsvarande inntekt som er over halvparten av grunnbeløpet.",
            English to "Your income determines how much money you are entitled to. The allowance is reduced by " +
                    "45 percent of the employment income or similar income when this is over half the basic amount.",
        )
    }

    title2 {
        text(
            Bokmal to "Beregnet omstillingsstønad",
            Nynorsk to "Utrekna omstillingsstønad",
            English to "Calculated adjustment allowance",
        )
    }

    includePhrase(Beregningsperiodetabell(beregningsperioder))

    paragraph {
        text(
            Bokmal to "Beløpene i tabellen er før skatt.",
            Nynorsk to "Beløpa i tabellen er før skatt.",
            English to "The amounts in the table are stated before tax.",
        )
    }

    title2 {
        text(
            Bokmal to "Stønad før reduksjon for inntekt",
            Nynorsk to "Stønad før reduksjon for inntekt",
            English to "Allowance paid before income reduction",
        )
    }

    showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Stønad per år før reduksjon av inntekt er beregnet til 2,25 ganger " +
                        "grunnbeløpet i folketrygden (G) ganget med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                        "trygdetid. Beløpet fordeles på 12 utbetalinger i året.",
                Nynorsk to "".expr() + "Stønad per år før reduksjon av inntekt er rekna ut til 2,25 gongar " +
                        "grunnbeløpet i folketrygda (G) gonga med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                        "trygdetid. Beløpet blir fordelt på 12 utbetalingar i året.",
                English to "".expr() + "Allowance per year before reduction of income are calculated " +
                        "based on 2.25 × national insurance basic amount (G) × " +
                        trygdetid.beregnetTrygdetidAar.format() + "/40 years of contribution time. This amount " +
                        "is distributed in 12 payments a year.",
            )
        }
    }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Stønad per år før reduksjon for inntekt er beregnet til 2,25 ganger " +
                        "grunnbeløpet i folketrygden ganget med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                        "trygdetid, ganget med forholdstallet " + trygdetid.prorataBroek.formatBroek() + ". " +
                        "Beløpet fordeles på 12 utbetalinger i året.",
                Nynorsk to "".expr() + "Stønad per år før reduksjon for inntekt er rekna ut til 2,25 gongar " +
                        "grunnbeløpet i folketrygda gonga med " + trygdetid.beregnetTrygdetidAar.format() + "/40 " +
                        "trygdetid, gonga med forholdstalet " + trygdetid.prorataBroek.formatBroek() + ". Beløpet " +
                        "blir fordelt på 12 utbetalingar i året.",
                English to "".expr() + "Allowance per year before reduction of income are calculated based on " +
                        "2.25 × national insurance basic amount (G) × " + trygdetid.beregnetTrygdetidAar.format() +
                        "/40 years of contribution time, multiplied by the proportional fraction " +
                        trygdetid.prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year.",
            )
        }
    }

    title2 {
        text(
            Bokmal to "Inntekten din",
            Nynorsk to "Inntekta di",
            English to "Your income",
        )
    }

    val inntekt = sisteBeregningsperiode.inntekt

    showIf(inntekt.greaterThan(0)) {
        paragraph {
            text(
                Bokmal to "I innvilgelsesåret skal inntekt opptjent før innvilgelse trekkes fra, og resterende " +
                        "forventet inntekt fordeles på gjenværende måneder.",
                Nynorsk to "I innvilgingsåret skal det trekkjast frå inntekt som blei tent opp før innvilginga. " +
                        "Resterande forventa inntekt skal fordelast på dei resterande månadene.",
                English to "In the year the allowance is granted, your income earned before the allowance is " +
                        "granted, is deducted, and your remaining estimated income for the year is divided by " +
                        "the number of remaining months.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Din oppgitte årsinntekt er ".expr() + aarsinntekt.format() + " kroner. Fratrekk " +
                        "for inntekt i år er " + fratrekkInnAar.format() + " kroner. Vi har lagt til grunn " +
                        "at du har en forventet inntekt på " + inntekt.format() + " kroner fra " +
                        virkningsdato.format() + ".",
                Nynorsk to "Du har ei oppgitt årsinntekt på ".expr() + aarsinntekt.format() + " kroner. " +
                        "Fråtrekk for inntekt i år er " + fratrekkInnAar.format() + " kroner. Vi har lagt til grunn " +
                        "at du har ei forventa inntekt på " + inntekt.format() + " kroner frå " +
                        virkningsdato.format() + ".",
                English to "Your estimated annual income is NOK ".expr() + aarsinntekt.format() + ". " +
                        "The deduction for income this year is NOK " + fratrekkInnAar.format() + ". We have " +
                        "assumed that your estimated remaining annual income will be NOK " + inntekt.format() +
                        " from " + virkningsdato.format() + ".",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Stønad per måned er redusert på følgende måte: (".expr() + inntekt.format() +
                        " kroner / " + gjenvaerendeMaaneder.format() + " måneder) minus (0,5 G / 12 måneder). " +
                        "Beløpet ganges med 45 prosent.",
                Nynorsk to "Stønaden per månad har blitt redusert på følgjande måte: (".expr() + inntekt.format() +
                        " kroner / " + gjenvaerendeMaaneder.format() + " månader) minus (0,5 G / 12 månader). " +
                        "Beløpet blir gonga med 45 prosent.",
                English to "The monthly allowance amount has been reduced as follows: (NOK ".expr() +
                        inntekt.format() + " / " + gjenvaerendeMaaneder.format() + " months) minus (0.5 G / 12 months). " +
                        "The amount is multiplied by 45 percent.",
            )
        }

        showIf(sisteBeregningsperiode.restanse.notEqualTo(0)) {
            val erRestanseTrekk = sisteBeregningsperiode.restanse.lessThan(0)
            val restanse = ifElse(
                erRestanseTrekk,
                sisteBeregningsperiode.restanse.multiply(Kroner(-1).expr()),
                sisteBeregningsperiode.restanse
            )
            title2 {
                textExpr(
                    Bokmal to ifElse(erRestanseTrekk, "Trekk", "Tillegg") + " i utbetalingen",
	                Nynorsk to "".expr(),
	                English to "".expr()
                )
            }
            paragraph {
	            textExpr(
                    Bokmal to "Den forventede inntekten din for inneværende år er blitt justert. ".expr() +
                            "Det blir " + ifElse(erRestanseTrekk, "gjort et trekk", "gitt et tillegg") +
                            " i utbetalingen for resten av året for å redusere etteroppgjøret. " +
                            "Du får " + restanse.format() + " kroner " + ifElse(erRestanseTrekk, "mindre", "mer") +
                            " enn det som fremgår i tabellen over, under “Utbetaling per måned”.".expr(),
		            Nynorsk to "".expr(),
		            English to "".expr()
	            )
            }
        }

    }.orShow {
        paragraph {
            text(
                Bokmal to "Vi har lagt til grunn at du ikke har arbeidsinntekt eller tilsvarende inntekt som " +
                        "omstillingsstønaden skal reduseres etter.",
                Nynorsk to "Vi har lagt til grunn at du ikkje har arbeidsinntekt eller tilsvarande inntekt som " +
                        "omstillingsstønaden skal reduserast etter. ",
                English to "Our calculation shows that you had no employment income or similar income from " +
                        "which the adjustment allowance can be reduced.",
            )
        }
    }

    konverterElementerTilBrevbakerformat(innhold)

}


private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.trygdetid(
    trygdetid: Expression<Trygdetid>,
) {
    title2 {
        text(
            Bokmal to "Trygdetid",
            Nynorsk to "Trygdetid",
            English to "Period of national insurance coverage",
        )
    }

    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            text(
                Bokmal to "Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden " +
                        "etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det vanligvis " +
                        "beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                Nynorsk to "Trygdetida svarer til talet på år avdøde var medlem i folketrygda etter fylte " +
                        "16 år. Dersom personen døydde før fylte 67 år, blir det vanlegvis rekna ut framtidig " +
                        "trygdetid fram til og med det året vedkomande ville ha fylt 66 år. ",
                English to "Contribution time is the number of years the deceased has been a member of the " +
                        "Norwegian National Insurance Scheme after reaching the age of 16. For deceased persons " +
                        "under 67 years of age at the time of death, the general rule is to calculate future " +
                        "contribution time up to and including the year the deceased would have turned 66.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "".expr() + "For å få full omstillingsstønad må avdødes trygdetid være beregnet " +
                        "til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. Avdødes samlede " +
                        "trygdetid er beregnet til " + trygdetid.beregnetTrygdetidAar.format() + " år.",
                Nynorsk to "".expr() + "For at du skal kunne få full omstillingsstønad, må den utrekna " +
                        "trygdetida til avdøde vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i " +
                        "utrekninga. Avdøde har ei samla trygdetid på " + trygdetid.beregnetTrygdetidAar.format() +
                        " år. ",
                English to "".expr() + "To be entitled to full adjustment allowance, the deceased must have " +
                        "accumulated at least 40 years of contribution time. Contribution time above 40 years of " +
                        "coverage is not included in the calculation. The deceased's total calculated contribution " +
                        "time is " + trygdetid.beregnetTrygdetidAar.format() + " years.",
            )
        }
        showIf(trygdetid.mindreEnnFireFemtedelerAvOpptjeningstiden) {
            paragraph {
                text(
                    Bokmal to "Tabellen under «Perioder med registrert trygdetid» viser full framtidig " +
                            "trygdetid. Siden avdøde har vært medlem i folketrygden i mindre enn 4/5 av tiden " +
                            "mellom fylte 16 år og dødsfallstidspunktet (opptjeningstiden), blir framtidig " +
                            "trygdetid redusert til 40 år minus 4/5 av opptjeningstiden. Dette er mindre enn " +
                            "det tabellen viser.",
                    Nynorsk to "Tabellen under «Periodar med registrert trygdetid» viser full framtidig " +
                            "trygdetid. Ettersom avdøde var medlem av folketrygda i mindre enn 4/5 av tida mellom " +
                            "fylte 16 år og fram til sin død (oppteningstid), blir framtidig trygdetid redusert " +
                            "til 40 år minus 4/5 av oppteningstida. Dette er mindre enn det tabellen viser.",
                    English to "The table in “Periods with Registered Contribution Time” shows the entire " +
                            "future contribution time. Since the deceased has been a member of the National Insurance " +
                            "Scheme for less than 4/5 of the time between turning 16 and the date of the death " +
                            "(qualifying period), the future contribution time is reduced to 40 years minus 4/5 of " +
                            "the qualifying period. This is less than what the table show.",
                )
            }
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                Bokmal to "For å få full omstillingsstønad må avdødes trygdetid være beregnet til minst 40 år. " +
                        "Trygdetid over 40 år blir ikke tatt med i beregningen.",
                Nynorsk to "For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida " +
                        "til avdøde vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga.",
                English to "To be entitled to full adjustment allowance, the deceased must have accumulated " +
                        "at least 40 years of contribution time. Contribution time above 40 years of coverage is not " +
                        "included in the calculation.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Omstillingsstønaden din er beregnet etter bestemmelsene i EØS-avtalen " +
                        "fordi vilkårene for rett til omstillingsstønad er oppfylt ved sammenlegging av avdødes " +
                        "opptjeningstid i Norge og andre EØS- eller avtaleland. Trygdetiden er beregnet etter avdødes " +
                        "samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges " +
                        "avdødes samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk " +
                        "opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS- eller " +
                        "avtaleland. Avdødes samlede trygdetid er beregnet til " +
                        trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                        trygdetid.prorataBroek.formatBroek() + ".",
                Nynorsk to "".expr() + "Omstillingsstønaden din er rekna ut etter føresegnene i EØS-avtalen, då vilkåra " +
                        "for rett til stønad er oppfylte ved samanlegging av oppteningstida til avdøde i Noreg og " +
                        "andre EØS- eller avtaleland. Trygdetida er rekna ut etter den samla oppteningstida som " +
                        "avdøde hadde i desse landa. For å rekne ut den norske del av denne trygdetida blir den " +
                        "samla oppteningstida til avdøde gonga med eit forholdstal som angir forholdet mellom " +
                        "faktisk oppteningstid i Noreg og samla faktisk oppteningstid i Noreg og andre EØS- eller " +
                        "avtaleland. Avdøde har ei samla trygdetid på " + trygdetid.beregnetTrygdetidAar.format() +
                        " år, og forholdstalet er " + trygdetid.prorataBroek.formatBroek() + ".",
                English to "".expr() + "Your adjustment allowance are calculated according to the provisions " +
                        "in the EEA Agreement because the conditions that entitle you to the allowance have been met, " +
                        "by compiling the deceased's contribution time in Norway and other EEA countries or other " +
                        "countries with which Norway has an agreement. Contribution time is calculated according to " +
                        "the deceased's total contribution time in these the countries. To calculate the Norwegian " +
                        "part of this contribution time, the deceased's total contribution time is multiplied with " +
                        "a proportional fraction that provides a ratio between the actual contribution time in Norway " +
                        "and the total actual contribution time in Norway and any other EEA or agreement country. " +
                        "The deceased's total contribution time amounts to " + trygdetid.beregnetTrygdetidAar.format() +
                        " years, and the proportional fraction of " + trygdetid.prorataBroek.formatBroek() + ".",
            )
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.BEST)) {
        paragraph {
            text(
                Bokmal to "For å få full omstillingsstønad må avdødes trygdetid være beregnet til minst 40 år. " +
                        "Trygdetid over 40 år blir ikke tatt med i beregningen. Når grunnlag for omstillingsstønaden " +
                        "er oppfylt etter nasjonale regler, og avdøde også har opptjening av medlemsperioder i land " +
                        "som Norge har trygdeavtale med, skal trygdetid gis etter den beste beregningen av kun " +
                        "nasjonal opptjening og av sammenlagt opptjening i Norge og avtaleland.",
                Nynorsk to "For at du skal kunne få full omstillingsstønad, må den utrekna trygdetida til " +
                        "avdøde vere minst 40 år. Trygdetid over 40 år blir ikkje teken med i utrekninga. Når " +
                        "grunnlaget for stønad er oppfylt etter nasjonale reglar, og avdøde også har opptening " +
                        "av medlemsperiodar i land som Noreg har trygdeavtale med, skal det bli gitt trygdetid " +
                        "etter gunstigaste utrekning: anten berre nasjonal opptening eller samanlagd opptening " +
                        "i Noreg og avtaleland.",
                English to "To be entitled to full adjustment allowance, the deceased must have accumulated " +
                        "at least 40 years of contribution time. Contribution time above 40 years of coverage is " +
                        "not included in the calculation. When the basis for the allowance is met according to " +
                        "national rules, and the deceased has also accrued membership periods in countries with " +
                        "which Norway has a national insurance agreement, the contribution time must be stated " +
                        "according to the best calculation of (only) national contribution and of the combined " +
                        "contribution time in Norway and the agreement countries.",
            )
        }
        paragraph {
            text(
                Bokmal to "Ved nasjonal beregning av trygdetid tilsvarer denne det antall år avdøde har vært " +
                        "medlem i folketrygden etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det " +
                        "vanligvis beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                Nynorsk to "Ved nasjonal utrekning av trygdetida vil denne svare til talet på år avdøde var " +
                        "medlem i folketrygda etter fylte 16 år. Dersom personen døydde før fylte 67 år, blir det " +
                        "vanlegvis rekna ut framtidig trygdetid fram til og med det året vedkomande ville ha " +
                        "fylt 66 år.",
                English to "For calculating national contribution time, this equals the number of years the " +
                        "deceased has been a member of the Norwegian National Insurance Scheme after reaching the " +
                        "age of 16. For deceased persons under 67 years of age at the time of death, the general " +
                        "rule is to calculate future contribution time up to and including the year the deceased " +
                        "would have turned 66.",
            )
        }
        paragraph {
            text(
                Bokmal to "Ved sammenlegging av avdødes opptjeningstid i Norge og andre EØS/avtale-land er " +
                        "trygdetiden beregnet etter avdødes samlede opptjeningstid i disse landene. For å beregne " +
                        "norsk del av denne trygdetiden ganges avdødes samlede opptjeningstid med et forholdstall, " +
                        "som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjeningstid " +
                        "i Norge og andre EØS-land.",
                Nynorsk to "Dersom ein legg saman oppteningstida som avdøde hadde i Noreg og andre " +
                        "EØS-/avtaleland, blir trygdetida rekna ut etter den samla oppteningstida til avdøde i " +
                        "desse landa. For å rekne ut den norske del av denne trygdetida blir den samla oppteningstida " +
                        "til avdøde gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i " +
                        "Noreg og samla faktisk oppteningstid i Noreg og andre EØS-land.",
                English to "For comparing the deceased's contribution time in Norway with other EEA/agreement " +
                        "countries, the contribution time is calculated according to the deceased's total " +
                        "contribution time in these the countries. To calculate the Norwegian part of this " +
                        "contribution time, the deceased's total contribution time is multiplied with a " +
                        "proportional fraction that provides the ratio between the actual contribution time " +
                        "in Norway and the total actual contribution time in Norway and any other EEA or " +
                        "agreement country.",
            )
        }

        showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "Avdødes samlede trygdetid fra avtaleland er beregnet til " +
                            trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                            trygdetid.prorataBroek.formatBroek() + ". Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to "".expr() + "Avdøde har ei samla trygdetid på " +
                            trygdetid.beregnetTrygdetidAar.format() + " år frå avtaleland, og forholdstalet " +
                            "er " + trygdetid.prorataBroek.formatBroek() +". Dette gir den gunstigaste utrekninga " +
                            "av trygdetid.",
                    English to "".expr() + "The deceased's total contribution time from agreement countries " +
                            "amounts to " + trygdetid.beregnetTrygdetidAar.format() + " years, and the proportional " +
                            "fraction of " + trygdetid.prorataBroek.formatBroek() + ". This provides the best " +
                            "calculation for total contribution time.",
                )
            }
        }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "Avdødes samlede trygdetid er beregnet til " +
                            trygdetid.beregnetTrygdetidAar.format() + " år ved nasjonal opptjening. " +
                            "Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to "".expr() + "Avdøde har ei samla trygdetid på " +
                            trygdetid.beregnetTrygdetidAar.format() + " år ved nasjonal opptening. Dette gir den " +
                            "gunstigaste utrekninga av trygdetid.",
                    English to "".expr() + "The deceased's total calculated contribution time is " +
                            trygdetid.beregnetTrygdetidAar.format() + " years in national contributions. " +
                            "This provides the best calculation for total contribution time."
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.perioderMedRegistrertTrygdetid(
    trygdetid: Expression<Trygdetid>
) {
    title2 {
        text(
            Bokmal to "Perioder med registrert trygdetid",
            Nynorsk to "Periodar med registrert trygdetid",
            English to "Periods of registered contribution time",
        )
    }

    showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)){
        paragraph {
            text(
                Bokmal to "Tabellen viser perioder avdøde har vært medlem av folketrygden, og registrert " +
                        "fremtidig trygdetid.",
                Nynorsk to "Tabellen viser periodar avdøde har vore medlem av folketrygda og registrert " +
                        "framtidig trygdetid. ",
                English to "The table shows the periods in which the deceased was a member of the National " +
                        "Insurance Scheme, and registered future contribution time.",
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                Bokmal to "Tabellen viser perioder avdøde har vært medlem av folketrygden og medlemsperioder " +
                        "avdøde har hatt i land som Norge har trygdeavtale med, som er tatt med i beregningen.",
                Nynorsk to "Tabellen viser periodar avdøde har vore medlem av folketrygda og medlemsperiodar " +
                        "avdøde har hatt i land som Noreg har trygdeavtale med, som er tekne med i utrekninga.",
                English to "The table shows periods in which the deceased was a member of the National " +
                        "Insurance Scheme and member periods which the deceased contributed in countries which " +
                        "Norway had a national insurance agreement which are included in the calculation.",
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.meldFraTilNav() {
    paragraph {
        text(
            Bokmal to "Hvis du mener at opplysningene brukt i beregningen er feil, må du melde fra til NAV. Det kan ha betydning for størrelsen på omstillingsstønaden din.",
            Nynorsk to "Sei frå til NAV dersom du meiner at det er brukt feil opplysningar i utrekninga. Det kan ha betydning for kor mykje omstillingsstønad du får.",
            English to "If you believe the information applied in the calculation is incorrect, you must notify NAV. Errors may affect your allowance amount."
        )
    }
}