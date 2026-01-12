package no.nav.pensjon.brev.template

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
