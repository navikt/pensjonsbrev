package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import java.time.LocalDate

data class BarnetilleggUTDto(val antallBarn: Int, val begrunnelse: BtBegrunnelseCode, val fodselsdato: LocalDate, val fom: LocalDate, val tom: LocalDate? = null)
enum class BtBegrunnelseCode {
    INNVILGET,
    ANNEN_FORLD_RETT_BT,
    BT_GITT_TIL_ANNEN,
    MINDRE_ETT_AR_BT_FLT,
    BT_INNT_OVER_1G,
    BRK_FORSO_IKKE_BARN,
    BRUKER_FLYTTET_IKKE_AVT_LAND,
    BARN_FLYTTET_IKKE_AVT_LAND,
    BARN_OPPH_IKKE_AVT_LAND,
    IKKE_MOTTATT_DOK,
    BT_OVER_18,
    OPPHOR_ANNEN_FORLD_RETT_BT,
    OPPHOR_BT_GITT_TIL_ANNEN,
    OPPHOR_BT_INNT_OVER_1G,
    OPPHOR_BRK_FORSO_IKKE_BARN,
    OPPHOR_BRUKER_FLYTTET_IKKE_AVT_LAND,
    OPPHOR_BARN_FLYTTET_IKKE_AVT_LAND,
    OPPHOR_BARN_OPPH_IKKE_AVT_LAND,
    OPPHOR_IKKE_MOTTATT_DOK,
    OPPHOR_BT_OVER_18,
    ANNET_AVSLAG,
    ANNET_OPPHOR,
}