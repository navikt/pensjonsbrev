package no.nav.pensjon.brevbaker.api.model

import java.util.Objects

/**
 * @property displayTitle Visningstittel i arkivet og brevmeny. Brukes for visning til saksbehandler og bruker. Se https://confluence.adeo.no/display/BOA/QDIST010+-+DistribuerForsendelseTilDittNAV
 * @property distribusjonstype Styrer viktigheten og typen varsel til bruker under distribusjon. Les mer på: https://confluence.adeo.no/display/BOA/RDIST002-1.+DistribuerJournalpost+-+For+Konsumenter
 * @property brevtype Endrer på enkelte standard-tekster i grunn-malen til brevet avhengig av om brevet er ett vedtak eller ikke.
 */
class LetterMetadata(
    val displayTitle: String,
    val distribusjonstype: Distribusjonstype,
    val brevtype: Brevtype,
) {
    enum class Distribusjonstype { VEDTAK, VIKTIG, ANNET, }
    enum class Brevtype{ VEDTAKSBREV, INFORMASJONSBREV }

    override fun equals(other: Any?): Boolean {
        if (other !is LetterMetadata) return false
        return displayTitle == other.displayTitle
                && distribusjonstype == other.distribusjonstype
                && brevtype == other.brevtype
    }

    override fun hashCode() = Objects.hash(displayTitle, distribusjonstype, brevtype)

    override fun toString() =
        "LetterMetadata(displayTitle='$displayTitle', distribusjonstype=$distribusjonstype, brevtype=$brevtype)"

}
