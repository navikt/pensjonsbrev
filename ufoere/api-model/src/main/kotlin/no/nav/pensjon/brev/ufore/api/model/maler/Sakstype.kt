package no.nav.pensjon.brev.ufore.api.model.maler

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

    override val kode = name
}