package no.nav.pensjon.brev

import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.something.Fagdelen
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.BaseTemplate
import no.nav.pensjon.brev.template.Language
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
                ArEgenerklaringOmsorgspoeng to 2020,
                Felles to Fagdelen.Felles(
                    dokumentDato = LocalDate.of(2021,9, 6),
                    returAdresse = Fagdelen.ReturAdresse(
                        navEnhetsNavn = "NAV Familie- og pensjonsytelser Porsgrunn",
                        adresseLinje1 = "Postboks 6600 Etterstad",
                        postNr = "0607",
                        postSted = "Oslo"
                    ),
                    mottaker = Fagdelen.Mottaker(
                        fornavn = "PERLAKI",
                        etternavn = "YASIR-MASK",
                        gatenavn = "JERBANETORGET",
                        husnummer = "4 F",
                        postnummer = "1344",
                        poststed = "HASLUM"
                    ),
//                    signerendeSaksbehandlere = Fagdelen.SignerendeSaksbehandlere("Jon Gunnar Aasen", "Kjetil Johannesen")
                )
            ),
            Language.Bokmal
        ).render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .also { File("test-params.tex").writeBytes(Base64.getDecoder().decode(it.files["letter.tex"])) }
//            .also { File("test-params.tex").writeBytes(Base64.getDecoder().decode(it.files["params.tex"])) }
            .let { LaTeXCompilerService().producePDF(it) }
            .also { File("test.pdf").writeBytes(Base64.getDecoder().decode(it)) }
    }

}