package no.nav.pensjon.brev.template.dsl.helpers

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class TemplateModelHelpers(@Suppress("unused") val additionalModels: Array<KClass<*>> = [])
