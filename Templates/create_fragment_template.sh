#!/bin/bash
fragTmpFirName="RayBasicFragmentTmp"
DIR="/Applications/Android Studio.app/Contents/plugins/android/lib/templates/other/${fragTmpFirName}/"
if [ -d "$DIR" ]; then
    printf '%s\n' "Template is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r "LibFragmentTmp"/ "$DIR"

