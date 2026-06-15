package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.api.model.TemplateDescription

@JvmInline
value class SaksId(val id: Long) {
    override fun toString() = id.toString()
}

@JvmInline
value class Sakstype(override val kode: String) : TemplateDescription.ISakstype {
    override fun toString() = kode
}