package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

object VarselSaksbehandlingstidAuto : AutobrevTemplate<VarselSaksbehandlingstidAutoDto> {

    override val kode = Brevkode.AutoBrev.UT_VARSEL_SAKSBEHANDLINGSTID_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VarselSaksbehandlingstidAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Automatisk varsel om saksbehandlingstid",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Bokmal to "NAV har mottatt søknaden din om uføretrygd",
                Nynorsk to "NAV har motteke søknaden din om uføretrygd",
                English to "NAV has received your application for disability benefit"
            )
        }
        outline {


            includePhrase(
                Ufoeretrygd.MeldeFraOmEndringer
            )

            includePhrase(
                Ufoeretrygd.HarDuSpoersmaalUfoeretrygd
            )
        }
    }
}
