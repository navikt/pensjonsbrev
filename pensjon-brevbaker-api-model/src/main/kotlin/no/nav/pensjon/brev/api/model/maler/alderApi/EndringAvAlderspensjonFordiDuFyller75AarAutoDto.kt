package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class EndringAvAlderspensjonFordiDuFyller75AarAutoDto(
    val harFlereBeregningsperioder: Boolean,
    val kravVirkDatoFom: LocalDate,
    val regelverkType: AlderspensjonRegelverkType,
    val totalPensjon: Kroner,
    val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto,
    val opplysningerBruktIBeregningenAlderspensjon: OpplysningerBruktIBeregningenAlderDto,
    ) : BrevbakerBrevdata