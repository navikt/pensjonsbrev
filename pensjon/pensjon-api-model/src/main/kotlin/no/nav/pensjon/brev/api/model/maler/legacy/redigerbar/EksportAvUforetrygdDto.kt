package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class EksportAvUforetrygdDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EksportAvUforetrygdDto.PesysData> {
    data class PesysData(
        val pe: PEgruppe10,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,

        val fullEksport: Boolean,
        val sumUtOgTillegg: Kroner,
        val virkningstidspunkt: LocalDate,
        val muligTilbakekrevingVarsel: Boolean,
        val ensligOgMinstesats: Boolean,
        val minstesatsTilBruker: Double,
        val barnetilleggAvtaleland: Boolean,
        val flereBarn: Boolean,
        val innvilgetBT: Boolean,
        val btInnvilgetMenIngenUtbetaling: Boolean,
        val gjtEndret: Boolean,
        val gjtUendret: Boolean,
        val gjtAvkortet: Int,
        val eosNordisk: Boolean,
    ) : FagsystemBrevdata

    enum class Arsak(override val displayText: String) : SaksbehandlerValgEnum {
        FLYTTEDATOFRABRUKER("Mottatt flyttedato fra bruker"),
        MOTTATTOPPLFLYTTING("Mottatt opplysninger om flytting fra andre"),
        LENGREOPPHOLDUTLAND("Mottatt opplysninger om lengre opphold i utlandet"),
    }

    enum class FlyktningArsak(override val displayText: String) : SaksbehandlerValgEnum {
        IKKEFLYKTNING("Ikke flyktningstatus"),
        FLYKTNINGOVER20ARREDUKSJON("Flyktning- over 20 år - reduksjon"),
        FLYKTNINGUNDER20ÅREØS("Flyktning, under 20 år - EØS"),
        FLYKTNINGINGENENDRINGAVTALELAND("Flyktning – ingen endring - avtaleland"),
    }
}
