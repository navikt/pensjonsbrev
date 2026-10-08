#!/bin/bash
# Puts an RTF file on the macOS clipboard as RTF (public.rtf) and, like WordPad, as plain text.
#   rtf-file-to-clipboard.sh [<file.rtf>] [--rtf-only | --source-as-text] [--menu]
# Without a file, pick a fixture from skribenten-web/frontend/test/fixtures/rtf (incl. subfolders, e.g. rtfpipe/List.rtf):
# with fzf if installed, otherwise (or with --menu) from a numbered three-column menu.
#   --rtf-only        RTF without the plain-text flavour
#   --source-as-text  the RTF source as plain text, for the console snippet that dispatches a synthetic text/rtf paste
# NB: Chrome/Edge on macOS derive text/html from the RTF, so the RTF path is only exercised in browsers that don't.
set -euo pipefail

FIXTURES="${FIXTURES:-$(git rev-parse --show-toplevel 2>/dev/null)/skribenten-web/frontend/test/fixtures/rtf}"
USAGE="Usage: $0 [<file.rtf>] [--rtf-only | --source-as-text] [--menu]"

file=""
mode=""
menu=false
for arg in "$@"; do
  case "$arg" in
    --rtf-only | --source-as-text) mode="$arg" ;;
    --menu) menu=true ;;
    -h | --help) echo "$USAGE"; exit 0 ;;
    -*) echo "Unknown option: $arg" >&2; echo "$USAGE" >&2; exit 1 ;;
    *) file="$arg" ;;
  esac
done

fixtures=()
while IFS= read -r fixture; do fixtures+=("$fixture"); done < <(cd "$FIXTURES" && find . -name "*.rtf" | sed "s|^\./||" | sort)

# Numbered, in three columns, sorted down each column.
print_columns() {
  local count=${#fixtures[@]} rows width=0 row col index line
  rows=$(((count + 2) / 3))
  for fixture in "${fixtures[@]}"; do ((${#fixture} > width)) && width=${#fixture}; done
  for ((row = 0; row < rows; row++)); do
    line=""
    for ((col = 0; col < 3; col++)); do
      index=$((col * rows + row))
      ((index < count)) && line+="$(printf "%3d) %-*s  " $((index + 1)) "$width" "${fixtures[index]}")"
    done
    echo "${line%"${line##*[! ]}"}"
  done
}

if [[ -z "$file" ]]; then
  if [[ ! -t 0 ]]; then
    echo "$USAGE" >&2
    print_columns >&2
    exit 1
  elif ! $menu && command -v fzf > /dev/null; then
    file="$(printf "%s\n" "${fixtures[@]}" | fzf --style full --height 60% --reverse  --prompt "RTF fixture> " \
      --preview "textutil -convert txt -stdout '$FIXTURES'/{} 2>/dev/null | head -60")" || exit 1
  else
    print_columns
    read -rp "Pick a number: " choice
    [[ "$choice" =~ ^[0-9]+$ ]] && ((choice >= 1 && choice <= ${#fixtures[@]})) || { echo "Invalid choice: $choice" >&2; exit 1; }
    file="${fixtures[choice - 1]}"
  fi
  echo "Picked: $file"
fi

name="$file"
[[ -f "$file" ]] || file="$FIXTURES/$name"
[[ -f "$file" ]] || { echo "Not found: $name" >&2; exit 1; }

if [[ "$mode" == "--source-as-text" ]]; then
  pbcopy -Prefer txt < "$file"
  echo "RTF source put on the clipboard as plain text"
  exit 0
fi

plain=""
[[ "$mode" == "--rtf-only" ]] || plain="$(textutil -convert txt -stdout "$file")"

osascript -l JavaScript - "$file" "$plain" <<'EOF'
ObjC.import("AppKit");
function run(argv) {
  const data = $.NSData.dataWithContentsOfFile(argv[0]);
  const pb = $.NSPasteboard.generalPasteboard;
  if (pb.isNil()) throw new Error("No access to the clipboard (is the script running in a sandbox?)");
  pb.clearContents;
  pb.setDataForType(data, $.NSPasteboardTypeRTF);
  if (argv[1]) pb.setStringForType($(argv[1]), $.NSPasteboardTypeString);
  return "The clipboard now holds: " + pb.types.js.map((type) => type.js).join(", ");
}
EOF
