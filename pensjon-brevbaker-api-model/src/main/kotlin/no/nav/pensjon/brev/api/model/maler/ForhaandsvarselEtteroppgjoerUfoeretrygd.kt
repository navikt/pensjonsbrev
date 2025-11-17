package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.maler.AutobrevData

@Suppress("unused")
data class ForhaandsvarselEtteroppgjoerUfoeretrygdDto(
    val erNyttEtteroppgjoer: Boolean,
    val harTjentOver80prosentAvOIFU: Boolean,
    val kanSoekeOmNyInntektsgrense: Boolean,
    val oppjustertInntektFoerUfoerhet: Kroner,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val opplysningerOmEtteroppgjoeretUfoeretrygd: OpplysningerOmEtteroppgjoeretDto,
) : AutobrevData