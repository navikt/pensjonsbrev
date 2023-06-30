package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year


@Suppress("unused")
data class ForhaandsvarselEtteroppgjoerAutoDto(
    // TODO: Include attachment BeregningAvEtteroppgjoeret
    // TODO: Include attachment OpplysningerOmBeregningen
    // TODO: Include attachment PraktiskInformasjonEtteroppgjoeret
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val resultatEtteroppgjoer: ResultatEtteroppgjoer,
    val ufoeretrygdEtteroppgjoer: UfoeretrygdEtteroppgjoer,
) {

    data class ResultatEtteroppgjoer(
        val avviksbeloep: Kroner, // TODO: Fjern minus tegn fra Pesys
        val harNyttEtteroppgjoer: Boolean,
    )

    data class UfoeretrygdEtteroppgjoer(
        val aarPeriodeFom: Year,  // TODO: Use YYYY from periodeFom
        val hoeyesteInntektsgrense: Kroner,  // TODO: Finnes ikke i dagensbrev
        val inntektOverInntektstak: Boolean,  // TODO: Finnes ikke i dagensbrev
        val oppjustertInntektFoerUfoerhet: Kroner, // TODO: OIFU * 0,8 (beregningen er gjort i Exstream). Finnes ikke i dagensbrev
        val ufoeregrad: Int,  // PE_Vedtaksdata_Beregningsdata_BeregningUfore_Uforetrygdberegning_Uforegrad  // TODO: Finnes ikke i dagensbrev
    )
}
