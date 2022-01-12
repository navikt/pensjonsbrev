package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.maler.EksempelBrev
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.template.LetterTemplate

object TemplateResource {
    private val templates: Map<String, LetterTemplate<*, *>> = setOf(
        EksempelBrev,
        OmsorgEgenAuto,
    ).associate { it.template.name to it.template }

    fun getTemplates(): Set<String> =
        templates.keys

    fun getTemplate(name: String): LetterTemplate<*, *>? =
        templates[name]

}