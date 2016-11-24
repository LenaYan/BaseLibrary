/*
 *
 *  Copyright (c) 2016 Lena.t.Yan
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and confidential.
 *  Created on Fri, 11 Nov 2016 21:59:16 +0800.
 *  ProjectName: V2EXAndroidClient ; ModuleName: app ; ClassName: TopicListCellVM.
 *  Author: Lena; Last Modified: Fri, 11 Nov 2016 21:59:16 +0800.
 *  This file is originally created by Lena.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.ray.mvvm.lib.model.service;

import android.support.annotation.StringDef;

import com.ray.mvvm.lib.model.model.ListRespEntity;
import com.ray.mvvm.lib.model.model.topic.TopicEntity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

import static com.ray.mvvm.lib.model.service.TopicService.TopicType.ANDROID;
import static com.ray.mvvm.lib.model.service.TopicService.TopicType.IOS;
import static com.ray.mvvm.lib.model.service.TopicService.TopicType.WEB;

/**
 * Created by Android Studio.
 * ProjectName: V2EXAndroidClient
 * Author:  Lena.t.Yan
 * Date: 1/19/16
 * Time: 15:26
 * \ ___________________
 * \| Happy New Year!  |
 * \ -------------------
 * \  \
 * \   \   \_\_    _/_/
 * \    \      \__/
 * \           (oo)\_______
 * \           (__)\       )\/\
 * \               ||----w |
 * \               ||     ||
 */
public interface TopicService {

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ANDROID, IOS, WEB})
    @interface TopicType {
        String KEY = "TopicType";
        String ANDROID = "Android";
        String IOS = "iOS";
        String WEB = "前端";
    }

    @GET("data/{type}/{pageSize}/{page}")
    Observable<ListRespEntity<TopicEntity>> topicList(@TopicType @Path("type") String type, @Path("pageSize") int pageSize, @Path("page") int page);

}
