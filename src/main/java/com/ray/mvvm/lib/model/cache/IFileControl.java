/*
 *  Copyright (C) 2015 Rayman Yan
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.ray.mvvm.lib.model.cache;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

public interface IFileControl {

    <T> T get(String key);

    <T> T get(String key, T defaultValue);

    <T> void put(String key, T value);

    int remove(String key);

    boolean exists(String key);

    void cacheCheck();

    void cleanCacheUpdateVersion();

    String requestCacheFloderPath();

    File getCacheImageFile(String fileName);

    File getCacheApkFile(String fileName);

    void clearApks();

    boolean clearCache();

    float getCacheSize() throws IOException;

    void saveImageFileSync(Bitmap bitmap);

    void saveImageFileSync(String fileName, Bitmap bitmap);

}
