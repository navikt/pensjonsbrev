package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.AlderspensjonBeregnetEtter
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


private fun createAlderspensjonPerManed() = MaanedligPensjonFoerSkattDto.AlderspensjonPerManed(
    virkDatoFom = LocalDate.of(2020, 1, 1),
    virkDatoTom = LocalDate.of(2020, 1, 2),
    grunnpensjon = Kroner(12),
    tilleggspensjon = Kroner(12),
    saertillegg = Kroner(12),
    pensjonstillegg = Kroner(12),
    skjermingstillegg = Kroner(12),
    gjenlevendetilleggKap19 = Kroner(12),
    minstenivaPensjonistPar = Kroner(12),
    minstenivaIndividuell = Kroner(12),
    fasteUtgifter = Kroner(12),
    familieTillegg = Kroner(12),
    fullTrygdetid = true,
    beregnetEtter = AlderspensjonBeregnetEtter.EGEN,
    grunnbeloep = Kroner(12),
    ektefelletillegg = Kroner(12),
    barnetilleggSB = Kroner(12),
    barnetilleggSBbrutto = Kroner(12),
    barnetilleggFB = Kroner(12),
    barnetilleggFBbrutto = Kroner(12),
    inntektspensjon = Kroner(12),
    inntektBruktIavkortningET = Kroner(12),
    inntektBruktIavkortningSB = Kroner(12),
    inntektBruktIavkortningFB = Kroner(12),
    fribelopET = Kroner(12),
    fribelopFB = Kroner(12),
    fribelopSB = Kroner(12),
    garantipensjon = Kroner(12),
    garantitillegg = Kroner(12),
    gjenlevendetillegg = Kroner(12),
    totalPensjon = Kroner(12),
    flyktningstatusErBrukt = true,
    avdodFlyktningstatusErBrukt = true,
    brukersSivilstand = MetaforceSivilstand.GIFT
)

internal fun createMaanedligPensjonFoerSkatt() = MaanedligPensjonFoerSkattDto(
    beregnetPensjonPerManedGjeldende = createAlderspensjonPerManed(),
    alderspensjonGjeldende = MaanedligPensjonFoerSkattDto.AlderspensjonGjeldende(
        regelverkstype = AlderspensjonRegelverkType.AP2016,
        erEksportberegnet = true,
        andelKap19 = 50,
        andelKap20 = 50,
        grunnpensjonSats = 100,
        gjenlevendetilleggKap19Innvilget = true,
        gjenlevendetilleggInnvilget = true
    ),
    institusjonsoppholdGjeldende = MaanedligPensjonFoerSkattDto.InstitusjonsoppholdGjeldende(
        aldersEllerSykehjem = true,
        epsPaInstitusjon = true,
        fengsel = true,
        helseinstitusjon = true,
        ensligPaInst = true,
    ),
    epsGjeldende = MaanedligPensjonFoerSkattDto.EPSgjeldende(
        mottarPensjon = true,
        borSammenMedBruker = true,
        harInntektOver2G = true
    ),
    ektefelletilleggGjeldende = MaanedligPensjonFoerSkattDto.EktefelletilleggGjeldende(
        innvilgetEktefelletillegg = true
    ),
    tilleggspensjonGjeldende = MaanedligPensjonFoerSkattDto.TilleggspensjonGjeldende(
        erRedusert = true,
        kombinertMedAvdod = true,
        pgaYrkesskade = true,
        pgaUngUfore = true,
        pgaUngUforeAvdod = true,
        pgaYrkesskadeAvdod = true,
    ),
    saerskiltSatsGjeldende = MaanedligPensjonFoerSkattDto.SaerskiltSatsGjeldende(
        saerskiltSatsErBrukt = true
    ),
    barnetilleggGjeldende = MaanedligPensjonFoerSkattDto.BarnetilleggGjeldende(
        innvilgetBarnetilleggFB = true,
        innvilgetBarnetilleggSB = true
    ),
    bruker = MaanedligPensjonFoerSkattDto.Bruker(
        foedselsDato = LocalDate.of(1950, 1, 1),
        sivilstand = MetaforceSivilstand.GIFT
    ),
    krav = MaanedligPensjonFoerSkattDto.Krav(
        virkDatoFom = LocalDate.of(2020, 1, 1)
    ),
    alderspensjonPerManed = listOf(createAlderspensjonPerManed(), createAlderspensjonPerManed())
)