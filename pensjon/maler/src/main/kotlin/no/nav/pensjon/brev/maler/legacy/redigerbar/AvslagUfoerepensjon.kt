package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDto
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_UP_07_010 Vedtak - avslag av uførepensjon
//Brevgruppe 3

@TemplateModelHelpers
object AvslagUfoerepensjon : RedigerbarTemplate<AvslagUfoerepensjonDto> {

//override val featureToggle = FeatureToggles.brevmalAvslagUfoerepensjon.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UP_AVSLAG_UFOEREPENSJON
    override val kategori = Brevkategori.UFOEREPENSJON
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på ufoerepensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"" },
                    nynorsk { +"" }
                )
            }
        }

    }
}