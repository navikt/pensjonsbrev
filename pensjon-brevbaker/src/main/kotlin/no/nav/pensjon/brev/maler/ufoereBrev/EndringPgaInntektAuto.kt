package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.EndringPgaInntektAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.EndringPgaInntektAutoDtoSelectors.fellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.EndringPgaInntektAutoDtoSelectors.saerkullsbarn
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
/*
@TemplateModelHelpers
object EndringPgaInntektAuto : AutobrevTemplate<EndringPgaInntektAutoDto> {

    override val kode = Brevkode.AutoBrev.UT_ENDRING_PGA_INNTEKT_AUTO

    override val template = createTemplate(
        name = EndringPgaInntektAuto.kode.name,
        letterDataType = EndringPgaInntektAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring på grunn av inntekt",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val harFellesbarn = fellesbarn.harBeloepEndringBarnetillegg.notNull()
        val harSaekullsbarn = saerkullsbarn.harBeloepEndringBarnetillegg.notNull()
        val harFlerebarn =
            gjelderFlereBarn or (fellesbarn.harBeloeEndringBarnetillegg and saerkullsbarn.harBeoepEndringBarnetillegg)
        title {
            showIf(harEndringIUfoeretrygd and not(harBarnetillegg)) {
                text(
                    Bokmal to "NAV har endret utbetalingen av uføretrygden din",
                    Nynorsk to "NAV har endra utbetalinga av uføretrygda di",
                    English to "NAV has changed your payment amount for disability benefit"
                )
            }
            showIf(harBeloepEndringBarnetillegg and not(harBeloepEndringUfoeretrygd)) {
                textExpr(
                    Bokmal to "NAV har endret utbetalingen av ".expr()
                            + ifElse(harFlerebarn, ifTrue = "barnetilleggene", ifFalse = "barnetillegget"),
                    Nynorsk to "NAV har endra utbetalinga av ".expr()
                            + ifElse(harFlerebarn, ifTrue = "barentillegga", ifFalse = "barnetillegget"),
                    English to "NAV has changed your payment for child ".expr()
                            + ifElse(harFlerebarn, ifTrue = "benefits", ifFalse = "benefit")
                )
            }
            showIf(harBeleopEndringUfoeretrygd and harBeloepEndringBarnetillegg) {
                textExpr(
                    Bokmal to "NAV har endret utbetalingen av uføretrygden din og ".expr()
                            + ifElse(harFlerebarn, ifTrue = "barnetilleggene", ifFalse = "barnetillegget"),
                    Nynorsk to "NAV har endra utbetalinga av uføretrygda di og ".expr()
                            + ifElse(harFlerebarn, ifTrue = "barnetillegga", ifFalse = "barnetillegget"),
                    English to "NAV has changed your payment amount for disability benefit and child ".expr()
                            + ifElse(harFlerebarn, ifTrue = "benefits", ifFalse = "benefit")
                )
            }
        }
*/