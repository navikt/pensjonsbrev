package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class PDFVedleggAppenderTest {

    private val appender: PDFVedleggAppender = PDFVedleggAppenderImpl()

    private fun pdfWithPages(antallSider: Int): ByteArray =
        PDDocument().use { doc ->
            repeat(antallSider) { doc.addPage(PDPage()) }
            val outputStream = ByteArrayOutputStream()
            doc.save(outputStream)
            outputStream.toByteArray()
        }

    private fun antallSider(pdf: ByteArray): Int =
        Loader.loadPDF(pdf).use { it.pages.count }

    /** Faktisk sideantall for de innebygde vedleggs-fixturene, brukt til å regne ut forventet resultat. */
    private fun antallSiderIVedlegg(filnavn: String, spraak: LanguageCode): Int =
        javaClass.getResourceAsStream("/vedlegg/${filnavn}-${spraak.name}.pdf")!!.use {
            Loader.loadPDF(it.readAllBytes()).use { doc -> doc.pages.count }
        }

    private fun vedleggMedFilnavn(filnavn: String): PDFVedlegg =
        PDFVedlegg().apply { side(filnavn) {} }

    @Test
    fun `returnerer originalt dokument uendret naar det ikke er noen vedlegg`() {
        val original = pdfWithPages(2)

        val resultat = appender.leggPaaVedlegg(original, emptyList(), LanguageCode.BOKMAL)

        assertThat(resultat).isSameAs(original)
    }

    @Test
    fun `legger til vedlegg etter det originale dokumentet`() {
        val original = pdfWithPages(1)
        val vedleggSideantall = antallSiderIVedlegg("InformasjonOmP1", LanguageCode.BOKMAL)

        val resultat = appender.leggPaaVedlegg(original, listOf(vedleggMedFilnavn("InformasjonOmP1")), LanguageCode.BOKMAL)

        val forventetBlankSide = if (vedleggSideantall % 2 == 1) 1 else 0
        assertThat(antallSider(resultat)).isEqualTo(1 + vedleggSideantall + forventetBlankSide)
    }

    @Test
    fun `legger til blank side foran vedlegg med oddetall sider`() {
        val original = pdfWithPages(1)
        val vedleggSideantall = antallSiderIVedlegg("InformasjonOmP1", LanguageCode.BOKMAL)

        val resultat = appender.leggPaaVedlegg(original, listOf(vedleggMedFilnavn("InformasjonOmP1")), LanguageCode.BOKMAL)

        if (vedleggSideantall % 2 == 1) {
            assertThat(antallSider(resultat)).isEqualTo(1 + 1 + vedleggSideantall)
        } else {
            assertThat(antallSider(resultat)).isEqualTo(1 + vedleggSideantall)
        }
    }

    @Test
    fun `legger til flere vedlegg i rekkefoelge`() {
        val original = pdfWithPages(1)
        val vedlegg1Sideantall = antallSiderIVedlegg("InformasjonOmP1", LanguageCode.BOKMAL)
        val vedlegg2Sideantall = antallSiderIVedlegg("InformasjonOmP1", LanguageCode.BOKMAL)

        val resultat = appender.leggPaaVedlegg(
            original,
            listOf(vedleggMedFilnavn("InformasjonOmP1"), vedleggMedFilnavn("InformasjonOmP1")),
            LanguageCode.BOKMAL,
        )

        val forventetBlankSider = (if (vedlegg1Sideantall % 2 == 1) 1 else 0) + (if (vedlegg2Sideantall % 2 == 1) 1 else 0)
        assertThat(antallSider(resultat)).isEqualTo(1 + vedlegg1Sideantall + vedlegg2Sideantall + forventetBlankSider)
    }

    @Test
    fun `kaster exception naar vedlegg ikke finnes for spraaket`() {
        val original = pdfWithPages(1)

        assertThatThrownBy {
            appender.leggPaaVedlegg(original, listOf(vedleggMedFilnavn("InformasjonOmP1")), LanguageCode.NYNORSK)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("InformasjonOmP1")
    }
}
