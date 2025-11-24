package no.nav.pensjon.brev.maler.alder.omregning.opptjening

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto.Pensjonsopptjening.Merknad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.GarantipensjonVedVirkSelectors.nettoUtbetaltPerManed_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harDagpenger
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harMerknadType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harUforetrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.pensjonsopptjeninger
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningSelectors.aarstall
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningSelectors.gjennomsnittligG
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningSelectors.merknader
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.PensjonsopptjeningSelectors.pensjonsgivendeinntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.VilkaarsVedtakSelectors.avslattGarantipensjon
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.garantipensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.pensjonsopptjeningKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.trygdetidNorge
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDtoSelectors.vilkarsVedtak
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner

// Spesialtilpasset V00011 i metaforce for endring pga. opptjening.
@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjening =
    createAttachment(
        title = newText(
            Bokmal to "Slik har vi beregnet pensjonen din",
            Nynorsk to "Slik har vi berekna pensjonen din",
            English to "This is how we have calculated your pension",
        ),
        includeSakspart = false
    ) {
        paragraph {
            text(
                bokmal { +"I dette vedlegget finner du opplysninger som vi har brukt for å regne ut alderspensjonen din." },
                nynorsk { +"I dette vedlegget finn du opplysningar som vi har brukt for å rekne ut alderspensjonen din." },
                english { +"In this attachment, you will find information that we have used to calculate your retirement pension." },
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du mener at opplysningene er feil, må du melde fra til Nav, fordi det kan ha betydning for størrelsen på pensjonen din." },
                nynorsk { +"Viss du meiner at opplysningane er feil, må du melde frå til Nav, fordi det kan ha noko å seie for storleiken på pensjonen din." },
                english { +"If you believe any information is incorrect, you must notify Nav, as it may affect the amount of your pension." },
            )
        }
        paragraph {
            text(
                bokmal { +"Uttaksgraden for alderspensjonen din er " + alderspensjonVedVirk.uttaksgrad.format() + " prosent." },
                nynorsk { +"Uttaksgraden for alderspensjonen din er " + alderspensjonVedVirk.uttaksgrad.format() + " prosent." },
                english { +"The rate of your retirement pension is " + alderspensjonVedVirk.uttaksgrad.format() + " percent." },
            )
        }
        includePhrase(
            OpplysningerBruktIBeregningTabellAP2025EndretPgaOpptjening(
                alderspensjonVedVirk = alderspensjonVedVirk,
                beregningKap20VedVirk = beregningKap20VedVirk,
                vilkarsVedtak = vilkarsVedtak,
                trygdetidsdetaljerKap20VedVirk = trygdetidsdetaljerKap20VedVirk,
                garantipensjonVedVirk = garantipensjonVedVirk,
                beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk
            )
        )

        val garantipensjonInnvilget = alderspensjonVedVirk.garantipensjonInnvilget
        val avslattGarantipensjon = vilkarsVedtak.avslattGarantipensjon
        val redusertTrygdetid = beregningKap20VedVirk.redusertTrygdetid

        paragraph {
            text(
                bokmal { +"Pensjonsopptjening" },
                nynorsk { +"Pensjonsopptening" },
                english { +"Your pension accruals" },
            )
            showIf(
                (garantipensjonInnvilget
                        and garantipensjonVedVirk.nettoUtbetaltPerManed_safe.ifNull(Kroner(0)).greaterThan(0))
                        or (not(garantipensjonInnvilget) and redusertTrygdetid)
            ) {
                text(
                    bokmal { +" og trygdetid" },
                    nynorsk { +" og trygdetid" },
                    english { +" and your national insurance coverage" },
                )
            }
            text(
                bokmal { +" tas med i beregningen av alderspensjon fra og med året etter at skatteoppgjøret er klart. Dette gjelder selv om skatteoppgjøret ditt er klart tidligere. I beregningen er det derfor brukt pensjonsopptjening til og med 2024." },
                nynorsk { +" blir teke med i berekninga av alderspensjon frå og med året etter at skatteoppgjeret er klart. Dette gjeld sjølv om skatteoppgjeret ditt er klart tidlegare. I berekninga er det difor brukt pensjonsopptening til og med 2024." },
                english { +" are taken into account when calculating retirement pension starting from the year after your tax assessment is finalised. This applies even if your tax assessment is completed earlier. In the calculation, pension accruals are used up to and including 2024." },
            )
        }
        title1 {
            text(
                bokmal { +"Delingstall" },
                nynorsk { +"Delingstall" },
                english { +"Life expectancy adjustment divisor at withdrawal" },
            )
        }
        paragraph {
            text(
                bokmal { +"Delingstallet uttrykker forventet levetid for ditt årskull på uttakstidspunktet. Alderen din ved uttak avgjør hvilket delingstall som gjelder for deg. Dette blir kalt levealdersjustering." },
                nynorsk { +"Delingstalet uttrykkjer forventa levetid for årskullet ditt på uttakstidspunktet. Alderen din ved uttak avgjer kva delingstal som gjeld for deg. Dette blir kalla levealdersjustering." },
                english { +"The divisor expresses the life expectancy for people born in the same year (cohort) at a specific pension withdrawal date. Your age at retirement determines which divisor applies to you. This is called life expectancy adjustment." },
            )
        }
        paragraph {
            text(
                bokmal { +"Inntektspensjonen " },
                nynorsk { +" " },
                english { +" " },
                FontType.BOLD
            )
            text(
                bokmal { +"din avhenger av arbeidsinntekt og annen pensjonsopptjening som du har hatt i folketrygden fra og med det året du fylte 17." },
                nynorsk { +"" },
                english { +"" },
            )
        }

        showIf(garantipensjonVedVirk.nettoUtbetaltPerManed_safe.ifNull(Kroner(0)).greaterThan(0)) {
            paragraph {
                text(
                    bokmal { +"Garantipensjon " },
                    nynorsk { +" " },
                    english { +" " },
                    FontType.BOLD
                )
                text(
                    bokmal { +"får du fordi du har tjent opp lav inntektspensjon. Garantipensjonen er avkortet mot inntektspensjonen din." },
                    nynorsk { +"" },
                    english { +"" },
                )
            }
        }

        showIf(
            garantipensjonVedVirk.nettoUtbetaltPerManed_safe.ifNull(Kroner(0)).greaterThan(0) and redusertTrygdetid
        ) {
            paragraph {
                text(
                    bokmal { +"Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Garantipensjonen din er redusert fordi du har mindre enn 40 års trygdetid." },
                    nynorsk { +"" },
                    english { +"" },
                )
            }
        }

        showIf(
            beregningKap20VedVirk.redusertTrygdetid and not(avslattGarantipensjon) and trygdetidNorge.size()
                .greaterThan(0)
        ) {
            includePhrase(Vedtak.TrygdetidOverskrift)
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetidInnledning)
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdetidNorge))
        }
        title1 {
            text(
                bokmal { +"Pensjonsopptjeningen din" },
                nynorsk { +"Pensjonsoppteninga di" },
                english { +"Your pension accrual" },
            )
        }
        paragraph {
            text(
                bokmal { +"Tabellen under viser den pensjonsgivende inntekten din og om du har andre typer pensjonsopptjening registrert på deg i 2024. Det er egne regler for pensjonsopptjening ved omsorgsarbeid, dagpenger ved arbeidsledighet eller uføretrygd." },
                nynorsk { +"Tabellen under viser den pensjonsgivande inntekta di og om du har andre typar pensjonsopptening registrert på deg i 2024. Det er egne reglar for pensjonsopptening ved omsorgsarbeid, dagpengar ved arbeidsløyse eller uføretrygd." },
                english { +"The table below shows your pensionable income, and you will be able to see your other types of pension accruals we have registered for 2024. There are special rules for pension accrual in connection with care work, unemployment benefit or disability benefit. " },
            )
        }

        showIf(pensjonsopptjeningKap20VedVirk.harDagpenger) {
            paragraph {
                text(
                    bokmal { +"For perioder du har hatt dagpenger er det egne regler for pensjonsopptjening." },
                    nynorsk { +"For periodar du har hatt dagpengar er det egne reglar for pensjonsopptening." },
                    english { +"For periods when you have had unemployment benefit, there are separate rules for pension accrual." },
                )
            }
        }

        showIf(pensjonsopptjeningKap20VedVirk.harUforetrygd) {
            paragraph {
                text(
                    bokmal { +"For perioder du har hatt uføretrygd er det egne regler for pensjonsopptjening." },
                    nynorsk { +"For periodar du har hatt uføretrygd er det egne reglar for pensjonsopptening." },
                    english { +"For periods when you have received disability benefit, there are separate rules for pension accrual." },
                )
            }
        }

        paragraph {
            text(
                bokmal { +"På $DIN_PENSJON_URL får du oversikt over pensjonsopptjeningen din for hvert enkelt år. Nav mottar opplysninger om pensjonsgivende inntekt fra Skatteetaten. Ta kontakt med Skatteetaten hvis du mener at inntektene i tabellen er feil." },
                nynorsk { +"På $DIN_PENSJON_URL får du oversikt over pensjonsoppteninga di for kvart enkelt år. Nav får opplysningar om pensjonsgivande inntekt frå Skatteetaten. Ta kontakt med Skatteetaten dersom du meiner at inntektene i tabellen er feil." },
                english { +"$DIN_PENSJON_URL provides details on your accumulated rights for each year. Nav receives information about pensionable income from the Norwegian Tax Administration. Contact the tax authorities if you think that this income is wrong." },
            )
        }

        showIf(pensjonsopptjeningKap20VedVirk.harMerknadType) {
            pensjonsgivendeInntekt(true)
        }.orShow {
            pensjonsgivendeInntekt(false)
        }
    }

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto>.pensjonsgivendeInntekt(
    medMerknader: Boolean
) {
    paragraph {
        table({
            column(columnSpan = 1, alignment = ColumnAlignment.RIGHT) {
                text(
                    bokmal { +"År" },
                    nynorsk { +"År" },
                    english { +"Year" },
                )
            }
            column(columnSpan = 2, alignment = ColumnAlignment.RIGHT) {
                text(
                    bokmal { +"Pensjonsgivende inntekt (kr)" },
                    nynorsk { +"Pensjonsgivande inntekt (kr)" },
                    english { +"Pensionable income (NOK)" },
                )
            }
            column(columnSpan = 2, alignment = ColumnAlignment.RIGHT) {
                text(
                    bokmal { +"Gjennomsnittlig G (kr)" },
                    nynorsk { +"Gjennomsnittlig G (kr)" },
                    english { +"Average G (NOK)" },
                )
            }
            if (medMerknader) {
                column(columnSpan = 4) {
                    text(
                        bokmal { +"Andre typer opptjeningsgrunnlag registrert" },
                        nynorsk { +"Andre typer oppteningsgrunnlag registrert" },
                        english { +"Other types of accruals registered" },
                    )
                }
            }
        }) {
            forEach(pensjonsopptjeningKap20VedVirk.pensjonsopptjeninger) {
                row {
                    cell { eval(it.aarstall.format()) }
                    cell { includePhrase(KronerText(it.pensjonsgivendeinntekt)) }
                    cell { includePhrase(KronerText(it.gjennomsnittligG)) }
                    if (medMerknader) {
                        cell {
                            forEach(it.merknader) { merknad ->
                                showIf(merknad.equalTo(Merknad.DAGPENGER)) {
                                    text(
                                        bokmal { +"Dagpenger " },
                                        nynorsk { +"Dagpenger " },
                                        english { +"Unemployment benefit " },
                                    )
                                }.orShowIf(merknad.equalTo(Merknad.OMSORGSOPPTJENING)) {
                                    text(
                                        bokmal { +"Omsorgsopptjening " },
                                        nynorsk { +"Omsorgsopptjening " },
                                        english { +"Care work accrual " },
                                    )
                                }.orShowIf(merknad.equalTo(Merknad.UFORETRYGD)) {
                                    text(
                                        bokmal { +"Uføretrygd " },
                                        nynorsk { +"Uføretrygd " },
                                        english { +"Disability benefit " },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
