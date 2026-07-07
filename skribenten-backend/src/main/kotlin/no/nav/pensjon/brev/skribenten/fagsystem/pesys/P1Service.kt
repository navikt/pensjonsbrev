package no.nav.pensjon.brev.skribenten.fagsystem.pesys

// Disse må være i sync med api-modellen
const val P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2"
const val P1_VEDLEGG_KEY = "p1Vedlegg"

sealed class P1Exception(override val message: String): Exception(){
    class ManglerDataException(message: String): P1Exception(message)
}