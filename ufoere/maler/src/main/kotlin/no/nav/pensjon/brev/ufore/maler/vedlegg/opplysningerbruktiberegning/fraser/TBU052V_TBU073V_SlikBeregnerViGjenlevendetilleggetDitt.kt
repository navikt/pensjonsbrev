package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt. Brevkode-/kravårsak-/
 * gjenlevendetillegg-betingelsene er trukket ut til [Visningsflagg].
 */
data class TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt(
    val flagg: Expression<Visningsflagg>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.visGjenlevendetilleggBeregning) {
            title1 {
                text(
                    bokmal { + "Slik beregner vi gjenlevendetillegget ditt" },
                    nynorsk { + "Slik bereknar vi attlevandetillegget ditt" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Når vi beregner gjenlevendetillegget ditt sammenligner vi det du har rett til i uføretrygd beregnet med den avdødes rettigheter, og det du har rett til i uføretrygd ut fra egen opptjening. Dette gjør vi med utgangspunkt i beregningsgrunnlaget ditt, og et beregningsgrunnlag som vi fastsetter for den avdøde. Dersom den avdøde ikke mottok uføretrygd, vil vi likevel fastsette et beregningsgrunnlag, slik at vi kan sammenligne disse grunnlagene. Gjenlevendetillegget utgjør som hovedregel 66 prosent av gjennomsnittet av begge beregningsgrunnlagene." },
                    nynorsk { + "Når vi bereknar attlevandetillegget ditt, samanliknar vi det du har rett til i uføretrygd berekna med rettane til den avdøde, og det du har rett til i uføretrygd ut frå eiga opptening. Vi gjer dette med utgangspunkt i berekningsgrunnlaget ditt, og eit berekningsgrunnlag som vi fastset for den avdøde. Dersom den avdøde ikkje fekk uføretrygd, vil vi likevel fastsetje eit berekningsgrunnlag, slik at vi kan samanlikne desse grunnlaga. Attlevandetillegget utgjer som hovudregel 66 prosent av gjennomsnittet av begge berekningsgrunnlaga." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Gjenlevendetillegg skal sikre at du får en samlet ytelse som minst tilsvarer den minsteytelsen avdøde hadde, eller ville hatt rett til i uføretrygd. Dersom vi har beregnet at avdødes uføretrygd gir rett til minsteytelse, og at denne gir et høyere gjenlevendetillegg, vil du få dette i stedet. Vi har også her trukket fra din egen uføretrygd i beregningen." },
                    nynorsk { + "Attlevandetillegg skal sikre at du får ei samla yting som minst svarar til den minsteytinga avdøde hadde, eller ville hatt rett til i uføretrygd. Dersom vi har berekna at avdøde si uføretrygd gir rett til minsteyting, og at den gir eit høgare attlevandetillegg, så vil du få dette i stedet. Vi har også her trukke frå di eiga uføretrygd i berekninga." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dersom den egenopptjente uføretrygden din er høyere enn uføretrygd beregnet med avdødes rettigheter, vil du få uføretrygd uten gjenlevendetillegg." },
                    nynorsk { + "Dersom den eigaopptente uføretrygda di er høgare enn uføretrygd berekna med avdøde sine rettar, vil du få uføretrygd utan attlevandetillegg." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Tillegget vil også bli justert ut fra uføregrad og trygdetid." },
                    nynorsk { + "Tillegget vil også bli justert ut frå uføregrad og trygdetid." },
                )
            }
        }

        showIf(flagg.visGjenlevendetilleggBeregningUP) {
            title1 {
                text(
                    bokmal { + "Slik beregner vi gjenlevendetillegget ditt" },
                    nynorsk { + "Slik bereknar vi attlevandetillegget ditt" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Når vi beregner gjenlevendetillegget ditt sammenligner vi det du har rett til i uførepensjon beregnet med avdødes rettigheter, og det du har rett til i uførepensjon ut fra egen opptjening. Vi regner om månedsbeløpene for desember 2014 til årsbeløp. Differansen mellom disse beløpene skal legges til grunn- og tilleggspensjonen din ut fra egen opptjening. Vi regner om grunn- og tilleggspensjonen din ut fra egen opptjening til 100 prosent uføregrad. For å ta hensyn til at skatten øker, blir det samlede beløpet justert opp etter overgangsregler. Dette er beregningsgrunnlaget ditt med gjenlevenderettighet." },
                    nynorsk { + "Når vi bereknar attlevandetillegget ditt, samanliknar vi det du har rett til i uførepensjon berekna med rettane til den avdøde, og det du har rett til i uførepensjon ut frå eiga opptening. Vi reknar om månadsbeløpa for desember 2014 til årsbeløp. Differansen mellom desse beløpa skal leggjast til grunn- og tilleggspensjonen din ut frå eiga opptening. Vi reknar om grunn- og tilleggspensjonen din ut frå di eiga opptening til 100 prosent uføregrad. For å ta omsyn til at skatten aukar, blir det samla beløpet justert opp etter overgangsreglar. Dette er berekningsgrunnlaget ditt med attlevanderett." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Beregningsgrunnlaget ditt ut fra egen opptjening skal justeres for trygdetid og trekkes fra beregningsgrunnlaget med gjenlevenderettighet. Denne differansen deles deretter på uføregraden din, og 66 prosent av dette vil utgjøre gjenlevendetillegget ditt. Tillegget vil til slutt justeres ut fra uføregraden din." },
                    nynorsk { + "Berekningsgrunnlaget ditt ut frå eiga opptening skal justerast for trygdetid og trekkjast frå berekningsgrunnlaget med attlevanderett. Denne differansen blir deretter delt på uføregraden din, og 66 prosent av dette vil utgjere attlevandetillegget ditt. Tillegget blir til slutt justert ut frå uføregraden din." },
                )
            }
        }
    }
}
