package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.InnhentingInformasjonFraBrukerDto
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata

fun createInnhentingInformasjonFraBrukerDto() =
    InnhentingInformasjonFraBrukerDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "bosattIEoesLandSedErEoesBlanketter" to true,
            "inntektsopplysninger" to true,
            "bankopplysninger" to true,
            "amerikanskSocialSecurityNumber" to true,
            "registreringAvSivilstand" to true,
            "eps60aarOgInntektUnder1g" to true,
            "eps62aarOgInntektUnder1gBoddArbeidUtland" to true,
            "epsInntektUnder2g" to true,
            "forsoergerEpsBosattIUtlandet" to true,
            "tidspunktForUttak" to true,
            "manglendeOpptjening" to true,
            "boOgArbeidsperioder" to true,
        ),
        pesysData = EmptyFagsystemdata
    )