package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.api.model.IBrevkategori
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.reflect.KProperty1

class SimpleSelector<Model : Any, Property>(override val className: String, property: KProperty1<Model, Property>) :
    TemplateModelSelector<Model, Property> {
    override val propertyName: String = property.name
    override val propertyType: String = property.returnType.toString()
    override val selector: Model.() -> Property = property.getter::call

    companion object {
        inline operator fun <reified Model : Any, Property> invoke(property: KProperty1<Model, Property>): SimpleSelector<Model, Property> =
            SimpleSelector(Model::class.simpleName ?: "Unknown class", property)
    }
}

fun redigerbarMal(init: TemplateRootScope<LangBokmal, RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata>.() -> Unit) =
    object : RedigerbarTemplate<RedigerbarBrevdataMedSaksbehandlerValgUtenFagsystemdata> {
        override val kategori: IBrevkategori get() = TODO("Not yet implemented")
        override val brevkontekst: TemplateDescription.Brevkontekst get() = TODO("Not yet implemented")
        override val sakstyper: Set<ISakstype> get() = TODO("Not yet implemented")
        override val kode: Brevkode.Redigerbart get() = TODO("Not yet implemented")

        override val template = createTemplate(
            languages = languages(Language.Bokmal),
            letterMetadata = LetterMetadata(
                displayTitle = "Dette er et redigerbart eksempel-brev", // Display title for external systems
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
            init = init,
        )
    }

fun autobrevMal(init: TemplateRootScope<LangBokmal, EmptyAutobrevdata>.() -> Unit) =
    object : AutobrevTemplate<EmptyAutobrevdata> {
        override val kode: Brevkode.Automatisk get() = TODO("Not yet implemented")

        override val template = createTemplate(
            languages = languages(Language.Bokmal),
            letterMetadata = LetterMetadata(
                displayTitle = "Dette er et automatisk eksempel-brev", // Display title for external systems
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
            init = init,
        )
    }