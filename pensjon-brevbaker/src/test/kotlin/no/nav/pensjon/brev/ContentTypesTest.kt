package no.nav.pensjon.brev

import io.ktor.http.ContentType
import io.ktor.http.withCharset
import no.nav.brev.brevbaker.ContentTypes
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ContentTypesTest {

    @Test
    fun `vaare egne ContentType-hardkodinger matcher med de fra biblioteket`() {
        assertEquals(ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(), ContentTypes.TEXT_HTML_UTF8)
        assertEquals(ContentType.Application.Pdf.toString(), ContentTypes.PDF)
    }
}