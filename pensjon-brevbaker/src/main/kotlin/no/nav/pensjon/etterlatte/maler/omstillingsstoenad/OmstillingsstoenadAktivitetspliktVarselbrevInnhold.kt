package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTOSelectors.aktivitetsgrad

data class OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO(
    val aktivitetsgrad: Aktivitetsgrad,
) : RedigerbartUtfallBrevDTO

enum class Aktivitetsgrad { IKKE_I_AKTIVITET, UNDER_50_PROSENT, OVER_50_PROSENT };

object AktivitetsgradFormatter : LocalizedFormatter<Aktivitetsgrad>(), StableHash by StableHash.of("AktivitetsgradFormatter") {
    override fun apply(aktivitetsgrad: Aktivitetsgrad, spraak: Language): String {
        return aktivitetsgrad.name
    }
}


@TemplateModelHelpers
object OmstillingsstoenadAktivitetspliktVarselInnhold :
    EtterlatteTemplate<OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AKTIVITETSPLIKT_VARSELBREV_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata =
        LetterMetadata(
            displayTitle = "Informasjon om aktivitetsplikt for omstillingsstønad",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "TODO",
                Nynorsk to "TODO",
                English to "TODO",
            )
        }

        outline {
            paragraph {
                textExpr(
                    Bokmal to "Bokmål ".expr() + aktivitetsgrad.format(AktivitetsgradFormatter),
                    Nynorsk to "Nynorsk ".expr() + aktivitetsgrad.format(AktivitetsgradFormatter),
                    English to "Engelsk ".expr() + aktivitetsgrad.format(AktivitetsgradFormatter),
                )
            }
        }
    }
}
