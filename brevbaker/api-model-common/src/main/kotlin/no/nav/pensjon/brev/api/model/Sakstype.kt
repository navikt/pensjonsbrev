package no.nav.pensjon.brev.api.model

interface ISakstype {
    fun kode(): String
    fun isIn(sakstyper: Set<ISakstype>): Boolean = sakstyper.map { it.kode() }.contains(this.kode())
}