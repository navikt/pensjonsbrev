package no.nav.pensjon.brev.template

interface TemplateModelSelector<Model : Any, Property> : StableHash {
    val className: String
    val propertyName: String
    val propertyType: String
    val selector: Model.() -> Property

    override fun stableHashCode(): Int {
        var result = className.hashCode()
        result = 31 * result + propertyName.hashCode()
        result = 31 * result + propertyType.hashCode()
        return result
    }
}