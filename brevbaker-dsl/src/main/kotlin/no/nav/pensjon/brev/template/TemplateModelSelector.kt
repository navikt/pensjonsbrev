package no.nav.pensjon.brev.template

interface TemplateModelSelector<Model : Any, Property> {
    val className: String
    val propertyName: String
    val propertyType: String
    val selector: Model.() -> Property

    fun stableHashCode(): Int {
        var result = className.hashCode()
        result = 31 * result + propertyName.hashCode()
        result = 31 * result + propertyType.hashCode()
        return result
    }
}
