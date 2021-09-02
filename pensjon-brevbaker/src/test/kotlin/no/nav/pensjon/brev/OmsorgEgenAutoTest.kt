package no.nav.pensjon.brev

import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.something.Fagdelen
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class OmsorgEgenAutoTest {

    @Test
    fun test() {
        val mottaker = Fagdelen.Mottaker("Alexander Hoem", "Rosbach", "Agmund Bolts vei", "2", "0664", "Oslo")
        Letter(
            OmsorgEgenAuto.template,
            mapOf(
                ReturAdresse to Fagdelen.ReturAdresse("hei", "joda", "jadda", "123"),
                Mottaker to mottaker,
                NorskIdentifikator to 12345678911,
                SaksNr to 1234,
                ArEgenerklaringOmsorgspoeng to 2021,
                Felles to Fagdelen.Felles("2021-09-02", "12345", "123456", "NAV jo", "55342596", mottaker, "1234")
            ),
            Language.Bokmal
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService().producePDF(it) }
            .let { File("test.pdf").writeBytes(Base64.getDecoder().decode(it)) }
    }
}