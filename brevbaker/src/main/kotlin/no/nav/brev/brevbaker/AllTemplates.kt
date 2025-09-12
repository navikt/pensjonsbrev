package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

interface AllTemplates {
    fun hentAutobrevmaler(): Set<AutoMal<*>>
    fun hentRedigerbareMaler(): Set<RedigerbarMal<RedigerbarBrevdata<*, *>>>
}

sealed class Mal<out LetterData: BrevbakerBrevdata, Kode: Brevkode<Kode>> {
    abstract val template: BrevTemplate<LetterData, *>
    abstract val featureToggle: FeatureToggle?
}

data class RedigerbarMal<out T : RedigerbarBrevdata<*, *>>(
    override val template: RedigerbarTemplate<out T>,
    override val featureToggle: FeatureToggle? = null
) : Mal<T, Brevkode.Redigerbart>()

data class AutoMal<out T : BrevbakerBrevdata>(
    override val template: AutobrevTemplate<T>,
    override val featureToggle: FeatureToggle? = null
) : Mal<T, Brevkode.Automatisk>()