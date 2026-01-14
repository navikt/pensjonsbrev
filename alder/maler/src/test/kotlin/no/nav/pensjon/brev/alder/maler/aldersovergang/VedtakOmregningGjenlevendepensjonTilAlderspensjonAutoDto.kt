package no.nav.pensjon.brev.alder.maler.aldersovergang

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createVedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto() =
    VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto(
        virkFom = vilkaarligDato,
        alderspensjonVedVirk = VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto.AlderspensjonVedVirk(
            totalPensjon = Kroner(20_000),
            uttaksgrad = 50,
            gjenlevenderettAnvendt = true,
            erEksportBeregnet = true,
            regelverkType = AlderspensjonRegelverkType.AP2011,
            innvilgetFor67 = true,
            godkjentYrkesskade = true,
            pensjonstilleggInnvilget = null,
            garantipensjonInnvilget = true,
        ),
        beregnetPensjonPerMaaned = VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto.BeregnetPensjonPerMaaned(
            antallBeregningsperioderPensjon = 1,
        ),
        avdod = VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto.Avdod(
            navn = "Ola Nordmann",
            avdodFnr = null,
        ),
        bruker = VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto.Bruker(
            faktiskBostedsland = "Norge",
            borINorge = true,
        ),
        inngangOgEksportVurdering = VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto.InngangOgEksportVurdering(
            minst20ArTrygdetid = true,
            eksportTrygdeavtaleAvtaleland = null,
            eksportTrygdeavtaleEOS = null,
            eksportBeregnetUtenGarantipensjon = true,
        ),
        beregnetPensjonPerManedVedVirk = VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto.BeregnetPensjonPerManedVedVirk(
            gjenlevendetillegg = 2000,
        ),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(Sakstype.ALDER),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025Dto = null,
    )

