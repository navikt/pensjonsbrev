package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDto
import no.nav.pensjon.brev.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto() =
    VedtakEndringAvAlderspensjonInstitusjonsoppholdDto(
        saksbehandlerValg = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.SaksbehandlerValg(
            alderspensjonUnderOppholdIInstitusjon = true,
            alderspensjonUnderSoning = true,
            alderspensjonVedVaretektsfengsling = true,
            alderspensjonRedusert = true,
            alderspensjonStanset = true,
            informasjonOmSivilstandVedInstitusjonsopphold = true,
            hvisReduksjonTilbakeITid = true,
            hvisEtterbetaling = true,
            hvisEndringIPensjon = true,
        ),
        pesysData = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.PesysData(
            beregnetPensjonPerManedVedVirk = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.PesysData.BeregnetPensjonPerManedVedVirk(
                totalPensjon = Kroner(1000),
                antallBeregningsperioderPensjon = 5
            ),
            krav = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.PesysData.Krav(
                virkDatoFom = LocalDate.of(2020, Month.JULY, 1)
            ),
            institusjonsoppholdVedVirk = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.PesysData.InstitusjonsoppholdVedVirk(
                helseinstitusjon = true,
                fengsel = true
            ),
            alderspensjonVedVirk = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.PesysData.AlderspensjonVedVirk(
                totalPensjon = Kroner(200),
                uforeKombinertMedAlder = true,
                regelverkType = AlderspensjonRegelverkType.AP2011
            ),
            orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            maanedligPensjonFoerSkattAlderspensjonDto = createMaanedligPensjonFoerSkattAlderspensjonDto()
        )
    )