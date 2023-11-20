package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles

@TemplateModelHelpers
val klageOgAnke = createAttachment(
    title = newText(
        Bokmal to "Informasjon om klage og anke",
        Nynorsk to "Informasjon om klage og anke",
        English to "Information on Complaints and Appeals",
    ),
    includeSakspart = false,
) {
    includePhrase(Felles.VeiledningFraNavForvaltningsloven11)
    includePhrase(Felles.HjelpFraAndreForvaltningsloven12)

    title2 {
        text(
            Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
            Nynorsk to "Klage på vedtaket – folketrygdlova § 21-12",
            English to "Appealing decisions – Section 21-12 of the National Insurance Act",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. " +
                    "Kontoret som har fattet vedtaket vil da vurdere saken din på nytt. " +
                    "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. " +
                    "Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten. ",
            Nynorsk to "Du kan klage på vedtaket innan seks veker frå du får det. " +
                    "Kontoret som fatta vedtaket, vil då vurdere saka di på nytt. " +
                    "Dersom du ikkje får gjennomslag for klaga di, blir ho send vidare til NAV Klageinstans for ny vurdering og avgjerd. " +
                    "Dersom du ikkje får gjennomslag hos klageinstansen heller, kan du anke saka inn for Trygderetten.",
            English to "You may appeal a decision within six weeks of receiving it. " +
                    "The office that made the decision will then reconsider your case. " +
                    "If your appeal is not successful, it will be forwarded to the NAV Appeals for reconsideration and decision. " +
                    "If your appeal is also rejected by NAV Appeals, you can appeal to the National Insurance Court (Trygderetten).",
        )
    }
    title2 {
        text(
            Bokmal to "Hvordan sende inn klage?",
            Nynorsk to "Slik sender du inn ei klage",
            English to "How do I file an appeal?",
        )
    }
    paragraph {
        text(
            Bokmal to "Klagen må være skriftlig og inneholde ditt navn, fødselsnummer og adresse. " +
                    "Du kan benytte skjemaet som du finner på ${Constants.KLAGE_URL}. " +
                    "Klagen kan sendes via innlogging på nettsiden vår, ${Constants.NAV_URL}, " +
                    "eller sendes til oss i posten til ${Constants.POSTADRESSE}. " +
                    "Hvis du velger å sende klagen per post, må du huske å undertegne den. " +
                    "Hvis klagen ikke er undertegnet, vil vi dessverre måtte returnere den til deg. ",
            Nynorsk to "Du må setje fram klaga skriftleg og oppgi namn, fødselsnummer og adresse. " +
                    "Bruk gjerne skjemaet du finn på ${Constants.KLAGE_URL}. Du kan logge på og sende klaga via nettsida vår, ${Constants.NAV_URL}, " +
                    "eller du kan sende ho per post til ${Constants.POSTADRESSE}. " +
                    "Dersom du vel å sende klaga per post, må du hugse å signere henne. " +
                    "Dersom klaga ikkje er signert, vil du diverre få henne i retur.",
            English to "The appeal must be made in writing and contain your name, national identity number and address. " +
                    "You can use the form that you will find online: ${Constants.Engelsk.KLAGE_URL}. " +
                    "The appeal can be submitted by logging in to our website (${Constants.NAV_URL}) or sending it to us by conventional mail to ${Constants.POSTADRESSE}. " +
                    "If you choose to mail your appeal, please remember to sign it. " +
                    "If your appeal is not signed, we will unfortunately have to return it to you.",
        )
    }
    title2 {
        text(
            Bokmal to "Hva må klagen inneholde?",
            Nynorsk to "Kva klaga skal innehalde",
            English to "What must the appeal contain?",
        )
    }
    paragraph {
        text(
            Bokmal to "Du må skrive",
            Nynorsk to "Du må opplyse",
            English to "You must tell us",
        )
        list {
            item {
                text(
                    Bokmal to "hvilket vedtak du klager på",
                    Nynorsk to "kva vedtak du klagar på",
                    English to "which decision you are appealing against",
                )
            }
            item {
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "kva endring du ønskjer i vedtaket",
                    English to "what change in the decision you are requesting",
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Du bør også",
            Nynorsk to "I tillegg bør du",
            English to "You should also",
        )
        list {
            item {
                text(
                    Bokmal to "skrive hvorfor du mener vedtaket er feil",
                    Nynorsk to "grunngi kvifor du meiner vedtaket er feil",
                    English to "explain why you think the decision is wrong",
                )
            }
            item {
                text(
                    Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                    Nynorsk to "nemne erklæringar og andre dokument du legg ved klaga",
                    English to "list any declarations and other documents that you enclose with your appeal",
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "NAV kan hjelpe deg med å skrive ned klagen. Trenger du hjelp, er du velkommen til å ringe oss på telefon ${Constants.KONTAKTTELEFON_PENSJON}.",
            Nynorsk to "NAV kan hjelpe deg med å skrive ned klaga. Viss du treng hjelp, må du gjerne ringje oss på telefon ${Constants.KONTAKTTELEFON_PENSJON}.",
            English to "The NAV office can help you with the wording of your appeal. If you need help, you are welcome to call us by phone (${Constants.KONTAKTTELEFON_PENSJON}).",
        )
    }
    // TODO: Tilbakekreving skal inn her dei gongane det er aktuelt
    title2 {
        text(
            Bokmal to "Du kan få dekket utgifter",
            Nynorsk to "Du kan få dekt utgifter",
            English to "You may be reimbursed for expenses",
        )
    }
    paragraph {
        text(
            Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller NAV.",
            Nynorsk to "Dersom du får medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. Statsforvaltaren, ein advokat eller NAV kan gi deg meir informasjon om denne ordninga.",
            English to "If your appeal is accepted and the decision reversed, you may be reimbursed for substantial expenses that have been necessary to amend the decision. You may be entitled to free legal aid pursuant to the Legal Aid Act. Information about this scheme can be obtained from the County Governor, an attorney or NAV.",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
            Nynorsk to "Sakskostnader er beskrive nærmare i forvaltingslova § 36.",
            English to "You can read about legal costs in Section 36 of the Public Administration Act.",
        )
    }
}