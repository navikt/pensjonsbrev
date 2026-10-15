@file:OptIn(InternKonstruktoer::class)

package no.nav.pensjon.brev.api.model.maler.selectors.redigerbarBrevdata

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

// Håndskrevet fordi RedigerbarBrevdata er generisk: KSP-generatoren lager én selectors-fil per
// klasse-deklarasjon og kan derfor ikke substituere Data per mal. Pakken etterligner konvensjonen
// for genererte selectors, slik at importen ser lik ut for malforfattere.
private object PesysDataSelector : TemplateModelSelector<RedigerbarBrevdata<FagsystemBrevdata>, FagsystemBrevdata> {
    override val className: String = RedigerbarBrevdata::class.qualifiedName!!
    override val propertyName: String = "pesysData"

    // Bounden, ikke den konkrete Data-typen: en extension property kan ikke ha reified typeparameter.
    override val propertyType: String = FagsystemBrevdata::class.qualifiedName!!
    override val selector: RedigerbarBrevdata<FagsystemBrevdata>.() -> FagsystemBrevdata = { pesysData }
}

@Suppress("UNCHECKED_CAST")
private fun <Data : FagsystemBrevdata> pesysDataSelector(): TemplateModelSelector<RedigerbarBrevdata<Data>, Data> =
    PesysDataSelector as TemplateModelSelector<RedigerbarBrevdata<Data>, Data>

val <Data : FagsystemBrevdata> TemplateGlobalScope<RedigerbarBrevdata<Data>>.pesysData: Expression<Data>
    get() = Expression.UnaryInvoke(
        Expression.FromScope.Argument(),
        UnaryOperation.Select(pesysDataSelector())
    )

val <Data : FagsystemBrevdata> Expression<RedigerbarBrevdata<Data>>.pesysData: Expression<Data>
    get() = Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(pesysDataSelector())
    )
