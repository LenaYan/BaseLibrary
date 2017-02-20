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

package com.ray.mvvm.lib.model.service;

import com.ray.mvvm.lib.model.model.topic.TopicEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

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

    @GET("topics/hot.json")
    Single<List<TopicEntity>> hot();

    @GET("topics/latest.json")
    Single<List<TopicEntity>> latest();

    @GET("topics/show.json")
    Single<List<TopicEntity>> topicsByUserName(@Query(value = "username") String userName);

    @GET("topics/show.json")
    Single<List<TopicEntity>> topicsByNodeId(@Query(value = "node_id") long nodeId);

    @GET("topics/show.json")
    Single<List<TopicEntity>> topicsByNodeName(@Query(value = "node_name") String nodeName);

    @GET("topics/show.json")
    Single<List<TopicEntity>> topicById(@Query(value = "id") long id);
}
