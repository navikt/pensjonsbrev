package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class InnhentingInformasjonFraBrukerDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<InnhentingInformasjonFraBrukerDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        @DisplayText("Bosatt i EØS-land. SED-er/EØS-blanketter")
        val bosattIEoesLandSedErEoesBlanketter: Boolean,
        val inntektsopplysninger: Boolean,
        val bankOpplysninger: Boolean,
        val amerikanskSocialSecurityNumber: Boolean,
        val registreringAvSivilstand: Boolean,
        @DisplayText("Ektefelle/partner/samboer 60 år og inntekt under 1G")
        val eps60aarOgInntektUnder1g: Boolean,
        @DisplayText("Ektefelle/partner/samboer 62 år og bodd og/eller arbeidet i utlandet")
        val eps62aarOgInntektUnder1gBoddArbeidUtland: Boolean,
        @DisplayText("Ektefelles/partners/samboers inntekt under 2G")
        val epsInntektUnder2g: Boolean,
        @DisplayText("Forsørger ektefellen/partneren/samboeren som er bosatt i utlandet")
        val forsoergerEpsBosattIUtlandet: Boolean,
        @DisplayText("Tidspunkt for uttak / ønsket uttaksgrad")
        val tidspunktForUttak: Boolean,
        @DisplayText("Manglende opptjening")
        val manglendeOpptjening: Boolean,
        @DisplayText("Bo- og arbeidsperioder")
        val boOgArbeidsperioder: Boolean,
    ) : BrevbakerBrevdata
}
