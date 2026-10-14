package no.nav.pensjon.brev.ufore.maler.svartid

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.api.model.maler.svartid.OrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object OrienteringOmSaksbehandlingstidRedigerbar : RedigerbarTemplate<OrienteringOmSaksbehandlingstidDto> {

    // PE_UP_07_105
    override val kode = Ufoerebrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om saksbehandlingstid uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
        letterDataType = OrienteringOmSaksbehandlingstidDto::class
    ) {
        val forlengetSaksbehandlingstid = saksbehandlervalg("forlengetSaksbehandlingstid", "Forlenget saksbehandlingstid").bool()
        val kurantsak = saksbehandlervalg("kurantsak", "Kurantsak - virkningstidspunkt langt frem i tid").bool()
        val avklartInntektsevne = saksbehandlervalg("avklartInntektsevne", "Vi trenger bekreftelse på avklart inntektsevne").bool()
        val sykepenger = saksbehandlervalg("sykepenger", "Bruker mottar sykepenger").bool()
        val yrkesskade = saksbehandlervalg("yrkesskade", "Yrkesskade").bool()
        val omgjoring = saksbehandlervalg("omgjoring", "Omgjøring").bool()
        val omgjoringKlage = saksbehandlervalg("omgjoringKlage", "Omgjøring ved for sent fremsatt klage").bool()
        val saksomkostninger = saksbehandlervalg("saksomkostninger", "Saksomkostninger").bool()
        val ettersendeDok = saksbehandlervalg("ettersendeDok", "Ettersende dokumentasjon").bool()

        title {
            showIf(forlengetSaksbehandlingstid) {
                text(
                    bokmal { + "Orientering om forlenget saksbehandlingstid" },
                    nynorsk { + "Orientering om forlenget saksbehandlingstid" },
                )
            }.orShow {
                text(
                    bokmal { + "Orientering om saksbehandlingstid" },
                    nynorsk { + "Orientering om saksbehandlingstid" },
                )
            }
        }
        outline {
            showIf(forlengetSaksbehandlingstid) {
                paragraph {
                    text(
                        bokmal {
                            +"Vi har " + fritekst("dato") + " mottatt din søknad om uføretrygd. "
                            +"Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. "
                            +"Forsinkelsen skyldes " + fritekst("årsak til forsinkelse") + "."
                        },
                        nynorsk {
                            +"Vi har " + fritekst("dato") + " fått søknaden din om uføretrygd. "
                            +"Det vil dessverre ta oss lengre tid enn venta å behandle kravet. "
                            +"Forsinkinga skuldast " + fritekst("årsak til forsinkelse") + "."
                        }
                    )
                }

                title1 {
                    text(
                        bokmal { + "Ny svartid" },
                        nynorsk { + "Ny svartid" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi antar at kravet ditt kan bli ferdigbehandlet innen " + fritekst("svartid") + "." },
                        nynorsk { + "Vi reknar med at kravet ditt kan bli ferdigbehandla innan " + fritekst("svartid") + "." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Vi har " + fritekst("dato") + " mottatt søknaden din om uføretrygd." },
                        nynorsk { +"Vi har " + fritekst("dato") + " fått søknaden din om uføretrygd." },
                    )
                }

                showIf(kurantsak) {
                    paragraph {
                        text(
                            bokmal { + "I vårt saksbehandlingssystem er det ikke mulig å behandle saker med virkningstidspunkt som er mer enn 3 måneder frem i tid. Virkningstidspunktet kan tidligst settes til den måneden perioden din med rett til sykepenger utløper. " },
                            nynorsk { + "I vårt saksbehandlingssystem er det ikkje mogleg å behandle saker med virkningstidspunkt som er meir enn 3 månader fram i tid. Virkningstidspunktet kan tidlegast setjast til den månaden perioden din med rett til sjukepengar utløper. " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Vi ser at du har alvorlig sykdom, og saken din vil bli prioritert så snart den kan behandles. " },
                            nynorsk { + "Vi ser at du har alvorleg sjukdom, og saka di vil bli prioritert så snart den kan behandlast. " },
                        )
                    }
                }

                showIf(avklartInntektsevne) {
                    paragraph {
                        text(
                            bokmal { +"Du har søkt om uføretrygd uten at det er skrevet ferdig en konklusjon, 14a vedtak, fra Nav-kontoret ditt om at du anses som ferdig avklart. Hvis du ikke allerede har hatt kontakt med Nav-kontoret om å søke om uføretrygd, er det svært viktig at du nå kontakter dem om dette. " },
                            nynorsk { +"Du har søkt om uføretrygd utan at det er skrive ferdig ein konklusjon, 14a vedtak, frå Nav-kontoret ditt om at du vert rekna som ferdig avklart. Hvis du ikkje allereie har hatt kontakt med Nav-kontoret om å søkje om uføretrygd, er det svært viktig at du no kontaktar dei om dette. " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Hvis Nav-kontoret ikke har tilstrekkelig informasjon til å ta stilling til om all behandling og utprøving av mulighetene i arbeidslivet er avklart i ditt tilfelle, vil det vanligvis ikke være grunnlag for å innvilge uføretrygd. Du kan da selv velge å trekke søknaden. " },
                            nynorsk { +"Hvis Nav-kontoret ikkje har tilstrekkeleg informasjon til å ta stilling til om all behandling og utprøving av moglegheitene i arbeidslivet er avklart i ditt tilfelle, vil det vanlegvis ikkje vere grunnlag for å innvilge uføretrygd. Du kan då sjølv velje å trekkje søknaden. " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Inntektsevnen din må være ferdig avklart før vi kan behandle søknaden din om uføretrygd. I de fleste tilfeller der inntektsevne ikke er ferdig avklart, vil søknaden om uføretrygd avslås. " },
                            nynorsk { +"Inntektsevnen din må vere ferdig avklart før vi kan behandle søknaden din om uføretrygd. I dei fleste tilfelle der inntektsevne ikkje er ferdig avklart, vil søknaden om uføretrygd avslås. " },
                        )
                    }
                }

                showIf(sykepenger) {
                    paragraph {
                        text(
                            bokmal { +"Vi behandler søknaden din før sykepengene dine tar slutt. Hvis saksbehandlingen tar lengre tid, kontakter vi deg med mer informasjon. " },
                            nynorsk { +"Vi behandlar søknaden din før sjukepengane dine tek slutt. Hvis saksbehandlinga tek lengre tid, kontaktar vi deg med meir informasjon. " },
                        )
                    }

                    title1 {
                        text(
                            bokmal { +"Hvis du skal ta ferie før sykepengene tar slutt " },
                            nynorsk { +"Hvis du skal ta ferie før sjukepengane tek slutt " },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Vi ber deg kontakte arbeidsgiveren din for å avklare om du skal ta ut ferie. Feriedager gjør at sykepengeperioden din forskyves, slik at du har rett til sykepenger lenger. Vi ber om tilbakemelding på om du tar ut ferie eller ikke. Du kan gi oss beskjed ved å skrive til oss på $NAV_URL " },
                            nynorsk { +"Vi ber deg kontakte arbeidsgjevaren din for å avklare om du skal ta ut ferie. Feriedagar gjer at sjukepengeperioden din forskyves, slik at du har rett til sjukepengar lenger. Vi ber om tilbakemelding på om du tek ut ferie eller ikkje. Du kan gi oss beskjed ved å skrive til oss på $NAV_URL " },
                        )
                    }
                }

                showIf(yrkesskade) {
                    paragraph {
                        text(
                            bokmal { +"Vi har mottatt søknaden din om å få beregnet uføretrygden din etter reglene for yrkesskade eller yrkessykdom. " },
                            nynorsk { +"Vi har motteke søknaden din om å få berekna uføretrygda di etter reglane for yrkesskade eller yrkessjukdom. " },
                        )
                    }
                }

                showIf(omgjoring) {
                    paragraph {
                        text(
                            bokmal { +"Du ber om omgjøring av " + fritekst("eks. uføretidspunktet/ung ufør") + ". " },
                            nynorsk { +"Du ber om omgjering av " + fritekst("eks. uføretidspunktet/ung ufør") + ". " },
                        )
                    }
                }.orShowIf(omgjoringKlage) {
                    paragraph {
                        text(
                            bokmal { + fritekst("Dato") + " mottok vi klagen din på vedtaket vårt datert " + fritekst("dato") + ". Fordi vi fikk klagen din mer enn 12 måneder etter at vi fattet vedtaket, blir klagen behandlet som et krav om omgjøring. Du ber om omgjøring av " + fritekst("eks. uføretidspunktet/ung ufør") + ". " },
                            nynorsk { + fritekst("Dato") + " motte vi klagen din på vedtaket vårt datert " + fritekst("dato") + ". Fordi vi fekk klagen din meir enn 12 månader etter at vi fatta vedtaket, vert klagen behandla som eit krav om omgjering. Du ber om omgjering av " + fritekst("eks. uføretidspunktet/ung ufør") + ". " },
                        )
                    }
                }

                showIf(saksomkostninger) {
                    paragraph {
                        text(
                            bokmal { +"For å kunne vurdere kravet ditt om saksomkostninger, må vi ha konkret timeliste med informasjon om antall timer, timetakst og hva arbeidet besto i. Vi ber om at du sender oss dette innen to uker fra du mottok dette brevet. Hvis vi ikke får timeliste, kan vi avslå kravet. " },
                            nynorsk { +"For å kunne vurdere kravet ditt om saksomkostninger, må vi ha konkret timeliste med informasjon om antall timer, timetakst og hva arbeidet besto i. Vi ber om at du sender oss dette innen to uker fra du mottok dette brevet. Hvis vi ikke får timeliste, kan vi avslå kravet. " },
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { + "Saksbehandlingstiden vår er vanligvis " + fritekst("svartid") + ". " },
                        nynorsk { + "Saksbehandlingstida vår er vanlegvis " + fritekst("svartid") + ". " },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Hvis søknaden ikke blir ferdig behandlet i løpet av denne tiden, tar vi kontakt med deg. " },
                        nynorsk { + "Hvis søknaden ikkje blir ferdig behandla i løpet av denne tida, tek vi kontakt med deg. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Når søknaden er ferdig behandlet, får du et svar fra oss på «Min side» på $NAV_URL " },
                        nynorsk { + "Når søknaden er ferdig behandla, får du eit svar frå oss på «Mi side» på $NAV_URL " },
                    )
                }
            }

            showIf(ettersendeDok) {
                paragraph {
                    text(
                        bokmal { +"Da du har krysset av for at du skal ettersende dokumentasjon, må du sende oss dette innen to uker etter at du har mottatt dette brevet. " },
                        nynorsk { +"Da du har krysse av for at du skal ettersende dokumentasjon, må du sende oss dette innan to veker etter at du har motteke dette brevet. " },
                    )
                }
                includePhrase(Felles.EttersendeDokumentasjon)
                paragraph {
                    text(
                        bokmal { +"Det er viktig at vi får de nødvendige opplysningene eller hører fra deg innen fristen. Vi viser til folketrygdloven § 21-3 om din plikt til å gi oss de opplysningene vi trenger for å behandle søknaden din. " },
                        nynorsk { +"Det er viktig at vi får dei nødvendige opplysningane eller høyrer frå deg innan fristen. Vi viser til folketrygdloven § 21-3 om din plikt til å gi oss dei opplysningane vi treng for å behandle søknaden din. " },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss." },
                        nynorsk { +"Dersom vi treng fleire opplysningar frå deg, vil du høyre frå oss." },
                    )
                }
            }
            includePhrase(OrienteringOmSaksbehandlingstidFraser.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}