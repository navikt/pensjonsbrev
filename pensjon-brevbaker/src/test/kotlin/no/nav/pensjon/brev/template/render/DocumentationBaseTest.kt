package no.nav.pensjon.brev.template.render

//import kotlinx.html.*
//import no.nav.pensjon.brev.template.*
//import org.junit.jupiter.api.Test
//import kotlinx.html.stream.*
//import no.nav.pensjon.brev.Fixtures
//import no.nav.pensjon.brev.api.model.LetterMetadata
//import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
//import no.nav.pensjon.brev.maler.UngUfoerAuto
//import no.nav.pensjon.brev.maler.fraser.common.KravVirkningFraOgMed
//import no.nav.pensjon.brev.template.Language.Bokmal
//import no.nav.pensjon.brev.template.dsl.*
//import no.nav.pensjon.brev.template.dsl.expression.*
//import java.io.*
//import java.time.*
//import kotlin.reflect.*
//import kotlin.reflect.jvm.*
//
//class RenderedHtmlDocumentation(val html: String) : RenderedLetter {
//    override fun base64EncodedFiles(): Map<String, String> = mapOf("index.html" to html)
//}
//
//object DocumentationHtml : BaseTemplate() {
//    override val languageSettings: LanguageSettings
//        get() = TODO("Not yet implemented")
//
//    override fun render(letter: Letter<*>): RenderedLetter {
//
//        val x = StringWriter().appendHTML(true)
//            .html {
//                lang = languageTag(letter.language)
//                head {
//                    meta(charset = Charsets.UTF_8.name())
//                    title {
////                        letter.template.title.forEach { renderDoc(this, scope, it) }
//                    }
//                    link("style.css", "stylesheet")
//                }
//                body {
//                    div("letter") {
//                        val scope = letter.toScope()
//
//                        h1("letter-title") {
//                            letter.template.title.forEach { renderDoc(this, scope, it) }
//                        }
//                        letter.template.outline.forEach { renderDoc(this, scope, it) }
//                    }
//                }
//            }
//
//        return RenderedHtmlDocumentation(x.toString())
//    }
//
//    private fun languageTag(lang: Language) =
//        when (lang) {
//            Bokmal -> "nb"
//            Language.English -> "en"
//            Language.Nynorsk -> "nn"
//        }
//
//    fun <T : FlowContent> renderDoc(
//        tag: T,
//        scope: ExpressionScope<*, *>,
//        element: Element<*>,
//    ) {
//        when (element) {
//            is Element.Conditional -> {
//                val toRender = if (element.predicate.eval(scope)) element.showIf else element.showElse
//                tag.div("conditional") {
//                    div("conditional-then") {
//                        element.showIf.forEach { renderDoc(tag, scope, it) }
//                    }
//
//                    if (element.showElse.isNotEmpty()) {
//                        div("conditional-else") {
//                            element.showElse.forEach { renderDoc(tag, scope, it) }
//                        }
//                    }
//                }
//            }
////            is Element.ForEachView<*, *> -> TODO()
////            is Element.Form.MultipleChoice -> TODO()
////            is Element.Form.Text -> TODO()
////            is Element.ItemList.Item -> TODO()
////            is Element.ItemList -> TODO()
////            is Element.NewLine -> TODO()
//            is Element.Paragraph -> {
//                tag.p { element.paragraph.forEach { renderDoc(this, scope, it) } }
//            }
////            is Element.Table.Row -> TODO()
////            is Element.Table -> TODO()
//            is Element.Text.Expression.ByLanguage -> {
//                tag.renderExpression(element.expr(scope.language))
//            }
//            is Element.Text.Expression -> {
//                tag.renderExpression(element.expression)
//            }
//            is Element.Text.Literal -> {
//                tag.text(element.text(scope.language))
//            }
//            is Element.Title1 -> {
//                tag.h1 {
//                    element.title1.forEach { renderDoc(this, scope, it) }
//                }
//            }
//            else -> {
//                println("Unsupported: ${element.schema}")
//            }
//        }
//    }
//}
//
//fun <T : FlowContent> T.renderExpression(expr: Expression<*>): Unit =
//    when (expr) {
//        is Expression.BinaryInvoke<*, *, *> -> {
//            when (expr.operation) {
//                is BinaryOperation.Concat -> {
//                    renderExpression(expr.first)
//                    renderExpression(expr.second)
//                }
//                is BinaryOperation.LocalizedDateFormat, BinaryOperation.LocalizedCurrencyFormat, BinaryOperation.LocalizedDoubleFormat, BinaryOperation.LocalizedIntFormat, BinaryOperation.LocalizedShortDateFormat -> {
//                    renderExpression(expr.first)
//                }
//                is BinaryOperation.IfElse<*> -> {
//                    span("expression") {
//                        renderExpression(expr.second)
//                    }
//                }
//                else -> {
//                    throw Exception("Burde ikke komme hit")
//                }
//            }
//        }
//        is Expression.Literal -> {
//            text(expr.value.toString())
//        }
//        is Expression.FromScope<*, *> -> {}
//        is Expression.UnaryInvoke<*, *> -> {
//            val operation = expr.operation
//            if (operation is UnaryOperation.Select) {
//                span("expression") { text(expr.findBestName() ?: throw Exception("FAILED TO FIND NAME: $expr")) }
////                val select = operation.select
////                renderExpression(expr.value)
////                when (select) {
////                    is KProperty<*> -> {
////                        val str = select.name
////                        span("expression") { text(str) }
////                    }
////                    is KFunction<*> -> {
////                        val str = select.name
////                        span("expression") { text(str) }
////                    }
////                    else -> {
////                        val str = select.toString()
////                        span("expression") { text(str) }
////                    }
////                }
//            } else {
//                renderExpression(expr.value)
//            }
//        }
//        else -> TODO("$expr")
//    }
//
//fun Expression<*>.findBestName(): String? =
//    when (this) {
//        is Expression.BinaryInvoke<*, *, *> -> TODO()
//        is Expression.FromScope<*, *> -> null
//        is Expression.Literal -> null
//        is Expression.UnaryInvoke<*, *> -> {
//            val moreSpecific = value.findBestName()
//            val operationReflect = operation
//            if (moreSpecific == null && operationReflect is UnaryOperation.Select) {
//                val selectReflect = operationReflect.select
//                if (selectReflect is KProperty<*>) selectReflect.name else null
//            } else {
//                moreSpecific
//            }
//        }
//        else -> throw Exception("Don't know how to find best name of: ${this.javaClass}")
//    }
//
//data class Dto(val name: String, val bursdag: KravVirkningFraOgMed)
//
//class DocumentationBaseTest {
//
//    private fun render(letter: Letter<*>) {
//        DocumentationHtml.render(letter)
//            .base64EncodedFiles()["index.html"]!!
//            .also {
//                val file = File("build/test_html/${letter.template.name}.html")
//                file.parentFile.mkdirs()
//                file.writeText(it)
//            }
//    }
//
//    @Test
//    fun writeDocs() {
//        render(
//            Letter(
//                UngUfoerAuto.template,
//                UngUfoerAutoDto(),
//                Bokmal,
//                Fixtures.fellesAuto
//            )
//        )
//
//    }
//
//    @Test
//    fun simpleCase() {
//        val template = createTemplate("simpleCase", PensjonLatex, Dto::class, languages(Bokmal), LetterMetadata("", true, LetterMetadata.Distribusjonstype.VIKTIG)) {
//            title { text(Bokmal to "Brevtittel") }
//            outline {
//                textExpr(Bokmal to "hei ".expr() + argument().select(Dto::bursdag).map { it.date.plusDays(10)!! }.format())
//            }
//        }
//
//        render(
//            Letter(
//                template,
//                Dto("Alexander", KravVirkningFraOgMed(LocalDate.of(1987, Month.APRIL, 22))),
//                Bokmal,
//                Fixtures.fellesAuto
//            )
//        )
//    }
//
//}