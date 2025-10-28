package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
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
): AttachmentTemplate<LangBokmalNynorskEnglish, EmptyVedleggData> {
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

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, EmptyVedleggData>.duKanFaaDekketUtgifter() {
    title2 {
        text(
            bokmal { +"Du kan få dekket utgifter" },
            nynorsk { +"Du kan få dekt utgifter" },
            english { +"You may be reimbursed for expenses" }
        )
    }
    paragraph {
        text(
            bokmal { +"Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige " +
                    "for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. " +
                    "Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller Nav." },
            nynorsk { +"Dersom du får medhald, kan du få dekt vesentlege utgifter som har vore nødvendige " +
                    "for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. " +
                    "Statsforvaltaren, ein advokat eller Nav kan gi deg meir informasjon om denne ordninga." },
            english { +"If your appeal is accepted and the decision reversed, " +
                    "you may be reimbursed for substantial expenses that have been necessary to amend the decision. " +
                    "You may be entitled to free legal aid pursuant to the Legal Aid Act. " +
                    "Information about this scheme can be obtained from the County Governor, an attorney or Nav." }
        )
    }
    paragraph {
        text(
            bokmal { +"Du kan lese om saksomkostninger i forvaltningsloven § 36." },
            nynorsk { +"Sakskostnader er beskrive nærmare i forvaltingslova § 36." },
            english { +"You can read about legal costs in Section 36 of the Public Administration Act." }
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, EmptyVedleggData>.hvaMaaKlagenInneholde(
    bosattUtland: Expression<Boolean>
) {
    title2 {
        text(
            bokmal { +"Hva må klagen inneholde?" },
            nynorsk { +"Kva klaga skal innehalde" },
            english { +"What must the appeal contain?" }
        )
    }
    paragraph {
        text(
            bokmal { +"Du må skrive" },
            nynorsk { +"Du må opplyse" },
            english { +"You must tell us" }
        )
        list {
            item {
                text(
                    bokmal { +"hvilket vedtak du klager på" },
                    nynorsk { +"kva vedtak du klagar på" },
                    english { +"which decision you are appealing against" }
                )
            }
            item {
                text(
                    bokmal { +"hvilken endring i vedtaket du ber om" },
                    nynorsk { +"kva endring du ønskjer i vedtaket" },
                    english { +"what change in the decision you are requesting" }
                )
            }
        }
    }
    paragraph {
        text(
            bokmal { +"Du bør også" },
            nynorsk { +"I tillegg bør du" },
            english { +"You should also" }
        )
        list {
            item {
                text(
                    bokmal { +"skrive hvorfor du mener vedtaket er feil" },
                    nynorsk { +"grunngi kvifor du meiner vedtaket er feil" },
                    english { +"explain why you think the decision is wrong" }
                )
            }
            item {
                text(
                    bokmal { +"nevne erklæringer og andre dokumenter som du legger ved klagen" },
                    nynorsk { +"nemne erklæringar og andre dokument du legg ved klaga" },
                    english { +"list any declarations and other documents that you enclose with your appeal" }
                )
            }
        }
    }
    paragraph {
        text(
            bokmal { +"Nav kan hjelpe deg med å skrive ned klagen. Trenger du hjelp, er du velkommen til å " +
                    "ringe oss på telefon " },
            nynorsk { +"Nav kan hjelpe deg med å skrive ned klaga. Viss du treng hjelp, " +
                    "må du gjerne ringje oss på telefon " },
            english { +"The Nav office can help you with the wording of your appeal. " +
                    "If you need help, you are welcome to call us by phone " }
        )
        kontakttelefonPensjon(bosattUtland)
        text(
            bokmal { +"." },
            nynorsk { +"." },
            english { +"." }
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, EmptyVedleggData>.hvordanSendeKlage(
    bosattUtland: Expression<Boolean>
) {
    title2 {
        text(
            bokmal { +"Hvordan sende inn klage?" },
            nynorsk { +"Slik sender du inn ei klage" },
            english { +"How do I file an appeal?" }
        )
    }
    paragraph {
        text(
            bokmal { +"Klagen må være skriftlig og inneholde ditt navn, fødselsnummer og adresse. " +
                    "Du kan benytte skjemaet som du finner på ${Constants.KLAGE_URL}. Klagen kan sendes via " +
                    "innlogging på nettsiden vår, ${Constants.NAV_URL}, eller sendes til oss i posten til " },
            nynorsk { +"Du må setje fram klaga skriftleg og oppgi namn, fødselsnummer og adresse. " +
                    "Bruk gjerne skjemaet du finn på ${Constants.KLAGE_URL}. " +
                    "Du kan logge på og sende klaga via nettsida vår, ${Constants.NAV_URL}, " +
                    "eller du kan sende ho per post til " },
            english { +"The appeal must be made in writing and contain your name, national identity number and address. " +
                    "You can use the form that you will find online: ${Constants.Engelsk.KLAGE_URL}. " +
                    "The appeal can be submitted by logging in to our website (${Constants.NAV_URL}) " +
                    "or sending it to us by conventional mail to  " }
        )
    }
    postadresse(bosattUtland)
    paragraph {
        text(
            bokmal { +"Hvis du velger å sende klagen " +
                    "per post, må du huske å undertegne den. Hvis klagen ikke er undertegnet, vil vi dessverre " +
                    "måtte returnere den til deg." },
            nynorsk { +"Dersom du vel å sende klaga per post, må du hugse å signere henne. " +
                    "Dersom klaga ikkje er signert, vil du diverre få henne i retur." },
            english { +"If you choose to mail your appeal, please remember to sign it. " +
                    "If your appeal is not signed, we will unfortunately have to return it to you." }
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, EmptyVedleggData>.klagePaaVedtaket() {
    title2 {
        text(
            bokmal { +"Klage på vedtaket - folketrygdloven § 21-12" },
            nynorsk { +"Klage på vedtaket – folketrygdlova § 21-12" },
            english { +"Appealing decisions – Section 21-12 of the National Insurance Act" }
        )
    }
    paragraph {
        text(
            bokmal { +"Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har " +
                    "fattet vedtaket vil da vurdere saken din på nytt. Hvis du ikke får gjennomslag for klagen din, " +
                    "blir den sendt videre til Nav klageinstans for ny vurdering og avgjørelse. Dersom du heller " +
                    "ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten." },
            nynorsk { +"Du kan klage på vedtaket innan seks veker frå du får det. Kontoret som " +
                    "fatta vedtaket, vil då vurdere saka di på nytt. Dersom du ikkje får gjennomslag for klaga di, " +
                    "blir ho send vidare til Nav klageinstans for ny vurdering og avgjerd. " +
                    "Dersom du ikkje får gjennomslag hos klageinstansen heller, kan du anke saka inn for Trygderetten." },
            english { +"You may appeal a decision within six weeks of receiving it. The office that " +
                    "made the decision will then reconsider your case. If your appeal is not successful, " +
                    "it will be forwarded to the Nav appeals for reconsideration and decision. " +
                    "If your appeal is also rejected by Nav appeals, " +
                    "you can appeal to the National Insurance Court (Trygderetten)." }
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, EmptyVedleggData>.hjelpFraAndre() {
    title2 {
        text(
            bokmal { +"Hjelp fra andre - forvaltningsloven § 12" },
            nynorsk { +"Hjelp frå andre – forvaltingslova § 12" },
            english { +"Help from others – Section 12 of the Public Administration Act" }
        )
    }
    paragraph {
        text(
            bokmal { +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, " +
                    "rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som " +
                    "hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne " +
                    "skjemaet du finner på ${Constants.FULLMAKT_URL}." },
            nynorsk { +"Du har under heile saksbehandlinga høve til å be om hjelp frå til dømes advokat, " +
                    "rettshjelpar, organisasjonar du er medlem av, eller andre myndige personar. Dersom personen som " +
                    "hjelper deg, ikkje er advokat, må du gi vedkomande ei skriftleg fullmakt. Bruk gjerne " +
                    "skjemaet du finn på ${Constants.FULLMAKT_URL}." },
            english { +"You can ask for help from others throughout case processing, such as an attorney, " +
                    "legal aid, an organization of which you are a member or another person of legal age. " +
                    "If the person helping you is not an attorney, you must give this person a written power of attorney. " +
                    "Feel free to use the form you find here: ${Constants.Engelsk.FULLMAKT_URL}." }
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, EmptyVedleggData>.veiledning() {
    title2 {
        text(
            bokmal { +"Veiledning fra Nav - forvaltningsloven § 11" },
            nynorsk { +"Rettleiing frå Nav – forvaltingslova § 11" },
            english { +"Guidance from Nav – Section 11 of the Public Administration Act" }
        )
    }
    paragraph {
        text(
            bokmal { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                    "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                    "vårt beste for å hjelpe deg." },
            nynorsk { +"Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, " +
                    "under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, vil vi gjere " +
                    "vårt beste for å hjelpe deg." },
            english { +"We have a duty to advise you of your rights and obligations in your case – before, " +
                    "during and after the case has been processed. " +
                    "If you have any questions or are unsure about anything, we will do our best to help you." }
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, EmptyVedleggData>.tilbakekreving() {
	title2 {
		text(
			bokmal { +"Tilbakekreving" },
			nynorsk { +"Innkrevjing" },
			english { +"Recovery" }
		)
	}
	paragraph {
		text(
			bokmal { +"Du må som hovedregel begynne å betale tilbake når du får fakturaen, selv om du klager på vedtaket. Dette framgår av forvaltningsloven § 42 med tilhørende rundskriv." },
			nynorsk { +"Sjølv om du klagar på vedtaket, må du som hovudregel byrje å betale tilbake når du får fakturaen. Dette går fram av forvaltingslova § 42 med tilhøyrande rundskriv." },
			english { +"As a general rule, you must start repaying what you owe when you receive the invoice, even if you appeal the decision. " +
                    "This is stated in Section 42 of the Public Administration Act and associated circulars." }
		)
	}
	paragraph {
		text(
			bokmal { +"Nav kan av eget tiltak bestemme at tilbakekrevingen skal utsettes til klagen er behandlet, " +
					"for eksempel hvis vi ser at det er sannsynlig at det påklagede vedtaket blir omgjort. " +
					"Du kan også søke om utsettelse av tilbakebetaling til klagen er behandlet. " +
					"Vi gjør deg oppmerksom på at det ikke gis utsettelse bare av økonomiske grunner." },
			nynorsk { +"Nav kan etter eige tiltak bestemme at innkrevjinga skal utsetjast fram til klaga er behandla. " +
                    "Dette kan vere aktuelt til dømes dersom vi forventar at vedtaket vil bli gjort om. " +
                    "Du kan også søkje om å få utsett tilbakebetalinga fram til klaga er behandla. " +
                    "Vi gjer merksam på at det ikkje blir gitt utsetjing av reint økonomiske grunnar." },
			english { +"Nav may, on its own initiative, decide that recovery of owed payments shall be postponed " +
                    "until the appeal has been processed, for example if we see that it is likely that the appealed decision will be reversed. " +
                    "You may also apply for a repayment extension until the appeal has been processed. " +
                    "Please be aware that deferments are not granted merely for financial reasons." }
		)
	}
}