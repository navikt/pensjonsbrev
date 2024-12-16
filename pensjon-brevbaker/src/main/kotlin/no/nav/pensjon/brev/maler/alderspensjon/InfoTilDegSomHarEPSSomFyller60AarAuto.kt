package no.nav.pensjon.brev.maler.alderspensjon

import no.nav.pensjon.brev.api.model.maler.InfoTilDegSomHarEPSSomFyller60AarAutoDto
import no.nav.pensjon.brev.api.model.maler.InfoTilDegSomHarEPSSomFyller60AarAutoDtoSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.alder.InfoSaerskiltSatsEPS60
import no.nav.pensjon.brev.maler.fraser.alder.InfoSaerskiltSatsEPS60DetteMaaDuGjoere
import no.nav.pensjon.brev.maler.fraser.alder.InfoSaerskiltSatsEPS60Grunnen
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV

@TemplateModelHelpers

// MF_000238 : VSS_INFO_EPS60_AUTO

object InfoTilDegSomHarEPSSomFyller60AarAuto : AutobrevTemplate<InfoTilDegSomHarEPSSomFyller60AarAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_INFO_EPS_SOM_FYLLER_60_AAR_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = InfoTilDegSomHarEPSSomFyller60AarAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon til deg med ektefelle/partner/samboer som fyller 60 år",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Informasjon til deg som har ektefelle/partner/samboer som snart fyller 60 år",
                Nynorsk to "Informasjon til deg som har ektefelle/partnar/sambuar som snart fyller 60 år",
                English to "Information for you with a spouse/partner/cohabitant who is soon turning 60"
            )
        }
        outline {
            // infoSoerSatsInnledning_001
            paragraph {
                text(
                    Bokmal to "Er inntekten til ektefellen/partneren/samboeren din lavere enn folketrygdens grunnbeløp (G)?",
                    Nynorsk to "Er inntekta til ektefellen/partnaren/sambuaren din lågare enn folketrygda sitt grunnbeløp (G)?",
                    English to "Is your spouse/partner/cohabitant's income lower than the National Insurance basic amount (G)?"
                )
            }
            includePhrase(InfoSaerskiltSatsEPS60(sakstype))
            includePhrase(InfoSaerskiltSatsEPS60Grunnen(sakstype))
            includePhrase(InfoSaerskiltSatsEPS60DetteMaaDuGjoere(sakstype))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}







