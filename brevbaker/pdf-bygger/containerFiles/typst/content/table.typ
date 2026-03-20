#import "state.typ": updateElementType, lastElementType
#import "../input.typ": languageSettings

// Colors matching LaTeX table styling
#let columnheadercolor = rgb("#E6F0FF")
#let linecolor = rgb("#7C8695")
#let headersepcolor = rgb("#000000")
#let row1color = rgb("#F2F3F5")
#let row2color = rgb("#FFFFFF")

#let tableTitle(title) = {
  context {
    if(lastElementType.get() != "title") {
      block(above: 26pt, sticky: true)[
        #text(size: 12pt, weight: "bold", tracking: 0.2pt, title)
      ]
    } else {
      block(sticky: true)[
        #text(size: 12pt, weight: "bold", tracking: 0.2pt, title)
      ]
    }
  }
  updateElementType("title")
}

#let letterTable(columns, caption, ..cells) = {
  tableTitle(caption)

  let cellArray = cells.pos()
  let numColumns = columns.len()
  let numRows = calc.ceil(cellArray.len() / numColumns)

  set table(
    columns: columns,
    column-gutter: 0pt,
    row-gutter: 0pt,
    inset: (x: 8pt, y: 6.4pt),
    stroke: (x, y) => (
      left: none,
      right: none,
      top: if y == 0 { none } else if y == 1 { 1pt + headersepcolor } else { 1pt + linecolor },
      bottom: 1pt + linecolor,
    ),
    fill: (x, y) => {
      if y == 0 {
        columnheadercolor
      } else if calc.odd(y) {
        row1color
      } else {
        row2color
      }
    },
  )

  // Build cells with header styling
  let styledCells = cellArray.enumerate().map(((i, cell)) => {
    let row = calc.floor(i / numColumns)
    if row == 0 {
      text(weight: "semibold", tracking: 0.2pt, size: 11pt, cell)
    } else {
      cell
    }
  })

  block(above: 5.5mm, below: 3.5mm)[
    #table(
      ..styledCells
    )
  ]

  updateElementType("table")
}