package no.nav.pensjon.brev.skribenten.fagsystem.pesys

sealed class P1Exception(override val message: String): Exception(){
    class ManglerDataException(message: String): P1Exception(message)
}