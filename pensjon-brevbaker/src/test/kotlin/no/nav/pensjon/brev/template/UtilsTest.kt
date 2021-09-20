//package no.nav.pensjon.brev.template
//
//import no.nav.pensjon.brev.template.base.DummyBase
//import no.nav.pensjon.brev.template.dsl.argument
//import org.hamcrest.MatcherAssert.assertThat
//import org.hamcrest.core.IsCollectionContaining
//import org.junit.jupiter.api.Test
//
//class UtilsTest {
//
//    object TestMaster : DummyBase() {
//        override val parameters = emptySet<TemplateParameter>()
//    }
//
//    val frase1 = Phrase.Static.create("jadda", Language.Bokmal to "jadda")
//    val frase2 = Phrase.Static.create("jadda2", Language.Bokmal to "jadda2")
//
//    val templ = createTemplate("test", newText(Language.Bokmal to "test"), TestMaster, languages(Language.Bokmal)) {
//        parameters {
//            required { PensjonInnvilget }
//            required { KortNavn }
//        }
//        outline {
//            showIf(argument(PensjonInnvilget)) {
//                text(Language.Bokmal to "hello1")
//                phrase(frase1)
//            } orShow {
//                title1 {
//                    text(Language.Bokmal to "hello2")
//                    phrase(frase2)
//                    eval(argument(KortNavn))
//                }
//            }
//
//            title1 { text(Language.Bokmal to "heisann") }
//            paragraph {
//                text(Language.Bokmal to "hello3")
//                phrase(frase2)
//
//                eval(argument(KortNavn))
//            }
//        }
//    }
//
//    @Test
//    fun `findExpressions finds all expressions`() {
//        val expressions = templ.outline.flatMap { it.findExpressions() }
//
//        assertThat(expressions, IsCollectionContaining.hasItems(argument(PensjonInnvilget), argument(KortNavn), argument(KortNavn)))
//    }
//
//    @Test
//    fun `requiredArguments finds all required arguments in an expression`() {
//        val expr = Expression.BinaryInvoke(
//            argument(KortNavn),
//            Expression.UnaryInvoke(argument(PensjonInnvilget), UnaryOperation.ToString()),
//            BinaryOperation.Equal()
//        )
//
//        assertThat(expr.requiredArguments(), IsCollectionContaining.hasItems(argument(KortNavn), argument(PensjonInnvilget)))
//    }
//}