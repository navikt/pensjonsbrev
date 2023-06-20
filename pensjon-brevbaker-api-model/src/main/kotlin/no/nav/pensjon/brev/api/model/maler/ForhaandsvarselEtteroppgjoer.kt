package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import no.nav.pensjon.brevbaker.api.model.Year


@Suppress("unused")
data class ForhaandsvarselEtteroppgjoerDto(
    val resultatEtteroppgjoer: ResultatEtteroppgjoer,
    val ufoeretrygdEtteroppgjoer: UfoeretrygdEtteroppgjoer,
) {

    data class ResultatEtteroppgjoer(
        val avviksbeloep: Kroner, // TODO: Fjern minus tegn fra Pesys
        val endretPensjonOgAndreYtelserBruker: Boolean,
        val endretPensjonOgAndreYtelserEPS: Boolean,
        val endretPersonGrunnlagInntektBruker: Boolean,
        val endretPersonGrunnlagInntektEPS: Boolean,
        val harEtterbetaling: Boolean, // TODO: PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'etterbet'
        val harTilbakePenger: Boolean, // TODO: PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'tilbakekr'
        val tidligereEOIverksatt: Boolean,
    )

    data class UfoeretrygdEtteroppgjoer(
        val aarPeriodeFom: Year,  // TODO: Use YYYY from periodeFom
        val inntektOverInntektstak: Boolean,
        val inntektsgrensebeloepAar: Kroner,
        val oppjustertInntektFoerUfoere: Kroner, // TODO: OIFU * 0,8
        val periodeFom: LocalDate,
        val ufoeregrad: Int,  // PE_Vedtaksdata_Beregningsdata_BeregningUfore_Uforetrygdberegning_Uforegrad
    )
}
