package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_NAERINGSINNTEKTER
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerNaeringsinntektDto
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object Naeringsinntekter : RedigerbarTemplate<InnhentingOpplysningerNaeringsinntektDto> {

    override val kode = UT_INNH_OPPL_NAERINGSINNTEKTER
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av opplysninger om næringsinntekter",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        val ikkeMottattInntektsskjema = saksbehandlervalg("ikkeMottattInntektsskjema", "Ikke mottatt inntektsskjema").bool()
        val beOmPersonligeSelvangivelser = saksbehandlervalg("beOmPersonligeSelvangivelser", "Dine personlige selvangivelser").bool()
        val beOmNaeringsspesifiskasjon = saksbehandlervalg("beOmNaeringsspesifiskasjon", "Næringsspesifikasjon levert i Altinn").bool()
        val beOmNaeringsoppgaver = saksbehandlervalg("beOmNaeringsoppgaver", "Næringsoppgave (gjelder kun før 2021)").bool()
        val beOmPersoninntektsskjema = saksbehandlervalg("beOmPersoninntektsskjema", "Personinntektsskjema (kun selvstendig næringsdrivende før 2022)").bool()
        val beOmForelopigResultatregnskap = saksbehandlervalg("beOmForelopigResultatregnskap", "Foreløpig resultatregnskap og fremtidig PGI").bool()

        title {
            text (bokmal { + "Vi trenger opplysninger om dine næringsinntekter " },
                nynorsk { + "Vi treng opplysningar om dine næringsinntekter " })
        }
        outline {
            paragraph {
                text(bokmal { + fritekst("Veiledning til saksbehandler: Gjør en selvstendig vurdering av hvilken dokumentasjon og informasjon som er nødvendig for å behandle saken. Rediger malen og tilpass til det du trenger.") },
                    nynorsk { + fritekst("Veiledning til saksbehandler: Gjør en selvstendig vurdering av hvilken dokumentasjon og informasjon som er nødvendig for å behandle saken. Rediger malen og tilpass til det du trenger.") })
            }
            paragraph {
                text(bokmal { +"Som et ledd i behandlingen av uføresaken din trenger vi bekreftede opplysninger om inntekten din. " },
                    nynorsk { +"Som eit ledd i handsaminga av uføresaka di treng vi bekrefta opplysningar om inntekta di. " })
            }

            showIf(ikkeMottattInntektsskjema) {
                paragraph {
                    text(bokmal { +"Du eier og driver " + fritekst("navn på firma") + ", og vi ber om at du fyller ut inntektsskjema for selvstendig næringsdrivende. " },
                        nynorsk { +"Du eig og driv " + fritekst("navn på firma") + ", og vi ber om at du fyller ut inntektsskjema for sjølvstendig næringsdrivande. " })
                }
                paragraph {
                    text(bokmal { +"Skjemaet finner du på nav.no/fyllut/nav120607 " },
                        nynorsk { +"Skjemaet finn du på nav.no/fyllut/nav120607 " })
                }
            }.orShow {
                paragraph {
                    text(bokmal { +"Du eier og driver " + fritekst("navn på firma") + ", og vi har derfor behov for flere opplysninger for å få vurdert din inntektsevne. " },
                        nynorsk { +"Du eig og driv " + fritekst("navn på firma") + ", og vi har derfor behov for fleire opplysningar for å få vurdert inntektsevna di. " })
                }
            }

            paragraph {
                text(bokmal { +"Dersom selskapet er avsluttet, så ber vi om dokumentasjon på dette; gjerne i form av melding om sletting fra Brønnøysundregisteret. " },
                    nynorsk { +"Dersom selskapet er avslutta, så ber vi om dokumentasjon på dette; gjerne i form av melding om sletting frå Brønnøysundregisteret. " })
            }
            showIf(beOmPersonligeSelvangivelser or beOmNaeringsspesifiskasjon or beOmNaeringsoppgaver or beOmPersoninntektsskjema or beOmForelopigResultatregnskap) {
                paragraph {
                    text(
                        bokmal { +"Dersom selskapet ikke er avsluttet og slettet, så ber vi om: " },
                        nynorsk { +"Dersom selskapet ikkje er avslutta og sletta, så ber vi om: " })
                    list {
                        showIf(beOmPersonligeSelvangivelser) {
                            item {
                                text(
                                    bokmal { +"dine personlige selvangivelser for årene " + fritekst("fom-tom") },
                                    nynorsk { +"dine personlege sjølvmeldingar for åra " + fritekst("fom-tom") })
                            }
                        }
                        showIf(beOmNaeringsspesifiskasjon) {
                            item {
                                text(
                                    bokmal { +"næringsspesifikasjon levert i Altinn/skatteetaten fra " + fritekst("år") },
                                    nynorsk { +"næringsspesifikasjon levert i Altinn/skatteetaten frå " + fritekst("år") })
                            }
                        }
                        showIf(beOmNaeringsoppgaver) {
                            item {
                                text(
                                    bokmal { +"næringsoppgaver for årene " + fritekst("fom-tom") },
                                    nynorsk { +"næringsoppgåver for åra " + fritekst("fom-tom") })
                            }
                        }
                        showIf(beOmPersoninntektsskjema) {
                            item {
                                text(
                                    bokmal { +"personinntektsskjema for " + fritekst("fom-tom") },
                                    nynorsk { +"personinntektsskjema for " + fritekst("fom-tom") })
                            }
                        }
                        showIf(beOmForelopigResultatregnskap) {
                            item {
                                text(
                                    bokmal { +"foreløpig resultatregnskap hittil i år og forventet fremtidig pensjonsgivende inntekt" },
                                    nynorsk { +"førebels resultatrekneskap hittil i år og forventa framtidig pensjonsgjevande inntekt" })
                            }
                        }
                    }
                }
            }
            paragraph {
                text(bokmal { +"Du må sende dokumentasjonen til oss enten digitalt via selvbetjeningsløsningen på $NAV_URL, eller i posten. Sender du skjemaet i posten, må du legge ved forsidearket som vi har lagt ved i dette brevet. Adressen er oppgitt på forsidearket." },
                    nynorsk { +"Du må sende dokumentasjonen til oss anten digitalt via sjølvbeteningsløysinga på $NAV_URL, eller i posten. Sender du skjemaet i posten, må du leggje ved forsidearket som vi har lagt ved i dette brevet. Adressa er oppgitt på forsidearket." })
            }
            paragraph {
                text(bokmal { +"Ved tap av delvis inntektsevne, gis det en gradert ytelse som svarer til den del av inntektsevnen/arbeidsevnen som er tapt. " },
                    nynorsk { +"Ved tap av delvis inntektsevne, vert det gitt ei gradert yting som svarar til den del av inntektsevna/arbeidsevna som er tapt. " })
            }
            paragraph {
                text(bokmal { +"For å kunne vurdere dette må vi ha bekreftede inntektsopplysninger der det fremgår hvilken inntekt du forventer å få fremover. " },
                    nynorsk { +"For å kunne vurdere dette må vi ha bekrefta inntektsopplysningar der det kjem fram kva inntekt du forventar å få framover. " })
            }
            paragraph {
                text(bokmal { +"Fristen for å sende inn nødvendige opplysninger settes til " + fritekst("dato") + ". Kontakt oss innen denne fristen hvis du trenger lengre tid."},
                    nynorsk { +"Fristen for å sende inn nødvendige opplysningar vert sett til " + fritekst("dato") + ". Kontakt oss innan denne fristen dersom du treng lengre tid." })
            }
            paragraph {
                text(bokmal { +"I folketrygdloven § 21-3 finner du informasjon om opplysningsplikten din til Nav. " },
                    nynorsk { +"I folketrygdlova § 21-3 finn du informasjon om opplysningsplikta di til Nav. " })
            }
            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
