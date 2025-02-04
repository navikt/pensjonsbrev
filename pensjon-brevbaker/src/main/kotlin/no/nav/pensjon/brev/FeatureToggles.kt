package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.ToggleName

data class UnleashToggle(val name: String) : FeatureToggle, StableHash by StableHash.of("Toggle: $name") {
    override fun isEnabled() = FeatureToggleServiceImpl.isEnabled(this)
    override fun key() = name
}

object FeatureToggleServiceImpl : FeatureToggleService {
    override fun isEnabled(toggle: ToggleName) = FeatureToggleHandler.isEnabled(toggle)
}

// problemstilling: få flytta denne over i pensjonsmaler
/*
utfordringa er primært å få isEnabled inn på eit bra vis
i malen der desse blir brukt gjerast jo togglenamn.isEnabled()
og det krev jo ei hardkoding opp mot unleash

rett fram-måten er sjølvsagt å krevje unleash som avhengnad til brevbaker-biblioteket
eventuelt som ein slags plugin


trur det er betre da med interface + singleton


 */
object FeatureToggles {
    // Sett inn featuretoggles her
    // val minFeature = UnleashToggle("minFeature")
    val pl7231ForventetSvartid = UnleashToggle("pl_7231.foreventet_svartid")
    val pl7822EndretInntekt = UnleashToggle("pl_7822.endringer_ut_endret_pga_inntekt")
}

enum class FeaToggle(private val noekkel: String) : FeatureToggle, StableHash by StableHash.of("Toggle: $noekkel") {
    pl7231ForventetSvartid("pl_7231.foreventet_svartid"),
    pl7822EndretInntekt("pl_7822.endringer_ut_endret_pga_inntekt");

    override fun key() = noekkel

    override fun isEnabled() = UnleashToggle(noekkel).isEnabled()
}