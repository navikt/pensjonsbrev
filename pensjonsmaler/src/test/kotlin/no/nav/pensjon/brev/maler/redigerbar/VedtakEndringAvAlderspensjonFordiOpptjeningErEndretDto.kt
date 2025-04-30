package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BelopEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto() =
    VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto(
        saksbehandlerValg = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.SaksbehandlerValg(
            visUendretOpptjening = true,
            visOektOpptjening = false,
            visRedusertOpptjening = false
        ),
        pesysData = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.PesysData(
            krav = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.Krav(
                virkDatoFom = LocalDate.of(2024, Month.MAY, 1),
                arsakErEndretOpptjening = true,
                erForstegangsbehandling = true
            ),
            alderspensjonVedVirk = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.AlderspensjonVedVirk(
                skjermingstilleggInnvilget = true,
                totalPensjon = Kroner(1000),
                skjermingstillegg = Kroner(500),
                uforeKombinertMedAlder = true,
                regelverkType = AlderspensjonRegelverkType.AP2016,
                fullUttaksgrad = true
            ),
            ytelseskomponentInformasjon = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.YtelseskomponentInformasjon(
                belopEndring = BelopEndring.ENDR_OKT
            ),
            behandlingKontekst = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.BehandlingKontekst(
                konteksttypeErKorrigeringopptjening = false
            ),
            orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            maanedligPensjonFoerSkattDto = Fixtures.create(MaanedligPensjonFoerSkattDto::class),
            maanedligPensjonFoerSkattAP2025Dto = Fixtures.create(MaanedligPensjonFoerSkattAP2025Dto::class),
            opplysningerBruktIBeregningenAlderDto = createOpplysningerBruktIBeregningAlderDto()
        )
    )