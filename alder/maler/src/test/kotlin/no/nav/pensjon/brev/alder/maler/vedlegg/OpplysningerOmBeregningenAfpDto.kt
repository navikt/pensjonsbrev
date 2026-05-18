package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.PoengTallsType
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto.AfpOrdning
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto.SivilstandKategori
import no.nav.pensjon.brev.alder.model.vedlegg.Pensjonspoeng
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import java.time.LocalDate

fun createOpplysningerOmBeregningenAfpDto() =
    OpplysningerOmBeregningenAfpDto(
        beregningVirkDatoFom = LocalDate.of(2026, 3, 1),
        afpPensjonsgrad = Percent(100),
        tidligereArbeidsinntekt = Kroner(450_000),
        framtidigArligInntekt = Kroner(50_000),
        ektefelletilleggInntektBruktIAvkortning = Kroner(75_000),
        afpOrdning = AfpOrdning.LO_NHO,
        sivilstand = SivilstandKategori.GIFT,
        ektefelleEllerPartnerMottarPensjon = false,
        ektefelleEllerPartnerInntektOver2G = false,
        brukerErFlyktning = false,
        trygdetid = 38,
        tilleggspensjon = OpplysningerOmBeregningenAfpDto.Tilleggspensjon(
            sluttpoengtallUtenOk = 5.32,
            poengaarUtenOk = 38,
            poengaarUtenOke91 = 15,
            poengaarUtenOkf92 = 23,
        ),
        poengrekke = listOf(
            Pensjonspoeng(
                pensjonsgivendeinntekt = Kroner(420_000),
                grunnbelopVeiet = Kroner(100_000),
                arstall = 2018,
                pensjonspoeng = 4.20,
                poengtallstype = null,
                bruktIBeregningen = true,
            ),
            Pensjonspoeng(
                pensjonsgivendeinntekt = Kroner(0),
                grunnbelopVeiet = Kroner(100_000),
                arstall = 2019,
                pensjonspoeng = 3.50,
                poengtallstype = PoengTallsType.J,
                bruktIBeregningen = false,
            ),
            Pensjonspoeng(
                pensjonsgivendeinntekt = Kroner(0),
                grunnbelopVeiet = Kroner(0),
                arstall = 2025,
                pensjonspoeng = 4.00,
                poengtallstype = PoengTallsType.FPP,
                bruktIBeregningen = false,
            ),
        ),
        harOmsorgspoeng = true,
    )
