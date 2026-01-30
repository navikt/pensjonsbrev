package no.nav.pensjon.brevbaker.api.model

enum class ElementTags(private val kontekst: Kontekst) {
    FRITEKST(Kontekst.LITERAL),
    REDIGERBAR_DATA(Kontekst.VARIABLE);

    companion object {
        fun forLiteral(tags: Set<ElementTags>) = tags.filter { it.kontekst == Kontekst.LITERAL }.toSet()
        fun forVariable(tags: Set<ElementTags>) = tags.filter { it.kontekst == Kontekst.VARIABLE }.toSet()
    }
}

private enum class Kontekst {
    LITERAL, VARIABLE
}