package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_LEGE_LEGEERKLAERING

@TemplateModelHelpers
object LegeLegeerklaering : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_LEGE_LEGEERKLAERING
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av legeerklæring - Uføretrygd",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Innhenting av legeerklæring - Uføretrygd" })
        }
        outline {
            paragraph {
                text(bokmal { +"Ovennevnte person har søkt om uføretrygd. " })
            }
            paragraph {
                text(bokmal { +"Vi ber derfor om at du sender oss legeerklæring ved arbeidsuførhet NAV 08-07.08. " })
            }
            paragraph {
                text(bokmal { +"Vi ber om at du sender oss erklæringen snarest mulig og senest innen fire uker. Erklæringen sendes elektronisk. " })
            }
            paragraph {
                text(bokmal { +"Erklæringen godgjøres etter honorartakst L40. " })
            }
            paragraph {
                text(bokmal { +"Folketrygdloven § 21-4 andre ledd gir Nav rett til å innhente nødvendige opplysninger. Dette gjelder selv om opplysningene er taushetsbelagte. Dette går frem av § 21-4 sjette ledd.  " })
            }
            paragraph {
                text(bokmal { +"Pålegget om utlevering av opplysninger kan påklages etter forvaltningsloven § 14. Klageadgangen gjelder kun lovligheten av pålegget. Fristen for å klage er tre dager etter at pålegget er mottatt. Klagen kan fremsettes muntlig eller skriftlig. " })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
