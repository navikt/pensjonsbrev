package no.nav.pensjon.brev.ufore.maler

import no.nav.pensjon.brev.api.model.ISakstype

enum class Sakstype : ISakstype {
    UFOREP,
    ;

    override fun kode() = name
}