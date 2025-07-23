package no.nav.pensjon.brev

import com.google.protobuf.kotlin.toByteString
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.Status
import io.grpc.StatusException
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.install
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.Brevbaker
import no.nav.brev.brevbaker.Fixtures
import no.nav.pensjon.brev.api.FeatureToggleService
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.latex.LaTeXCompilerGrpcService
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.pdfbygger.rpc.PdfCompile
import no.nav.pensjon.brev.pdfbygger.rpc.PdfCompileServiceGrpcKt
import no.nav.pensjon.brev.pdfbygger.rpc.compilePdfResponse
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.brevbakerConfig
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.coroutines.CoroutineContext

class GrpcCoroutineTest {
    val letter: Letter<BrevbakerBrevdata> = LetterImpl(
        LetterExample.template,
        createLetterExampleDto(),
        Language.Bokmal,
        Fixtures.fellesAuto
    )
    val server = embeddedServer(Netty, port = 9080) {
        FeatureToggleSingleton.init(object : FeatureToggleService {
            override fun isEnabled(toggle: FeatureToggle): Boolean = true
        })

        val latexCompileService = LaTeXCompilerGrpcService(
            "localhost",
            8081,
        )
        val brevbaker = Brevbaker(latexCompileService)

        install(ContentNegotiation) {
            jackson {
                brevbakerConfig()
            }
        }
        routing {
            get("/render") {
                val x = brevbaker.renderPDF(letter)
                call.respond(x)
            }
        }
    }.start()

    val client = HttpClient(CIO) {}


    @Test
    fun bla(): Unit = runBlocking {
        val x = client.get("http://localhost:9080/render")
        println(x.status)
    }

    @Test
    fun retrystuff(): Unit = runBlocking {
        val server: Server = ServerBuilder
            .forPort(9081)
            .addService(MyTest(coroutineContext))
            .build()

        server.start()
        val latexCompileService = LaTeXCompilerGrpcService(
            "localhost",
            9081,
        )
        val brevbaker = Brevbaker(latexCompileService)

        brevbaker.renderPDF(letter)

        server.shutdown()
    }
//    @OptIn(DelicateCoroutinesApi::class)
//    @Test
//    fun `latex compile grpc service can handle mulitple parallell coroutines`() = runBlocking {
//        withContext(Dispatchers.IO) {
//            val jobs = List(100) {
//                async {
//                    brevbaker.renderPDF(letter)
//                }
//            }
//            jobs.forEach { it.await() }
//        }
//    }
}

class MyTest(context: CoroutineContext) : PdfCompileServiceGrpcKt.PdfCompileServiceCoroutineImplBase(context) {
    private var attempts = 0
    private var previous: Instant? = null

    override suspend fun compilePdf(request: PdfCompile.CompilePdfRequest): PdfCompile.CompilePdfResponse {
        val now = Instant.now()
        if (previous != null) {
            println(now.toEpochMilli() - previous!!.toEpochMilli())
        }
        previous = now

        return if (attempts < 5) {
            attempts++
            compilePdfResponse {
                println("Failed attempt: $attempts")
                throw StatusException(Status.UNAVAILABLE.withDescription("Service unavailable, attempt: $attempts"))
            }
        } else {
            compilePdfResponse {
                pdf = "PDF content".toByteArray().toByteString()
            }
        }
    }
}