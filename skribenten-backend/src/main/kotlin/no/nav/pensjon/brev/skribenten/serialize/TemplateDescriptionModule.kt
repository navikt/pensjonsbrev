package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.model.BrevmalOverstyring.kategori
import no.nav.pensjon.brev.skribenten.services.addAbstractTypeMapping

object TemplateDescriptionModule : SimpleModule() {
    private fun readResolve(): Any = TemplateDescriptionModule

    init {
        addAbstractTypeMapping<TemplateDescription.IBrevkategori, Brevkategori>()
    }
}

@JvmInline
value class Brevkategori(override val kode: String) : TemplateDescription.IBrevkategori