package no.nav.pensjon.brev.maler.klagebrev

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Test

class OversendelsesbrevTilKlageinstansUTTest {

    @Test
    fun `legacy paragraf selections remain renderable`() {
        listOf(
            "FastsettelseIFU",
            "FastsettelseIEU",
            "AutomatiskInntektsreduksjon",
            "Etteropgjoer",
            "EtteroppgjoerBarnetillegg",
        ).forEach { paragraf ->
            LetterTestImpl(
                OversendelsesbrevTilKlageinstansUT.template,
                EmptyRedigerbarBrevdata(saksbehandlerValg = lagSaksbehandlervalg(mapOf("paragraf" to paragraf))),
                Language.Bokmal,
                Fixtures.felles,
            ).renderTestHtml("OversendelsesbrevTilKlageinstansUT_$paragraf")
        }
    }
}
