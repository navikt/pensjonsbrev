package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAlderspensjonTrygdeavtaleDto() =
    InnvilgelseAvAlderspensjonTrygdeavtaleDto(
        saksbehandlerValg = InnvilgelseAvAlderspensjonTrygdeavtaleDto.SaksbehandlerValg(
            nyBeregningAvInnvilgetAP = false,
            medfoererInnvilgelseAvAPellerOektUttaksgrad = false,
            beloepEndring = BeloepEndring.UENDRET
        ),
        pesysData = InnvilgelseAvAlderspensjonTrygdeavtaleDto.PesysData(
            afpPrivatResultatFellesKontoret = false,
            alderspensjonVedVirk = InnvilgelseAvAlderspensjonTrygdeavtaleDto.AlderspensjonVedVirk(
                garantipensjonInnvilget = false,
                garantitilleggInnvilget = false,
                gjenlevenderettAnvendt = false,
                gjenlevendetilleggKap19Innvilget = false,
                godkjentYrkesskade = false,
                innvilgetFor67 = false,
                pensjonstilleggInnvilget = false,
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
            erSluttbehandlingNorgeUtland = false,
            fullTrygdtid = false,
            harFlereBeregningsperioder = false,
            inngangOgEksportVurdering = InnvilgelseAvAlderspensjonTrygdeavtaleDto.InngangOgEksportVurdering(
                eksportTrygdeavtaleAvtaleland = true,
                eksportTrygdeavtaleEOS = true,
                harOppfyltVedSammenlegging = false, // If one of these is true: oppfyltVedSammenleggingKap19 or oppfyltVedSammenleggingKap20 or oppfyltVedSammenleggingFemArKap19 or oppfyltVedSammenleggingFemArKap20
            ),
            kravVirkDatoFom = LocalDate.of(2025, 6, 1),
            regelverkType = AlderspensjonRegelverkType.AP2025,
            sakstype = Sakstype.ALDER,
            vedtakEtterbetaling = true,
            vedtaksresultatUtland = InnvilgelseAvAlderspensjonTrygdeavtaleDto.VedtaksresultatUtland(
                antallLandVilkarsprovd = 2,
                landNavn = listOf("Sverige", "Finland"),
            ),
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
            opplysningerOmAvdodBruktIBeregning = null,
            orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto()
        )
    )