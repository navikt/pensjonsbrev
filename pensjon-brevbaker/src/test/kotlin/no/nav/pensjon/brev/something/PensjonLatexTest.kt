package no.nav.pensjon.brev.something

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.api.LetterRequest
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.Alderspensjon
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

//@Disabled //pdf compilation integration test
internal class PensjonLatexTest {

    val returAdresse = Fagdelen.ReturAdresse("En NAV enhet", "En adresse 1", "1337", "Et poststed")
    private val templateArgs: Map<String, JsonNode> = with(jacksonObjectMapper()) {
        mapOf(
            ReturAdresse.name to valueToTree(returAdresse),
            SaksNr.name to valueToTree(1234),
            PensjonInnvilget.name to valueToTree(true),
            Mottaker.name to valueToTree(
                Fagdelen.Mottaker(
                    "FornavnMottaker",
                    "EtternavnMottaker",
                    "GatenavnMottaker",
                    "21 A",
                    "0123",
                    "PoststedMottaker"
                )
            ),
            NorskIdentifikator.name to valueToTree(13374212345),
        )
    }

    @Test
    fun render() {
        LetterRequest(Alderspensjon.template.name, templateArgs, Language.Bokmal)
            .let { LetterResource.create(it) }
            .render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService().producePDF(it) }
            .let { Base64.getDecoder().decode(it) }
            .let { File("test.pdf").writeBytes(it) }
    }

}