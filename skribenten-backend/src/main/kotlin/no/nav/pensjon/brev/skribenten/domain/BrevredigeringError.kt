package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.api.model.maler.Brevkode

sealed interface BrevredigeringError

data class BrevmalFinnesIkke(val brevkode: Brevkode.Redigerbart) : BrevredigeringError
