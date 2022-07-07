package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Test
import kotlin.reflect.*

@LetterTemplateMarker
class XScope<Lang : LanguageSupport, LetterData : Any> : OutlineScopeBase<Lang, LetterData, XScope<Lang, LetterData>>() {

    fun includePhrase(phrase: XPhrase<out Lang, Unit>) {
        phrase.apply(this, Unit.expr())
    }

    fun <PhraseData> includePhrase(phrase: XPhrase<out Lang, PhraseData>, data: Expression<PhraseData>) {
        phrase.apply(this, data)
    }

    override fun scopeFactory(): XScope<Lang, LetterData> = XScope()

}


class XPhrase<Lang : LanguageSupport, PhraseData>(private val phraseBody: XScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) {
    fun apply(scope: XScope<in Lang, *>, data: Expression<PhraseData>) {
        XScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}

interface Dto {
    val name: String
}
val myPhrase = XPhrase<LangBokmal, Dto> {

}

class Xpr<out T>(val t: T) {
    fun <R, X> select(mapper: X): Xpr<R> where X : (T) -> R, X: KProperty<R> {
        println("Fetching: " + mapper.name)
        return Xpr(mapper(t))
    }
}

class MappingBuilder<T, R> {
    fun <X> add(property: KProperty<X>): MappingBuilder<(X, T) -> Pair<X, T>, R>
        = MappingBuilder()

    fun build(): (T) -> R = TODO()
}

data class LetterDto(val nameOfPerson: String, val age: Int)
data class Bla(val x1: String, val x2: Int, val x3: String, val x4: Int, val x5: String, val x6: Boolean)


class MyTest {

    val x: KFunction2<String, Int, LetterDto> = ::LetterDto
    val y: KFunction10<String, Int, String, Int, String, Boolean, String, String, String, String, Bla>? = null

    val z = MappingBuilder<String, Unit>().add(LetterDto::age).build()


//    @Test
    fun bla() {

        val template = XScope<LangBokmal, LetterDto>().apply {
            val jadda = Xpr(LetterDto("hei", 2)).select(LetterDto::nameOfPerson)
        }

    }
}

