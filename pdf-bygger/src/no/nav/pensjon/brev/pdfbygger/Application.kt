package no.nav.pensjon.brev.pdfbygger

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.netty.*

const val IN_MEMORY_ROOT_PATH = "/tmp/"

val laTeXService = LaTeXService(IN_MEMORY_ROOT_PATH)
fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation){
        gson{}
    }

    install(Compression){
        gzip {
            condition { request.uri == "/compile" }
        }
    }
    routing {
        post("/compile") {
            val pdfCompilationInput = call.receive<PdfCompilationInput>()
            call.respond(laTeXService.producePDF(pdfCompilationInput.files))
        }

        get("/ping") {
            call.respond("pong")
        }
    }
}