package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class InnhentingInformasjonFraBrukerDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<InnhentingInformasjonFraBrukerDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        // TODO forbedre tekst når displayTitle er mulig.
        val bosattIEoesLandSedErEoesBlanketter: Boolean, //Bosatt i EØS-land. SED-er/EØS-blanketter
        val inntektsopplysninger: Boolean,
        val bankOpplysninger: Boolean,
        val amerikanskSocialSecurityNumber: Boolean,
        val registreringAvSivilstand: Boolean,
        val eps60aarOgInntektUnder1g: Boolean, //Ektefelle/partner/samboer 60 år og inntekt under 1G
        val eps62aarOgInntektUnder1gBoddArbeidUtland: Boolean, //Ektefelle/partner/samboer 62 år og bodd og/eller arbeidet i utlandet
        val epsInntektUnder2g: Boolean, //Ektefelles/partners/samboers inntekt under 2G
        val forsoergerEpsBosattIUtlandet: Boolean, //Forsørger ektefellen/partneren/samboeren som er bosatt i utlandet
        val tidspunktForUttak: Boolean, //Tidspunkt for uttak / ønsket uttaksgrad
        val manglendeOpptjening: Boolean, //Manglende opptjening
        val boOgArbeidsperioder: Boolean, //Bo- og arbeidsperioder
    ) : BrevbakerBrevdata
}
