package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

// PE_UT_23_001 Varsel - etteroppgjør av uføretrygd ved feilutbetaling (auto)
@Suppress("unused")
data class ForhaandsvarselEtteroppgjoerDto(
    val periodeFom: LocalDate,
) {
data class ForrigeEtteroppgjoer(
    val tidligereEOIverksatt: Boolean,
    val harEtterbetaling: Boolean, // PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'etterbet'
    val harTilbakePenger: Boolean, // PE_Vedtaksbrev_Vedtaksdata_ForrigeEO_ResultatForrigeEO = 'tilbakekr'
    val endretPersonGrunnlagInntekt: Boolean
)
}