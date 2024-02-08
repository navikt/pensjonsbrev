package no.nav.pensjon.etterlatte.maler.andre

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTOSelectors.tittel
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat

@TemplateModelHelpers
object TomMalInformasjonsbrev : EtterlatteTemplate<ManueltBrevMedTittelDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TOM_MAL_INFORMASJONSBREV

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevMedTittelDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Infomasjonsbrev",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            ifNotNull(tittel) { tittel ->
                textExpr(
                    Bokmal to tittel,
                    Nynorsk to tittel,
                    English to tittel
                )
            } orShow {
                text(Bokmal to "", Nynorsk to "", English to "")
            }
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)
        }
    }
}
