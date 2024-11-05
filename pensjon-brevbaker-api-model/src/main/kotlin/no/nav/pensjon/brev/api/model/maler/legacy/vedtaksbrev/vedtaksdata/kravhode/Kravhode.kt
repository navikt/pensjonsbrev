package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.kravhode

import java.time.LocalDate

data class Kravhode(
    val boddarbeidutland: Boolean?,
    val boddarbeidutlandavdod: Boolean?,
    val brukerkonvertertup: Boolean?,
    val kravarsaktype: String?,
    val kravgjelder: String?,
    val kravlinjeliste: List<Kravlinje>,
    val kravmottattdato: LocalDate?,
    val vurderetrygdeavtale: Boolean,
    val onsketvirkningsdato: LocalDate?,
    )