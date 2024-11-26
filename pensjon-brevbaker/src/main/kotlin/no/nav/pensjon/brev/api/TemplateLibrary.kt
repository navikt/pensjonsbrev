package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.redigerbar.OrienteringOmSaksbehandlingstidV2
import no.nav.pensjon.brev.maler.ufoereBrev.VarselSaksbehandlingstidAutoV2
import no.nav.pensjon.brev.template.BrevTemplate

class TemplateLibrary<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(templates: Set<T>) {
    private val templates: Map<String, T> = templates.associateBy { it.kode.kode() }

    fun listTemplatesWithMetadata() = templates.map { getTemplate(it.key)!!.description() }

    fun listTemplatekeys() = templates.keys

    fun getTemplate(kode: Kode) = getTemplate(kode.kode())

    fun getTemplate(kode: String) = when {
        // Legg inn her hvis du ønsker å styre forskjellige versjoner, feks
        // kode == DinBrevmal.kode && FeatureToggles.dinToggle.isEnabled() -> DinBrevmalV2
        kode == Brevkode.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID.kode() && FeatureToggles.pl7231ForventetSvartid.isEnabled() -> OrienteringOmSaksbehandlingstidV2
        kode == Brevkode.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO.kode() && FeatureToggles.pl7231ForventetSvartid.isEnabled() -> VarselSaksbehandlingstidAutoV2
        else -> templates[kode]
    }
}