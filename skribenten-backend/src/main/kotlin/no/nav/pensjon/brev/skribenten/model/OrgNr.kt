package no.nav.pensjon.brev.skribenten.model

@JvmInline
value class OrgNr(val value: String) {
    init {
        require(NI_SIFFER.matches(value)) { "Organisasjonsnummer må være ni siffer" }
    }

    override fun toString() = value

    companion object {
        private val NI_SIFFER = Regex("\\d{9}")
        private val TSS_ORGNR = Regex("00(\\d{9})")

        fun normaliser(orgNr: String): OrgNr? =
            when {
                NI_SIFFER.matches(orgNr) -> OrgNr(orgNr)
                else -> TSS_ORGNR.matchEntire(orgNr)?.groupValues?.get(1)?.let(::OrgNr)
            }
    }
}
