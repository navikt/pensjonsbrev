package no.nav.pensjon.brev.api.model

interface ISakstype {
    fun kode(): String
    fun isRelevantRegelverk(first: Any?, second: Any?): Boolean = true
    fun isIn(sakstyper: Set<ISakstype>): Boolean = sakstyper.map { it.kode() }.contains(this.kode())
}