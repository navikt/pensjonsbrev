package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brev.maler.example.*
import java.time.LocalDate
import kotlin.reflect.KClass

object Fixtures {

    val felles = Felles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = NAVEnhet(
            returAdresse = ReturAdresse(
                adresseLinje1 = "Postboks 6600 Etterstad",
                postNr = "0607",
                postSted = "Oslo",
            ),
            nettside = "nav.no",
            navn = "NAV Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = Telefonnummer("55553334"),
        ),
        bruker = Bruker(
            fornavn = "TEST",
            mellomnavn = "\"bruker\"",
            etternavn = "TESTERSON",
            foedselsnummer = Foedselsnummer("01019878910"),
        ),
        mottaker = Adresse(
            linje1 = "TEST TESTERSON",
            linje2 = "JERNBANETORGET 4 F",
            linje3 = "1344 HASLUM",
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
    )

    val fellesAuto = felles.copy(signerendeSaksbehandlere = null)

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            LetterExampleDto::class -> LetterExampleDto(
                pensjonInnvilget = true,
                datoInnvilget = LocalDate.of(2020, 1, 1),
                navneliste = listOf("test testerson1", "test testerson2", "test testerson3"),
                tilleggEksempel = listOf(
                    ExampleTilleggDto(
                        navn = "Test testerson 1",
                        tillegg1 = Kroner(300),
                        tillegg3 = Kroner(500),
                    ), ExampleTilleggDto(
                        navn = "Test testerson 2",
                        tillegg1 = Kroner(100),
                        tillegg2 = Kroner(600),
                    ), ExampleTilleggDto(
                        navn = "Test testerson 3",
                        tillegg2 = Kroner(300),
                    )
                ), datoAvslaatt = LocalDate.of(2020, 1, 1),
                pensjonBeloep = 100
            ) as T

            MaanedligUfoeretrygdFoerSkattDto::class -> MaanedligUfoeretrygdFoerSkattDto(
                gjeldendeBeregnetUTPerMaaned = create(MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class),
                krav_virkningsDatoFraOgMed = LocalDate.of(2020, 1, 1),
                tidligereUfoeretrygdPerioder = emptyList(),
            ) as T

            MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class -> MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned(
                annetBelop = Kroner(0),
                barnetillegg = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
                dekningFasteUtgifter = Kroner(0),
                garantitilleggNordisk27 = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
                grunnbeloep = Kroner(0),
                ordinaerUTBeloep = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
                totalUTBeloep = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
                virkningFraOgMed = LocalDate.of(2020, 1, 1),
                virkningTilOgMed = LocalDate.of(2020, 1, 2),
                erAvkortet = true,
            ) as T

            OmsorgEgenAutoDto::class -> OmsorgEgenAutoDto(Year(2020), Year(2021)) as T

            OpplysningerBruktIBeregningUTDto::class -> OpplysningerBruktIBeregningUTDto(
                barnetilleggGjeldende = create(OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende::class),
                beregnetUTPerManedGjeldende = create(OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende::class),
                inntektEtterUfoereGjeldende_beloepIEU = Kroner(0),
                inntektFoerUfoereGjeldende = create(OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende::class),
                inntektsAvkortingGjeldende = create(OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende::class),
                minsteytelseGjeldende_sats = 0.0,
                trygdetidsdetaljerGjeldende = create(OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende::class),
                ufoeretrygdGjeldende = create(OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende::class),
                ungUfoerGjeldende_erUnder20Aar = false,
                yrkesskadeGjeldende = create(OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende::class),
            ) as T

            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende::class -> OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende(
                saerkullsbarn = create(OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn::class),
                grunnlag = create(OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Grunnlag::class),
            ) as T

            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Grunnlag::class -> OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Grunnlag(
                erIkkeUtbetaltpgaTak = false,
                erRedusertMotTak = false,
                gradertOIFU = Kroner(0),
                prosentsatsGradertOIFU = 0,
                totaltAntallBarn = 0,
            ) as T

            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn::class -> OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn(
                avkortningsbeloepAar = Kroner(0),
                beloep = Kroner(0),
                beloepAar = Kroner(0),
                beloepAarFoerAvkort = Kroner(0),
                erRedusertMotinntekt = false,
                fribeloep = Kroner(0),
                fribeloepEllerInntektErPeriodisert = false,
                inntektBruktIAvkortning = Kroner(0),
                inntektOverFribeloep = Kroner(0),
                inntektstak = Kroner(0),
                justeringsbeloepAar = Kroner(0),
            ) as T

            OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende::class -> OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende(
                brukerErFlyktning = false,
                brukersSivilstand = Sivilstand.ENSLIG,
                grunnbeloep = Kroner(0),
                virkDatoFom = LocalDate.of(2020, 1, 1),
            ) as T

            OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende::class -> OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende(
                erSannsynligEndret = false,
                ifuInntekt = Kroner(0),
            ) as T

            OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende::class -> OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende(
                forventetInntektAar = Kroner(0),
                inntektsgrenseAar = Kroner(0),
                inntektstak = Kroner(0),
            ) as T

            OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende::class -> OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende(
                anvendtTT = 0,
                beregningsmetode = Beregningsmetode.FOLKETRYGD,
                faktiskTTEOS = 0,
                faktiskTTNordiskKonv = 0,
                faktiskTTNorge = 0,
                framtidigTTNorsk = 0,
                nevnerTTEOS = 0,
                nevnerTTNordiskKonv = 0,
                samletTTNordiskKonv = 0,
                tellerTTEOS = 0,
                tellerTTNordiskKonv = 0,
                utenforEOSogNorden = create(OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden::class),
            ) as T

            OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden::class -> OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden(
                faktiskTTBilateral = 0,
                nevnerProRata = 0,
                tellerProRata = 0,
            ) as T

            OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende::class -> OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende(
                beloepsgrense = Kroner(0),
                beregningsgrunnlagBeloepAar = Kroner(0),
                erKonvertert = false,
                kompensasjonsgrad = 0.0,
                ufoeregrad = 0,
                ufoeretidspunkt = LocalDate.of(2020, 1, 1),
            ) as T

            OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende::class -> OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende(
                beregningsgrunnlagBeloepAar = Kroner(0),
                inntektVedSkadetidspunkt = Kroner(0),
                skadetidspunkt = LocalDate.of(2020, 1, 1),
                yrkesskadegrad = 0,
            ) as T

            OrienteringOmRettigheterUfoereDto::class -> OrienteringOmRettigheterUfoereDto(
                bruker_borINorge = false,
                institusjon_gjeldende = Institusjon.INGEN,
                avdoed_sivilstand = Sivilstand.ENSLIG,
                ufoeretrygdPerMaaned_barnetilleggGjeldende = Kroner(0),
            ) as T

            UfoerOmregningEnsligDto::class -> UfoerOmregningEnsligDto(
                opplysningerBruktIBeregningUT = create(OpplysningerBruktIBeregningUTDto::class),
                orienteringOmRettigheterOgPlikter = create(OrienteringOmRettigheterUfoereDto::class),
                maanedligUfoeretrygdFoerSkatt = create(MaanedligUfoeretrygdFoerSkattDto::class),
                minsteytelseVedvirk_sats = 0.0,
                avdoed = UfoerOmregningEnsligDto.Avdoed(
                    navn = "Avdod Person",
                    ektefelletilleggOpphoert = false,
                    sivilstand = Sivilstand.SAMBOER3_2,
                    harFellesBarnUtenBarnetillegg = false,
                ),
                krav_virkningsDatoFraOgMed = LocalDate.of(2020, 1, 1),
                beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak = 0,
                institusjonsoppholdVedVirk = Institusjon.INGEN,
                ufoeretrygdVedVirk = UfoerOmregningEnsligDto.UfoeretrygdVedVirk(
                    kompensasjonsgrad = 0.5,
                    totalUfoereMaanedligBeloep = Kroner(5),
                    erInntektsavkortet = false
                ),
                inntektFoerUfoerhetVedVirk = UfoerOmregningEnsligDto.InntektFoerUfoerhetVedVirk(
                    oppjustertBeloep = Kroner(0),
                    beloep = Kroner(0),
                    erMinsteinntekt = false,
                    erSannsynligEndret = false
                ),
                barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt = false,
                bruker = UfoerOmregningEnsligDto.Bruker(
                    borIAvtaleLand = false,
                    borINorge = true,
                ),
                barnetilleggVedVirk = UfoerOmregningEnsligDto.BarnetilleggVedVirk(
                    barnetilleggSaerkullsbarnVedVirk = UfoerOmregningEnsligDto.BarnetilleggSaerkullsbarnVedvirk(
                        beloep = Kroner(0),
                        erRedusertMotInntekt = false,
                        inntektBruktIAvkortning = Kroner(0),
                        fribeloepVedvirk = Kroner(0),
                        justeringsbeloepAar = Kroner(0),
                        inntektstak = Kroner(0),
                        barnTidligereSaerkullsbarn = listOf(
                            "Tidligere saerkullsbarn 1",
                            "Tidligere saerkullsbarn 2",
                            "Tidligere saerkullsbarn 3",
                        ),
                        barnOverfoertTilSaerkullsbarn = listOf(
                            "Overfoert til saerkullsbarn 1",
                            "Overfoert til saerkullsbarn 2",
                            "Overfoert til saerkullsbarn 3",
                        ),
                    ),
                    barnetilleggGrunnlag = UfoerOmregningEnsligDto.BarnetilleggGrunnlagVedVirk(
                        erRedusertMotTak = false,
                        prosentsatsGradertOverInntektFoerUfoer = 0,
                        gradertOverInntektFoerUfoer = Kroner(0),
                        erIkkeUtbetaltPgaTak = false,
                        beloepFoerReduksjon = Kroner(0),
                        beloepEtterReduksjon = Kroner(0),
                    )
                )
            ) as T

            UngUfoerAutoDto::class -> UngUfoerAutoDto(
                kravVirkningFraOgMed = LocalDate.now(),
                totaltUfoerePerMnd = Kroner(9000),
                ektefelle = null,
                gjenlevende = UngUfoerAutoDto.InnvilgetTillegg(true),
                fellesbarn = UngUfoerAutoDto.InnvilgetBarnetillegg(false, 1, Kroner(10_000)),
                saerkullsbarn = UngUfoerAutoDto.InnvilgetBarnetillegg(true, 2, Kroner(10_000)),
                minsteytelseVedVirkSats = 2.91,
                maanedligUfoeretrygdFoerSkatt = create(MaanedligUfoeretrygdFoerSkattDto::class),
                orienteringOmRettigheterUfoere = create(OrienteringOmRettigheterUfoereDto::class),
            ) as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}