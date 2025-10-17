package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.forrigeetteroppgjor

data class ForrigeEtteroppgjor(
    val eoendringeps: EoEndringEps?,
    val eoendringbruker: EoEndringBruker?,
    val resultatforrigeeo: String?,
    val tidligereeoiverksatt: Boolean?,
)
