package no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.beholdningForForsteUttak
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.nettoUtbetaltPerManed
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harDagpenger
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harMerknadType
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harOmsorgsopptjeningFOM2010
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harOmsorgsopptjeningTOM2009
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harUforepensjon
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harUforepensjonKonvertertTilUforetrygd
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.harUforetrygd
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningKap20VedVirkSelectors.pensjonsopptjeninger
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.aarstall
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.gjennomsnittligG
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.merknader
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.PensjonsopptjeningSelectors.pensjonsgivendeinntekt
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.VedtakSelectors.sisteOpptjeningsAr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.VilkaarsVedtakSelectors.avslattGarantipensjon
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.epsVedVirk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.erBeregnetSomEnsligPgaInstitusjonsopphold
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.garantipensjonVedVirk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.pensjonsopptjeningKap20VedVirk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.trygdetidNorge
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.vedtak
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.vilkarsVedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningenAlderAP2025 =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderAP2025Dto>(
        //vedleggBeregnAP2025Tittel_001
        title = {
            text(
                bokmal { +"Slik har vi beregnet pensjonen din" },
                nynorsk { +"Slik har vi berekna pensjonen din" },
                english { +"This is how we have calculated your pension" },
            )
        },
        includeSakspart = false
    ) {
        //vedleggBeregnInnlednAP2025_001
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
        //vedleggBeregnUttaksgrad_001
        paragraph {
            text(
                bokmal { +"Uttaksgraden for alderspensjonen din er " + alderspensjonVedVirk.uttaksgrad.format() + " prosent." },
                nynorsk { +"Uttaksgraden for alderspensjonen din er " + alderspensjonVedVirk.uttaksgrad.format() + " prosent." },
                english { +"The rate of your retirement pension is " + alderspensjonVedVirk.uttaksgrad.format() + " percent." },
            )
        }
        includePhrase(
            OpplysningerBruktIBeregningTabellAP2025(
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
        showIf(
            not(garantipensjonInnvilget)
                    and (not(redusertTrygdetid) or avslattGarantipensjon)
        ) {
            //vedleggBeregnPensjonsBeholdning_001
            title1 {
                text(
                    bokmal { +"Pensjonsbeholdning" },
                    nynorsk { +"Pensjonsbehaldning" },
                    english { +"Accumulated pension capital" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Pensjonsbeholdningen ved uttaket er summen av all pensjonsopptjening fra tidligere år. Du kan tjene opp inntektspensjon fra og med det året du fyller 17. Den årlige pensjonsopptjeningen din er 18,1 prosent av samlet opptjeningsgrunnlag opp til 7,1 av folketrygdens gjennomsnittlige grunnbeløp (G) for dette året. Pensjonsbeholdningen er regulert hvert år fram til du begynner å ta ut alderspensjon." },
                    nynorsk { +"Pensjonsbehaldninga ved uttaket er summen av all pensjonsopptening frå tidlegare år. Du kan tene opp inntektspensjon frå og med det året du fyller 17. Den årlege pensjonsoppteninga di er 18,1 prosent av samla opptjeningsgrunnlag opp til 7,1 av folketrygdens gjennomsnittlege grunnbeløp (G). Pensjonsbehaldninga er regulert kvart år med lønnsveksten fram til du byrjar å ta ut alderspensjon." },
                    english { +"The accumulated pension capital is the sum of all pension accruals from previous years. You can earn income pension starting from the year you turn 17. Your annual pension accrual is 18.1 percent of the total earnings basis up to 7.1 times the National Insurance average basic amount (G) for that year. The pension balance is adjusted annually until you begin to receive retirement pension." },
                )
            }
        }

        showIf(
            (garantipensjonInnvilget and garantipensjonVedVirk.safe { nettoUtbetaltPerManed }.ifNull(Kroner(0))
                .greaterThan(0))
                    or (redusertTrygdetid and not(avslattGarantipensjon))
        ) {
            //vedleggBeregnPensjonsbeholdningOgTrygdetid_001
            title1 {
                text(
                    bokmal { +"Pensjonsbeholdning og trygdetid" },
                    nynorsk { +"Pensjonsbehaldning og trygdetid" },
                    english { +"Accumulated pension capital and Norwegian national insurance coverage" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Pensjonsbeholdningen ved uttaket er summen av all pensjonsopptjening fra tidligere år. Du kan tjene opp inntektspensjon fra og med det året du fyller 17. Den årlige pensjonsopptjeningen din er 18,1 prosent av samlet opptjeningsgrunnlag opp til 7,1 av folketrygdens gjennomsnittlige grunnbeløp (G) for dette året. Pensjonsbeholdningen er regulert hvert år fram til du begynner å ta ut alderspensjon." },
                    nynorsk { +"Pensjonsbehaldninga ved uttaket er summen av all pensjonsopptening frå tidlegare år. Du kan tene opp inntektspensjon frå og med det året du fyller 17. Den årlege pensjonsoppteninga di utgjer 18,1 prosent av samla opptjeningsgrunnlag opp til 7,1 av det gjennomsnittleg grunnbeløpet i folketrygda for dette året. Pensjonsbehaldninga er regulert kvart år fram til du byrjar å ta ut alderspensjon." },
                    english { +"The accumulated pension capital is the sum of all pension accruals from previous years. You can earn income pension starting from the year you turn 17. Your annual pension accrual amounts to 18.1 percent of the total earnings basis up to 7.1 times the average National Insurance basic amount (G). The accumulated pension capital is adjusted annually until you begin to receive retirement pension." },
                )
            }

            showIf(redusertTrygdetid or garantipensjonInnvilget) {
                //vedleggBeregnTrygdetidKap20_001
                paragraph {
                    text(
                        bokmal { +"Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge fra fylte 16 år til og med det året du fyller 66 år. Trygdetid har betydning for beregning av garantipensjon. Full trygdetid er 40 år." },
                        nynorsk { +"Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg frå fylte 16 år til og med det året du fyller 66 år. Trygdetid har betydning for berekninga av garantipensjon. Full trygdetid er 40 år." },
                        english { +"National insurance coverage refers to the periods during which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these periods include the time you have lived or worked in Norway from the age of 16 until the year you turn 66. The period of national insurance coverage is relevant for calculating the guaranteed pension, full coverage requires 40 years." },
                    )
                }
            }
        }

        showIf(vedtak.sisteOpptjeningsAr.lessThan(2023)) {
            //forklaringSisteOpptjeningsaar_001
            paragraph {
                text(
                    bokmal { +"Pensjonsopptjening" },
                    nynorsk { +"Pensjonsopptening" },
                    english { +"Your pension accruals" },
                )
                showIf(
                    (garantipensjonInnvilget
                            and garantipensjonVedVirk.safe { nettoUtbetaltPerManed }.ifNull(Kroner(0)).greaterThan(0))
                            or (not(garantipensjonInnvilget) and redusertTrygdetid)
                ) {
                    text(
                        bokmal { +" og trygdetid" },
                        nynorsk { +" og trygdetid" },
                        english { +" and your national insurance coverage" },
                    )
                }
                text(
                    bokmal {
                        +" tas med i beregningen av alderspensjon fra og med året etter at skatteoppgjøret er klart. Dette gjelder selv om skatteoppgjøret ditt er klart tidligere. I beregningen er det derfor brukt pensjonsopptjening til og med "
                        +vedtak.sisteOpptjeningsAr.format() + "."
                    },
                    nynorsk {
                        +" blir teke med i berekninga av alderspensjon frå og med året etter at skatteoppgjeret er klart. Dette gjeld sjølv om skatteoppgjeret ditt er klart tidlegare. I berekninga er det difor brukt pensjonsopptening til og med "
                        +vedtak.sisteOpptjeningsAr.format() + "."
                    },
                    english {
                        +" are taken into account when calculating retirement pension starting from the year after your tax assessment is finalised. This applies even if your tax assessment is completed earlier. In the calculation, pension accruals are used up to and including "
                        +vedtak.sisteOpptjeningsAr.format() + "."
                    },
                )
            }
            //forklaringSisteOpptjeningsaar_001
            paragraph {
                text(
                    bokmal { +"Du vil få et nytt vedtak fra oss når pensjonen din blir omregnet." },
                    nynorsk { +"Du vil få eit nytt vedtak frå oss når pensjonen din blir omrekna." },
                    english { +"You will receive a new decision from us when your pension is calculated." },
                )
            }
        }
//vedleggBeregnDelingstall_001
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
//vedleggBeregnInntektspensjonOverskrift_001
        title1 {
            text(
                bokmal { +"Inntektspensjon" },
                nynorsk { +"Inntektspensjon" },
                english { +"Income pension" },
            )
        }
        showIf(alderspensjonVedVirk.uttaksgrad.equalTo(100)) {
            paragraph {
                text(
                    bokmal {
                        +"Din årlige inntektspensjon blir beregnet ved å dele pensjonsbeholdningen din på delingstallet ved uttak. Pensjonsbeholdningen din er på " +
                                beregningKap20VedVirk.beholdningForForsteUttak.format() + ", og delingstallet ved uttak er "
                        +beregningKap20VedVirk.delingstallLevealder.format() + "."
                    },

                    nynorsk {
                        +"Den årlege inntektspensjonen din blir rekna ut ved å dele pensjonsbehaldninga di på delingstalet ved uttak. Pensjonsbehaldninga di er på " +
                                beregningKap20VedVirk.beholdningForForsteUttak.format() + ", og delingstalet ved uttak er "
                        +beregningKap20VedVirk.delingstallLevealder.format() + "."
                    },

                    english {
                        +"Your annual income pension is calculated by dividing your pension capital by the life expectancy adjustment divisor at the time of the withdrawal. Your accumulated pension capital is " +
                                beregningKap20VedVirk.beholdningForForsteUttak.format() + ", and the divisor at the time of withdrawal is "
                        +beregningKap20VedVirk.delingstallLevealder.format() + "."
                    },
                )
            }
        }.orShow {
            //vedleggBeregnInntektspensjonGradertUttak_001
            paragraph {
                text(
                    bokmal {
                        +"Din årlige inntektspensjon blir beregnet ved å dele pensjonsbeholdningen din på delingstallet ved uttak. Pensjonsbeholdningen din er på " +
                                beregningKap20VedVirk.beholdningForForsteUttak.format() + ", og delingstallet ved uttak er " +
                                beregningKap20VedVirk.delingstallLevealder.format() + ". Siden du ikke tar ut full pensjon, vil du kun få utbetalt "
                        +alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet."
                    },

                    nynorsk {
                        +"Den årlege inntektspensjonen din blir rekna ut ved å dele pensjonsbehaldninga di på delingstalet ved uttak. Pensjonsbehaldninga di er på " +
                                beregningKap20VedVirk.beholdningForForsteUttak.format() + ", og delingstalet ved uttak er " +
                                beregningKap20VedVirk.delingstallLevealder.format() + ".  Sidan du ikkje tek ut full pensjon, vil du berre få utbetalt "
                        +alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet."
                    },

                    english {
                        +"Your annual income pension is calculated by dividing your accumulated pension capital by the life expectancy adjustment divisor at the time of the initial withdrawal. Your accumulated pension capital is " +
                                beregningKap20VedVirk.beholdningForForsteUttak.format() + ", and the divisor at the time of withdrawal is " +
                                beregningKap20VedVirk.delingstallLevealder.format() + ". Since you are not withdrawing the full pension, you will only receive "
                        +alderspensjonVedVirk.uttaksgrad.format() + " percent of this amount."
                    },
                )
            }
        }
        showIf(
            garantipensjonVedVirk.safe { nettoUtbetaltPerManed }.equalTo(0)
                    and not(avslattGarantipensjon)
                    and redusertTrygdetid
        ) {
            //vedleggBeregnGarantipensjonIkkeUtbetales_001
            title1 {
                text(
                    bokmal { +"Garantipensjon" },
                    nynorsk { +"Garantipensjon" },
                    english { +"Guaranteed pension" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Inntektspensjonen din er over grensen for at du kan få utbetalt garantipensjon." },
                    nynorsk { +"Inntektspensjonen din er over grensa for at du kan få utbetalt garantipensjon." },
                    english { +"You are not eligible for a guaranteed pension because your income pension exceeds the threshold." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Garantipensjonen skal sikre et visst minimum på pensjonen. Størrelsen på garantipensjonen fastsettes ut fra en sats som er avhengig av din sivilstand. Satsen blir redusert når trygdetiden din er under 40 år." },
                    nynorsk { +"Garantipensjonen skal sikre eit visst minimum på pensjonen. Størrelsen på garantipensjonen fastsettast ut frå ein sats som er avhengig av din sivilstand. Satsen blir redusert når trygdetida di er under 40 år." },
                    english { +"The guaranteed pension ensures a minimum level for the pension. The size of the guaranteed pension is based on a rate that depends on your marital status. The rate is reduced if your national insurance coverage is less than 40 years." },
                )
            }
        }


//vedleggBeregnGarantipensjonOverskrift_001
        includePhrase(
            OpplysningerBruktIBeregningenGarantipensjon(
                garantipensjonVedVirk = garantipensjonVedVirk,
                alderspensjonVedVirk = alderspensjonVedVirk,
                vilkarsVedtak = vilkarsVedtak,
                beregningKap20VedVirk = beregningKap20VedVirk,
                beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk,
                epsVedVirk = epsVedVirk,
                erBeregnetSomEnsligPgaInstitusjonsopphold = erBeregnetSomEnsligPgaInstitusjonsopphold,
                trygdetidsdetaljerKap20VedVirk = trygdetidsdetaljerKap20VedVirk,
            )
        )

        showIf(
            beregningKap20VedVirk.redusertTrygdetid and not(avslattGarantipensjon) and trygdetidNorge.size()
                .greaterThan(0)
        ) {
            includePhrase(Vedtak.TrygdetidOverskrift)
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetidInnledning)
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdetidNorge))
        }
//vedleggBeregnPensjonsOpptjeningKap20_001
        title1 {
            text(
                bokmal { +"Pensjonsopptjeningen din" },
                nynorsk { +"Pensjonsoppteninga di" },
                english { +"Your pension accrual" },
            )
        }
        paragraph {
            text(
                bokmal { +"Tabellen under viser den pensjonsgivende inntekten din og om du har andre typer pensjonsopptjening registrert på deg i det enkelte år. Det er egne regler for pensjonsopptjening ved omsorgsarbeid, dagpenger ved arbeidsledighet eller uføretrygd." },
                nynorsk { +"Tabellen under viser den pensjonsgivande inntekta di og om du har andre typar pensjonsopptening registrert på deg i det enkelte år. Det er egne reglar for pensjonsopptening ved omsorgsarbeid, dagpengar ved arbeidsløyse eller uføretrygd." },
                english { +"The table below shows your pensionable income, and you will be able to see your other types of pension accruals we have registered for each year. There are special rules for pension accrual in connection with care work, unemployment benefit or disability benefit. " },
            )
        }

        showIf(pensjonsopptjeningKap20VedVirk.harOmsorgsopptjeningFOM2010) {
            //vedleggBeregnAP2025Omsorgsopptjening_001
            paragraph {
                text(
                    bokmal { +"Opptjening fra omsorgsarbeid garanterer at du får pensjonsopptjening som tilsvarer inntekt inntil 4,5 ganger gjennomsnittlig G. Hvis pensjonsgivende inntekt eller annen opptjening er lavere i det aktuelle året, er du sikret denne opptjeningen." },
                    nynorsk { +"Opptening frå omsorgsarbeid garanterer at du får pensjonsopptening som utgjer ei inntekt inntil 4,5 gongar gjennomsnittleg G. Dersom pensjonsgivande inntekt eller anna opptening er lågare i det aktuelle året, er du sikra denne oppteninga." },
                    english { +"Accrual from care work guarantees that you will receive pension accrual corresponding to income up to 4.5 times the average G. If pensionable income or other pension accrual is lower in the year in question, you are guaranteed this pension accrual from care work." },
                )
                showIf(pensjonsopptjeningKap20VedVirk.harOmsorgsopptjeningTOM2009) {
                    text(
                        bokmal { +" Fram til 2010 var garantien 4 ganger gjennomsnittlig G." },
                        nynorsk { +" Fram til 2010 var garantien 4 gongar gjennomsnittleg G." },
                        english { +" Prior to 2010, the pension accrual from care work was 4 times the average G." },
                    )
                }
            }
        }.orShowIf(pensjonsopptjeningKap20VedVirk.harOmsorgsopptjeningTOM2009) {
            //vedleggBeregnAP2025OmsorgsopptjeningFor2010_001
            paragraph {
                text(
                    bokmal { +"Opptjening fra omsorgsarbeid garanterer at du får pensjonsopptjening som tilsvarer inntekt inntil 4 ganger gjennomsnittlig G. Hvis pensjonsgivende inntekt eller annen opptjening er lavere i det aktuelle året, er du sikret denne opptjeningen." },
                    nynorsk { +"Opptening frå omsorgsarbeid garanterer at du får pensjonsopptening som utgjer ei inntekt inntil 4 gongar gjennomsnittleg G. Dersom pensjonsgivande inntekt eller anna opptening er lågare i det aktuelle året, er du sikra denne oppteninga." },
                    english { +"Accrual from care work guarantees that you will receive pension accrual corresponding to income up to 4 times the average G. If pensionable income or other pension accrual is lower in the year in question, you are guaranteed this pension accrual from care work." },
                )
            }
        }

        showIf(pensjonsopptjeningKap20VedVirk.harDagpenger) {
            paragraph {
                text(
                    bokmal { +"For perioder du har hatt dagpenger er det egne regler for pensjonsopptjening. Reglene gjelder fra og med 2010." },
                    nynorsk { +"For periodar du har hatt dagpengar er det egne reglar for pensjonsopptening. Reglane gjelder frå og med 2010." },
                    english { +"For periods when you have had unemployment benefit, there are separate rules for pension accrual. The rules apply from 2010 onwards." },
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

        showIf(pensjonsopptjeningKap20VedVirk.harUforepensjonKonvertertTilUforetrygd) {
            paragraph {
                text(
                    bokmal { +"For perioder du har hatt uførepensjon og uføretrygd er det egne regler for pensjonsopptjening." },
                    nynorsk { +"For periodar du har hatt uførepensjon eller uføretrygd er det egne reglar for pensjonsopptening." },
                    english { +"For periods when you have received disability pension and disability benefit, there are separate rules for pension accrual." },
                )
            }
        }

        showIf(pensjonsopptjeningKap20VedVirk.harUforepensjon) {
            paragraph {
                text(
                    bokmal { +"For perioder du har hatt uførepensjon er det egne regler for pensjonsopptjening." },
                    nynorsk { +"For periodar du har hatt uførepensjon er det egne reglar for pensjonsopptening." },
                    english { +"For periods when you have received disability pension, there are separate rules for pension accrual. " },
                )
            }
        }

        paragraph {
            text(
                bokmal { +"I nettjenesten Din pensjon på ${Constants.DIN_PENSJON_URL} får du oversikt over pensjonsopptjeningen din for hvert enkelt år. Nav mottar opplysninger om pensjonsgivende inntekt fra Skatteetaten. Ta kontakt med Skatteetaten hvis du mener at inntektene i tabellen er feil." },
                nynorsk { +"I nettenesta Din pensjon på ${Constants.DIN_PENSJON_URL} får du oversikt over pensjonsoppteninga di for kvart enkelt år. Nav får opplysningar om pensjonsgivande inntekt frå Skatteetaten. Ta kontakt med Skatteetaten dersom du meiner at inntektene i tabellen er feil." },
                english { +"Our online service " + quoted("Din pensjon") + " at ${Constants.DIN_PENSJON_URL} provides details on your accumulated rights for each year. Nav receives information about pensionable income from the Norwegian Tax Administration. Contact the tax authorities if you think that this income is wrong." },
            )
        }
        showIf(pensjonsopptjeningKap20VedVirk.harMerknadType) {
            pensjonsgivendeInntekt(true)
        }.orShow {
            pensjonsgivendeInntekt(false)
        }
    }

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderAP2025Dto>.pensjonsgivendeInntekt(
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
                                showIf(merknad.equalTo(OpplysningerBruktIBeregningenAlderAP2025Dto.Pensjonsopptjening.Merknad.DAGPENGER)) {
                                    text(
                                        bokmal { +"Dagpenger " },
                                        nynorsk { +"Dagpenger " },
                                        english { +"Unemployment benefit " },
                                    )
                                }.orShowIf(merknad.equalTo(OpplysningerBruktIBeregningenAlderAP2025Dto.Pensjonsopptjening.Merknad.OMSORGSOPPTJENING)) {
                                    text(
                                        bokmal { +"Omsorgsopptjening " },
                                        nynorsk { +"Omsorgsopptjening " },
                                        english { +"Care work accrual " },
                                    )
                                }
                                    .orShowIf(merknad.equalTo(OpplysningerBruktIBeregningenAlderAP2025Dto.Pensjonsopptjening.Merknad.UFOREPENSJON)) {
                                        text(
                                            bokmal { +"Uførepensjon " },
                                            nynorsk { +"Uførepensjon " },
                                            english { +"Disability pension " },
                                        )
                                    }
                                    .orShowIf(merknad.equalTo(OpplysningerBruktIBeregningenAlderAP2025Dto.Pensjonsopptjening.Merknad.UFORETRYGD)) {
                                        text(
                                            bokmal { +"Uføretrygd " },
                                            nynorsk { +"Uføretrygd " },
                                            english { +"Disability benefit " },
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
