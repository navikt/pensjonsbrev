package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.feilutbetaltTotalBeloep
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.harMotregning
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.resultatAvVurderingenForTotalBeloep
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sluttPeriodeForTilbakekreving
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.startPeriodeForTilbakekreving
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sumTilInnkrevingTotalBeloep
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
// Conditional for inclusion is if incorrect payment is to be repaid, in full or in part -> TilbakekrevingResultat = FULL_TILBAKEKREV || DELVIS_TILBAKEKREV
object TilbakekrevingAvFeilutbetaltBeloep : RedigerbarTemplate<TilbakekrevingAvFeilutbetaltBeloepDto> {

    // MF_000190
    override val kode = Pesysbrevkoder.Redigerbar.PE_TILBAKEKREVING_AV_FEILUTBETALT_BELØP
    override val kategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = TilbakekrevingAvFeilutbetaltBeloepDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - tilbakekreving av feilutbetalt",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val feilutbetaltTotalBeloep = pesysData.feilutbetaltTotalBeloep
        val harMotregning = pesysData.harMotregning
        val resultatAvVurderingenForTotalBeloep = pesysData.resultatAvVurderingenForTotalBeloep
        val sakstype = pesysData.sakstype
        val sluttPeriodeForTilbakekreving = pesysData.sluttPeriodeForTilbakekreving
        val startPeriodeForTilbakekreving = pesysData.startPeriodeForTilbakekreving
        val sumTilInnkrevingTotalBeloep = pesysData.sumTilInnkrevingTotalBeloep

        title {
            text(
                Bokmal to "Du må betale tilbake ",
                Nynorsk to "Du må betale tilbake ",
                English to "You have to repay ",
            )
            showIf(sakstype.isOneOf(Sakstype.AFP)) {
                text(Bokmal to "AFP", Nynorsk to "AFP", English to "contractual pension (AFP)")
            }
            showIf(sakstype.isOneOf(Sakstype.AFP_PRIVAT)) {
                text(
                    Bokmal to "AFP i privat sektor",
                    Nynorsk to "AFP i privat sektor",
                    English to "contractual pension (AFP) in the private sector"
                )
            }
            showIf(sakstype.isOneOf(Sakstype.ALDER)) {
                text(Bokmal to "alderspensjon", Nynorsk to "alderspensjon", English to "retirement pension")
            }.orShow {
                val ytelse = fritekst("ytelse")
                textExpr(Bokmal to ytelse, Nynorsk to ytelse, English to ytelse)
            }
        }
        outline {
            paragraph {
                val dato = fritekst("dato")
                text(
                    Bokmal to "Vi viser til forhåndsvarselet vårt ",
                    Nynorsk to "Vi viser til førehandsvarselet vårt ",
                    English to "This is in reference to our advancenotification dated"
                )
                textExpr(Bokmal to dato, Nynorsk to dato, English to dato)
            }
            paragraph {
                text(
                    Bokmal to "Du har fått for mye ",
                    Nynorsk to "Du har fått for mykje ",
                    English to "You have received too much "
                )
                showIf(sakstype.isOneOf(Sakstype.AFP)) {
                    text(Bokmal to "AFP", Nynorsk to "AFP", English to "contractual pension (AFP)")
                }
                showIf(sakstype.isOneOf(Sakstype.AFP_PRIVAT)) {
                    text(
                        Bokmal to "AFP i privat sektor",
                        Nynorsk to "AFP i privat sektor",
                        English to "contractual pension (AFP) in the private sector"
                    )
                }
                showIf(sakstype.isOneOf(Sakstype.ALDER)) {
                    text(Bokmal to "alderspensjon", Nynorsk to "alderspensjon", English to "retirement pension")
                }.orShow {
                    val ytelse = fritekst("ytelse")
                    textExpr(Bokmal to ytelse, Nynorsk to ytelse, English to ytelse)
                }
                textExpr(
                    Bokmal to " utbetalt fra ".expr() + startPeriodeForTilbakekreving.format() + " til ".expr() + sluttPeriodeForTilbakekreving.format() + ". ",
                    Nynorsk to " utbetalt frå ".expr() + startPeriodeForTilbakekreving.format() + " til ".expr() + sluttPeriodeForTilbakekreving.format() + ". ",
                    English to " in the period from ".expr() + startPeriodeForTilbakekreving.format() + " to ".expr() + sluttPeriodeForTilbakekreving.format() + ". "
                )
                textExpr(
                    Bokmal to "Dette er ".expr() + feilutbetaltTotalBeloep.format() + " kroner inkludert skatt.",
                    Nynorsk to "Dette er ".expr() + feilutbetaltTotalBeloep.format() + " kroner inkludert skatt.",
                    English to "This amounts to NOK ".expr() + feilutbetaltTotalBeloep.format() + " including tax."

                )
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har kommet fram til at du skal betale tilbake hele beløpet. Det vil si ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trukket fra.",
                        Nynorsk to "Vi har kome fram til at du skal betale tilbake heile beløpet. Det vil seie ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trektfrå.",
                        English to "We have concluded that you must repay the full excess payment you have received. This amounts to NOK ".expr() + sumTilInnkrevingTotalBeloep.format() + " after deduction of tax."
                    )
                }
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har kommet fram til at du skal betale tilbake deler av beløpet. Det vil si ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trukket fra.",
                        Nynorsk to "Vi har kome fram til at du skalbetale tilbake delar av beløpet. Det vil seie ".expr() + sumTilInnkrevingTotalBeloep.format() + " kroner etter at skatten er trektfrå.",
                        English to "We have concluded that you mustrepay some of the excess payment you have received. This amounts to NOK ".expr() + sumTilInnkrevingTotalBeloep.format() + " after deduction of tax."
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Beløpet som er trukket i skatt vil Nav få tilbake av Skatteetaten.",
                    Nynorsk to "Beløpet som er trekt i skatt vil Nav få tilbakeav Skatteetaten.",
                    English to "Nav will collect the tax amount from the Norwegian Tax Administration."
                )
            }
            showIf(harMotregning) {
                paragraph {
                    text(
                        Bokmal to "Du har i samme periode fått utbetalt for lite ",
                        Nynorsk to "Du har i same perioden fått utbetalt for lite ",
                        English to "In the same period, you have received too little "
                    )
                    showIf(sakstype.isOneOf(Sakstype.AFP)) {
                        text(Bokmal to "AFP. ", Nynorsk to "AFP. ", English to "contractual pension (AFP). ")
                    }
                    showIf(sakstype.isOneOf(Sakstype.AFP_PRIVAT)) {
                        text(
                            Bokmal to "AFP i privat sektor. ",
                            Nynorsk to "AFP i privat sektor. ",
                            English to "contractual pension (AFP) in the private sector. "
                        )
                    }
                    showIf(sakstype.isOneOf(Sakstype.ALDER)) {
                        text(
                            Bokmal to "alderspensjon. ",
                            Nynorsk to "alderspensjon. ",
                            English to "retirement pension. "
                        )
                    }.orShow {
                        val ytelse = fritekst("ytelse. ")
                        textExpr(Bokmal to ytelse, Nynorsk to ytelse, English to ytelse)
                    }
                    text(
                        Bokmal to "Dette er tatt med i beregningen vår.",
                        Nynorsk to "Dette er teke med i berekninga vår.",
                        English to "This has been taken into account in our calculations."
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 22-15.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 22-15.",
                    English to "This decision was made pursuant to the provisions of section 22-15 of the National Insurance Act."
                )
            }
            paragraph {
                text(
                    Bokmal to "I vedlegget finner du en oversikt over periodene med feilutbetalinger og beløpet du må betale tilbake.",
                    Nynorsk to "I vedlegget finn du ei oversikt overperiodane med feilutbetalingar og beløpet du må betale tilbake.",
                    English to "The attachment contains an overview ofthe periods with payments in error and the amounts you must repay"
                )
            }
            title1 {
                text(
                    Bokmal to "Betydning for skatteoppgjøret",
                    Nynorsk to "Betydning for skatteoppgjeret",
                    English to "Impact on your tax settlement"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi rapporterer endringen til Skatteetaten. De vil korrigere skatteoppgjøret ditt ut fra denne endringen.",
                    Nynorsk to "Vi rapporterer endringa til Skatteetaten. Dei vil korrigere skatteoppgjeretditt ut frå denne endringa.",
                    English to "We will report the change to the Norwegian Tax Administration. They will correct your tax settlement in view of this change.",
                )
            }
            title1 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. " +
                            "Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage. I vedlegget får du vite mer om hvordan du går fram.",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. " +
                            "Klaga skal vera skriftleg. Du finn skjema og informasjon på nav.no/klage. I vedlegget får du vite meir om korleis du går fram.",
                    English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. " +
                            "Your appeal must be made in writing. You will find a form and information about this at nav.no/klage. The appendix includes information on how to proceed."
                )
            }
            paragraph {
                text(
                    Bokmal to "Selv om du klager på vedtaket, må du begynne å betale tilbake. Dette går frem av forvaltningsloven § 42 og hvordan vi må praktisere regelverket.",
                    Nynorsk to "Sjølv om du klagar på vedtaket, må du begynne å betale tilbake. Dette går fram av forvaltningslova § 42 og korleis vi må praktisere regelverket.",
                    English to "Even if you appeal this decision, you will need to start the repayments. " +
                            "This follows from section 42 of the Public Administration Act and the rules on our implementation of the regulations."
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi kan utsette tilbakekrevingen til klagen er behandlet, for eksempel hvis det er sannsynlig at vedtaket blir omgjort. " +
                            "Du kan også søke om utsettelse av tilbakebetaling, men vi kan ikke ta hensyn til den økonomiske situasjonen din når vi vurderer om du kan utsette og betale tilbake",
                    Nynorsk to "Vi kan utsetje tilbakekrevjinga til klaga er behandla, for eksempel dersom det er sannsynleg at vedtaket blir gjort om. " +
                            "Du kan også søkje om utsetjing av tilbakebetalinga, men vi kan ikkje ta omsyn til den økonomiske situasjonen din når vi vurderer om du kan utsetje å betale tilbake.",
                    English to "We may postpone sending you the claim for repayment until your appeal has been processed; for example, if it seems likely that the decision will be overturned. " +
                            "You can also apply for deferral of the repayment, but we cannot take your financial situation into account when assessing whether you qualify for a deferral."
                )
            }
            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Felles.HarDuSpoersmaal.alder)
            // vedlegg: oversiktOverFeilutbetalingerPE
            // vedlegg: Dine rettigheter og mulighet til å klage
        }
    }
}