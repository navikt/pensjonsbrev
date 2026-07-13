package no.nav.pensjon.brev.skribenten

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import io.getunleash.util.UnleashConfig
import io.ktor.utils.io.core.Closeable
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext

private const val unleashTogglePrefix = "pensjonsbrev.skribenten."

data class UnleashToggle(val name: String) {
    suspend fun isEnabled() = Features.isEnabled(this)
}

interface FeatureToggleService : Closeable {
    suspend fun isEnabled(toggle: UnleashToggle): Boolean
}

class UnleashService(config: UnleashCfg) : FeatureToggleService {
    private val unleash: Unleash = initUnleash(config)


    @Suppress("unused") // Brukes av ktor-di
    constructor(config: SkribentenConfig): this(config.unleash)

    override suspend fun isEnabled(toggle: UnleashToggle): Boolean =
        unleash.isEnabled(unleashTogglePrefix + toggle.name, context())

    override fun close() {
        unleash.shutdown()
    }

    private fun initUnleash(config: UnleashCfg): Unleash = DefaultUnleash(
        UnleashConfig.builder()
            .appName(config.appName)
            // TODO: Slett environment her når vi oppgraderer til neste unleash-sdk
            .environment(config.environment)
            .unleashAPI(config.host + "/api")
            .apiKey(config.apiToken)
            .build()
    )

    private suspend fun context(): UnleashContext =
        PrincipalInContext.get()
            ?.let { UnleashContext.builder().userId(it.navIdent.id).build() }
            ?: UnleashContext.builder().build()

}

object Features {
//    val exampleToggle = UnleashToggle("exampleToggle")
    val hindreDuplikateAvsnitt = UnleashToggle("hindreDuplikateAvsnitt")
    val vergeForExstream = UnleashToggle("vergeForExstream")
    val foersteside = UnleashToggle("foersteside")
    val pdfvedleggISkribenten = UnleashToggle("pdfvedleggISkribenten")

    var toggleService: FeatureToggleService? = null
        private set

    fun init(toggleService: FeatureToggleService) {
        this.toggleService = toggleService
    }

    suspend fun isEnabled(toggle: UnleashToggle): Boolean =
        toggleService?.isEnabled(toggle) ?: false

}
