package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createOpplysningerBruktIBeregningAlderDto() =
    OpplysningerBruktIBeregningenAlderDto(
        beregnetPensjonPerManedVedVirk = OpplysningerBruktIBeregningenAlderDto.AlderspensjonPerManed(
            virkDatoFom = LocalDate.now(),
            brukersSivilstand = MetaforceSivilstand.EKTEFELLE,
            flyktningstatusErBrukt = false,
            tilleggspensjon = Kroner(200),
        ),
        beregnetSomEnsligPgaInstitusjon = true,
        alderspensjonVedVirk = OpplysningerBruktIBeregningenAlderDto.AlderspensjonVedVirk(
            regelverkType = AlderspensjonRegelverkType.AP2016,
            andelKap19 = 4,
            andelKap20 = 6,
            uttaksgrad = 100,
            skjermingstilleggInnvilget = true,
            erEksportberegnet = false
        ),
        trygdetidsdetaljerKap19VedVirk = OpplysningerBruktIBeregningenAlderDto.TrygdetidsdetaljerKap19VedVirk(
            beregningsmetode = Beregningsmetode.FOLKETRYGD,
            anvendtTT = 40,
            tellerTTEOS = null,
            nevnerTTEOS = null,
            tellerProRata = null,
            nevnerProRata = null
        ),
        trygdetidsdetaljerKap20VedVirk = OpplysningerBruktIBeregningenAlderDto.TrygdetidsdetaljerKap20VedVirk(
            anvendtTT = 40,
            tellerTTEOS = null,
            nevnerTTEOS = null,
            tellerProRata = null,
            nevnerProRata = null,
            beregningsmetode = Beregningsmetode.NORDISK
        ),
        beregningKap19VedVirk = OpplysningerBruktIBeregningenAlderDto.BeregningKap19VedVirk(
            redusertTrygdetid = false,
            sluttpoengtall = null,
            poengAar = 40,
            poengArf92 = 25,
            poengAre91 = 15,
            poengarNevner = null,
            poengArTeller = null,
            skjermingsgrad = 10,
            forholdstall67Soeker = 0.9,
            uforegradVed67 = 80,
            forholdstallLevealder = 1.0
        ),
        beregningKap20VedVirk = OpplysningerBruktIBeregningenAlderDto.BeregningKap20VedVirk(
            redusertTrygdetid = false,
            beholdningForForsteUttak = Kroner(10000),
            delingstallLevealder = 1.0,
            nyOpptjening = Kroner(1500)
        ),
        tilleggspensjonVedVirk = OpplysningerBruktIBeregningenAlderDto.TilleggspensjonVedVirk(
            pgaUngUfore = false
        ),
        yrkesskadeDetaljerVedVirk = OpplysningerBruktIBeregningenAlderDto.YrkesskadeDetaljerVedVirk(
            yrkesskadeUforegrad = 60,
            sluttpoengtall = 1.0,
            poengAr = 40,
            poengArf92 = 25,
            poengAre91 = 15
        ),
        inngangOgEksportVurdering = OpplysningerBruktIBeregningenAlderDto.InngangOgEksportVurdering(
            eksportBeregnetUtenGarantipensjon = false
        ),
        epsVedVirk = OpplysningerBruktIBeregningenAlderDto.EPSvedVirk(
            borSammenMedBruker = true,
            mottarPensjon = true,
            harInntektOver2G = true
        ),
        trygdetidNorge = listOf(),
        trygdetidEOS = listOf(),
        trygdetidAvtaleland = listOf(),
        bruker = OpplysningerBruktIBeregningenAlderDto.Bruker(
            foedselsdato = LocalDate.now()
        ),
        krav = OpplysningerBruktIBeregningenAlderDto.Krav(
            erForstegangsbehandling = true
        ),
        poengrekkeVedVirk = OpplysningerBruktIBeregningenAlderDto.PoengrekkeVedVirk(
            inneholderOmsorgspoeng = true,
            pensjonspoeng = listOf()
        ),
        skalSkjuleTrygdetidstabellerPgaAldersovergang = false
    )