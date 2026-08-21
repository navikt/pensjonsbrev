package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable

fun Edit.Letter.variablesValueMap(): Map<Int, String> =
    blocks.variablesValueMap()

fun Edit.Attachment.variablesValueMap(): Map<Int, String> =
    blocks.variablesValueMap()

private fun List<Edit.Block>.variablesValueMap(): Map<Int, String> =
    variables.mapNotNull { it.id?.let { id -> id to it.text } }.toMap()

private val List<Edit.Block>.variables: List<Variable>
    get() = object : EditLetterVisitor<Variable>(this) {
        override fun visit(content: Variable) = emit(content)
    }.build()
