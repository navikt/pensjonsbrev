package no.nav.pensjon.brev.template.dsl.helpers

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.*
import com.tschuchort.compiletesting.*
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Test
import java.io.File

private val unsupportedAnnotationTarget = UnsupportedAnnotationTarget::class.qualifiedName!!
private val invalidObjectTarget = InvalidObjectTarget::class.qualifiedName!!
private val annotationName = TemplateModelHelpers::class.qualifiedName!!
private val hasModelName = HasModel::class.qualifiedName!!

class TemplateModelHelpersAnnotationProcessorTest {

    // Used in tests
    @Suppress("unused")
    data class AModel(val navn: String)

    @Test
    fun `can generate helpers`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest

                    @TemplateModelHelpers
                    object MyClass : HasModel<TemplateModelHelpersAnnotationProcessorTest.AModel> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelSelectors"))))
    }

    @Test
    fun `can generate helpers for class in default package`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest

                    @TemplateModelHelpers
                    object MyClass : HasModel<DefaultPackageModel> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("DefaultPackageModelSelectors"))))
    }

    @Test
    fun `generates expected helpers`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.TemplateModelSelector
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                    import no.nav.pensjon.brev.template.dsl.helpers.AModelSelectors.navn
                    import no.nav.pensjon.brev.template.dsl.helpers.AModelSelectors

                    @TemplateModelHelpers
                    object MyClass : HasModel<TemplateModelHelpersAnnotationProcessorTest.AModel> {
                        fun someusage() {
                            val fromScope: Expression<String> = TemplateGlobalScope<TemplateModelHelpersAnnotationProcessorTest.AModel>().navn
                            val fromOtherExpression: Expression<String> = Expression.Literal(TemplateModelHelpersAnnotationProcessorTest.AModel("jadda")).navn
                            val actualSelector: TemplateModelSelector<TemplateModelHelpersAnnotationProcessorTest.AModel, String> = AModelSelectors.navnSelector
                        }
                    }
                    """.trimIndent()
        ).compile()

        // We use the generated helpers in MyClass above, with type declarations, so if they are not as expected then compilation will fail.
        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
    }


    @Test
    fun `fails for annotated regular class`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

                @TemplateModelHelpers
                class MyClass {}
                """.trimIndent()
        ).compile()


        assertThat(
            result.messages, hasKspErrorMessages("e: [ksp] $unsupportedAnnotationTarget: Annotation $annotationName does not support target class kind CLASS (only supports OBJECT): MyClass")
        )
        assertThat(result.generatedFiles, hasOnlySourceAndNoHelpers())
    }

    @Test
    fun `fails for annotated object that does not extend HasModel`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

                    @TemplateModelHelpers
                    object MyClass {}
                """.trimIndent()
        ).compile()

        assertThat(result.generatedFiles, hasOnlySourceAndNoHelpers())
        assertThat(result.messages, hasKspErrorMessages("e: [ksp] $invalidObjectTarget: $annotationName annotated target OBJECT must extend $hasModelName: MyClass"))
    }

    @Test
    fun `generates helpers for annotated object that extends interface that extends HasModel`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
        
                    interface AnotherInterface<Model2: Any> : HasModel<Model2>


                    @TemplateModelHelpers
                    object MyClass : AnotherInterface<TemplateModelHelpersAnnotationProcessorTest.AModel> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelSelectors"))))
    }

    @Test
    fun `generates helpers for annotated object that has type HasModel`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
        
                    @TemplateModelHelpers
                    object MyClass : HasModel<TemplateModelHelpersAnnotationProcessorTest.AModel> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelSelectors"))))
    }

    @Test
    fun `generates helpers for annotated object that is descendant of HasModel with mixing of type parameters`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
        
                    interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
                    interface AThirdInterface<Unused2, Model3: Any>: AnotherInterface<Model3, String>


                    @TemplateModelHelpers
                    object MyClass : AThirdInterface<Int, TemplateModelHelpersAnnotationProcessorTest.AModel> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelSelectors"))))
    }

    @Test
    fun `generates helpers for annotated object that is descendant of HasModel where super type declare the model`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
        
                    interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
                    interface AThirdInterface<Unused2>: AnotherInterface<TemplateModelHelpersAnnotationProcessorTest.AModel, Unused2>


                    @TemplateModelHelpers
                    object MyClass : AThirdInterface<Int> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelSelectors"))))
    }

    @Test
    fun `generates helpers for annotated property with type that is descendant of HasModel`() {
        val result = SourceFile.kotlin(
            "MyFile.kt", """
                import no.nav.pensjon.brev.template.HasModel
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                
                interface SimpleAttachment<T: Any>: HasModel<T>

                @TemplateModelHelpers
                val anAttachment: SimpleAttachment<SimpleModel> = object : SimpleAttachment<SimpleModel> {}
            """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("SimpleModelSelectors"))))
    }

    @Test
    fun `generates helpers for annotated property with type that is descendant of HasModel where super type declare the model`() {
        val result = SourceFile.kotlin(
            "MyFile.kt", """
                import no.nav.pensjon.brev.template.HasModel
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                import no.nav.pensjon.brev.template.thirdpkg.SimpleModel

                interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
                interface AThirdInterface<Unused2>: AnotherInterface<TemplateModelHelpersAnnotationProcessorTest.AModel, Unused2>

                @TemplateModelHelpers
                val anAttachment: AThirdInterface<String> = object : AThirdInterface<String> {}
            """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelSelectors"))))
    }

    @Test
    fun `generates helpers for annotated property with type HasModel`() {
        val result = SourceFile.kotlin(
            "MyFile.kt", """
                import no.nav.pensjon.brev.template.HasModel
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                
                @TemplateModelHelpers
                val anAttachment: HasModel<SimpleModel> = object : HasModel<SimpleModel> {}
            """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("SimpleModelSelectors"))))
    }

    @Test
    fun `generates helper for data class type argument of List`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModelSelectors.name

                    @TemplateModelHelpers
                    object MyClass : HasModel<List<SimpleModel>> {
                        val x: Expression<String> = Expression.Literal(SimpleModel("et navn")).name
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("SimpleModelSelectors"))))
    }

    @Test
    fun `generates helpers for multiple nested levels of models`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import FirstSelectors.second
                    import SecondSelectors.third
                    import ThirdSelectors.fourth
                    import FourthSelectors.value
    
                    data class First(val second: Second)
                    data class Second(val third: Third)
                    data class Third(val fourth: Fourth)
                    data class Fourth(val value: Int)

                    @TemplateModelHelpers
                    object MyClass : HasModel<First> {
                        val expr: Expression<First> = Expression.Literal(First(Second(Third(Fourth(99)))))
                        val value: Expression<Int> = expr.second.third.fourth.value
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        assertThat(result.generatedFiles, hasSize(greaterThan(6)))
        assertThat(
            result.generatedFiles, allOf(
                anyElement(has(File::getName, containsSubstring("FirstSelectors"))),
                anyElement(has(File::getName, containsSubstring("SecondSelectors"))),
                anyElement(has(File::getName, containsSubstring("ThirdSelectors"))),
                anyElement(has(File::getName, containsSubstring("FourthSelectors"))),
            )
        )
    }

}