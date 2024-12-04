package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDtoSelectors.ytelseForAldersovergang
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV

@TemplateModelHelpers

// MF_000129 : AP_INFO_AO67_AUTO
object InfoAldersovergang67AarAuto : AutobrevTemplate<InfoAlderspensjonOvergang67AarAutoDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_AP_INFO_ALDERSOVERGANG_67_AAR_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = InfoAlderspensjonOvergang67AarAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon til deg som snart fyller 67 år",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Informasjon om alderspensjon til deg som snart fyller 67 år",
                Nynorsk to "Informasjon om alderspensjon til deg som snart fyller 67 år",
                English to "Information about retirement pension for people who are about to turn 67"
            )
        }
        outline {
            includePhrase(InnledningInfoYtelse(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoVelgeAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoOmregningUTtilAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoOenskeSokeAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoSivilstandAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoFTAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoAFPprivatAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoSoekeAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoSoekeAnnenGradAP(ytelseForAldersovergangKode = ytelseForAldersovergang))
            includePhrase(InfoSkattAP)
            includePhrase(InfoLevealderAP)
            includePhrase(InfoInntektAP)
            includePhrase(InfoBoddArbeidetUtlandet)
            includePhrase(InfoPensjonFraAndreAP)
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}




