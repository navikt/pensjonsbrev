package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.Bruker

fun Bruker.fulltNavn() = listOfNotNull(fornavn, mellomnavn, etternavn).joinToString(" ")