package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagFoer67AarAP2016Dto
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object AvslagFoer67AarAP2016 : RedigerbarTemplate<AvslagFoer67AarAP2016Dto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_FOER_67_AAR_AP2016
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagFoer67AarAP2016Dto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på søknad om uttak av alderspensjon før 67 år (AP2016)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Bokmal to "Nav har avslatt søknaden din om alderspensjon før du blir 67 år",
                Nynorsk to "Nav har avslatt søknaden din om alderspensjon før du blir 67 år",
                English to "Nav has declined you application for retirement pension before the age of 67",
            )
        }
        outline {
            paragraph {
                text(

                )
            }
        }

    }
}