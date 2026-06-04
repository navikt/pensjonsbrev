package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.AarsakEndring
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.Avdoed
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.Beregning
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.EktefelleData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.ForventetInntektNivaa
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.Komponent
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.PesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.Saksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.SkattAlternativ
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.Sivilstand
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.UtbetalingAlternativ
import no.nav.pensjon.brev.fixtures.legacy.createOpplysningerOmBeregningGPUtlandDto
import no.nav.pensjon.brev.fixtures.legacy.createOversiktOverPensjonensStoerrelseGjenlevendepensjonDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createVedtakEndringGjenlevendepensjonBosattUtlandDto() =
    VedtakEndringGjenlevendepensjonBosattUtlandDto(
        saksbehandlerValg = Saksbehandlervalg(
            aarsakEndring = AarsakEndring.OEKNING_AV_INNTEKT,
            forventetInntektNivaa = ForventetInntektNivaa.OVER_HALV_G,
            harBehovForOppfoelging = true,
            skattAlternativ = SkattAlternativ.INGEN_INFORMASJON_OM_SKATT,
            utbetalingAlternativ = UtbetalingAlternativ.ETTERBETALING,
        ),
        pesysData = PesysData(
            virkningFom = LocalDate.of(2026, 1, 1),
            beregning = Beregning(
                virkDatoFom = LocalDate.of(2026, 1, 1),
                grunnbeloep = Kroner(124_028),
                framtidigAarligInntekt = Kroner(250_000),
                brutto = Kroner(15_000),
                netto = Kroner(15_000),
                grunnpensjon = Komponent(brutto = Kroner(12_000), netto = Kroner(11_000)),
                tilleggspensjon = Komponent(brutto = Kroner(2_000), netto = Kroner(1_000)),
                saertillegg = Komponent(brutto = Kroner(1_000), netto = Kroner(500)),
                fasteUtgifter = null,
                familietillegg = null,
                harYrkesskadegradFraAvdoed = false,
            ),
            avdoed = Avdoed(
                doedsfallSkyldesYrkesskade = false,
                flyktning = false,
                ungUfoerFodtEtter1940 = true,
                ungUfoerFodtFor1941 = false,
            ),
            ektefelle = EktefelleData(
                mottarPensjon = false,
                inntektOver2g = false,
            ),
            sivilstand = Sivilstand.SAMBOER_3_2,
            opplysningerOmBeregningen = createOpplysningerOmBeregningGPUtlandDto(),
            oversiktOverPensjonensStoerrelse = createOversiktOverPensjonensStoerrelseGjenlevendepensjonDto(),
        ),
    )

