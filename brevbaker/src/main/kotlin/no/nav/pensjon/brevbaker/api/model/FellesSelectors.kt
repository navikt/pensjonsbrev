package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
import no.nav.pensjon.brev.template.ExpressionScope

object FellesSelectors {
    val avsenderEnhetSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Felles, no.nav.pensjon.brevbaker.api.model.NAVEnhet> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Felles"
       override val propertyName: String = "avsenderEnhet"
       override val propertyType: String = "no.nav.pensjon.brevbaker.api.model.NAVEnhet"
       override val selector = no.nav.pensjon.brevbaker.api.model.Felles::avsenderEnhet
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Felles>.avsenderEnhet: Expression<no.nav.pensjon.brevbaker.api.model.NAVEnhet>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(avsenderEnhetSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles>.avsenderEnhet: Expression<no.nav.pensjon.brevbaker.api.model.NAVEnhet>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(avsenderEnhetSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles?>.avsenderEnhet_safe: Expression<no.nav.pensjon.brevbaker.api.model.NAVEnhet?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(avsenderEnhetSelector)
       )
    
    val brukerSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Felles, no.nav.pensjon.brevbaker.api.model.Bruker> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Felles"
       override val propertyName: String = "bruker"
       override val propertyType: String = "no.nav.pensjon.brevbaker.api.model.Bruker"
       override val selector = no.nav.pensjon.brevbaker.api.model.Felles::bruker
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Felles>.bruker: Expression<no.nav.pensjon.brevbaker.api.model.Bruker>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(brukerSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles>.bruker: Expression<no.nav.pensjon.brevbaker.api.model.Bruker>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(brukerSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles?>.bruker_safe: Expression<no.nav.pensjon.brevbaker.api.model.Bruker?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(brukerSelector)
       )
    
    val dokumentDatoSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Felles, java.time.LocalDate> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Felles"
       override val propertyName: String = "dokumentDato"
       override val propertyType: String = "java.time.LocalDate"
       override val selector = no.nav.pensjon.brevbaker.api.model.Felles::dokumentDato
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Felles>.dokumentDato: Expression<java.time.LocalDate>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(dokumentDatoSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles>.dokumentDato: Expression<java.time.LocalDate>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(dokumentDatoSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles?>.dokumentDato_safe: Expression<java.time.LocalDate?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(dokumentDatoSelector)
       )
    
    val saksnummerSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Felles, kotlin.String> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Felles"
       override val propertyName: String = "saksnummer"
       override val propertyType: String = "kotlin.String"
       override val selector = no.nav.pensjon.brevbaker.api.model.Felles::saksnummer
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Felles>.saksnummer: Expression<kotlin.String>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(saksnummerSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles>.saksnummer: Expression<kotlin.String>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(saksnummerSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles?>.saksnummer_safe: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(saksnummerSelector)
       )
    
    val signerendeSaksbehandlereSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Felles, no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere?> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Felles"
       override val propertyName: String = "signerendeSaksbehandlere"
       override val propertyType: String = "no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere?"
       override val selector = no.nav.pensjon.brevbaker.api.model.Felles::signerendeSaksbehandlere
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Felles>.signerendeSaksbehandlere: Expression<no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere?>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(signerendeSaksbehandlereSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles>.signerendeSaksbehandlere: Expression<no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(signerendeSaksbehandlereSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles?>.signerendeSaksbehandlere_safe: Expression<no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(signerendeSaksbehandlereSelector)
       )
    
    val vergeNavnSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Felles, kotlin.String?> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Felles"
       override val propertyName: String = "vergeNavn"
       override val propertyType: String = "kotlin.String?"
       override val selector = no.nav.pensjon.brevbaker.api.model.Felles::vergeNavn
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Felles>.vergeNavn: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(vergeNavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles>.vergeNavn: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(vergeNavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Felles?>.vergeNavn_safe: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(vergeNavnSelector)
       )
    

}
