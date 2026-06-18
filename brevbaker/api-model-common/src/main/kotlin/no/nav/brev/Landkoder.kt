package no.nav.brev

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.util.Locale
import java.util.Objects


object BrevLandmodell {
    @JvmInline
    value class Landkode(val landkode: String) {
        init {
            require(Landkoder.isValidLandkode(landkode)) {
                "Ugyldig landkode '$landkode': må være 2 bokstaver i henhold til ISO3166-1 alfa-2"
            }
        }

        override fun toString() = landkode
    }

    class Land(
        val kode: Landkode,
        val navn: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (other !is Land) return false
            return kode == other.kode && navn == other.navn
        }

        override fun hashCode() = Objects.hash(kode, navn)
        override fun toString() = "Land(kode=$kode, navn='$navn')"
    }

    /**
     * Verktøy for å 2-bokstavers landkoder i henhold til iso3166-1 alfa-2.
     */
    object Landkoder {
        val alleLandkoder: Set<String> = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2)

        fun formaterLandnavn(landkode: Landkode, spraak: LanguageCode): String =
            when (spraak) {
                LanguageCode.BOKMAL -> Locale.of("NB", "NO")
                LanguageCode.NYNORSK -> Locale.of("NN", "NO")
                LanguageCode.ENGLISH -> Locale.ENGLISH
            }.let { Locale.of("", landkode.landkode.uppercase()).getDisplayCountry(it) }

        internal fun isValidLandkode(landkode: String): Boolean =
            landkode.length == 2 && alleLandkoder.contains(landkode.uppercase())

    }
}