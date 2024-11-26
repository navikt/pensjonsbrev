package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.BrevTemplate

class TemplateLibrary<Kode : Enum<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(templates: Set<T>) {
    private val templates: Map<Kode, T> = templates.associateBy { it.kode }

    fun listTemplatesWithMetadata() = templates.map { getTemplate(it.key)!!.description() }

    fun listTemplatekeys() = templates.keys

    fun getTemplate(kode: Kode) = when {
        // Legg inn her hvis du ønsker å styre forskjellige versjoner, feks
        // kode == DinBrevmal.kode && FeatureToggles.dinToggle.isEnabled() -> DinBrevmalV2
        else -> templates[kode]
    }
}