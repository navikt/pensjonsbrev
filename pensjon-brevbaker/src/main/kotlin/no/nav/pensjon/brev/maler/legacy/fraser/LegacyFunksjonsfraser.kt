@file:Suppress("ClassName")

package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThanOrEqual
import no.nav.pensjon.brev.template.dsl.text

// Fraser som var funksjoner i Exstream
object LegacyFunksjonsfraser {
    data class PE_UT_fradrag_hoyere_lavere(val pe: Expression<PE>) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(
                pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag()
                    .greaterThan(0)
            ) {
                text(
                    Bokmal to "høyere",
                    Nynorsk to "høgare",
                    English to "higher"
                )
            }.orShowIf(
                pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag()
                    .equalTo(0)
            ) {
                text(
                    Bokmal to "lavere",
                    Nynorsk to "lågare",
                    English to "lower"
                )
            }
        }

        /*
        IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop)
THEN
    IF UCase(PE_XML_brev_spraak) ='NN' THEN value = "høgare"
    ELSEIF UCase(PE_XML_brev_spraak) ='EN'  THEN value = "higher"
    ELSE  value = "høyere"
    ENDIF

ELSEIF (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop)
THEN
    IF UCase(PE_XML_brev_spraak) ='NN' THEN value = "lågare"
    ELSEIF UCase(PE_XML_brev_spraak) ='EN' THEN value = "lower"
    ELSE  value = "lavere"
    ENDIF

ENDIF
         */


    }

    data class PE_UT_inntekt_hoyere_lavere(val pe: Expression<PE>) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(
                pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                    .greaterThan(
                        pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                    )
            ) {
                text(
                    Bokmal to "høyere",
                    Nynorsk to "høgare",
                    English to "higher"
                )
            }.orShowIf(
                pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                    .lessThanOrEqual(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())
            ) {
                text(
                    Bokmal to "lavere",
                    Nynorsk to "lågare",
                    English to "lower"
                )
            }
        }

    }

    data class PE_UT_ikke(val pe: Expression<PE>) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto())) {
                text(
                    Bokmal to "ikke ",
                    Nynorsk to "ikkje ",
                    English to "not "
                )
            }
        }

    }
}
