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
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.vedlegg.oversiktOverFeilutbetalingerPE
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
// MF_000190
// If an incorrect pension benefit payment is to be repaid, in full or in part -> TilbakekrevingResultat = FULL_TILBAKEKREV || DELVIS_TILBAKEKREV

object TilbakekrevingAvFeilutbetaltBeloep : RedigerbarTemplate<TilbakekrevingAvFeilutbetaltBeloepDto> {

    override val featureToggle = FeatureToggles.vedtakTilbakekrevingAvFeilutbetaltBeloep.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_TILBAKEKREVING_AV_FEILUTBETALT_BELOEP
    override val kategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
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
            text(
                bokmal { + "Du må betale tilbake " + sakstype },
                nynorsk { + "Du må betale tilbake " + sakstype },
                english { + "You have to repay " + sakstype },
            )
        }
        outline {
            paragraph {
                val dato = fritekst("dato")
                text(
                    bokmal { + "Vi viser til forhåndsvarselet vårt " + dato + "." },
                    nynorsk { + "Vi viser til førehandsvarselet vårt " + dato + "." },
                    english { + "This is in reference to our advance notification dated " + dato + "." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har fått for mye " + sakstype
                            + " utbetalt fra " + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format()
                            + ". Dette er " + feilutbetaltTotalBeloep.format() + " inkludert skatt." },

                    nynorsk { + "Du har fått for mykje " + sakstype
                            + " utbetalt frå " + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format()
                            + ". Dette er " + feilutbetaltTotalBeloep.format() + " inkludert skatt." },

                    english { + "You have received too much " + sakstype
                            + " in the period from " + startPeriodeForTilbakekreving.format() + " to " + sluttPeriodeForTilbakekreving.format()
                            + ". This amounts to " + feilutbetaltTotalBeloep.format() + " including tax." },
                )
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
                paragraph {
                    text(
                        bokmal { + "Vi har kommet fram til at du skal betale tilbake hele beløpet. Det vil si " + sumTilInnkrevingTotalBeloep.format() + " etter at skatten er trukket fra." },
                        nynorsk { + "Vi har kome fram til at du skal betale tilbake heile beløpet. Det vil seie " + sumTilInnkrevingTotalBeloep.format() + " etter at skatten er trekt frå." },
                        english { + "We have concluded that you must repay the full excess payment you have received. This amounts to " + sumTilInnkrevingTotalBeloep.format() + " after deduction of tax." }
                    )
                }
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
                paragraph {
                    text(
                        bokmal { + "Vi har kommet fram til at du skal betale tilbake deler av beløpet. Det vil si " + sumTilInnkrevingTotalBeloep.format() + " etter at skatten er trukket fra." },
                        nynorsk { + "Vi har kome fram til at du skal betale tilbake delar av beløpet. Det vil seie " + sumTilInnkrevingTotalBeloep.format() + " etter at skatten er trektfrå." },
                        english { + "We have concluded that you must repay some of the excess payment you have received. This amounts to " + sumTilInnkrevingTotalBeloep.format() + " after deduction of tax." }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Beløpet som er trukket i skatt vil Nav få tilbake av Skatteetaten." },
                    nynorsk { + "Beløpet som er trekt i skatt vil Nav få tilbake av Skatteetaten." },
                    english { + "Nav will collect the tax amount from the Norwegian Tax Administration." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 22-15." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 22-15." },
                    english { + "This decision is made pursuant to the provisions of section 22-15 of the National Insurance Act." }
                )
            }
            paragraph {
                text(
                    bokmal { + "I vedlegget " },
                    nynorsk { + "I vedlegget " },
                    english { + "The attachment titled " }
                )
                namedReference(oversiktOverFeilutbetalingerPE)
                text(
                    bokmal { + " finner du en oversikt over periodene med feilutbetalinger og beløpet du må betale tilbake." },
                    nynorsk { + " finn du ei oversikt over periodane med feilutbetalingar og beløpet du må betale tilbake." },
                    english { + " provides details on the periods with payment errors and the amounts that need to be repaid." }
                )
            }
            title1 {
                text(
                    bokmal { + "Betydning for skatteoppgjøret" },
                    nynorsk { + "Betydning for skatteoppgjeret" },
                    english { + "Significance for the tax settlement" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi rapporterer endringen til Skatteetaten. De vil korrigere skatteoppgjøret ditt ut fra denne endringen." },
                    nynorsk { + "Vi rapporterer endringa til Skatteetaten. Dei vil korrigere skatteoppgjeret ditt ut frå denne endringa." },
                    english { + "We will report the change to the Norwegian Tax Administration. They will correct your tax settlement in view of this change." }
                )
            }
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            paragraph {
                text(
                    bokmal { + "Selv om du klager på vedtaket, må du begynne å betale tilbake. Dette går frem av forvaltningsloven § 42 og hvordan vi må praktisere regelverket." },
                    nynorsk { + "Sjølv om du klagar på vedtaket, må du begynne å betale tilbake. Dette går fram av forvaltningslova § 42 og korleis vi må praktisere regelverket." },
                    english { + "Even if you appeal this decision, you must start repaying. " +
                            "This is stated in section 42 of the Public Administration Act and in the the guidelines for our application of the regulations." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi kan utsette tilbakekrevingen til klagen er behandlet, for eksempel hvis det er sannsynlig at vedtaket blir omgjort. " +
                            "Du kan også søke om utsettelse av tilbakebetaling, men vi kan ikke ta hensyn til den økonomiske situasjonen din når vi vurderer om du kan utsette og betale tilbake." },
                    nynorsk { + "Vi kan utsetje tilbakekrevjinga til klaga er behandla, for eksempel dersom det er sannsynleg at vedtaket blir gjort om. " +
                            "Du kan også søkje om utsetjing av tilbakebetalinga, men vi kan ikkje ta omsyn til den økonomiske situasjonen din når vi vurderer om du kan utsetje å betale tilbake." },
                    english { + "We may postpone sending you the claim for repayment until your appeal has been processed; for example, if it seems likely that the decision will be overturned. " +
                            "You can also apply for deferral of the repayment, but we cannot take your financial situation into account when assessing whether you qualify for a deferral." }
                )
            }
            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(oversiktOverFeilutbetalingerPE, pesysData.oversiktOverFeilutbetalingPEDto)
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
    }
}