package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAlderspensjonTrygdeavtaleDto() =
    InnvilgelseAvAlderspensjonTrygdeavtaleDto(
        saksbehandlerValg = InnvilgelseAvAlderspensjonTrygdeavtaleDto.SaksbehandlerValg(
            nyBeregningAvInnvilgetAP = false,
            innvilgelseAvAPEllerOektUttaksgrad = false,
            ingenEndringIPensjonen = false,
            innvilgelseAPellerOektUttaksgrad = false,
            oekningIPensjonen = false,
            reduksjonIPensjonen = false,
            supplerendeStoenad = false,
            etterbetaling = false,
        ),
        pesysData = InnvilgelseAvAlderspensjonTrygdeavtaleDto.PesysData(
            afpPrivatResultatFellesKontoret = false,
            alderspensjonVedVirk = InnvilgelseAvAlderspensjonTrygdeavtaleDto.AlderspensjonVedVirk(
                garantipensjonInnvilget = false,
                gjenlevenderettAnvendt = false,
                gjenlevendetilleggKap19Innvilget = false,
                godkjentYrkesskade = false,
                innvilgetFor67 = false,
                pensjonstilleggInnvilget = true,
                privatAFPErBrukt = false,
                skjermingstilleggInnvilget = false,
                totalPensjon = Kroner(30000),
                uforeKombinertMedAlder = false,
                uttaksgrad = 100,
            ),
            avtalelandNavn = null,
            borIAvtaleland = false,
            borINorge = false,
            erEOSLand = false,
            erMellombehandling = true,
            erSluttbehandlingNorgeUtland = true,
            fullTrygdtid = false,
            harFlereBeregningsperioder = false,
            inngangOgEksportVurdering = InnvilgelseAvAlderspensjonTrygdeavtaleDto.InngangOgEksportVurdering(
                eksportTrygdeavtaleAvtaleland = false,
                eksportTrygdeavtaleEOS = true,
                harOppfyltVedSammenlegging = true, // hvis oppfyltVedSammenleggingKap19 eller oppfyltVedSammenleggingKap20
            ),
            kravVirkDatoFom = LocalDate.of(2025, 6, 1),
            regelverkType = AlderspensjonRegelverkType.AP2016,
            sakstype = Sakstype.ALDER,
            vedtakEtterbetaling = false,
            vedtaksresultatUtland = InnvilgelseAvAlderspensjonTrygdeavtaleDto.VedtaksresultatUtland(
                antallLandVilkarsprovd = 1,
                landNavn = "Sverige",
            ),
            dineRettigheterOgMulighetTilAaKlageDto = DineRettigheterOgMulighetTilAaKlageDto(
                sakstype = Sakstype.ALDER,
                brukerUnder18Aar = false
            ),
            maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
            maanedligPensjonFoerSkattAP2025Dto = MaanedligPensjonFoerSkattAP2025Dto(
                beregnetPensjonPerManedGjeldende = MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
                    inntektspensjon = Kroner(1000),
                    totalPensjon = Kroner(2000),
                    garantipensjonInnvilget = Kroner(500),
                    garantipensjon = Kroner(1000),
                    minstenivaIndividuell = Kroner(1000),
                    virkDatoFom = LocalDate.now(),
                    virkDatoTom = null,
                ),
                beregnetPensjonperManed = listOf(),
                kravVirkFom = LocalDate.now()
            ),
        )
    )