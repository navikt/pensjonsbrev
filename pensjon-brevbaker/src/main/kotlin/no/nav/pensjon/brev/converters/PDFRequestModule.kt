package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestImpl
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescriptionImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadataImpl

@OptIn(InterneDataklasser::class)
object PDFRequestModule : SimpleModule() {
    private fun readResolve(): Any = PDFRequestModule

    init {
        addInterfaceDeserializer<PDFRequest, PDFRequestImpl>()
        addInterfaceDeserializer<TemplateDescription.Autobrev, TemplateDescriptionImpl.AutobrevImpl>()
        addInterfaceDeserializer<TemplateDescription.Redigerbar, TemplateDescriptionImpl.RedigerbarImpl>()
        addInterfaceDeserializer<LetterMetadata, LetterMetadataImpl>()
    }
}