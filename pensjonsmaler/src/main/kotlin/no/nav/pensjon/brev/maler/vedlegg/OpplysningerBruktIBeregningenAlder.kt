package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.Beregningsmetode.FOLKETRYGD
import no.nav.pensjon.brev.api.model.PoengTallsType
import no.nav.pensjon.brev.api.model.PoengTallsType.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap19_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap20_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.PensjonspoengSelectors.arstall
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.PensjonspoengSelectors.bruktIBeregningen
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.PensjonspoengSelectors.grunnbelopVeiet
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.PensjonspoengSelectors.pensjonsgivendeinntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.PensjonspoengSelectors.pensjonspoeng
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.PensjonspoengSelectors.poengtallstype
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.inneholderOmsorgspoeng_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.pensjonspoeng
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.PoengrekkeVedVirkSelectors.pensjonspoeng_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregnetSomEnsligPgaInstitusjon
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.krav
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.poengrekkeVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.skalSkjuleTrygdetidstabellerPgaAldersovergang
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidNorge
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.yrkesskadeDetaljerVedVirk
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningTabellKap19
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningTabellKap20
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenSivilstand
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.Kroner

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningenAlder =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderDto>(
        title = newText(
            Bokmal to "Opplysninger brukt i beregningen",
            Nynorsk to "Opplysningar brukte i berekninga",
            English to "Information about your calculation",
        ),
        includeSakspart = false,
        outline = {

            //vedleggBeregnInnledn_001
            paragraph {
                text(
                    Bokmal to "I dette vedlegget finner du opplysninger om deg og din pensjonsopptjening som vi har brukt i beregningen av pensjonen din. Hvis du mener at opplysningene er feil, må du melde fra til Nav, fordi det kan ha betydning for størrelsen på pensjonen din.",
                    Nynorsk to "I dette vedlegget finn du opplysningar om deg og pensjonsoppteninga di som vi har brukt i berekninga av pensjonen din. Dersom du meiner at opplysningane er feil, må du melde frå til Nav, fordi det kan ha noko å seie for storleiken på pensjonen din.",
                    English to "This appendix contains information about you and your accumulated pension rights, which we have used to calculate your pension. If you think the information is incorrect, you must notify Nav, as this may affect the size of your pension.",
                )
            }


            includePhrase(
                OpplysningerBruktIBeregningenSivilstand(
                    beregnetSomEnsligPgaInstitusjon = beregnetSomEnsligPgaInstitusjon,
                    epsVedVirk = epsVedVirk,
                    alderspensjonVedVirk = alderspensjonVedVirk,
                    beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk
                )
            )

            title1 {
                text(
                    Bokmal to "Opplysninger brukt i beregningen av pensjonen din",
                    Nynorsk to "Opplysningar brukte i berekninga av pensjonen din",
                    English to "Information used to calculate your pension",
                )
            }

            val regelverkstype = alderspensjonVedVirk.regelverkType
            val foedselsaar = bruker.foedselsdato.year
            showIf(regelverkstype.isOneOf(AP2016)) {
                val andelKap19 = alderspensjonVedVirk.andelKap19_safe.ifNull(0)
                val andelKap20 = alderspensjonVedVirk.andelKap20_safe.ifNull(0)

                //vedleggBelopAP2016Oversikt_001
                paragraph {
                    textExpr(
                        Bokmal to "De som er født i perioden 1954–1962 får en kombinasjon av alderspensjon etter gamle og nye regler i folketrygdloven (kapittel 19 og 20). Fordi du er født i ".expr() +
                                foedselsaar.format() + " får du beregnet " + andelKap19.format() + "/10 av pensjonen etter gamle regler, og "
                                + andelKap20.format() + "/10 etter nye regler.",

                        Nynorsk to "Dei som er fødde i perioden 1954–1962, får ein kombinasjon av alderspensjon etter gamle og nye reglar i folketrygdlova (kapittel 19 og 20). Fordi du er fødd i ".expr() +
                                foedselsaar.format() + ", får du rekna ut " + andelKap19.format() + "/10 av pensjonen etter gamle reglar, og "
                                + andelKap20.format() + "/10 etter nye reglar.",

                        English to "Individuals born between 1954 and 1962 will receive a combination of retirement pension calculated on the basis of both old and new provisions in the National Insurance Act (Chapters 19 and 20). Because you are born in ".expr() +
                                foedselsaar.format() + ", " + andelKap19.format() + "/10 of your pension is calculated on the basis of the old provisions, and "
                                + andelKap20.format() + "/10 is calculated on the basis of new provisions.",
                    )
                }
            }

            paragraph {
                textExpr(
                    Bokmal to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                    Nynorsk to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                    English to "The rate of your retirement pension is ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent.",
                )
            }

            showIf(regelverkstype.isOneOf(AP2016)) {
                paragraph {
                    text(
                        Bokmal to "For den delen av pensjonen din som er beregnet etter regler i kapittel 19 har vi brukt disse opplysningene i beregningen vår:",
                        Nynorsk to "For den delen av pensjonen din som er berekna etter reglar i kapittel 19, har vi brukt desse opplysningane i berekninga vår:",
                        English to "We have used the following information to calculate the part of your pension that comes under the provisions of Chapter 19:",
                    )
                }
            }

            includePhrase(
                OpplysningerBruktIBeregningTabellKap19(
                    trygdetidsdetaljerKap19VedVirk = trygdetidsdetaljerKap19VedVirk,
                    tilleggspensjonVedVirk = tilleggspensjonVedVirk,
                    beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk,
                    beregningKap19VedVirk = beregningKap19VedVirk,
                    beregningKap20VedVirk = beregningKap20VedVirk,
                    yrkesskadeDetaljerVedVirk = yrkesskadeDetaljerVedVirk,
                    alderspensjonVedVirk = alderspensjonVedVirk,
                )
            )

            showIf(regelverkstype.isOneOf(AP2016)) {
                includePhrase(
                    OpplysningerBruktIBeregningTabellKap20(
                        beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk,
                        beregningKap19VedVirk = beregningKap19VedVirk,
                        trygdetidsdetaljerKap20VedVirk = trygdetidsdetaljerKap20VedVirk,
                        beregningKap20VedVirk = beregningKap20VedVirk,
                        krav = krav
                    )
                )
            }

            includePhrase(
                OpplysningerBruktIBeregningenTrygdetidTabeller(
                    trygdetidsdetaljerKap19VedVirk = trygdetidsdetaljerKap19VedVirk,
                    trygdetidsdetaljerKap20VedVirk = trygdetidsdetaljerKap20VedVirk,
                    beregningKap19VedVirk = beregningKap19VedVirk,
                    beregningKap20VedVirk = beregningKap20VedVirk,
                    alderspensjonVedVirk = alderspensjonVedVirk,
                    inngangOgEksportVurdering = inngangOgEksportVurdering,
                    trygdetidNorge = trygdetidNorge,
                    trygdetidEOS = trygdetidEOS,
                    trygdetidAvtaleland = trygdetidAvtaleland,
                    skalSkjuleTrygdetidstabellerPgaAldersovergang = skalSkjuleTrygdetidstabellerPgaAldersovergang,
                )
            )

            showIf(
                regelverkstype.isOneOf(AP2011)
                        and trygdetidsdetaljerKap19VedVirk.beregningsmetode.equalTo(FOLKETRYGD)
                        and beregnetPensjonPerManedVedVirk.tilleggspensjon.isNull()
            ) {
                includePhrase(PensjonsopptjeningenDinTittel)
                paragraph {
                    text(
                        Bokmal to "Du er registrert med ingen eller for lav inntekt til å ha rett til tilleggspensjon. For å ha rett til tilleggspensjon må du ha minst tre år med pensjonspoeng. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp (G) eller omsorgspoeng.",
                        Nynorsk to "Du er registrert med inga eller for låg inntekt til å ha rett til tilleggspensjon. For å ha rett til tilleggspensjon må du ha minst tre år med pensjonspoeng. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda (G) eller omsorgspoeng.",
                        English to "You have been registered with no or too little income to be eligible for a supplementary pension. To be eligible for a supplementary pension, you must have earned pension points for at least three years. You earn pension points when your income is higher than the national insurance basic amount (G) or you can earn points for care work.",
                    )
                }
                includePhrase(PensjonsopptjeningenLesPaaNett)
            }

            showIf(
                (regelverkstype.isOneOf(AP2016) and poengrekkeVedVirk.pensjonspoeng_safe.ifNull(emptyList()).size()
                    .greaterThan(0))
                        or (regelverkstype.isOneOf(AP2011)
                        and beregnetPensjonPerManedVedVirk.tilleggspensjon.ifNull(Kroner(0)).greaterThan(0))
            ) {
                includePhrase(PensjonsopptjeningenDinTittel)
                includePhrase(PensjonsopptjeningenLesPaaNett)

                paragraph {
                    text(
                        Bokmal to "Tabellen under viser den pensjonsgivende inntekten din og pensjonspoengene dine. Det er bare inntekt for ferdiglignede år som vises i tabellen.",
                        Nynorsk to "Tabellen under viser den pensjonsgivande inntekta di og pensjonspoenga dine. Det er berre inntekt for ferdiglikna år som viser i tabellen.",
                        English to "The table below shows your pensionable income and your pension points. Only income from assessed years are shown in the table.",
                    )
                }

                showIf(regelverkstype.isOneOf(AP2016)) {
                    title1 {
                        text(
                            Bokmal to "Pensjonsopptjening etter kapittel 19 (gamle regler)",
                            Nynorsk to "Pensjonsopptening etter kapittel 19 (gamle reglar)",
                            English to "Accumulation of pension rights on the basis of the old provisions of Chapter 19)",
                        )
                    }

                    paragraph {
                        text(
                            Bokmal to "Den delen av pensjonen din som er beregnet etter kapittel 19, baserer seg på pensjonspoeng.",
                            Nynorsk to "Den delen av pensjonen din som er berekna etter kapittel 19, baserer seg på pensjonspoeng.",
                            English to "The part of your pension calculated on the basis of the old provisions in Chapter 19 is based on pension points.",
                        )
                    }
                }

                title1 {
                    text(
                        Bokmal to "Du kan tidligst tjene opp pensjonspoeng fra det året du fylte 17 år.",
                        Nynorsk to "Du kan tidlegast tene opp pensjonspoeng frå det året du fylte 17 år.",
                        English to "You can earn pension points at the earliest from the year you turned 17.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Tilleggspensjon beregnes ved hjelp av et sluttpoengtall og antall poengår. Sluttpoengtallet er beregnet som gjennomsnittet av de 20 beste pensjonspoengene dine, eller alle pensjonspoengene dine hvis det er færre enn 20. Disse er uthevet i tabellen. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp (G). De årene du har opptjent pensjonspoeng regnes som poengår.",
                        Nynorsk to "Tilleggspensjon blir berekna ved hjelp av eit sluttpoengtal og talet på poengår. Sluttpoengtalet er berekna som gjennomsnittet av dei 20 beste pensjonspoenga, eller alle pensjonspoenga dersom det er færre enn 20. Desse er utheva i tabellen. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda (G). Dei åra du har tent opp pensjonspoeng, blir rekna som poengår.",
                        English to "Supplementary pension is calculated using a final points total and the number of pension point earning years. The final points total has been calculated as the average of the 20 best pension point earning years, or all pension point earning years, if there are fewer than 20. These are highlighted in the table. You earn pension points for years when your income is higher than the national insurance basic amount (G). The years in which you have earned pension points are referred to as pension point earning years.",
                    )
                }

                showIf(poengrekkeVedVirk.inneholderOmsorgspoeng_safe.ifNull(false)) {
                    paragraph {
                        text(
                            Bokmal to "Omsorgspoengene dine vises bare hvis annen inntekt gir et lavere pensjonspoeng enn omsorgspoenget.",
                            Nynorsk to "Omsorgspoenga dine viser berre dersom anna inntekt gir eit lågare pensjonspoeng enn omsorgspoenget.",
                            English to "Your points for care work are only shown if other income earns fewer pension points than your points for care work.",
                        )
                    }
                }

                showIf(regelverkstype.isOneOf(AP2016)) {
                    title1 {
                        text(
                            Bokmal to "Pensjonsopptjening etter kapittel 20 (nye regler)",
                            Nynorsk to "Pensjonsopptening etter kapittel 20 (nye reglar)",
                            English to "Accumulation of pension rights on the basis of the new provisions of Chapter 20)",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Den delen av pensjonen din som er beregnet etter kapittel 20 baserer seg på pensjonsbeholdningen din. Pensjonsbeholdningen kan sammenlignes med en sparekonto der det for hvert år settes av en viss sum. Pensjonsbeholdningen ved uttaket utgjør summen av all pensjonsopptjening og reguleres hvert år med lønnsveksten fram til du begynner å ta ut alderspensjon.",
                            Nynorsk to "Den delen av pensjonen din som er berekna etter kapittel 20, baserer seg på pensjonsbehaldninga di. Pensjonsbehaldninga kan samanliknast med ein sparekonto der det for kvart år blir sett av ein viss sum. Pensjonsbehaldninga ved uttaket utgjer summen av all pensjonsopptening og blir kvart år regulert med lønnsveksten fram til du byrjar å ta ut alderspensjon.",
                            English to "The part of your pension which is calculated on the basis of the new provisions in Chapter 20 is based on your accumulated pension capital. Your accumulated pension capital is comparable to a savings account to which a certain amount is allocated each year. When you start withdrawing your pension, your accumulated pension capital is the total of all your accumulated rights and is adjusted annually according to wage growth until you start withdrawing your retirement pension.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du kan tjene opp pensjon fra og med det året du fyller 17. Den årlige pensjonsopptjeningen din utgjør 18,1 prosent av samlet opptjeningsgrunnlag opp til 7,1 G.",
                            Nynorsk to "Du kan tene opp pensjon frå og med det året du fyller 17. Den årlege pensjonsoppteninga di utgjer 18,1 prosent av samla oppteningsgrunnlag opp til 7,1 G.",
                            English to "You can start accumulating pension capital from and including the year you turn 17. Your annual pension rights are 18.1 percent of the total contribution basis up to 7.1 G.",
                        )
                    }
                }

                paragraph {
                    text(
                        Bokmal to "Nav mottar opplysninger om pensjonsgivende inntekt fra Skatteetaten. Ta kontakt med skattekontoret ditt hvis du mener at inntektene i tabellen er feil.",
                        Nynorsk to "Nav får opplysningar om pensjonsgivande inntekt frå Skatteetaten. Ta kontakt med skattekontoret ditt dersom du meiner at inntektene i tabellen er feil.",
                        English to "Nav receives information about pensionable income from the Norwegian Tax Administration. Contact your local tax office if you think that this income is wrong.",
                    )
                }

                title1 {
                    text(
                        Bokmal to "Pensjonspoengene dine",
                        Nynorsk to "Pensjonspoenga dine",
                        English to "Your pension points",
                    )
                }

                paragraph {
                    table({
                        column(alignment = RIGHT, columnSpan = 1) {
                            text(
                                Bokmal to "År",
                                Nynorsk to "År",
                                English to "Year"
                            )
                        }
                        column(alignment = RIGHT, columnSpan = 1) {
                            text(
                                Bokmal to "Pensjonsgivende inntekt (kr)",
                                Nynorsk to "Pensjonsgivande inntekt (kr)",
                                English to "Pensionable income (NOK)"
                            )
                        }
                        column(alignment = RIGHT, columnSpan = 1) {
                            text(
                                Bokmal to "Gj.snittlig G (kr)",
                                Nynorsk to "Gj.snittlig G (kr)",
                                English to "Average G (NOK)"
                            )
                        }
                        column(alignment = RIGHT, columnSpan = 1) {
                            text(
                                Bokmal to "Pensjonspoeng",
                                Nynorsk to "Pensjonspoeng",
                                English to "Pension points"
                            )
                        }
                        column(columnSpan = 2) { text(Bokmal to "Merknad", Nynorsk to "Merknad", English to "Notes") }
                    }) {
                        forEach(poengrekkeVedVirk.pensjonspoeng) {
                            val bruktIBeregningen = it.bruktIBeregningen
                            row {
                                cell {
                                    showIf(bruktIBeregningen) {
                                        eval(it.arstall.format(), BOLD)
                                    }.orShow {
                                        eval(it.arstall.format())
                                    }
                                }

                                cell {
                                    showIf(bruktIBeregningen) {
                                        eval(it.pensjonsgivendeinntekt.format(), BOLD)
                                    }.orShow {
                                        eval(it.pensjonsgivendeinntekt.format())
                                    }
                                }

                                cell {
                                    showIf(bruktIBeregningen) {
                                        eval(it.grunnbelopVeiet.format(), BOLD)
                                    }.orShow {
                                        eval(it.grunnbelopVeiet.format())
                                    }
                                }

                                cell {
                                    showIf(bruktIBeregningen) {
                                        eval(it.pensjonspoeng.format(), BOLD)
                                    }.orShow {
                                        eval(it.pensjonspoeng.format())
                                    }
                                }
                                cell {
                                    ifNotNull(it.poengtallstype) { poengtallstype ->
                                        showIf(bruktIBeregningen) {
                                            includePhrase(PoengTallsTypeMerknad(poengtallstype, BOLD))
                                        }.orShow {
                                            includePhrase(PoengTallsTypeMerknad(poengtallstype))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    )

private data class PoengTallsTypeMerknad(
    val poengTallsType: Expression<PoengTallsType>,
    val fontType: Element.OutlineContent.ParagraphContent.Text.FontType = PLAIN
) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(poengTallsType.isOneOf(G, H, J, K, L)) {
            text(
                Bokmal to "Omsorgspoeng er godskrevet",
                Nynorsk to "Omsorgspoeng er godskrevet",
                English to "Points for care work are credited",
                fontType
            )
        }.orShowIf(poengTallsType.isOneOf(FPP)) {
            text(
                Bokmal to "Framtidig pensjonspoeng",
                Nynorsk to "Framtidig pensjonspoeng",
                English to "Pension point earning year",
                fontType
            )
        }
    }
}

private object PensjonsopptjeningenLesPaaNett: OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "I nettjenesten Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsopptjeningen din for hvert enkelt år. Der vil du kunne se hvilke andre typer pensjonsopptjening som er registrert på deg.",
                Nynorsk to "I nettenesta Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsoppteninga di for kvart enkelt år. Der kan du sjå kva andre typar pensjonsopptening som er registrert på deg.",
                English to "Our online service «Din pensjon» at $DIN_PENSJON_URL provides details on your accumulated rights for each year. Here you will be able to see your other types of pension rights we have registered.",
            )
        }
    }
}

private object PensjonsopptjeningenDinTittel: OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Pensjonsopptjeningen din",
                Nynorsk to "Pensjonsoppteninga di",
                English to "Your accumulated pension capital",
            )
        }
    }

}