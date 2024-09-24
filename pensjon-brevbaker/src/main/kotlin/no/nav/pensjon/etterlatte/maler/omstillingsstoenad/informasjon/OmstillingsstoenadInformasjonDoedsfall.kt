package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
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
                    Bokmal to "Informasjon om omstillingsstønad",
                    Nynorsk to "Informasjon om omstillingsstønad",
                    English to "Information about adjustment allowance",
                )
            }

            outline {
                paragraph {
                    textExpr(
                        Bokmal to "Vi skriver til deg fordi vi har fått melding om at ".expr() + avdoedNavn + " er død, og du kan ha rett til omstillingsstønad.".expr(),
                        Nynorsk to "Vi skriv til deg fordi vi har fått melding om at ".expr() + avdoedNavn + " er død, og du kan ha rett til omstillingsstønad. ".expr(),
                        English to "We are writing to you because we have received notice that ".expr() + avdoedNavn + " has died, and you may have rights as a surviving spouse.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvem kan få omstillingsstønad?",
                        Nynorsk to "Kven kan få omstillingsstønad?",
                        English to "Who is entitled to an adjustment allowance?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Retten til å få omstillingsstønad avhenger av ditt forhold til avdøde.",
                        Nynorsk to "Det vil avhenge av forholdet ditt til avdøde om du har rett på omstillingsstønad.",
                        English to "Your right to adjustment allowance will depend on your relationship with the deceased.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis dere var gift, partnere, samboere eller separert da dødsfallet skjedde, kan du få omstillingsstønad hvis noe av dette gjelder:",
                        Nynorsk to "Viss de var gifte, partnarar, sambuarar eller separerte då dødsfallet skjedde, kan du få omstillingsstønad dersom eit av kriteria under er oppfylt:",
                        English to "If you were married, registered partners, cohabitants or separated at the time of death, you may qualify for adjustment allowance if any of the following applies to you:",
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                Bokmal to "Ekteskapet varte i minst 5 år.",
                                Nynorsk to "Ekteskapet varte i minst 5 år.",
                                English to "You had been married more than 5 years.",
                            )
                        }
                        item {
                            text(
                                Bokmal to "Du har vært gift eller samboer med den avdøde, og har eller har hatt barn med den avdøde.",
                                Nynorsk to "Du har vore gift eller sambuar med avdøde, og har eller har hatt barn med avdøde.",
                                English to "You were married to or living with the deceased, and you have or have had a child/children with the deceased.",
                            )
                        }
                        item {
                            text(
                                Bokmal to "Ha omsorg for barn under 18 år i minst 50 prosent dersom dere var gift i mindre enn 5 år. Dette gjelder alle barn du har omsorg for, ikke bare felles barn.",
                                Nynorsk to "Du har omsorg for barn under 18 år i minst 50 prosent dersom de var gifte i mindre enn 5 år. Dette gjeld alle barn du har omsorg for, ikkje berre felles barn.",
                                English to "You had been married for less than 5 years, but you have custody of a child/children under the age of 18 for more than 50 percent of the time. This includes all children, not just joint children.",
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        Bokmal to "Du må som hovedregel være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet. I noen tilfeller kan medlemskap i trygdeordninger i andre EØS-land telle likt som medlemskap i folketrygden.",
                        Nynorsk to "Du må som hovudregel vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra før sin død. I enkelte tilfelle kan medlemskap i trygdeordningar i andre EØS-land telje likt som medlemskap i folketrygda.",
                        English to "Normally, you must be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme the last five years before their death. In some cases, social security membership from another EEA country may be considered equal to membership in the National Insurance Scheme.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om disse rettighetene på $OMS_URL.",
                        Nynorsk to "Du kan lese meir om dine rettar på $OMS_URL.",
                        English to "You can read more about this at $OMS_URL. ",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Kort om omstillingsstønaden",
                        Nynorsk to "Kort om omstillingsstønaden",
                        English to "What is an adjustment allowance?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Omstillingsstønad er en tidsbegrenset stønad som vanligvis varer i tre år. Støtten skal sikre inntekt og gi hjelp til selvhjelp i en omstillingsperiode etter dødsfallet. Etter seks måneder må du som hovedregel være i arbeid eller en annen aktivitet slik at du etter hvert kan forsørge deg selv.",
                        Nynorsk to "Omstillingsstønad er ein tidsavgrensa stønad som vanlegvis varar i tre år. Støtta skal sikre inntekt og gi hjelp til sjølvhjelp i ein omstillingsperiode etter dødsfallet. Etter seks månader må du som hovudregel være i arbeid eller en annan aktivitet slik at du etter kvart kan forsørgje deg sjølv.",
                        English to "The adjustment allowance is a time-limited benefit that normally only lasts three years. The allowance is intended to guarantee an income and to help you help yourself in an adjustment period after the death. After six months, you will normally be expected to have found employment or be participating in another type of activity, so that you can eventually provide for yourself.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvor mye kan du få?",
                        Nynorsk to "Kor mykje du kan få?",
                        English to "How much are you entitled to?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Full omstillingsstønad er 2,25 ganger grunnbeløpet i folketrygden (G).",
                        Nynorsk to "Full omsstillingsstønad er 2,25 gongar grunnbeløpet per år.",
                        English to "The benefit is 2.25 times the National Insurance basic amount G, depending on the period of national insurance coverage for the person who died.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Inntekten din avgjør hvor mye du kan få. Stønaden reduseres med 45 prosent av den inntekten din som overstiger halvparten av folketrygdens grunnbeløp. Noen ytelser fra NAV, som for eksempel sykepenger og dagpenger, likestilles med arbeidsinntekt.",
                        Nynorsk to "Inntekta di avgjer kor mykje pengar du kan få. Stønaden blir redusert med 45 prosent av inntekta di som overstig halve grunnbeløpet i folketrygda. Nokre ytingar frå NAV, som til dømes sjukepengar og dagpengar, er likestilte med arbeidsinntekt.",
                        English to "Your income determines how much money you are entitled to. The adjustment allowance will be reduced on the basis of income that you receive or expect to receive. Some benefits, such as sickness benefits and unemployment benefits are equivalent to earned income. Your pension will be reduced by 45 per cent of your income that exceeds half of the National Insurance basic amount.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis den avdøde har bodd utenfor Norge etter fylte 16 år, kan det også få betydning for størrelsen på stønaden.",
                        Nynorsk to "Viss den avdøde har budd utanfor Noreg etter fylte 16 år, kan det påverke storleiken på stønaden.",
                        English to "If the deceased has lived outside of Norway after turning 16, it may also affect the amount of the allowance.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om grunnbeløp på ${GRUNNBELOEP_URL}.",
                        Nynorsk to "Du kan lese meir om grunnbeløp på ${GRUNNBELOEP_URL}.",
                        English to "You can read more about the National Insurance basic amount at ${GRUNNBELOEP_URL}.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvordan søker du?",
                        Nynorsk to "Korleis søkjer du?",
                        English to "How do you apply",
                    )
                }
                showIf(borIutland.not()) {
                    paragraph {
                        text(
                            Bokmal to "Du finner informasjon og søknad på $OMS_URL.",
                            Nynorsk to "Du finn informasjon på $OMS_URL.",
                            English to "You will find information and the application form at $OMS_URL.",
                        )
                    }
                }
                showIf(borIutland) {
                    paragraph {
                        text(
                            Bokmal to "Vi har informasjon om at du bor i utlandet. Bor du i et land Norge har trygdeavtale med, må du kontakte trygdemyndigheten i bostedslandet ditt før du søker omstillingsstønad. Du finner informasjon om hvilke land Norge har avtale med på ${Constants.Utland.OMS}.",
                            Nynorsk to "Etter dei opplysningane vi har, bur du i utlandet. Dersom du bur i eit land som Noreg har trygdeavtale med, må du kontakte trygdemaktene i dette landet før du søkjer om omstillingsstønad. På ${Constants.Utland.OMS} finn du meir informasjon om kva land Noreg har avtale med.",
                            English to "According to our records, you live abroad. Go to $OMS_URL for more information on how to apply. If you live in a country Norway has a social security agreement with, you must contact the social security authority in your country of residence before you apply for adjustment allowance. See which countries Norway has a social security agreement with here: ${Constants.Utland.OMS}.",
                        )
                    }
                }
                paragraph {
                    text(
                        Bokmal to "Vi oppfordrer deg til å søke så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder.",
                        Nynorsk to "Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader.",
                        English to "We encourage you to apply as soon as possible because we normally only pay retroactively for three months.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
                        Nynorsk to "Rettar når avdøde har budd eller arbeidd i utlandet",
                        English to "Rights if the deceased has lived or worked abroad",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis avdøde tidligere har bodd eller arbeidet i utlandet, kan dette få betydning for hvor mye du får utbetalt. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rettigheter fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                        Nynorsk to "Dersom avdøde tidlegare har budd eller arbeidd i utlandet, kan det påverke kor mykje du får utbetalt. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rettar frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.",
                        English to "If the deceased has lived or worked abroad, this may affect the amount of your adjustment allowance. Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. Therefore, you may also be entitled to a pension from other countries. We can assist you with your application to countries with which Norway has a social security agreement.",
                    )
                }

                showIf(borIutland.not()) {
                    title2 {
                        text(
                            Bokmal to "Andre stønader til gjenlevende",
                            Nynorsk to "Andre ytingar du kan ha rett til som attlevande",
                            English to "Other benefits you may be entitled to as a surviving spouse",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du kan også ha rett til stønad til barnetilsyn, tilleggsstønad og stønad til skolepenger. Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd.",
                            Nynorsk to "Du kan også ha rett til stønad til barnetilsyn, tilleggsstønad og stønad til skulepengar. Forsørgjer du barn under 18 år, kan du ha rett til utvida barnetrygd.",
                            English to "You may also be entitled to child care benefits, supplemental benefits and an allowance to cover tuition fees. If you provide for children under the age of 18, you may be entitled to extended child benefit.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du kan lese mer om dette på $OMS_ANDRE_STOENADER_URL",
                            Nynorsk to "Du kan lese meir om dette på $OMS_ANDRE_STOENADER_URL",
                            English to "",
                        )
                    }
                }

                title2 {
                    text(
                        Bokmal to "Andre pensjonsordninger",
                        Nynorsk to "Andre pensjonsordningar",
                        English to "Other pension schemes",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
                        Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
                        English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dersom du selv mottar avtalefestet pensjon fra offentlig sektor, må du kontakte oss for nærmere veiledning.",
                        Nynorsk to "Dersom du sjølv mottek avtalefesta pensjon frå offentleg sektor, må du kontakte oss for nærare rettleiing.",
                        English to "If you are receiving tariff-based early retirement pensions (AFP) in the public sector, you should contact us for further guidance.",
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
