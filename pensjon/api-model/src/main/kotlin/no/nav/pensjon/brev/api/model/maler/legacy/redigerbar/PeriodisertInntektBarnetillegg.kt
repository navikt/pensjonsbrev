package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brevbaker.api.model.DisplayText

enum class PeriodisertInntektBarnetillegg(override val displayText: String) : SaksbehandlerValgEnum {
    PERIODISERT_INNTEKT("Periodisert inntekt"),
    INNTEKT_HELE_ARET("Inntekt gjelder for hele året"),
    BARN_FYLLER_18("Barn fyller 18 ila året"),
    INGEN("Skriv begrunnelse selv")
}