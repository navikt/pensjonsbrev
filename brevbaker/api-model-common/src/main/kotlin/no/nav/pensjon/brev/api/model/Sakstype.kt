package no.nav.pensjon.brev.api.model

interface ISakstype {
    fun kode(): String
    fun isRelevantRegelverk(first: Any?, second: Any?): Boolean = true
}