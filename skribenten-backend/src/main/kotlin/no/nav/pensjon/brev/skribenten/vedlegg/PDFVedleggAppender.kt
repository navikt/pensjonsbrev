package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.P1_BREVKODE
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.PDFMerger
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import org.apache.pdfbox.pdmodel.PDDocument

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        vedlegg: List<() -> PDDocument>,
    ): ByteArray
}

class PDFVedleggAppenderImpl : PDFVedleggAppender {
    override fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        vedlegg: List<() -> PDDocument>,
    ): ByteArray = PDFMerger.merge(pdfCompilationOutput, vedlegg)
}

val vedleggsliste = mapOf(
    P1_BREVKODE to listOf(
        PDFVedleggSkribenten(
                mapOf(
                    LanguageCode.BOKMAL to "P1 – Samlet melding om pensjonsvedtak",
                    LanguageCode.ENGLISH to "P1 – Summary of Pension Decisions"
                )
        ) { data ->
            (data.brevdata["p1Vedlegg"] as? P1RedigerbarDto)?.let { dto ->
                P1pdfV2Dto.create(dto, data.felles)
            }
        },
        PDFVedleggSkribenten(
                mapOf(
                    LanguageCode.BOKMAL to "Informasjon om skjemaet P1 og hvordan det brukes",
                    LanguageCode.ENGLISH to "Information about the P1 form and its use"
                )
        ) { PDFVedlegg().apply { side("InformasjonOmP1") {} } },
    )
)
data class PDFVedleggSkribenten(val tittel: Map<LanguageCode, String>, val vedlegg: (data: BrevdataResponse.Data) -> PDFVedlegg?)