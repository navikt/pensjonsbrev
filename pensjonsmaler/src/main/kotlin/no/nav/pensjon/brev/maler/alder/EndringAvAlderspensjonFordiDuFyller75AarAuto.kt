package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype

// MF_000118 : AP_INNV_AO_75_AUTO / AO_AP_GRAD_AP_75
@TemplateModelHelpers
object EndringAvAlderspensjonFordiDuFyller75AarAuto :
    AutobrevTemplate<EndringAvAlderspensjonFordiDuFyller75AarAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ENDRING_FORDI_DU_FYLLER_75_AAR_AUTO

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon til deg som snart fyller 67 år",
                    isSensitiv = false,
                    distribusjonstype = Distribusjonstype.VIKTIG,
                    brevtype = Brevtype.VEDTAKSBREV
                ),

            ) {
            title {
                text(
                    bokmal { +"Vi har økt alderspensjonen til 100 prosent fra " },
                    nynorsk { +"Vi har auka alderspensjonen til 100 prosent frå" },
                    english { +"We have increased your retirement pension to 100 percent from" }
                )
            }
        }
}