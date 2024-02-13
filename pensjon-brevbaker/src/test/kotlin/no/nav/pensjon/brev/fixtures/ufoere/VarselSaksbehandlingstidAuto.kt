package no.nav.pensjon.brev.fixtures.ufoere

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import java.time.LocalDate

fun createVarselSaksbehandlingstidAutoDto() =
    VarselSaksbehandlingstidAutoDto(
        mottattDatoMinus2Dager = LocalDate.now(),
        utvidetBehandlingstid = false,
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )