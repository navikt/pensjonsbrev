package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.exstreamFunctions.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.*
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.skalViseUtbetalingVedInntektsendringTittel
import no.nav.pensjon.brev.maler.legacy.vedlegg.skalViseUtbetalingVedInntektsendringDetaljer
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(pe.skalViseUtbetalingVedInntektsendringTittel()){

            title1 {
                text (
                    bokmal { + "Slik beregner vi utbetaling av uføretrygden når inntekten din endres" },
                    nynorsk { + "Slik bereknar vi utbetaling av uføretrygda når inntekta di er endra" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Utbetalingen av uføretrygden din er beregnet på nytt, fordi inntekten din er endret. Det er den innmeldte inntekten din og uføretrygden du har fått utbetalt hittil i år, som avgjør hvor mye du får utbetalt i de månedene som er igjen i kalenderåret." },
                    nynorsk { + "Utbetalinga av uføretrygda di er berekna på nytt fordi inntekta di er endra. Det er den innmelde inntekta di og uføretrygda du har fått utbetalt hittil i år, som avgjer kor mykje du får utbetalt i dei månadene som er att av kalenderåret." },
                )
            }
        }

        showIf(pe.skalViseUtbetalingVedInntektsendringDetaljer()){
            paragraph {
                text (
                    bokmal { + "Uføretrygden reduseres med " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " prosent av inntekten over " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()
                        .format() + " fordi du har en reduksjonsprosent som er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " prosent." },
                    nynorsk { + "Uføretrygda blir redusert med " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " prosent av inntekta over " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()
                        .format() + " fordi du har ein reduksjonsprosent som er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .format() + " prosent." },
                )
            }
        }

        showIf(
            pe.skalViseUtbetalingVedInntektsendringDetaljer()
        ){
            paragraph {
                text (
                    bokmal { + "Du har et bunnfradrag på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()
                        .format() + " og den innmeldte inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                        .format() + ". Dette betyr at overskytende inntekt er " + pe.functions.pe_ut_overskytende.format() + "." },
                    nynorsk { + "Du har eit botnfrådrag på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()
                        .format() + ", og den innmelde inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                        .format() + ". Dette vil seie at overskytande inntekt er " + pe.functions.pe_ut_overskytende.format() + "." },
                )
            }
        }
    }
}
