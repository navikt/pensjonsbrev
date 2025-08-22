package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAlderspensjonDto() =
    InnvilgelseAvAlderspensjonDto(
        saksbehandlerValg = InnvilgelseAvAlderspensjonDto.SaksbehandlerValg(
            kravVirkDatoFomSenereEnnOensketUttakstidspunkt = false,
            harGjenlevenderett = false,
            harGjenlevendetillegg = false,
            egenOpptjening = false,
            kildeskatt = false,
        ),
        pesysData = InnvilgelseAvAlderspensjonDto.PesysData(
            afpPrivatResultatFellesKontoret = false,
            alderspensjonVedVirk = InnvilgelseAvAlderspensjonDto.AlderspensjonVedVirk(
                erEksportberegnet = false,
                garantipensjonInnvilget = true,
                garantitilleggInnvilget = true,
                gjenlevenderettAnvendt = false,
                gjenlevendetilleggInnvilget = false,
                gjenlevendetilleggKap19Innvilget = true,
                godkjentYrkesskade = true,
                innvilgetFor67 = false,
                pensjonstilleggInnvilget = false,
                privatAFPErBrukt = false,
                skjermingstilleggInnvilget = false,
                totalPensjon = Kroner(30000),
                uforeKombinertMedAlder = true,
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
            harFlereBeregningsperioder = false,
            inngangOgEksportVurdering = InnvilgelseAvAlderspensjonDto.InngangOgEksportVurdering(
                eksportForbud = false,
                eksportTrygdeavtaleAvtaleland = false,
                eksportTrygdeavtaleEOS = true,
                harOppfyltVedSammenlegging = true, // hvis oppfyltVedSammenleggingKap19 eller oppfyltVedSammenleggingKap20
                minst20ArBotidKap19Avdod = false,
                minst20ArTrygdetid = false,
                minst20ArTrygdetidKap20Avdod = false,
            ),
            kravVirkDatoFom = LocalDate.of(2025, 6, 1),
            norgeBehandlendeLand = false,
            regelverkType = AlderspensjonRegelverkType.AP2016,
            sakstype = Sakstype.ALDER,
            vedtakEtterbetaling = false,
            orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
            maanedligPensjonFoerSkattAP2025Dto = MaanedligPensjonFoerSkattAP2025Dto(
                beregnetPensjonPerManedGjeldende = MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
                    inntektspensjon = Kroner(1000),
                    totalPensjon = Kroner(2000),
                    garantipensjon = Kroner(1000),
                    minstenivaIndividuell = Kroner(1000),
                    virkDatoFom = LocalDate.now(),
                    virkDatoTom = null,
                ),
                beregnetPensjonperManed = listOf(),
                kravVirkFom = LocalDate.now()
            ),
            opplysningerBruktIBeregningenAlderspensjon = createOpplysningerBruktIBeregningAlderDto(),
            opplysningerBruktIBeregningenAlderspensjonAP2025 = createOpplysningerBruktIBeregningAlderAP2025Dto(),
            opplysningerOmAvdodBruktIBeregning = null
        )
    )