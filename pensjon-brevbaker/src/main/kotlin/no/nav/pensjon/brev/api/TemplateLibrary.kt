package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate

class TemplateLibrary<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(templates: Set<T>) {
    private val templates: Map<String, T> = templates.associateBy { it.kode.kode() }

    fun listTemplatesWithMetadata() = templates.map { getTemplate(it.key)!!.description() }

    fun listTemplatekeys() = templates.keys

    fun getTemplate(kode: Kode) = getTemplate(kode.kode())

    fun getTemplate(kode: String) = when {
        // Legg inn her hvis du ønsker å styre forskjellige versjoner, feks
        // kode == DinBrevmal.kode && FeatureToggles.dinToggle.isEnabled() -> DinBrevmalV2
        else -> templates[kode]
    }
}