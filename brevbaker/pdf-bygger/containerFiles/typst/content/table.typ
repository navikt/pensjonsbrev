
#import "../input.typ": languageSettings

#let columnheadercolor = rgb("#E6F0FF")
#let linecolor = rgb("#7C8695")
#let headersepcolor = rgb("#000000")
#let row1color = rgb("#F2F3F5")
#let row2color = rgb("#FFFFFF")

#let next-page-table(title: none, ..table-args) = context {
  let columns = table-args.named().at("columns", default: 1)
  let column-amount = if type(columns) == int {
    columns
  } else if type(columns) == array {
    columns.len()
  } else {
    1
  }

  // Extract header cells from the positional arguments
  // The first `column-amount` positional args are header cells
  let pos-args = table-args.pos()
  let header-cells = pos-args.slice(0, calc.min(column-amount, pos-args.len()))
  let body-cells = pos-args.slice(calc.min(column-amount, pos-args.len()))

  // Counter of tables so we can create a unique table-part-counter for each table
  let table-counter = counter("table")
  table-counter.step()

  // Counter for the amount of pages in the table
  // It is increased by one for each footer repetition
  let table-part-counter = counter("table-part" + str(table-counter.get().first()))

  show <table-footer>: footer => {
    table-part-counter.step()
    context {
      if table-part-counter.get() != table-part-counter.final() {
        // Display the footer only if we aren't at the last page
        footer
      }
    }
  }

  show <table-header>: it => {
    table-part-counter.step()
    context {
      if table-part-counter.get().first() != 1 {
        // Display the "continued from previous page" message only on continuation pages
        it
      }
    }
  }

  // Build styled header cells
  let styled-header-cells = header-cells.map(cell =>
    table.cell(
      inset: (x: 8pt, y: 11pt),
      fill: columnheadercolor,
    )[#text(weight: "semibold", tracking: 0.2pt, cell)]
  )

  table(
    columns: columns,
    column-gutter: 0pt,
    row-gutter: 0pt,
    inset: (x: 8pt, y: 6.4pt),
    stroke: (x, y) => (
      left: none,
      right: none,
      top: if y <= 1 { none } else if y == 2 { 1pt + headersepcolor } else { 1pt + linecolor },
      bottom: 1pt + linecolor,
    ),
    fill: (x, y) => {
      if y <= 1 {
        // Header rows (continuation message + actual headers)
        none  // Fill is set per-cell for headers
      } else if calc.even(y) {
        row1color
      } else {
        row2color
      }
    },
    table.header(
      // Continuation message - shown only on subsequent pages via the show rule
      table.cell(
        inset: 0pt,
        colspan: column-amount,
        stroke: none,
        fill: white,
      )[#languageSettings.tablecontinuedfrompreviouspage <table-header>],
      // Actual header cells - styled with bold text and extra padding
      ..styled-header-cells,
    ),
    // Body cells
    ..body-cells,
    table.footer(
      // The 'next page' content spans all columns and has no stroke
      // Must be selectable by the show rule above which hides it at the last page
      table.cell(
        inset: 0pt,
        colspan: column-amount,
        stroke: none,
        fill: white,
        align(right)[#languageSettings.tablenextpagecontinuation <table-footer>],
      ),
    ),
  )

  // Compensate for the empty footer at the last page of the table
  v(-1em)
}