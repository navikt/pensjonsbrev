package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.SaksbehandlerValgSelectors.aarEgenerklaringOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.SaksbehandlerValgSelectors.aarInnvilgetOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.vedlegg.egenerklaeringPleieOgOmsorgsarbeidManuell
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OmsorgEgenManuell : RedigerbarTemplate<OmsorgEgenManuellDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_OMSORG_EGEN_MANUELL

    override val kategori = TemplateDescription.Brevkategori.BREV_MED_SKJEMA
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = Sakstype.all // TODO stemmer dette?
    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmsorgEgenManuellDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av egenerklæring om pleie- og omsorgsarbeid (omsorgsopptjening)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Language.Bokmal to "Du må sende oss egenerklæring om pleie- og omsorgsarbeid",
                Language.Nynorsk to "Du må sende oss eigenmelding om pleie- og omsorgsarbeid",
                Language.English to "Personal declaration about the circumstances of care",
            )
        }

        outline {
            paragraph {
                val aarEgenerklaringOmsorgspoeng = saksbehandlerValg.aarEgenerklaringOmsorgspoeng.format()
                textExpr(
                    Language.Bokmal to
                            "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ".expr()
                            + aarEgenerklaringOmsorgspoeng
                            + " år. Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.",

                    Language.Nynorsk to
                            "Vi treng ei stadfesting på at du har utført pleie- og omsorgsarbeid i ".expr()
                            + aarEgenerklaringOmsorgspoeng
                            + " år. Du må difor nytte det vedlagde skjemaet og sende til oss innan fire veker.",

                    Language.English to
                            "We need you to confirm that you have provided nursing and care work in ".expr()
                            + aarEgenerklaringOmsorgspoeng
                            + " years. Therefore, it is required that you complete the enclosed form and return it to Nav within four weeks.",
                )
            }
            paragraph {
                val aarInnvilgetOmsorgspoeng = saksbehandlerValg.aarInnvilgetOmsorgspoeng.format()
                textExpr(
                    Language.Bokmal to "Du har fått godkjent pensjonsopptjening for ".expr() + aarInnvilgetOmsorgspoeng + " år.",
                    Language.Nynorsk to "Du har fått godkjend pensjonsopptening for ".expr() + aarInnvilgetOmsorgspoeng + " år.",
                    Language.English to "You have accumulated pensionable earnings for ".expr() + aarInnvilgetOmsorgspoeng + " years.",
                )
            }
            includePhrase(Felles.HarDuSpoersmaal.omsorg)
        }
        includeAttachment(egenerklaeringPleieOgOmsorgsarbeidManuell, argument)
    }
}