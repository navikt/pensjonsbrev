package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

data class VedtakEndringAvAlderspensjonGjenlevenderettigheterDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.PesysData> {
    data class PesysData(
        val avdod: Avdod,
        val bruker: Bruker,
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val gjenlevendetilleggKapittel19VedVirk: GjenlevendetilleggKapittel19VedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk
    ) : BrevbakerBrevdata

    data class Avdod(
        val navn: String
    )

    data class Bruker(
        val fodselsdato: LocalDate
    )

    data class Krav(
        val virkDatoFom: LocalDate,
        val kravInitiertAv: KravInitiertAv
    )

    data class AlderspensjonVedVirk(
        val regelverkType: AlderspensjonRegelverkType,
        val uttaksgrad: Int,
        val gjenlevendetilleggKap19Innvilget: Boolean,
        val gjenlevenderettAnvendt: Boolean
    )

    data class YtelseskomponentInformasjon(
        val beloepEndring: BeloepEndring,
    )

    data class GjenlevendetilleggKapittel19VedVirk(
        val apKap19utenGJR: Boolean
    )

    data class BeregnetPensjonPerManedVedVirk(
        val inntektspensjon: Boolean?
    )

    @Suppress("EnumEntryName")
    enum class KravInitiertAv {
        BRUKER,
        NAV,
        VERGE,
    }

    @Suppress("EnumEntryName")
    enum class BeloepEndring {
        ENDR_OKT,
        ENDR_RED,
        UENDRET
    }
}