package brev.sivilstand.fraser

import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
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
                AlderspensjonRegelverkType.AP2025,
            ),
        ) {
            showIf(beloepEndring.equalTo(BeloepEndring.UENDRET)) {
                paragraph {
                    text(
                        bokmal { + "Dette får ingen betydning for utbetalingen din." },
                        nynorsk { + "Dette får ingen følgjer for utbetalinga di." },
                        english { + "This does not affect the amount you will receive." },
                    )
                }
            }
            showIf(beloepEndring.equalTo(BeloepEndring.ENDR_OKT)) {
                paragraph {
                    text(
                        bokmal { + "Dette fører til at pensjonen din øker." },
                        nynorsk { + "Dette fører til at pensjonen din aukar." },
                        english { + "This leads to an increase in your retirement pension." },
                    )
                }
            }
            showIf(beloepEndring.equalTo(BeloepEndring.ENDR_RED)) {
                paragraph {
                    text(
                        bokmal { + "Dette fører til at pensjonen din blir redusert." },
                        nynorsk { + "Dette fører til at pensjonen din blir redusert." },
                        english { + "This leads to a reduction in your retirement pension." },
                    )
                }
            }
        }
    }
}
