package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgetGarantitilleggDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgetGarantitilleggDtoSelectors.PesysDataSelectors.garantitillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgetGarantitilleggDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgetGarantitilleggDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgetGarantitilleggDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.math.E

// MF_000102 med krav.arsak = ALDERSOVERGANG
// Brevet gjelder for AP2016/AP2025 brukere når garantitillegg er innvilget etter kapittel 20 i ny alderspensjon

@TemplateModelHelpers
object InnvilgetGarantitillegg : RedigerbarTemplate<InnvilgetGarantitilleggDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGET_GARANTITILLEGG
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgetGarantitilleggDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Innvilgelse av garantitillegg i alderspensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val kravVirkDatoFom = pesysData.kravVirkDatoFom
        val garantitillegg = pesysData.garantitillegg
        val regelverkType = pesysData.regelverkType

        title {
            textExpr(
                Bokmal to "Du har fått innvilget garantitillegg fra ".expr() + kravVirkDatoFom.format(),
                Nynorsk to "Du har fått innvilga garantitillegg frå ".expr() + kravVirkDatoFom.format(),
                English to "You have been granted a guarantee supplement for accumulated pension capital rights from ".expr() + kravVirkDatoFom.format(),
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Garantitillegget skal sikre at du får en alderspensjon som tilsvarer den pensjonen du hadde tjent opp før pensjonsreformen i 2010.",
                    Nynorsk to "Garantitillegget skal sikre at du får ein alderspensjon ved 67 år som svarer til den pensjonen du hadde tent opp før pensjonsreforma i 2010.",
                    English to "The guarantee supplement for accumulated pension capital rights is to ensure that you receive a retirement pension at age 67 that corresponds " +
                            "to the pension you had earned before the pension reform in 2010.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Tillegget utbetales sammen med alderspensjonen og kan tidligst utbetales fra måneden etter du fyller 67 år.",
                    Nynorsk to "Tillegget blir betalt ut samen med alderspensjonen og kan tidlegast betalast ut frå månaden etter du fyller 67 år.",
                    English to "The supplement will be paid in addition to your retirement pension and can at the earliest be paid from the month after you turn 67 years of age.",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Garantitillegget utgjør ".expr() + garantitillegg.format() + " kroner per måned før skatt fra ".expr() + kravVirkDatoFom.format(),
                    Nynorsk to "Garantitillegget utgjer ".expr() + garantitillegg.format() + " kroner per månad før skatt frå ".expr() + kravVirkDatoFom.format(),
                    English to "Your monthly guarantee supplement for accumulated pension capital rights will be NOK "
                        .expr() + garantitillegg.format() + " before tax from ".expr() + kravVirkDatoFom.format()
                )
            }

            includePhrase(Utbetalingsinformasjon)

            showIf(regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025)) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-20.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-20.",
                        English to "This decision was made pursuant to the provisions of § 20-20 of the National Insurance Act."
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act."
                    )
                }
            }
        }
    }
}