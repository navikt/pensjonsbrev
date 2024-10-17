package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak


data class Vilkar(
    val yrkesskaderesultat: String?,
    val unguforresultat: String?,
    val fortsattmedlemskap: FortsattMedlemskap?,
    val medlemskapforutettertrygdeavtaler: MedlemskapForUTetterTrygdeavtaler?,
)