import { flattenBlocks, indexFields } from "./draftOps";
import { newId } from "./ids";
import { type CommentAnchor, type Draft, type DraftComment } from "./types";

/** Forfatter og tidspunkt stemples av serveren. Klienten setter dem aldri. */
export function addComment(
  comments: readonly DraftComment[],
  input: { anchor: CommentAnchor; text: string; role?: string; scenarioId?: string },
): DraftComment[] {
  const text = input.text.trim();
  if (text === "") {
    return [...comments];
  }
  const comment: DraftComment = {
    id: newId(),
    anchor: input.anchor,
    text,
    replies: [],
    states: [],
    role: input.role?.trim() ? input.role.trim() : null,
    scenarioId: input.scenarioId ?? null,
  };
  return [...comments, comment];
}

/** Historikk beholdes: svar og statusinnslag legges bare til, aldri endres eller slettes. */
export function addReply(comments: readonly DraftComment[], commentId: string, text: string): DraftComment[] {
  const trimmed = text.trim();
  if (trimmed === "") {
    return [...comments];
  }
  return comments.map((c) =>
    c.id === commentId ? { ...c, replies: [...c.replies, { id: newId(), text: trimmed }] } : c,
  );
}

export function isResolved(comment: DraftComment): boolean {
  return comment.states.at(-1)?.resolved ?? false;
}

export function setResolved(comments: readonly DraftComment[], commentId: string, resolved: boolean): DraftComment[] {
  return comments.map((c) =>
    c.id === commentId && isResolved(c) !== resolved ? { ...c, states: [...c.states, { id: newId(), resolved }] } : c,
  );
}

/** Kommentaren er foreldreløs når ankeret ikke lenger finnes. Den beholdes likevel synlig. */
export function isOrphaned(comment: DraftComment, draft: Draft): boolean {
  const { blockId, fieldId, choiceId } = comment.anchor;
  if (blockId && !flattenBlocks(draft.outline).some((f) => f.block.id === blockId)) {
    return true;
  }
  if (fieldId && !indexFields(draft.fields).has(fieldId)) {
    return true;
  }
  if (choiceId && !draft.choices.some((c) => c.id === choiceId)) {
    return true;
  }
  return false;
}

export function commentsForBlock(comments: readonly DraftComment[] | undefined, blockId: string): DraftComment[] {
  return (comments ?? []).filter((c) => c.anchor.blockId === blockId);
}

export function openCommentCount(comments: readonly DraftComment[] | undefined): number {
  return (comments ?? []).filter((c) => !isResolved(c)).length;
}
