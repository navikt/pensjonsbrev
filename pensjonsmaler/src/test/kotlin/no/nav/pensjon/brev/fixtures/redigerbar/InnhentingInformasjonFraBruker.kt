package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDto

fun createInnhentingInformasjonFraBrukerDto() =
    InnhentingInformasjonFraBrukerDto(
        saksbehandlerValg = InnhentingInformasjonFraBrukerDto.SaksbehandlerValg(
            bosattIEoesLandSedErEoesBlanketter = true,
            inntektsopplysninger = true,
            bankOpplysninger = true,
            amerikanskSocialSecurityNumber = true,
            registreringAvSivilstand = true,
            eps60aarOgInntektUnder1g = true,
            eps62aarOgInntektUnder1gBoddArbeidUtland = true,
            epsInntektUnder2g = true,
            forsoergerEpsBosattIUtlandet = true,
            tidspunktForUttak = true,
            manglendeOpptjening = true,
            boOgArbeidsperioder = true,
        ),
        pesysData = EmptyBrevdata
    )