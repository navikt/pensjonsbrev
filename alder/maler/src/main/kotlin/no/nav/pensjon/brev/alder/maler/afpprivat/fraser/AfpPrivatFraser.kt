package no.nav.pensjon.brev.alder.maler.afpprivat.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Felles fraser for AFP privat-brev.
 *
 * Brukes av [no.nav.pensjon.brev.alder.maler.afpprivat.InnvilgelseAvAfpInnhold],
 * [no.nav.pensjon.brev.alder.maler.afpprivat.AvslagAfpPrivat].
 */
object AfpPrivatFraser {

    /**
     * Standard innsyn-paragraf med henvisning til forvaltningsloven § 18.
     * Brukes uten egen tittel — kalleren plasserer den under en passende
     * `title1` («Dine rettigheter» i innvilgelse, «Din rett til innsyn og
     * klage» i avslag).
     */
    object InnsynForvaltningsloven18 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven " +
                            "paragraf 18."
                    },
                    nynorsk {
                        +"Du har som hovudregel rett til å sjå saksdokumenta etter føresegnene i forvaltingslova " +
                            "paragraf 18."
                    },
                    english {
                        +"As a main rule, you are entitled to see all case documents pursuant to section 18 of the " +
                            "Public Administration Act."
                    },
                )
            }
        }
    }

    /**
     * Standard klagerett-paragraf med henvisning til folketrygdloven § 21-12.
     * Brukes som siste paragraf under «Dine rettigheter» i AFP-vedtak hvor
     * brukeren har klagerett mot vedtaket (innvilgelse av AFP privat og
     * offentlig sektor).
     */
    object KlagerettFolketrygdloven2112 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket " +
                            "etter bestemmelsene i folketrygdloven paragraf 21-12. Fristen for å klage er seks uker " +
                            "fra du mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket ikkje er i samsvar med det du har søkt om, kan du klage på " +
                            "vedtaket etter føresegnene i folketrygdlova paragraf 21-12. Fristen for å klage er seks " +
                            "veker frå du får dette brevet."
                    },
                    english {
                        +"If you believe that the decision is not in accordance with what you applied for, you can " +
                            "appeal the decision pursuant to section 21-12 of the National Insurance Act. The time " +
                            "limit for filing an appeal is six weeks from the date you received this letter."
                    },
                )
            }
        }
    }
}