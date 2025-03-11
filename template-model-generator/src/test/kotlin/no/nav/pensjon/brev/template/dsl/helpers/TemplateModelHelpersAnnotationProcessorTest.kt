package no.nav.pensjon.brev.template.dsl.helpers

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.*
import com.tschuchort.compiletesting.*
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
    @Suppress("unused")
    interface AModelInterface{
        val fornavn: String
    }

    @Test
    fun `can generate helpers`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.brev.InternKonstruktoer

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<TemplateModelHelpersAnnotationProcessorTest.AModel> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelSelectors"))))
    }


    @Test
    fun `can generate helpers for interface model`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.brev.InternKonstruktoer

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<TemplateModelHelpersAnnotationProcessorTest.AModelInterface> {}
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        // If the processor didn't generate code, then we should have two files (MyClass and module file)
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("AModelInterfaceSelectors"))))
    }

    @Test
    fun `can generate helpers for class in default package`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.brev.InternKonstruktoer

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
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
                    import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTestSelectors.AModelSelectors.navn
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTestSelectors.AModelSelectors.navnSelector
                    import no.nav.brev.InternKonstruktoer

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<TemplateModelHelpersAnnotationProcessorTest.AModel> {
                        fun someusage() {
                            val fromScope: Expression<String> = SimpleTemplateScope<TemplateModelHelpersAnnotationProcessorTest.AModel>().navn
                            val fromOtherExpression: Expression<String> = Expression.Literal(TemplateModelHelpersAnnotationProcessorTest.AModel("jadda")).navn
                            val actualSelector: TemplateModelSelector<TemplateModelHelpersAnnotationProcessorTest.AModel, String> = navnSelector
                        }
                    }
                    """.trimIndent(),
        ).compile()

        // We use the generated helpers in MyClass above, with type declarations, so if they are not as expected then compilation will fail.
        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
    }


    @Test
    fun `fails for annotated regular class`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                import no.nav.brev.InternKonstruktoer

                @TemplateModelHelpers
                @OptIn(InternKonstruktoer::class)
                class MyClass {}
                """.trimIndent()
        ).compile()


        assertThat(
            result.messages, hasKspErrorMessages("$unsupportedAnnotationTarget: @$annotationName does not support target class kind CLASS (only supports OBJECT): MyClass")
        )
        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.INTERNAL_ERROR))
    }

    @Test
    fun `fails for annotated object that does not extend HasModel`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.brev.InternKonstruktoer

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass {}
                """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.INTERNAL_ERROR))
        assertThat(result.messages, hasKspErrorMessages("$invalidObjectTarget: @$annotationName annotated target OBJECT must extend $hasModelName"))
    }

    @Test
    fun `generates helpers for annotated object that extends interface that extends HasModel`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.brev.InternKonstruktoer
        
                    interface AnotherInterface<Model2: Any> : HasModel<Model2>


                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
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
                    import no.nav.brev.InternKonstruktoer
        
                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
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
                    import no.nav.brev.InternKonstruktoer

                    interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
                    interface AThirdInterface<Unused2, Model3: Any>: AnotherInterface<Model3, String>


                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
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
                    import no.nav.brev.InternKonstruktoer
        
                    interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
                    interface AThirdInterface<Unused2>: AnotherInterface<TemplateModelHelpersAnnotationProcessorTest.AModel, Unused2>


                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
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
                import no.nav.brev.InternKonstruktoer

                interface SimpleAttachment<T: Any>: HasModel<T>

                @TemplateModelHelpers
                @OptIn(InternKonstruktoer::class)
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
                import no.nav.brev.InternKonstruktoer

                interface AnotherInterface<Model2: Any, Unused> : HasModel<Model2>
                interface AThirdInterface<Unused2>: AnotherInterface<TemplateModelHelpersAnnotationProcessorTest.AModel, Unused2>

                @TemplateModelHelpers
                @OptIn(InternKonstruktoer::class)
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
                import no.nav.brev.InternKonstruktoer
                
                @TemplateModelHelpers
                @OptIn(InternKonstruktoer::class)
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
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModelSelectors.name
                    import no.nav.brev.InternKonstruktoer

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<List<SimpleModel>> {
                        val x: Expression<String> = Expression.Literal(SimpleModel("et navn")).name
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        assertThat(result.generatedFiles, hasSize(greaterThan(2)) and anyElement(has(File::getName, containsSubstring("SimpleModelSelectors"))))
    }

    @Test
    fun `generates helpers for multiple levels of models`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import FirstSelectors.second
                    import SecondSelectors.third
                    import ThirdSelectors.fourth
                    import FourthSelectors.value
                    import no.nav.brev.InternKonstruktoer
    
                    data class First(val second: Second)
                    data class Second(val third: Third)
                    data class Third(val fourth: Fourth)
                    data class Fourth(val value: Int)

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
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

    @Test
    fun `generates helpers for additionalModels`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import AnotherModelSelectors.age
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModelSelectors.name
                    import no.nav.brev.InternKonstruktoer

                    data class AnotherModel(val age: Int)

                    @TemplateModelHelpers([AnotherModel::class])
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<List<SimpleModel>> {
                        val x: Expression<Int> = Expression.Literal(AnotherModel(35)).age
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        assertThat(result.generatedFiles, hasSize(greaterThan(3)) and anyElement(has(File::getName, containsSubstring("AnotherModelSelectors"))))
    }

    @Test
    fun `generates helpers for additionalModels when target is interface`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import AnotherModelSelectors.age
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest
                    import no.nav.brev.InternKonstruktoer

                    data class AnotherModel(val age: Int)

                    @TemplateModelHelpers([AnotherModel::class])
                    @OptIn(InternKonstruktoer::class)
                    interface MyInterface 
                    @OptIn(InternKonstruktoer::class)
                    val x: Expression<Int> = Expression.Literal(AnotherModel(35)).age
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        assertThat(result.generatedFiles, hasSize(greaterThan(3)) and anyElement(has(File::getName, containsSubstring("AnotherModelSelectors"))))
    }


    @Test
    fun `generates helpers for models referenced in submodel`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                    import ParentModelSelectors.child
                    import ParentModelSelectors.ChildModelSelectors.uncle
                    import UncleModelSelectors.name
                    import no.nav.brev.InternKonstruktoer
                    
                    data class UncleModel(val name: String)
            
                    data class ParentModel(val child: ChildModel) {
                        data class ChildModel(val uncle: UncleModel)                    
                    }

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<ParentModel> {
                        val data: Expression<ParentModel> = Expression.Literal(ParentModel(ParentModel.ChildModel(UncleModel("Scrooge"))))
                        val child: Expression<ParentModel.ChildModel> = data.child
                        val uncleName: Expression<String> = child.uncle.name
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        assertThat(result.generatedFiles, allOf(
            hasSize(greaterThan(3)),
            anyElement(has(File::getName, containsSubstring("ParentModelSelectors"))),
            anyElement(has(File::getName, containsSubstring("UncleModelSelectors"))),
        ))
    }

    @Test
    fun `does not fail when a model is referenced in a nested model and is used in another place`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                    import ParentModelSelectors.child
                    import ParentModelSelectors.ChildModelSelectors.uncle
                    import UncleModelSelectors.name
                    import no.nav.brev.InternKonstruktoer

                    data class UncleModel(val name: String)
            
                    data class ParentModel(val child: ChildModel) {
                        data class ChildModel(val uncle: UncleModel)                    
                    }

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<ParentModel> {
                        val data: Expression<ParentModel> = Expression.Literal(ParentModel(ParentModel.ChildModel(UncleModel("Scrooge"))))
                        val child: Expression<ParentModel.ChildModel> = data.child
                        val uncleName: Expression<String> = child.uncle.name
                    }
                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object ReUse : HasModel<UncleModel> {
                        val uncleName: Expression<String> = Expression.Literal(UncleModel("Scrooge")).name
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
    }

    @Test
    fun `referencing a model in another nested model should result in complete hierarchy and not a top-level selector`() {
        val result = SourceFile.kotlin(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.thirdpkg.SimpleModel
                    import ParentModelSelectors.child
                    import ParentModelSelectors.ChildModelSelectors.uncle
                    import AMotherSelectors.UncleModelSelectors.name
                    import no.nav.brev.InternKonstruktoer
                    
                    data class AMother(val child: UncleModel) {
                        data class UncleModel(val name: String)
                    }
            
                    data class ParentModel(val child: ChildModel) {
                        data class ChildModel(val uncle: AMother.UncleModel)                    
                    }

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass : HasModel<ParentModel> {
                        val data: Expression<ParentModel> = Expression.Literal(ParentModel(ParentModel.ChildModel(AMother.UncleModel("Scrooge"))))
                        val child: Expression<ParentModel.ChildModel> = data.child
                        val uncleName: Expression<String> = child.uncle.name
                    }
                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object ReUse : HasModel<AMother.UncleModel> {
                        val uncleName: Expression<String> = Expression.Literal(AMother.UncleModel("Scrooge")).name
                    }
                    """.trimIndent()
        ).compile()

        assertThat(result.exitCode, equalTo(KotlinCompilation.ExitCode.OK))
        assertThat(result.generatedFiles, allOf(
            anyElement(has(File::getName, startsWith("AMotherSelectors\$UncleModelSelectors"))),
            allElements(has(File::getName, startsWith("UncleModelSelectors").not()))
        ))
    }
}