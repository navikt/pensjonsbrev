package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.feilutbetaltTotalBeloep
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.oversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.resultatAvVurderingenForTotalBeloep
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sluttPeriodeForTilbakekreving
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.startPeriodeForTilbakekreving
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sumTilInnkrevingTotalBeloep
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.vedlegg.oversiktOverFeilutbetalingerPE
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
// MF_000190
// If an incorrect pension benefit payment is to be repaid, in full or in part -> TilbakekrevingResultat = FULL_TILBAKEKREV || DELVIS_TILBAKEKREV

object TilbakekrevingAvFeilutbetaltBeloep : RedigerbarTemplate<TilbakekrevingAvFeilutbetaltBeloepDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_TILBAKEKREVING_AV_FEILUTBETALT_BELOEP
    override val kategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = TilbakekrevingAvFeilutbetaltBeloepDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - tilbakekreving av feilutbetalt beløp",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val feilutbetaltTotalBeloep = pesysData.feilutbetaltTotalBeloep
        val resultatAvVurderingenForTotalBeloep = pesysData.resultatAvVurderingenForTotalBeloep
        val sluttPeriodeForTilbakekreving = pesysData.sluttPeriodeForTilbakekreving
        val startPeriodeForTilbakekreving = pesysData.startPeriodeForTilbakekreving
        val sumTilInnkrevingTotalBeloep = pesysData.sumTilInnkrevingTotalBeloep
        val sakstype = pesysData.sakstype.format().ifNull(fritekst("ytelse"))

        title {
            textExpr(
                Bokmal to "Du må betale tilbake ".expr() + sakstype,
                Nynorsk to "Du må betale tilbake ".expr() + sakstype,
                English to "You have to repay ".expr() + sakstype,
            )
        }
        outline {
            paragraph {
                val dato = fritekst("dato")
                textExpr(
                    Bokmal to "Vi viser til forhåndsvarselet vårt ".expr() + dato + ".",
                    Nynorsk to "Vi viser til førehandsvarselet vårt ".expr() + dato + ".",
                    English to "This is in reference to our advance notification dated ".expr() + dato + "."
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du har fått for mye ".expr() + sakstype
                            + " utbetalt fra ".expr() + startPeriodeForTilbakekreving.format() + " til ".expr() + sluttPeriodeForTilbakekreving.format()
                            + ". Dette er ".expr() + feilutbetaltTotalBeloep.format() + " kroner inkludert skatt.",

                    Nynorsk to "Du har fått for mykje ".expr() + sakstype
                            + " utbetalt frå ".expr() + startPeriodeForTilbakekreving.format() + " til ".expr() + sluttPeriodeForTilbakekreving.format()
                            + ". Dette er ".expr() + feilutbetaltTotalBeloep.format() + " kroner inkludert skatt.",

                    English to "You have received too much ".expr() + sakstype
                            + " in the period from ".expr() + startPeriodeForTilbakekreving.format() + " to ".expr() + sluttPeriodeForTilbakekreving.format()
                            + ". This amounts to NOK ".expr() + feilutbetaltTotalBeloep.format() + " including tax.",
                )
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har kommet fram til at du skal betale tilbake hele beløpet. Det vil si ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trukket fra.",
                        Nynorsk to "Vi har kome fram til at du skal betale tilbake heile beløpet. Det vil seie ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trekt frå.",
                        English to "We have concluded that you must repay the full excess payment you have received. This amounts to NOK ".expr() + sumTilInnkrevingTotalBeloep.format() + " after deduction of tax."
                    )
                }
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har kommet fram til at du skal betale tilbake deler av beløpet. Det vil si ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trukket fra.",
                        Nynorsk to "Vi har kome fram til at du skal betale tilbake delar av beløpet. Det vil seie ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trektfrå.",
                        English to "We have concluded that you must repay some of the excess payment you have received. This amounts to NOK ".expr() + sumTilInnkrevingTotalBeloep.format() + " after deduction of tax."
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Beløpet som er trukket i skatt vil Nav få tilbake av Skatteetaten.",
                    Nynorsk to "Beløpet som er trekt i skatt vil Nav få tilbake av Skatteetaten.",
                    English to "Nav will collect the tax amount from the Norwegian Tax Administration."
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 22-15.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 22-15.",
                    English to "This decision is made pursuant to the provisions of section 22-15 of the National Insurance Act."
                )
            }
            paragraph {
                text(
                    Bokmal to "I vedlegget ",
                    Nynorsk to "I vedlegget ",
                    English to "The attachment titled "
                )
                namedReference(oversiktOverFeilutbetalingerPE)
                text(
                    Bokmal to " finner du en oversikt over periodene med feilutbetalinger og beløpet du må betale tilbake.",
                    Nynorsk to " finn du ei oversikt over periodane med feilutbetalingar og beløpet du må betale tilbake.",
                    English to " provides details on the periods with payment errors and the amounts that need to be repaid."
                )
            }
            title1 {
                text(
                    Bokmal to "Betydning for skatteoppgjøret",
                    Nynorsk to "Betydning for skatteoppgjeret",
                    English to "Significance for the tax settlement"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi rapporterer endringen til Skatteetaten. De vil korrigere skatteoppgjøret ditt ut fra denne endringen.",
                    Nynorsk to "Vi rapporterer endringa til Skatteetaten. Dei vil korrigere skatteoppgjeret ditt ut frå denne endringa.",
                    English to "We will report the change to the Norwegian Tax Administration. They will correct your tax settlement in view of this change."
                )
            }
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            paragraph {
                text(
                    Bokmal to "Selv om du klager på vedtaket, må du begynne å betale tilbake. Dette går frem av forvaltningsloven § 42 og hvordan vi må praktisere regelverket.",
                    Nynorsk to "Sjølv om du klagar på vedtaket, må du begynne å betale tilbake. Dette går fram av forvaltningslova § 42 og korleis vi må praktisere regelverket.",
                    English to "Even if you appeal this decision, you must start repaying. " +
                            "This is stated in section 42 of the Public Administration Act and in the the guidelines for our application of the regulations."
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi kan utsette tilbakekrevingen til klagen er behandlet, for eksempel hvis det er sannsynlig at vedtaket blir omgjort. " +
                            "Du kan også søke om utsettelse av tilbakebetaling, men vi kan ikke ta hensyn til den økonomiske situasjonen din når vi vurderer om du kan utsette og betale tilbake.",
                    Nynorsk to "Vi kan utsetje tilbakekrevjinga til klaga er behandla, for eksempel dersom det er sannsynleg at vedtaket blir gjort om. " +
                            "Du kan også søkje om utsetjing av tilbakebetalinga, men vi kan ikkje ta omsyn til den økonomiske situasjonen din når vi vurderer om du kan utsetje å betale tilbake.",
                    English to "We may postpone sending you the claim for repayment until your appeal has been processed; for example, if it seems likely that the decision will be overturned. " +
                            "You can also apply for deferral of the repayment, but we cannot take your financial situation into account when assessing whether you qualify for a deferral."
                )
            }
            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(oversiktOverFeilutbetalingerPE, pesysData.oversiktOverFeilutbetalingPEDto)
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
    }
}