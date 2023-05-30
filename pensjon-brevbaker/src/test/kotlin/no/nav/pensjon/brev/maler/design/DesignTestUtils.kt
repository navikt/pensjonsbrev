package no.nav.pensjon.brev.maler.design

import no.nav.pensjon.brev.maler.example.lipsums
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object DesignTestUtils {
    object Title1 : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() =
            title1 { text(Language.Bokmal to "Tittel 1") }
    }

    object Title2 : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() =
            title2 { text(Language.Bokmal to "Tittel 2") }
    }

    object ItemList : ParagraphPhrase<LangBokmal>() {
        override fun ParagraphOnlyScope<LangBokmal, Unit>.template() {
            list {
                for (i in 1..3) {
                    item { text(Language.Bokmal to "Eksempel tekst nummer $i") }
                }
            }
        }
    }

    private const val COLUMNS = 4
    private const val ROWS = 4

    object Table : ParagraphPhrase<LangBokmal>() {
        override fun ParagraphOnlyScope<LangBokmal, Unit>.template() {
            table(header = {
                for (i in 1 .. COLUMNS) {
                    column(1) { text(Language.Bokmal to "Kolonne $i") }
                }
            }) {
                for (i in 0 until ROWS) {
                    row {
                        for (a in 1 .. COLUMNS) {
                            cell { text(Language.Bokmal to "Celle ${i * COLUMNS + a}") }
                        }
                    }
                }
            }
        }
    }

    object Lipsum : ParagraphPhrase<LangBokmal>() {
        override fun ParagraphOnlyScope<LangBokmal, Unit>.template() {
            text(
                Language.Bokmal to lipsums[0],
            )
        }
    }
}