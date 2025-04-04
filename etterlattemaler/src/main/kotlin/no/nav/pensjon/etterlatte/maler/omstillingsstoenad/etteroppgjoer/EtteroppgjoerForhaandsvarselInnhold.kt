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

data class EtteroppgjoerForhaandsvarselRedigerbartBrevDTO(val type: String) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object EtteroppgjoerForhaandsvarselInnhold : EtterlatteTemplate<EtteroppgjoerForhaandsvarselRedigerbartBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_FORHAANDSVARSEL_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerForhaandsvarselRedigerbartBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Etteroppgjør Forhåndsvarsel Innhold",
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

