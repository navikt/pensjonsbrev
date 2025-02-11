package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.StringExpression

enum class SakType {
    BARNEPENSJON,
    OMSTILLINGSSTOENAD,
}

object SaktypeFormatter : LocalizedFormatter<SakType>() {
    override fun apply(
        sakType: SakType,
        spraak: Language,
    ): String {
        return when (spraak) {
            Language.Bokmal, Language.Nynorsk ->
                when (sakType) {
                    SakType.BARNEPENSJON -> "barnepensjon"
                    SakType.OMSTILLINGSSTOENAD -> "omstillingsstønad"
                }

            Language.English ->
                when (sakType) {
                    SakType.BARNEPENSJON -> "children's pension"
                    SakType.OMSTILLINGSSTOENAD -> "adjustment allowance"
                }
        }
    }

    override fun stableHashCode(): Int = "SaktypeFormatter".hashCode()
}

fun Expression<SakType>.format(formatter: LocalizedFormatter<SakType> = SaktypeFormatter): StringExpression =
    Expression.BinaryInvoke(
        first = this,
        second = Expression.FromScope.Language,
        operation = formatter,
    )
