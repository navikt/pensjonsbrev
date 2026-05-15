package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Felles fraser for AFP privat sektor-brev (innvilgelse og avslag).
 *
 * Brukes av [no.nav.pensjon.brev.alder.maler.afp.InnvilgelseAvAfpInnhold]
 * og [no.nav.pensjon.brev.alder.maler.afp.AvslagAfpPrivat].
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
}
