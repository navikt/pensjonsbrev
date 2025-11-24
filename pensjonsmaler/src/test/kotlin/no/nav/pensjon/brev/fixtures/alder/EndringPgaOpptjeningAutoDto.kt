package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.alderApi.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringPgaOpptjeningAutoDto() = EndringPgaOpptjeningAutoDto(
    opptjeningType = OpptjeningType.TILVEKST,
    opptjening = Opptjening(
        sisteGyldigeOpptjeningsAar = 2023,
        antallAarEndretOpptjening = 2,
        endretOpptjeningsAar = emptySet()
    ),
    belopEndring = BeloepEndring.ENDR_OKT,
    beregnetPensjonPerMaaned = BeregnetPensjonPerMaaned(antallBeregningsperioderPensjon = 1),
    beregnetPensjonPerMaanedGjeldende = BeregnetPensjonPerMaanedGjeldende(
        totalPensjon = Kroner(20000),
        virkFom = LocalDate.of(2025, 1, 1),
    ),
    beregnetPensjonPerMaanedVedVirk = BeregnetPensjonPerMaanedVedVirk(
        uttaksgrad = 100,
        totalPensjon = Kroner(21000),
        virkFom = LocalDate.of(2025, 1, 1),
        minstenivaIndividuellInnvilget = false,
        pensjonstilleggInnvilget = true,
        minstenivaPensjonistParInnvilget = false,
        gjenlevenderettAnvendt = false,
        garantipensjonInnvilget = false,
    ),
    virkFom = LocalDate.of(2025, 1, 1),
    borINorge = true,
    erFoerstegangsbehandling = false,
    uforeKombinertMedAlder = false,
    regelverkType = AlderspensjonRegelverkType.AP2025,
    orienteringOmRettigheterOgPlikter = createOrienteringOmRettigheterOgPlikterDto(),
    opplysningerBruktIBeregningenAlder = null,
    opplysningerBruktIBeregningenAlderAP2025 = null,
    maanedligPensjonFoerSkatt = null,
    maanedligPensjonFoerSkattAP2025 = null,
    opplysningerOmAvdoedBruktIBeregning = null,
    opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjening = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto(
        alderspensjonVedVirk = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.AlderspensjonVedVirk(
            uttaksgrad = 100,
            garantipensjonInnvilget = true,
        ),
        beregnetPensjonPerManedVedVirk = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.BeregnetPensjonPerManedVedVirk(
            virkDatoFom = LocalDate.now(),
            brukersSivilstand = MetaforceSivilstand.ENSLIG
        ),
        beregningKap20VedVirk = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.BeregningKap20VedVirk(
            beholdningForForsteUttak = Kroner(20000),
            delingstallLevealder = 17.2,
            redusertTrygdetid = false,
            nyOpptjening = Kroner(100),
        ),
        vilkarsVedtak = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.VilkaarsVedtak(
            avslattGarantipensjon = false
        ),
        vedtak = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.Vedtak(
            sisteOpptjeningsAr = 2024
        ),
        garantipensjonVedVirk = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.GarantipensjonVedVirk(
            nettoUtbetaltPerManed = Kroner(1000),
        ),
        trygdetidsdetaljerKap20VedVirk = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.TrygdetidsdetaljerKap20VedVirk(
            anvendtTT = 38
        ),
        epsVedVirk = null,
        erBeregnetSomEnsligPgaInstitusjonsopphold = false,
        trygdetidNorge = emptyList(),
        trygdetidEOS = emptyList(),
        trygdetidAvtaleland = emptyList(),
        pensjonsopptjeningKap20VedVirk = OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.PensjonsopptjeningKap20VedVirk(
            harOmsorgsopptjeningFOM2010 = true,
            harOmsorgsopptjeningTOM2009 = true,
            harDagpenger = true,
            harUforetrygd = true,
            harUforepensjonKonvertertTilUforetrygd = true,
            harMerknadType = true,
            pensjonsopptjeninger = listOf(
                OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.Pensjonsopptjening(
                    aarstall = 2024,
                    pensjonsgivendeinntekt = Kroner(500000),
                    gjennomsnittligG = Kroner(124000),
                    merknader = listOf(
                        OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.Pensjonsopptjening.Merknad.DAGPENGER,
                        OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.Pensjonsopptjening.Merknad.UFORETRYGD,
                        OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.Pensjonsopptjening.Merknad.DAGPENGER,
                    ),
                )
            ),
        )
    ),
)
