package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val informasjonOmYrkesskade = createAttachment(
    title = newText(
        Bokmal to "Informasjon om dødsfall som skyldes yrkesskade/yrkessykdom",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false,
) {
    paragraph {
        text(
            Bokmal to "Ifølge folketrygdloven § 17-12 gis det gjenlevendepensjon etter særbestemmelser " +
                    "dersom den avdøde døde som følge av en skade eller sykdom som går inn under " +
                    "folketrygdloven kapittel 13.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Særbestemmelsene gjelder forutgående medlemskap for avdøde, fortsatt medlemskap " +
                    "for etterlatte og ingen reduksjon på grunn av trygdetiden til avdøde.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Dersom avdøde døde som følge av en skade eller sykdom etter folketrygdloven " +
                    "kapittel 13, så skal det foreligge vedtak om fra NAV om dette.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Dersom det ikke foreligger vedtak, kan du søke om å få godkjent avdødes sykdom " +
                    "som yrkesskade eller yrkessykdom. For mer informasjon ber vi dere kontakte NAV lokalt, " +
                    "eller ringe NAV Kontaktsenter på telefon 55 55 33 33, for å få informasjon om hvordan dere " +
                    "kan få godkjent sykdom som yrkessykdom.",
            Nynorsk to "",
            English to "",
        )
    }
}
