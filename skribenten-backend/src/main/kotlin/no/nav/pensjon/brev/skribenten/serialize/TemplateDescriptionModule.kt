package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.services.addAbstractTypeMapping

object TemplateDescriptionModule : SimpleModule() {
    private fun readResolve(): Any = TemplateDescriptionModule

    init {
        addAbstractTypeMapping<TemplateDescription.IBrevkategori, Brevkategori>()
    }
}

@JvmInline
value class Brevkategori(val kategori: String) : TemplateDescription.IBrevkategori {
    override fun kode(): String = kategori
}