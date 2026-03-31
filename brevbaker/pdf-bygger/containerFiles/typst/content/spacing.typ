// Collision matrix for spacing between element types
// Format: "from-to": space (vertical space to add above the element)
#let spacingMatrix = (
  // From "start" (beginning of document/section)
  "start-title": 28pt,
  "start-paragraph": 33pt,
  "start-list": 17.5pt,
  "start-table": 28pt,
  "start-form": 23pt,

  // From "title"
  "title-title": 5pt,
  "title-paragraph": 12pt,
  "title-list": 10.5pt,
  "title-table": 11pt,
  "title-form": 12pt,

  // From "paragraph"
  "paragraph-title": 29pt,
  "paragraph-paragraph": 25pt,
  "paragraph-list": 9pt,
  "paragraph-table": 18pt,
  "paragraph-form": 16pt,

  // From "list"
  "list-title": 23pt,
  "list-paragraph": 26.5pt,
  "list-list": 26pt,
  "list-table": 20pt,
  "list-form": 25pt,

  // From "table"
  "table-title": 26pt,
  "table-paragraph": 27pt,
  "table-list": 18pt,
  "table-table": 25pt,
  "table-form": 16pt,

  // From "form"
  "form-title": 26pt,
  "form-paragraph": 25pt,
  "form-list": 26pt,
  "form-table": 16pt,
  "form-form": 28pt,
)

// Get spacing for transitioning from previous element to current element
#let getSpacing(fromType, toType) = {
  let key = fromType + "-" + toType
  spacingMatrix.at(key)
}
