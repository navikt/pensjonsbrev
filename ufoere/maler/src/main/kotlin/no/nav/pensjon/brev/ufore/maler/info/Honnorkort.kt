package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_HONNORKORT
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object Honnorkort : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_HONNORKORT
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Honnørkort",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Honnørkort" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Du har rett til honnørrabatt når du bruker offentlig transport på innenlandsreiser. Vedlagt følger honnørkortet. " +
                            "Dersom du leser dette brevet digitalt så informerer vi om at honnørkortet vil komme med papirversjonen av brevet som kommer i posten. " },
                )
            }
        }
    }
}