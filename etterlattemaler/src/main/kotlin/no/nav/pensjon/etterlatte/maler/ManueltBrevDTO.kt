package no.nav.pensjon.etterlatte.maler

data class ManueltBrevMedTittelData(
    val tittel: String? = null,
)

data class ManueltBrevMedTittelDTO(
    override val innhold: List<Element> = emptyList(),
    override val data: ManueltBrevMedTittelData = ManueltBrevMedTittelData(),
) : FerdigstillingBrevDTO

data class ManueltBrevDTO(
    override val innhold: List<Element> = emptyList(),
) : BrevDTO