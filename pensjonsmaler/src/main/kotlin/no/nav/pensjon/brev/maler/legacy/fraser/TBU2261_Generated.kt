package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear
import no.nav.pensjon.brev.maler.legacy.ut_nettoakk_pluss_nettorestar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_virkningfom
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU2261_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() +
                    pe.ut_nettoakk_pluss_nettorestar()
                        .format() + " kroner.",
                Nynorsk to "På bakgrunn av den innmelde inntekta di utgjer uføretrygda di ".expr() +
                    pe.ut_nettoakk_pluss_nettorestar()
                        .format() + " kroner.",
                English to "On the basis of your reported income, your disability benefit will be total NOK ".expr() +
                    pe.ut_nettoakk_pluss_nettorestar()
                        .format() + ".",
            )

            // IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
            showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_virkningfom())))) {
                textExpr(
                    Bokmal to " Hittil i år har du fått utbetalt ".expr() +
                        pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                            .format() + " kroner.",
                    Nynorsk to " Hittil i år har du fått utbetalt ".expr() +
                        pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                            .format() + " kroner.",
                    English to " So far this year, you have been paid NOK ".expr() +
                        pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                            .format() + ".",
                )
            }
            textExpr(
                Bokmal to " Du har derfor rett til en utbetaling av uføretrygd på ".expr() +
                    pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto()
                        .format() + " kroner per måned for resten av året.",
                Nynorsk to " Du har derfor rett til ei utbetaling av uføretrygd på ".expr() +
                    pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto()
                        .format() + " kroner per månad for resten av kalenderåret.",
                English to " Therefore, you are entitled to a disability benefit payment of ".expr() +
                    pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto()
                        .format() + " per month for the remainder of the calendar year.",
            )
        }
    }
}
