package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

/**
 * Håndskrevet bro for `.pesysData`-tilgang i [RedigerbarTemplate]-maler.
 *
 * KSP (`@TemplateModelHelpers`) kan ikke generere denne selv: [RedigerbarTemplate]s Model-type er alltid
 * `RedigerbarBrevdata<FagData>`, og kodegeneratoren besøker `RedigerbarBrevdata`s egen (usubstituerte)
 * klassedeklarasjon for å finne dens felter - der er `pesysData` fortsatt en uløst generisk
 * typeparameter (`Data`), ikke den konkrete Pesys-dataklassen malen faktisk bruker. Samme grunn som
 * `saksbehandlerValg` er håndskrevet (se Saksbehandlervalg.kt). I motsetning til `saksbehandlerValg`,
 * som har fast type (SaksbehandlervalgIDSL), varierer `pesysData` sin type per mal, så selectoren
 * bygges med en reifisert typeparameter for at `propertyType` skal bli den faktiske Pesys-typen
 * (viktig for dokumentasjonsgenerering i TemplateDocumentationRendererV2Expr).
 *
 * KSP genererer fortsatt selectors for feltene *på* den konkrete Pesys-dataklassen selv (f.eks.
 * `pesysData.kravMottattDato`) - det er kun broen fra `RedigerbarBrevdata<Data>` til `Data` som må
 * håndskrives.
 */
@PublishedApi
internal inline fun <reified Data : FagsystemBrevdata> pesysDataSelector(): TemplateModelSelector<RedigerbarBrevdata<Data>, Data> =
    object : TemplateModelSelector<RedigerbarBrevdata<Data>, Data> {
        override val className: String = RedigerbarBrevdata::class.qualifiedName!!
        override val propertyName: String = "pesysData"
        override val propertyType: String = Data::class.qualifiedName!!
        override val selector: RedigerbarBrevdata<Data>.() -> Data = RedigerbarBrevdata<Data>::pesysData
    }

inline val <reified Data : FagsystemBrevdata> TemplateGlobalScope<RedigerbarBrevdata<Data>>.pesysData: Expression<Data>
    get() = Expression.UnaryInvoke(argument, UnaryOperation.Select(pesysDataSelector()))

inline val <reified Data : FagsystemBrevdata> Expression<RedigerbarBrevdata<Data>>.pesysData: Expression<Data>
    get() = Expression.UnaryInvoke(this, UnaryOperation.Select(pesysDataSelector()))
