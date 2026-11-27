package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text


data class TBU2280_Generated(
	val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        val harBarnetillegg =
            pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or
                pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()

        paragraph {
            text(
                bokmal {
                    +"Vi vil foreta et etteroppgjør hvis du har fått utbetalt for mye eller for lite uføretrygd " +
                        ifElse(harBarnetillegg, "og barnetillegg ", "") +
                        "i løpet av året. Dette gjør vi når likningen fra Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd" +
                        ifElse(harBarnetillegg, " og barnetillegg", "") +
                        ", vil vi utbetale dette beløpet til deg. Hvis du har fått utbetalt for mye i uføretrygd" +
                        ifElse(harBarnetillegg, " og barnetillegg", "") +
                        ", må du betale dette tilbake."
                },
                nynorsk {
                    +"Vi gjer eit etteroppgjer dersom du har fått utbetalt for mykje eller for lite uføretrygd " +
                        ifElse(harBarnetillegg, "og barnetillegg ", "") +
                        "i løpet av året. Dette gjer vi når likninga frå Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd" +
                        ifElse(harBarnetillegg, " og barnetillegg", "") +
                        ", vil vi betale deg dette beløpet. Dersom du har fått utbetalt for mykje i uføretrygd" +
                        ifElse(harBarnetillegg, " og barnetillegg", "") +
                        ", må du betale dette tilbake."
                },
            )
        }
    }
}
        