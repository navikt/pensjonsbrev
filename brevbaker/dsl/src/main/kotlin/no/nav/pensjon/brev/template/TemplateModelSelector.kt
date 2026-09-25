package no.nav.pensjon.brev.template

import java.util.Objects

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

class SimpleSelector<Model : Any, Property>(
    override val className: String,
    override val propertyName: String,
    override val propertyType: String,
    override val selector: Model.() -> Property,
) : TemplateModelSelector<Model, Property> {
    override fun equals(other: Any?) = other is SimpleSelector<*, *> &&
            className == other.className &&
            propertyName == other.propertyName &&
            propertyType == other.propertyType &&
            selector == other.selector

    override fun hashCode() = Objects.hash(className, propertyName, propertyType, selector)
    override fun toString(): String = "Vedlegg($className, $propertyName, $propertyType, $selector)"
}
