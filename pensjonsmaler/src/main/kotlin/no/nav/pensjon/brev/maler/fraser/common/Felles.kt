package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_OPEN_HOURS
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.lessThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.navn

object Felles {
    /**
     * TBU1074, TBU2242NB, TBU1075NN, TBU2242EN, RettTilInnsynPesys_001
     */
    data class RettTilInnsyn(
        val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                    nynorsk { + "Du har rett til innsyn" },
                    english { + "You have the right to access your file" },
                )
            }

            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg " },
                    nynorsk { + "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg " },
                    english { + "You are entitled to see your case documents. Refer to the attachment " },
                )
                namedReference(vedlegg)
                text(
                    bokmal { + " for informasjon om hvordan du går fram." },
                    nynorsk { + " for informasjon om korleis du går fram." },
                    english { + " for information about how to proceed." },
                )
            }
        }
    }

    data class HarDuSpoersmaal(
        val merInformasjonUrl: String,
        val telefonnummer: String,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Har du spørsmål?" },
                    nynorsk { + "Har du spørsmål?" },
                    english { + "Do you have questions?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du finner mer informasjon på $merInformasjonUrl." +
                        " På $KONTAKT_URL kan du chatte eller skrive til oss." +
                        " Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $telefonnummer" +
                        " hverdager kl. $NAV_KONTAKTSENTER_AAPNINGSTID." },
                    nynorsk { + "Du finn meir informasjon på $merInformasjonUrl." +
                        " På $KONTAKT_URL kan du chatte eller skrive til oss." +
                        " Om du ikkje finn svar på $NAV_URL, kan du ringe oss på telefon $telefonnummer," +
                        " kvardagar kl. $NAV_KONTAKTSENTER_AAPNINGSTID." },
                    english { + "You can find more information at $merInformasjonUrl." +
                        " At $KONTAKT_URL, you can chat or write to us." +
                        " If you do not find the answer at $NAV_URL, you can call us at: +47 $telefonnummer," +
                        " weekdays $NAV_KONTAKTSENTER_OPEN_HOURS." },
                )
            }
        }

        companion object {
            val familiepleie =
                HarDuSpoersmaal(Constants.FAMILIEPLEIER_URL, Constants.NAV_KONTAKTSENTER_TELEFON)
            val gjenlevende =
                HarDuSpoersmaal(
                    Constants.GJENLEVENDEPENSJON_URL,
                    Constants.NAV_KONTAKTSENTER_TELEFON,
                )
            val ufoeretrygd =
                HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON)
            val omsorg =
                HarDuSpoersmaal(
                    Constants.OMSORGSOPPTJENING_URL,
                    Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON,
                )
            val alder =
                HarDuSpoersmaal(Constants.PENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON)
        }
    }

    // TBU2213, TBU1100, RettTilKlagePesys_001
    // TBU2452NN, TBU2452EN, TBU2452
    data class RettTilAAKlage(
        val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til å klage" },
                    nynorsk { + "Du har rett til å klage" },
                    english { + "You have the right of appeal" },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget " },
                    nynorsk { + 
                        "Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget " },
                    english { + 
                        "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. In the attachment " },
                )
                namedReference(vedlegg)
                text(
                    bokmal { + " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på ${Constants.KLAGE_URL}." },
                    nynorsk { + " får du vite meir om korleis du går fram. Du finn skjema og informasjon på ${Constants.KLAGE_URL}." },
                    english { + 
                        ", you can find out more about how to proceed. You will find forms and information at ${Constants.KLAGE_URL}." },
                )
            }
        }
    }

    data class MaanederText(
        val antall: Expression<Int>,
    ) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            text(
                bokmal { + antall.format() + " måneder" },
                nynorsk { + antall.format() + " månadar" },
                english { + antall.format() + " months" },
            )
    }

    data class TextOrList(
        val foedselsdatoer: Expression<Collection<String>>,
        val limit: Int = 2,
    ) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(foedselsdatoer.size().lessThanOrEqual(limit)) {
                val foedselsDato = foedselsdatoer.format()
                text(
                    bokmal { + " " + foedselsDato + "." },
                    nynorsk { + " " + foedselsDato + "." },
                    english { + " " + foedselsDato + "." },
                )
            }.orShow {
                text(bokmal { + ":" }, nynorsk { + ":" }, english { + ":" })
                list {
                    forEach(foedselsdatoer) {
                        item { text(bokmal { + it }, nynorsk { + it }, english { + it }) }
                    }
                }
            }
        }
    }

    fun Expression<Bruker>.fulltNavn(): Expression<String> = UnaryOperation.BrukerFulltNavn(this)

    object ReturTilEtterstadOslo : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { + felles.avsenderEnhet.navn },
                    nynorsk { + felles.avsenderEnhet.navn },
                    english { + felles.avsenderEnhet.navn },
                )
                newline()
                text(
                    bokmal { + "Postboks 6600 Etterstad" },
                    nynorsk { + "Postboks 6600 Etterstad" },
                    english { + "Postboks 6600 Etterstad" },
                )
                newline()
                text(
                    bokmal { + "0607 Oslo" },
                    nynorsk { + "0607 Oslo" },
                    english { + "0607 Oslo, Norway" },
                )
            }
        }
    }

    object MeldeFraEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Meld fra om endringer" },
                    nynorsk { + "Meld frå om endringar" },
                    english { + "Duty to report changes" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du må melde fra til oss med en gang hvis det skjer endringer som kan ha betydning for saken din, " +
                        "for eksempel ved endring av sivilstand eller ved flytting." },
                    nynorsk { + "Du må melde frå til oss med ein gong dersom det skjer endringar som kan ha noko å seie for saka din, " +
                        "for eksempel ved endring av sivilstand eller ved flytting." },
                    english { + "You must notify us immediately if there are any changes that may affect your case, " +
                        "such as a change in your marital status or if you move." },
                )
            }
        }
    }

    object RettTilInnsynRedigerbarebrev : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                    nynorsk { + "Du har rett til innsyn" },
                    english { + "You have the right to access your file" },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Du har rett til å se dokumentene i saken din. Du kan logge deg inn via $NAV_URL for å se dokumenter i saken din." },
                    nynorsk { + 
                        "Du har rett til å sjå dokumenta i saka di. Du kan logge deg inn via $NAV_URL for å sjå dokumenta i saka di." },
                    english { + 
                        "You are entitled to see your case documents. You can log in via $NAV_URL to view documents related to your case." },
                )
            }
        }
    }

    object DuKanLeseMer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { + "Du kan lese mer om dette på $NAV_URL." },
                    nynorsk { + "Du kan lese meir om dette på $NAV_URL." },
                    english { + "You can read more about this at $NAV_URL." },
                )
            }
        }
    }

    object FlereBeregningsperioder : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { + "Du kan lese mer om andre beregningsperioder i vedlegget." },
                    nynorsk { + "Du kan lese meir om andre berekningsperiodar i vedlegget." },
                    english { + "There is more information about other calculation periods in the attachment." },
                )
            }
        }
    }
}
