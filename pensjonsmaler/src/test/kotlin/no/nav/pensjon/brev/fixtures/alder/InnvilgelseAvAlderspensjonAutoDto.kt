package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BorI
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAlderspensjonAutoDto() =
    InnvilgelseAvAlderspensjonAutoDto(
        afpPrivatResultatFellesKontoret = false,
        alderspensjonVedVirk = InnvilgelseAvAlderspensjonAutoDto.AlderspensjonVedVirk(
            erEksportberegnet = false,
            garantipensjonInnvilget = false,
            garantitilleggInnvilget = false,
            gjenlevenderettAnvendt = false,
            gjenlevendetilleggKap19Innvilget = false,
            godkjentYrkesskade = false,
            innvilgetFor67 = true,
            pensjonstilleggInnvilget = false,
            privatAFPErBrukt = false,
            skjermingstilleggInnvilget = false,
            totalPensjon = Kroner(30000),
            uforeKombinertMedAlder = true,
            uttaksgrad = 50,
        ),
        avtalelandNavn = "Norge",
        borI = BorI.NORGE,
        erEOSLand = false,
        erForstegangsbehandletNorgeUtland = true,
        faktiskBostedsland = "Norge",
        fullTrygdetid = true,
        harFlereBeregningsperioder = true,
        inngangOgEksportVurdering = InnvilgelseAvAlderspensjonAutoDto.InngangOgEksportVurdering(
            eksportTrygdeavtaleAvtaleland = false,
            eksportTrygdeavtaleEOS = false,
            eksportBeregnetUtenGarantipensjon = false,
            harOppfyltVedSammenlegging = false
        ),
        kravVirkDatoFom = LocalDate.of(2025, 6, 1),
        norgeBehandlendeLand = true,
        regelverkType = AlderspensjonRegelverkType.AP2025,
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
        )