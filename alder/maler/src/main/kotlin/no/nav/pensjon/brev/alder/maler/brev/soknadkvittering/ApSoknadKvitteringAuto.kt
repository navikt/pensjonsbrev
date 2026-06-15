package no.nav.pensjon.brev.alder.maler.brev.soknadkvittering

import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDto
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.afpPrivat
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.familieforhold
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.innledning
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.personopplysninger
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.utland
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpOpphoerSelectors.opphoerArsak
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpOpphoerSelectors.sisteDagArbeid
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.ansattDato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.ansattType
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.ansattforholdOpphoert
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.arbeidetUtlandEtter53
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.arbeidsgiverAdresse
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.arbeidsgiverNavn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.arbeidsgiverOrgnr
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.epost
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.inntektUtenArbeidsplikt
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.naeringsvirkEierandel20
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.omsorgForBarnUnder7 as afpOmsorgForBarnUnder7
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.opphoer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.permisjonSiste3Ar
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.permittertSiste3Ar
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.redusertStillingSiste3Ar
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.samtykkeEpost
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.stillingUnder20Etter53Ar
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatDetaljerSelectors.sykemeldtMerEnn26Siste3Ar
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatSelectors.detaljer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatSelectors.soektAfpPrivat
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AvdoedSelectors.foedselsnummer as avdoedFoedselsnummer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AvdoedSelectors.navn as avdoedNavn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsNavnOgFoedselsnummerSelectors.foedselsnummer as epsFoedselsnummer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsNavnOgFoedselsnummerSelectors.navn as epsNavn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.betegnelse
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.betegnelseGenitiv
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.betegnelseGenitivStor
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.giftOgBarn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.leverVarigAdskilt
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.navnOgFoedselsnummer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.pensjonOgInntekt
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.EpsSelectors.samboerFraDato as epsSamboerFraDato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.FamilieforholdSelectors.avdoed
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.FamilieforholdSelectors.eps
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.FamilieforholdSelectors.harSamboerSpoersmaal
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.FamilieforholdSelectors.omsorgForBarnUnder7
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.FamilieforholdSelectors.samboer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.FamilieforholdSelectors.sivilstand
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.SamboerSelectors.foedselsnummer as samboerFoedselsnummer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.SamboerSelectors.navn as samboerNavn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.SamboerSelectors.samboerskapOpphoertDato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.GiftOgBarnSelectors.harFellesBarn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.GiftOgBarnSelectors.tidligereGift
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.HarSamboerSpoersmaalSelectors.erNySamboer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.HarSamboerSpoersmaalSelectors.svar
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.InnledningSelectors.erNyttRegelverk
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.InnledningSelectors.iverksettelsesdato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.InnledningSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PensjonOgInntektSelectors.harAnnenInntekt
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PensjonOgInntektSelectors.mottarAfp
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PensjonOgInntektSelectors.sumInntekt
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.adresselinjer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.erFlyktning
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.erUtenlandsk
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.foedselsnummer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.kontonummer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.navn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.statsborgerskapLand
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.telefon
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.arbeidet
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.bodd
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.land
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.pensjonsordning
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.sluttDato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.startDato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.tilleggsinformasjon
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtenlandsoppholdSelectors.utlandsId
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtlandSelectors.harBoddArbeidetUtland
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtlandSelectors.opphold
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dsl.TableHeaderScope
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.formatMonthYear
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

private typealias KvitteringLang = LanguageSupport.Single<Language.Bokmal>
private typealias KvitteringTabell = TableScope<KvitteringLang, ApSoknadKvitteringAutoDto>
private typealias KvitteringHeader = TableHeaderScope<KvitteringLang, ApSoknadKvitteringAutoDto>

/**
 * Kvittering for innsendt søknad om alderspensjon (AP_SOKNAD_KVITTERING).
 *
 * Brevet journalføres som kvittering på at søknad om alderspensjon er mottatt og
 * distribueres IKKE til bruker. Innholdet speiler den gamle XSL-FO-baserte
 * kvitteringen (`alderspensjon.xsl`) felt for felt.
 */
@TemplateModelHelpers
object ApSoknadKvitteringAuto : AutobrevTemplate<ApSoknadKvitteringAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.AP_SOKNAD_KVITTERING

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Kvittering - Søknad om alderspensjon",
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal {
                        +ifElse(
                            afpPrivat.soektAfpPrivat,
                            "Søknad om alderspensjon og AFP i privat sektor",
                            "Søknad om alderspensjon",
                        )
                    },
                )
            }
            outline {
                paragraph {
                    table(header = seksjon("Innledning")) {
                        tekstRad("Ønsket start for uttak av pensjon", innledning.iverksettelsesdato.formatMonthYear())
                        showIf(innledning.erNyttRegelverk) {
                            tekstRad("Ønsket uttaksgrad", innledning.uttaksgrad.format() + " %")
                        }
                    }
                }

                paragraph {
                    table(header = seksjon("Opplysninger om deg")) {
                        tekstRad("Navn", personopplysninger.navn)
                        tekstRad("Fødselsnummer", personopplysninger.foedselsnummer)
                        adresseRad("Adresse", personopplysninger.adresselinjer)
                        tekstRad("Telefonnummer", personopplysninger.telefon.ifNull(""))
                        tekstRad("Statsborgerskap", ifElse(personopplysninger.erUtenlandsk, "Annet", "Norsk"))
                        ifNotNull(personopplysninger.statsborgerskapLand) { statsborgerskapsland ->
                            tekstRad("Statsborgerskap", statsborgerskapsland)
                        }
                        ifNotNull(personopplysninger.erFlyktning) { flyktning ->
                            jaNeiRad("Registrert som flyktning", flyktning)
                        }
                        tekstRad("Kontonummer for utbetaling", personopplysninger.kontonummer.ifNull(""))
                    }
                }

                paragraph {
                    table(header = seksjon("Familieforhold")) {
                        ifNotNull(familieforhold.omsorgForBarnUnder7) { omsorg ->
                            jaNeiRad("Omsorg for barn under 7 år i perioden 1967-1991", omsorg)
                        }
                        tekstRad("Sivilstand", familieforhold.sivilstand)
                        ifNotNull(familieforhold.avdoed) { avdoedRelasjon ->
                            tekstRad("Avdødes navn", avdoedRelasjon.avdoedNavn)
                            tekstRad("Avdødes fødselsnummer", avdoedRelasjon.avdoedFoedselsnummer.ifNull(""))
                        }
                        ifNotNull(familieforhold.samboer) { samboerRelasjon ->
                            tekstRad("Samboers navn", samboerRelasjon.samboerNavn)
                            tekstRad("Samboers fødselsnummer", samboerRelasjon.samboerFoedselsnummer.ifNull(""))
                            ifNotNull(samboerRelasjon.samboerskapOpphoertDato) { opphoertDato ->
                                tekstRad("Samboerskapet opphørte", opphoertDato.format(short = true))
                            }
                        }
                        ifNotNull(familieforhold.harSamboerSpoersmaal) { spoersmaal ->
                            rad(
                                "Jeg har ".expr() + ifElse(spoersmaal.erNySamboer, "ny samboer", "samboer"),
                                ifElse(spoersmaal.svar, "Ja", "Nei"),
                            )
                        }
                        ifNotNull(familieforhold.eps) { ektefelle ->
                            ifNotNull(ektefelle.navnOgFoedselsnummer) { navnFnr ->
                                rad(ektefelle.betegnelseGenitivStor + " navn", navnFnr.epsNavn)
                                rad(
                                    ektefelle.betegnelseGenitivStor + " fødselsnummer",
                                    navnFnr.epsFoedselsnummer.ifNull(""),
                                )
                            }
                            ifNotNull(ektefelle.epsSamboerFraDato) { fraDato ->
                                tekstRad("Samboer fra dato", fraDato.format(short = true))
                            }
                            ifNotNull(ektefelle.giftOgBarn) { giftBarn ->
                                jaNeiRad("Tidligere vært gift med", giftBarn.tidligereGift)
                                jaNeiRad("Har barn med samboer", giftBarn.harFellesBarn)
                            }
                            ifNotNull(ektefelle.leverVarigAdskilt) { varigAdskilt ->
                                jaNeiRad(
                                    "Lever du og ".expr() + ektefelle.betegnelse + " varig adskilt?",
                                    varigAdskilt,
                                )
                            }
                            ifNotNull(ektefelle.pensjonOgInntekt) { pensjonInntekt ->
                                jaNeiRad(
                                    "Mottar/søker din ".expr() + ektefelle.betegnelse + " AFP fra offentlig sektor?",
                                    pensjonInntekt.mottarAfp,
                                )
                                jaNeiRad(
                                    "Har ".expr() + ektefelle.betegnelse + " inntekt?",
                                    pensjonInntekt.harAnnenInntekt,
                                )
                                ifNotNull(pensjonInntekt.sumInntekt) { sum ->
                                    rad(
                                        "Din ".expr() + ektefelle.betegnelseGenitiv + " samlede årlige inntekt før skatt",
                                        sum.format(LocalizedFormatter.CurrencyFormat) + " kr per år",
                                    )
                                }
                            }
                        }
                    }
                }

                paragraph {
                    table(header = seksjon("Opphold utenfor Norge")) {
                        jaNeiRad("Har du bodd eller arbeidet utenfor Norge etter at du fylte 16 år?", utland.harBoddArbeidetUtland)
                        forEach(utland.opphold) { opphold ->
                            tekstRad("Land", opphold.land)
                            jaNeiRad("Bodde du i landet?", opphold.bodd)
                            jaNeiRad("Arbeidet du i landet?", opphold.arbeidet)
                            tekstRad("Startdato for oppholdet", opphold.startDato.format(short = true).ifNull(""))
                            tekstRad("Sluttdato for oppholdet", opphold.sluttDato.format(short = true).ifNull(""))
                            tekstRad("Pensjonsordning under oppholdet", opphold.pensjonsordning.ifNull(""))
                            tekstRad("Identitetsnummeret ditt i landet eller i pensjonsordningen", opphold.utlandsId.ifNull(""))
                            tekstRad("Tilleggsinformasjon", opphold.tilleggsinformasjon.ifNull(""))
                        }
                    }
                }

                paragraph {
                    table(header = seksjon("AFP i privat sektor")) {
                        jaNeiRad("Ønsker du å søke om AFP i privat sektor?", afpPrivat.soektAfpPrivat)
                        ifNotNull(afpPrivat.detaljer) { afp ->
                            tekstRad("Arbeidsgivers navn", afp.arbeidsgiverNavn.ifNull(""))
                            adresseRad("Arbeidsgivers adresse", afp.arbeidsgiverAdresse)
                            ifNotNull(afp.arbeidsgiverOrgnr) { orgnr ->
                                tekstRad("Arbeidsgivers organisasjonsnummer", orgnr)
                            }
                            ifNotNull(afp.afpOmsorgForBarnUnder7) { omsorg ->
                                jaNeiRad("Omsorg for barn under 7 år i perioden 1967-1991", omsorg)
                            }
                            tekstRad("Når ble du ansatt hos nåværende arbeidsgiver?", afp.ansattDato.format(short = true).ifNull(""))
                            jaNeiRadValgfri(
                                "Har arbeidsforholdet opphørt, eller er det bestemt når arbeidsforholdet skal opphøre?",
                                afp.ansattforholdOpphoert,
                            )
                            ifNotNull(afp.opphoer) { opphoer ->
                                tekstRad("Dato for opphør av arbeidsforholdet", opphoer.sisteDagArbeid.format(short = true).ifNull(""))
                                tekstRad("Årsak til opphør av arbeidsforhold", opphoer.opphoerArsak.ifNull(""))
                            }
                            tekstRad("Er du heltidsansatt, deltidsansatt, midlertidig ansatt eller sesongansatt?", afp.ansattType.ifNull(""))
                            jaNeiRadValgfri("Har du i løpet av de siste 3 år før du fylte 62 år fått redusert din stillingsprosent?", afp.redusertStillingSiste3Ar)
                            jaNeiRadValgfri("Har stillingsprosenten din vært under 20 prosent etter du fylte 53 år?", afp.stillingUnder20Etter53Ar)
                            jaNeiRadValgfri("Har du i løpet av de 3 siste år til sammen vært sykemeldt mer enn 26 uker?", afp.sykemeldtMerEnn26Siste3Ar)
                            jaNeiRadValgfri("Har du i løpet av de siste 3 år vært permittert?", afp.permittertSiste3Ar)
                            jaNeiRadValgfri(
                                "Har du i løpet av de siste 3 år hatt permisjon i en eller flere perioder som til sammen utgjør mer enn 26 uker?",
                                afp.permisjonSiste3Ar,
                            )
                            jaNeiRadValgfri(
                                "Har du i de siste 3 årene før du fylte 62 år fått utbetalt pensjon, ventelønn eller andre ytelser uten arbeidsplikt?",
                                afp.inntektUtenArbeidsplikt,
                            )
                            jaNeiRadValgfri(
                                "Har du etter fylte 53 år drevet næringsvirksomhet eller hatt en eierandel på 20 prosent eller mer i søknadsbedriften eller annen virksomhet?",
                                afp.naeringsvirkEierandel20,
                            )
                            jaNeiRadValgfri("Har du etter fylte 53 år arbeidet i utlandet?", afp.arbeidetUtlandEtter53)
                            jaNeiRadValgfri("Vil du at Fellesordningen for AFP skal kontakte deg på e-post?", afp.samtykkeEpost)
                            ifNotNull(afp.epost) { epostadresse ->
                                tekstRad("E-postadressen din", epostadresse)
                            }
                        }
                    }
                }
            }
        }
}

private fun seksjon(overskrift: String): KvitteringHeader.() -> Unit = {
    column(columnSpan = 7) { text(bokmal { +overskrift }) }
    column(columnSpan = 3) { }
}

private fun KvitteringTabell.rad(ledetekst: Expression<String>, verdi: Expression<String>) {
    row {
        cell { text(bokmal { +ledetekst }) }
        cell { text(bokmal { +verdi }) }
    }
}

private fun KvitteringTabell.tekstRad(ledetekst: String, verdi: Expression<String>) {
    rad(ledetekst.expr(), verdi)
}

private fun KvitteringTabell.adresseRad(ledetekst: String, adresselinjer: Expression<List<String>>) {
    row {
        cell { text(bokmal { +ledetekst }) }
        cell {
            forEach(adresselinjer) { linje ->
                text(bokmal { +linje })
                newline()
            }
        }
    }
}

private fun KvitteringTabell.jaNeiRad(ledetekst: String, verdi: Expression<Boolean>) {
    rad(ledetekst.expr(), ifElse(verdi, "Ja", "Nei"))
}

private fun KvitteringTabell.jaNeiRad(ledetekst: Expression<String>, verdi: Expression<Boolean>) {
    rad(ledetekst, ifElse(verdi, "Ja", "Nei"))
}

private fun KvitteringTabell.jaNeiRadValgfri(ledetekst: String, verdi: Expression<Boolean?>) {
    rad(ledetekst.expr(), ifElse(verdi.notNull(), ifElse(verdi.ifNull(false), "Ja", "Nei"), "".expr()))
}
