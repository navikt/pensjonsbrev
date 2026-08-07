package no.nav.pensjon.brevbaker.api.model

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class DisplayText(val text: String)
