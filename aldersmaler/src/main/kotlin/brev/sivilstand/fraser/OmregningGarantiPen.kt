package brev.sivilstand.fraser

import no.nav.pensjon.brev.api.model.maler.AlderspensjonRegelverkType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text

data class OmregningGarantiPen(
    val regelverkType: Expression<AlderspensjonRegelverkType>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
            // omregning_GarantiPen
            paragraph {
                text(
                    bokmal { + "Derfor har vi vurdert garantipensjonen din på nytt." },
                    nynorsk { + "Derfor har vi vurdert garantipensjonen din på nytt." },
                    english { + "We have therefore recalculated your guaranteed pension." },
                )
            }
        }
    }
}
