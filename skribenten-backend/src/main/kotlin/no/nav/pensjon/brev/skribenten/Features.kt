package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import io.getunleash.util.UnleashConfig
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext

private const val UNLEASH_TOGGLE_PREFIX = "pensjonsbrev.skribenten."

data class UnleashToggle(val name: String) {
    suspend fun isEnabled() = Features.isEnabled(this)
}

object Features {
    val brevbakerbrev = UnleashToggle("brevbakerbrev")
    val attestant = UnleashToggle("attestant")

    private var unleash: Unleash? = null
    private val overrides = mutableMapOf<String, Boolean>()

    fun override(
        key: UnleashToggle,
        value: Boolean,
    ) {
        overrides[key.name] = value
    }

    fun initUnleash(config: Config) {
        unleash =
            DefaultUnleash(
                UnleashConfig.builder()
                    .appName(config.getString("appName"))
                    .environment(config.getString("environment"))
                    .unleashAPI(config.getString("host") + "/api")
                    .apiKey(config.getString("apiToken")).build(),
            )
    }

    suspend fun isEnabled(toggle: UnleashToggle): Boolean =
        overrides[toggle.name]
            ?: unleash?.isEnabled(UNLEASH_TOGGLE_PREFIX + toggle.name, context())
            ?: false

    private suspend fun context(): UnleashContext =
        PrincipalInContext.get()
            ?.let { UnleashContext.builder().userId(it.navIdent.id).build() }
            ?: UnleashContext.builder().build()

    fun shutdown() = unleash?.shutdown()
}
