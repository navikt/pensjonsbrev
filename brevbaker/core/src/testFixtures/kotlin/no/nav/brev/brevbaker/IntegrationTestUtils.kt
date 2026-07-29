package no.nav.brev.brevbaker

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.Letter2MarkupV2
import no.nav.brev.brevbaker.template.render.toMarkup
import no.nav.brev.brevbaker.template.toScope
import no.nav.brev.brevbaker.markup.dsl.letterPDFRequest
import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.maler.BestillBrevRequest
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.HTMLDocument
import no.nav.pensjon.brev.template.render.HTMLDocumentRenderer
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.nio.file.Path
import kotlin.io.path.Path

val BREVBAKER_URL = System.getenv("BREVBAKER_URL") ?: "http://localhost:8080"

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

fun <ParameterType : BrevbakerBrevdata> Letter<ParameterType>.renderTestPDF(
    pdfFileName: String,
    path: Path = Path.of("build", "test_pdf"),
    pdfByggerService: PDFByggerService? = null,
): Letter<ParameterType> {
    if (!FeatureToggleSingleton.isInitialized) {
        FeatureToggleSingleton.init(object : FeatureToggleService {
            override fun isEnabled(toggle: FeatureToggle): Boolean = true
            override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) { }
        })
    }

    val pdfBygger = pdfByggerService ?: PdfByggerTestService()

    Letter2Markup.render(this)
        .let {
            runBlocking {
                pdfBygger.producePDF(
                    PDFRequest(
                        it.letterMarkup,
                        it.attachments,
                        language.toCode(),
                        template.letterMetadata.brevtype,
                        Letter2Markup.renderPDFTitle(this@renderTestPDF.toScope(), listOf())
                    ),
                )
            }
        }
        .also { writeTestPDF(pdfFileName, it.bytes, path) }
    return this
}

fun <ParameterType : BrevbakerBrevdata> Letter<ParameterType>.renderTestPDFV2(
    pdfFileName: String,
    path: Path = Path.of("build", "test_pdf"),
    pdfByggerService: PDFByggerService? = null,
): Letter<ParameterType> {
    if (!FeatureToggleSingleton.isInitialized) {
        FeatureToggleSingleton.init(object : FeatureToggleService {
            override fun isEnabled(toggle: FeatureToggle): Boolean = true
            override fun verifiserAtAlleBrytereErDefinert(entries: List<FeatureToggle>) { }
        })
    }

    val pdfBygger = pdfByggerService ?: PdfByggerTestService()

    Letter2MarkupV2.render(this)
        .let { rendered ->
            runBlocking {
                pdfBygger.producePDFV2(
                    letterPDFRequest(
                        spraak = language.toCode().toMarkup(),
                        brevtype = template.letterMetadata.brevtype.toMarkup(),
                        letter = rendered.letterMarkup,
                    ) {
                        rendered.attachments.forEach { a -> attachment(a) }
                        Letter2MarkupV2.renderPDFTitle(this@renderTestPDFV2.toScope(), listOf())
                            .forEach { t -> pdfVedlegg(t) }
                    },
                )
            }
        }
        .also { writeTestPDF(pdfFileName, it.bytes, path) }
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

fun <ParameterType : Any> Letter<ParameterType>.renderTestHtml(htmlFileName: String, buildSubDir: String = "test_html"): Letter<ParameterType> {
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
        .also { writeTestHTML(htmlFileName, it, buildSubDir) }

    return this
}

fun <AttachmentData : VedleggData, Lang : LanguageSupport> createVedleggTestTemplate(
    template: AttachmentTemplate<Lang, AttachmentData>,
    attachmentData: Expression<AttachmentData>,
    languages: Lang,
) = createTemplate(
    letterDataType = EmptyAutobrevdata::class,
    languages = languages,
    letterMetadata = LetterMetadata(
        "test mal",
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

inline fun <reified LetterData : Any> outlineTestTemplate(
    noinline function: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit,
): LetterTemplate<LangBokmal, LetterData> =
    createTemplate(
        letterDataType = LetterData::class,
        languages = languages(Bokmal),
        letterMetadata = testLetterMetadata,
    ) {
        title { text(bokmal { +"test brev" }) }
        outline(function)
    }

fun LetterTemplate<LangBokmal, EmptyAutobrevdata>.renderTestPDF(fileName: String, felles: BrevbakerFelles = FellesFactory.felles, pdfByggerService: PDFByggerService) =
    LetterImpl(this, EmptyAutobrevdata, Bokmal, felles).renderTestPDF(fileName, pdfByggerService = pdfByggerService)

fun LetterTemplate<LangBokmal, EmptyAutobrevdata>.renderTestPDFV2(fileName: String, felles: BrevbakerFelles = FellesFactory.felles, pdfByggerService: PDFByggerService) =
    LetterImpl(this, EmptyAutobrevdata, Bokmal, felles).renderTestPDFV2(fileName, pdfByggerService = pdfByggerService)

val testLetterMetadata = LetterMetadata(
    displayTitle = "En fin display tittel",
    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
)
