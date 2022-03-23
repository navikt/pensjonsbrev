package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.maler.UngUfoerAuto
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.StaticTemplate

private val productionTemplates = setOf(
    OmsorgEgenAuto,
    UngUfoerAuto,
)

class TemplateResource(templates: Set<StaticTemplate> = productionTemplates) {
    private var templateMap: Map<String, LetterTemplate<*, *>> =
        templates.associate { it.template.name to it.template }

    fun getTemplates(): Set<String> =
        templateMap.keys

    fun getTemplate(name: String): LetterTemplate<*, *>? =
        templateMap[name]
}