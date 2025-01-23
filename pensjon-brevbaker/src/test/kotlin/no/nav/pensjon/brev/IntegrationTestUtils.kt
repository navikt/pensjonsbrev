package no.nav.pensjon.brev

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.OutlineElement
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.HTMLDocument
import no.nav.pensjon.brev.template.render.HTMLDocumentRenderer
import no.nav.pensjon.brev.template.render.LatexDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

val BREVBAKER_URL = System.getenv("BREVBAKER_URL") ?: "http://localhost:8080"
const val PDF_BUILDER_URL = "http://localhost:8081"

object TestTags {
    const val INTEGRATION_TEST = "integration-test"

    // For visual inspection of documents/design
    const val MANUAL_TEST = "manual-test"
}


fun requestLetter(client: HttpClient, letterRequest: BestillBrevRequest<Brevkode.Automatisk>): LetterResponse =
    runBlocking {
        client.post("letter/autobrev/pdf") {
            contentType(ContentType.Application.Json)
            setBody(letterRequest)
        }.body()
    }

fun writeTestPDF(pdfFileName: String, pdf: ByteArray, path: Path = Path.of("build", "test_pdf")) {
    val file = path.resolve("${pdfFileName.replace(" ", "_")}.pdf").toFile()
    file.parentFile.mkdirs()
    file.writeBytes(pdf)
    println("Test-file written to file:${"\\".repeat(3)}${file.absolutePath}".replace('\\', '/'))
}

private val laTeXCompilerService = LaTeXCompilerService(PDF_BUILDER_URL, maxRetries = 0)

fun renderTestPdfOutline(
    outputFolder: String,
    testName: String,
    felles: Felles? = null,
    brevtype: LetterMetadata.Brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
    attachments: List<AttachmentTemplate<LangBokmal, EmptyBrevdata>> = emptyList(),
    title: String? = null,
    outlineInit: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit,
) {
    val template = createTemplate(
        testName, EmptyBrevdata::class, languages(Bokmal), LetterMetadata(
            testName,
            false,
            LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype
        )
    ) {
        title {
            text(Bokmal to (title ?: testName))
        }
        outline { outlineInit() }
        attachments.forEach { includeAttachment(it) }
    }
    val letter = Letter(template, Unit, Bokmal, felles ?: Fixtures.fellesAuto)
    letter.renderTestPDF(testName, Path.of("build/$outputFolder"))
}

fun renderTestVedleggPdf(
    testName: String,
    title: String? = null,
    includeSakspart: Boolean,
    outputFolder: String,
    felles: Felles? = null,
    outlineInit: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit,
) {
    val vedlegg: AttachmentTemplate<LangBokmal, EmptyBrevdata> = createAttachment<LangBokmal, EmptyBrevdata>(
        title = newText(
            Bokmal to (title ?: testName)
        ),
        includeSakspart = includeSakspart,
    ) {
        outlineInit()
    }
    renderTestPdfOutline(attachments = listOf(vedlegg), outputFolder = outputFolder, testName = testName, title = title, felles = felles) { }
}


fun <ParameterType : Any> Letter<ParameterType>.renderTestPDF(
    pdfFileName: String,
    path: Path = Path.of("build", "test_pdf"),
): Letter<ParameterType> {
    Letter2Markup.render(this)
        .let {
            LatexDocumentRenderer.render(
                it.letterMarkup,
                it.attachments,
                language,
                felles,
                template.letterMetadata.brevtype
            )
        }
        .let { runBlocking { laTeXCompilerService.producePDF(it).base64PDF } }
        .also { writeTestPDF(pdfFileName, Base64.getDecoder().decode(it), path) }
    return this
}

fun writeTestHTML(letterName: String, htmlLetter: HTMLDocument, buildSubDir: String = "test_html") {
    val dir = Path("build/$buildSubDir/$letterName")
    dir.toFile().mkdirs()
    htmlLetter.files.forEach { it.writeTo(dir) }
    htmlLetter.files.firstOrNull { it.fileName == "index.html" }
        ?.also {
            println("""Test index-html written to file://${dir.resolve(it.fileName).toAbsolutePath()}""")
        }
}

fun <ParameterType : Any> Letter<ParameterType>.renderTestHtml(htmlFileName: String): Letter<ParameterType> {
    Letter2Markup.render(this)
        .let {
            HTMLDocumentRenderer.render(
                it.letterMarkup,
                it.attachments,
                language,
                felles,
                template.letterMetadata.brevtype
            )
        }
        .also { writeTestHTML(htmlFileName, it) }

    return this
}

fun <AttachmentData : Any, Lang : LanguageSupport> createVedleggTestTemplate(
    template: AttachmentTemplate<Lang, AttachmentData>,
    attachmentData: Expression<AttachmentData>,
    languages: Lang,
) = createTemplate(
    name = "test-template",
    letterDataType = Unit::class,
    languages = languages,
    letterMetadata = LetterMetadata(
        "test mal",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
        brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
    ),
) {
    title {
        eval("Tittel".expr())
    }

    outline {}

    includeAttachment(template, attachmentData)
}

internal inline fun <reified LetterData : Any> outlineTestTemplate(
    noinline function: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit,
): LetterTemplate<LangBokmal, LetterData> =
    createTemplate(
        name = "test",
        letterDataType = LetterData::class,
        languages = languages(Bokmal),
        letterMetadata = testLetterMetadata,
    ) {
        title.add(bokmalTittel)
        outline(function)
    }

internal fun LetterTemplate<LangBokmal, EmptyBrevdata>.renderTestPDF(fileName: String, felles: Felles = Fixtures.felles) =
    Letter(this, EmptyBrevdata, Bokmal, felles).renderTestPDF(fileName)

internal fun outlineTestLetter(vararg elements: OutlineElement<LangBokmal>) = LetterTemplate(
    name = "test",
    title = listOf(bokmalTittel),
    letterDataType = Unit::class,
    language = languages(Bokmal),
    outline = elements.asList(),
    letterMetadata = testLetterMetadata
)