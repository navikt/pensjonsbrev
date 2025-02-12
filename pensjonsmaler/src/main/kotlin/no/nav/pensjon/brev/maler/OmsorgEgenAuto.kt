package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.aarEgenerklaringOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.aarInnvilgetOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.egenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.vedlegg.egenerklaeringPleieOgOmsorgsarbeid
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV

@TemplateModelHelpers
object OmsorgEgenAuto : AutobrevTemplate<OmsorgEgenAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_OMSORG_EGEN_AUTO

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmsorgEgenAutoDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Egenerklæring godskriving omsorgspoeng",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = INFORMASJONSBREV,
                ),
        ) {

            val aarEgenerklaringOmsorgspoeng = aarEgenerklaringOmsorgspoeng.format()

            title {
                text(
                    Bokmal to "Du må sende oss egenerklæring om pleie- og omsorgsarbeid",
                    Nynorsk to "Du må sende oss eigenmelding om pleie- og omsorgsarbeid",
                    English to "Personal declaration about the circumstances of care",
                )
            }

            outline {
                paragraph {
                    textExpr(
                        Bokmal to
                            "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ".expr() +
                            aarEgenerklaringOmsorgspoeng +
                            ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.",
                        Nynorsk to
                            "Vi treng ei stadfesting på at du har utført pleie- og omsorgsarbeid i ".expr() +
                            aarEgenerklaringOmsorgspoeng +
                            ". Du må difor nytte det vedlagde skjemaet og sende til oss innan fire veker.",
                        English to
                            "We need you to confirm that you have provided nursing and care work in ".expr() +
                            aarEgenerklaringOmsorgspoeng +
                            ". Therefore, it is required that you complete the enclosed form and return it to Nav within four weeks.",
                    )
                }
                paragraph {
                    val aarInnvilgetOmsorgspoeng = aarInnvilgetOmsorgspoeng.format()
                    textExpr(
                        Bokmal to "Du har fått godkjent pensjonsopptjening for ".expr() + aarInnvilgetOmsorgspoeng + ".",
                        Nynorsk to "Du har fått godkjend pensjonsopptening for ".expr() + aarInnvilgetOmsorgspoeng + ".",
                        English to "You have accumulated pensionable earnings for ".expr() + aarInnvilgetOmsorgspoeng + ".",
                    )
                }
                includePhrase(Felles.HarDuSpoersmaal.omsorg)
            }
            includeAttachment(egenerklaeringPleieOgOmsorgsarbeid, egenerklaeringOmsorgsarbeidDto)
        }
}
