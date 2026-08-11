package no.nav.pensjon.brev.pdfbygger

import java.time.LocalDate

internal object PdfByggerTestData {
    const val gjelderNavn = "Test \"bruker\" Testerson"
    const val gjelderPersonidentifikator = "01019878910"
    const val saksnummer = "1337123"
    val dokumentDato: LocalDate = LocalDate.of(2020, 1, 1)
    const val navAvsenderEnhet = "Nav Familie- og pensjonsytelser Porsgrunn"
    const val hilsenTekst = "Med vennlig hilsen"
    const val saksbehandlerNavn = "Ole Saksbehandler"
    const val attesterendeSaksbehandlerNavn = "Per Attesterende"
    const val vergeNavn = "Verge vergeson"
}
