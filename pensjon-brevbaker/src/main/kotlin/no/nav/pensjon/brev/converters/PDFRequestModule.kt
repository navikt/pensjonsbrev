package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestImpl
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescriptionImpl
import no.nav.pensjon.brev.pdfbygger.FellesDeserializer

@OptIn(InterneDataklasser::class)
object PDFRequestModule : SimpleModule() {
    private fun readResolve(): Any = PDFRequestModule

    init {
        addDeserializer(
            PDFRequest::class.java,
            object : FellesDeserializer<PDFRequest, PDFRequestImpl>(PDFRequestImpl::class.java) {})
        addDeserializer(TemplateDescription.Autobrev::class.java, TemplateDescriptionAutobrevDeserializer)
        addDeserializer(TemplateDescription.Redigerbar::class.java, TemplateDescriptionRedigerbarDeserializer)
    }

    private object TemplateDescriptionAutobrevDeserializer :
        FellesDeserializer<TemplateDescription.Autobrev, TemplateDescriptionImpl.AutobrevImpl>(TemplateDescriptionImpl.AutobrevImpl::class.java)

    private object TemplateDescriptionRedigerbarDeserializer :
        FellesDeserializer<TemplateDescription.Redigerbar, TemplateDescriptionImpl.RedigerbarImpl>(
            TemplateDescriptionImpl.RedigerbarImpl::class.java)
}