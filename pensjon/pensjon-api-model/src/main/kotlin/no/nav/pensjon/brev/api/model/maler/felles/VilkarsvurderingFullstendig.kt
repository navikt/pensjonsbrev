package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

data class VilkarsvurderingFullstendig(
    val virkningstidpunkt: LocalDate,
    val virkningstidspunktBegrunnelse: VirkningstidspunktBegrunnelse?,

    val uforetidspunkt: LocalDate,

    val uforegrad: Int,

    val ifuInntekt: Kroner,
    val ifuBegrunnelse: InntektForUforhetBegrunnelse?,

    val ieuInntekt: Kroner,
    val ieuBegrunnelse: InntektEtterUforhetBegrunnelse?,

    val ungUforResultat: VilkarResultat?,

    val nedsattInntektsevneResultat: VilkarResultat?,

    val yrkesskade: YrkesskadeVilkar?,
) {
    val virkningstidspunktMnd: Month = virkningstidpunkt.month

    data class YrkesskadeVilkar(
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
        val yrkesskadeBegrunnelse: String,
        val yrkesskadeResultat: VilkarResultat,
        val inntektVedSkadetidspunktBegrunnelse: InntektVedSkadetidspunktBegrunnelse,
        val inntektVedSkadetidspunkt: Kroner,
    )

    enum class VirkningstidspunktBegrunnelse {
        STDBEGR_22_12_1_1, STDBEGR_22_12_1_2, STDBEGR_22_12_1_3, STDBEGR_22_12_1_4, STDBEGR_22_12_1_5, STDBEGR_22_12_1_11, STDBEGR_22_12_1_12
    }

    enum class InntektForUforhetBegrunnelse {
        STDBEGR_12_8_2_1, STDBEGR_12_8_2_10, STDBEGR_12_8_2_11, STDBEGR_12_8_2_2, STDBEGR_12_8_2_3, STDBEGR_12_8_2_4, STDBEGR_12_8_2_5, STDBEGR_12_8_2_9
    }

    enum class InntektEtterUforhetBegrunnelse {
        STDBEGR_12_8_1_1, STDBEGR_12_8_1_2, STDBEGR_12_8_1_3
    }

    enum class InntektVedSkadetidspunktBegrunnelse {
        STDBEGR_12_17_3_1, STDBEGR_12_17_3_2
    }
}