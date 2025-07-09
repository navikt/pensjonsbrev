package no.nav.pensjon.brev.skribenten.model

import java.util.Locale

@JvmInline
value class Landkode(val landkode: String) {
    init {
        require(Landkoder.isValidLandkode(landkode)) {
            "Ugyldig landkode '$landkode': må være 2 bokstaver i henhold til ISO3166-1 alfa-2"
        }
    }
}

data class Land(
    val kode: Landkode,
    val navn: String
)

/**
 * Verktøy for å 2-bokstavers landkoder i henhold til iso3166-1 alfa-2.
 */
object Landkoder {
    private val alleLandkoder: Set<String> = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2)
    val landkoderMedNavn = alleLandkoder.map {
        Land(Landkode(it), Locale.of("", it).getDisplayCountry(Locale.of("NB", "NO")))
    }

    fun isValidLandkode(landkode: String): Boolean =
        landkode.length == 2 && alleLandkoder.contains(landkode.uppercase())

}