package no.nav.pensjon.brev.template.dsl.helpers

import com.natpryce.hamkrest.*
import com.tschuchort.compiletesting.*
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import java.io.File

fun hasKspErrorMessages(msg: String): Matcher<String> =
    containsSubstring("e: Error occurred in KSP") and
            containsSubstring(msg)

fun hasOnlySourceAndNoHelpers(): Matcher<Collection<File>> =
    hasSize(equalTo(2)) and allElements(has(File::getName, endsWith("MyClass.class") or endsWith("main.kotlin_module")))

fun SourceFile.compile(): JvmCompilationResult =
    listOf(this).compile()

fun List<SourceFile>.compile(): JvmCompilationResult {
    val sourcesToCompile = this
    return KotlinCompilation().apply {
        inheritClassPath = true
        kspWithCompilation = true
        configureKsp(useKsp2 = true) {
            symbolProcessorProviders += listOf(TemplateModelHelpersAnnotationProcessorProvider())
            withCompilation = true
        }
        sources = sourcesToCompile
    }.compile()
}

typealias SimpleTemplateScope<LetterData> = TemplateRootScope<LangBokmal, LetterData>
