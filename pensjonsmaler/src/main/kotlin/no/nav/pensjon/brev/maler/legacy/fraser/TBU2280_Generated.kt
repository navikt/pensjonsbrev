package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class TBU2280_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        // [TBU2280NN, TBU2280, TBU2280EN]

        paragraph {
            text(
                Bokmal to "Vi vil foreta et etteroppgjør hvis du har fått utbetalt for mye eller for lite uføretrygd ",
                Nynorsk to "Vi gjer eit etteroppgjer dersom du har fått utbetalt for mykje eller for lite uføretrygd ",
            )

            // IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    Bokmal to "og barnetillegg ",
                    Nynorsk to "og barnetillegg ",
                )
            }
            text(
                Bokmal to "i løpet av året. Dette gjør vi når likningen fra Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd",
                Nynorsk to "i løpet av året. Dette gjer vi når likninga frå Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd",
            )

            // IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                )
            }
            text(
                Bokmal to ", vil vi utbetale dette beløpet til deg. Hvis du har fått utbetalt for mye i uføretrygd",
                Nynorsk to ", vil vi betale deg dette beløpet. Dersom du har fått utbetalt for mykje i uføretrygd",
            )

            // IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                )
            }
            text(
                Bokmal to ", må du betale dette tilbake.",
                Nynorsk to ", må du betale dette tilbake.",
            )
        }
    }
}
