package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate
object KombinereUfoeretrygdMedInntekt {

    // TBU1201
    data class KombinereUfoeretrygdOgInntektOverskrift(
        val ufoeregrad: Expression<Int>,
        val utbetalingsgrad: Expression<Int>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val h = ufoeregrad.equalTo(100)
            val har100ProsentUtbetalingsgrad = utbetalingsgrad.equalTo(100)
            title1 {
           //     showIf(har100ProsentUfoeregrad and har100ProsentUtbetalingsgrad)
                textExpr(
                    Bokmal to "Skal du kombinere uføretrygd og inntekt?".expr(),
                    Nynorsk to "Skal du kombinere uføretrygd og inntekt?".expr(),
                    English to "Will you combine disability benefit with income?".expr()
                )
            }
        }
    }
}