package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto.Beregning
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto.Ektefelletillegg
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto.YtelsesKomponent
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto.PesysData
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto.SivilstandGruppe
import no.nav.pensjon.brev.alder.model.vedlegg.HvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createInnvilgelseAvAfpOffentligSektorDto(): InnvilgelseAvAfpOffentligSektorDto =
    InnvilgelseAvAfpOffentligSektorDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = PesysData(
            kravMottattDato = LocalDate.of(2026, 1, 15),
            virkningFom = LocalDate.of(2026, 3, 1),
            beregningVirkDatoFom = LocalDate.of(2026, 3, 1),
            afpPensjonsgrad = 100,
            grunnbeloep = Kroner(124_028),
            framtidigArligInntekt = Kroner(50_000),
            tidligereArbeidsinntekt = Kroner(450_000),
            flerePerioder = false,
            etterbetaling = true,
            sivilstand = SivilstandGruppe.BOR_MED_EKTEFELLE,
            avsenderEnhet = "NAV Familie- og pensjonsytelser Oslo",
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
            hvordanPensjonenErBeregnet = HvordanPensjonenErBeregnetAfpOffentligDto(
                grunnbeloep = Kroner(124_028),
                trygdetid = 38,
                brukerErFlyktning = false,
                ektefelleInntektOver2G = false,
                ektefelleMottarPensjon = false,
                tilleggspensjon = HvordanPensjonenErBeregnetAfpOffentligDto.Tilleggspensjon(
                    sluttpoengtallUtenOk = 5.32,
                    poengaarUtenOk = 38,
                    poengaarUtenOke91 = 15,
                    poengaarUtenOkf92 = 23,
                ),
                saertilleggInnvilget = false,
                ektefelletillegg = HvordanPensjonenErBeregnetAfpOffentligDto.Ektefelletillegg(
                    fribeloep = Kroner(124_028),
                ),
            ),
        ),
    )
