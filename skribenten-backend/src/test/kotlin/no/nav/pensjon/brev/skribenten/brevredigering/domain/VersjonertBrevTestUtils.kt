package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2

fun VersjonertBrev.asV1(): Edit.Letter = (this as VersjonertBrev.V1).brev

fun VersjonertBrev.asV2(): EditV2.Letter = (this as VersjonertBrev.V2).brev
