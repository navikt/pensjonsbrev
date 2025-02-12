package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU052V-TBU073V]

        // IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_pebrevkode <> "PE_UT_04_300" AND PE_pebrevkode <> "PE_UT_14_300" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_04_500" AND PE_pebrevkode <> "PE_UT_07_200") THEN      INCLUDE ENDIF
        showIf(
            (
                pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg() and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and
                    pe.pebrevkode()
                        .notEqualTo("PE_UT_04_500") and pe.pebrevkode().notEqualTo("PE_UT_07_200")
            ),
        ) {
            title1 {
                text(
                    Bokmal to "Slik beregner vi gjenlevendetillegget ditt",
                    Nynorsk to "Slik bereknar vi attlevandetillegget ditt",
                    English to "This is how we calculate your survivor's supplement",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når vi beregner gjenlevendetillegget ditt sammenligner vi det du har rett til i uføretrygd beregnet med den avdødes rettigheter, og det du har rett til i uføretrygd ut fra egen opptjening. Dette gjør vi med utgangspunkt i beregningsgrunnlaget ditt, og et beregningsgrunnlag som vi fastsetter for den avdøde. Dersom den avdøde ikke mottok uføretrygd, vil vi likevel fastsette et beregningsgrunnlag, slik at vi kan sammenligne disse grunnlagene. Gjenlevendetillegget utgjør som hovedregel 66 prosent av gjennomsnittet av begge beregningsgrunnlagene.",
                    Nynorsk to "Når vi bereknar attlevandetillegget ditt, samanliknar vi det du har rett til i uføretrygd berekna med rettane til den avdøde, og det du har rett til i uføretrygd ut frå eiga opptening. Vi gjer dette med utgangspunkt i berekningsgrunnlaget ditt, og eit berekningsgrunnlag som vi fastset for den avdøde. Dersom den avdøde ikkje fekk uføretrygd, vil vi likevel fastsetje eit berekningsgrunnlag, slik at vi kan samanlikne desse grunnlaga. Attlevandetillegget utgjer som hovudregel 66 prosent av gjennomsnittet av begge berekningsgrunnlaga.",
                    English to "When calculating your survivor's supplement we will compare your disability benefit including the rights of deceased with your disability benefit not including the rights of deceased. We do this based on your basis for calculation, and a basis for calculation that we set for the deceased. We will set a base of calculation for the deceased even if the deceased did not receive disability benefit, so that we can compare the two bases of calculation. The survivor's supplement will as a main rule equal 66 percent of the averaged of the two bases for calculation.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegg skal sikre at du får en samlet ytelse som minst tilsvarer den minsteytelsen avdøde hadde, eller ville hatt rett til i uføretrygd. Dersom vi har beregnet at avdødes uføretrygd gir rett til minsteytelse, og at denne gir et høyere gjenlevendetillegg, vil du få dette i stedet. Vi har også her trukket fra din egen uføretrygd i beregningen.",
                    Nynorsk to "Attlevandetillegg skal sikre at du får ei samla yting som minst svarar til den minsteytinga avdøde hadde, eller ville hatt rett til i uføretrygd. Dersom vi har berekna at avdøde si uføretrygd gir rett til minsteyting, og at den gir eit høgare attlevandetillegg, så vil du få dette i stedet. Vi har også her trukke frå di eiga uføretrygd i berekninga.",
                    English to "The survivor's supplement will ensure that your minimum benefit including the survivor's supplement will not be lower then the deceased’s right for a minimum benefit. You will get a higher survivor's supplement if we have calculated the deceased disability benefit as a minimum benefit, and this gives you a higher survivor's supplement, you will get this instead. In this amount your own disability benefit is subtracted.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom den egenopptjente uføretrygden din er høyere enn uføretrygd beregnet med avdødes rettigheter, vil du få uføretrygd uten gjenlevendetillegg.",
                    Nynorsk to "Dersom den eigaopptente uføretrygda di er høgare enn uføretrygd berekna med avdøde sine rettar, vil du få uføretrygd utan attlevandetillegg.",
                    English to "You will get disability benefit without survivor's supplement in the case of your own disability benefit being higher then the disability benefit including the deceased rights.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Tillegget vil også bli justert ut fra uføregrad og trygdetid.",
                    Nynorsk to "Tillegget vil også bli justert ut frå uføregrad og trygdetid.",
                    English to "Your survivor's supplement is adjusted in accordance with your degree of disability and your period of national insurance coverage.",
                )
            }
        }

        // IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND (PE_pebrevkode = "PE_UT_04_300" OR PE_pebrevkode = "PE_UT_14_300")
        showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and (pe.pebrevkode().equalTo("PE_UT_04_300") or pe.pebrevkode().equalTo("PE_UT_14_300"))) {
            title1 {
                text(
                    Bokmal to "Slik beregner vi gjenlevendetillegget ditt",
                    Nynorsk to "Slik bereknar vi attlevandetillegget ditt",
                    English to "This is how we calculate your survivor's supplement",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når vi beregner gjenlevendetillegget ditt sammenligner vi det du har rett til i uførepensjon beregnet med avdødes rettigheter, og det du har rett til i uførepensjon ut fra egen opptjening. Vi regner om månedsbeløpene for desember 2014 til årsbeløp. Differansen mellom disse beløpene skal legges til grunn- og tilleggspensjonen din ut fra egen opptjening. Vi regner om grunn- og tilleggspensjonen din ut fra egen opptjening til 100 prosent uføregrad. For å ta hensyn til at skatten øker, blir det samlede beløpet justert opp etter overgangsregler. Dette er beregningsgrunnlaget ditt med gjenlevenderettighet.",
                    Nynorsk to "Når vi bereknar attlevandetillegget ditt, samanliknar vi det du har rett til i uførepensjon berekna med rettane til den avdøde, og det du har rett til i uførepensjon ut frå eiga opptening. Vi reknar om månadsbeløpa for desember 2014 til årsbeløp. Differansen mellom desse beløpa skal leggjast til grunn- og tilleggspensjonen din ut frå eiga opptening. Vi reknar om grunn- og tilleggspensjonen din ut frå di eiga opptening til 100 prosent uføregrad. For å ta omsyn til at skatten aukar, blir det samla beløpet justert opp etter overgangsreglar. Dette er berekningsgrunnlaget ditt med attlevanderett.",
                    English to "When we calculate your survivor's supplement, we compare the amount you would be entitled to in disability pension with the decedent's rights with what you would be entitled to in disability pension with your accumulated rights. The monthly amounts applicable for December 2014 will be recalculated into an annual amount. The difference between these amounts will be added to your basic and supplementary pension with your accumulated rights. We recalculated your basic and supplementary pension with your accumulated rights to a degree of disability of 100 percent. In order to compensate for increased taxes, your annual amount is increased in accordance with transition rules. This is your basis for calculation including your survivor's rights.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Beregningsgrunnlaget ditt ut fra egen opptjening skal justeres for trygdetid og trekkes fra beregningsgrunnlaget med gjenlevenderettighet. Denne differansen deles deretter på uføregraden din, og 66 prosent av dette vil utgjøre gjenlevendetillegget ditt. Tillegget vil til slutt justeres ut fra uføregraden din.",
                    Nynorsk to "Berekningsgrunnlaget ditt ut frå eiga opptening skal justerast for trygdetid og trekkjast frå berekningsgrunnlaget med attlevanderett. Denne differansen blir deretter delt på uføregraden din, og 66 prosent av dette vil utgjere attlevandetillegget ditt. Tillegget blir til slutt justert ut frå uføregraden din.",
                    English to "Your basis for calculation with your own accumulated rights is adjusted for your period of national insurance coverage and deducted from your basis for calculation with survivor's rights. This difference is then divided by your degree of disability, and 66 percent of that total will be your survivor's supplement. Finally, the supplement will be adjusted in accordance with your degree of disability.",
                )
            }
        }
    }
}
