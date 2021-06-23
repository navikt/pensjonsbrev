package no.nav.pensjon.brev

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.request.*
import no.nav.pensjon.brev.dto.AdHocLetterRequest
import no.nav.pensjon.brev.dto.Letter
import no.nav.pensjon.brev.dto.LetterTemplate
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.LatexLetterBuilder

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    val laTeXCompilerService = LaTeXCompilerService()
    val latexLetterBuilder = LatexLetterBuilder(laTeXCompilerService)

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        post("/adhoc-brev") {
            val (standardFields, letterText) = call.receive<AdHocLetterRequest>()
            latexLetterBuilder.buildLatex(Letter(standardFields, LetterTemplate(letterText)))
        }
    }
}

