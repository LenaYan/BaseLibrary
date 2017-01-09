#!/bin/bash
layoutResTmpDir="LayoutResourceFile"
DIR="/Applications/Android Studio.app/Contents/plugins/android/lib/templates/other/${layoutResTmpDir}/"
if [ -d "$DIR" ]; then
    printf '%s\n' "Template is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r "LibLayoutResTmp" "$DIR"

