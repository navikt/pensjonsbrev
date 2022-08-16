package no.nav.pensjon.brev.template

interface TemplateModelSelector<Model: Any, Property> {
    val className: String
    val propertyName: String
    val propertyType: String
    val selector: Model.() -> Property
}