package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.redigerbar

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.Vedlegg

@TemplateModelHelpers
object OmstillingsstoenadVedleggBeregningRedigerbartUtfall : EtterlatteTemplate<ManueltBrevDTO>, Vedlegg {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_VEDLEGG_BEREGNING_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utfall beregning",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            includePhrase(Fraser.Placeholder)
        }
    }

    private object Fraser {
        object Placeholder : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                paragraph {
                    text(
                        Bokmal to "SKRIV INN HVILKEN INNTEKT SOM ER LAGT TIL GRUNN. HER KAN DU OGSÅ LEGGE INN OM INSTITUSJONSOPPHOLD E.L. OM DET SKULLE VÆRE AKTUELT.",
                        Nynorsk to "SKRIV INN HVILKEN INNTEKT SOM ER LAGT TIL GRUNN. HER KAN DU OGSÅ LEGGE INN OM INSTITUSJONSOPPHOLD E.L. OM DET SKULLE VÆRE AKTUELT.",
                        English to "SKRIV INN HVILKEN INNTEKT SOM ER LAGT TIL GRUNN. HER KAN DU OGSÅ LEGGE INN OM INSTITUSJONSOPPHOLD E.L. OM DET SKULLE VÆRE AKTUELT.",
                    )
                }
            }
        }
    }

}