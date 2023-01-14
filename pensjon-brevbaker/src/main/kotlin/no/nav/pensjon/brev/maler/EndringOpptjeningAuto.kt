package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.ufoerBeloepOekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.ufoerBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.endringIOpptjening
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.folketrygdloven
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.virkningsDato
import no.nav.pensjon.brev.api.model.maler.FolketrygdlovenSelectors.harYrkesskadeGradUtbetaling
import no.nav.pensjon.brev.api.model.maler.FolketrygdlovenSelectors.innvilgetEktefelletillegg
import no.nav.pensjon.brev.api.model.maler.FolketrygdlovenSelectors.innvilgetFellesbarntillegg
import no.nav.pensjon.brev.api.model.maler.FolketrygdlovenSelectors.innvilgetGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.FolketrygdlovenSelectors.innvilgetSaerkullsbarntillegg
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.ektefelletilleggUtbeltalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.gjenlevendetilleggUtbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.EndringOpptjening
import no.nav.pensjon.brev.maler.fraser.common.HjemlerFolketrygdloven
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
object EndringOpptjeningAuto : VedtaksbrevTemplate<EndringOpptjeningAutoDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.UT_ENDRING_OPPTJENING_AUTO

    override val template = createTemplate(
        name = OmsorgEgenAuto.kode.name,
        letterDataType = EndringOpptjeningAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ny beregning av UT pga endring i opptjening",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        )
    ) {
        val harBarnetilleggFellesbarn = barnetilleggFellesbarn.notNull()
        val harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.notNull()
        title {
            text(
                Bokmal to "NAV har beregnet uføretrygden din på nytt",
                Nynorsk to "NAV har berekne uføretrygden di på nytt",
                English to "NAV has recalculated your disability benefit"
            )
        }
        outline {
            includePhrase(EndringOpptjening.OpplysningerFraSkatteetaten)

            includePhrase(
                EndringOpptjening.BetydningForUfoeretrygden(
                    ufoerBeloepOekt = endringIOpptjening.ufoerBeloepOekt,
                    ufoerBeloepRedusert = endringIOpptjening.ufoerBeloepRedusert,
                    virkningsDato = virkningsDato
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = ufoeretrygd.utbetaltPerMaaned,
                    ufoeretrygd = ufoeretrygd.harUtbetalingsgrad,
                    ektefelle = ufoeretrygd.ektefelletilleggUtbeltalt_safe.notNull(),
                    gjenlevende = ufoeretrygd.gjenlevendetilleggUtbetalt_safe.notNull(),
                    fellesbarn = harBarnetilleggFellesbarn,
                    saerkullsbarn = harBarnetilleggSaerkullsbarn,
                )
            )

            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd)
            includePhrase(Vedtak.ViktigAaLeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(EndringOpptjening.UfoerInntektListe)

            includePhrase(
                EndringOpptjening.EndringIOpptjeningTilUfoeretrygd(
                    ufoerBeloepOekt = endringIOpptjening.ufoerBeloepOekt,
                    ufoerBeloepRedusert = endringIOpptjening.ufoerBeloepRedusert,
                    virkningsDato = virkningsDato
                )
            )

            includePhrase(Ufoeretrygd.HenvisningTilVedleggOpplysningerOmBeregningenUfoer)

            includePhrase(
                HjemlerFolketrygdloven.Folketrygdloven(
                    innvilgetEktefelletillegg = folketrygdloven.innvilgetEktefelletillegg,
                    innvilgetGjenlevendetillegg = folketrygdloven.innvilgetGjenlevendetillegg,
                    innvilgetFellesbarntillegg = folketrygdloven.innvilgetFellesbarntillegg,
                    innvilgetSaerkullsbarntillegg = folketrygdloven.innvilgetSaerkullsbarntillegg,
                    harYrkesskadegradUtbetaling = folketrygdloven.harYrkesskadeGradUtbetaling
                )
            )
        }
    }
}