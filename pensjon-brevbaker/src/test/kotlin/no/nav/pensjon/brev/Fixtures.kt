package no.nav.pensjon.brev

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
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
            nettside = "nav.no",
            navn = "NAV Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = Telefonnummer("55553334"),
        ),
        bruker = Bruker(
            fornavn = "Test",
            mellomnavn = "\"bruker\"",
            etternavn = "Testerson",
            foedselsnummer = Foedselsnummer("01019878910"),
            foedselsdato = LocalDate.of(1998, 1, 1),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
        vergeNavn = null,
    )

    val fellesAuto = felles.copy(signerendeSaksbehandlere = null)

    inline fun <reified T: Any> create(): T = create(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> create(letterDataType: KClass<T>): T =
        when (letterDataType) {
            EgenerklaeringOmsorgsarbeidDto::class -> createEgenerklaeringOmsorgsarbeidDto() as T
            EndringIOpptjeningAutoDto.Avkortningsinformasjon::class -> createEndringIOpptjeningAutoDtoAkortningsinformasjon() as T
            EndringIOpptjeningAutoDto.BeregningUfoereEndringIOpptjening::class -> createEndringIOpptjeningAutoDtoBeregningUfoereEndringIOpptjening() as T
            EndringIOpptjeningAutoDto.FellesbarnTillegg::class -> createEndringIOpptjeningAutoDtoBarnetilleggFellesbarn() as T
            EndringIOpptjeningAutoDto.SaerkullsbarnTillegg::class -> createEndringIOpptjeningAutoDtoBarnetilleggSaerkullsbarn() as T
            EndringIOpptjeningAutoDto::class -> createEndringIOpptjeningAutoDto() as T
            InformasjonOmSaksbehandlingstidDto::class -> createInformasjonOmSaksbehandlingstidDto() as T
            LetterExampleDto::class -> createLetterExampleDto() as T
            MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned::class -> createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() as T
            MaanedligUfoeretrygdFoerSkattDto::class -> createMaanedligUfoeretrygdFoerSkattDto() as T
            OmsorgEgenAutoDto::class -> createOmsorgEgenAutoDto() as T
            OpphoerBarnetilleggAutoDto::class -> createOpphoerBarnetilleggAutoDto() as T
            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Fellesbarn::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeFellesbarn() as T
            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeSaerkullsbarn() as T
            OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende::class -> createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende() as T
            OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende::class -> createOpplysningerBruktIBeregningUTDtoBeregnetUTPerManedGjeldende() as T
            OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende::class -> createOpplysningerBruktIBeregningUTDtoInntektFoerUfoereGjeldende() as T
            OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende::class -> createOpplysningerBruktIBeregningUTDtoInntektsAvkortingGjeldende() as T
            OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden::class -> createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldendeUtenforEOSogNorden() as T
            OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende::class -> createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldende() as T
            OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende::class -> createOpplysningerBruktIBeregningUTDtoUfoeretrygdGjeldende() as T
            OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende::class -> createOpplysningerBruktIBeregningUTDtoYrkesskadeGjeldende() as T
            OpplysningerBruktIBeregningUTDto::class -> createOpplysningerBruktIBeregningUTDto() as T
            OpptjeningVedForhoeyetHjelpesatsDto::class -> OpptjeningVedForhoeyetHjelpesatsDto(Year(2021)) as T
            OrienteringOmRettigheterUfoereDto::class -> createOrienteringOmRettigheterUfoereDto() as T
            UfoerOmregningEnsligDto::class -> createUfoerOmregningEnsligDto() as T
            UngUfoerAutoDto::class -> createUngUfoerAutoDto() as T
            Unit::class -> Unit as T
            OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygd::class -> createOpplysningerBruktIBeregningUTDtoOpptjeningUfoeretrygd() as T
            OpplysningerBruktIBeregningUTDto.Opptjeningsperiode::class -> createOpplysningerBruktIBeregningUTDtoOpptjeningsperiode() as T
            OpplysningerBruktIBeregningUTDto.TrygdetidGjeldende::class -> createOpplysningerBruktIBeregningUTDtoTrygdetidGjeldende() as T
            OpplysningerBruktIBeregningUTDto.NorskTrygdetid::class -> createOpplysningerBruktIBeregningUTDtoNorskTrygdetid() as T
            OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidEOS::class -> createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidEOS() as T
            OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidBilateral::class -> createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidBilateral() as T
            OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed::class -> createOpplysningerBruktIBeregningUTDtoGjenlevendetilleggInformasjon() as T
            OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer::class -> createOpplysningerBruktIBeregningUTDtoUfoeretrygdOrdinaer() as T
            OpplysningerBruktIBeregningUTDto.BeregningUfoere::class -> createOpplysningerBruktIBeregningUTDtoBeregningUfoere() as T
            OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygdAvdoed::class -> createOpplysningerBruktIBeregningUTDtoOpptjeningUfoeretrygdAvdoed() as T
            OpplysningerBruktIBeregningUTDto.OpptjeningsperiodeAvdoed::class -> createOpplysningerBruktIBeregningUTDtoOpptjeningsperiodeAvdoede() as T

            else -> throw IllegalArgumentException("Don't know how to construct: ${letterDataType.qualifiedName}")
        }
}