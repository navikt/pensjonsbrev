package no.nav.pensjon.brev.template

interface StaticTemplate {
    val template: LetterTemplate<*, *>
}