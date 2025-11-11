package no.nav.pensjon.brev.model.alder.aldersovergang

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.model.alder.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonFordiDuFyller75AarAutoDto(
    val kravVirkDatoFom: LocalDate, // v3.Krav
    val regelverkType: AlderspensjonRegelverkType,
    val totalPensjon: Kroner,  // v4.Alderpensjon
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val opplysningerBruktIBeregningenAlder: OpplysningerBruktIBeregningenAlderDto?,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto?,
) : BrevbakerBrevdata