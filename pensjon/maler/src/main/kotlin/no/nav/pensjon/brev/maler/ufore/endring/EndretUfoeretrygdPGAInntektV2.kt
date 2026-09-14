package no.nav.pensjon.brev.maler.ufore.endring


import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUTPgaInntektDtoV2.uforetrygd.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUTPgaInntektDtoV2.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntektV2 : AutobrevTemplate<EndretUTPgaInntektDtoV2> {

    // PE_UT_05_100
    // Brukes for eksempel når inntektsendring skjer via Inntektsplanlegger og BPEN090
    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT_V2

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt (automatisk)",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val endretUt = uforetrygd.endringsbelop.notEqualTo(0)
        title {
            showIf(endretUt and not(btfbEndret or btsbEndret)) {
                text(
                    bokmal { + "Vi endrer utbetalingen av uføretrygden du får " },
                    nynorsk { + "Vi endrar utbetalinga av uføretrygda du får " },
                )
            }.orShowIf(endretUt and (btfbEndret or btsbEndret)) {
                text(
                    bokmal { + "Vi endrer utbetalingen av uføretrygden og barnetillegget du får " },
                    nynorsk { + "Vi endrar utbetalinga av uføretrygda og barnetillegget du får " },
                )
            }.orShow {
                text(
                    bokmal { + "Vi endrer utbetalingen av barnetillegget du får " },
                    nynorsk { + "Vi endrar utbetalinga av barnetillegget du får " },
                )
            }
        }

        outline {
            includePhrase(EndretUfoeretrygdPGAInntekt.Outline(argument))
        }


        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
