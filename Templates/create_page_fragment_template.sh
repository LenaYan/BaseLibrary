#!/bin/bash
DIR="/Applications/Android Studio.app/Contents/plugins/android/lib/templates/other/MVVMPageFragment/"
if [ -d "$DIR" ]; then
    printf '%s\n' "MVVMPageFragment is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r "MVVMPageFragmentTmp"/ "$DIR"

