package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.maler.vedlegg.EgenerklaeringPleieOgOmsorgsarbeid
import no.nav.pensjon.brev.maler.vedlegg.egenerklaeringPleieOgOmsorgsarbeid
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

object OmsorgEgenAuto : VedtaksbrevTemplate {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.OMSORG_EGEN_AUTO

    override val template = createTemplate(
        name = kode.name,
        base = PensjonLatex,
        letterDataType = OmsorgEgenAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Egenerklæring godskriving omsorgspoeng",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        )
    ) {

        val aarEgenerklaringOmsorgspoeng = argument().select(OmsorgEgenAutoDto::aarEgenerklaringOmsorgspoeng).format()

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
                val aarInnvilgetOmsorgspoeng = argument().select(OmsorgEgenAutoDto::aarInnvilgetOmsorgspoeng).format()
                textExpr(
                    Bokmal to "Du har fått godkjent pensjonsopptjening for ".expr() + aarInnvilgetOmsorgspoeng + ".",
                    Nynorsk to "Du har fått godkjend pensjonsopptening for ".expr() + aarInnvilgetOmsorgspoeng + ".",
                    English to "You have accumulated pensionable earnings for ".expr() + aarInnvilgetOmsorgspoeng + ".",
                )
            }

        }

        includeAttachment(egenerklaeringPleieOgOmsorgsarbeid, argument().map {
            EgenerklaeringPleieOgOmsorgsarbeid(it.aarEgenerklaringOmsorgspoeng)
        })
    }

}