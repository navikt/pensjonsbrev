package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable

fun Edit.Letter.variablesValueMap(): Map<Int, String> =
    variables.mapNotNull { it.id?.let { id -> id to it.text } }.toMap()

private val Edit.Letter.variables: List<Variable>
    get() = object : EditLetterVisitor<Variable>(this) {
        override fun visit(content: Variable) = emit(content)
    }.build()