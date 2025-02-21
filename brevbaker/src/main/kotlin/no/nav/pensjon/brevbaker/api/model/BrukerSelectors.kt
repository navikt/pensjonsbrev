package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
import no.nav.pensjon.brev.template.ExpressionScope

object BrukerSelectors {
    val etternavnSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Bruker, kotlin.String> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Bruker"
       override val propertyName: String = "etternavn"
       override val propertyType: String = "kotlin.String"
       override val selector = no.nav.pensjon.brevbaker.api.model.Bruker::etternavn
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Bruker>.etternavn: Expression<kotlin.String>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(etternavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker>.etternavn: Expression<kotlin.String>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(etternavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker?>.etternavn_safe: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(etternavnSelector)
       )
    
    val foedselsnummerSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Bruker, no.nav.pensjon.brevbaker.api.model.Foedselsnummer> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Bruker"
       override val propertyName: String = "foedselsnummer"
       override val propertyType: String = "no.nav.pensjon.brevbaker.api.model.Foedselsnummer"
       override val selector = no.nav.pensjon.brevbaker.api.model.Bruker::foedselsnummer
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Bruker>.foedselsnummer: Expression<no.nav.pensjon.brevbaker.api.model.Foedselsnummer>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(foedselsnummerSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker>.foedselsnummer: Expression<no.nav.pensjon.brevbaker.api.model.Foedselsnummer>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(foedselsnummerSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker?>.foedselsnummer_safe: Expression<no.nav.pensjon.brevbaker.api.model.Foedselsnummer?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(foedselsnummerSelector)
       )
    
    val fornavnSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Bruker, kotlin.String> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Bruker"
       override val propertyName: String = "fornavn"
       override val propertyType: String = "kotlin.String"
       override val selector = no.nav.pensjon.brevbaker.api.model.Bruker::fornavn
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Bruker>.fornavn: Expression<kotlin.String>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(fornavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker>.fornavn: Expression<kotlin.String>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(fornavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker?>.fornavn_safe: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(fornavnSelector)
       )
    
    val mellomnavnSelector = object : TemplateModelSelector<no.nav.pensjon.brevbaker.api.model.Bruker, kotlin.String?> {
       override val className: String = "no.nav.pensjon.brevbaker.api.model.Bruker"
       override val propertyName: String = "mellomnavn"
       override val propertyType: String = "kotlin.String?"
       override val selector = no.nav.pensjon.brevbaker.api.model.Bruker::mellomnavn
    }
    
    val TemplateGlobalScope<no.nav.pensjon.brevbaker.api.model.Bruker>.mellomnavn: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           Expression.FromScope.Argument(),
           UnaryOperation.Select(mellomnavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker>.mellomnavn: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.Select(mellomnavnSelector)
       )
    
    val Expression<no.nav.pensjon.brevbaker.api.model.Bruker?>.mellomnavn_safe: Expression<kotlin.String?>
       get() = Expression.UnaryInvoke(
           this,
           UnaryOperation.SafeCall(mellomnavnSelector)
       )
    

}
