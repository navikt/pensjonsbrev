package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDtoSelectors.ytelseForAldersovergang
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoAFPprivatAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoBoddArbeidetUtlandet
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoFTAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoInntektAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoOenskeSokeAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoOnsketUttakAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoPensjonFraAndreAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoSivilstandAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoSkattAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoSoekeAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoSoekeAnnenGradAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoVelgeAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnledningInfoYtelse
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV

// MF_000129 : AP_INFO_AO67_AUTO
@TemplateModelHelpers
object InfoAldersovergang67AarAuto : AutobrevTemplate<InfoAlderspensjonOvergang67AarAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_INFO_ALDERSOVERGANG_67_AAR_AUTO

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = InfoAlderspensjonOvergang67AarAutoDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon til deg som snart fyller 67 år",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { + "Informasjon om alderspensjon til deg som snart fyller 67 år" },
                    nynorsk { + "Informasjon om alderspensjon til deg som snart fyller 67 år" },
                    english { + "Information about retirement pension for people who are about to turn 67" },
                )
            }
            outline {
                includePhrase(InnledningInfoYtelse(ytelseForAldersovergang))
                includePhrase(InfoVelgeAP(ytelseForAldersovergang))
                includePhrase(InfoOnsketUttakAP(ytelseForAldersovergang))
                includePhrase(InfoOenskeSokeAP(ytelseForAldersovergang))
                includePhrase(InfoSivilstandAP(ytelseForAldersovergang))
                includePhrase(InfoFTAP(ytelseForAldersovergang))
                includePhrase(InfoAFPprivatAP(ytelseForAldersovergang))
                includePhrase(InfoSoekeAP(ytelseForAldersovergang))
                includePhrase(InfoSoekeAnnenGradAP(ytelseForAldersovergang))
                includePhrase(InfoSkattAP)
                includePhrase(InfoInntektAP)
                includePhrase(InfoBoddArbeidetUtlandet)
                includePhrase(InfoPensjonFraAndreAP)
                includePhrase(Felles.HarDuSpoersmaal.alder)
            }
        }
}
