package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.skribenten.services.addAbstractTypeMapping

object SakstypeModule : SimpleModule() {
    private fun readResolve(): Any = SakstypeModule

    init {
        addAbstractTypeMapping<ISakstype, Sakstype>()
    }
}

@JvmInline
value class Sakstype(override val kode: String) : ISakstype {
    override fun toString() = kode
}