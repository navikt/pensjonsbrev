package no.nav.pensjon.brev.maler.adhoc.vedlegg

import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlagePensjonStatisk =
    createAttachment<LangBokmalNynorskEnglish, Unit>(
        title = newText(
            Bokmal to "Dine rettigheter og mulighet til å klage",
            Nynorsk to "Rettane dine og høve til å klage",
            English to "Your rights and how to appeal"
        ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggVeiledning)



        title1 {
            text(
                Bokmal to "Innsyn i saken din - forvaltningsloven § 18",
                Nynorsk to "Innsyn i saka di - forvaltningslova § 18",
                English to "Access to your case - Section 18 of the Public Administration Act"
            )
        }
        paragraph {
            text(
                Bokmal to "Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn på $NAV_URL for å se all kommunikasjon som har vært mellom deg og NAV i saken din. Du kan også ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON.",
                Nynorsk to "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan logge deg inn på $NAV_URL for å sjå all kommunikasjon som har vore mellom deg og NAV i saka di. Du kan også ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON.",
                English to "With some exceptions, you are entitled to access all the documents relating to your case. Log on to $NAV_URL to review the communication between you and NAV in connection with your case. You can also call us at tel.: +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON."
            )
        }

        includePhrase(VedleggHjelpFraAndre)

        title1 {
            text(
                Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
                Nynorsk to "Klage på vedtaket - folketrygdlova § 21-12",
                English to "Appealing a decision - Section 21-12 of the National Insurance Act"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket vil da vurdere saken din på nytt.",
                Nynorsk to "Du kan klage på vedtaket innan seks veker frå du fekk det. Kontoret som har gjort vedtaket, vurderer då saka di på nytt.",
                English to "You may appeal the decision within six weeks of receiving it. The department that made the decision will then review your case."
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
                Nynorsk to "Dersom du ikkje får gjennomslag for klaga di, blir ho send vidare til NAV Klageinstans for ny vurdering og avgjerd. Dersom du heller ikkje får gjennomslag hos klageinstansen, kan du anke saka inn for Trygderetten.",
                English to "If your complaint is declined, it will be forwarded to NAV Appeals for a new review and decision. If this review is also unsuccessful, you may appeal to The National Insurance Court."
            )
        }
        paragraph {
            text(
                Bokmal to "Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finner på $KLAGE_URL. Trenger du hjelp, er du velkommen til å ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON.",
                Nynorsk to "Klaga må vere skriftleg og innehalde namn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finn på $KLAGE_URL. Treng du hjelp, er du velkomen til å ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON.",
                English to "Your appeal must be made in writing and include your name, national identity number and address. Feel free to use the form found at $KLAGE_URL. Should you need assistance in writing the appeal, please call us at tel.: $NAV_KONTAKTSENTER_TELEFON_PENSJON."
            )
        }
        paragraph {
            text(Bokmal to "Du ", Nynorsk to "Du ", English to "You ")
            text(Bokmal to "må", Nynorsk to "må", English to "must", FontType.ITALIC)
            text(Bokmal to " skrive", Nynorsk to " skrive", English to " specify")
            list {
                item {
                    text(
                        Bokmal to "hvilket vedtak du klager på",
                        Nynorsk to "kva vedtak du klagar på",
                        English to "which decision you are appealing"
                    )
                }
                item {
                    text(
                        Bokmal to "hvilken endring i vedtaket du ber om",
                        Nynorsk to "kva endring i vedtaket du ber om",
                        English to "how you believe the decision should be amended"
                    )
                }
            }
        }
        paragraph {
            text(Bokmal to "Du bør også", Nynorsk to "Du bør også", English to "You should also")
            list {
                item {
                    text(
                        Bokmal to "skrive hvorfor du mener vedtaket er feil",
                        Nynorsk to "skrive kvifor du meiner vedtaket er feil",
                        English to "specify why you believe the decision is wrong"
                    )
                }
                item {
                    text(
                        Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                        Nynorsk to "nemne erklæringar og andre dokument som du legg ved klaga",
                        English to "list statements and other documents attached to the appeal"
                    )
                }
            }
        }

        paragraph {
            text(
                Bokmal to "Husk å undertegne klagen, ellers må vi sende den i retur til deg.",
                Nynorsk to "Hugs å skrive under klaga, elles må vi sende henne i retur til deg.",
                English to "Please remember to sign the appeal, otherwise it will be returned to you."
            )
        }
        paragraph {
            text(
                Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller NAV.",
                Nynorsk to "Får du medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. Informasjon om denne ordninga kan du få hos statsforvalteren, advokatar eller NAV.",
                English to "If your appeal is successful, you may be eligible for compensation for costs incurred to have the decision overturned. You may also be eligible for free legal aid, pursuant to the Legal Aid Act. Information about the legal aid scheme can be obtained from the county governor, lawyers or NAV."
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
                Nynorsk to "Du kan lese om saksomkostningar i forvaltningslova § 36.",
                English to "Read more about costs of action in Section 36 of the Public Administration Act."
            )
        }
    }

