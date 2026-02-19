package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brevbaker.api.model.BrevFelles.Bruker

fun Bruker.fulltNavn() = listOfNotNull(fornavn, mellomnavn, etternavn).joinToString(" ")