package no.nav.pensjon.brev.alder.maler.endring

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
        beregnetPensjonPerManedVedVirk = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.BeregnetPensjonPerManedVedVirk(
            totalPensjon = Kroner(1000),
            antallBeregningsperioderPensjon = 5
        ),
        krav = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.Krav(
            virkDatoFom = LocalDate.of(2020, Month.JULY, 1)
        ),
        institusjonsoppholdVedVirk = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.InstitusjonsoppholdVedVirk(
            helseinstitusjon = true,
            fengsel = true
        ),
        alderspensjonVedVirk = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.AlderspensjonVedVirk(
            totalPensjon = Kroner(200),
            uforeKombinertMedAlder = true,
            regelverkType = AlderspensjonRegelverkType.AP2011
        ),
        beloepEndring = BeloepEndring.ENDR_RED,
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
        maanedligPensjonFoerSkattAlderspensjonDto = createMaanedligPensjonFoerSkattAlderspensjonDto(),
    )