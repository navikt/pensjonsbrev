package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.hentMuligOverstyrtMal
import no.nav.pensjon.brev.template.BrevTemplate

class TemplateLibrary<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(templates: Set<T>) {
    private val templates: Map<String, T> = templates.associateBy { it.kode.kode() }

    fun listTemplatesWithMetadata() = templates.mapNotNull { getTemplate(it.key) }.map { it.description() }

    fun listTemplatekeys() = templates.filter { getTemplate(it.key) != null }.keys

    fun getTemplate(kode: Kode) = getTemplate(kode.kode())

    private fun getTemplate(kode: String) = hentMuligOverstyrtMal(kode) ?: templates[kode]
        ?.takeIf { it.featureToggle?.let { toggle -> FeatureToggleSingleton.isEnabled(toggle) } ?: true }
}