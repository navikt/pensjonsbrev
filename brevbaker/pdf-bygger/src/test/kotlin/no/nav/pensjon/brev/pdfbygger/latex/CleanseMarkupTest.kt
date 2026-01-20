package no.nav.pensjon.brev.pdfbygger.latex

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.pdfbygger.attachment
import no.nav.pensjon.brev.pdfbygger.letterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@OptIn(InterneDataklasser::class)
class CleanseMarkupTest {

    @Test
    fun `tom tittel blir fjernet`() {
        val markup = letterMarkup {
            outline {
                title1 {
                    text("   ")
                    newLine()
                    text("      ")
                }
            }
        }
        assertThat(markup.clean().blocks).isEmpty()
    }

    @Test
    fun `tittel med tekst blir ikke fjernet men newlines blir fjern`() {
        val cleansed = letterMarkup {
            outline {
                title1 {
                    text("aaa")
                    newLine()
                    text("Innhold")
                }
            }
        }.clean()

        assertThat(cleansed.blocks).isNotEmpty()
        assertThat(cleansed.blocks[0]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.Title1Impl::class.java) { title1 ->
            assertThat(title1.content).allSatisfy { it !is LetterMarkup.ParagraphContent.Text.NewLine }
        }
    }

    @Test
    fun `to tomme titler etter hverandre blir fjernet`() {
        val markup = letterMarkup {
            outline {
                title1 {
                    text("   ")
                    newLine()
                    text("      ")
                }
                title1 {
                    text(" ")
                }
            }
        }
        val cleansed = markup.clean()
        assertThat(cleansed.blocks).isEmpty()
    }

    @Test
    fun `tomme paragraphs fjernes`() {
        val markup = letterMarkup {
            outline {
                paragraph {
                    text("   ")
                    newLine()
                    text("      ")
                }
                paragraph {
                    text(" ")
                }
                paragraph {
                    text("Innhold")
                }
            }
        }
        val cleansed = markup.clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
            assertThat(paragraph.content).anySatisfy {
                if (it is LetterMarkup.ParagraphContent.Text) {
                    assertThat(it.text).isEqualTo("Innhold")
                }
            }
        }
    }

    @Test
    fun `newLine maa ha noe innhold forran seg for aa beholdes`() {
        val cleansed = letterMarkup {
            outline {
                paragraph {
                    newLine()
                    text("Innhold etter newLine")
                }
                paragraph {
                    text("")
                    newLine()
                    text("      ")
                    newLine()
                    text("Innhold etter newLine")
                }
            }
        }.clean()
        assertThat(cleansed.blocks).hasSize(2)
        assertThat(cleansed.blocks[0]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
            assertThat(paragraph.content).hasSize(1)
            assertThat(paragraph.content).allSatisfy { it !is LetterMarkup.ParagraphContent.Text.NewLine }
        }
        assertThat(cleansed.blocks[1]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
            assertThat(paragraph.content).hasSize(3)
            assertThat(paragraph.content).allSatisfy { it !is LetterMarkup.ParagraphContent.Text.NewLine }
        }
    }

    @Test
    fun `newline kan ikke starte i en blokk`() {
        val cleansed = letterMarkup {
            outline {
                paragraph { }
                paragraph {
                    newLine()
                    text("Innhold etter newLine")
                }
            }
        }.clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
            assertThat(paragraph.content).hasSize(1)
            assertThat(paragraph.content).allSatisfy { it !is LetterMarkup.ParagraphContent.Text.NewLine }
        }
    }

    @Test
    fun `newline etter itemlist fjernes`() {
        val cleansed = letterMarkup {
            outline {
                paragraph {
                    text("Før list")
                    list {
                        item { text("Punkt 1") }
                        item { text("Punkt 2") }
                    }
                    newLine()
                    text("Innhold etter newLine")
                }
            }
        }.clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
            assertThat(paragraph.content).hasSize(3)
            assertThat(paragraph.content).allSatisfy { it !is LetterMarkup.ParagraphContent.Text.NewLine }
        }
    }

    @Test
    fun `newline etter table fjernes`() {
        val cleansed = letterMarkup {
            outline {
                paragraph {
                    text("Før table")
                    table(
                        header = {
                            column { text("Kolonne 1") }
                            column { text("Kolonne 2") }
                        }
                    ) {
                        row {
                            cell { text("Celle 1") }
                            cell { text("Celle 2") }
                        }
                        row {
                            cell { text("Celle 3") }
                            cell { text("Celle 4") }
                        }
                    }
                    newLine()
                    text("Innhold etter newLine")
                }
            }
        }.clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
            assertThat(paragraph.content).hasSize(3)
            assertThat(paragraph.content).allSatisfy { it !is LetterMarkup.ParagraphContent.Text.NewLine }
        }
    }

    @Test
    fun `renser ogsaa vedlegg`() {
        val cleansed = attachment {
            title {
                text("Tittel1")
            }
            outline {
                title2 {
                    text("")
                }
                paragraph {
                    text("Innhold")
                }
            }
        }.clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0]).isInstanceOfSatisfying(LetterMarkupImpl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
            assertThat(paragraph.content).hasSize(1)
            assertThat(paragraph.content).allSatisfy { it is LetterMarkup.ParagraphContent.Text.Literal }
        }
    }
}