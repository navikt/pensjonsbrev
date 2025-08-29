package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.*
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.GRUNNBELOEP_URL
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.OMS_ANDRE_STOENADER_URL
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.OMS_URL
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingstoenadInformasjonDoedsfallDTOSelectors.avdoedNavn
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingstoenadInformasjonDoedsfallDTOSelectors.borIutland

data class OmstillingstoenadInformasjonDoedsfallDTO(
    val avdoedNavn: String,
    val borIutland: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInformasjonDoedsfall : EtterlatteTemplate<OmstillingstoenadInformasjonDoedsfallDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INFORMASJON_DOEDSFALL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingstoenadInformasjonDoedsfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon om omstillingsstønad",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Informasjon om omstillingsstønad" },
                    nynorsk { +"Informasjon om omstillingsstønad" },
                    english { +"Information about adjustment allowance" },
                )
            }

            outline {
                paragraph {
                    text(
                        bokmal { +"Vi skriver til deg fordi vi har fått melding om at " + avdoedNavn + " er død, og du kan ha rett til omstillingsstønad." },
                        nynorsk { +"Vi skriv til deg fordi vi har fått melding om at " + avdoedNavn + " er død, og du kan ha rett til omstillingsstønad. " },
                        english { +"We are writing to you because we have received notice that " + avdoedNavn + " has died, and you may have rights as a surviving spouse." },
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
                        bokmal { +"Retten til å få omstillingsstønad avhenger av ditt forhold til avdøde." },
                        nynorsk { +"Det vil avhenge av forholdet ditt til avdøde om du har rett på omstillingsstønad." },
                        english { +"Your right to adjustment allowance will depend on your relationship with the deceased." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis dere var gift, partnere, samboere eller separert da dødsfallet skjedde, kan du få omstillingsstønad hvis noe av dette gjelder:" },
                        nynorsk { +"Viss de var gifte, partnarar, sambuarar eller separerte då dødsfallet skjedde, kan du få omstillingsstønad dersom eit av kriteria under er oppfylt:" },
                        english { +"If you were married, registered partners, cohabitants or separated at the time of death, you may qualify for adjustment allowance if any of the following applies to you:" },
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                bokmal { +"Ekteskapet varte i minst 5 år." },
                                nynorsk { +"Ekteskapet varte i minst 5 år." },
                                english { +"You had been married more than 5 years." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Du har vært gift eller samboer med den avdøde, og har eller har hatt barn med den avdøde." },
                                nynorsk { +"Du har vore gift eller sambuar med avdøde, og har eller har hatt barn med avdøde." },
                                english { +"You were married to or living with the deceased, and you have or have had a child/children with the deceased." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Ha omsorg for barn under 18 år i minst 50 prosent dersom dere var gift i mindre enn 5 år. Dette gjelder alle barn du har omsorg for, ikke bare felles barn." },
                                nynorsk { +"Du har omsorg for barn under 18 år i minst 50 prosent dersom de var gifte i mindre enn 5 år. Dette gjeld alle barn du har omsorg for, ikkje berre felles barn." },
                                english { +"You had been married for less than 5 years, but you have custody of a child/children under the age of 18 for more than 50 percent of the time. This includes all children, not just joint children." },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Du må som hovedregel være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet. I noen tilfeller kan medlemskap i trygdeordninger i andre EØS-land telle likt som medlemskap i folketrygden." },
                        nynorsk { +"Du må som hovudregel vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra før sin død. I enkelte tilfelle kan medlemskap i trygdeordningar i andre EØS-land telje likt som medlemskap i folketrygda." },
                        english { +"Normally, you must be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme the last five years before their death. In some cases, social security membership from another EEA country may be considered equal to membership in the National Insurance Scheme." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan lese mer om disse rettighetene på $OMS_URL." },
                        nynorsk { +"Du kan lese meir om dine rettar på $OMS_URL." },
                        english { +"You can read more about this at $OMS_URL. " },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Kort om omstillingsstønaden" },
                        nynorsk { +"Kort om omstillingsstønaden" },
                        english { +"What is an adjustment allowance?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Omstillingsstønad er en tidsbegrenset stønad som vanligvis varer i tre år. Støtten skal sikre inntekt og gi hjelp til selvhjelp i en omstillingsperiode etter dødsfallet. Etter seks måneder må du som hovedregel være i arbeid eller en annen aktivitet slik at du etter hvert kan forsørge deg selv." },
                        nynorsk { +"Omstillingsstønad er ein tidsavgrensa stønad som vanlegvis varar i tre år. Støtta skal sikre inntekt og gi hjelp til sjølvhjelp i ein omstillingsperiode etter dødsfallet. Etter seks månader må du som hovudregel være i arbeid eller en annan aktivitet slik at du etter kvart kan forsørgje deg sjølv." },
                        english { +"The adjustment allowance is a time-limited benefit that normally only lasts three years. The allowance is intended to guarantee an income and to help you help yourself in an adjustment period after the death. After six months, you will normally be expected to have found employment or be participating in another type of activity, so that you can eventually provide for yourself." },
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
                        nynorsk { +"Full omsstillingsstønad er 2,25 gongar grunnbeløpet per år." },
                        english { +"The benefit is 2.25 times the National Insurance basic amount G, depending on the period of national insurance coverage for the person who died." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Inntekten din avgjør hvor mye du kan få. Stønaden reduseres med 45 prosent av den inntekten din som overstiger halvparten av folketrygdens grunnbeløp. Noen ytelser fra Nav, som for eksempel sykepenger og dagpenger, likestilles med arbeidsinntekt." },
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
                        bokmal { +"Du kan lese mer om grunnbeløp på ${GRUNNBELOEP_URL}." },
                        nynorsk { +"Du kan lese meir om grunnbeløp på ${GRUNNBELOEP_URL}." },
                        english { +"You can read more about the National Insurance basic amount at ${GRUNNBELOEP_URL}." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Hvordan søker du?" },
                        nynorsk { +"Korleis søkjer du?" },
                        english { +"How do you apply" },
                    )
                }
                showIf(borIutland.not()) {
                    paragraph {
                        text(
                            bokmal { +"Du finner informasjon og søknad på $OMS_URL." },
                            nynorsk { +"Du finn informasjon på $OMS_URL." },
                            english { +"You will find information and the application form at $OMS_URL." },
                        )
                    }
                }
                showIf(borIutland) {
                    paragraph {
                        text(
                            bokmal { +"Vi har informasjon om at du bor i utlandet. Bor du i et land Norge har trygdeavtale med, må du kontakte trygdemyndigheten i bostedslandet ditt før du søker omstillingsstønad. Du finner informasjon om hvilke land Norge har avtale med på ${Constants.Utland.OMS}." },
                            nynorsk { +"Etter dei opplysningane vi har, bur du i utlandet. Dersom du bur i eit land som Noreg har trygdeavtale med, må du kontakte trygdemaktene i dette landet før du søkjer om omstillingsstønad. På ${Constants.Utland.OMS} finn du meir informasjon om kva land Noreg har avtale med." },
                            english { +"According to our records, you live abroad. Go to $OMS_URL for more information on how to apply. If you live in a country Norway has a social security agreement with, you must contact the social security authority in your country of residence before you apply for adjustment allowance. See which countries Norway has a social security agreement with here: ${Constants.Utland.OMS}." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Vi oppfordrer deg til å søke så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder." },
                        nynorsk { +"Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader." },
                        english { +"We encourage you to apply as soon as possible because we normally only pay retroactively for three months." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Rettigheter hvis avdøde har bodd eller arbeidet i utlandet" },
                        nynorsk { +"Rettar når avdøde har budd eller arbeidd i utlandet" },
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

                showIf(borIutland.not()) {
                    title2 {
                        text(
                            bokmal { +"Andre stønader til gjenlevende" },
                            nynorsk { +"Andre ytingar du kan ha rett til som attlevande" },
                            english { +"Other benefits you may be entitled to as a surviving spouse" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du kan også ha rett til stønad til barnetilsyn, tilleggsstønad og stønad til skolepenger. Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd." },
                            nynorsk { +"Du kan også ha rett til stønad til barnetilsyn, tilleggsstønad og stønad til skulepengar. Forsørgjer du barn under 18 år, kan du ha rett til utvida barnetrygd." },
                            english { +"You may also be entitled to child care benefits, supplemental benefits and an allowance to cover tuition fees. If you provide for children under the age of 18, you may be entitled to extended child benefit." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du kan lese mer om dette på $OMS_ANDRE_STOENADER_URL" },
                            nynorsk { +"Du kan lese meir om dette på $OMS_ANDRE_STOENADER_URL" },
                            english { +"" },
                        )
                    }
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
                        bokmal { +"Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet." },
                        nynorsk { +"Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet." },
                        english { +"If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Dersom du selv mottar avtalefestet pensjon fra offentlig sektor, må du kontakte oss for nærmere veiledning." },
                        nynorsk { +"Dersom du sjølv mottek avtalefesta pensjon frå offentleg sektor, må du kontakte oss for nærare rettleiing." },
                        english { +"If you are receiving tariff-based early retirement pensions (AFP) in the public sector, you should contact us for further guidance." },
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
