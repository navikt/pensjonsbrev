package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*

data class OpplysningerBruktIBeregningUTDto(
val blad:String)


val opplysningerBruktIBeregningUT = createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
        title = newText(
                Bokmal to "Opplysninger om beregningen",
                Nynorsk to "Opplysningar om utrekninga",
                English to "Information about calculations"
        ),
        includeSakspart = true,
) {

}