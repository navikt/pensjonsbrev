package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto.PesysData> {

    data class PesysData(
        val oppgjoersAar: BrevbakerType.Year,
        val pensjonsgivendeInntekt: Kroner,
        val medlemAvApotekerordningen: Boolean,
        val toleranseBeloep: Kroner,
        val scenario: Scenario,
    ) : FagsystemBrevdata

    enum class Scenario {
        HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK,
        HEL_AFP_HELE_AARET_INGEN_INNTEKT,
        IKKE_AFP_FULL_INNTEKT,
        HEL_AFP_DELER_AV_AARET,
    }
}