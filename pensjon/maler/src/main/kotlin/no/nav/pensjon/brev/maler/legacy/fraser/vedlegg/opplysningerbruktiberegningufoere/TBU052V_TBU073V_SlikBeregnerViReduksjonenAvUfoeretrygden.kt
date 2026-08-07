package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.exstreamFunctions.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.*
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        // [TBU052V-TBU073V]
        // IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
        //    AND BelopGammelUT <> BelopNyUT
        //    AND ForventetInntekt >= Inntektsgrense
        //    AND Inntektsgrense < Inntektstak
        //    AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109"
        //    AND BelopNyUT > 0) THEN INCLUDE ENDIF
        val skalViseReduksjon =
            pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") and
                pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()) and
                pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThanOrEqual(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()) and
                pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()) and
                pe.pebrevkode().notEqualTo("PE_UT_04_108") and
                pe.pebrevkode().notEqualTo("PE_UT_04_109") and
                pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0)

        showIf(skalViseReduksjon) {
            title1 {
                text (
                    bokmal { + "Slik beregner vi reduksjonen av uføretrygden" },
                    nynorsk { + "Slik bereknar vi reduksjonen av uføretrygda" },
                )
            }

            paragraph {
                text (
                    bokmal { + pe.functions.pe_ut_overskytende.format(false) + " kr" },
                    nynorsk { + pe.functions.pe_ut_overskytende.format(false) + " kr" },
                )
                text (
                    bokmal { + " x " },
                    nynorsk { + " x " },
                )
                text (
                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " %" },
                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " %" },
                )
                text (
                    bokmal { + " = " },
                    nynorsk { + " = " },
                )
                text (
                    bokmal { + pe.functions.pe_ut_opplyningerombergeningen_nettoperar.format() },
                    nynorsk { + pe.functions.pe_ut_opplyningerombergeningen_nettoperar.format() },
                )
                text (
                    bokmal { + " i reduksjon for året" },
                    nynorsk { + " i reduksjon for året" },
                )
            }
        }
    }

}
