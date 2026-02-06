package no.nav.pensjon.brev.alder.maler.sivilstand

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BorMedSivilstand
import no.nav.pensjon.brev.alder.model.Institusjon
import no.nav.pensjon.brev.alder.model.SivilstandAvdoed
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonAvdodAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createEndringAvAlderspensjonAvdodAuto() = EndringAvAlderspensjonAvdodAutoDto(
    alderspensjonVedVirk = EndringAvAlderspensjonAvdodAutoDto.AlderspensjonVedVirk(
        harEndretPensjon = true,
        totalPensjon = Kroner(6000000),
        regelverkType = AlderspensjonRegelverkType.AP1967,
        uttaksgrad = 50,
        minstenivaIndividuellInnvilget = true
    ),
    beregnetPensjonPerManed = EndringAvAlderspensjonAvdodAutoDto.BeregnetPensjonPerManed(
        antallBeregningsperioderPensjon = 2,
        erPerioderMedUttak = true,
        garantiPensjon = Kroner(5000)
    ),
    avdodInformasjon = EndringAvAlderspensjonAvdodAutoDto.AvdodInformasjon(
        sivilstandAvdoed = SivilstandAvdoed.SAMBOER3_2,
        ektefelletilleggOpphort = true,
        gjenlevendesAlder = 68,
        avdodNavn = "Test Testio"
    ),
    institusjonsoppholdVedVirk = Institusjon.FENGSEL,
    institusjonsoppholdGjeldende = Institusjon.FENGSEL,
    sivilstand = BorMedSivilstand.GIFT_LEVER_ADSKILT,
    virkFom = vilkaarligDato,
    harBarnUnder18 = true,
    etterBetaling = true,
    orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
    maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
    maanedligPensjonFoerSkattAP2025Dto = createMaanedligPensjonFoerSkattAP2025Dto(),
    opplysningerBruktIBeregningenAlderDto = createOpplysningerBruktIBeregningAlderDto(),
    opplysningerOmAvdoedBruktIBeregningDto = createOpplysningerOmAvdoedBruktIBeregningDto(),
    informasjonOmMedlemskap = null,
)