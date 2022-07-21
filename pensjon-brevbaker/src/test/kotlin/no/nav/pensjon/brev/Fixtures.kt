package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brev.fixtures.*
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
            foedselsdato = LocalDate.of(1998, 1, 1),
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

    inline fun <reified T: Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            LetterExampleDto::class -> createLetterExampleDto() as T
            MaanedligUfoeretrygdFoerSkattDto::class -> createMaanedligUfoeretrygdFoerSkattDto() as T
            MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class -> createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() as T
            OmsorgEgenAutoDto::class -> OmsorgEgenAutoDto(Year(2020), Year(2021)) as T
            OpplysningerBruktIBeregningUTDto::class -> createOpplysningerBruktIBeregningUTDto() as T
            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende() as T
            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Grunnlag::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeGrunnlag() as T
            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeSaerkullsbarn() as T
            OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende::class -> createOpplysningerBruktIBeregningUTDtoBeregnetUTPerManedGjeldende() as T
            OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende::class -> createOpplysningerBruktIBeregningUTDtoInntektFoerUfoereGjeldende() as T
            OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende::class -> createOpplysningerBruktIBeregningUTDtoInntektsAvkortingGjeldende() as T
            OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende::class -> createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldende() as T
            OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden::class -> createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldendeUtenforEOSogNorden() as T
            OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende::class -> createOpplysningerBruktIBeregningUTDtoUfoeretrygdGjeldende() as T
            OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende::class -> createOpplysningerBruktIBeregningUTDtoYrkesskadeGjeldende() as T
            OpptjeningVedForhoeyetHjelpesatsDto::class -> OpptjeningVedForhoeyetHjelpesatsDto(Year(2021)) as T
            OrienteringOmRettigheterUfoereDto::class -> createOrienteringOmRettigheterUfoereDto() as T
            UfoerOmregningEnsligDto::class -> createUfoerOmregningEnsligDto() as T
            UngUfoerAutoDto::class -> createUngUfoerAutoDto() as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}