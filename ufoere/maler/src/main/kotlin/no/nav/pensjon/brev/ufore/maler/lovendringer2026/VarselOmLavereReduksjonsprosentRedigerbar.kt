package no.nav.pensjon.brev.ufore.maler.lovendringer2026

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselOmLavereReduksjonsprosentRedigerbar : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.varselhoyereminstesatsifuoglaverereduksjonsprosent.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_S_VARSEL_LAVERE_REDUKSJONSPROSENT
    override val kategori = Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - lavere reduksjonsprosent",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
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
            includePhrase(UforetrygdLovendringer2026Fraser.LavereReduksjonsprosent)
            includePhrase(UforetrygdLovendringer2026Fraser.DetteKanDuGjoreNa)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
