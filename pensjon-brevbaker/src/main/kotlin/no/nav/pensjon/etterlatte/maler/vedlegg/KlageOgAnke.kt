package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon
import no.nav.pensjon.etterlatte.maler.fraser.common.postadresse

fun klageOgAnke(
	bosattUtland: Boolean,
	tilbakekreving: Boolean = false,
): AttachmentTemplate<LangBokmalNynorskEnglish, Any> {
	return createAttachment(
		title = newText(
			Bokmal to "Informasjon om klage og anke",
			Nynorsk to "Informasjon om klage og anke",
			English to "Information on Complaints and Appeals"
		),
		includeSakspart = false,
	) {
		veiledning()
		hjelpFraAndre()
		klagePaaVedtaket()
		hvordanSendeKlage(bosattUtland.expr())
		hvaMaaKlagenInneholde(bosattUtland.expr())
		if (tilbakekreving) {
			tilbakekreving()
		}
		duKanFaaDekketUtgifter()
	}
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.duKanFaaDekketUtgifter() {
    title2 {
        text(
            Bokmal to "Du kan få dekket utgifter",
            Nynorsk to "Du kan få dekt utgifter",
            English to "You may be reimbursed for expenses"
        )
    }
    paragraph {
        text(
            Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige " +
                    "for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. " +
                    "Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller Nav.",
            Nynorsk to "Dersom du får medhald, kan du få dekt vesentlege utgifter som har vore nødvendige " +
                    "for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. " +
                    "Statsforvaltaren, ein advokat eller Nav kan gi deg meir informasjon om denne ordninga.",
            English to "If your appeal is accepted and the decision reversed, " +
                    "you may be reimbursed for substantial expenses that have been necessary to amend the decision. " +
                    "You may be entitled to free legal aid pursuant to the Legal Aid Act. " +
                    "Information about this scheme can be obtained from the County Governor, an attorney or Nav."
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
            Nynorsk to "Sakskostnader er beskrive nærmare i forvaltingslova § 36.",
            English to "You can read about legal costs in Section 36 of the Public Administration Act."
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.hvaMaaKlagenInneholde(
    bosattUtland: Expression<Boolean>
) {
    title2 {
        text(
            Bokmal to "Hva må klagen inneholde?",
            Nynorsk to "Kva klaga skal innehalde",
            English to "What must the appeal contain?"
        )
    }
    paragraph {
        text(
            Bokmal to "Du må skrive",
            Nynorsk to "Du må opplyse",
            English to "You must tell us"
        )
        list {
            item {
                text(
                    Bokmal to "hvilket vedtak du klager på",
                    Nynorsk to "kva vedtak du klagar på",
                    English to "which decision you are appealing against"
                )
            }
            item {
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "kva endring du ønskjer i vedtaket",
                    English to "what change in the decision you are requesting"
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Du bør også",
            Nynorsk to "I tillegg bør du",
            English to "You should also"
        )
        list {
            item {
                text(
                    Bokmal to "skrive hvorfor du mener vedtaket er feil",
                    Nynorsk to "grunngi kvifor du meiner vedtaket er feil",
                    English to "explain why you think the decision is wrong"
                )
            }
            item {
                text(
                    Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                    Nynorsk to "nemne erklæringar og andre dokument du legg ved klaga",
                    English to "list any declarations and other documents that you enclose with your appeal"
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Nav kan hjelpe deg med å skrive ned klagen. Trenger du hjelp, er du velkommen til å " +
                    "ringe oss på telefon ",
            Nynorsk to "Nav kan hjelpe deg med å skrive ned klaga. Viss du treng hjelp, " +
                    "må du gjerne ringje oss på telefon ",
            English to "The Nav office can help you with the wording of your appeal. " +
                    "If you need help, you are welcome to call us by phone ("
        )
        kontakttelefonPensjon(bosattUtland)
        text(
            Bokmal to ".",
            Nynorsk to ".",
            English to ")."
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.hvordanSendeKlage(
    bosattUtland: Expression<Boolean>
) {
    title2 {
        text(
            Bokmal to "Hvordan sende inn klage?",
            Nynorsk to "Slik sender du inn ei klage",
            English to "How do I file an appeal?"
        )
    }
    paragraph {
        text(
            Bokmal to "Klagen må være skriftlig og inneholde ditt navn, fødselsnummer og adresse. " +
                    "Du kan benytte skjemaet som du finner på ${Constants.KLAGE_URL}. Klagen kan sendes via " +
                    "innlogging på nettsiden vår, ${Constants.NAV_URL}, eller sendes til oss i posten til ",
            Nynorsk to "Du må setje fram klaga skriftleg og oppgi namn, fødselsnummer og adresse. " +
                    "Bruk gjerne skjemaet du finn på ${Constants.KLAGE_URL}. " +
                    "Du kan logge på og sende klaga via nettsida vår, ${Constants.NAV_URL}, " +
                    "eller du kan sende ho per post til ",
            English to "The appeal must be made in writing and contain your name, national identity number and address. " +
                    "You can use the form that you will find online: ${Constants.Engelsk.KLAGE_URL}. " +
                    "The appeal can be submitted by logging in to our website (${Constants.NAV_URL}) " +
                    "or sending it to us by conventional mail to  "
        )
    }
    postadresse(bosattUtland)
    paragraph {
        text(
            Bokmal to "Hvis du velger å sende klagen " +
                    "per post, må du huske å undertegne den. Hvis klagen ikke er undertegnet, vil vi dessverre " +
                    "måtte returnere den til deg.",
            Nynorsk to "Dersom du vel å sende klaga per post, må du hugse å signere henne. " +
                    "Dersom klaga ikkje er signert, vil du diverre få henne i retur.",
            English to "If you choose to mail your appeal, please remember to sign it. " +
                    "If your appeal is not signed, we will unfortunately have to return it to you."
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.klagePaaVedtaket() {
    title2 {
        text(
            Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
            Nynorsk to "Klage på vedtaket – folketrygdlova § 21-12",
            English to "Appealing decisions – Section 21-12 of the National Insurance Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har " +
                    "fattet vedtaket vil da vurdere saken din på nytt. Hvis du ikke får gjennomslag for klagen din, " +
                    "blir den sendt videre til Nav klageinstans for ny vurdering og avgjørelse. Dersom du heller " +
                    "ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
            Nynorsk to "Du kan klage på vedtaket innan seks veker frå du får det. Kontoret som " +
                    "fatta vedtaket, vil då vurdere saka di på nytt. Dersom du ikkje får gjennomslag for klaga di, " +
                    "blir ho send vidare til Nav klageinstans for ny vurdering og avgjerd. " +
                    "Dersom du ikkje får gjennomslag hos klageinstansen heller, kan du anke saka inn for Trygderetten.",
            English to "You may appeal a decision within six weeks of receiving it. The office that " +
                    "made the decision will then reconsider your case. If your appeal is not successful, " +
                    "it will be forwarded to the Nav appeals for reconsideration and decision. " +
                    "If your appeal is also rejected by Nav appeals, " +
                    "you can appeal to the National Insurance Court (Trygderetten)."
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.hjelpFraAndre() {
    title2 {
        text(
            Bokmal to "Hjelp fra andre - forvaltningsloven § 12",
            Nynorsk to "Hjelp frå andre – forvaltingslova § 12",
            English to "Help from others – Section 12 of the Public Administration Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, " +
                    "rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som " +
                    "hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne " +
                    "skjemaet du finner på ${Constants.FULLMAKT_URL}.",
            Nynorsk to "Du har under heile saksbehandlinga høve til å be om hjelp frå til dømes advokat, " +
                    "rettshjelpar, organisasjonar du er medlem av, eller andre myndige personar. Dersom personen som " +
                    "hjelper deg, ikkje er advokat, må du gi vedkomande ei skriftleg fullmakt. Bruk gjerne " +
                    "skjemaet du finn på ${Constants.FULLMAKT_URL}.",
            English to "You can ask for help from others throughout case processing, such as an attorney, " +
                    "legal aid, an organization of which you are a member or another person of legal age. " +
                    "If the person helping you is not an attorney, you must give this person a written power of attorney. " +
                    "Feel free to use the form you find here: ${Constants.Engelsk.FULLMAKT_URL}."
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.veiledning() {
    title2 {
        text(
            Bokmal to "Veiledning fra Nav - forvaltningsloven § 11",
            Nynorsk to "Rettleiing frå Nav – forvaltingslova § 11",
            English to "Guidance from Nav – Section 11 of the Public Administration Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                    "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                    "vårt beste for å hjelpe deg.",
            Nynorsk to "Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, " +
                    "under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, vil vi gjere " +
                    "vårt beste for å hjelpe deg.",
            English to "We have a duty to advise you of your rights and obligations in your case – before, " +
                    "during and after the case has been processed. " +
                    "If you have any questions or are unsure about anything, we will do our best to help you."
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.tilbakekreving() {
	title2 {
		text(
			Bokmal to "Tilbakekreving",
			Nynorsk to "Innkrevjing",
			English to "Recovery"
		)
	}
	paragraph {
		text(
			Bokmal to "Du må som hovedregel begynne å betale tilbake når du får fakturaen, selv om du klager på vedtaket. Dette framgår av forvaltningsloven § 42 med tilhørende rundskriv.",
			Nynorsk to "Sjølv om du klagar på vedtaket, må du som hovudregel byrje å betale tilbake når du får fakturaen. Dette går fram av forvaltingslova § 42 med tilhøyrande rundskriv.",
			English to "As a general rule, you must start repaying what you owe when you receive the invoice, even if you appeal the decision. " +
                    "This is stated in Section 42 of the Public Administration Act and associated circulars."
		)
	}
	paragraph {
		text(
			Bokmal to "Nav kan av eget tiltak bestemme at tilbakekrevingen skal utsettes til klagen er behandlet, " +
					"for eksempel hvis vi ser at det er sannsynlig at det påklagede vedtaket blir omgjort. " +
					"Du kan også søke om utsettelse av tilbakebetaling til klagen er behandlet. " +
					"Vi gjør deg oppmerksom på at det ikke gis utsettelse bare av økonomiske grunner.",
			Nynorsk to "Nav kan etter eige tiltak bestemme at innkrevjinga skal utsetjast fram til klaga er behandla. " +
                    "Dette kan vere aktuelt til dømes dersom vi forventar at vedtaket vil bli gjort om. " +
                    "Du kan også søkje om å få utsett tilbakebetalinga fram til klaga er behandla. " +
                    "Vi gjer merksam på at det ikkje blir gitt utsetjing av reint økonomiske grunnar.",
			English to "Nav may, on its own initiative, decide that recovery of owed payments shall be postponed " +
                    "until the appeal has been processed, for example if we see that it is likely that the appealed decision will be reversed. " +
                    "You may also apply for a repayment extension until the appeal has been processed. " +
                    "Please be aware that deferments are not granted merely for financial reasons."
		)
	}
}