package no.nav.pensjon.brev.ufore.maler.lovendringer2026

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AutoBrev.UT_VARSEL_HOYERE_MINSTESATS_IFU
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselOmHoyereMinstesatsForIFU : AutobrevTemplate<EmptyAutobrevdata> {

    override val kode = UT_VARSEL_HOYERE_MINSTESATS_IFU

    override val template = createTemplate(
        languages = languages(Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - høyere minstesats for IFU",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Varsel om endring av uføretrygd" },
                nynorsk { +"Varsel om endring av uføretrygd" },
            )
        }
        outline {
            includePhrase(UforetrygdLovendringer2026Fraser.Introduksjon)
            includePhrase(UforetrygdLovendringer2026Fraser.HoyereMinstesatsIFU)
            includePhrase(UforetrygdLovendringer2026Fraser.DetteKanDuGjoreNa)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
