package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.BrevTemplate

interface TemplateLibrary<Kode : Brevkode<Kode>, out Data : BrevbakerBrevdata, out T : BrevTemplate<Data, Kode>> {
    fun listTemplatesWithMetadata(): List<TemplateDescription>
    fun listTemplatekeys(): Set<String>
    fun getTemplate(kode: Kode): BrevTemplate<Data, out Brevkode<*>>?
}

class TemplateLibraryImpl<Kode : Brevkode<Kode>, out Data : BrevbakerBrevdata, out T : BrevTemplate<Data, Kode>>(
    templates: Set<T>,
) : TemplateLibrary<Kode, Data, T> {
    private val templates: Map<String, T> = templates.associateBy { it.kode.kode() }

    override fun listTemplatesWithMetadata() = templates.mapNotNull { getTemplate(it.key) }.map { it.description() }

    override fun listTemplatekeys() = templates.filter { getTemplate(it.key) != null }.keys

    override fun getTemplate(kode: Kode): BrevTemplate<Data, out Brevkode<*>>? = getTemplate(kode.kode())

    private fun getTemplate(kode: String) =
        templates[kode]?.takeIf { it.featureToggle?.let { toggle -> FeatureToggleSingleton.isEnabled(toggle) } ?: true }
}