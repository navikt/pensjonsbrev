@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.pdfbygger.typst

import kotlinx.coroutines.runBlocking
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.LaTeXCompilerService
import no.nav.brev.brevbaker.PDFByggerTestContainer
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
 */
@Tag(TestTags.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TypstEscapingVisualITest {

    private val pdfByggerService = LaTeXCompilerService(PDFByggerTestContainer.mappedUrl())

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
            title {
                text(ALL_THE_SYMBOLS)
            }

            sakspart {
                gjelderNavn = ALL_THE_SYMBOLS
                saksnummer = ALL_THE_SYMBOLS
                annenMottakerNavn = ALL_THE_SYMBOLS
            }

            signatur {
                saksbehandlerNavn = ALL_THE_SYMBOLS
                attesterendeSaksbehandlerNavn = ALL_THE_SYMBOLS
                navAvsenderEnhet = ALL_THE_SYMBOLS
            }

            outline {
                title1 {
                    text(ALL_THE_SYMBOLS)
                }

                title2 {
                    text(ALL_THE_SYMBOLS)
                }

                title3 {
                    text(ALL_THE_SYMBOLS)
                }

                paragraph {
                    text(ALL_THE_SYMBOLS)
                }

                paragraph {
                    text(ALL_THE_SYMBOLS, FontType.BOLD)
                }

                paragraph {
                    text(ALL_THE_SYMBOLS, FontType.ITALIC)
                }

                paragraph {
                    text(ALL_THE_SYMBOLS)
                    newLine()
                    text(ALL_THE_SYMBOLS, FontType.BOLD)
                    newLine()
                    text(ALL_THE_SYMBOLS, FontType.ITALIC)
                }

                paragraph {
                    list {
                        item { text(ALL_THE_SYMBOLS) }
                        item { text(ALL_THE_SYMBOLS, FontType.BOLD) }
                        item { text(ALL_THE_SYMBOLS, FontType.ITALIC) }
                    }
                }

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
