package no.nav.pensjon.brev.pdfbygger

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

internal class ApplicationKtTest {
    @Test
    fun testRoot() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Post, "/compile"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    Gson().toJson(
                    PdfCompilationInput(
                        mapOf(
                                "letter.tex" to
                                """
                                \documentclass{article}
                                \begin{document}
                                    Hello world!
                                \end{document}
                            """.toByteArray()
                        )
                    )
                ))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }

    }
}