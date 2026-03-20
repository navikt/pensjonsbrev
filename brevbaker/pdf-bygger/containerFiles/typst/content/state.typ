#let lastElementType = state("lastElementType", "title")

#let updateElementType(elementType) = {
  lastElementType.update(elementType)
}

#let sectionCounter = counter("section-counter")

#let section-start(sectionNumber) = {
  sectionCounter.step()
    counter(page).update(1)
  [#metadata("section-start")#label("section-start-" + str(sectionNumber))]
}

#let section-end(sectionNumber) = {
  [#metadata("section-end")#label("section-end-" + str(sectionNumber))]
}


// todo reset in attachment