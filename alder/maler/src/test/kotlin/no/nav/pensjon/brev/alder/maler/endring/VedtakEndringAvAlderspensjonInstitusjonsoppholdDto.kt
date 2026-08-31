package no.nav.pensjon.brev.alder.maler.endring

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.endring.VedtakEndringAvAlderspensjonInstitusjonsoppholdDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvAlderspensjonInstitusjonsoppholdDto() =
    VedtakEndringAvAlderspensjonInstitusjonsoppholdDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "alderspensjonUnderOppholdIInstitusjon" to true,
            "alderspensjonUnderSoning" to true,
            "alderspensjonVedVaretektsfengsling" to true,
            "alderspensjonRedusert" to true,
            "alderspensjonStanset" to true,
            "informasjonOmSivilstandVedInstitusjonsopphold" to true,
            "hvisReduksjonTilbakeITid" to true,
            "etterbetaling" to true
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
            beloepEndring = BeloepEndring.ENDR_RED,
            orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            maanedligPensjonFoerSkattAlderspensjonDto = createMaanedligPensjonFoerSkattAlderspensjonDto(),
        )
    )