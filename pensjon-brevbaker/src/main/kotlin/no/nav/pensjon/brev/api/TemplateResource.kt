package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.template.LetterTemplate

object TemplateResource {
    private val templates: Map<String, LetterTemplate<*, *>> = setOf(
        OmsorgEgenAuto,
        UngUfoerAuto,
    ).associate { it.template.name to it.template }

    fun getTemplates(): Set<String> =
        templates.keys

    fun getTemplate(name: String): LetterTemplate<*, *>? =
        templates[name]

}