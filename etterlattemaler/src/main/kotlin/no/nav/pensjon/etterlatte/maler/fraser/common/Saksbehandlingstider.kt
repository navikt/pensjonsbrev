package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language

fun saksbehandlingstiderUrl(sakType: Expression<SakType>): Expression<String> =
    Expression.BinaryInvoke(
        first = sakType,
        second = Expression.FromScope.Language,
        operation = Saksbehandlingstider,
    )

object Saksbehandlingstider : BinaryOperation<SakType, Language, String>() {
    override fun apply(
        first: SakType,
        second: Language,
    ): String {
        return if (first == SakType.BARNEPENSJON) {
            when (second) {
                is Language.English -> Constants.Engelsk.SAKSBEHANDLINGSTIDER_BP
                else -> Constants.SAKSBEHANDLINGSTIDER_BP
            }
        } else {
            when (second) {
                is Language.English -> Constants.Engelsk.SAKSBEHANDLINGSTIDER_OMS
                else -> Constants.SAKSBEHANDLINGSTIDER_OMS
            }
        }
    }

    override fun stableHashCode(): Int {
        return "BinaryOperation.Saksbehandlingstider".hashCode()
    }
}
