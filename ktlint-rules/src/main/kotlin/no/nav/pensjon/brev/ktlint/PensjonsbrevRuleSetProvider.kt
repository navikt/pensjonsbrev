package no.nav.pensjon.brev.ktlint

import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.rule.engine.core.api.RuleSetId

internal const val PENSJONSBREV_RULE_SET_ID = "pensjonsbrev"

/**
 * Registers custom ktlint rules for this repository. Discovered by ktlint via
 * `META-INF/services/com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3`.
 */
class PensjonsbrevRuleSetProvider : RuleSetProviderV3(RuleSetId(PENSJONSBREV_RULE_SET_ID)) {
    override fun getRuleProviders(): Set<RuleProvider> =
        setOf(
            RuleProvider { NoNonDeterministicExpressionLiteralRule() },
        )
}
