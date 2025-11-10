package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

data class TBU1100_1_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1100.1EN, TBU1100.1NN, TBU1100_MedMargin, TBU1100.1]
        title1 {
            text(
                bokmal { + "Du har rett til å klage " },
                nynorsk { + "Du har rett til å klage " },
                english { + "You have the right to appeal " }
            )
        }
        paragraph {
            text(
                bokmal { + "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du fikk vedtaket. " +
                        "I vedlegget " },
                nynorsk { + "Dersom du meiner at vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. " +
                        "I vedlegget " },
                english { + "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. " +
                        "In the attachment " }
            )
            namedReference(vedleggDineRettigheterOgPlikterUfoere)
            text(
                bokmal { + " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL. " },
                nynorsk { + " kan du lese meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL. " },
                english {+ " you will find more about how to proceed. You will find forms and information at $KLAGE_URL." }
            )
        }
    }
}
        