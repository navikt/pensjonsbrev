package no.nav.pensjon.brev.aldersovergang

import brev.felles.HarDuSpoersmaalAlder
import brev.felles.InfoInntektAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoAFPprivatAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoBoddArbeidetUtlandet
import no.nav.pensjon.brev.aldersovergang.fraser.InfoFTAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoOenskeSokeAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoOnsketUttakAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoPensjonFraAndreAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoSivilstandAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoSkattAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoSoekeAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoSoekeAnnenGradAP
import no.nav.pensjon.brev.aldersovergang.fraser.InfoVelgeAP
import no.nav.pensjon.brev.aldersovergang.fraser.InnledningInfoYtelse
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAlderspensjonOvergang67AarAutoDtoSelectors.ytelseForAldersovergang
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InfoAldersovergang67AarAuto : AutobrevTemplate<InfoAlderspensjonOvergang67AarAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_INFO_ALDERSOVERGANG_67_AAR_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon til deg som snart fyller 67 år",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Informasjon om alderspensjon til deg som snart fyller 67 år" },
                    nynorsk { +"Informasjon om alderspensjon til deg som snart fyller 67 år" },
                    english { +"Information about retirement pension for people who are about to turn 67" },
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
                includePhrase(HarDuSpoersmaalAlder)
            }
        }
}
