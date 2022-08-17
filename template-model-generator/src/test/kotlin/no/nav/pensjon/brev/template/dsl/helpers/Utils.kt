package no.nav.pensjon.brev

import com.natpryce.hamkrest.*
import com.tschuchort.compiletesting.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorProvider
import java.io.File

fun hasKspErrorMessages(msg: String): Matcher<String> =
    containsSubstring("e: Error occurred in KSP") and
            containsSubstring(msg)

fun hasOnlySourceAndNoHelpers(): Matcher<Collection<File>> =
    hasSize(equalTo(2)) and allElements(has(File::getName, endsWith("MyClass.class") or endsWith("main.kotlin_module")))

fun SourceFile.compile(): KotlinCompilation.Result =
    listOf(this).compile()

fun List<SourceFile>.compile(): KotlinCompilation.Result {
    val sourcesToCompile = this
    return KotlinCompilation().apply {
        inheritClassPath = true
        symbolProcessorProviders = listOf(TemplateModelHelpersAnnotationProcessorProvider())
        kspWithCompilation = true
        sources = sourcesToCompile
    }.compile()
}