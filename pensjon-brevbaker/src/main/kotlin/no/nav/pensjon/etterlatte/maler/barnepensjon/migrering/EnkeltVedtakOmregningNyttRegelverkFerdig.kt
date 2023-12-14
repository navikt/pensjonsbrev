package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.utlandInformasjonTilDegSomMottarBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.informasjonTilDegSomHandlerPaaVegneAvBarnet
import no.nav.pensjon.etterlatte.maler.vedlegg.utlandInformasjonTilDegSomHandlerPaaVegneAvBarnet

@TemplateModelHelpers
object EnkeltVedtakOmregningNyttRegelverkFerdig : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkFerdigDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK_OMREGNING_FERDIG
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkFerdigDTO> = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOmregnetNyttRegelverkFerdigDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Utkast til vedtak - endring av barnepensjon",
                Language.Nynorsk to "Utkast til vedtak – endring av barnepensjon",
                Language.English to "Draft decision – adjustment of children's pension",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)
        }

        // Over 18 år vedlegg
        includeAttachment(utlandInformasjonTilDegSomMottarBarnepensjon, this.argument, data.erUnder18Aar.not().and(data.erBosattUtlandet))
        includeAttachment(informasjonTilDegSomMottarBarnepensjon, this.argument, data.erUnder18Aar.not().and(data.erBosattUtlandet.not()))

        // Under 18 år vedlegg
        includeAttachment(utlandInformasjonTilDegSomHandlerPaaVegneAvBarnet, this.argument, data.erUnder18Aar.and(data.erBosattUtlandet))
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnet, this.argument, data.erUnder18Aar.and(data.erBosattUtlandet.not()))

        includeAttachment(dineRettigheterOgPlikter, data.erUnder18Aar)
    }
}