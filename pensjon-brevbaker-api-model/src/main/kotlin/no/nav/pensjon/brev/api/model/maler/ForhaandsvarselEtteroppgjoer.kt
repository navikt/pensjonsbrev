package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year


@Suppress("unused")
data class ForhaandsvarselEtteroppgjoerAutoDto(
    val resultatEtteroppgjoer: ResultatEtteroppgjoer,
    val ufoeretrygdEtteroppgjoer: UfoeretrygdEtteroppgjoer,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) {

    data class ResultatEtteroppgjoer(
        val avviksbeloep: Kroner, // TODO: Fjern minus tegn fra Pesys
        val harNyttEtteroppgjoer: Boolean,
    )

    data class UfoeretrygdEtteroppgjoer(
        val aarPeriodeFom: Year,  // TODO: Use YYYY from periodeFom
        val hoeyesteInntektsgrense: Kroner,
        val inntektOverInntektstak: Boolean,
        val oppjustertInntektFoerUfoerhet: Kroner, // TODO: OIFU * 0,8
        val ufoeregrad: Int,  // PE_Vedtaksdata_Beregningsdata_BeregningUfore_Uforetrygdberegning_Uforegrad
    )
}
