package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brevbaker.api.model.DisplayText

enum class PeriodisertInntektBarnetillegg {
    @DisplayText("Periodisert inntekt")
    PERIODISERT_INNTEKT,

    @DisplayText("Inntekt gjelder for hele året")
    INNTEKT_HELE_ARET,

    @DisplayText("Barn fyller 18 ila året")
    BARN_FYLLER_18,

    @DisplayText("Skriv begrunnelse selv")
    INGEN
}