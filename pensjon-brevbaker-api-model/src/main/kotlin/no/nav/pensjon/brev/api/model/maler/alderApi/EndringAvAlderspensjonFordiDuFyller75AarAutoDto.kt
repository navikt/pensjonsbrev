package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
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