package no.nav.pensjon.brev

import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.something.Fagdelen
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import java.util.*

class OmsorgEgenAutoTest {

    @Disabled
    @Test
    fun test() {
        Letter(
            OmsorgEgenAuto.template,
            mapOf(
                NorskIdentifikator to 12345678911,
                SaksNr to 1234,
                ArEgenerklaringOmsorgspoeng to 2021,
                Felles to Fagdelen.Felles(
                    dokumentDato = LocalDate.now(),
                    returAdresse = Fagdelen.ReturAdresse(
                        navEnhetsNavn = "hei",
                        adresseLinje1 = "joda",
                        postNr = "jadda",
                        postSted = "123"
                    ),
                    mottaker = Fagdelen.Mottaker(
                        fornavn = "Alexander Hoem",
                        etternavn = "Rosbach",
                        gatenavn = "Agmund Bolts vei",
                        husnummer = "2",
                        postnummer = "0664",
                        poststed = "Oslo"
                    )
                )
            ),
            Language.Bokmal
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService().producePDF(it) }
            .let { File("test.pdf").writeBytes(Base64.getDecoder().decode(it)) }
    }
}