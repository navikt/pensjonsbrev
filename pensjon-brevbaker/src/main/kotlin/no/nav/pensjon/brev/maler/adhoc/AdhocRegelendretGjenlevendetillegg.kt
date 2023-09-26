package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocRegelendretGjenlevendetillegg : AutobrevTemplate<Unit> {
    override val kode = Brevkode.AutoBrev.UT_2023_INFO_REGLERENDRET_GJT_12_18
    override val template: LetterTemplate<*, Unit> = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - forlengelse av gjenlevendetillegg i uføretrygden din",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title{
            text(
                Bokmal to "TEST",
                Nynorsk to "TEST",
                English to "TEST"
            )
        }
    }


}