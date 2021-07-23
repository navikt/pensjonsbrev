package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.OutputStream

@JsonIgnoreProperties("parameters")
abstract class BaseTemplate {
    val name: String = this::class.java.name

    abstract val parameters: Set<TemplateParameter>
    abstract fun render(letter: Letter, out: OutputStream)

    companion object {
        @JsonCreator
        @JvmStatic
        fun creator(name: String?): BaseTemplate? {
            return BaseTemplate::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .firstOrNull { it.name == name }
        }
    }

}
