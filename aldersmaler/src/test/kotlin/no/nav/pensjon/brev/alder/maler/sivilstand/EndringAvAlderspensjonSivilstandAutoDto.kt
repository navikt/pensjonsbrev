package no.nav.pensjon.brev.alder.maler.sivilstand

import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.KravArsakType
import no.nav.pensjon.brev.alder.model.MetaforceSivilstand
import no.nav.pensjon.brev.alder.model.sivilstand.EndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvAlderspensjonSivilstandAutoDto() =
    EndringAvAlderspensjonSivilstandAutoDto(
        alderspensjonVedVirk =
            EndringAvAlderspensjonSivilstandAutoDto.AlderspensjonVedVirk(
                garantipensjonInnvilget = false,
                innvilgetFor67 = false,
                minstenivaaIndividuellInnvilget = true,
                minstenivaaPensjonsistParInnvilget = false,
                pensjonstilleggInnvilget = false,
                saertilleggInnvilget = false,
                ufoereKombinertMedAlder = false,
                uttaksgrad = 100,
            ),
        beregnetPensjonPerManedVedVirk =
            EndringAvAlderspensjonSivilstandAutoDto.BeregnetPensjonPerManedVedVirk(
                grunnpensjon = Kroner(320000),
                totalPensjon = Kroner(340000),
            ),
        epsVedVirk =
            EndringAvAlderspensjonSivilstandAutoDto.EpsVedVirk(
                harInntektOver2G = false,
            ),
        kravAarsak = KravArsakType.SIVILSTANDSENDRING,
        kravVirkDatoFom = LocalDate.of(2025, 6, 1),
        regelverkType = AlderspensjonRegelverkType.AP2011,
        sivilstand = MetaforceSivilstand.GIFT,
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025Dto =
            MaanedligPensjonFoerSkattAP2025Dto(
                beregnetPensjonPerManedGjeldende =
                    MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
                        inntektspensjon = Kroner(1000),
                        totalPensjon = Kroner(2000),
                        garantipensjon = Kroner(1000),
                        minstenivaIndividuell = Kroner(1000),
                        virkDatoFom = LocalDate.now(),
                        virkDatoTom = null,
                    ),
                beregnetPensjonperManed = listOf(),
                kravVirkFom = LocalDate.now(),
            ),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
        beloepEndring = BeloepEndring.UENDRET,
    )
