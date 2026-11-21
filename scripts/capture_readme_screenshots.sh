#!/usr/bin/env bash
# Capture one README screenshot per feature from a running emulator or device.
#
# Prerequisites: a booted emulator (e.g. Pixel 6, API 34) visible to adb and a
# debug build (./gradlew :app:assembleDebug). The script installs the APK,
# clears app data, switches to dark mode and writes docs/readme-assets/*.png.
#
# Usage: scripts/capture_readme_screenshots.sh
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$ROOT/docs/readme-assets"
APK="$ROOT/app/build/outputs/apk/debug/randomizer-debug.apk"
WIDTH=360
export PATH="$PATH:${ANDROID_HOME:-$HOME/Android/Sdk}/platform-tools"

# Tap the centre of the first view whose text, content-desc or resource-id matches.
tap() {
    local query="$1" index="${2:-0}"
    adb shell uiautomator dump /sdcard/ui.xml >/dev/null
    local xy
    xy=$(adb shell cat /sdcard/ui.xml | python3 -c '
import re, sys
query, index = sys.argv[1], int(sys.argv[2])
hits = []
for node in re.findall(r"<node [^>]*>", sys.stdin.read()):
    if any(f"{a}=\"{query}\"" in node for a in ("text", "content-desc", "resource-id")):
        x1, y1, x2, y2 = map(int, re.search(r"bounds=\"\[(\d+),(\d+)\]\[(\d+),(\d+)\]\"", node).groups())
        hits.append(f"{(x1 + x2) // 2} {(y1 + y2) // 2}")
if len(hits) <= index:
    sys.exit(f"no view matches {query!r}")
print(hits[index])
' "$query" "$index")
    adb shell input tap $xy
    sleep 0.8
}

type_text() {
    adb shell input text "$1"
    sleep 0.4
}

enter() {
    adb shell input keyevent KEYCODE_ENTER
    sleep 0.6
}

hide_keyboard() {
    if adb shell dumpsys input_method | grep -q "mInputShown=true"; then
        adb shell input keyevent KEYCODE_BACK
        sleep 0.6
    fi
}

back() {
    adb shell input keyevent KEYCODE_BACK
    sleep 1
}

shot() {
    sleep "${2:-1.5}"
    adb exec-out screencap -p | magick - -resize "${WIDTH}x" -strip "$OUT/$1.png"
    echo "wrote docs/readme-assets/$1.png"
}

adb wait-for-device
adb install -r "$APK" >/dev/null
adb shell pm clear carl.coding >/dev/null

# Clean status bar.
adb shell settings put global sysui_demo_allowed 1
adb shell am broadcast -a com.android.systemui.demo -e command enter >/dev/null
adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1200 >/dev/null
adb shell am broadcast -a com.android.systemui.demo -e command battery -e level 100 -e plugged false >/dev/null
adb shell am broadcast -a com.android.systemui.demo -e command network -e wifi show -e level 4 >/dev/null
adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false >/dev/null
trap 'adb shell am broadcast -a com.android.systemui.demo -e command exit >/dev/null' EXIT

adb shell am start -W -n carl.coding/.StartMenu >/dev/null
sleep 1.5
tap "Switch to dark mode"
sleep 1

tap "Coin"; tap "Flip"; shot coin; back
tap "Dice"; tap "Roll"; shot dice; back

tap "Yatzy dice"; tap "Roll"; sleep 1.5
tap "carl.coding:id/die_image" 1
tap "carl.coding:id/die_image" 3
shot yatzy 0.5; back

tap "Yes or no"; tap "Decide"; shot yes-no 0.8; back

tap "Categories"
tap "Category name"
for option in Pizza Sushi Tacos Curry Pasta; do type_text "$option"; enter; done
hide_keyboard
tap "Withdraw picked option from the pool"
tap "Start"; tap "Pick next"; shot categories 2; back; back

tap "Team randomizer"
tap "Player name"
for player in Alex Billie Charlie Dana Eli Frankie; do type_text "$player"; enter; done
hide_keyboard
tap "Shuffle teams"; shot teams 1.5; back; back

tap "Number generator"
tap "Minimum"; type_text 1
tap "Maximum"; type_text 100
enter
shot number 2
