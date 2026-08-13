package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.vilkarsvedtaklist

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.VilkarsVedtak


data class VilkarsVedtakList(
    val vilkarsvedtak: List<VilkarsVedtak>
)