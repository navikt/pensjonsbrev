package no.nav.pensjon.brev.maler.ufore.endring

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUfoeretrygdPGAInntektRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUfoeretrygdPGAInntektRedigerbarDto.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUfoeretrygdPGAInntektRedigerbarDto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUTPgaInntektDtoV2.uforetrygd.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUTPgaInntektDtoV2.*
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntektRedigerbar : RedigerbarTemplate<EndretUfoeretrygdPGAInntektRedigerbarDto> {

    override val featureToggle = FeatureToggles.brevmalUtEndretPgaInntektRedigerbar.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_ENDRET_PGA_INNTEKT_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = pesysData.data
        val endretUt = data.uforetrygd.endringsbelop.notEqualTo(0)
        val btfbEndret = data.btfbEndret
        val btsbEndret = data.btsbEndret

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
            includePhrase(EndretUfoeretrygdPGAInntekt.Outline(data, erRedigerbar = true))
        }

        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}
