package no.nav.pensjon.brev.maler.ufore.endring

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.pe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.virkningFom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArRedigerbarDto.pesysData
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArRedigerbarDto.pesysData.vedtakData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUforetrygdPGAInntektNesteArRedigerbar : RedigerbarTemplate<EndretUforetrygdPGAInntektNesteArRedigerbarDto> {

    override val featureToggle = FeatureToggles.brevmalUtDesemberkjoring.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_ENDRET_PGA_INNTEKT_NESTE_AR_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - inntekt og uføretrygd neste år",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = pesysData.vedtakData

        title {
            text(
                bokmal { +"Slik blir dine utbetalinger i " + data.virkningFom.year.format() },
                nynorsk { +"Slik blir utbetalingane dine i " + data.virkningFom.year.format() },
            )
        }

        outline {
            includePhrase(EndretUforetrygdPGAInntektNesteArFelles.Outline(data))
        }

        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}
