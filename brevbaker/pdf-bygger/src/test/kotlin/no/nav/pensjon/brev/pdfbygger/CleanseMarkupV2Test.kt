package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2Impl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(InterneDataklasser::class)
class CleanseMarkupV2Test {

    @Test
    fun `tom tittel blir fjernet`() {
        val markup = letterMarkupV2 {
            outline {
                title2 {
                    text("   ")
                    newLine()
                    text("      ")
                }
            }
        }
        Assertions.assertThat(markup.clean().blocks).isEmpty()
    }

    @Test
    fun `tittel med tekst blir ikke fjernet men newlines blir fjernet`() {
        val cleansed = letterMarkupV2 {
            outline {
                title2 {
                    text("aaa")
                    newLine()
                    text("Innhold")
                }
            }
        }.clean()

        Assertions.assertThat(cleansed.blocks).isNotEmpty()
        Assertions.assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.Title2Impl::class.java) { title2 ->
                Assertions.assertThat(title2.content).allSatisfy { it !is LetterMarkupV2.Text.NewLine }
            }
    }

    @Test
    fun `to tomme titler etter hverandre blir fjernet`() {
        val markup = letterMarkupV2 {
            outline {
                title2 {
                    text("   ")
                    newLine()
                    text("      ")
                }
                title3 {
                    text(" ")
                }
            }
        }
        val cleansed = markup.clean()
        Assertions.assertThat(cleansed.blocks).isEmpty()
    }

    @Test
    fun `tomme paragraphs fjernes`() {
        val markup = letterMarkupV2 {
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
        Assertions.assertThat(cleansed.blocks).hasSize(1)
        Assertions.assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
                Assertions.assertThat(paragraph.content).anySatisfy {
                    Assertions.assertThat(it.text).isEqualTo("Innhold")
                }
            }
    }

    @Test
    fun `newLine maa ha noe innhold forran seg for aa beholdes`() {
        val cleansed = letterMarkupV2 {
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
        Assertions.assertThat(cleansed.blocks).hasSize(2)
        Assertions.assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
                Assertions.assertThat(paragraph.content).hasSize(1)
                Assertions.assertThat(paragraph.content).allSatisfy { it !is LetterMarkupV2.Text.NewLine }
            }
        Assertions.assertThat(cleansed.blocks[1])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
                Assertions.assertThat(paragraph.content).hasSize(3)
                Assertions.assertThat(paragraph.content).allSatisfy { it !is LetterMarkupV2.Text.NewLine }
            }
    }

    @Test
    fun `newline kan ikke starte i en blokk`() {
        val cleansed = letterMarkupV2 {
            outline {
                paragraph { }
                paragraph {
                    newLine()
                    text("Innhold etter newLine")
                }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(1)
        Assertions.assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
                Assertions.assertThat(paragraph.content).hasSize(1)
                Assertions.assertThat(paragraph.content).allSatisfy { it !is LetterMarkupV2.Text.NewLine }
            }
    }

    @Test
    fun `tom liste fjernes`() {
        val cleansed = letterMarkupV2 {
            outline {
                paragraph { text("Før list") }
                list { }
                paragraph { text("Etter list") }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(2)
        Assertions.assertThat(cleansed.blocks).noneMatch { it is LetterMarkupV2.Block.ListContent }
    }

    @Test
    fun `liste med innhold beholdes som egen blokk`() {
        val cleansed = letterMarkupV2 {
            outline {
                paragraph { text("Før list") }
                list {
                    item { text("Punkt 1") }
                    item { text("Punkt 2") }
                }
                paragraph { text("Etter list") }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(3)
        Assertions.assertThat(cleansed.blocks[1]).isInstanceOf(LetterMarkupV2Impl.BlockImpl.ItemListImpl::class.java)
    }

    @Test
    fun `tom tabell fjernes`() {
        val cleansed = letterMarkupV2 {
            outline {
                paragraph { text("Før table") }
                table(header = { }) { }
                paragraph { text("Etter table") }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(2)
        Assertions.assertThat(cleansed.blocks).noneMatch { it is LetterMarkupV2.Block.Table }
    }

    @Test
    fun `tom formChoice fjernes ikke`() {
        val cleansed = letterMarkupV2 {
            outline {
                paragraph { text("Før form") }
                formChoice { }
                paragraph { text("Etter form") }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(3)
        Assertions.assertThat(cleansed.blocks[1]).isInstanceOf(LetterMarkupV2Impl.BlockImpl.FormChoiceImpl::class.java)
    }

    @Test
    fun `formChoice med prompt og choices beholdes uendret`() {
        val cleansed = letterMarkupV2 {
            outline {
                formChoice {
                    prompt { text("Velg et alternativ") }
                    choice { text("Ja") }
                    choice { text("Nei") }
                }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(1)
        Assertions.assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.FormChoiceImpl::class.java) { formChoice ->
                Assertions.assertThat(formChoice.prompt).hasSize(1)
                Assertions.assertThat(formChoice.choices).hasSize(2)
            }
    }

    @Test
    fun `tom formText fjernes ikke`() {
        val cleansed = letterMarkupV2 {
            outline {
                paragraph { text("Før form") }
                formText { }
                paragraph { text("Etter form") }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(3)
        Assertions.assertThat(cleansed.blocks[1]).isInstanceOf(LetterMarkupV2Impl.BlockImpl.FormTextImpl::class.java)
    }

    @Test
    fun `formText med prompt beholdes uendret`() {
        val cleansed = letterMarkupV2 {
            outline {
                formText(size = LetterMarkupV2.Block.FormText.Size.LONG) {
                    prompt { text("Skriv inn navn") }
                }
            }
        }.clean()
        Assertions.assertThat(cleansed.blocks).hasSize(1)
        Assertions.assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.FormTextImpl::class.java) { formText ->
                Assertions.assertThat(formText.prompt).hasSize(1)
                Assertions.assertThat(formText.size).isEqualTo(LetterMarkupV2.Block.FormText.Size.LONG)
            }
    }

    @Test
    fun `renser ogsaa vedlegg`() {
        val cleansed = attachmentV2 {
            title1 {
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
        Assertions.assertThat(cleansed.blocks).hasSize(1)
        Assertions.assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(LetterMarkupV2Impl.BlockImpl.ParagraphImpl::class.java) { paragraph ->
                Assertions.assertThat(paragraph.content).hasSize(1)
                Assertions.assertThat(paragraph.content).allSatisfy { it is LetterMarkupV2.Text.Literal }
            }
    }
}
