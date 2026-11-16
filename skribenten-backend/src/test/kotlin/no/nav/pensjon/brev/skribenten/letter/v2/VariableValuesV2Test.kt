package no.nav.pensjon.brev.skribenten.letter.v2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VariableValuesV2Test {

    @Test
    fun `henter variabel verdier`() {
        val letter = editedLetter {
            paragraph {
                literal(text = "lit1")
                variable(id = 0, text = "var-0")
                variable(id = 1, text = "var-1")
            }
            itemList {
                item { variable(id = 2, text = "var-2") }
                item {
                    variable(id = 3, text = "var-3")
                    variable(id = 4, text = "var-4")
                }
            }
            table {
                header {
                    colSpec { variable(id = 9, text = "var-9") }
                    colSpec { variable(id = 10, text = "var-10") }
                }
                row {
                    cell {
                        variable(id = 5, text = "var-5")
                        variable(id = 6, text = "var-6")
                    }
                    cell { variable(id = 7, text = "var-7") }
                }
                row {
                    cell {
                        variable(id = 8, text = "var-8")
                        literal(text = "lit2")
                    }
                }
            }
            title2 { literal(text = "lit3"); variable(id = 11, text = "var-11"); variable(id = 12, text = "var-12") }
            title3 { literal(text = "lit3"); variable(id = 13, text = "var-13"); variable(id = 14, text = "var-14") }
            title4 { literal(text = "lit3"); variable(id = 15, text = "var-15"); variable(id = 16, text = "var-16") }
            numberedList {
                item { variable(id = 17, text = "var-17") }
            }
        }

        val expectedVars = (0..17).associate { it to "var-$it" }
        assertEquals(expectedVars, letter.variablesValueMap())
    }
}
