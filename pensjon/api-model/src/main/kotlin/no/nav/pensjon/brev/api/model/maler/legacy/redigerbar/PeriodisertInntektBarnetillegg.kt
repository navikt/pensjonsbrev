package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brevbaker.api.model.DisplayText

enum class PeriodisertInntektBarnetillegg(override val displayText: String) : SaksbehandlerValgEnum {
    @DisplayText("Periodisert inntekt")
    PERIODISERT_INNTEKT("Periodisert inntekt"),

    @DisplayText("Inntekt gjelder for hele året")
    INNTEKT_HELE_ARET("Inntekt gjelder for hele året"),

    @DisplayText("Barn fyller 18 ila året")
    BARN_FYLLER_18("Barn fyller 18 ila året"),

    @DisplayText("Skriv begrunnelse selv")
    INGEN("Skriv begrunnelse selv")
}