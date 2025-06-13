package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAlderspensjonDto() =
    InnvilgelseAvAlderspensjonDto(
        saksbehandlerValg = InnvilgelseAvAlderspensjonDto.SaksbehandlerValg(
            kravVirkDatoFomSenereEnnOensketUttakstidspunkt = false,
            harGjenlevenderett = false,
            harGjenlevendetillegg = false,
            harGjenlevendetilleggKap19 = false,
            egenOpptjening = false,
            supplerendeStoenad = false,
            kildeskatt = false,
            ikkeKildeskatt = false,
            etterbetaling = false,
        ),
        pesysData = InnvilgelseAvAlderspensjonDto.PesysData(
            afpPrivatResultatFellesKontoret = false,
            alderspensjonVedVirk = InnvilgelseAvAlderspensjonDto.AlderspensjonVedVirk(
                erEksportberegnet = false,
                garantipensjonInnvilget = false,
                garantitilleggInnvilget = false,
                gjenlevenderettAnvendt = false,
                gjenlevendetilleggInnvilget = false,
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
            avdodFnr = null,
            avdodNavn = null,
            avtalelandNavn = null,
            borIAvtaleland = false,
            borINorge = false,
            erEOSLand = false,
            erForstegangsbehandletNorgeUtland = false,
            faktiskBostedsland = null,
            fullTrygdtid = false,
            gjenlevendetilleggKap19 = Kroner(0),
            harAvdod = false,
            harFlereBeregningsperioder = false,
            inngangOgEksportVurdering = InnvilgelseAvAlderspensjonDto.InngangOgEksportVurdering(
                eksportForbud = false,
                eksportTrygdeavtaleAvtaleland = false,
                eksportTrygdeavtaleEOS = true,
                harOppfyltVedSammenlegging = true, // hvis oppfyltVedSammenleggingKap19 eller oppfyltVedSammenleggingKap20
                minst20ArBotidKap19 = false,
                minst20ArTrygdetid = false,
                minst20ArTrygdetidKap20 = false,
            ),
            kravVirkDatoFom = LocalDate.of(2025, 6, 1),
            norgeBehandlendeLand = false,
            regelverkType = AlderspensjonRegelverkType.AP2016,
            sakstype = Sakstype.ALDER,
            vedtakEtterbetaling = false,
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