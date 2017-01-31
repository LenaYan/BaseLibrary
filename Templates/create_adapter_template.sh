#!/bin/bash
DIR="/Applications/Android Studio.app/Contents/plugins/android/lib/templates/other/MVVMAdapter/"
if [ -d "$DIR" ]; then
    printf '%s\n' "Template is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r "MVVMAdapterTmp" "$DIR"