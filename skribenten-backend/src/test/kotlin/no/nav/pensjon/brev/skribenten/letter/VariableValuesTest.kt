package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.*
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.ItemList.Item
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.*
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VariableValuesTest {

    class VariableCreator {
        private var count = 0
        private val _variables = mutableMapOf<Int, String>()
        val variables: Map<Int, String> get() = _variables

        fun create(): Text.Variable {
            val id = count++
            _variables[id] = "var-$id"
            return Text.Variable(id, "var-$id")
        }
    }

    @Test
    fun `henter variabel verdier`() {
        val vars = VariableCreator()
        val letter = editedLetter(
            Paragraph(
                null,
                true,
                listOf(
                    Literal(null, "lit1"),
                    vars.create(),
                    vars.create(),
                    ItemList(
                        null,
                        listOf(
                            Item(null, listOf(vars.create())),
                            Item(null, listOf(vars.create(), vars.create())),
                        ),
                    ),
                    Table(
                        null,
                        listOf(
                            Row(null, listOf(Cell(null, listOf(vars.create(), vars.create())), Cell(null, listOf(vars.create())))),
                            Row(null, listOf(Cell(null, listOf(vars.create(), Literal(null, "lit2"))))),
                        ),
                        Header(
                            null,
                            listOf(
                                ColumnSpec(null, Cell(null, listOf(vars.create())), ColumnAlignment.LEFT, 1),
                                ColumnSpec(null, Cell(null, listOf(vars.create())), ColumnAlignment.LEFT, 1),
                            ),
                        ),
                    )
                )
            ),
            Edit.Block.Title1(null, true, listOf(Literal(null, "lit3"), vars.create(), vars.create())),
            Edit.Block.Title2(null, true, listOf(Literal(null, "lit3"), vars.create(), vars.create())),
        )

        assertEquals(vars.variables, letter.variablesValueMap())
    }
}