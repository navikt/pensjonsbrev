@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.pdfbygger.typst

import kotlinx.coroutines.runBlocking
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.LaTeXCompilerService
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.writeTestPDF
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.pdfbygger.attachment
import no.nav.pensjon.brev.pdfbygger.letterMarkup
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.FontType
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import java.nio.file.Path

/**
 * Visual integration test that verifies proper escaping of special characters
 * in Typst document rendering.
 *
 * This test puts ALL special characters into EVERY possible field/scenario
 * to verify that escaping works correctly everywhere.
 *
 * To run this test locally:
 * 1. Start pdf-bygger locally on port 8081
 */
@Tag(TestTags.MANUAL_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TypstEscapingVisualITest {

    private val pdfByggerService = LaTeXCompilerService("http://localhost:8081")

    /**
     * The ultimate escape test string - contains ALL characters that might need escaping:
     * - Typst content mode special chars: \ # * _ ` $ < > @ [ ]
     * - Typst string special chars: \ " \n \t \r
     * - Common special chars: & % ^ ~ { } | / ? ! ; : ' "
     * - Unicode/international: æ ø å Æ Ø Å ü ö ä ß é è ê ë ñ
     * - Math/currency: + - = × ÷ ± ∞ € £ ¥ $ ¢ ©
     * - Injection attempts: ]#[ #import #eval #set #show
     */
    private val ALL_THE_SYMBOLS = """Test: \ # * _ ` $ < > @ [ ] & % ^ ~ { } | / ? ! ; : ' " æøåÆØÅ üöäß éèêëñ +-=×÷±∞ €£¥$¢© ]#[attack] #import #eval("x") #set text(red) #show """

    private fun renderTestPdf(testName: String, pdfRequest: PDFRequest) {
        runBlocking {
            val result = pdfByggerService.producePDF(pdfRequest, shouldRetry = false)
            writeTestPDF(testName, result.bytes, Path.of("build/test_visual/typst"))
        }
    }

    @Test
    fun `escaping all symbols in all fields`() {
        val letter = letterMarkup {
            // Title with all symbols
            title {
                text(ALL_THE_SYMBOLS)
            }

            // Sakspart with all symbols in all fields
            sakspart {
                gjelderNavn = ALL_THE_SYMBOLS
                saksnummer = ALL_THE_SYMBOLS
                annenMottakerNavn = ALL_THE_SYMBOLS
            }

            // Signatur with all symbols
            signatur {
                saksbehandlerNavn = ALL_THE_SYMBOLS
                attesterendeSaksbehandlerNavn = ALL_THE_SYMBOLS
                navAvsenderEnhet = ALL_THE_SYMBOLS
            }

            outline {
                // Title1 with all symbols
                title1 {
                    text(ALL_THE_SYMBOLS)
                }

                // Title2 with all symbols
                title2 {
                    text(ALL_THE_SYMBOLS)
                }

                // Title3 with all symbols
                title3 {
                    text(ALL_THE_SYMBOLS)
                }

                // Paragraph with plain text
                paragraph {
                    text(ALL_THE_SYMBOLS)
                }

                // Paragraph with bold text
                paragraph {
                    text(ALL_THE_SYMBOLS, FontType.BOLD)
                }

                // Paragraph with italic text
                paragraph {
                    text(ALL_THE_SYMBOLS, FontType.ITALIC)
                }

                // Paragraph with mixed formatting
                paragraph {
                    text(ALL_THE_SYMBOLS)
                    newLine()
                    text(ALL_THE_SYMBOLS, FontType.BOLD)
                    newLine()
                    text(ALL_THE_SYMBOLS, FontType.ITALIC)
                }

                // List with all symbols in items
                paragraph {
                    list {
                        item { text(ALL_THE_SYMBOLS) }
                        item { text(ALL_THE_SYMBOLS, FontType.BOLD) }
                        item { text(ALL_THE_SYMBOLS, FontType.ITALIC) }
                    }
                }

                // Table with all symbols in headers and cells
                paragraph {
                    table(
                        header = {
                            column { text(ALL_THE_SYMBOLS) }
                            column { text(ALL_THE_SYMBOLS) }
                        },
                        bodyBlock = {
                            row {
                                cell { text(ALL_THE_SYMBOLS) }
                                cell { text(ALL_THE_SYMBOLS) }
                            }
                            row {
                                cell { text(ALL_THE_SYMBOLS, FontType.BOLD) }
                                cell { text(ALL_THE_SYMBOLS, FontType.ITALIC) }
                            }
                        }
                    )
                }

                // FormText with all symbols
                paragraph {
                    formText(
                        size = LetterMarkup.ParagraphContent.Form.Text.Size.LONG,
                        vspace = true
                    ) {
                        text(ALL_THE_SYMBOLS)
                    }
                }

                paragraph {
                    formText(
                        size = LetterMarkup.ParagraphContent.Form.Text.Size.SHORT,
                        vspace = false
                    ) {
                        text(ALL_THE_SYMBOLS)
                    }
                }

                paragraph {
                    formText(
                        size = LetterMarkup.ParagraphContent.Form.Text.Size.FILL,
                        vspace = true
                    ) {
                        text(ALL_THE_SYMBOLS)
                    }
                }

                // FormChoice with all symbols
                paragraph {
                    formChoice(
                        vspace = true,
                        prompt = { text(ALL_THE_SYMBOLS) },
                        choices = {
                            choice { text(ALL_THE_SYMBOLS) }
                            choice { text(ALL_THE_SYMBOLS) }
                            choice { text(ALL_THE_SYMBOLS) }
                        }
                    )
                }
            }
        }

        // Attachment with all symbols
        val attachment = attachment {
            title {
                text(ALL_THE_SYMBOLS)
            }
            includeSakspart = true

            outline {
                title1 {
                    text(ALL_THE_SYMBOLS)
                }

                paragraph {
                    text(ALL_THE_SYMBOLS)
                }

                paragraph {
                    list {
                        item { text(ALL_THE_SYMBOLS) }
                    }
                }

                paragraph {
                    table(
                        header = {
                            column { text(ALL_THE_SYMBOLS) }
                        },
                        bodyBlock = {
                            row {
                                cell { text(ALL_THE_SYMBOLS) }
                            }
                        }
                    )
                }

                paragraph {
                    formText(
                        size = LetterMarkup.ParagraphContent.Form.Text.Size.LONG
                    ) {
                        text(ALL_THE_SYMBOLS)
                    }
                }

                paragraph {
                    formChoice(
                        vspace = true,
                        prompt = { text(ALL_THE_SYMBOLS) },
                        choices = {
                            choice { text(ALL_THE_SYMBOLS) }
                        }
                    )
                }
            }
        }

        // PDF vedlegg with all symbols in title
        val pdfVedleggTitles = listOf(
            PDFTittel(
                title = listOf(
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 1,
                        text = ALL_THE_SYMBOLS,
                        fontType = FontType.PLAIN
                    )
                )
            )
        )

        val pdfRequest = PDFRequest(
            letterMarkup = letter,
            attachments = listOf(attachment),
            language = Language.Bokmal.toCode(),
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            pdfVedlegg = pdfVedleggTitles
        )

        renderTestPdf("escaping_all_symbols_in_all_fields", pdfRequest)
    }
}
