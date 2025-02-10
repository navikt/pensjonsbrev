package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.FeatureToggleHandler
import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmSaksbehandlingstidV2
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAutoV2
import no.nav.pensjon.brev.template.BrevTemplate

class TemplateLibrary<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(templates: Set<T>) {
    private val templates: Map<String, T> = templates.associateBy { it.kode.kode() }

    fun listTemplatesWithMetadata() = templates.mapNotNull { getTemplate(it.key) }.map { it.description() }

    fun listTemplatekeys() = templates.filter { getTemplate(it.key) != null }.keys

    fun getTemplate(kode: Kode) = getTemplate(kode.kode())

    private fun getTemplate(kode: String) = when {
        // Legg inn her hvis du ønsker å styre forskjellige versjoner, feks
        // kode == DinBrevmal.kode && FeatureToggles.dinToggle.isEnabled() -> DinBrevmalV2
        kode == Pesysbrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID.kode() && FeatureToggleSingleton.isEnabled(FeatureToggles.pl7231ForventetSvartid) -> OrienteringOmSaksbehandlingstidV2
        kode == Pesysbrevkoder.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO.kode() && FeatureToggleSingleton.isEnabled(FeatureToggles.pl7231ForventetSvartid) -> VarselSaksbehandlingstidAutoV2
        else -> templates[kode]?.takeIf { it.kode.toggle()?.let { FeatureToggleHandler.isEnabled(it) } ?: true }
    }
}