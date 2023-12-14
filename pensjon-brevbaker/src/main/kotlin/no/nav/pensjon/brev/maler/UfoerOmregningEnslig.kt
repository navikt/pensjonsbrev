package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.SivilstandAvdoed.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.AvdoedSelectors.ektefelletilleggOpphoert
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.AvdoedSelectors.harFellesBarnUtenBarnetillegg
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.AvdoedSelectors.navn
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.AvdoedSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.barnOverfoertTilSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.barnOverfoertTilSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.barnTidligereSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.barnTidligereSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.beloep
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.erRedusertMotInntekt
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.erRedusertMotInntekt_safe
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.fribeloepVedvirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BarnetilleggSaerkullsbarnVedvirkSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BrukerSelectors.borIAvtaleLand
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.BrukerSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.InntektFoerUfoerhetVedVirkSelectors.beloep
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.InntektFoerUfoerhetVedVirkSelectors.erMinsteinntekt
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.InntektFoerUfoerhetVedVirkSelectors.erSannsynligEndret
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.InntektFoerUfoerhetVedVirkSelectors.oppjustertBeloep
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.UfoeretrygdVedVirkSelectors.erInntektsavkortet
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.UfoeretrygdVedVirkSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.UfoeretrygdVedVirkSelectors.totalUfoereMaanedligBeloep
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.UfoeretrygdVedVirkSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.avdoed
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.barnetilleggSaerkullsbarnVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.harAvdoedRettigheterFoer2024
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.harBarnetillegg
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.inntektFoerUfoerhetVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.krav_virkningsDatoFraOgMed
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.minsteytelseVedvirk_sats
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.orienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.ufoeretrygdVedVirk
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

// 000073
@TemplateModelHelpers
object UfoerOmregningEnslig : AutobrevTemplate<UfoerOmregningEnsligDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_OMREGNING_ENSLIG_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = UfoerOmregningEnsligDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – omregning til enslig uføretrygdet (automatisk)",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = VEDTAKSBREV
        ),
    ) {
        val harMinsteytelseVedVirk = minsteytelseVedvirk_sats.notNull()

        title {
            text(
                Bokmal to "NAV har regnet om uføretrygden din",
                Nynorsk to "NAV har rekna om uføretrygda di",
                English to "NAV has altered your disability benefit",
            )
        }

        outline {
            val harBarnetilleggForSaerkullsbarnVedVirk = barnetilleggSaerkullsbarnVedVirk.notNull()
            val harBarnOverfoertTilSaerkullsbarn =
                barnetilleggSaerkullsbarnVedVirk.barnOverfoertTilSaerkullsbarn_safe.ifNull(emptyList()).isNotEmpty()
            val harbarnSomTidligerVarSaerkullsbarn =
                barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn_safe.ifNull(emptyList()).isNotEmpty()
            val harUfoereMaanedligBeloepVedvirk = ufoeretrygdVedVirk.totalUfoereMaanedligBeloep.greaterThan(0)

            includePhrase(Vedtak.Overskrift)

            includePhrase(
                OmregnEPSInnledning(
                    harMinsteytelseVedVirk = harMinsteytelseVedVirk,
                    inntektFoerUfoereErSannsynligEndret = inntektFoerUfoerhetVedVirk.erSannsynligEndret,
                    ektefelletilleggOpphoert = avdoed.ektefelletilleggOpphoert,
                    harBarnetilleggForSaerkullsbarnVedVirk = harBarnetilleggForSaerkullsbarnVedVirk,
                    harBarnOverfoertTilSaerkullsbarn = harBarnOverfoertTilSaerkullsbarn,
                    avdoedNavn = avdoed.navn,
                    kravVirkningsDatoFraOgMed = krav_virkningsDatoFraOgMed,
                )
            )

            includePhrase(
                UtbetalingUfoeretrygd(
                    totalUfoereMaanedligBeloep = ufoeretrygdVedVirk.totalUfoereMaanedligBeloep,
                    harBarnetilleggForSaerkullsbarnVedVirk = harBarnetilleggForSaerkullsbarnVedVirk,
                    institusjonsoppholdVedVirk = institusjonsoppholdVedVirk,
                    harFlereUfoeretrygdPerioder = beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak.greaterThan(1),
                )
            )

            showIf(harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret) {
                includePhrase(Vedtak.BegrunnelseOverskrift)
                ifNotNull(minsteytelseVedvirk_sats) { minsteytelseVedVirkSats ->
                    showIf(inntektFoerUfoerhetVedVirk.erSannsynligEndret) {
                        includePhrase(
                            EndringMinsteytelseOgMinstInntektFoerUfoerhetDoedEPS(
                                minsteytelseSats = minsteytelseVedVirkSats,
                                inntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.beloep,
                                oppjustertInntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                                kompensasjonsgradUfoeretrygd = ufoeretrygdVedVirk.kompensasjonsgrad,
                            )
                        )
                    } orShow {
                        includePhrase(
                            EndretMinsteytelseDoedEPS(
                                minsteytelseSatsVedvirk = minsteytelseVedVirkSats,
                                kompensasjonsgradUfoeretrygdVedvirk = ufoeretrygdVedVirk.kompensasjonsgrad
                            )
                        )
                    }
                } orShow {
                    includePhrase(
                        EndretMinstInntektFoerUfoerhetDoedEPS(
                            inntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.beloep,
                            oppjustertInntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.oppjustertBeloep,
                            kompensasjonsgradUfoeretrygd = ufoeretrygdVedVirk.kompensasjonsgrad,
                        )
                    )
                }
            }

            includePhrase(
                HjemmelSivilstandUfoeretrygd(
                    harMinsteinntektFoerUfoerhet = inntektFoerUfoerhetVedVirk.erMinsteinntekt,
                    ufoeretrygdErInntektsavkortet = ufoeretrygdVedVirk.erInntektsavkortet,
                )
            )

            showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.HELSE)) {
                includePhrase(HjemmelEPSDoedInstitusjonUfoeretrygd)
            }.orShowIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                includePhrase(HjemmelEPSDoedFengselUfoerUfoeretrygd)
            }

            showIf(avdoed.ektefelletilleggOpphoert) {
                includePhrase(OpphoerEktefelletilleggOverskrift)
                includePhrase(OpphoerEktefelletillegg)
                includePhrase(HjemmelEktefelletillegg)
            }

            ifNotNull(barnetilleggSaerkullsbarnVedVirk) { barnetilleggSaerkullsbarnVedVirk ->

                val harNettoBeloep = barnetilleggSaerkullsbarnVedVirk.beloep.greaterThan(0)
                val harJusteringsbeloepSaerkull = barnetilleggSaerkullsbarnVedVirk.justeringsbeloepAar.notEqualTo(0)

                showIf(harBarnOverfoertTilSaerkullsbarn) {
                    includePhrase(OmregningFellesbarnOverskrift)
                    includePhrase(InfoFellesbarnTilSaerkullsbarn(barnetilleggSaerkullsbarnVedVirk.barnOverfoertTilSaerkullsbarn))

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt
                                and harbarnSomTidligerVarSaerkullsbarn
                                and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret)
                                and not(harMinsteytelseVedVirk)
                    ) {
                        includePhrase(InfoTidligereSaerkullsbarn(barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn))
                    }

                    showIf(
                        harbarnSomTidligerVarSaerkullsbarn and (inntektFoerUfoerhetVedVirk.erSannsynligEndret or harMinsteytelseVedVirk)
                                and barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt
                    ) {
                        includePhrase(InfoTidligereSaerkullsbarnOgEndretUfoeretrygd(barnetilleggSaerkullsbarnVedVirk.barnTidligereSaerkullsbarn))
                    }
                }

                showIf(
                    harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert)
                ) {
                    includePhrase(EndringUfoeretrygdPaavirkerBarnetilleggOverskrift)
                }

                showIf(
                    harBarnOverfoertTilSaerkullsbarn or (harBarnetilleggForSaerkullsbarnVedVirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert))
                ) {

                    showIf(not(harBarnOverfoertTilSaerkullsbarn)) {
                        includePhrase(InfoBarnetilleggSaerkullsbarnInntekt)
                    }

                    showIf(harBarnOverfoertTilSaerkullsbarn) {
                        includePhrase(InfoBarnetilleggOverfortTilSaerkullsbarnInntekt)
                    }

                    showIf(
                        not(barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt)
                    ) {
                        includePhrase(
                            IkkeRedusertBarnetilleggSaerkullsbarnPgaInntekt(
                                barnetilleggSaerkullsbarnInntektBruktIAvkortning = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetilleggSaerkullsbarnFribeloep = barnetilleggSaerkullsbarnVedVirk.fribeloepVedvirk,
                            )
                        )
                    }

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and harNettoBeloep or (not(harNettoBeloep) and harJusteringsbeloepSaerkull)
                    ) {
                        includePhrase(
                            RedusertBarnetilleggSaerkullsbarnPgaInntekt(
                                barnetilleggSaerkullsbarnInntektBruktIAvkortning = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetilleggSaerkullsbarnFribeloep = barnetilleggSaerkullsbarnVedVirk.fribeloepVedvirk,
                            )
                        )
                    }

                    showIf(harJusteringsbeloepSaerkull and harNettoBeloep) {
                        includePhrase(JusterBeloepRedusertBarnetilleggPgaInntekt)
                    }

                    showIf(harJusteringsbeloepSaerkull and not(harNettoBeloep)) {
                        includePhrase(IkkeUtbetaltBarnetilleggPgaInntektOgJusteringsbelop)
                    }

                    showIf(
                        barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt and not(harNettoBeloep) and not(
                            harJusteringsbeloepSaerkull
                        )
                    ) {
                        includePhrase(
                            IkkeUtbetaltBarnetilleggSaerkullsbarnPgaInntekt(
                                barnetilleggSaerkullsbarnInntektBruktIAvkortning = barnetilleggSaerkullsbarnVedVirk.inntektBruktIAvkortning,
                                barnetilleggSaerkullsbarnInntektstak = barnetilleggSaerkullsbarnVedVirk.inntektstak,
                            )
                        )
                    }

                    showIf(barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt) {
                        includePhrase(HjemmelBarnetilleggRedusert)
                        includePhrase(MerInfoBarnetillegg)
                    } orShow {
                        includePhrase(HjemmelBarnetillegg)
                    }
                }
            }

            showIf(harAvdoedRettigheterFoer2024) {
                showIf(avdoed.sivilstand.isOneOf(SAMBOER3_2)) {
                    includePhrase(GjenlevenderettSamboerOverskrift(avdoed.navn))
                    includePhrase(GjenlevenderettUfoeretrygdSamboer)
                }
                showIf(avdoed.sivilstand.isOneOf(GIFT, PARTNER, SAMBOER1_5)) {
                    includePhrase(RettTilGjenlevendetilleggOverskrift)
                    includePhrase(HvemHarRettTilGjenlevendetilleggVilkaar)
                    includePhrase(HvordanSoekerDuOverskrift)
                    includePhrase(SoekGjenlevendetillegg)

                    showIf(bruker.borIAvtaleLand) {
                        includePhrase(SoekGjenlevendetilleggAvtaleland)
                    }
                }
            }.orShowIf(
                avdoed.sivilstand.isOneOf(GIFT, PARTNER, SAMBOER1_5)
                        and ufoeretrygdVedVirk.ufoeregrad.greaterThan(0)
                        and ufoeretrygdVedVirk.ufoeregrad.lessThan(0)
            ) {
                title1 {
                    text(
                        Bokmal to "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad.",
                        Nynorsk to "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad.",
                        English to "When you have graded disability benefit, you may be entitled to an adjustment allowance",
                        //English to "When you receive partial disability benefit, you may be entitled to an adjustment allowance.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Omstillingsstønad er en tidsbegrenset stønad som vanligvis varer i tre år. Omstillingsstønad er pensjonsgivende inntekt, og kan derfor påvirke hva du får utbetalt i uføretrygd.",
                        Nynorsk to "Omstillingsstønad er ein tidsavgrensa stønad som vanlegvis varar i tre år. Omstillingsstønad er pensjonsgivande inntekt, og kan derfor påverke kva du får utbetalt i uføretrygd.",
                        English to "The adjustment allowance is a time-limited benefit that normally only lasts three years. The adjustment allowance is pensionable income and can therefore affect what you receive in disability benefits.",
                    )
                }
                title1 {
                    text(
                        Bokmal to "Hvem kan ha rett til omstillingstønad?",
                        Nynorsk to "Kven kan ha rett til omstillingsstønad?",
                        English to "Who is entitled to an adjustment allowance?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "For å ha rett til omstillingsstønaden må du ved dødsfallet som hovedregel:",
                        Nynorsk to "For å ha rett til stønaden må du ved dødsfallet som hovudregel:",
                        English to "To be entitled to an adjustment allowance, you at the time of the death must as a rule:",
                    )
                    list {
                        item {
                            text(
                                Bokmal to "være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet",
                                Nynorsk to "vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet",
                                English to "be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme for the last five years prior to death",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ha vært gift med den avdøde i minst fem år, eller",
                                Nynorsk to "ha vore gift med den avdøde i minst fem år, eller",
                                English to "have been married to the deceased for at least five years, or",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ha vært gift eller samboer med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                                Nynorsk to "ha vore gift eller sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                                English to "have been married to or a cohabitant with the deceased, and have/had children together, or",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ha omsorg for barn minst halvparten av tiden.",
                                Nynorsk to "ha omsorg for barn minst halvparten av full tid.",
                                English to "care for a child at least half the time.",
                            )
                        }
                    }
                }
                title1 {
                    text(
                        Bokmal to "Hvor mye kan du få i omstillingsstønad?",
                        Nynorsk to "Hvor mykje kan du få?",
                        English to "How much are you entitled to?",
                    )
                }
                paragraph {
                    text(
                        // TODO G ved virk
                        Bokmal to "Stønaden er 2,25 ganger grunnbeløpet i folketrygden per år. Grunnbeløpet er <TODO>. Hvis den avdøde har bodd utenfor Norge etter fylte 16 år, kan det påvirke størrelsen.",
                        Nynorsk to "Stønaden er 2,25 gongar grunnbeløpet i folketrygda per år. Grunnbeløpet er <TODO> kronar. Viss den avdøde har budd utanfor Noreg etter fylte 16 år, kan det påverke stønaden.",
                        English to "The benefit is 2.25 times the National Insurance basic amount G, depending on the period of national insurance coverage for the person who died. The National Insurance basic amount is NOK <hent inn G ved virk>.",
                        // Her mener jeg at engelsk-versjonen er direkte feil når den snakker om "Hvis den avdøde har bodd utenfor Norge etter fylte 16 år"

                        //English to "The allowance is 2.25 times the basic amount in the National Insurance Scheme per year. The basic amount is 118,620 NOK. If the deceased lived outside Norway after the age of 16, it may affect the amount.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du har arbeidsinntekt ved siden av uføretrygden din, blir omstillingsstønaden redusert med 45 prosent av den inntekten som er over halve grunnbeløpet. " +
                                "Noen ytelser, som for eksempel sykepenger og dagpenger, likestilles med arbeidsinntekt.",

                        Nynorsk to "Viss du har arbeidsinntekt ved sida av uføretrygda di, blir stønaden redusert med 45 prosent av den inntekta som er over halve grunnbeløpet. " +
                                "Nokre ytingar, som til dømes sjukepengar og dagpengar, er likestilte med arbeidsinntekt.",

                        English to "The adjustment allowance will be reduced on the basis of income that you receive or expect to receive alongside your disability benefit. " +
                                "Your adjustment allowance will be reduced by 45 per cent of your income that exceeds half of the National Insurance basic amount. " +
                                "Some benefits, such as sickness benefits and unemployment benefits are equivalent to earned income.",
                        // or expect to recieve? Er det riktig?

                        // forslag:
                        //English to "If you have earned income in addition to your disability benefit, the adjustment allowance will be reduced by 45 percent of the income that exceeds half the National Insurance basic amount. " +
                                //"Some benefits, such as sickness benefits and unemployment benefits, are considered earned income.",
                    )
                }
                title1 {
                    text(
                        Bokmal to "Hvordan søker du?",
                        Nynorsk to "Korleis søkjer du?",
                        English to "How do you apply?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi oppfordrer deg til å søke om omstillingsstønaden så snart som mulig fordi vi vanligvis bare etterbetaler for tre måneder.",
                        Nynorsk to "Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader.",
                        English to "We encourage you to apply as soon as possible because we normally only pay retroactively for three months.",
                    )
                }
                paragraph {
                    text(
                        //TODO URL variabel
                        Bokmal to "Du finner mer informasjon og søknad på nav.no/omstillingsstonad.",
                        Nynorsk to "Du finn informasjon og søknad på nav.no/omstillingsstonad",
                        English to "You will find information and the application form at nav.no/omstillingsstonad",
                    )
                }
                showIf(bruker.borIAvtaleLand) {
                    paragraph {
                        text(
                            Bokmal to "Hvis du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde.",
                            Nynorsk to "Dersom du bur i utlandet, må du kontakte trygdemyndigheitene i bustadlandet ditt.",
                            English to "If you live outside Norway, you must contact the National Insurance authority in your country of residence.",
                        )
                    }
                }
            }


            // bla død før 2024...
            //
            showIf(avdoed.sivilstand.isOneOf(GIFT, PARTNER, SAMBOER1_5)) {
                includePhrase(AvdoedBoddArbeidetIUtlandOverskrift)
                includePhrase(AvdoedBoddEllerArbeidetIUtland(bruker.borIAvtaleLand))
                includePhrase(PensjonFraAndreOverskrift)
                includePhrase(InfoAvdoedPenFraAndre)
            }

            showIf(avdoed.harFellesBarnUtenBarnetillegg) {
                includePhrase(HarBarnUnder18Overskrift)
                includePhrase(HarBarnUtenBarnetillegg)
                includePhrase(HarBarnUnder18)
            }

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)

            showIf(
                harUfoereMaanedligBeloepVedvirk and (harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret or avdoed.ektefelletilleggOpphoert)
            ) {
                includePhrase(VirkningstidspunktUfoeretrygd(krav_virkningsDatoFraOgMed))
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret)
                        and not(avdoed.ektefelletilleggOpphoert)
                        and not(harBarnOverfoertTilSaerkullsbarn)
            ) {
                includePhrase(VirkningstidspunktUfoeretrygdIngenEndring(krav_virkningsDatoFraOgMed))
            }

            showIf(
                harUfoereMaanedligBeloepVedvirk
                        and not(harMinsteytelseVedVirk)
                        and not(inntektFoerUfoerhetVedVirk.erSannsynligEndret)
                        and not(avdoed.ektefelletilleggOpphoert)
                        and harBarnOverfoertTilSaerkullsbarn
            ) {
                includePhrase(VirkningstidspunktOmregningBarnetillegg(krav_virkningsDatoFraOgMed))
            }

            showIf(not(harUfoereMaanedligBeloepVedvirk)) {
                includePhrase(VirkningstidspunktUfoeretrygdAvkortetTil0(krav_virkningsDatoFraOgMed))
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)

            showIf(harBarnetillegg) {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektBarnetillegg)
            }.orShow {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(bruker.borINorge))
            includePhrase(Ufoeretrygd.HarDuSpoersmaalUfoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)


        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseMinsteytelse = true,
                skalViseBarnetillegg = false,
            ),
            opplysningerBruktIBeregningUT,
            barnetilleggSaerkullsbarnVedVirk.erRedusertMotInntekt_safe.ifNull(false) or harMinsteytelseVedVirk or inntektFoerUfoerhetVedVirk.erSannsynligEndret
        )

        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterOgPlikter)
    }
}