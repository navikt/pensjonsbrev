package no.nav.pensjon.brev.maler.alder.omregning

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.virkFom
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object OmregningAlderUfore2016Auto : AutobrevTemplate<OmregningAlderUfore2016Dto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_OMREGNING_ALDER_UFORE_2016_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmregningAlderUfore2016Dto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag endring av uttaksgrad - AP2016",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har regnet om uføretrygden din til alderspensjon",
                Nynorsk to "Vi har rekna om uføretrygda di til alderspensjon",
                English to "We have converted your disability pension into retirement pension",
            )
        }
        outline {
            includePhrase(
                OmregningAlderUfore2016Felles(virkFom = virkFom)
            )

        }
        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }
}