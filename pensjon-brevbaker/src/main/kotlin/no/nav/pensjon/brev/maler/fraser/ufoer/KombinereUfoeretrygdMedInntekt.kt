package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd.SjekkUtbetalingene.template
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
            val harFullUfoeregrad = ufoeregrad.equalTo(100)
            val harFullUtbetalingsgrad = utbetalingsgrad.equalTo(100)
            val harStoerreUtbetalingsgrad =
            title1 {
                showIf(harFullUfoeregrad and harFullUtbetalingsgrad) {
                    textExpr(
                        Bokmal to "Skal du kombinere uføretrygd og inntekt?".expr(),
                        Nynorsk to "Skal du kombinere uføretrygd og inntekt?".expr(),
                        English to "Will you combine disability benefit with salary income?".expr()
                    )
                }.orShowIf(ufoeregrad.greaterThan(0) and not(harFullUfoeregrad)) {
                    textExpr(
                        Bokmal to "For deg som kombinerer uføretrygd og inntekt".expr(),
                        Nynorsk to "For deg som kombinerer uføretrygd og inntekt".expr(),
                        English to "If you combine disabilty benefit with salary income".expr()
                    )
                }
            }
        }
    }
}