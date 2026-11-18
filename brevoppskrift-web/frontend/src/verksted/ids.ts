/** Alle nye noder (blokker, felt, valg, scenarier, kommentarer) får en stabil UUID fra nettleseren. */
export function newId(): string {
  return crypto.randomUUID();
}
