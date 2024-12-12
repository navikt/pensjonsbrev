package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak


data class Vilkar(
    val alderbegrunnelse: String?,
    val alderresultat: String?,
    val fortsattmedlemsskapresultat: String?,
    val forutgaendemedlemskapresultat: String?,
    val hensiktsmessigarbeidsrettedetiltakbegrunnelse: String?,
    val hensiktsmessigarbeidsrettedetiltakresultat: String?,
    val hensiktsmessigbehandlingbegrunnelse: String?,
    val hensiktsmessigbehandlingresultat: String?,
    val nedsattinntektsevnebegrunnelse: String?,
    val nedsattinntektsevneresultat: String?,
    val sykdomskadelytebegrunnelse: String?,
    val sykdomskadelyteresultat: String?,
    val unguforbegrunnelse: String?,
    val unguforresultat: String?,
    val fortsattmedlemskap: FortsattMedlemskap?,
    val medlemskapforutettertrygdeavtaler: MedlemskapForUTetterTrygdeavtaler?,
    val yrkesskadebegrunnelse: String?,
    val yrkesskaderesultat: String?,
)