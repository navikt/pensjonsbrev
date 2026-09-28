package no.nav.pensjon.brev.template.dsl.helpers.testcases

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.selectors.redigerbarBrevdata.pesysData
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.dsl.helpers.SimpleTemplateScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.helpers.testcases.selectors.redigerbarBrevdataAsModel.pesysData.*
import no.nav.pensjon.brev.template.dsl.helpers.testcases.selectors.redigerbarBrevdataAsModel.sak.*

/**
 * Verify that a template which uses RedigerbarBrevdata directly as its model gets selectors
 * generated for the Data type-argument (and its nested models), and that the hand-written
 * selectors for RedigerbarBrevdata in brevbaker:dsl provide `pesysData`.
 *
 * If it does not compile then the test has failed.
 */
@TemplateModelHelpers
@OptIn(InternKonstruktoer::class)
object RedigerbarBrevdataAsModel : HasModel<RedigerbarBrevdata<RedigerbarBrevdataAsModel.PesysData>> {
    data class PesysData(val navn: String, val sak: Sak) : FagsystemBrevdata
    data class Sak(val nummer: Int)

    private val scope = SimpleTemplateScope<RedigerbarBrevdata<PesysData>>()

    fun fraScope() {
        val pesysData: Expression<PesysData> = scope.pesysData
        val navn: Expression<String> = pesysData.navn
        val nummer: Expression<Int> = pesysData.sak.nummer
    }

    fun fraExpression() {
        val argument: Expression<RedigerbarBrevdata<PesysData>> = scope.argument
        val navn: Expression<String> = argument.pesysData.navn
    }
}
