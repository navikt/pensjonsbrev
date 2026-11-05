package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.letter.Edit

fun VersjonertBrev.somV1(): Edit.Letter = (this as VersjonertBrev.V1).brev
