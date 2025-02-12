package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brevbaker.api.model.Bruker

fun Bruker.fulltNavn() = listOfNotNull(fornavn, mellomnavn, etternavn).joinToString(" ")
