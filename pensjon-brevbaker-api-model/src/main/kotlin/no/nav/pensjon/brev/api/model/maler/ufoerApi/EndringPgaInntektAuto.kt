package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata


@Suppress("unused")
data class EndringPgaInntektAutoDto(
    val harFlereFellesbarn: Boolean,
    val sivilstand: Sivilstand,
    val endringIUtbetaling: EndringIUtbetaling,
): BrevbakerBrevdata

data class EndringIUtbetaling(
    val harBeloepEndringUfoeretrygd: Boolean,
    val harBeloepEndringBarnetilleggFellesbarn: Boolean,
    val harBeloepEndringBarnetilleggSaerkullsbarn: Boolean,
)




