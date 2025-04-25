package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.time.LocalDate

data class VedleggSlikErUforetrygdenDinBeregnetDto (
    val virkningFom: LocalDate,
    val grunnbelop: Int,
    val uforetidspunkt: LocalDate,
    val oifu: Int,
    val ifu: Int,
    val ieu: Int,
    val uforegrad: Int,
    val inntektsgrense: Int,
    val forventetInntekt: Int,
    val kompensasjonsgrad: Int,
    val inntektstak: Int,
    val sivilstand: String,

    val antallBarn: Int,
    val fribelopBTSB: Int,
    val forventetInntektBT: Int,
    val giftLeverAdskilt: Boolean,
    val partnerLeverAdskilt: Boolean,
    val ungUfor: Boolean,
    val yrkesskade: Yrkesskade?,
    val beregnetFlyktningsstatus: Boolean,
    val trygdetid: Trygdetid,
    val prorataBrok: Brok,
    val beregningsmetode: Beregningsmetode,


) : BrevbakerBrevdata {
    data class Yrkesskade (
        val yrkesskadegrad: Int,
        val inntektForYrkesskade: Int
    )

    data class Trygdetid (
        val anvendt: Int,
        val framtidig: Int,
        val fraEos: Boolean,
        val fraAvtaleland: Boolean,
        val faktisk: Int,
        val faktiskEos: Int?,
        val faktiskNordisk: Int?,
        val faktiskAvtaleland: Int?,
        val forholdstall: Brok,
        val samlet: Int,
        val forholdstallNorskFtt: Brok,
        val forholdstallNordiskFtt: Brok,
    )

    data class Brok (
        val teller: Int,
        val nevner: Int,
    )

    enum class Beregningsmetode {
        FOLKETRYGD,
        EOS,
        NORDISK,
    }
}