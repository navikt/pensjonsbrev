package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDto

fun createInnhentingInformasjonFraBrukerDto() =
    InnhentingInformasjonFraBrukerDto(
        saksbehandlerValg = InnhentingInformasjonFraBrukerDto.SaksbehandlerValg(
            bosattIEosLandSedErEosBlanketter = true,
            inntektsopplysninger = true,
            bankOpplysninger = true,
            amerikanskSocialSecurityNumber = true,
            registreringAvSivilstand = true,
            eps60aarOgInntektUnder1G = true,
            eps62aarOgInntektUnder1GBoddArbeidUtland = true,
            epsInntektUnder2G = true,
            forsorgerEpsBosattIUtlandet = true,
            tidspunktForUttak = true,
            manglendeOpptjening = true,
            boOgArbeidsperioder = true,
            bosattEOSLandSedEOSBlanketter = true
        ),
        pesysData = EmptyBrevdata
    )