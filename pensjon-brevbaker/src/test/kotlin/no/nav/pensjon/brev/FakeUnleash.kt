package no.nav.pensjon.brev

import io.getunleash.MoreOperations
import io.getunleash.Unleash
import io.getunleash.UnleashContext
import io.getunleash.Variant
import java.util.function.BiPredicate

class FakeUnleash(private val overrides: MutableMap<String, Boolean>) : Unleash {
    override fun isEnabled(
        toggleName: String,
        context: UnleashContext,
        fallbackAction: BiPredicate<String, UnleashContext>,
    ) = overrides[toggleName]
        ?: false.also { println("Ingen override for brytar $toggleName. Returnerer false som standard") }

    override fun getVariant(p0: String, p1: UnleashContext): Variant {
        TODO("Not yet implemented")
    }

    override fun getVariant(p0: String, p1: UnleashContext, p2: Variant): Variant {
        TODO("Not yet implemented")
    }

    override fun deprecatedGetVariant(p0: String, p1: UnleashContext): Variant {
        TODO("Not yet implemented")
    }

    override fun deprecatedGetVariant(p0: String, p1: UnleashContext, p2: Variant): Variant {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getFeatureToggleNames(): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun more(): MoreOperations {
        TODO("Not yet implemented")
    }
}