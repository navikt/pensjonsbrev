package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

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
        val virkDatoFom = pesysData.virkDatoFom,


        title {
            textExpr(
                Bokmal to "Vi stanser alderspensjonen din fra ".expr() + virkDatoFom.format,
                Nynorsk to "Vi stansar alderspensjonen din frå ".expr() + virkDatoFom.format,
                English to "We are stopping your retirement pension from ".expr() + virkDatoFom.format,
            )
        }

    }
}