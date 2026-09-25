package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.*
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.GRUNNBELOEP_URL
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.MISTET_NOEN_URL
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.OMS_ANDRE_STOENADER_URL
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.OMS_URL
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.selectors.omstillingstoenadInformasjonDoedsfallDTO.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.selectors.omstillingstoenadInformasjonDoedsfallData.*

data class OmstillingstoenadInformasjonDoedsfallData(
    val avdoedNavn: String,
    val borIutland: Boolean,
)

data class OmstillingstoenadInformasjonDoedsfallDTO(
    override val data: OmstillingstoenadInformasjonDoedsfallData,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInformasjonDoedsfall : EtterlatteTemplate<OmstillingstoenadInformasjonDoedsfallDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INFORMASJON_DOEDSFALL

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Du kan søke om omstillingsstønad",
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Du kan søke om omstillingsstønad" },
                    nynorsk { +"Du kan søkje om omstillingsstønad" },
                    english { +"You can apply for adjustment allowance" },
                )
            }

            outline {
                paragraph {
                    text(
                        bokmal { +"Vi har fått melding om at " + data.avdoedNavn + " er død. Du kan ha rett til omstillingsstønad, som er en midlertidig støtte til etterlatte etter dødsfall." },
                        nynorsk { +"Vi har fått melding om at " + data.avdoedNavn + " er død. Du kan ha rett til omstillingsstønad, som er ein tidsavgrensa stønad til etterlatte etter dødsfall." },
                        english { +"We have been informed that " + data.avdoedNavn + " has died. You may have rights as a surviving spouse, which is a temporary form of support for surviving relatives following a death." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du ønsker å søke, må du oppfylle vilkårene for å få stønaden. Nedenfor kan du lese om vilkårene og hvordan du søker." },
                        nynorsk { +"Viss du vil søkje, må du oppfylle vilkåra for å få stønaden. Nedanfor kan du lese om vilkåra og korleis du søkjer." },
                        english { +"If you wish to apply, you must meet the conditions for receiving benefits. You can read about the conditions for receiving benefits and how to apply below." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Hvem kan få omstillingsstønad?" },
                        nynorsk { +"Kven kan få omstillingsstønad?" },
                        english { +"Who is entitled to an adjustment allowance?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Retten til å få omstillingsstønad avhenger av ditt forhold til avdøde. Det har betydning om dere var gift, samboere, separert eller skilt da dødsfallet skjedde." },
                        nynorsk { +"Retten til å få omstillingsstønad avheng av ditt forhold til avdøde. Det har betydning om du var gift eller sambuar med avdøde, separert eller skilt frå då dødsfallet skjedde." },
                        english { +"Your right to adjustment allowance will depend on your relationship with the deceased. It matters whether you were married, cohabiting, separated or divorced at the time of death." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"En partner har samme rettigheter som en ektefelle." },
                        nynorsk { +"Ein partner har same rettighetar som ein ektefelle." },
                        english { +"A registered partner has the same rights as a surviving spouse." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Hvis dere var gift eller separert, kan du få omstillingsstønad hvis" },
                        nynorsk { +"Viss de var gifte eller separerte då dødsfallet skjedde, kan du få omstillingsstønad dersom" },
                        english { +"If you were married or separated, you may qualify for adjustment allowance if" },
                    )
                }

                paragraph {
                    list {
                        item {
                            text(
                                bokmal { +"Ekteskapet varte i minst 5 år" },
                                nynorsk { +"ekteskapet varte i minst 5 år" },
                                english { +"you had been married at least 5 years" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"dere har eller har hatt barn sammen" },
                                nynorsk { +"du har eller har hatt barn med avdøde" },
                                english { +"you have or have had children together" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"dere var gift i mindre enn 5 år, men du hadde minst 50 prosent omsorg for barn under 18 år. Dette gjelder alle barn du har omsorg for, ikke bare felles barn" },
                                nynorsk { +"du og avdøde var gift i mindre enn 5 år, men du hadde minst 50 prosent omsorg for barn under 18 år. Dette gjelder alle barn du har omsorg for, ikkje berre felles barn" },
                                english { +"you had been married for less than 5 years, but you have custody of a child under the age of 18 for more than 50 percent of the time. This includes all children, not just joint children" },
                            )
                        }
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Hvis dere var samboere når dødsfallet skjedde, kan du få omstillingsstønad hvis" },
                        nynorsk { +"Viss de var sambuarar då dødsfallet skjedde, kan du få omstillingsstønad dersom" },
                        english { +"If you were cohabiting at the time of death, you may qualify for adjustment allowance if" },
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                bokmal { +"dere har eller har hatt barn sammen" },
                                nynorsk { +"du har eller har hatt barn med avdøde" },
                                english { +"you have or have had children together" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"dere var samboere i mer enn 5 år, og tidligere var gift med hverandre " },
                                nynorsk { +"du og avdøde var sambuarar meir enn 5 år, og tidlegare var gift med kvarandre" },
                                english { +"you were cohabiting for more than 5 years, and you were previously married to each other." },
                            )
                        }
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Hvis dere var skilt, kan du få omstillingsstønad hvis" },
                        nynorsk { +"Dersom du var skilt frå avdøde, kan du få omstillingsstønad dersom" },
                        english { +"If you were divorced, you may qualify to adjustment allowance if" },
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                bokmal { +"ekteskapet varte i minst 25 år (eller 15 år hvis dere har eller har hatt felles barn), og du var helt eller i hovedsak forsørget av den avdøde" },
                                nynorsk { +"ekteskapet varte i minst 25 år (eller 15 år dersom du og avdøde har eller har hatt felles barn), og du var heilt eller i hovudsak forsørgd av den avdøde" },
                                english { +"the marriage lasted for at least 25 years (or 15 years if you have or have had children together), and you were wholly or mainly dependent on the deceased." },
                            )
                        }
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Du må som hovedregel være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste 5 årene fram til dødsfallet. I noen tilfeller kan medlemskap i trygdeordninger i andre EØS-land telle likt som medlemskap i folketrygden." },
                        nynorsk { +"Du må som hovudregel vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste 5 åra før sin død. I enkelte tilfelle kan medlemskap i trygdeordningar i andre EØS-land telje likt som medlemskap i folketrygda." },
                        english { +"Normally, you must be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme the last 5 years before their death. In some cases, social security membership from another EEA country may be considered equal to membership in the National Insurance Scheme." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan lese mer om disse rettighetene på $OMS_URL." },
                        nynorsk { +"Du kan lese meir om dine rettar på $OMS_URL." },
                        english { +"You can read more about this at $OMS_URL." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Kort om omstillingsstønaden og aktivitetsplikt " },
                        nynorsk { +"Kort om omstillingsstønad og aktivitetsplikt" },
                        english { +"What is an adjustment allowance and activity requirement" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan vanligvis få omstillingsstønad i inntil 3 år fra dødsfallstidspunktet. Stønaden kan forlenges med inntil 2 år hvis du tar utdanning som er nødvendig og hensiktsmessig, eller har behov for tiltak for å komme i arbeid. Du kan ikke få omstillingsstønad etter fylte 67 år." },
                        nynorsk { +"Du får vanlegvis omstillingsstønad i inntil 3 år frå datoen for dødsfallet. Stønaden kan forlengjast med inntil 2 år dersom du tek utdanning som er nødvendig og føremålstenleg, eller har behov for tiltak for å kome i arbeid. Du kan ikkje få omstillingsstønad etter fylte 67 år." },
                        english { +"The adjustment allowance is a time-limited benefit. You can normally receive adjustment allowance for up to 3 years from the date of death. Therefore, it is important that you apply as soon as possible after the death. The allowance may be extended for up to 2 additional years if you obtain an education that is necessary and appropriate, or if you need measures to help you enter employment. You cannot receive adjustment allowance after the age of 67." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Støtten skal sikre inntekt og gi hjelp til selvhjelp i en omstillingsperiode etter dødsfallet." },
                        nynorsk { +"Stønaden skal sikre inntekt og gi hjelp til sjølvhjelp i ein omstillingsperiode etter dødsfallet." },
                        english { +"The purpose of the adjustment allowance is to ensure a decent income and support self-reliance, enabling you to support yourself through employment after a transitional period following the death of your spouse." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"For å ha rett til omstillingsstønad må du være i arbeidsrettet aktivitet fra og med 6 måneder etter dødsdato. Målet er at du skal bli i stand til å forsørge deg selv med egen inntekt. Dette kalles aktivitetsplikt." },
                        nynorsk { +"For å ha rett til omstillingsstønad må du vere i aktivitet retta mot å kome i arbeid frå og med 6 månader etter dødsdatoen. Målet er at du skal bli i stand til å forsørgje deg sjølv med eiga inntekt. Dette blir kalla aktivitetsplikt." },
                        english { +"To be entitled to adjustment allowance, you must take part in work-related activities. The aim is for you to be able to support yourself with your own income. This is called the activity requirement." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Aktivitetsplikt betyr at" },
                        nynorsk { +"Aktivitetsplikt inneber at" },
                        english { +"The activity requirement means that" },
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                bokmal { +"6 måneder etter dødsfallet må du være i minst 50 prosent jobb eller annen aktivitet med sikte på å komme i jobb" },
                                nynorsk { +"6 månader etter dødsfallet må du vere i minst 50 prosent arbeid eller annan aktivitet med sikte på å kome i arbeid" },
                                english { +"6 months after the death, you must be in at least 50 percent employment or other activity aimed at getting a job" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"når det er gått 1 år siden dødsfallet, kan vi kreve at du er i opp mot 100 prosent aktivitet" },
                                nynorsk { +"når det har gått 1 år sidan dødsfallet, kan vi krevje at du er i opp mot 100 prosent aktivitet" },
                                english { +"1 year after the death, we may require you to be in up to 100 percent activity" },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"På $OMS_ANDRE_STOENADER_URL kan du lese mer om aktivitetsplikten og hva som kan skje med stønaden din hvis du ikke oppfyller den." },
                        nynorsk { +"På $OMS_ANDRE_STOENADER_URL kan du lese meir om aktivitetsplikta og kva som kan skje med stønaden din dersom du ikkje oppfyller den." },
                        english { +"You can read more about the activity requirement and what may happen to your allowance if you do not meet it at $OMS_ANDRE_STOENADER_URL." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Hvor mye kan du få?" },
                        nynorsk { +"Kor mykje du kan få?" },
                        english { +"How much are you entitled to?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Full omstillingsstønad er 2,25 ganger grunnbeløpet i folketrygden (G)." },
                        nynorsk { +"Full omstillingsstønad er 2,25 gongar grunnbeløpet per år." },
                        english { +"The benefit is 2.25 times the National Insurance basic amount G, depending on the period of national insurance coverage for the person who died." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Inntekten din avgjør hvor mye du kan få. Stønaden reduseres med 45 prosent av den inntekten din som overstiger halvparten av folketrygdens grunnbeløp. Noen ytelser fra Nav, som for eksempel sykepenger og dagpenger, regnes som arbeidsinntekt." },
                        nynorsk { +"Inntekta di avgjer kor mykje pengar du kan få. Stønaden blir redusert med 45 prosent av inntekta di som overstig halve grunnbeløpet i folketrygda. Nokre ytingar frå Nav, som til dømes sjukepengar og dagpengar, er likestilte med arbeidsinntekt." },
                        english { +"Your income determines how much money you are entitled to. The adjustment allowance will be reduced on the basis of income that you receive or expect to receive. Some benefits, such as sickness benefits and unemployment benefits are equivalent to earned income. Your pension will be reduced by 45 per cent of your income that exceeds half of the National Insurance basic amount." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis den avdøde har bodd utenfor Norge etter fylte 16 år, kan det også få betydning for størrelsen på stønaden." },
                        nynorsk { +"Viss den avdøde har budd utanfor Noreg etter fylte 16 år, kan det påverke storleiken på stønaden." },
                        english { +"If the deceased has lived outside of Norway after turning 16, it may also affect the amount of the allowance." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan lese mer om grunnbeløp på $GRUNNBELOEP_URL." },
                        nynorsk { +"Du kan lese meir om grunnbeløp på $GRUNNBELOEP_URL." },
                        english { +"You can read more about the National Insurance basic amount at $GRUNNBELOEP_URL." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Hvordan søker du?" },
                        nynorsk { +"Korleis søkjer du?" },
                        english { +"How do you apply?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du finner informasjon og søknad på $OMS_URL." },
                        nynorsk { +"Du finn informasjon og søknad på $OMS_URL." },
                        english { +"You will find information and the application form at $OMS_URL." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Omstillingsstønad kan som hovedregel bare etterbetales for de siste 3 månedene. Vi ber deg derfor om å søke så snart som mulig." },
                        nynorsk { +"Omstillingsstønad kan som hovudregel berre etterbetalast for de siste tre månadene. Vi ber deg difor om å søke så snart som mogleg." },
                        english { +"We encourage you to apply as soon as possible because we normally only pay retroactively for 3 months." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du bor i utlandet i et land som Norge har trygdeavtale med, må du kontakte trygdemyndighetene i bostedslandet før du søker. Du finner oversikt over hvilke land dette gjelder på ${Constants.Utland.OMS}." },
                        nynorsk { +"Dersom du bur i utlandet og i eit land som Noreg har trygdeavtale med, må du kontakte trygdemaktene i landet du bur i før du søkjer. Du finn oversikt over kva land dette gjeld på ${Constants.Utland.OMS}." },
                        english { +"If you live in a country Norway has a social security agreement with, you must contact the social security authority in your country of residence before you apply for adjustment allowance. See which countries Norway has a social security agreement with here: ${Constants.Utland.OMS}." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du bor i utlandet i et land som Norge ikke har trygdeavtale med, kan du søke direkte til Nav." },
                        nynorsk { +"Dersom du bur i utlandet i eit land som Noreg ikkje har trygdeavtale med, kan du søkje direkte til Nav." },
                        english { +"If you live in a country Norway does not have a social security agreement with, you can apply directly to Nav." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Har avdøde bodd eller arbeidet i utlandet?" },
                        nynorsk { +"Har avdøde budd eller arbeidd i utlandet?" },
                        english { +"Rights if the deceased has lived or worked abroad" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis avdøde tidligere har bodd eller arbeidet i utlandet, kan dette få betydning for hvor mye du får utbetalt. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rettigheter fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med." },
                        nynorsk { +"Dersom avdøde tidlegare har budd eller arbeidd i utlandet, kan det påverke kor mykje du får utbetalt. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rettar frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med." },
                        english { +"If the deceased has lived or worked abroad, this may affect the amount of your adjustment allowance. Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. Therefore, you may also be entitled to a pension from other countries. We can assist you with your application to countries with which Norway has a social security agreement." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Andre stønader til du kan ha rett på" },
                        nynorsk { +"Andre ytingar du kan ha rett til" },
                        english { +"Other benefits you may be entitled to as a surviving spouse" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan også ha rett til stønad til barnetilsyn, tilleggsstønad og stønad til skolepenger. Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd." },
                        nynorsk { +"Du kan også ha rett til stønad til barnetilsyn, tilleggsstønad og stønad til skulepengar. Forsørger du barn under 18 år, kan du ha rett til utvida barnetrygd." },
                        english { +"If you stay in Norway, you may also be entitled to childcare benefits, supplemental benefits and an allowance to cover tuition fees. If you provide for children under the age of 18, you may be entitled to extended child benefit." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan lese mer om dette på $MISTET_NOEN_URL." },
                        nynorsk { +"Du kan lese meir om dette på $MISTET_NOEN_URL." },
                        english { +"You can read more on $MISTET_NOEN_URL." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Andre pensjonsordninger" },
                        nynorsk { +"Andre pensjonsordningar" },
                        english { +"Other pension schemes" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet." },
                        nynorsk { +"Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet." },
                        english { +"If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du selv mottar avtalefestet pensjon fra offentlig sektor, må du kontakte oss for nærmere veiledning." },
                        nynorsk { +"Dersom du sjølv mottek avtalefesta pensjon frå offentleg sektor, må du kontakte oss for nærare rettleiing." },
                        english { +"If you are receiving tariff-based early retirement pensions (AFP) in the public sector, you should contact us for further guidance." },
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
