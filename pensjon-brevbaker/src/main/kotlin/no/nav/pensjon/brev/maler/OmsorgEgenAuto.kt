package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.aarEgenerklaringOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.aarInnvilgetOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDtoSelectors.egenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.vedlegg.egenerklaeringPleieOgOmsorgsarbeid
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object OmsorgEgenAuto : AutobrevTemplate<OmsorgEgenAutoDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_OMSORG_EGEN_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmsorgEgenAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Egenerklæring godskriving omsorgspoeng",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
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
                            "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ".expr()
                            + aarEgenerklaringOmsorgspoeng
                            + ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.",

                    Nynorsk to
                            "Vi treng ei stadfesting på at du har utført pleie- og omsorgsarbeid i ".expr()
                            + aarEgenerklaringOmsorgspoeng
                            + ". Du må difor nytte det vedlagde skjemaet og sende til oss innan fire veker.",

                    English to
                            "We need you to confirm that you have provided nursing and care work in ".expr()
                            + aarEgenerklaringOmsorgspoeng
                            + ". Therefore, it is required that you complete the enclosed form and return it to NAV within four weeks.",
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
            paragraph {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to ""
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på ${Constants.OMSORGSOPPTJENING_URL}."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON},"
                            + " hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.OMSORGSOPPTJENING_URL}."
                            + " Om du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON},"
                            + " kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to "You can find more information at ${Constants.OMSORGSOPPTJENING_URL}."
                            + " At ${Constants.KONTAKT_URL}, you can chat or write to us."
                            + " If you do not find the answer at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON},"
                            + " weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                )
            }
        }

        includeAttachment(egenerklaeringPleieOgOmsorgsarbeid, egenerklaeringOmsorgsarbeidDto)
    }

}