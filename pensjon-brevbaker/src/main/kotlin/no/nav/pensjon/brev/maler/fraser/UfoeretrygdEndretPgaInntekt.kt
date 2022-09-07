package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class UfoeretrygdEndretPgaInntektTitle(
    val harEndretUfoeretrygd: Expression<Boolean>,
    val harEndretBarnetillegg: Expression<Boolean>,
    val harFlereBarnetillegg: Expression<Boolean>,
): TextOnlyPhrase<LangBokmal>(){
    override fun TextOnlyScope<LangBokmal, Unit>.template() {
        showIf(harEndretUfoeretrygd and not(harEndretBarnetillegg)) {
            text(
                Language.Bokmal to "NAV har endret utbetalingen av uføretrygden din",
            )
        }

        showIf(harEndretUfoeretrygd and harEndretBarnetillegg) {
            textExpr(
                Language.Bokmal to "NAV har endret utbetalingen av uføretrygden og ".expr()
                        + ifElse(harFlereBarnetillegg, "barnetilleggene dine", "barnetillegget ditt"),
            )
        }

        showIf(not(harEndretUfoeretrygd) and harEndretBarnetillegg) {
            textExpr(
                Language.Bokmal to "NAV har endret utbetalingen av ".expr() + ifElse(
                    harFlereBarnetillegg,
                    "barnetilleggene",
                    "barnetillegget"
                ) + " i uføretrygden din".expr()
            )
        }
    }

}