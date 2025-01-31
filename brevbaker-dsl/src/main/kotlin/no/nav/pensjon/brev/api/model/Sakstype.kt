package no.nav.pensjon.brev.api.model

// TODO: Introduser interface for denne, og bruk det frå TemplateDescription heller enn denne
enum class Sakstype {
    AFP,
    AFP_PRIVAT,
    ALDER,
    BARNEP,
    FAM_PL,
    GAM_YRK,
    GENRL,
    GJENLEV,
    GRBL,
    KRIGSP,
    OMSORG,
    UFOREP,
    ;

    companion object {
        val all: Set<Sakstype> = entries.toSet()
        val pensjon: Set<Sakstype> = setOf(AFP, AFP_PRIVAT, ALDER, BARNEP, FAM_PL, GAM_YRK, GENRL, GJENLEV, GRBL, KRIGSP, OMSORG)
    }
}