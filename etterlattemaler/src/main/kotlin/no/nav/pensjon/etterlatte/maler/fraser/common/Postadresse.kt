package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, out Any>.postadresse(
    utland: Expression<Boolean>
) {
    paragraph {
        showIf(utland) {
            text(
                bokmal { +"Nav familie- og pensjonsytelser" },
                nynorsk { +"Nav familie- og pensjonsytelser" },
                english { +"Nav familie- og pensjonsytelser" }
            )
            newline()
            text(
                bokmal { +"Postboks 6600 Etterstad" },
                nynorsk { +"Postboks 6600 Etterstad" },
                english { +"Postboks 6600 Etterstad" }
            )
            newline()
            text(
                bokmal { +"0607 Oslo" },
                nynorsk { +"0607 Oslo" },
                english { +"0607 Oslo" }
            )
            newline()
            text(
                bokmal { +"Norge/Norway" },
                nynorsk { +"Noreg/Norway" },
                english { +"Norway" }
            )
        }.orShow {
            text(
                bokmal { +"Nav skanning" },
                nynorsk { +"Nav skanning" },
                english { +"Nav skanning" }
            )
            newline()
            text(
                bokmal { +"Postboks 1400" },
                nynorsk { +"Postboks 1400" },
                english { +"Postboks 1400" }
            )
            newline()
            text(
                bokmal { +"0109 OSLO" },
                nynorsk { +"0109 OSLO" },
                english { +"0109 OSLO" }
            )
        }
    }

}