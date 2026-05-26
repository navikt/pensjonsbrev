package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.vedlegg.HvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createHvordanPensjonenErBeregnetAfpOffentligDto() =
    HvordanPensjonenErBeregnetAfpOffentligDto(
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
    )
