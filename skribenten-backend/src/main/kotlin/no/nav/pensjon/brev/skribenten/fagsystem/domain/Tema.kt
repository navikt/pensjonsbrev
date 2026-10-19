package no.nav.pensjon.brev.skribenten.fagsystem.domain

@JvmInline
value class Tema(val value: String) {
    init {
        require(value.length <= 32) { "Tema må være mindre enn eller lik 32 tegn, var ${value.length}" }
    }
}