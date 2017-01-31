#!/bin/bash
DIR="/Applications/Android Studio.app/Contents/plugins/android/lib/templates/activities/MVVMListActivity/"
if [ -d "$DIR" ]; then
    printf '%s\n' "MVVMListActivityTmp is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r "MVVMListActivityTmp" "$DIR"