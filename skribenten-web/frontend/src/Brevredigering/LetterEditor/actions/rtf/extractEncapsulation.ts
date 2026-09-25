import {
  type ByteDecoder,
  createByteDecoder,
  decodeUnicodeParam,
  RTF_SYMBOL_WORDS,
  type RtfControlToken,
  type RtfToken,
  skipUnicodeFallback,
  tokenizeRtf,
} from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/**
 * Outlook puts mail bodies on the clipboard as RTF that wraps the original HTML (`\fromhtml1`) or
 * plain text (`\fromtext`). This restores the original, following MS-OXRTFEX section 2:
 * https://learn.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxrtfex
 *
 * - `{\*\htmltag …}` holds HTML markup, emitted as is.
 * - Text outside htmltag is document text, emitted HTML-escaped.
 * - `\htmlrtf` … `\htmlrtf0` wraps RTF-only duplicates of the content, which are dropped.
 */

export type EncapsulatedContent = { format: "html"; html: string } | { format: "text"; text: string };

type Format = EncapsulatedContent["format"];

/** The spec requires `\fromhtml1`/`\fromtext` in the header, before any other group or text. */
const HEADER_SCAN_LIMIT = 10;

const SKIPPED_DESTINATIONS: ReadonlySet<string> = new Set([
  "colortbl",
  "fonttbl",
  "info",
  "pict",
  "pntext",
  "stylesheet",
]);

type Destination = "body" | "htmltag" | "skip";

interface GroupState {
  destination: Destination;
  htmlrtf: boolean;
  ignorableIfUnknown: boolean;
  ucSkip: number;
}

interface DeencapsulationContext {
  format: Format;
  decodeBytes: ByteDecoder;
  groups: GroupState[];
  output: string[];
  pendingBytes: number[];
  unicodeSkip: number;
}

function detectFormat(tokens: RtfToken[]): Format | undefined {
  if (tokens[0]?.type !== "groupStart") return undefined;
  for (const token of tokens.slice(1, 1 + HEADER_SCAN_LIMIT)) {
    if (token.type !== "control") return undefined;
    if (token.word === "fromhtml" && token.param === 1) return "html";
    if (token.word === "fromtext") return "text";
  }
  return undefined;
}

const escapeHtml = (text: string) => text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");

const currentGroup = (ctx: DeencapsulationContext): GroupState => ctx.groups.at(-1)!;

function emit(ctx: DeencapsulationContext, value: string) {
  const group = currentGroup(ctx);
  if (value.length === 0 || group.destination === "skip" || group.htmlrtf) return;

  if (group.destination === "htmltag") {
    if (ctx.format === "html") ctx.output.push(value);
  } else {
    ctx.output.push(ctx.format === "html" ? escapeHtml(value) : value);
  }
}

function flushPendingBytes(ctx: DeencapsulationContext) {
  if (ctx.pendingBytes.length === 0) return;
  const decoded = ctx.decodeBytes(ctx.pendingBytes);
  ctx.pendingBytes = [];
  emit(ctx, decoded);
}

function handleControlWord(ctx: DeencapsulationContext, token: RtfControlToken) {
  const group = currentGroup(ctx);
  if (group.destination === "skip") return;

  if (token.word === "*") {
    group.ignorableIfUnknown = true;
  } else if (group.ignorableIfUnknown) {
    group.ignorableIfUnknown = false;
    group.destination = token.word === "htmltag" ? "htmltag" : "skip";
  } else if (SKIPPED_DESTINATIONS.has(token.word)) {
    group.destination = "skip";
  } else if (token.word === "rtf" && ctx.groups.length > 2) {
    // Outlook appends mail signatures as a nested, plain RTF document.
    group.destination = "body";
    group.htmlrtf = false;
  } else if (token.word === "htmlrtf") {
    group.htmlrtf = !token.hasParam || token.param !== 0;
  } else if (token.word === "uc") {
    group.ucSkip = Math.max(0, token.param);
  } else if (token.word === "u") {
    emit(ctx, decodeUnicodeParam(token.param));
    ctx.unicodeSkip = group.ucSkip;
  } else if (token.word === "par" || token.word === "line") {
    emit(ctx, "\r\n");
  } else {
    const symbol = RTF_SYMBOL_WORDS.get(token.word);
    if (symbol !== undefined) emit(ctx, symbol);
  }
}

/** Returns the original HTML or text of Outlook's encapsulating RTF, or undefined for other RTF. */
export function extractEncapsulatedContent(rtf: string): EncapsulatedContent | undefined {
  const tokens = tokenizeRtf(rtf);
  const format = detectFormat(tokens);
  if (!format) return undefined;

  const ctx: DeencapsulationContext = {
    format,
    decodeBytes: createByteDecoder(rtf),
    groups: [{ destination: "body", htmlrtf: false, ignorableIfUnknown: false, ucSkip: 1 }],
    output: [],
    pendingBytes: [],
    unicodeSkip: 0,
  };

  for (const rawToken of tokens) {
    if (rawToken.type !== "hexByte") flushPendingBytes(ctx);

    let token: RtfToken | undefined = rawToken;
    if (ctx.unicodeSkip > 0) {
      const skip = skipUnicodeFallback(ctx.unicodeSkip, rawToken);
      ctx.unicodeSkip = skip.remaining;
      token = skip.rest;
    }
    if (!token) continue;

    switch (token.type) {
      case "groupStart": {
        ctx.groups.push({ ...currentGroup(ctx), ignorableIfUnknown: false });
        break;
      }
      case "groupEnd": {
        if (ctx.groups.length > 1) ctx.groups.pop();
        break;
      }
      case "hexByte": {
        ctx.pendingBytes.push(token.byte);
        break;
      }
      case "text": {
        if (currentGroup(ctx).ignorableIfUnknown) currentGroup(ctx).destination = "skip";
        emit(ctx, token.value);
        break;
      }
      case "control": {
        handleControlWord(ctx, token);
        break;
      }
    }
  }
  flushPendingBytes(ctx);

  const content = ctx.output.join("");
  return format === "html" ? { format, html: content } : { format, text: content };
}
