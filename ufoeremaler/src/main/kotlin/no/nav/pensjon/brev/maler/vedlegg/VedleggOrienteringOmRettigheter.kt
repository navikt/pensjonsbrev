package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.Constants.FULLMAKT_URL
import no.nav.pensjon.brev.maler.fraser.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_KONTAKTSENTER_TELEFON_UFORE
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.telefonnummer
import no.nav.pensjon.brevbaker.api.model.Telefonnummer


// VedleggPlikter_001, VedleggPlikterUT_001
object VedleggPlikter : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Plikt til å opplyse om endringer - folketrygdloven § 21-3" },
                nynorsk { + "Plikt til å opplyse om endringar - folketrygdlova § 21-3" },
                english { + "Duty to inform of changes - Section 21-3 of the National Insurance Act" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du må gi oss beskjed hvis" },
                nynorsk { + "Du må gi oss beskjed hvis" },
                english { + "You must inform us if" }
            )
        }
    }
}


object VedleggPlikterEndretSivilstatus : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "sivilstanden din endrer seg" },
            nynorsk { + "sivilstanden din endrar seg" },
            english { + "your civil status changes" },
        )
    }
}

// VedleggPlikterUT_001
object VedleggPlikterUT1 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "inntekten din endrer seg. Du kan informere Nav om endret inntekt ved å bruke selvbetjeningsløsningen på $NAV_URL" },
            nynorsk { + "inntekta di endrar seg. Du kan informere Nav om endra inntekt ved å bruke sjølvbeteningsløysninga på $NAV_URL" },
            english { + "your income changes. You can notify Nav of changes in your income by using the online service at $NAV_URL" }
        )
}

// VedleggPlikterUT2_001
object VedleggPlikterUT2 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(bokmal { + "du endrer adresse" }, nynorsk { + "du endrar adresse" }, english { + "you change address" })
}

// VedleggPlikterUT3_001
object VedleggPlikterUT3 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du skal begynne å arbeide i utlandet" },
            nynorsk { + "du skal begynne å arbeide i utlandet" },
            english { + "you will start working abroad" }
        )
}

// VedleggPlikterUT4_001
object VedleggPlikterUT4 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du skal oppholde deg utenfor Norge lengre enn seks måneder" },
            nynorsk { + "du skal opphalde deg utanfor Noreg lengre enn seks månader" },
            english { + "you intend to stay outside Norway for more than six months" }
        )
}

// VedleggPlikterUT5_001
object VedleggPlikterUT5 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du skal flytte til et annet land" },
            nynorsk { + "du flyttar til eit anna land" },
            english { + "you are moving to another country" }
        )
}

object VedleggPlikterEndretInntektBarnetillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "din ektefelle, partner eller samboers inntekt endrer seg og du har barnetillegg for felles barn" },
            nynorsk { + "inntekta til ektefellen, partnaren eller sambuaren din endrar seg, og du har barnetillegg for felles barn" },
            english { + "the income of your spouse, partner or cohabitant changes, and you are receiving a child supplement for your joint child(ren)" },
        )
    }
}

// VedleggPlikterUT7_001
data class VedleggPlikterUT7(
    val harTilleggForFlereBarn: Expression<Boolean>
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "du forsørger barn og det skjer endringer i omsorgssituasjonen" },
            nynorsk { + "du forsørgjer barn og det skjer endringar av omsorgsituasjonen " },
            english { + "you support children and there are changes in the care situation" },
        )
    }
}

// VedleggPlikterUT8_001
object VedleggPlikterUT8 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "tjenestepensjon fra offentlig eller private ordninger endrer seg" },
            nynorsk { + "tenestepensjon frå offentlege eller private ordningar endrar seg" },
            english { + "your occupational pension from public or private schemes changes" }
        )
}

// VedleggPlikterUT9_001
object VedleggPlikterUT9 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "individuelle pensjonsordninger, livrente og gavepensjon endrer seg" },
            nynorsk { + "individuelle pensjonsordningar, livrente og gåvepensjon endrar seg" },
            english { + "your individual pension scheme, annuity or gratuity pension changes" }
        )
}

// VedleggPlikterUT10_001
object VedleggPlikterUT10 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "ytelser og pensjon fra andre land endrer seg" },
            nynorsk { + "ytingar og pensjon frå andre land endrar seg" },
            english { + "benefits and pensions from other countries change" }
        )
}

// VedleggPlikterUT11_001
object VedleggPlikterUT11 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du blir innlagt på institusjon" },
            nynorsk { + "du blir innlagd på institusjon" },
            english { + "you are admitted to an institution" }
        )
}

// VedleggPlikterUT12_001
object VedleggPlikterUT12 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du sitter i varetekt, soner straff eller er under forvaring" },
            nynorsk { + "du sit i varetekt, sonar straff eller er under forvaring" },
            english { + "you are remanded in custody or serving time in prison or preventive custody" }
        )
}

// VedleggPlikterUT13_001
data class VedleggPlikterUT13(
    val harTilleggForFlereBarn: Expression<Boolean>
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val barnFlertall = harTilleggForFlereBarn
        text(
            bokmal { + ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørger skal flytte til et annet land" },
            nynorsk { + ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørgjer skal flytte til eit anna land" },
            english { + ifElse(
                barnFlertall,
                "children in your care move",
                "the child in your care moves"
            ) + " to another country" }
        )
    }
}

// VedleggPlikterUT14_001
data class VedleggPlikterUT14(
    val harTilleggForFlereBarn: Expression<Boolean>
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val barnFlertall = harTilleggForFlereBarn
        text(
            bokmal { + ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørger skal oppholde seg i et annet land mer enn 90 dager i løpet av en tolvmånedersperiode" },
            nynorsk { + ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørgjer skal opphalde seg i eit anna land i meir enn 90 dagar i løpet av ein tolv månedars periode" },
            english { + ifElse(
                barnFlertall,
                "children in your care stay",
                "the child in your care stays"
            ) + " in another country for more than 90 days in a 12 month period" }
        )
    }
}

// VedleggPlikterAFP_001
object VedleggPlikterAFP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Plikt til å opplyse om endringer" },
                nynorsk { + "Plikt til å opplyse om endringar" },
                english { + "Duty to inform of changes" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du må melde fra til Nav om endringer som har betydning for størrelsen på pensjonen din. Du må alltid melde fra dersom" },
                nynorsk { + "Du må melde frå til Nav om endringar som har noko å seie for storleiken på pensjonen din. Du må alltid melde frå dersom" },
                english { + "You must notify Nav of changes that may be important for your pension. You must always notify us if" }
            )
        }
    }
}

// VedleggPlikterAFP1_001
object VedleggPlikterAFP1 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "inntekten din endrer seg" },
            nynorsk { + "inntekta di endrar seg" },
            english { + "your income changes" }
        )
}

// VedleggPlikterAFP2_001
object VedleggPlikterAFP2 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du gifter deg eller inngår samboerskap" },
            nynorsk { + "du giftar deg eller inngår sambuarskap" },
            english { + "you get married or get a cohabitant" }
        )
}

// VedleggPlikterAFP3_001
object VedleggPlikterAFP3 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land" },
            nynorsk { + "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land" },
            english { + "you intend to stay outside Norway for a long period or intend to move to another country" }
        )
}

// VedleggPlikterAFP4_001
object VedleggPlikterAFP4 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland" },
            nynorsk { + "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no" },
            english { + "you move to another country, move back to Norway, or you change address in your current country of residence" }
        )
}


// VedleggVeiledning_001
object VedleggVeiledning : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Veiledning fra Nav - forvaltningsloven § 11" },
                nynorsk { + "Rettleiing frå Nav - forvaltningslova § 11" },
                english { + "Guidance from Nav - Section 11 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal { + "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre vårt beste for å hjelpe deg." },
                nynorsk { + "Vi har plikt til å rettleie deg om rettane og pliktene dine i saka di, både før, under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, vil vi gjere vårt beste for å hjelpe deg." },
                english { + "We have a duty to inform you of your rights and obligations in connection with your case, both before, during and after the administrative process. If you have any questions or are uncertain about something, we will do our best to help you." }
            )
        }
    }
}

// TODO: Hvorfor har vi to versjoner av Innsyn (VedleggInnsynSakPensjon og VedleggInnsynSakUfoeretrygdPesys)

// VedleggInnsynSakPensjon_001
data class VedleggInnsynSakPensjon(val telefonnummer: Expression<Telefonnummer>, val nettside: Expression<String>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Innsyn i saken din - forvaltningsloven § 18" },
                nynorsk { + "Innsyn i saka di - forvaltningslova § 18" },
                english { + "Access to your case - Section 18 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal { + "Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn på " + nettside + " for å se all kommunikasjon som har vært mellom deg og Nav i saken din. Du kan også ringe oss på telefon " + telefonnummer.format() + "." },
                nynorsk { + "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan logge deg inn på " + nettside + " for å sjå all kommunikasjon som har vore mellom deg og Nav i saka di. Du kan også ringje oss på telefon " + telefonnummer.format() + "." },
                english { + "With some exceptions, you are entitled to access all the documents relating to your case. Log on to " + nettside + " to review the communication between you and Nav in connection with your case. You can also call us at tel.: " + telefonnummer.format() + "." }
            )
        }
    }
}

// VedleggInnsynSakUTPesys_001
object VedleggInnsynSakUfoeretrygdPesys : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Innsyn i saken din - forvaltningsloven § 18" },
                nynorsk { + "Innsyn i saka di - forvaltningslova § 18" },
                english { + "Access to your case - Section 18 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal { + "Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn via $NAV_URL for å se dokumenter i saken din. Du kan også ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_UFORE." },
                nynorsk { + "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan logge deg inn via $NAV_URL for å sjå dokumenter i saka di. Du kan også ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_UFORE." },
                english { + "With some exceptions, you are entitled to access the documents relating to your case. Log on to $NAV_URL to review the documents in connection with your case. You can also call us at telephone +47 $NAV_KONTAKTSENTER_TELEFON_UFORE." },
            )
        }
    }
}

// TODO: Dette er formuleringa brukt for VedleggInnsynSakUTPesys_001 i vedlegg 1 og vedlegg 2 i doksys
// Denne bør eigentleg samkøyrast med den over, dei er _nesten_ like, men ikkje heilt.
object VedleggInnsynSakUfoeretrygdPesysNoenDokumenter : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Innsyn i saken din - forvaltningsloven § 18" },
                nynorsk { + "Innsyn i saka di - forvaltningslova § 18" },
                english { + "Access to your case - Section 18 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal { + "Med få unntak har du rett til å se dokumentene i saken din. Du kan se noen dokumenter på ${DITT_NAV}. Du kan også ringe oss på telefon " + felles.avsenderEnhet.telefonnummer.format() + "." },
                nynorsk { + "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan sjå nokre dokument på ${DITT_NAV}. Du kan også ringe oss på telefon " + felles.avsenderEnhet.telefonnummer.format() + "." },
                english { + "With some exceptions, you are entitled to access all the documents pertaining to your case. You can read some of the documents at ${DITT_NAV}. You can also call us at tel.: " + felles.avsenderEnhet.telefonnummer.format() + "." },
                )
        }
    }
}

// VedleggHjelpFraAndre_001
object VedleggHjelpFraAndre : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Hjelp fra andre - forvaltningsloven § 12" },
                nynorsk { + "Hjelp frå andre - forvaltningslova § 12" },
                english { + "Assistance from a third party - Section 12 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne skjemaet du finner på $FULLMAKT_URL." },
                nynorsk { + "Du kan be om hjelp frå andre under heile saksbehandlinga, for eksempel av advokat, rettshjelpar, ein organisasjon du er medlem av, eller ein annan myndig person. Dersom den som hjelper deg, ikkje er advokat, må du gi denne personen ei skriftleg fullmakt. Bruk gjerne skjemaet du finn på $FULLMAKT_URL." },
                english { + "You can use the assistance of a third party throughout the administrative process, e.g. from a lawyer, legal services provider, an organisation of which you are a member, or another competent person. If the person assisting you is not a lawyer, you must issue a written power of attorney authorising them to help you. You can use the form found on $FULLMAKT_URL." }
            )
        }
    }
}

// TODO: Hvorfor er det forskjell på VedleggKlagePensjon og VedleggKlagePesys

// VedleggKlagePensjon_001
data class VedleggKlagePaaVedtaket(val telefonnummer: Expression<Telefonnummer>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Klage på vedtaket - folketrygdloven § 21-12" },
                nynorsk { + "Klage på vedtaket - folketrygdlova § 21-12" },
                english { + "Appealing a decision - Section 21-12 of the National Insurance Act" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket vil da vurdere saken din på nytt." },
                nynorsk { + "Du kan klage på vedtaket innan seks veker frå du fekk det. Kontoret som har gjort vedtaket, vurderer då saka di på nytt." },
                english { + "You may appeal the decision within six weeks of receiving it. The department that made the decision will then review your case." }
            )
        }
        paragraph {
            text(
                bokmal { + "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til Nav klageinstans for ny vurdering og avgjørelse. Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten." },
                nynorsk { + "Dersom du ikkje får gjennomslag for klaga di, blir ho send vidare til Nav klageinstans for ny vurdering og avgjerd. Dersom du heller ikkje får gjennomslag hos klageinstansen, kan du anke saka inn for Trygderetten." },
                english { + "If your complaint is declined, it will be forwarded to Nav Appeals for a new review and decision. If this review is also unsuccessful, you may appeal to The National Insurance Court." }
            )
        }
        paragraph {
            text(
                bokmal { + "Klagen må inneholde navn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finner på $KLAGE_URL. Trenger du hjelp, er du velkommen til å ringe oss på telefon " + telefonnummer.format() + "." },
                nynorsk { + "Klaga må innehalde namn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finn på $KLAGE_URL. Treng du hjelp, er du velkomen til å ringje oss på telefon " + telefonnummer.format() + "." },
                english { + "Your appeal must include your name, national identity number and address. Feel free to use the form found at $KLAGE_URL. Should you need assistance in writing the appeal, please call us at tel.: " + telefonnummer.format() + "." }
            )
        }
        paragraph {
            text(bokmal { + "Du må skrive" }, nynorsk { + "Du må skrive" }, english { + "You must specify" })
            list {
                item {
                    text(
                        bokmal { + "hvilket vedtak du klager på" },
                        nynorsk { + "kva vedtak du klagar på" },
                        english { + "which decision you are appealing" }
                    )
                }
                item {
                    text(
                        bokmal { + "hvilken endring i vedtaket du ber om" },
                        nynorsk { + "kva endring i vedtaket du ber om" },
                        english { + "how you believe the decision should be amended" }
                    )
                }
            }
        }
        paragraph {
            text(bokmal { + "Du bør også" }, nynorsk { + "Du bør også" }, english { + "You should also" })
            list {
                item {
                    text(
                        bokmal { + "skrive hvorfor du mener vedtaket er feil" },
                        nynorsk { + "skrive kvifor du meiner vedtaket er feil" },
                        english { + "specify why you believe the decision is wrong" }
                    )
                }
                item {
                    text(
                        bokmal { + "nevne erklæringer og andre dokumenter som du legger ved klagen" },
                        nynorsk { + "nemne erklæringar og andre dokument som du legg ved klaga" },
                        english { + "list statements and other documents attached to the appeal" }
                    )
                }
            }
        }

        paragraph {
            text(
                bokmal { + "Husk å undertegne klagen, ellers må vi sende den i retur til deg." },
                nynorsk { + "Hugs å skrive under klaga, elles må vi sende henne i retur til deg." },
                english { + "Please remember to sign the appeal, otherwise it will be returned to you." }
            )
        }
        paragraph {
            text(
                bokmal { + "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller Nav." },
                nynorsk { + "Får du medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. Informasjon om denne ordninga kan du få hos statsforvalteren, advokatar eller Nav." },
                english { + "If your appeal is successful, you may be eligible for compensation for costs incurred to have the decision overturned. You may also be eligible for free legal aid, pursuant to the Legal Aid Act. Information about the legal aid scheme can be obtained from the county governor, lawyers or Nav." }
            )
        }
        paragraph {
            text(
                bokmal { + "Du kan lese om saksomkostninger i forvaltningsloven § 36." },
                nynorsk { + "Du kan lese om saksomkostningar i forvaltningslova § 36." },
                english { + "Read more about costs of action in Section 36 of the Public Administration Act." }
            )
        }
    }
}



