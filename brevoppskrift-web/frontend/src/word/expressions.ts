import { type DataSource, type Expr, ExprType } from "~/api/templateDocumentationTypes";

export function expressionText(expr: Expr): string {
  switch (expr.exprType) {
    case ExprType.LITERAL:
      return expr.kind === "STRING" ? JSON.stringify(expr.value) : expr.value;
    case ExprType.FIELD_PATH:
      return [sourceText(expr.source), ...expr.segments].filter(Boolean).join(".");
    case ExprType.ASSOCIATIVE_OP: {
      const operator = { AND: " og ", OR: " eller ", CONCAT: " + ", PLUS: " + " }[expr.op];
      return `(${expr.operands.map(expressionText).join(operator)})`;
    }
    case ExprType.COMPARISON: {
      const operator = {
        EQUAL: "=",
        NOT_EQUAL: "!=",
        GREATER_THAN: ">",
        GREATER_THAN_OR_EQUAL: ">=",
        LESS_THAN: "<",
        LESS_THAN_OR_EQUAL: "<=",
      }[expr.op];
      return `(${expressionText(expr.left)} ${operator} ${expressionText(expr.right)})`;
    }
    case ExprType.NOT:
      return `ikke (${expressionText(expr.term)})`;
    case ExprType.FUNCTION_CALL:
      return `${expr.name}(${expr.args.map(expressionText).join(", ")})`;
    case ExprType.FORMAT:
      return `${expr.formatterName}(${expressionText(expr.value)})`;
    case ExprType.CONDITIONAL_EXPR:
      return `hvis (${expressionText(expr.predicate)}) så ${expressionText(expr.ifTrue)} ellers ${expressionText(expr.ifElse)}`;
    case ExprType.NULL_COALESCE:
      return `(${expressionText(expr.value)} ?: ${expressionText(expr.fallback)})`;
    case ExprType.EDITABLE_FIELD:
      return `${expr.kind}(${[expr.value, expr.fallback]
        .filter((value) => value !== undefined)
        .map(expressionText)
        .join(", ")})`;
  }
}

function sourceText(source: DataSource): string {
  switch (source.dataSourceType) {
    case "SCOPE":
      return source.name;
    case "FOR_EACH_VAR":
      return `${source.label} (løkke ${source.depth})`;
    case "COMPUTED":
      return `(${expressionText(source.expr)})`;
  }
}
