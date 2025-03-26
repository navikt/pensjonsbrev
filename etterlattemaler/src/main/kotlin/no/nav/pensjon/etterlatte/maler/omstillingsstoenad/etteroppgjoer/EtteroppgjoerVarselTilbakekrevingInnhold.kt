package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO


data class EtteroppgjoerVarselData(
    val type: String
)

data class EtteroppgjoerVarselDTO(
    override val innhold: List<Element>,
    val data: EtteroppgjoerVarselData,
) : FerdigstillingBrevDTO

data class EtteroppgjoerVarselRedigerbartBrevDTO(val type: String) : RedigerbartUtfallBrevDTO


@TemplateModelHelpers
object EtteroppgjoerVarselTilbakekrevingInnhold : EtterlatteTemplate<EtteroppgjoerVarselRedigerbartBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_VARSEL_TILBAKEKREVING_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerVarselRedigerbartBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel tilbakekreving etteroppgjør innhold",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV

        ),
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to ""
            )
        }
        outline {
            title2 {
                text(
                    Bokmal to "Redigerbar tekst i varsel om tilbakekreving",
                    Nynorsk to "Grunngiving for vedtaket",
                    English to "Grounds for the decision",
                )
            }
        }
    }
}

