package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class ForhaandsvarselEtteroppgjoerDto(
    val resultatEtterOppgjoer: ResultatEtterOppgjoer,
    val ufoeretrygdEtteroppgjoer: UfoeretrygdEtteroppgjoer,
    val avkortningsinformasjon: Avkortningsinformasjon,
) {

data class ResultatEtterOppgjoer(
    val tidligereEOIverksatt: Boolean,
    val harEtterbetaling: Boolean, // PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'etterbet'
    val harTilbakePenger: Boolean, // PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'tilbakekr'
    val endretPersonGrunnlagInntektBruker: Boolean,
    val endretPersonGrunnlagInntektEPS: Boolean,
    val endretPensjonOgAndreYtelserBruker: Boolean,
    val endretPensjonOgAndreYtelserEPS: Boolean,
    val avviksbeloep: Kroner, // TODO: Fjern minus tegn fra Pesys
)
    data class UfoeretrygdEtteroppgjoer(
        val periodeFom: LocalDate,
        val inntektOverInntektstak: Boolean,
        val inntektsgrensebeloepAar: Kroner,
    )
    data class Avkortningsinformasjon(
        val inntektsgrense: Kroner?,
        val oppjustertInntektFoerUfoere: Kroner? // TODO: OIFU * 0,8
    )
}
// TODO:
// PE_Vedtaksdata_ForrigeEO inneholder ikke avkortningsinformasjon i dag
// Avkortningsinformasjon finnes i PE_Vedtaksdata_Beregningsdata_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner