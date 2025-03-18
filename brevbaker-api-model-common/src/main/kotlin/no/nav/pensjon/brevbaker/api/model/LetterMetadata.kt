package no.nav.pensjon.brevbaker.api.model

/**
 * @property displayTitle Visningstittel i arkivet. Brukes for visning til saksbehandler og bruker. Se https://confluence.adeo.no/display/BOA/QDIST010+-+DistribuerForsendelseTilDittNAV
 * @property isSensitiv Styrer om innholdet i brevet er sensitivt. Sensitive brev skal ikke vises ved nivå 3 innlogging
 * @property distribusjonstype Styrer viktigheten og typen varsel til bruker under distribusjon. Les mer på: https://confluence.adeo.no/display/BOA/RDIST002-1.+DistribuerJournalpost+-+For+Konsumenter
 * @property brevtype Endrer på enkelte standard-tekster i grunn-malen til brevet avhengig av om brevet er ett vedtak eller ikke.
 */
data class LetterMetadata(
    val displayTitle: String,
    val isSensitiv: Boolean,
    val distribusjonstype: Distribusjonstype,
    val brevtype: Brevtype,
) {
    enum class Distribusjonstype { VEDTAK, VIKTIG, ANNET, }
    enum class Brevtype{ VEDTAKSBREV, INFORMASJONSBREV }
}
