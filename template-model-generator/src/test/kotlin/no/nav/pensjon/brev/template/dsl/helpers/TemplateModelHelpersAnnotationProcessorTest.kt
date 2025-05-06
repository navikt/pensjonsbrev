package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.impl.KotlinSymbolProcessing
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private fun hasSelectorFile(name: String) =
    anyElement(has(KotlinSourceFile::name, equalTo("${name}Selectors.kt")))

class TemplateModelHelpersAnnotationProcessorTest {

    @Test
    fun `HasModel typeparameter name matches name from reflection`() {
        // To avoid exposing a transitive dependency on kotlin-reflect just to find the name of this type-parameter, we have it set as constant.
        // Thus, we need this test to verify that constant actually matches the name from source.
        assertEquals(HasModel::class.typeParameters.first().name, HAS_MODEL_TYPE_PARAMETER_NAME)
    }

    @Test
    fun `fails for annotated regular class`() {
        val result = assertThrows<UnsupportedAnnotationTarget> {
            KotlinSourceFile(
                "MyClass.kt", """
                import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                import no.nav.brev.InternKonstruktoer

                @TemplateModelHelpers
                @OptIn(InternKonstruktoer::class)
                class MyClass {}
                """.trimIndent()
            ).generateSelectors()
        }
        assertThat(result.msg, containsSubstring("@$ANNOTATION_NAME does not support target class kind CLASS (only supports OBJECT): MyClass"))
    }

    @Test
    fun `fails for annotated object that does not extend HasModel`() {
        val result = assertThrows<InvalidObjectTarget> {
            KotlinSourceFile(
                "MyClass.kt", """
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.brev.InternKonstruktoer

                    @TemplateModelHelpers
                    @OptIn(InternKonstruktoer::class)
                    object MyClass {}
                """.trimIndent()
            ).generateSelectors()
        }

        assertThat(result.msg, containsSubstring("@$ANNOTATION_NAME annotated target OBJECT must extend $HAS_MODEL_INTERFACE_NAME"))
    }

    @Test
    fun `generates helpers for models referenced in submodel`() {
        val result = KotlinSourceFile(
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
        ).generateSelectors()

        assertThat(result.exitCode, equalTo(KotlinSymbolProcessing.ExitCode.OK))
        assertThat(
            result.generatedSources, allOf(
                hasSelectorFile("ParentModel"),
                hasSelectorFile("UncleModel"),
            )
        )
    }


    @Test
    fun `referencing a model in another nested model should result in complete hierarchy and not a top-level selector`() {
        val result = KotlinSourceFile(
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
        ).generateSelectors()

        assertThat(result.exitCode, equalTo(KotlinSymbolProcessing.ExitCode.OK))
        assertThat(
            result.generatedSources, allOf(
                hasSelectorFile("AMother"),
                hasSelectorFile("ParentModel"),
                hasSelectorFile("UncleModel").not(),
                hasSelectorFile("ChildModel").not(),
            )
        )
    }

    @Test
    fun `ignores model that is from standard library`() {
        val result = KotlinSourceFile(
            "MyClass.kt", """
                    import no.nav.pensjon.brev.template.HasModel
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
                    import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpersAnnotationProcessorTest

                    @TemplateModelHelpers
                    object MyClass : HasModel<List<String>> {}
                    """.trimIndent()
        ).generateSelectors()

        assertThat(result.exitCode, equalTo(KotlinSymbolProcessing.ExitCode.OK))
        // Neither List nor String should get generators
        assertThat(result.generatedSources, isEmpty)
    }
}