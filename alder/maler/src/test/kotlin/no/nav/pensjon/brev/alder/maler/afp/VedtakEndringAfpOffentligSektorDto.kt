package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.vedlegg.createOversiktOverPensjonenAfpDto
import no.nav.pensjon.brev.alder.model.afp.VedtakEndringAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.model.afp.AfpOffentligSektor.Beregning
import no.nav.pensjon.brev.alder.model.afp.AfpOffentligSektor.Ektefelletillegg
import no.nav.pensjon.brev.alder.model.afp.VedtakEndringAfpOffentligSektorDto.PesysData
import no.nav.pensjon.brev.alder.model.afp.AfpOffentligSektor
import no.nav.pensjon.brev.alder.model.afp.AfpOffentligSektor.YtelsesKomponent
import no.nav.pensjon.brev.alder.model.PoengTallsType
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto.AfpOrdning
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto.SivilstandKategori
import no.nav.pensjon.brev.alder.model.vedlegg.Pensjonspoeng
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import java.time.LocalDate

fun createVedtakEndringAfpOffentligSektorDto(): VedtakEndringAfpOffentligSektorDto =
    VedtakEndringAfpOffentligSektorDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = PesysData(
            virkningFom = LocalDate.of(2026, 3, 1),
            beregningVirkDatoFom = LocalDate.of(2026, 3, 1),
            afpPensjonsgrad = Percent(80),
            grunnbeloep = Kroner(124_028),
            framtidigArligInntekt = Kroner(50_000),
            tidligereArbeidsinntekt = Kroner(450_000),
            flerePerioder = true,
            etterbetaling = true,
            sivilstand = AfpOffentligSektor.Sivilstand.BOR_MED_EKTEFELLE,
            beregning = Beregning(
                brutto = Kroner(20_500),
                netto = Kroner(15_300),
                grunnpensjon = YtelsesKomponent(brutto = Kroner(8_500), netto = Kroner(6_300)),
                tilleggspensjon = YtelsesKomponent(brutto = Kroner(9_000), netto = Kroner(6_700)),
                saertillegg = null,
                minstenivaatilleggIndividuelt = null,
                afpTillegg = YtelsesKomponent(brutto = Kroner(1_700), netto = Kroner(1_300)),
                ektefelletillegg = Ektefelletillegg(
                    brutto = Kroner(1_300),
                    netto = Kroner(1_000),
                    inntektBruktIAvkortning = Kroner(75_000),
                ),
                fasteUtgifterInstitusjon = null,
                familietillegg = null,
                barnetilleggSerkull = false,
                barnetilleggFelles = false,
            ),
            opplysningerOmBeregningen = OpplysningerOmBeregningenAfpDto(
                beregningVirkDatoFom = LocalDate.of(2026, 3, 1),
                afpPensjonsgrad = Percent(80),
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
            ),
            oversiktOverPensjonen = createOversiktOverPensjonenAfpDto(),
        ),
    )
