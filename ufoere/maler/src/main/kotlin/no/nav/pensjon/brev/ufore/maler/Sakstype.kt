package no.nav.pensjon.brev.ufore.maler

import no.nav.pensjon.brev.api.model.ISakstype

enum class Sakstype : ISakstype {
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

    override fun kode() = name

    companion object {
        val all: Set<Sakstype> = entries.toSet()
        val pensjon: Set<Sakstype> = setOf(
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
            OMSORG
        )
    }
}