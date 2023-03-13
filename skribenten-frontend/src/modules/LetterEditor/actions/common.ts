export enum MergeTarget { PREVIOUS = "PREVIOUS", NEXT = "NEXT" }

export function cleanseText(text: string): string {
    return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ")
}