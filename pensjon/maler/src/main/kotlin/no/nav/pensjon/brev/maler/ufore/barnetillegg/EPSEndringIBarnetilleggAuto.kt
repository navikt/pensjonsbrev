package no.nav.pensjon.brev.maler.ufore.barnetillegg

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.EPSEndringIBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.ePSEndringIBarnetilleggAutoDto.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.ePSEndringIBarnetilleggData.*
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EPSEndringIBarnetilleggAuto : AutobrevTemplate<EPSEndringIBarnetilleggAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_EPS_ENDRING_I_BARNETILLEGG

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring i barnetillegg pga. inntekt ektefelle/samboer",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = vedtakData

        title {
            text(
                bokmal { +"Vi har endret barnetillegget i uføretrygden din" },
                nynorsk { +"Vi har endra barnetillegget i uføretrygda di" })
        }
        outline {
            includePhrase(EPSEndringIBarnetillegg.Outline(
                EPSEndringIBarnetillegg.Brevdata(
                    virkningstidspunkt = data.virkningstidspunkt,
                    nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg,
                    nettoBarnetilleggFB = data.nettoBarnetilleggFB,
                    nettoBarnetilleggSB = data.nettoBarnetilleggSB,
                    totalbelop = data.totalbelop,
                    harBarnetilleggSB = data.harBarnetilleggSB,
                )
            ))
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, data.dineRettigheterOgPlikterUfore)
    }
}