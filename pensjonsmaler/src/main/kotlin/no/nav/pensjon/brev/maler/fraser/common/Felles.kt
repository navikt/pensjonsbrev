package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.navn



object Felles {

    /**
     * TBU1074, TBU2242NB, TBU1075NN, TBU2242EN, RettTilInnsynPesys_001
     */
    data class RettTilInnsyn(val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn",
                    English to "You have the right to access your file",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg ",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg ",
                    English to "You are entitled to see your case documents. Refer to the attachment ",
                )
                namedReference(vedlegg)
                text(
                    Bokmal to " for informasjon om hvordan du går fram.",
                    Nynorsk to " for informasjon om korleis du går fram.",
                    English to " for information about how to proceed.",
                )
            }
        }
    }

    data class HarDuSpoersmaal(val merInformasjonUrl: String, val telefonnummer: String) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på $merInformasjonUrl."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon $telefonnummer,"
                            + " hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to "Du finn meir informasjon på $merInformasjonUrl."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Om du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon $telefonnummer,"
                            + " kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to "You can find more information at $merInformasjonUrl."
                            + " At ${Constants.KONTAKT_URL}, you can chat or write to us."
                            + " If you do not find the answer at ${Constants.NAV_URL}, you can call us at: +47 $telefonnummer,"
                            + " weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                )
            }
        }

        companion object {
            val ufoeretrygd = HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON)
            val omsorg = HarDuSpoersmaal(Constants.OMSORGSOPPTJENING_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON)
            val alder = HarDuSpoersmaal(Constants.PENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON)
        }
    }

    // TBU2213, TBU1100, RettTilKlagePesys_001
    // TBU2452NN, TBU2452EN, TBU2452
    data class RettTilAAKlage(val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right of appeal"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget ",
                    Nynorsk to "Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget ",
                    English to "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. In the attachment ",
                )
                namedReference(vedlegg)
                text(
                    Bokmal to " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Nynorsk to " får du vite meir om korleis du går fram. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                    English to ", you can find out more about how to proceed. You will find forms and information at ${Constants.KLAGE_URL}.",
                )
            }
        }
    }

    data class MaanederText(val antall: Expression<Int>) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            textExpr(
                Bokmal to antall.format() + " måneder",
                Nynorsk to antall.format() + " måneder",
                English to antall.format() + " months"
            )
    }

    data class TextOrList(val foedselsdatoer: Expression<Collection<String>>, val limit: Int = 2) :
        ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(foedselsdatoer.size().lessThanOrEqual(limit)) {
                val foedselsDato = foedselsdatoer.format()
                textExpr(
                    Bokmal to " ".expr() + foedselsDato + ".",
                    Nynorsk to " ".expr() + foedselsDato + ".",
                    English to " ".expr() + foedselsDato + ".",
                )
            }.orShow {
                text(Bokmal to ":", Nynorsk to ":", English to ":")
                list {
                    forEach(foedselsdatoer) {
                        item { textExpr(Bokmal to it, Nynorsk to it, English to it) }
                    }
                }
            }
        }
    }

    fun Expression<Bruker>.fulltNavn(): Expression<String> =
        Expression.UnaryInvoke(
            value = this,
            operation = UnaryOperation.BrukerFulltNavn
        )

    object ReturTilEtterstadOslo : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to felles.avsenderEnhet.navn,
                    Nynorsk to felles.avsenderEnhet.navn,
                    English to felles.avsenderEnhet.navn
                )
                newline()
                textExpr(
                    Bokmal to "Postboks 6600 Etterstad".expr(),
                    Nynorsk to "Postboks 6600 Etterstad".expr(),
                    English to "Postboks 6600 Etterstad".expr(),
                )
                newline()
                textExpr(
                    Bokmal to "0607 Oslo".expr(),
                    Nynorsk to "0607 Oslo".expr(),
                    English to "0607 Oslo, Norway".expr(),
                )
            }
        }
    }

    object MeldeFraEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Meld fra om endringer",
                    Nynorsk to "Meld frå om endringar",
                    Language.English to "Duty to report changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må melde fra til oss med en gang hvis det skjer endringer som kan ha betydning for saken din, "
                            + "for eksempel ved endring av sivilstand eller ved flytting.",
                    Nynorsk to "Du må melde frå til oss med ein gong dersom det skjer endringar som kan ha noko å seie for saka din, "
                            + "for eksempel ved endring av sivilstand eller ved flytting.",
                    Language.English to "You must notify us immediately if there are any changes that may affect your case, "
                            + "such as a change in your marital status or if you move."
                )
            }
        }
    }

}