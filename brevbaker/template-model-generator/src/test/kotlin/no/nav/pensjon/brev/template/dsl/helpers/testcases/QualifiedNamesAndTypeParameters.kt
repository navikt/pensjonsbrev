package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.otherpkg.QualifiedNamesAndTypeParameters_Model2
import no.nav.pensjon.brev.template.somepkg.QualifiedNamesAndTypeParameters_Model1
import no.nav.pensjon.brev.template.somepkg.QualifiedNamesAndTypeParameters_Model1Selectors.thing
import no.nav.pensjon.brev.template.thirdpkg.QualifiedNamesAndTypeParameters_Model3

/**
 * Verify that selectors are generated for models with (nested) type parameters that cross
 * multiple packages, i.e. confirm that qualified names are used (or imports are added).
 *
 * TODO: This is not actually supported yet. The generator will stop at properties of a type with type-parameters.
 *       Specifically TemplateModelVisitor:findModel only accepts empty type-parameters and iterable-types (List<T> etc.)
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
object QualifiedNamesAndTypeParameters : HasModel<QualifiedNamesAndTypeParameters_Model1> {
    fun doSomething() {
        val model2: Expression<QualifiedNamesAndTypeParameters_Model2<QualifiedNamesAndTypeParameters_Model3<QualifiedNamesAndTypeParameters_Model1.ActualModel>>> =
            SimpleTemplateScope<QualifiedNamesAndTypeParameters_Model1>().thing

        // TODO: Include these when properties of types with type-parameters
//        val model3: Expression<QualifiedNamesAndTypeParameters_Model3<QualifiedNamesAndTypeParameters_Model1.ActualModel>> = model2.value
//        val simpleModel: Expression<SimpleModel> = model3.value
    }
}