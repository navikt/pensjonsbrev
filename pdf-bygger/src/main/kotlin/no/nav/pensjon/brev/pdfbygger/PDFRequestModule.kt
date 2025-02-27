package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestImpl
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescriptionImpl

@OptIn(InterneDataklasser::class)
object PDFRequestModule : SimpleModule() {
    private fun readResolve(): Any = PDFRequestModule

    init {
        addDeserializer(PDFRequest::class.java, PDFRequestDeserializer)
        addDeserializer(TemplateDescription.Autobrev::class.java, TemplateDescriptionAutobrevDeserializer)
        addDeserializer(TemplateDescription.Redigerbar::class.java, TemplateDescriptionRedigerbarDeserializer)
    }

    private object PDFRequestDeserializer : FellesDeserializer<PDFRequest, PDFRequestImpl>(PDFRequestImpl::class.java)

    private object TemplateDescriptionAutobrevDeserializer :
        FellesDeserializer<TemplateDescription.Autobrev, TemplateDescriptionImpl.AutobrevImpl>(TemplateDescriptionImpl.AutobrevImpl::class.java)

    private object TemplateDescriptionRedigerbarDeserializer :
        FellesDeserializer<TemplateDescription.Redigerbar, TemplateDescriptionImpl.RedigerbarImpl>(TemplateDescriptionImpl.RedigerbarImpl::class.java)
}