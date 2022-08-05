package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.VedtaksbrevTemplate

val productionTemplates: Set<VedtaksbrevTemplate<*>> = setOf<VedtaksbrevTemplate<*>>(
    OmsorgEgenAuto,
    UngUfoerAuto,
    UfoerOmregningEnslig,
    OpptjeningVedForhoeyetHjelpesats,
)

class TemplateResource(templates: Set<VedtaksbrevTemplate<*>> = productionTemplates) {
    private var templateMap: Map<Brevkode.Vedtak, VedtaksbrevTemplate<*>> =
        templates.associateBy { it.kode }

    fun getTemplates(): Set<Brevkode.Vedtak> =
        templateMap.keys

    fun getTemplate(kode: Brevkode.Vedtak): LetterTemplate<*, *>? =
        templateMap[kode]?.template
}