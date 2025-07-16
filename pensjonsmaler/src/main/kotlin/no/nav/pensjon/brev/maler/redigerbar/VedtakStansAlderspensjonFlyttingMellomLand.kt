package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.EksportForbudKode
import no.nav.pensjon.brev.api.model.EksportForbudKode.FLYKT_ALDER
import no.nav.pensjon.brev.api.model.EksportForbudKode.UFOR25_ALDER
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.brukersBostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.eksportForbudKode
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newTextExpr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakStansAlderspensjonFlyttingMellomLand : RedigerbarTemplate<VedtakStansAlderspensjonFlyttingMellomLandDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_STANS_FLYTTING_MELLOM_LAND
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_FLYTTE_MELLOM_LAND
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakStansAlderspensjonFlyttingMellomLandDto::class,
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - stans av alderspensjon ved flytting mellom land",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val kravVirkDatoFom = pesysData.kravVirkDatoFom
        val brukersBostedsland = pesysData.brukersBostedsland
        val eksportForbudKode = pesysData.eksportForbudKode


        title {
            textExpr(
                Bokmal to "Vi stanser alderspensjonen din fra ".expr() + kravVirkDatoFom.format(),
                Nynorsk to "Vi stansar alderspensjonen din frå ".expr() + kravVirkDatoFom.format(),
                English to "We are stopping your retirement pension from ".expr() + kravVirkDatoFom.format(),
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            // flyttingAPstans
            paragraph {
                textExpr(
                    Bokmal to "Vi har fått melding om at du har flyttet til ".expr() + brukersBostedsland,
                    Nynorsk to "Vi har fått melding om at du har flytta ti ".expr() + brukersBostedsland,
                    English to "We have received notice that you have moved to ".expr() + brukersBostedsland
                )
            }
            showIf(eksportForbudKode.isOneOf(UFOR25_ALDER)) {
                paragraph {
                    text(
                        Bokmal to "Når du flytter til utlandet har du ikke lenger rett til pensjon etter reglene for unge uføre. "
                                + "Derfor stanser vi utbetalingen av alderspensjonen din.",
                        Nynorsk to "Når du flyttar til utlandet har du ikkje lenger rett til alderspensjon etter reglane for unge uføre. "
                                + "Derfor stansar vi utbetalinga av alderspensjonen din.",
                        English to "When you move abroad, you are no longer eligible for retirement pension calculated in accordance with the regulations for young people with disabilities, "
                                + "you have to live in Norway. We are therefore stopping your retirement pension.",
                    )
                }
            }.orShowIf(eksportForbudKode.isOneOf(FLYKT_ALDER)) {
                paragraph {
                    text(
                        Bokmal to "Når du flytter til et land utenfor EØS-området har du ikke lenger rett til alderspensjon etter reglene for flyktninger. "
                                + "Derfor stanser vi utbetalingen av alderspensjonen din.",
                        Nynorsk to "Når du flyttar til eit land utanfor EØS-området har du ikkje lenger rett til alderspensjon etter reglane for flyktningar. "
                                + "Derfor stansar vi utbetalinga av alderspensjonen din.",
                        English to "When you move to a country outside the EEA region, you are no longer eligible for retirement pension calculated in accordance with the regulations for refugees. "
                                + "We are therefore stopping your retirement pension.",
                    )
                }
            }
            showIf()
        }


    }
}