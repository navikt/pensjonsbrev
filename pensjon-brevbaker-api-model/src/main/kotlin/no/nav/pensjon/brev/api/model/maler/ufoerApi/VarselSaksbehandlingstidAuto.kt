package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate


@Suppress("unused")
data class VarselSaksbehandlingstidAutoDto(
    val mottattDatoMinus2Dager: LocalDate,
    val utvidetBehandlingstid: Boolean,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) : BrevbakerBrevdata