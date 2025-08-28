package no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text

data class BetydningForUtbetaling(
    val regelverkType: Expression<AlderspensjonRegelverkType>,
    val beloepEndring: Expression<BeloepEndring>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            regelverkType.isOneOf(
                AlderspensjonRegelverkType.AP2011,
                AlderspensjonRegelverkType.AP2016,
            ),
        ) {
            showIf(beloepEndring.equalTo(BeloepEndring.UENDRET)) {
                paragraph {
                    text(
                        Language.Bokmal to "Dette får ingen betydning for utbetalingen din.",
                        Language.Nynorsk to "Dette får ingen følgjer for utbetalinga di.",
                        Language.English to "This does not affect the amount you will receive.",
                    )
                }
            }
            showIf(beloepEndring.equalTo(BeloepEndring.ENDR_OKT)) {
                paragraph {
                    text(
                        Language.Bokmal to "Dette fører til at pensjonen din øker.",
                        Language.Nynorsk to "Dette fører til at pensjonen din aukar.",
                        Language.English to "This leads to an increase in your retirement pension.",
                    )
                }
            }
            showIf(beloepEndring.equalTo(BeloepEndring.ENDR_RED)) {
                paragraph {
                    text(
                        Language.Bokmal to "Dette fører til at pensjonen din blir redusert.",
                        Language.Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                        Language.English to "This leads to a reduction in your retirement pension.",
                    )
                }
            }
        }
    }
}
