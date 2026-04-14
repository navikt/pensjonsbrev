// Collision matrix for spacing between element types
// Format: "from-to": space (vertical space to add above the element)
#let spacingMatrix = (
  // From "start" (beginning of document/section)
  "start-title": 26pt,
  "start-paragraph": 26pt,
  "start-list": 26pt,
  "start-table": 26pt,
  "start-form": 26pt,

  // From "title"
  "title-title": 10pt,
  "title-paragraph": 10pt,
  "title-list": 10pt,
  "title-table": 10pt,
  "title-form": 10pt,

  // From "paragraph"
  "paragraph-title": 28pt,
  "paragraph-paragraph": 16pt,
  "paragraph-list": 11pt,
  "paragraph-table": 16pt,
  "paragraph-form": 22pt,

  // From "list"
  "list-title": 28pt,
  "list-paragraph": 16pt,
  "list-list": 16pt,
  "list-table": 22pt,
  "list-form": 22pt,

  // From "table"
  "table-title": 28pt,
  "table-paragraph": 16pt,
  "table-list": 16pt,
  "table-table": 22pt,
  "table-form": 16pt,

  // From "form"
  "form-title": 28pt,
  "form-paragraph": 22pt,
  "form-list": 16pt,
  "form-table": 16pt,
  "form-form": 22pt,
)

// Get spacing for transitioning from previous element to current element
#let getSpacing(fromType, toType) = {
  let key = fromType + "-" + toType
  spacingMatrix.at(key)
}
