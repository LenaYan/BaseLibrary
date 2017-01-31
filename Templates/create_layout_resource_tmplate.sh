#!/bin/bash
DIR="/Applications/Android Studio.app/Contents/plugins/android/lib/templates/other/MVVMLibLayoutRes/"
if [ -d "$DIR" ]; then
    printf '%s\n' "MVVMLibLayoutResTmp is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r "MVVMLibLayoutResTmp" "$DIR"

