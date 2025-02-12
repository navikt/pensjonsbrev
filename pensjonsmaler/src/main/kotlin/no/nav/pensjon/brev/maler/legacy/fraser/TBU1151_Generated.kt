package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1151_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU1151EN, TBU1151NN, TBU1151]

        paragraph {
            text(
                Bokmal to "For å bli innvilget rettighet som ung ufør, er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert.",
                Nynorsk to "For å bli innvilga rett som ung ufør er det eit krav at du blei ufør før du fylte 26 år på grunn av ein alvorleg og varig sjukdom eller skade, som er klart dokumentert.",
                English to "To be granted rights as a young disabled person, you must have become disabled before turning 26 due to a serious and permanent illness or injury, which has been clearly documented.",
            )
            text(
                Bokmal to "Du oppfyller ikke vilkåret om rettighet som ung ufør, fordi det ikke er dokumentert at du ble alvorlig og varig syk før du fylte 26 år. <FRITEKST: konkret begrunnelse>. ",
                Nynorsk to "Du oppfyller ikkje vilkåret om rett som ung ufør fordi det ikkje er dokumentert at du blei alvorleg og varig sjuk før du fylte 26 år. <FRITEKST: Konkret begrunnelse>.",
                English to "You do not meet the criteria regarding rights as a young disabled person, because it has not been documented that you had a serious and permanent illness before you turned 26. <FRITEKST: konkret begrunnelse>.",
            )
        }
    }
}
