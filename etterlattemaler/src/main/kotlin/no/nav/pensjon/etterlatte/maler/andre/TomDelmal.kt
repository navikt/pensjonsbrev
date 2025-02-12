package no.nav.pensjon.etterlatte.maler.andre

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO

@TemplateModelHelpers
object TomDelmal : EtterlatteTemplate<ManueltBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TOM_DELMAL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = ManueltBrevDTO::class,
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Tom delmal",
                    isSensitiv = true,
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Language.Bokmal to "",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            outline {
                includePhrase(
                    TomDelmalTekst,
                )
            }
        }
}

object TomDelmalTekst : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Language.Bokmal to "Dette er en tom brevmal",
                Language.Nynorsk to "Dette er en tom brevmal",
                Language.English to "Dette er en tom brevmal",
            )
        }
        paragraph {
            text(
                Language.Bokmal to "Det finnes ingen brevmal for denne sak- eller vedtakstypen.",
                Language.Nynorsk to "Det finnes ingen brevmal for denne sak- eller vedtakstypen.",
                Language.English to "Det finnes ingen brevmal for denne sak- eller vedtakstypen.",
            )
        }
    }
}
