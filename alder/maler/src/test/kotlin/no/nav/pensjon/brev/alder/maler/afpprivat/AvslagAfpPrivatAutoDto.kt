package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageAfpPrivatDto
import java.time.LocalDate

fun createAvslagAfpPrivatAutoDto(): AvslagAfpPrivatAutoDto =
    AvslagAfpPrivatAutoDto(
        kravMottattDato = LocalDate.of(2026, 1, 15),
        begrunnelse = AvslagAfpPrivatDto.Begrunnelse.BRUKER_AVSLAG_AP,
        dineRettigheterOgMulighetTilAaKlageAfpPrivat = createDineRettigheterOgMulighetTilAaKlageAfpPrivatDto(),
    )
