package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.something.ExperimentTemplates
import no.nav.pensjon.brev.template.LetterTemplate

object TemplateResource {

    fun getTemplates(): List<String> =
        listOf(ExperimentTemplates.eksempelBrev.name, ExperimentTemplates.alderspensjon.name)

    fun getTemplate(name: String): LetterTemplate? =
        when(name){
            ExperimentTemplates.eksempelBrev.name -> ExperimentTemplates.eksempelBrev
            ExperimentTemplates.alderspensjon.name -> ExperimentTemplates.alderspensjon
            else -> null
        }
}