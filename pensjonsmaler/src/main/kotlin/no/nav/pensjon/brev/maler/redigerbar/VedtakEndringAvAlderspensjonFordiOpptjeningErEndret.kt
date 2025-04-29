package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.AlderspensjonVedVirkSelectors.skjermingstillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakEndringAvAlderspensjonFordiOpptjeningErEndret : RedigerbarTemplate<VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_PGA_OPPTJENING
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av alderspensjon fordi opptjening er endret",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            textExpr(
                Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ".expr() + pesysData.krav.virkDatoFom.format(),
                Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + pesysData.krav.virkDatoFom.format(),
                English to "We have recalculated your retirement pension from ".expr() + pesysData.krav.virkDatoFom.format(),
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            // skjermTillUtbetEndret_001
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler som gjør at du får skjermingstillegg i alderspensjonen din fra folketrygden.",
                    Nynorsk to "Stortinget har vedtatt nye reglar som gjer at du får skjermingstillegg i alderspensjonen din frå folketrygda.",
                    English to "The Storting has passed new rules that grant you a supplement for the disabled in your retirement pension from the National Insurance Scheme."
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Vi har derfor beregnet pensjonen din på nytt fra ".expr() + pesysData.krav.virkDatoFom.format() + ". Det gjør at du får mer i alderspensjon.",
                    Nynorsk to "Vi har difor rekna ut pensjonen din på nytt frå ".expr() + pesysData.krav.virkDatoFom.format() + ". Det gjer at du får meir i alderspensjon.",
                    English to "Therefore, we have recalculated your pension from ".expr() + pesysData.krav.virkDatoFom.format() + ". This means you will receive more in retirement pension."
                )
            }
            ifNotNull(pesysData.alderspensjonVedVirk.skjermingstillegg) { skjermingstillegg ->
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner i alderspensjon fra folketrygden hver måned før skatt. Av dette er skjermingstillegget " + skjermingstillegg.format() + " kroner.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + "  kroner i alderspensjon frå folketrygda kvar månad før skatt. Av dette er skjermingstillegget " + skjermingstillegg.format() + " kroner.",
                        English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " in retirement pension from the National Insurance Scheme each month before tax. Of this, the supplement for the disabled is NOK " + skjermingstillegg.format()  + "."
                    )
                }
            }

            paragraph {

            }
        }
    }
}