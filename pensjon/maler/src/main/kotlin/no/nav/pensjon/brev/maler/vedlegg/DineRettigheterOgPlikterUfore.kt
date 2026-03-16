package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
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

val vedleggDineRettigheterOgPlikterUfore = createAttachment<LangBokmalNynorsk, EmptyVedleggData>(
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
                nynorsk() { +"Du må blant anna gi oss beskjed hvis: " }
            )
            list {
                item {
                    text(
                        bokmal { +"inntekten din endrer seg. Dette gjelder også inntekten til samboer eller ektefelle hvis du har barnetillegg for felles barn. Du kan informere Nav om endret inntekt ved å bruke inntektsplanleggeren på $NAV_URL" },
                        nynorsk { +"inntekta di endrar seg. Dette gjeld også inntekta til sambuar eller ektefelle hvis du har barnetillegg for felles barn. Du kan informere Nav om endra inntekt ved å bruke inntektsplanleggeren på $NAV_URL" }
                    )
                }
                item {
                    text(
                        bokmal { +"du endrer adresse" },
                        nynorsk() { +"du endrar adresse" }
                    )
                }
                item {
                    text(
                        bokmal { +"u skal begynne å arbeide i utlandet" },
                        nynorsk() { +"du skal begynne å arbeide i utlandet" }
                    )
                }
                item {
                    text(
                        bokmal { +"du skal oppholde deg utenfor Norge lengre enn seks måneder" },
                        nynorsk() { +"du skal opphalde deg utanfor Noreg lengre enn seks månader" }
                    )
                }
                item {
                    text(
                        bokmal { +"du skal flytte til et annet land" },
                        nynorsk() { +"du skal flytte til eit anna land" }
                    )
                }
                item {
                    text(
                        bokmal { +"sivilstanden din endrer seg" },
                        nynorsk() { +"sivilstanda di endrar seg" }
                    )
                }
                item {
                    text(
                        bokmal { +"det skjer endringer i omsorgsituasjonen for barna du forsørger" },
                        nynorsk() { +"det skjer endringar i omsorgsituasjonen for barna du forsørger" }
                    )
                }
                item {
                    text(
                        bokmal { +"barna du forsørger skal flytte til et annet land" },
                        nynorsk() { +"barna du forsørger skal flytte til eit anna land" }
                    )
                }
                item {
                    text(
                        bokmal { +"barna du forsørger skal oppholde seg i et annet land mer enn 90 dager i løpet av en tolvmånedersperiode" },
                        nynorsk() { +"barna du forsørger skal opphalde seg i eit anna land meir enn 90 dagar i løpet av ein tolvmånadersperiode" }
                    )
                }
                item {
                    text(
                        bokmal { +"tjenestepensjon fra offentlig eller private ordninger endrer seg" },
                        nynorsk() { +"tjenestepensjon fra offentlige eller private ordningar endrar seg" }
                    )
                }
                item {
                    text(
                        bokmal { +"individuelle pensjonsordninger, livrente og gavepensjon endrer seg" },
                        nynorsk() { +"individuelle pensjonsordningar, livrente og gavepensjon endrar seg" }
                    )
                }
                item {
                    text(
                        bokmal { +"ytelser og pensjon fra andre land endrer seg" },
                        nynorsk() { +"ytelser og pensjon fra andre land endrar seg" }
                    )
                }
                item {
                    text(
                        bokmal { +"du, samboer eller ektefelle blir innlagt på institusjon" },
                        nynorsk() { +"du, sambuar eller ektefelle blir innlagt på institusjon" }
                    )
                }
                item {
                    text(
                        bokmal { +" du, samboer eller ektefelle sitter i varetekt, soner straff eller er under forvaring" },
                        nynorsk() { +" du, sambuar eller ektefelle sitter i varetekt, soner straff eller er under forvaring" }
                    )
                }
            }
        }

        paragraph {
            text(bokmal { +"Du kan lese om plikt til å melde fra i folketrygdloven § 21-3." },
                nynorsk() { +"Du kan lese om plikt til å melde fra i folketrygdloven § 21-3." }
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
                nynorsk { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saka di, både før, under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, vil vi gjere vårt beste for å hjelpe deg." }
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
                nynorsk { +"Du kan få hjelp fra andre" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne skjemaet du finner på $FULLMAKT_URL." },
                nynorsk { +"Du kan be om hjelp fra andre under hele saksbehandlinga, for eksempel av advokat, rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne skjemaet du finner på $FULLMAKT_URL." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan lese hvem som kan hjelpe deg i forvaltningsloven § 12." },
                nynorsk { +"Du kan lese hvem som kan hjelpe deg i forvaltningslova § 12." }
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
                nynorsk { +"Med få unntak har du rett til å se dokumentene i saka di. Du kan logge deg inn via nav.no for å se dokumenter i saka di. Du kan også ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON." }
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
                nynorsk { +"Du kan klage på vedtaket innen seks veker fra du mottok det." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan sende oss en skriftlig klage, bruk gjerne skjemaet som du finner på ${KLAGE_URL}. Kan du ikke skrive klagen selv, kan vi hjelpe deg. Du er velkommen til å ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON." },
                nynorsk { +"Du kan sende oss en skriftlig klage, bruk gjerne skjemaet som du finner på ${KLAGE_URL}. Kan du ikke skrive klagen selv, kan vi hjelpe deg. Du er velkommen til å ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON." }
            )
        }
        paragraph {
            text(
                bokmal { +"I klagen " },
                nynorsk { +" I klagen " }
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
                        nynorsk { +"hvilket vedtak du klagar på" }
                    )
                }
                item {
                    text(
                        bokmal { +"hvilken endring i vedtaket du ber om" },
                        nynorsk { +"hvilken endring i vedtaket du ber om" }
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
                        nynorsk { +"skrive hvorfor du mener vedtaket er feil" }
                    )
                }
                item {
                    text(
                        bokmal { +"fortelle oss hvilke erklæringer og andre dokumenter som du legger ved klagen" },
                        nynorsk { +"fortelle oss hvilke erklæringer og andre dokumenter som du legger ved klagen" }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { +"Husk å signere klagen, ellers må vi sende den i retur til deg." },
                nynorsk { +"Husk å signere klagen, ellers må vi sende den i retur til deg." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller Nav." },
                nynorsk { +"Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller Nav." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan sende klagen direkte til Nav eller gjennom trygdemyndighetene i landet du bor i dersom du bor i utlandet." },
                nynorsk { +"Du kan sende klagen direkte til Nav eller gjennom trygdemyndighetene i landet du bor i dersom du bor i utlandet." }
            )
        }

        title2 {
            text(
                bokmal { +"Dette skjer når vi får klagen" },
                nynorsk { +"Dette skjer når vi får klagen" }
            )
        }
        paragraph {
            text(
                bokmal { +"Kontoret som har fattet vedtaket vil vurdere saken din på nytt. Hvis du ikke får medhold i klagen din, blir klagen sendt videre til Nav klageinstans for ny vurdering og avgjørelse." },
                nynorsk { +"Kontoret som har fattet vedtaket vil vurdere saka di på nytt. Hvis du ikke får medhold i klagen din, blir klagen sendt videre til Nav klageinstans for ny vurdering og avgjørelse." }
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
                nynorsk { +"Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan lese om saksomkostninger i forvaltningslova § 36." }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan lese om dine rettigheter til å klage i folketrygdloven § 21-12." },
                nynorsk { +"Du kan lese om dine rettigheter til å klage i folketrygdloven § 21-12." }
            )
        }
    }
