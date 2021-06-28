package no.nav.pensjon.brev.pdfbygger

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import org.slf4j.event.Level
import java.util.*

val laTeXService = LaTeXService()
fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        jackson()
    }

    install(CallLogging) {
        level = Level.INFO
    }

//    install(Compression){
//        gzip {
//            condition { request.uri == "/compile" }
//        }
//    }
    routing {
        post("/compile") {
            try {
                val pdfCompilationInput = call.receive<PdfCompilationInput>()
                call.respond(laTeXService.producePDF(pdfCompilationInput.files))
            } catch (e: Exception) {
                call.respond(PDFCompilationOutput(e.stackTraceToString()))
            }
        }

        get("/ping") {
            call.respond("pong")
        }
    }
}