package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OversendelsesbrevTilKlageinstansUTDto
import no.nav.pensjon.brev.fixtures.redigerbar.createOversendelsesbrevTilKlageinstansDto
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OversendelsesbrevTilKlageinstansUTtest {

    privat val data = OversendelsesbrevTilKlageinstansUTDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "trygdetid" to true,
        ),
        pesysData = EmptyFagsystemdata
    )
