package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO

data class TilbakekrevingRedigerbartBrevDTO(val utenVariabler: Boolean = true) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object TilbakekrevingInnhold: EtterlatteTemplate<TilbakekrevingRedigerbartBrevDTO>, Delmal {
	override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_INNHOLD

	override val template = createTemplate(
		name = kode.name,
		letterDataType = TilbakekrevingRedigerbartBrevDTO::class,
		languages = languages(Bokmal, Nynorsk, English),
		letterMetadata = LetterMetadata(
			displayTitle = "Vedtak - Tilbakekreving",
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
			title2 {
				text(
					Bokmal to "Begrunnelse for vedtaket",
					Nynorsk to "Grunngiving for vedtaket",
					English to "Grounds for the decision",
				)
			}
			paragraph {
				text(
					Bokmal to "FRITEKSTFELT HENTET FRA MALER PÅ NAVET",
					Nynorsk to "FRITEKSTFELT HENTET FRA MALER PÅ NAVET",
					English to "FRITEKSTFELT HENTET FRA MALER PÅ NAVET",
				)
			}

		}
	}
}
