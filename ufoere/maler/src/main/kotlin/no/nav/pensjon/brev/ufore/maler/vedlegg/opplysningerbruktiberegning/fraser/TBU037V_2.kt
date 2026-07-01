package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/** Portert fra legacy TBU037V_2. Fotnoter for inntektstabell (bruker, folketrygd-metode). */
data class TBU037V_2(
    val flagg: Expression<Visningsflagg>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Inntektene som er uthevet er valgt siden dette gir det beste resultatet for deg." },
                nynorsk { + "Inntektene som er utheva, er valde sidan dette gir det beste resultatet for deg." },
            )
        }
        showIf(flagg.harOpptjeningUTMedFoerstegangstjenesteOgOmsorg) {
            paragraph {
                text(
                    bokmal { + "*) Markerer år med omsorgsopptjening og militær eller sivil førstegangstjeneste. Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel. Dersom inntekten i året før militær eller sivil førstegangstjeneste tok til er høyere, benyttes denne inntekten." },
                    nynorsk { + "*) Markerer år med omsorgsopptening og militær eller sivil førstegongsteneste. Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel. Dersom inntekta i året før militær eller sivil førstegongsteneste tok til, er høgare, blir denne inntekta brukt." },
                )
            }
        }
        showIf(flagg.harOpptjeningUTMedOmsorgOgIkkeFoerstegangstjeneste) {
            paragraph {
                text(
                    bokmal { + "*) Markerer år med omsorgsopptjening. Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel." },
                    nynorsk { + "*) Markerer år med omsorgsopptening. Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel." },
                )
            }
        }
        showIf(flagg.harOpptjeningUTMedFoerstegangstjenesteOgIkkeOmsorg) {
            paragraph {
                text(
                    bokmal { + "*) Markerer år med militær eller sivil førstegangstjeneste. Dersom inntekten i året før tjenesten tok til er høyere, benyttes denne inntekten." },
                    nynorsk { + "*) Markerer år med militær eller sivil førstegongsteneste. Dersom inntekta i året før tenesta tok til, er høgare, blir denne inntekta brukt." },
                )
            }
        }
    }
}
