package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator

sealed class Phrase {
    val type: String = this::class.java.name
    abstract fun text(): String

    companion object {
        @JsonCreator
        @JvmStatic
        fun creator(type: String): Phrase? =
            Phrase::class.findSealedObjects().firstOrNull { it.type == type }
    }

    data class Static(val name: String, val text: String) : Phrase() {
        override fun text() = text
    }
}
