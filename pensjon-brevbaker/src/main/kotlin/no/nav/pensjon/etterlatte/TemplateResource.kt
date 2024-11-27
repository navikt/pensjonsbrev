package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.etterlatte.EtterlatteMaler.prodAutobrevTemplates

class TemplateResource(
    autobrevTemplates: Set<EtterlatteTemplate<*>> = prodAutobrevTemplates,
) {
    private val autoBrevMap: Map<EtterlatteBrevKode, EtterlatteTemplate<*>> =
        autobrevTemplates.associateBy { it.kode }

    fun getAutoBrev(kode: EtterlatteBrevKode): LetterTemplate<*, *>? = autoBrevMap[kode]?.template

}
