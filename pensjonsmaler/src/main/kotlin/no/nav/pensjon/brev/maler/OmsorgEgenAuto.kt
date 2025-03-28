package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.aarEgenerklaringOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.aarInnvilgetOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.egenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.OmsorgEgenerklaeringOutline
import no.nav.pensjon.brev.maler.fraser.OmsorgEgenerklaeringTittel
import no.nav.pensjon.brev.maler.vedlegg.egenerklaeringPleieOgOmsorgsarbeid
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV

@TemplateModelHelpers
object OmsorgEgenAuto : AutobrevTemplate<OmsorgEgenAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_OMSORG_EGEN_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmsorgEgenAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Egenerklæring godskriving omsorgspoeng",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = INFORMASJONSBREV,
        )
    ) {

        title {
            includePhrase(OmsorgEgenerklaeringTittel)
        }
        outline {
            includePhrase(
                OmsorgEgenerklaeringOutline(
                    aarEgenerklaringOmsorgspoeng = aarEgenerklaringOmsorgspoeng.format(),
                    aarInnvilgetOmsorgspoeng = aarInnvilgetOmsorgspoeng.format()
                )
            )
        }
        includeAttachment(egenerklaeringPleieOgOmsorgsarbeid, egenerklaeringOmsorgsarbeidDto)
    }
}