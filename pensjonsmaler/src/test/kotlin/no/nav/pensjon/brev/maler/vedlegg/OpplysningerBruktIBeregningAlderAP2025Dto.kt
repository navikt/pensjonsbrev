package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.GarantipensjonSatsType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

fun createOpplysningerBruktIBeregningAlderAP2025Dto() = OpplysningerBruktIBeregningenAlderAP2025Dto(
    alderspensjonVedVirk = OpplysningerBruktIBeregningenAlderAP2025Dto.AlderspensjonVedVirk(
        uttaksgrad = 100,
        garantipensjonInnvilget = true,
        nettoUtbetaltPerManed = Kroner(1000)
    ),
    beregnetPensjonPerManedVedVirk = OpplysningerBruktIBeregningenAlderAP2025Dto.BeregnetPensjonPerManedVedVirk(
        virkDatoFom = LocalDate.of(2024, Month.JANUARY, 5),
        brukersSivilstand = MetaforceSivilstand.GIFT
    ),
    beregningKap20VedVirk = OpplysningerBruktIBeregningenAlderAP2025Dto.BeregningKap20VedVirk(
        beholdningForForsteUttak = Kroner(100),
        delingstallLevealder = 1.0,
        redusertTrygdetid = false,
    ),
    vilkarsVedtak = OpplysningerBruktIBeregningenAlderAP2025Dto.VilkaarsVedtak(
        avslattGarantipensjon = false
    ),
    vedtak = OpplysningerBruktIBeregningenAlderAP2025Dto.Vedtak(
        sisteOpptjeningsAr = 2024
    ),
    garantipensjonVedVirk = OpplysningerBruktIBeregningenAlderAP2025Dto.GarantipensjonVedVirk(
        delingstalletVed67Ar = 1.0,
        satsType = GarantipensjonSatsType.HOY,
        garantipensjonSatsPerAr = Kroner(2000),
        nettoUtbetaltPerManed = Kroner(3000),
        beholdningForForsteUttak = Kroner(50)
    ),
    trygdetidsdetaljerKap20VedVirk = OpplysningerBruktIBeregningenAlderAP2025Dto.TrygdetidsdetaljerKap20VedVirk(
        anvendtTT = 40
    ),
    epsVedVirk = OpplysningerBruktIBeregningenAlderAP2025Dto.EpsVedVirk(
        borSammenMedBruker = true,
        harInntektOver2G = true,
        mottarPensjon = false
    ),
    erBeregnetSomEnsligPgaInstitusjonsopphold = false,
    trygdetidNorge = listOf(),
    trygdetidEOS = listOf(),
    trygdetidAvtaleland = listOf(),
    pensjonsopptjeningKap20VedVirk = OpplysningerBruktIBeregningenAlderAP2025Dto.PensjonsopptjeningKap20VedVirk(
        harOmsorgsopptjeningFOM2010 = false,
        harOmsorgsopptjeningTOM2009 = false,
        harDagpenger = false,
        harUforetrygd = false,
        harUforepensjonKonvertertTilUforetrygd = false,
        harUforepensjon = false,
        harMerknadType = false,
        pensjonsopptjeninger = listOf()
    )
)