package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Sivilstand.GIFT
import no.nav.pensjon.brev.api.model.Sivilstand.GIFT_LEVER_ADSKILT
import no.nav.pensjon.brev.api.model.Telefonnummer
import no.nav.pensjon.brev.template.BaseLanguages
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Phrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.phrase

object VedleggBeregnTittel_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                    Bokmal to "Opplysninger brukt i beregningen",
                    Nynorsk to "Opplysningar brukte i berekninga",
                    English to "Information about your calculation"
            )
        }
    }
}
