package no.nav.pensjon.brev.skribenten.letter

class VariableValuesVisitor(val letter: Edit.Letter) : EditedLetterVisitor() {
    private val variableValues: MutableMap<Int, String> = mutableMapOf()

    fun build(): Map<Int, String> {
        visit(letter)
        return variableValues
    }

    override fun visit(content: Edit.ParagraphContent.Text.Variable) {
        super.visit(content)
        if (content.id != null) {
            variableValues[content.id] = content.text
        }
    }
}