package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageAfpPrivatDto

fun createDineRettigheterOgMulighetTilAaKlageAfpPrivatDto() = DineRettigheterOgMulighetTilAaKlageAfpPrivatDto(
    sakstype = Sakstype.AFP_PRIVAT,
)
