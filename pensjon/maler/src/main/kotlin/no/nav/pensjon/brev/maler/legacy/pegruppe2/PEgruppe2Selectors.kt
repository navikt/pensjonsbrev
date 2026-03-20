@file:Suppress("FunctionName", "unused")

package no.nav.pensjon.brev.maler.legacy.pegruppe2

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.PEgruppe2
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.PEgruppe2Selectors.grunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.PEgruppe2Selectors.vedtaksbrev
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.GrunnlagSelectors.persongrunnlagsliste
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.GrunnlagSelectors.persongrunnlagslisteavdod
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagsliste.PersongrunnlagslisteSelectors.inngangogeksportgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagsliste.PersongrunnlagslisteSelectors.trygdeavtaler
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagsliste.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.eksportunntak
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagsliste.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.minst20arbotid
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagsliste.trygdeavtaler.TrygdeavtalerSelectors.avtaletype
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.inngangogeksportgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.trygdeavtaledetaljer
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.trygdeavtaler
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.trygdetid
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.trygdetidsgrunnlaglistebilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.trygdetidsgrunnlaglisteeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.trygdetidsgrunnlaglistenor
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.PersongrunnlagslisteavdodSelectors.ttutlandtrygdeavtale
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.eksportforbud
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.eksportunntak
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.minst20arbotid
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.minstettarfmnorge
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.minsttrearsfmnorge
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.oppfyltettergamleregler
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.oppfyltvedsammenlegging
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.inngangogeksportgrunnlag.InngangogeksportgrunnlagSelectors.unntakfraforutgaendemedlemskap
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdeavtaledetaljer.TrygdeavtaledetaljerSelectors.art10bruktgp
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdeavtaledetaljer.TrygdeavtaledetaljerSelectors.ttnordiskar
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdeavtaledetaljer.TrygdeavtaledetaljerSelectors.ttnordiskmnd
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdeavtaler.TrygdeavtalerSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdeavtaler.TrygdeavtalerSelectors.avtaletype
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdeavtaler.TrygdeavtalerSelectors.bostedslandbeskrivelse
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.fatt
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.fatteos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.fattnorge
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.framtidigtteos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.framtidigttnorsk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.ttnevnereos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.ttnevnernordisk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.ttnordisk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.tttellereos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.TrygdetidSelectors.tttellernordisk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.fatt.FattSelectors.a10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetid.fatt.a10.A10Selectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglistebilateral.TrygdetidsgrunnlaglistebilateralSelectors.trygdetidsgrunnlagbilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglistebilateral.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagbilateralSelectors.trygdetidbilateralland
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglistebilateral.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagbilateralSelectors.trygdetidfombilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglistebilateral.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagbilateralSelectors.trygdetidtombilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglisteeos.TrygdetidsgrunnlaglisteeosSelectors.trygdetidsgrunnlageos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglisteeos.trygdetidsgrunnlageos.TrygdetidsgrunnlageosSelectors.trygdetideosland
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglisteeos.trygdetidsgrunnlageos.TrygdetidsgrunnlageosSelectors.trygdetidfomeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglisteeos.trygdetidsgrunnlageos.TrygdetidsgrunnlageosSelectors.trygdetidtomeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglistenor.TrygdetidsgrunnlaglistenorSelectors.trygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglistenor.trygdetidsgrunnlag.TrygdetidsgrunnlagSelectors.trygdetidfom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.trygdetidsgrunnlaglistenor.trygdetidsgrunnlag.TrygdetidsgrunnlagSelectors.trygdetidtom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.ttutlandtrygdeavtale.TtutlandtrygdeavtaleSelectors.fattbilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.ttutlandtrygdeavtale.TtutlandtrygdeavtaleSelectors.framtidigttavtaleland
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.ttutlandtrygdeavtale.TtutlandtrygdeavtaleSelectors.ttnevnerbilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.grunnlag.persongrunnlagslisteavdod.ttutlandtrygdeavtale.TtutlandtrygdeavtaleSelectors.tttellerbilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.VedtaksbrevSelectors.vedtaksdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.beregningsdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.kravhode
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.vilkarsvedtaklist
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.virkningfom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.VedtaksdataSelectors.virkningtom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsdataSelectors.beregning
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsdataSelectors.beregningantallperioder
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsdataSelectors.beregningperiode
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.BeregningsdataSelectors.beregningsrelasjoner
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningektefelleinntektover2g
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningektefellemottarpensjon
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningnokkelinfo
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningsinformasjonkapittel19
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningsivilstandanvendt
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningssammendragavdod
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningssammendragbruker
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningytelsekomp
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.beregningytelseskomp
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.brutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.periodearsakliste
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.virkdatofom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.BeregningSelectors.yug
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.BeregningnokkelinfoSelectors.beregningnokkelinfo2
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.BeregningnokkelinfoSelectors.ttanvbest
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.Beregningnokkelinfo2Selectors.beregningsmetode2
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.Beregningnokkelinfo2Selectors.opt
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.Beregningnokkelinfo2Selectors.penpersonfnr
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.Beregningnokkelinfo2Selectors.spt
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.Beregningnokkelinfo2Selectors.ttanv
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.Beregningnokkelinfo2Selectors.ypt
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.opt.OptSelectors.sluttpoengtallmedok
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.opt.OptSelectors.sluttpoengtallmedokeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.opt.OptSelectors.sluttpoengtallmedoknordisk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.SptSelectors.poengrekke
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.SptSelectors.poengrekkepopulert
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.SptSelectors.sluttpoengtallutenok
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.SptSelectors.sluttpoengtallutenokeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.SptSelectors.sluttpoengtallutenoknordisk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.framtidigpoengarnordenbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.framtidigpoengarnordennetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.framtidigpoengtall
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarnevnereos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengartellereos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarutenok
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarutenoke91
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarutenokf92
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarutenokfaktiskeeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarutenokfaktiskenorden
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarutenokfaktiskenorge
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengarutenokteoretiskeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.PoengrekkeSelectors.poengtall
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.poengtall.PoengtallSelectors.ar
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.poengtall.ar.ArSelectors.arstall
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.poengtall.ar.ArSelectors.grunnbelopveiet
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.poengtall.ar.ArSelectors.pensjonsgivendeinntekt
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.poengtall.ar.ArSelectors.pensjonspoeng
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.spt.poengrekke.poengtall.ar.ArSelectors.poengtallstype
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.ypt.YptSelectors.poengrekke
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.ypt.YptSelectors.sluttpoengtallyrke
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.ypt.poengrekke.PoengrekkeSelectors.poengaryrke
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.ypt.poengrekke.PoengrekkeSelectors.poengaryrkee91
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningnokkelinfo.beregningnokkelinfo2.ypt.poengrekke.PoengrekkeSelectors.poengaryrkef92
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningsinformasjonkapittel19.Beregningsinformasjonkapittel19Selectors.yug
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragavdod.BeregningssammendragavdodSelectors.avdoddodsdato
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragavdod.BeregningssammendragavdodSelectors.avdoddodsfallskyldesyrkesskade
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragavdod.BeregningssammendragavdodSelectors.avdodflyktning
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragavdod.BeregningssammendragavdodSelectors.avdodskadetidspunktyrkesskade
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragavdod.BeregningssammendragavdodSelectors.avdodunguforfodtetter1940
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragavdod.BeregningssammendragavdodSelectors.avdodunguforfodtfor1941
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragbruker.BeregningssammendragbrukerSelectors.brukerflyktning
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningssammendragbruker.BeregningssammendragbrukerSelectors.brukerfpi
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.BeregningytelsekompSelectors.familetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.BeregningytelsekompSelectors.fasteutgifter
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.BeregningytelsekompSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.BeregningytelsekompSelectors.sertillegg
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.BeregningytelsekompSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.familetillegg.FamiletilleggSelectors.familietilleggbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.familetillegg.FamiletilleggSelectors.familietillegginnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.familetillegg.FamiletilleggSelectors.familietilleggnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.fasteutgifter.FasteutgifterSelectors.fasteutgifterbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.fasteutgifter.FasteutgifterSelectors.fasteutgifterinnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.fasteutgifter.FasteutgifterSelectors.fasteutgifternetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.grunnpensjon.GrunnpensjonSelectors.gpbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.grunnpensjon.GrunnpensjonSelectors.gpnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.sertillegg.SertilleggSelectors.stinnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.sertillegg.SertilleggSelectors.stnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.tilleggspensjon.TilleggspensjonSelectors.tpbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.tilleggspensjon.TilleggspensjonSelectors.tpinnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelsekomp.tilleggspensjon.TilleggspensjonSelectors.tpnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelseskomp.BeregningytelseskompSelectors.sertillegg
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.beregningytelseskomp.sertillegg.SertilleggSelectors.stbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregning.periodearsakliste.PeriodearsaklisteSelectors.periodearsakkode
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.beregningssammendragbruker
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.beregningytelsekomp
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.brutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.periodearsakliste
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.virkdatofom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.BeregningperiodeSelectors.virkdatotom
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningssammendragbruker.BeregningssammendragbrukerSelectors.brukerfpi
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.BeregningytelsekompSelectors.familietillegg
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.BeregningytelsekompSelectors.fasteutgifter
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.BeregningytelsekompSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.BeregningytelsekompSelectors.sertillegg
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.BeregningytelsekompSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.familietillegg.FamilietilleggSelectors.familietilleggbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.familietillegg.FamilietilleggSelectors.familietillegginnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.familietillegg.FamilietilleggSelectors.familietilleggnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.fasteutgifter.FasteutgifterSelectors.fasteutgifterbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.fasteutgifter.FasteutgifterSelectors.fasteutgifterinnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.fasteutgifter.FasteutgifterSelectors.fasteutgifternetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.grunnpensjon.GrunnpensjonSelectors.gpbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.grunnpensjon.GrunnpensjonSelectors.gpnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.sertillegg.SertilleggSelectors.stbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.sertillegg.SertilleggSelectors.stinnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.sertillegg.SertilleggSelectors.stnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.tilleggspensjon.TilleggspensjonSelectors.tpbrutto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.tilleggspensjon.TilleggspensjonSelectors.tpinnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.beregningytelsekomp.tilleggspensjon.TilleggspensjonSelectors.tpnetto
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningperiode.periodearsakliste.PeriodearsaklisteSelectors.periodearsakkode
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningsrelasjoner.BeregningsrelasjonerSelectors.avdodetternavn
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningsrelasjoner.BeregningsrelasjonerSelectors.avdodfornavn
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.beregningsdata.beregningsrelasjoner.BeregningsrelasjonerSelectors.avdodmellomnavn
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.kravhode.KravhodeSelectors.behandlendeenhet
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.kravhode.KravhodeSelectors.kravmottatdato
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.kravhode.KravhodeSelectors.vurderetrygdeavtale
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.VilkarsvedtaklistSelectors.vilkarsvedtak
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe2.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak.VilkarsvedtakSelectors.vilkaranvendtvurdering

fun Expression<PEgruppe2>.grunnlag_persongrunnlagsliste_inngangogeksportgrunnlag_eksportunntak(): Expression<String> =
    grunnlag.safe{persongrunnlagsliste}.safe{inngangogeksportgrunnlag}.safe{eksportunntak}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagsliste_inngangogeksportgrunnlag_minst20arbotid(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagsliste}.safe{inngangogeksportgrunnlag}.safe{minst20arbotid}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaletype(): Expression<String> =
    grunnlag.safe{persongrunnlagsliste}.safe{trygdeavtaler}.safe{avtaletype}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportforbud(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{eksportforbud}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportunntak(): Expression<String> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{eksportunntak}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minst20arbotid(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{minst20arbotid}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minstettarfmnorge(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{minstettarfmnorge}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minsttrearsfmnorge(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{minsttrearsfmnorge}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltettergamleregler(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{oppfyltettergamleregler}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltvedsammenlegging(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{oppfyltvedsammenlegging}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_unntakfraforutgaendemedlemskap(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{inngangogeksportgrunnlag}.safe{unntakfraforutgaendemedlemskap}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdeavtaledetaljer_art10bruktgp(): Expression<Boolean> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdeavtaledetaljer}.safe{art10bruktgp}.ifNull(false)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdeavtaledetaljer_ttnordiskar(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdeavtaledetaljer}.safe{ttnordiskar}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdeavtaledetaljer_ttnordiskmnd(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdeavtaledetaljer}.safe{ttnordiskmnd}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland(): Expression<String> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdeavtaler}.safe{avtaleland}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaletype(): Expression<String> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdeavtaler}.safe{avtaletype}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_bostedslandbeskrivelse(): Expression<String> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdeavtaler}.safe{bostedslandbeskrivelse}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_fatt_a10_netto(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{fatt}.safe{a10}.safe{netto}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_fatteos(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{fatteos}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_fattnorge(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{fattnorge}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_framtidigtteos(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{framtidigtteos}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_framtidigttnorsk(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{framtidigttnorsk}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_ttnevnereos(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{ttnevnereos}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_ttnevnernordisk(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{ttnevnernordisk}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_ttnordisk(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{ttnordisk}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_tttellereos(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{tttellereos}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetid_tttellernordisk(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetid}.safe{tttellernordisk}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidbilateralland(): Expression<String> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglistebilateral}.safe{trygdetidsgrunnlagbilateral}.safe{trygdetidbilateralland}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral(): Expression<LocalDate> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglistebilateral}.safe{trygdetidsgrunnlagbilateral}.safe{trygdetidfombilateral}.ifNull(TODO())

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidtombilateral(): Expression<LocalDate> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglistebilateral}.safe{trygdetidsgrunnlagbilateral}.safe{trygdetidtombilateral}.ifNull(TODO())

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglisteeos_trygdetidsgrunnlageos_trygdetideosland(): Expression<String> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglisteeos}.safe{trygdetidsgrunnlageos}.safe{trygdetideosland}.ifNull("")

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglisteeos_trygdetidsgrunnlageos_trygdetidfomeos(): Expression<LocalDate> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglisteeos}.safe{trygdetidsgrunnlageos}.safe{trygdetidfomeos}.ifNull(TODO())

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglisteeos_trygdetidsgrunnlageos_trygdetidtomeos(): Expression<LocalDate> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglisteeos}.safe{trygdetidsgrunnlageos}.safe{trygdetidtomeos}.ifNull(TODO())

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom(): Expression<LocalDate> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglistenor}.safe{trygdetidsgrunnlag}.safe{trygdetidfom}.ifNull(TODO())

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidtom(): Expression<LocalDate> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{trygdetidsgrunnlaglistenor}.safe{trygdetidsgrunnlag}.safe{trygdetidtom}.ifNull(TODO())

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_ttutlandtrygdeavtale_fattbilateral(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{ttutlandtrygdeavtale}.safe{fattbilateral}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_ttutlandtrygdeavtale_framtidigttavtaleland(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{ttutlandtrygdeavtale}.safe{framtidigttavtaleland}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_ttutlandtrygdeavtale_ttnevnerbilateral(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{ttutlandtrygdeavtale}.safe{ttnevnerbilateral}.ifNull(0)

fun Expression<PEgruppe2>.grunnlag_persongrunnlagslisteavdod_ttutlandtrygdeavtale_tttellerbilateral(): Expression<Int> =
    grunnlag.safe{persongrunnlagslisteavdod}.safe{ttutlandtrygdeavtale}.safe{tttellerbilateral}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningektefelleinntektover2g(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningektefelleinntektover2g}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningektefellemottarpensjon(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningektefellemottarpensjon}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{beregningsmetode2}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_opt_sluttpoengtallmedok(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{opt}.safe{sluttpoengtallmedok}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_opt_sluttpoengtallmedokeos(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{opt}.safe{sluttpoengtallmedokeos}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_opt_sluttpoengtallmedoknordisk(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{opt}.safe{sluttpoengtallmedoknordisk}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_penpersonfnr(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{penpersonfnr}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_framtidigpoengarnordenbrutto(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{framtidigpoengarnordenbrutto}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_framtidigpoengarnordennetto(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{framtidigpoengarnordennetto}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_framtidigpoengtall(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{framtidigpoengtall}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarnevnereos(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarnevnereos}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengartellereos(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengartellereos}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarutenok(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarutenok}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarutenoke91(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarutenoke91}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarutenokf92(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarutenokf92}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarutenokfaktiskeeos(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarutenokfaktiskeeos}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarutenokfaktiskenorden(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarutenokfaktiskenorden}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarutenokfaktiskenorge(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarutenokfaktiskenorge}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengarutenokteoretiskeos(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengarutenokteoretiskeos}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengtall_ar_arstall(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengtall}.safe{ar}.safe{arstall}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengtall_ar_grunnbelopveiet(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengtall}.safe{ar}.safe{grunnbelopveiet}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengtall_ar_pensjonsgivendeinntekt(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengtall}.safe{ar}.safe{pensjonsgivendeinntekt}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengtall_ar_pensjonspoeng(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengtall}.safe{ar}.safe{pensjonspoeng}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekke_poengtall_ar_poengtallstype(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekke}.safe{poengtall}.safe{ar}.safe{poengtallstype}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_poengrekkepopulert(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{poengrekkepopulert}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_sluttpoengtallutenok(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{sluttpoengtallutenok}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_sluttpoengtallutenokeos(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{sluttpoengtallutenokeos}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_spt_sluttpoengtallutenoknordisk(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{spt}.safe{sluttpoengtallutenoknordisk}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_ttanv(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{ttanv}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_ypt_poengrekke_poengaryrke(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{ypt}.safe{poengrekke}.safe{poengaryrke}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_ypt_poengrekke_poengaryrkee91(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{ypt}.safe{poengrekke}.safe{poengaryrkee91}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_ypt_poengrekke_poengaryrkef92(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{ypt}.safe{poengrekke}.safe{poengaryrkef92}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_ypt_sluttpoengtallyrke(): Expression<Double> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{beregningnokkelinfo2}.safe{ypt}.safe{sluttpoengtallyrke}.ifNull(0.0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_ttanvbest(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningnokkelinfo}.safe{ttanvbest}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningsinformasjonkapittel19_yug(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningsinformasjonkapittel19}.safe{yug}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningsivilstandanvendt}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdoddodsdato(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragavdod}.safe{avdoddodsdato}.ifNull(TODO())

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdoddodsfallskyldesyrkesskade(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragavdod}.safe{avdoddodsfallskyldesyrkesskade}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdodflyktning(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragavdod}.safe{avdodflyktning}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdodskadetidspunktyrkesskade(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragavdod}.safe{avdodskadetidspunktyrkesskade}.ifNull(TODO())

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdodunguforfodtetter1940(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragavdod}.safe{avdodunguforfodtetter1940}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdodunguforfodtfor1941(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragavdod}.safe{avdodunguforfodtfor1941}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerflyktning(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragbruker}.safe{brukerflyktning}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningssammendragbruker}.safe{brukerfpi}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{familetillegg}.safe{familietilleggbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietillegginnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{familetillegg}.safe{familietillegginnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{familetillegg}.safe{familietilleggnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifterbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{fasteutgifter}.safe{fasteutgifterbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifterinnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{fasteutgifter}.safe{fasteutgifterinnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifternetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{fasteutgifter}.safe{fasteutgifternetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{grunnpensjon}.safe{gpbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{grunnpensjon}.safe{gpnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stinnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{sertillegg}.safe{stinnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{sertillegg}.safe{stnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{tilleggspensjon}.safe{tpbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpinnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{tilleggspensjon}.safe{tpinnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelsekomp}.safe{tilleggspensjon}.safe{tpnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_beregningytelseomp_sertillegg_stbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{beregningytelseskomp}.safe{sertillegg}.safe{stbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_brutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{brutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_grunnbelop(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{grunnbelop}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_netto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{netto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_periodearsakliste_periodearsakkode(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{periodearsakliste}.safe{periodearsakkode}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_virkdatofom(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{virkdatofom}.ifNull(TODO())

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregning_yug(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregning}.safe{yug}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningantallperioder(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningantallperioder}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningssammendragbruker_brukerfpi(): Expression<Int> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningssammendragbruker}.safe{brukerfpi}.ifNull(0)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_familietillegg_familietilleggbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{familietillegg}.safe{familietilleggbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_familietillegg_familietillegginnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{familietillegg}.safe{familietillegginnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_familietillegg_familietilleggnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{familietillegg}.safe{familietilleggnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_fasteutgifter_fasteutgifterbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{fasteutgifter}.safe{fasteutgifterbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_fasteutgifter_fasteutgifterinnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{fasteutgifter}.safe{fasteutgifterinnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_fasteutgifter_fasteutgifternetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{fasteutgifter}.safe{fasteutgifternetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_grunnpensjon_gpbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{grunnpensjon}.safe{gpbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_grunnpensjon_gpnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{grunnpensjon}.safe{gpnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_sertilegg_stnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{sertillegg}.safe{stnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_sertillegg_stbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{sertillegg}.safe{stbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_sertillegg_stinnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{sertillegg}.safe{stinnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_tilleggspensjon_tpbrutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{tilleggspensjon}.safe{tpbrutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_tilleggspensjon_tpinnvilget(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{tilleggspensjon}.safe{tpinnvilget}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_beregningytelsekomp_tilleggspensjon_tpnetto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{beregningytelsekomp}.safe{tilleggspensjon}.safe{tpnetto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_brutto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{brutto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_grunnbelop(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{grunnbelop}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_netto(): Expression<Kroner> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{netto}.ifNull(Kroner(0))

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_periodearsakliste_periodearsakkode(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{periodearsakliste}.getOrNull().safe{periodearsakkode}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_virkdatofom(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{virkdatofom}.ifNull(TODO())

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningperiode_virkdatotom(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningperiode}.getOrNull().safe{virkdatotom}.ifNull(TODO())

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningsrelasjoner}.safe{avdodetternavn}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningsrelasjoner}.safe{avdodfornavn}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningsrelasjoner}.safe{avdodmellomnavn}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{beregningsdata}.safe{beregningsrelasjoner}.safe{avdodmellomnavn}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_etterbetaling(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{etterbetaling}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_kravhode_behandlendeenhet(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{kravhode}.safe{behandlendeenhet}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_kravhode_kravmottatdato(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{kravhode}.safe{kravmottatdato}.ifNull(TODO())

fun Expression<PEgruppe2>.vedtaksdata_kravhode_vurderetrygdeavtale(): Expression<Boolean> =
    vedtaksbrev.safe{vedtaksdata}.safe{kravhode}.safe{vurderetrygdeavtale}.ifNull(false)

fun Expression<PEgruppe2>.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkaranvendtvurdering(): Expression<String> =
    vedtaksbrev.safe{vedtaksdata}.safe{vilkarsvedtaklist}.safe{vilkarsvedtak}.getOrNull().safe{vilkaranvendtvurdering}.ifNull("")

fun Expression<PEgruppe2>.vedtaksdata_virkningfom(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{virkningfom}.ifNull(TODO())

fun Expression<PEgruppe2>.vedtaksdata_virkningtom(): Expression<LocalDate> =
    vedtaksbrev.safe{vedtaksdata}.safe{virkningtom}.ifNull(TODO())
