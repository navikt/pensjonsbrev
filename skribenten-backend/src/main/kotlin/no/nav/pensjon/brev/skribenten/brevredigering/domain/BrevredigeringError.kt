package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.api.model.maler.Brevkode

sealed interface BrevredigeringError

data class BrevmalFinnesIkke(val brevkode: Brevkode.Redigerbart) : BrevredigeringError

data class FeilMarkupVersjon(val forventet: MarkupVersjon, val faktisk: MarkupVersjon) : BrevredigeringError

fun Brevredigering.sjekkMarkupVersjon(versjon: MarkupVersjon?): FeilMarkupVersjon? =
    versjon?.takeIf { it != markupVersjon }?.let { FeilMarkupVersjon(forventet = markupVersjon, faktisk = it) }
