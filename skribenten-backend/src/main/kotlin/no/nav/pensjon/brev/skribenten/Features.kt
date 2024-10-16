package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import io.getunleash.util.UnleashConfig
import io.ktor.server.application.*

private const val unleashTogglePrefix = "pensjonsbrev.skribenten."

data class UnleashToggle(val name: String) {
    fun isEnabled(call: ApplicationCall) = Features.isEnabled(this, call)
}

object Features {
    val brevbakerbrev = UnleashToggle("brevbakerbrev")
    val brevutendata = UnleashToggle("brevutendata")

    private var unleash: Unleash? = null
    private val overrides = mutableMapOf<String, Boolean>()

    fun override(key: UnleashToggle, value: Boolean) {
        overrides[key.name] = value
    }

    fun initUnleash(config: Config) {
        unleash = DefaultUnleash(
            UnleashConfig.builder()
                .appName(config.getString("appName"))
                .environment(config.getString("environment"))
                .unleashAPI(config.getString("host") + "/api")
                .apiKey(config.getString("apiToken")).build()
        )
    }

    fun isEnabled(toggle: UnleashToggle, call: ApplicationCall? = null): Boolean =
        overrides[toggle.name]
            ?: unleash?.isEnabled(unleashTogglePrefix + toggle.name, context(call))
            ?: false

    private fun context(call: ApplicationCall?): UnleashContext =
        call?.let { UnleashContext.builder().userId(it.principal().navIdent).build() } ?: UnleashContext.builder().build()
}
