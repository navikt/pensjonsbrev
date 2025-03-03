package no.nav.pensjon.brev.template.expression

import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

interface ExpressionMapper<In, Out>: StableHash {
    val name: String
    val apply: (In) -> Out

    override fun stableHashCode(): Int = StableHash.of(name).stableHashCode()
}

object IntToKroner : ExpressionMapper<Int, Kroner> {
    override val name: String = "IntToKroner"
    override val apply = ::Kroner
}

object IntToYear : ExpressionMapper<Int, Year> {
    override val name: String = "IntToYear"
    override val apply = ::Year
}