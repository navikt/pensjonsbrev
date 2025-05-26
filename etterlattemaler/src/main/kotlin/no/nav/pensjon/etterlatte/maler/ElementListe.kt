package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.dsl.VedleggbarListe

class ElementListe(override val liste: List<Element>) : VedleggbarListe<Element> {
    companion object {
        fun tom() = ElementListe(emptyList())
    }
}