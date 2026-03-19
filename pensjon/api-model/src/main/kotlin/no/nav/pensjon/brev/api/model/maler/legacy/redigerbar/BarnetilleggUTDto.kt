package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import java.time.LocalDate

data class BarnetilleggUTDto(val resultat: BarnetilleggResultatCode, val fodselsdato: LocalDate, val fom: LocalDate, val tom: LocalDate? = null)
    enum class BarnetilleggResultatCode {
        INNVILGET,
        BT_GITT_TIL_ANNEN,
        ANNEN_FORLD_RETT_BT,
        BT_OVER_18,
        MINDRE_ETT_AR_BT_FLT,
        BT_INNT_OVER_1G,
        BRK_FORSO_IKKE_BARN,
        BRUKER_FLYTTET_IKKE_AVT_LAND,
        BARN_FLYTTET_IKKE_AVT_LAND,
        BARN_OPPH_IKKE_AVT_LAND
    }