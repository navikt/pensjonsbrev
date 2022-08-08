package no.nav.pensjon.brev.template.dsl.helpers

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import com.tschuchort.compiletesting.*
import no.nav.pensjon.brev.compile
import org.junit.jupiter.api.Test
import java.io.File

@Suppress("unused")
data class NestedModel(val name: String, val second: SecondModel) {
    data class SecondModel(val lastName: String)
}

@Suppress("unused")
data class NestedModelWithRepetition(val name: String, val first: SubModel, val second: SubModel) {
    data class SubModel(val lastName: String)
}

@Suppress("unused")
data class ModelWithTypeParameters(val name: String, val aList: List<String>, val otherList: List<NestedModel>, val aMap: Map<String, NestedModel>)

class TemplateModelVisitorTest {

    @Test
    fun `generates helpers for models that has data classes as property types`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.NestedModel
                    import no.nav.pensjon.brev.template.dsl.helpers.NestedModelSelectors.second
                    import no.nav.pensjon.brev.template.dsl.helpers.SecondModelSelectors.lastName

                    @TemplateModelHelpers
                    object MyClass : HasModel<NestedModel> {
                        fun doSomething() {
                            TemplateGlobalScope<NestedModel>().second.lastName
                        }
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)))
        assertThat(result.generatedFiles, anyElement(has(File::getName, containsSubstring("NestedModelSelectors"))))
        assertThat(result.generatedFiles, anyElement(has(File::getName, containsSubstring("SecondModelSelectors"))))
    }

    @Test
    fun `generates helpers for models that has data classes as property types with repetition`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.NestedModelWithRepetition
                    import no.nav.pensjon.brev.template.dsl.helpers.NestedModelWithRepetitionSelectors.second
                    import no.nav.pensjon.brev.template.dsl.helpers.SubModelSelectors.lastName

                    @TemplateModelHelpers
                    object MyClass : HasModel<NestedModelWithRepetition> {
                        fun doSomething() {
                            TemplateGlobalScope<NestedModelWithRepetition>().second.lastName
                        }
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)))
        assertThat(result.generatedFiles, anyElement(has(File::getName, containsSubstring("NestedModelWithRepetitionSelectors"))))
        assertThat(result.generatedFiles, anyElement(has(File::getName, containsSubstring("SubModelSelectors"))))
    }

    @Test
    fun `generates helpers for models that has properties with type parameters`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                import no.nav.pensjon.brev.template.HasModel
                import no.nav.pensjon.brev.template.Expression
                import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                import no.nav.pensjon.brev.template.dsl.helpers.ModelWithTypeParameters
                import no.nav.pensjon.brev.template.dsl.helpers.ModelWithTypeParametersSelectors.otherList
                import no.nav.pensjon.brev.template.dsl.helpers.NestedModel

                @TemplateModelHelpers
                object MyClass : HasModel<ModelWithTypeParameters> {
                    fun doSomething() {
                        val list: Expression<List<NestedModel>> = TemplateGlobalScope<ModelWithTypeParameters>().otherList
                    }
                }
            """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)))
        assertThat(result.generatedFiles, anyElement(has(File::getName, containsSubstring("ModelWithTypeParametersSelectors"))))
//        assertThat(result.generatedFiles, anyElement(has(File::getName, containsSubstring("NestedModelSelectors"))))
    }

    @Test
    fun `generates helpers for models that has properties with type parameters that require qualified type names`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                import no.nav.pensjon.brev.template.HasModel
                import no.nav.pensjon.brev.template.Expression
                import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                import no.nav.pensjon.brev.template.somepkg.ModelOutsideOfTestPackage
                import no.nav.pensjon.brev.template.somepkg.ModelOutsideOfTestPackageSelectors.thing
                import no.nav.pensjon.brev.template.otherpkg.TypeParameterModel
                import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                import no.nav.pensjon.brev.template.thirdpkg.OtherWithTypeParameter

                @TemplateModelHelpers
                object MyClass : HasModel<ModelOutsideOfTestPackage> {
                    fun doSomething() {
                        val list: Expression<TypeParameterModel<OtherWithTypeParameter<SimpleModel>>> = TemplateGlobalScope<ModelOutsideOfTestPackage>().thing
                    }
                }
            """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)))
        assertThat(result.generatedFiles, anyElement(has(File::getName, containsSubstring("ModelOutsideOfTestPackageSelectors"))))
    }

    @Test
    fun `ignores model that is not a data class`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest

                    @TemplateModelHelpers
                    object MyClass : HasModel<List<String>> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(equalTo(2)) and anyElement(has(File::getName, containsSubstring("ListSelectors").not())))
    }

}