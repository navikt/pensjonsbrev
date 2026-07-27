package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.P1_BREVKODE
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.PDFMerger
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import org.apache.commons.codec.language.bm.Lang

interface PDFVedleggAppender {
    fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): ByteArray
}

class PDFVedleggAppenderImpl : PDFVedleggAppender {
    override fun leggPaaVedlegg(
        pdfCompilationOutput: ByteArray,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): ByteArray = PDFMerger.merge(pdfCompilationOutput, attachments.map { { SideAppender.lesInnPDF(it.sider, spraak) { spraak, side -> "/vedlegg/${side.filnavn}-${spraak.name}" } } })
}

val vedleggsliste = mapOf(
    P1_BREVKODE to listOf(
        mapOf(
            LanguageCode.BOKMAL to "Informasjon om skjemaet P1 og hvordan det brukes",
            LanguageCode.ENGLISH to "Information about the P1 form and its use",
        ),
        mapOf(
            LanguageCode.BOKMAL to "P1 – Samlet melding om pensjonsvedtak",
            LanguageCode.ENGLISH to "P1 – Summary of Pension Decisions",
        )
    )
)