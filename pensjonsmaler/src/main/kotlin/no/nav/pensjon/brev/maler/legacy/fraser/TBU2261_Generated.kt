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
            text(
                bokmal { + "Ut fra den årlige inntekten din vil uføretrygden utgjøre " + pe.ut_nettoakk_pluss_nettorestar()
                    .format() + "." },
                nynorsk { + "På bakgrunn av den innmelde inntekta di utgjer uføretrygda di " + pe.ut_nettoakk_pluss_nettorestar()
                    .format() + "." },
                english { + "On the basis of your reported income, your disability benefit will be total " + pe.ut_nettoakk_pluss_nettorestar()
                    .format() + "." },
            )

            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
            showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_virkningfom())))) {
                text(
                    bokmal { + " Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                        .format() + "." },
                    nynorsk { + " Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
                        .format() + "." },
                    english { + " So far this year, you have been paid " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + "." }
                )
            }
            text(
                bokmal { + " Du har derfor rett til en utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto()
                    .format() + " per måned for resten av året." },
                nynorsk { + " Du har derfor rett til ei utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto()
                    .format() + " per månad for resten av kalenderåret." },
                english { + " Therefore, you are entitled to a disability benefit payment of " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto()
                    .format() + " per month for the remainder of the calendar year." }
            )
        }

    }
}
        