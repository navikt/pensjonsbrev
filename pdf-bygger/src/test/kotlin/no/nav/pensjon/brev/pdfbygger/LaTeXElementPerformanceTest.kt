package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.brevbaker.LaTeXCompilerService
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.VedleggPDFTestUtils.renderTestPdfOutline
import no.nav.pensjon.brev.pdfbygger.LaTeXElementPerformanceTest.ElementType.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

private const val WARMUP_RUNS = 5
private const val NUMBER_OF_SAMPLES = 5
private const val ELEMENT_COUNT = 100

// Brukes for å måle hvordan ulike typer innholds-elementer påvirker kompilerings-tiden
@Tag(TestTags.MANUAL_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LaTeXElementPerformanceTest {

    val laTeXCompilerService = LaTeXCompilerService(PDFByggerTestContainer.mappedUrl())


    private data class TimingResult(val elementType: ElementType, val time: Duration, val count: Int)

    @Test
    fun performanceTest() {

        // warmup the application to get to a consistent performance
        repeat(WARMUP_RUNS) { timedRender(BLANK, 0) }

        val results = (0..NUMBER_OF_SAMPLES).flatMap {
            ElementType.entries.map { elementType ->
                TimingResult(
                    elementType = elementType,
                    time = timedRender(elementType, ELEMENT_COUNT),
                    count = ELEMENT_COUNT
                )
            }
        }

        val (baseline, other) = results.partition { it.elementType == BLANK }
        val baselineTime = baseline.averaged().first().time

        println("Ytelse for blankt dokument var: ${baselineTime.toIsoString()}")

        other.averaged().forEach {
            println(
                """Ytelse for element med type ${it.elementType.name}
                    | Count: ${it.count}
                    | Time: ${it.time.toIsoString()}
                    | Time over baseline: ${baselineTime.minus(it.time).toIsoString()}
                """.trimMargin()
            )
        }
    }

    private fun List<TimingResult>.averaged(): List<TimingResult> =
        groupBy { it.elementType.name }
            .map { elementResults ->
                val first = elementResults.value.first()
                val average =
                    elementResults.value.sumOf { it.time.inWholeMilliseconds / it.count } / elementResults.value.size
                TimingResult(
                    first.elementType,
                    average.toDuration(DurationUnit.MILLISECONDS),
                    first.count
                )
            }


    private fun timedRender(elementType: ElementType, count: Int) =
        measureTime {
            render(overrideName = "timing_${elementType.name}_$count") {
                addElement(elementType, count)
            }
        }

    private fun OutlineOnlyScope<LangBokmal, *>.addElement(elementType: ElementType, count: Int) {

        when (elementType) {
            TITLE_1 -> repeat(count) { title1 { text(bokmal { +"bla" }) } }
            TITLE_2 -> repeat(count) { title2 { text(bokmal { +"bla" }) } }
            MULTIPLE_TABLES -> repeat(count) {
                paragraph {
                    table(header = {
                        column(columnSpan = 2) { text(bokmal { +"Tekst" }) }
                        column(alignment = RIGHT) { text(bokmal { +"Kroner" }) }
                    }) {
                        row {
                            cell { text(bokmal { +"Rad $it" }) }
                            cell { text(bokmal { +"$it Kroner" }) }
                        }
                    }
                }
            }

            MULTIPLE_ROWS -> paragraph {
                table(header = {
                    column(columnSpan = 2) { text(bokmal { +"Tekst" }) }
                    column(alignment = RIGHT) { text(bokmal { +"Kroner" }) }
                }) {
                    repeat(count) {
                        row {
                            cell { text(bokmal { +"Rad $it" }) }
                            cell { text(bokmal { +"$it Kroner" }) }
                        }
                    }
                }
            }

            BLANK -> {}
        }
    }

    enum class ElementType {
        TITLE_1,
        TITLE_2,
        MULTIPLE_TABLES,
        MULTIPLE_ROWS,
        BLANK,
    }


    private fun render(
        overrideName: String? = null,
        title: String? = null,
        felles: Felles? = null,
        brevtype: LetterMetadata.Brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        outlineInit: OutlineOnlyScope<LangBokmal, *>.() -> Unit,
    ) {
        val testName = overrideName ?: StackWalker.getInstance()
            .walk { frames -> frames.skip(2).findFirst().map { it.methodName }.orElse("") }
        renderTestPdfOutline(
            "test_performance/pdf",
            testName = testName,
            felles = felles,
            brevtype = brevtype,
            outlineInit = outlineInit,
            title = title ?: testName,
            pdfByggerService = laTeXCompilerService
        )
    }

}