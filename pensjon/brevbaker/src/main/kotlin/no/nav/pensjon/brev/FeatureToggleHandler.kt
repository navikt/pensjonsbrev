package no.nav.pensjon.brev

import io.getunleash.DefaultUnleash
import io.getunleash.FakeUnleash
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import io.getunleash.util.UnleashConfig
import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import org.slf4j.LoggerFactory
import kotlin.properties.Delegates

const val unleashTogglePrefix = "pensjonsbrev.brevbaker."

object FeatureToggleHandler : FeatureToggleService {
    private lateinit var unleash: Unleash
    private var isFakeUnleash by Delegates.notNull<Boolean>()
    private val logger = LoggerFactory.getLogger(FeatureToggleHandler::class.java)

    override fun isEnabled(toggle: FeatureToggle): Boolean =
        unleash.isEnabled(unleashTogglePrefix + toggle.key(), UnleashContext.builder().build())

    fun configure(block: FeatureToggleConfig.() -> Unit) {
        unleash = createUnleash(FeatureToggleConfig().apply(block))
        FeatureToggleSingleton.init(this)
    }

    private fun createUnleash(config: FeatureToggleConfig): Unleash = with(config) {
        if (useFakeUnleash) {
            isFakeUnleash = true
            FakeUnleash().apply {
                if (fakeUnleashEnableAll) enableAll() else disableAll()
            }
        } else {
            isFakeUnleash = false
            DefaultUnleash(
                UnleashConfig.builder()
                    .appName(appName!!)
                    .environment(environment!!)
                    .unleashAPI("${host!!}/api")
                    .apiKey(apiToken!!).build()
            )
        }
    }

    fun shutdown() = unleash.shutdown()

    override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) {
        if (isFakeUnleash) {
            return
        }
        val alleDefinerteBrytere = unleash.more().featureToggleNames
        val malerIkkeIUnleash = entries.filterNot {
            alleDefinerteBrytere.contains("pensjonsbrev.brevbaker.${it.name}")
        }
        if (malerIkkeIUnleash.isNotEmpty()) {
            logger.error("Alle toggles må være definert i Unleash, men $malerIkkeIUnleash")
        }
    }
}

class FeatureToggleConfig {
    var useFakeUnleash: Boolean = false
    var fakeUnleashEnableAll: Boolean = false

    var appName: String? = null
    var environment: String? = null
    var host: String? = null
    var apiToken: String? = null
}

