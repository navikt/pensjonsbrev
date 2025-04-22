package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvAlderspensjonGjenlevenderettigheterDto() =
    VedtakEndringAvAlderspensjonGjenlevenderettigheterDto(
        saksbehandlerValg = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.SaksbehandlerValg(
            visTilleggspensjonavsnittAP1967 = true,
            omregnetTilEnsligISammeVedtak = true,
            pensjonenOeker = true,
            brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar = true,
            avdoedeHarRedusertTrygdetidEllerPoengaar = true,
            endringIPensjonsutbetaling = true,
            etterbetaling = true
        ),
        pesysData = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.PesysData(
            avdod = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.Avdod(
                navn = "Peder Ås"
            ),
            bruker = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.Bruker(
                fodselsdato = LocalDate.of(2000, Month.JANUARY, 1)
            ),
            krav = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.Krav(
                virkDatoFom = LocalDate.of(2024, Month.FEBRUARY, 2),
                kravInitiertAv = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.KravInitiertAv.NAV
            ),
            alderspensjonVedVirk = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.AlderspensjonVedVirk(
                totalPensjon = Kroner(1000),
                regelverkType = AlderspensjonRegelverkType.AP2011,
                uttaksgrad = 85,
                gjenlevendetilleggKap19Innvilget = true,
                gjenlevenderettAnvendt = true,
                gjenlevendetilleggInnvilget = true,
                saertilleggInnvilget = true,
                minstenivaIndividuellInnvilget = true,
                pensjonstilleggInnvilget = true,
                garantipensjonInnvilget = true,
                harEndretPensjon = true
            ),
            ytelseskomponentInformasjon = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.YtelseskomponentInformasjon(
                beloepEndring = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.BeloepEndring.ENDR_OKT
            ),
            gjenlevendetilleggKapittel19VedVirk = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.GjenlevendetilleggKapittel19VedVirk(
                apKap19utenGJR = 90
            ),
            beregnetPensjonPerManedVedVirk = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.BeregnetPensjonPerManedVedVirk(
                inntektspensjon = 100,
                gjenlevendetilleggKap19 = Kroner(2500),
                gjenlevendetillegg = Kroner(750),
                antallBeregningsperioderPensjon = 2
            ),
            orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt()
        )
    )