package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto

fun createOpplysningerBruktIBeregningUTLegacyDto() =
    OpplysningerBruktIBeregningUTLegacyDto(
        pe = createPEgruppe10(),
        ftl_12_2_3_ledd = false,
    )
