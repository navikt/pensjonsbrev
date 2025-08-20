package no.nav.pensjon.etterlatte.maler

data class ManueltBrevMedTittelDTO(
    override val innhold: List<Element> = emptyList(),
    val tittel: String? = null,
) : FerdigstillingBrevDTO

data class ManueltBrevDTO(
    override val innhold: List<Element> = emptyList(),
) : BrevDTO