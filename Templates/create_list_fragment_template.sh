#!/bin/bash
DIR="/Applications/Android Studio.app/Contents/plugins/android/lib/templates/other/MVVMListFragment/"
if [ -d "$DIR" ]; then
    printf '%s\n' "MVVMListFragmentTmp is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r "MVVMListFragmentTmp"/ "$DIR"

