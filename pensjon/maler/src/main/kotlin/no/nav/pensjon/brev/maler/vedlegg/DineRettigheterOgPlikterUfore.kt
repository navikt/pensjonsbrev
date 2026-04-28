package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDtoSelectors.utland
import no.nav.pensjon.brev.maler.fraser.common.Constants.FULLMAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.MELDE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType

val vedleggDineRettigheterOgPlikterUfore = createAttachment<LangBokmalNynorsk, DineRettigheterOgPlikterUforeDto>(
        title = {
            text(
                bokmal { +"Dine rettigheter og plikter" },
                nynorsk { +"Rettane og pliktene dine" },
            )
        },
        includeSakspart = false,
    ) {
        title1 {
            text(
                bokmal { +"Du må gi oss beskjed om endringer i din situasjon" },
                nynorsk { +"Du må gi oss beskjed om endringar i situasjonen din" }
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { +"Du kan gi oss beskjed ved å logge inn på nav.no: $MELDE_URL" },
                        nynorsk { +"Du kan gi oss beskjed ved å logge inn på nav.no: $MELDE_URL" }
                    )
                }
                item {
                    text(
                        bokmal { +"Kan du ikke logge inn, kan du ringe oss på $NAV_KONTAKTSENTER_TELEFON. " },
                        nynorsk { +"Kan du ikkje logge inn, kan du ringe oss på $NAV_KONTAKTSENTER_TELEFON. " }
                    )
                }
                item {
                    text(
                        bokmal { +"Du kan også skrive til oss $KONTAKT_URL" },
                        nynorsk { +"Du kan også skrive til oss $KONTAKT_URL" }
                    )
                }
            }
        }
        paragraph {
            text( bokmal { +"Du må blant annet gi oss beskjed hvis: " },
                nynorsk { +"Du må blant anna gi oss beskjed dersom: " }
            )
            list {
                item {
                    text(
                        bokmal { +"inntekten din endrer seg. Dette gjelder også inntekten til samboer eller ektefelle hvis du har barnetillegg for felles barn. Du kan informere Nav om endret inntekt ved å bruke inntektsplanleggeren på $NAV_URL" },
                        nynorsk { +"inntekta di endrar seg. Dette gjeld også inntekta til sambuar eller ektefelle dersom du har barnetillegg for felles barn. Du kan informere Nav om endra inntekt ved å bruke inntektsplanleggaren på $NAV_URL" }
                    )
                }
                item {
                    text(
                        bokmal { +"du endrer adresse" },
                        nynorsk { +"du endrar adresse" }
                    )
                }
                item {
                    text(
                        bokmal { +"du skal begynne å arbeide i utlandet" },
                        nynorsk { +"du skal byrje å arbeide i utlandet" }
                    )
                }
                item {
                    showIf(utland) {
                        text(
                            bokmal { +"du skal oppholde deg utenfor bostedslandet ditt" },
                            nynorsk { +"du skal opphalde deg utanfor bustadlandet ditt" }
                        )
                    }.orShow {
                        text(
                            bokmal { +"du skal oppholde deg utenfor Norge lengre enn seks måneder" },
                            nynorsk { +"du skal opphalde deg utanfor Noreg lenger enn seks månader" }
                        )
                    }
                }
                item {
                    text(
                        bokmal { +"du skal flytte til et annet land" },
                        nynorsk { +"du skal flytte til eit anna land" }
                    )
                }
                item {
                    text(
                        bokmal { +"sivilstanden din endrer seg (inngår ekteskap/partnerskap/samboerskap, blir separert eller skiller deg)" },
                        nynorsk { +"sivilstanden din endrar seg (inngår ekteskap/partnarskap/sambuarskap, blir separert eller skil deg)" }
                    )
                }
                item {
                    text(
                        bokmal { +"det skjer endringer i omsorgsituasjonen for barna du forsørger" },
                        nynorsk { +"det skjer endringar i omsorgssituasjonen for barna du forsørgjer" }
                    )
                }
                item {
                    text(
                        bokmal { +"barna du forsørger skal flytte til et annet land" },
                        nynorsk { +"barna du forsørgjer skal flytte til eit anna land" }
                    )
                }
                item {
                    text(
                        bokmal { +"barna du forsørger skal oppholde seg i et annet land mer enn 90 dager i løpet av en tolvmånedersperiode" },
                        nynorsk { +"barna du forsørgjer skal opphalde seg i eit anna land meir enn 90 dagar i løpet av ein tolvmånadersperiode" }
                    )
                }
                item {
                    text(
                        bokmal { +"tjenestepensjon fra offentlig eller private ordninger endrer seg" },
                        nynorsk { +"tenestepensjon frå offentlege eller private ordningar endrar seg" }
                    )
                }
                item {
                    text(
                        bokmal { +"individuelle pensjonsordninger, livrente og gavepensjon endrer seg" },
                        nynorsk { +"individuelle pensjonsordningar, livrente og gåvepensjon endrar seg" }
                    )
                }
                item {
                    text(
                        bokmal { +"ytelser og pensjon fra andre land endrer seg" },
                        nynorsk { +"ytingar og pensjon frå andre land endrar seg" }
                    )
                }
                item {
                    text(
                        bokmal { +"du, samboer eller ektefelle blir innlagt på institusjon" },
                        nynorsk { +"du, sambuar eller ektefelle blir innlagd på institusjon" }
                    )
                }
                item {
                    text(
                        bokmal { +" du, samboer eller ektefelle sitter i varetekt, soner straff eller er under forvaring" },
                        nynorsk { +"du, sambuar eller ektefelle sit i varetekt, sonar straff eller er under forvaring" }
                    )
                }
            }
        }

        paragraph {
            text(bokmal { +"Du kan lese om plikt til å melde fra i folketrygdloven § 21-3." },
                nynorsk { +"Du kan lese om plikt til å melde frå i folketrygdlova § 21-3." }
            )
        }

        title2 {
            text(
                bokmal { +"Vi kan hjelpe deg" },
                nynorsk { +"Vi kan hjelpe deg" }
            )
        }
        paragraph {
            text(
                bokmal { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre vårt beste for å hjelpe deg." },
                nynorsk { +"Vi har plikt til å rettleie deg om rettane og pliktene dine i saka di, både før, under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, vil vi gjere vårt beste for å hjelpe deg." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan lese mer om vår plikt til å hjelpe deg i forvaltningsloven § 11." },
                nynorsk { +"Du kan lese meir om vår plikt til å hjelpe deg i forvaltningslova § 11." }
            )
        }

        title2 {
            text(
                bokmal { +"Du kan få hjelp fra andre" },
                nynorsk { +"Du kan få hjelp frå andre" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne skjemaet du finner på $FULLMAKT_URL." },
                nynorsk { +"Du kan be om hjelp frå andre under heile saksbehandlinga, for eksempel av advokat, rettshjelpar, ein organisasjon du er medlem av, eller ein annan myndig person. Dersom den som hjelper deg, ikkje er advokat, må du gi denne personen ei skriftleg fullmakt. Bruk gjerne skjemaet du finn på $FULLMAKT_URL." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan lese hvem som kan hjelpe deg i forvaltningsloven § 12." },
                nynorsk { +"Du kan lese kven som kan hjelpe deg i forvaltningslova § 12." }
            )
        }

        title2 {
            text(
                bokmal { +"Innsyn i saken din" },
                nynorsk { +"Innsyn i saka di" }
            )
        }
        paragraph {
            text(
                bokmal { +"Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn via nav.no for å se dokumenter i saken din. Du kan også ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON." },
                nynorsk { +"Med få unntak har du rett til å sjå dokumenta i saka di. Du kan logge deg inn via nav.no for å sjå dokument i saka di. Du kan også ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan lese om retten til innsyn i forvaltningsloven § 18." },
                nynorsk { +"Du kan lese om retten til innsyn i forvaltningslova § 18." }
            )
        }

        title1 {
            text(
                bokmal { +"Klage på vedtaket" },
                nynorsk { +"Klage på vedtaket" }
            )
        }
        title2 {
            text(
                bokmal { +"Slik klager du" },
                nynorsk { +"Slik klagar du" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan klage på vedtaket innen seks uker fra du mottok det." },
                nynorsk { +"Du kan klage på vedtaket innan seks veker frå du fekk det." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan sende oss en skriftlig klage, bruk gjerne skjemaet som du finner på ${KLAGE_URL}. Kan du ikke skrive klagen selv, kan vi hjelpe deg. Du er velkommen til å ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON. Bor du i utlandet kan du ringe oss på telefon +47 21 07 31 00." },
                nynorsk { +"Du kan sende oss ei skriftleg klage, bruk gjerne skjemaet som du finn på ${KLAGE_URL}. Kan du ikkje skrive klaga sjølv, kan vi hjelpe deg. Du er velkomen til å ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON. Bur du i utlandet kan du ringe oss på telefon +47 21 07 31 00." }
            )
        }
        paragraph {
            text(
                bokmal { +"I klagen " },
                nynorsk { +"I klaga " }
            )
            text(
                bokmal { +"må" },
                nynorsk { +"må" },
                FontType.ITALIC
            )
            text(
                bokmal { +" du gi oss informasjon om" },
                nynorsk { +" du gi oss informasjon om" }
            )
            list {
                item {
                    text(
                        bokmal { +"navn, fødselsnummer og adresse" },
                        nynorsk { +"namn, fødselsnummer og adresse" }
                    )
                }
                item {
                    text(
                        bokmal { +"hvilket vedtak du klager på" },
                        nynorsk { +"kva vedtak du klagar på" }
                    )
                }
                item {
                    text(
                        bokmal { +"hvilken endring i vedtaket du ber om" },
                        nynorsk { +"kva endring i vedtaket du ber om" }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { +"Du " },
                nynorsk { +"Du " }
            )
            text(
                bokmal { +"bør" },
                nynorsk { +"bør" },
                FontType.ITALIC
            )
            text(
                bokmal { +" også" },
                nynorsk { +" også" }
            )
            list {
                item {
                    text(
                        bokmal { +"skrive hvorfor du mener vedtaket er feil" },
                        nynorsk { +"skrive kvifor du meiner vedtaket er feil" }
                    )
                }
                item {
                    text(
                        bokmal { +"fortelle oss hvilke erklæringer og andre dokumenter som du legger ved klagen" },
                        nynorsk { +"nemne erklæringar og andre dokument som du legg ved klaga" }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { +"Husk å signere klagen, ellers må vi sende den i retur til deg." },
                nynorsk { +"Hugs å skrive under klaga, elles må vi sende henne i retur til deg." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller Nav." },
                nynorsk { +"Du kan ha krav på fri rettshjelp etter rettshjelplova. Informasjon om denne ordninga kan du få hos statsforvaltaren, advokatar eller Nav." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan sende klagen direkte til Nav eller gjennom trygdemyndighetene i landet du bor i dersom du bor i utlandet." },
                nynorsk { +"Du kan sende klaga direkte til Nav eller gjennom trygdemyndigheitene i landet du bur i dersom du bur i utlandet." }
            )
        }

        title2 {
            text(
                bokmal { +"Dette skjer når vi får klagen" },
                nynorsk { +"Dette skjer når vi får klaga" }
            )
        }
        paragraph {
            text(
                bokmal { +"Kontoret som har fattet vedtaket vil vurdere saken din på nytt. Hvis du ikke får medhold i klagen din, blir klagen sendt videre til Nav klageinstans for ny vurdering og avgjørelse." },
                nynorsk { +"Kontoret som har gjort vedtaket, vurderer då saka di på nytt. Dersom du ikkje får medhald i klaga di, blir klaga send vidare til Nav klageinstans for ny vurdering og avgjerd." }
            )
        }
        paragraph {
            text(
                bokmal { +"Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten." },
                nynorsk { +"Dersom du heller ikkje får gjennomslag hos klageinstansen, kan du anke saka inn for Trygderetten." }
            )
        }
        paragraph {
            text(
                bokmal { +"Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan lese om saksomkostninger i forvaltningsloven § 36." },
                nynorsk { +"Får du medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan lese om saksomkostningar i forvaltningslova § 36." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan lese om dine rettigheter til å klage i folketrygdloven § 21-12." },
                nynorsk { +"Du kan lese om rettane dine til å klage i folketrygdlova § 21-12." }
            )
        }
    }
