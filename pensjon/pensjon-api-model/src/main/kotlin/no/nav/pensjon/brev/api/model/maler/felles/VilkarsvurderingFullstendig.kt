package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

//TODO er vi sikker på de som ikke er nullable her?
//todo husk logikken bak yrkesskade
data class VilkarsvurderingFullstendig(
    val virkningstidpunkt: LocalDate,
    val virkningstidspunktBegrunnelse: VirkningstidspunktBegrunnelse?,

    val uforetidspunkt: LocalDate,

    val uforegrad: Int,

    val ifuInntekt: Kroner,
    val ifuBegrunnelse: InntektForUforhetBegrunnelse?,

    val ieuInntekt: Kroner,
    val ieuBegrunnelse: InntektEtterUforhetBegrunnelse?,

    val ungUforResultat: VilkarResultat,

    val nedsattInntektsevneResultat: VilkarResultat,

    val yrkesskadeResultat: VilkarResultat,
    val yrkesskadeBegrunnelse: YrkesskadeBegrunnelse?,
    val yrkesskade: Yrkesskade?,
) {
    fun virkningstidspunktMnd() = virkningstidpunkt.month

    data class Yrkesskade(
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
        val inntektVedSkadetidspunkt: Kroner,
        val inntektVedSkadetidspunktBegrunnelse: YrkesskadeBegrunnelse?,
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

    enum class YrkesskadeBegrunnelse {
        STDBEGR_12_17_1_I_1, STDBEGR_12_17_1_I_2, STDBEGR_12_17_1_O_1, STDBEGR_12_17_1_O_2, STDBEGR_12_17_1_O_3
    }

    enum class InntektVedSkadetidspunktBegrunnelse {
        STDBEGR_12_17_1_I_1, STDBEGR_12_17_1_I_2, STDBEGR_12_17_1_O_1, STDBEGR_12_17_1_O_2, STDBEGR_12_17_1_O_3
    }
}