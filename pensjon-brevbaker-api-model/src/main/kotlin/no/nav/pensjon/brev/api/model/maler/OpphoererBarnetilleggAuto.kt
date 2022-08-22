package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class OpphoererBarnetilleggAutoDto(
    val barnetilleggFellesbarnInnvilget: Boolean,
    val barnetilleggSaerkullsbarnInnvilget: Boolean
)

data class Beloep(
    val perMaaned: Kroner,
    val ektefelle: Boolean,
    val gjenlevende: Boolean,
    val fellesbarn: Boolean,
    val saerkullsbarn: Boolean,
    val ufoertrygd: Boolean
)

data class BTFBinntektBruktiAvkortningOgFribeloepOgGrunnbeloep(
    val fellesbarnFribeloep: Kroner,
    val fellesbarnInntektAnnenForelder: Kroner,
    val fellesbarnInntektBruktiAvkortning: Kroner,
    val grunnbeloep: Kroner
)

data class BTfribeloep(
    val saerkullsbarnFribeloep: Kroner,
    val fellesbarnFribeloep: Kroner
)

data class BTinntektstak(
    val fellesbarnInntektstak: Kroner,
    val saerkullsbarnInntektstak: Kroner
)

data class BToensketVirkningsDatoOgFdatoPaaBTopphoert(
    val foedselsdatoPaaBarnetilleggOpphoert: Number,
    val oensketVirkningsDato: LocalDate
)

data class BTSBinntektBruktiAvkortningOgBTfribeleop(
    val saerkullsbarnInntektBruktiAvkortning: Kroner,
    val saerkullsbarnFribeloep: Kroner
)

data class InnvilgetBarnetillegg(
    val utbetalt: Boolean, val antallBarn: Int, val inntektstak: Kroner
)

data class OensketVirkningsDate(
    val oensketVirkningsDato: LocalDate
)
