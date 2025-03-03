package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFCompilationOutputImpl
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestImpl
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescriptionImpl
import no.nav.pensjon.brev.pdfbygger.FellesDeserializer
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadataImpl

@OptIn(InterneDataklasser::class)
object PDFRequestModule : SimpleModule() {
    private fun readResolve(): Any = PDFRequestModule

    init {
        addDeserializer(
            PDFRequest::class.java,
            object : FellesDeserializer<PDFRequest, PDFRequestImpl>(PDFRequestImpl::class.java) {})
        addDeserializer(TemplateDescription.Autobrev::class.java, TemplateDescriptionAutobrevDeserializer)
        addDeserializer(TemplateDescription.Redigerbar::class.java, TemplateDescriptionRedigerbarDeserializer)
        addDeserializer(LetterMetadata::class.java, LetterMetadataDeserializer)
        addDeserializer(PDFCompilationOutput::class.java, PDFCompilationOutputDeserializer)
    }

    private object TemplateDescriptionAutobrevDeserializer :
        FellesDeserializer<TemplateDescription.Autobrev, TemplateDescriptionImpl.AutobrevImpl>(TemplateDescriptionImpl.AutobrevImpl::class.java)

    private object TemplateDescriptionRedigerbarDeserializer :
        FellesDeserializer<TemplateDescription.Redigerbar, TemplateDescriptionImpl.RedigerbarImpl>(
            TemplateDescriptionImpl.RedigerbarImpl::class.java)

    private object LetterMetadataDeserializer :
            FellesDeserializer<LetterMetadata, LetterMetadataImpl>(LetterMetadataImpl::class.java)

    private object PDFCompilationOutputDeserializer :
            FellesDeserializer<PDFCompilationOutput, PDFCompilationOutputImpl>(PDFCompilationOutputImpl::class.java)
}