package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class ForhaandsvarselEtteroppgjoerDto(
    val forrigeEtteroppgjoer: ForrigeEtteroppgjoer,
    val etteroppgjoerResultat: EtteroppgjoerResultat,
    val ufoeretrygdEtteroppgjoer: UfoeretrygdEtteroppgjoer,
) {
data class ForrigeEtteroppgjoer(
    val tidligereEOIverksatt: Boolean,
    val harEtterbetaling: Boolean, // PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'etterbet'
    val harTilbakePenger: Boolean, // PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'tilbakekr'
    val endretPersonGrunnlagInntektBruker: Boolean,
    val endretPersonGrunnlagInntektEPS: Boolean,
    val endretPensjonOgAndreYtelserBruker: Boolean,
    val endretPensjonOgAndreYtelserEPS: Boolean,
)

    data class EtteroppgjoerResultat(
        val avviksbeloep: Kroner, // TODO: Fjern minus tegn fra Pesys
    )

    data class UfoeretrygdEtteroppgjoer(
        val periodeFom: LocalDate,
        val inntektOverInntektstak: Boolean,
        val inntektsgrensebeloepAar: Kroner,

    )
}