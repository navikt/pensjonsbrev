package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDtoSelectors.PesysDataSelectors.avdoedNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDtoSelectors.PesysDataSelectors.avdoedNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDtoSelectors.PesysDataSelectors.gjenlevendesAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InformasjonOmGjenlevenderettigheter : RedigerbarTemplate<InformasjonOmGjenlevenderettigheterDto> {
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.BARNEP, Sakstype.UFOREP, Sakstype.GJENLEV, Sakstype.ALDER)

    // 000069 / DOD_INFO_RETT_MAN
    override val kode = Pesysbrevkoder.Redigerbar.PE_INFORMASJON_OM_GJENLEVENDERETTIGHETER
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InformasjonOmGjenlevenderettigheterDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om gjenlevenderettigheter",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    ) {
        title {
            showIf(pesysData.sakstype.isOneOf(Sakstype.GJENLEV, Sakstype.UFOREP, Sakstype.ALDER)) {
                text(
                    Bokmal to "Informasjon om gjenlevenderettigheter",
                    Nynorsk to "Informasjon om attlevanderettar",
                    English to "Information about survivor's rights",
                )
            }.orShowIf(pesysData.sakstype.isOneOf(Sakstype.BARNEP)) {
                text(
                    Bokmal to "Informasjon om barnepensjon",
                    Nynorsk to "Informasjon om barnepensjon",
                    English to "Information about children's pension",
                )
            }
        }
        outline {
            showIf(pesysData.sakstype.isOneOf(Sakstype.GJENLEV, Sakstype.UFOREP, Sakstype.ALDER)) {
                paragraph {
                    text(
                        Bokmal to "Vi skriver til deg fordi vi har mottatt melding om at ",
                        Nynorsk to "Vi skriv til deg fordi vi har fått melding om at ",
                        English to "We are writing to you because we have received notice that "
                    )
                    ifNotNull(pesysData.avdoedNavn) {
                        eval(it)
                    }.orShow {
                        eval(fritekst("avdød navn"))
                    }
                    text(
                        Bokmal to " er død, og du kan ha rettigheter etter avdøde.",
                        Nynorsk to " er død, og du kan ha rettar etter avdøde.",
                        English to " has died ,and you may have rights as a surviving spouse."
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.BARNEP)) {
                paragraph {
                    val fritekst = fritekst("avdød navn")
                    textExpr(
                        Bokmal to "Vi skriver til deg fordi vi har mottatt melding om at ".expr() + fritekst + " er død, og du kan ha rett til barnepensjon.",
                        Nynorsk to "Vi skriv til deg fordi vi har fått melding om at ".expr() + fritekst + " er død, og du kan ha rett til barnepensjon.",
                        English to "We are writing to you because we have received notice that ".expr() + fritekst + " has died ,and we would like to inform you about children's pension."
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.ALDER, Sakstype.UFOREP)) {
                paragraph {
                    eval(fritekst("Opplysninger/forhold du vil informere bruker om i saken"))
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.GJENLEV, Sakstype.ALDER)) {
                title2 {
                    text(
                        Bokmal to "Hvem kan ha rett til ytelser etter avdøde?",
                        Nynorsk to "Kven kan ha rett til ytingar etter avdøde?",
                        English to "Who may be entitled to benefits as a surviving spouse?"
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.UFOREP)) {
                title2 {
                    text(
                        Bokmal to "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad",
                        Nynorsk to "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad",
                        English to "When you have partial disability benefit, you may be entitled to an adjustment allowance"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Omstillingsstønad er en tidsbegrenset stønad som vanligvis varer i tre år. Omstillingsstønad er pensjonsgivende inntekt, og kan derfor påvirke hva du får utbetalt i uføretrygd.",
                        Nynorsk to "Omstillingsstønad er ein tidsavgrensa stønad som vanlegvis varar i tre år. Omstillingsstønad er pensjonsgivande inntekt, og kan derfor påverke kva du får utbetalt i uføretrygd.",
                        English to "The adjustment allowance is a time-limited benefit that normally only lasts three years. The adjustment allowance is pensionable income and can therefore affect what you receive in disability benefits."
                    )
                }

                paragraph {
                    text(
                        Bokmal to "For å ha rett til stønaden må du ved dødsfallet som hovedregel:",
                        Nynorsk to "For å ha rett til stønaden må du ved dødsfallet som hovudregel:",
                        English to "To be entitled to an adjustment allowance, you at the time of the death must as a rule:"
                    )
                    list {
                        item {
                            text(
                                Bokmal to "være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet",
                                Nynorsk to "vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet",
                                English to "be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme for the last three years prior to death",
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
                                Bokmal to "ha omsorg for barn med minst halvparten av tiden.",
                                Nynorsk to "ha omsorg for barn minst halvparten av full tid.",
                                English to "care for a child at least half the time.",
                            )
                        }
                    }
                }

                paragraph {
                    text(
                        Bokmal to "Har du tidligere vært gift med avdøde, og ikke har giftet deg igjen, kan du ha rett til omstillingsstønad. Da må dere ha vært gift i minst 25 år eller i minst 15 år hvis dere hadde barn sammen.",
                        Nynorsk to "Har du tidlegare vore gift med avdøde og ikkje har gifta deg igjen, kan du ha rett til attlevandetillegg i uføretrygda. Då må de ha vore gifte i minst 25 år eller i minst 15 år dersom de hadde barn saman.",
                        English to "If you were divorced from the deceased, and have not remarried, you may be entitled to a survivor's supplement to disability benefit. You must then have been married for at least 25 years, or at least 15 years if you had children together."
                    )
                }
                paragraph {
                    eval(fritekst("fyll ut mer informasjon knyttet til forsørget av bidrag fra den avdøde"))
                }
                includePhrase(Felles.DuKanLeseMer)
            }

            showIf(pesysData.sakstype.isOneOf(Sakstype.ALDER)) {
                paragraph {
                    text(
                        Bokmal to "For å ha rett til gjenlevenderett i alderspensjonen, må du som hovedregel:",
                        Nynorsk to "For å ha rett til attlevanderett i alderspensjonen, må du som hovudregel:",
                        English to "To be entitled to a survivor's rights in a retirement pension, you must as a rule:",
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
                                Bokmal to "ha vært gift eller vært samboer med den avdøde og har eller ha hatt barn med den avdøde, eller",
                                Nynorsk to "ha vore gift eller vore sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                                English to "have been married to or a cohabitant with the deceased, and have/had children together, or",
                            )
                        }

                        item {
                            text(
                                Bokmal to "ha hatt omsorgen for den avdødes barn på dødsfallstidspunktet. Ekteskapet og omsorgen for barnet etter dødsfallet må til sammen ha vart minst fem år.",
                                Nynorsk to "ha hatt omsorga for barna til den avdøde på dødsfallstidspunktet. Ekteskapet og omsorga for barnet etter dødsfallet må til saman ha vart minst fem år.",
                                English to "have had care of the children of the deceased at the time of the death. The marriage and care of the child after the death must have lasted for at least five years.",
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        Bokmal to "Selv om du ikke har rett til ytelsen etter hovedreglene, kan du likevel ha rettigheter etter avdøde. Du kan lese mer om dette på ${Constants.NAV_URL}",
                        Nynorsk to "Sjølv om du ikkje har rett til ytinga etter hovudreglane, kan du likevel ha rettar etter avdøde. Du kan lese meir om dette på ${Constants.NAV_URL}.",
                        English to "Even if you are not entitled to benefits in accordance with the general rules, you may nevertheless have rights as a surviving spouse. You can read more about this at ${Constants.NAV_URL}",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Har du tidligere vært gift med avdøde, og ikke har giftet deg igjen, kan du ha rett til gjenlevenderett i alderspensjonen. Da må dere ha vært gift i minst 25 år eller i minst 15 år hvis dere hadde barn sammen. Dødsfallet må ha skjedd innen 5 år etter skilsmissen.",
                        Nynorsk to "Har du tidlegare vore gift med avdøde og ikkje har gifta deg igjen, kan du ha rett til attlevanderett i alderspensjonen. Då må de ha vore gifte i minst 25 år eller i minst 15 år dersom de hadde barn saman. Dødsfallet må ha skjedd innan 5 år etter skilsmissa.",
                        English to "If you were divorced from the deceased, and have not remarried, you may be entitled to a survivor's rights in a retirement pension. You must then have been married for at least 25 years, or at least 15 years if you had children together. The death must have occurred within 5 years after the divorce.",
                    )
                }
                includePhrase(Felles.DuKanLeseMer)
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.BARNEP)) {
                title2 {
                    text(
                        Bokmal to "Hvem kan ha rett til barnepensjon?",
                        Nynorsk to "Kven kan ha rett til barnepensjon?",
                        English to "Who may be entitled to a children’s pension?",
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                Bokmal to "For å ha rett til barnepensjon, må du som hovedregel være medlem i folketrygden og under 20 år.",
                                Nynorsk to "For å ha rett til barnepensjon må du som hovudregel vere medlem i folketrygda og under 20 år.",
                                English to "To be entitled to a children’s pension, you must as a rule have been a member of the National Insurance Scheme and under the age of 20.",
                            )
                        }
                        item {
                            text(
                                Bokmal to "Du kan ha rett til barnepensjon hvis du er under 20 år, foreldreløs og under utdanning.",
                                Nynorsk to "Du kan ha rett til barnepensjon dersom du er under 20 år, foreldrelaus og under utdanning.",
                                English to "You may be entitled to a children’s pension if you are under the age of 20, orphaned or in an education programme."
                            )
                        }
                        item {
                            text(
                                Bokmal to "Du kan ha rett til barnepensjon hvis du er under 21 år, har mistet en eller begge foreldrene, er under utdanning og forelderen døde av yrkesskade.",
                                Nynorsk to "Du kan ha rett til barnepensjon dersom du er under 21 år, har mista éin av eller begge foreldra, er under utdanning og forelderen har døydd av yrkesskade.",
                                English to "You may be entitled to a children's pension if you are under the age of 21, have lost one or both of your parents, are in an education programme or your parent died from an occupational injury."
                            )
                        }
                    }
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.UFOREP)) {
                title2 {
                    text(
                        Bokmal to "Hvor mye kan du få i omstillingsstønad?",
                        Nynorsk to "Kor mykje kan du få?",
                        English to "How much are you entitled to?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Stønaden er 2,25 ganger grunnbeløpet i folketrygden per år. Hvis den avdøde har bodd utenfor Norge etter fylte 16 år, kan det påvirke størrelsen.",
                        Nynorsk to "Stønaden er 2,25 gongar grunnbeløpet i folketrygda per år. Viss den avdøde har budd utanfor Noreg etter fylte 16 år, kan det påverke stønaden.",
                        English to "The allowance is 2.25 times the basic amount in the National Insurance Scheme per year. If the deceased lived outside Norway after the age of 16, it may affect the amount.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du har arbeidsinntekt ved siden av uføretrygden din, blir omstillingsstønaden redusert med 45 prosent av den inntekten som er over halve grunnbeløpet. Noen ytelser, som for eksempel sykepenger og dagpenger, likestilles med arbeidsinntekt.",
                        Nynorsk to "Viss du har arbeidsinntekt ved sida av uføretrygda di, blir stønaden redusert med 45 prosent av den inntekta som er over halve grunnbeløpet. Nokre ytingar, som til dømes sjukepengar og dagpengar, er likestilte med arbeidsinntekt.",
                        English to "If you have earned income in addition to your disability benefit, the adjustment allowance will be reduced by 45 percent of the income that exceeds half the National Insurance basic amount. Some benefits, such as sickness benefits and unemployment benefits, are considered earned income.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvordan søker du?",
                        Nynorsk to "Korleis søkjer du?",
                        English to "How do you apply?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi oppfordrer deg til å søke om omstillingsstønaden så snart som mulig fordi vi vanligvis bare etterbetaler for tre måneder. Du finner mer informasjon og søknad på ${Constants.OMSTILLINGSSTOENAD_URL}",
                        Nynorsk to "Vi oppmodar deg til å søkje om omstillingsstønaden så snart som mogleg, sidan vi vanlegvis berre etterbetaler for tre månader. Du finn meir informasjon og søknad på ${Constants.OMSTILLINGSSTOENAD_URL}",
                        English to "We encourage you to apply for the adjustment allowance as soon as possible, as we usually only retroactively pay for three months. You can find more information and the application on ${Constants.OMSTILLINGSSTOENAD_URL}"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde.",
                        Nynorsk to "Dersom du bur i utlandet, må du ta kontakt med trygdemyndigheitene i bostadslandet ditt og søkje om ytingar etter den avdøde.",
                        English to "If you live abroad, you should contact the social security authorities in your country of residence and apply for benefits related to the deceased.",
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.ALDER)) {
                paragraph {
                    text(
                        Bokmal to "Vi oppfordrer deg til å søke så snart som mulig frodi vi vi vanligvis kun etterbetaler for tre måneder. Du finner informasjon og søknad på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}.",
                        Nynorsk to "Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetalar for tre månader. Du finn informasjon og søknad på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}.",
                        English to "We encourage you to apply as soon as possible because we normally only pay retroactively for three months. You will find information and the application form at ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi oppfordrer deg til å søke så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder. Dersom du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt. Du kan lese mer om dette på ${Constants.NAV_URL}.",
                        Nynorsk to "Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader. Dersom du bur i utlandet, må du kontakte trygdeorgana i bustedslandet ditt.",
                        English to "You should apply as soon as possible as surviving person's benefits are not paid for more than 3 months in arrears. If you live outside Norway, you must contact the National Insurance authorities in your country of residence and apply for a pension.",
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.BARNEP)) {
                paragraph {
                    text(
                        Bokmal to "Du finner informasjon og søknadsskjema på ${Constants.BARNEPENSJON_URL}. Er du under 18 år, må vergen din søke om barnepensjon på vegne av deg.",
                        Nynorsk to "Du finn informasjon og søknadsskjema på ${Constants.BARNEPENSJON_URL}. Er du under 18 år, må verja di søkje om barnepensjon på dine vegner.",
                        English to "You will find information and the application form at ${Constants.BARNEPENSJON_URL}. If you are under the age of 18, your guardian must apply for a children's pension on your behalf.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dersom du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om pensjon. Du kan lese mer om dette på ${Constants.NAV_URL}.",
                        Nynorsk to "Dersom du bor i utlandet, må du kontakte trygdeorgana i bustedslandet ditt og søkje om pensjon. Du kan lese meir om dette på ${Constants.NAV_URL}.",
                        English to "If you live outside Norway, you must contact the National Insurance authorities in your country of residence and apply for a pension.",
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.GJENLEV, Sakstype.UFOREP)) {
                includePhrase(Felles.DuKanLeseMer)
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.GJENLEV, Sakstype.UFOREP, Sakstype.ALDER)) {
                title2 {
                    text(
                        Bokmal to "For deg som har barn under 18 år",
                        Nynorsk to "For deg som har barn under 18 år",
                        English to "If you have children under the age of 18"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finner søknadskjema og mer informasjon om dette på ${Constants.NAV_URL}.",
                        Nynorsk to "Forsørgjer du barn under 18 år, kan du ha rett til utvida barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finn søknadsskjema og meir informasjon om dette på ${Constants.NAV_URL}.",
                        English to "If you provide for children under the age of 18, you may be entitled to extended child benefits. In addition, children may be entitled to a children's pension. You will find the application form and more information about this at ${Constants.NAV_URL}"
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.UFOREP)) {
                paragraph {
                    text(
                        Bokmal to "Har du barnetillegg i uføretrygden, kan barnetillegget ditt bli endret. Du vil få et vedtak fra oss dersom barnetillegget endres. Hvis du ikke er innvilget barnetillegg, kan du søke om dette.",
                        Nynorsk to "Har du barnetillegg i uføretrygda, kan barnetillegget ditt bli endra. Du får eit vedtak frå oss dersom barnetillegget blir endra. Dersom du ikkje har fått innvilga barnetillegg, kan du søkje om det.",
                        English to "If you have a child supplement to your disability benefit, your child supplement may be changed. You will receive a decision from us if the child supplement changes. If you have not been granted a child supplement, you can apply for this.",
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.ALDER)) {
                paragraph {
                    text(
                        Bokmal to "Har du barnetillegg i alderspensjonen, kan barnetillegget ditt bli endret. Du vil få et vedtak fra oss dersom barnetillegget endres.",
                        Nynorsk to "Har du barnetillegg i alderspensjonen, kan barnetillegget ditt bli endra. Du får eit vedtak frå oss dersom barnetillegget blir endra.",
                        English to "If you have a child supplement to your retirement pension, your child supplement may be changed. You will receive a decision from us if the child supplement changes.",
                    )
                }
            }
            showIf(pesysData.sakstype.isOneOf(Sakstype.UFOREP, Sakstype.ALDER)) {
                includePhrase(Felles.DuKanLeseMer)
            }
            showIf(pesysData.gjenlevendesAlder.lessThan(67).and(pesysData.gjenlevendesAlder.greaterThanOrEqual(61))) {
                title2 {
                    text(
                        Bokmal to "AFP i offentlig sektor og rettigheter som gjenlevende",
                        Nynorsk to "AFP i offentleg sektor og rettar som attlevande",
                        English to "Tariff-based early retirement pensions (AFP) in the public sector and your rights as a surviving spouse",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan ikke få både avtalefestet pensjon og omstillingsstønad, men du må velge en av ytelsene. Du må kontakte oss for å få råd og hjelp.",
                        Nynorsk to "Du kan ikkje få både avtalefesta pensjon og omstillingsstønad, men du må velje ei av ytingane. Du må kontakte oss for å få råd og hjelp.",
                        English to "You cannot receive both a tariff-based early retirement pension and an adjustment allowance, you must choose one of the benefits. You must contact us for advice and assistance.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "AFP i privat sektor og rettigheter som gjenlevende",
                        Nynorsk to "AFP i privat sektor og rettar som attlevande",
                        English to "Tariff-based early retirement pensions (AFP) in the private sector and your rights as a surviving spouse",
                        )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan ha rett til omstillingsstønad dersom du får avtalefestet pensjon fra privat sektor og har valgt å ikke få utbetalt alderspensjon. Du må kontakte oss for å få råd og hjelp",
                        Nynorsk to "Du kan ha rett til omstillingsstønad dersom du får avtalefesta pensjon frå privat sektor og har valt å ikkje få utbetalt alderspensjon. Du må kontakte oss for å få råd og hjelp.",
                        English to "You may be entitled to an adjustment allowance if you receive a tariff-based early retirement pension (AFP) from the private secotr and have chosen not to receive payment of your retirement pension. You must contact us for advice and assistance.",
                    )
                }
            }

            title2 {
                text(
                    Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
                    Nynorsk to "Rettar når avdøde har budd eller arbeidd i utlandet",
                    English to "Rights if the deceased has lived or worked abroad"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis avdøde har bodd eller arbeidet i utlandet kan dette få betydning for hvor mye du får ubetalt i pensjon. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                    Nynorsk to "Dersom avdøde har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får utbetalt i pensjon. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rett til pensjon frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.",
                    English to "If the deceased has lived or worked abroad, this may affect the amount of your pension. Norway has social security cooperates with a number of countries through the EEA Agreement and other social security agreements. You may therefore also be entitled to a pension from other countries. We can help assist you with your application to apply to countries with which Norway has a social security agreement."
                )
            }






            showIf(pesysData.sakstype.isOneOf(Sakstype.GJENLEV, Sakstype.UFOREP, Sakstype.ALDER)) {
                title2 {
                    text(
                        Bokmal to "Andre pensjonsordninger",
                        Nynorsk to "Andre pensjonsordningar",
                        English to "Other pension schemes"
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
                        Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
                        English to "If the deceased was a member of a private or publicpension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company."
                    )
                }
            }

            includePhrase(Felles.HarDuSpoersmaal.alder)

        }
    }
}