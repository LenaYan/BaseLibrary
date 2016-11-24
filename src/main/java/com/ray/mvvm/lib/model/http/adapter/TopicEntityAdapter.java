/*
 *
 *  Copyright (c) 2016 Lena.t.Yan
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and confidential.
 *  Created on Sat, 8 Oct 2016 23:38:42 +0800.
 *  ProjectName: V2EXAndroidClient ; ModuleName: app ; ClassName: TopicListCellVM.
 *  Author: Lena; Last Modified: Sat, 8 Oct 2016 23:38:42 +0800.
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

package com.ray.mvvm.lib.model.http.adapter;

import com.ray.mvvm.lib.model.model.common.RealmString;
import com.ray.mvvm.lib.model.model.topic.TopicEntity;
import com.ray.mvvm.lib.model.model.topic.TopicJsonEntity;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class TopicEntityAdapter {

    @ToJson
    public TopicJsonEntity toJson(TopicEntity topicEntity) {
        TopicJsonEntity topicJsonEntity = new TopicJsonEntity();
        topicJsonEntity.setId(topicEntity.getId());
        topicJsonEntity.setCreateAt(topicEntity.getCreateAt());
        topicJsonEntity.setDesc(topicEntity.getDesc());
        topicJsonEntity.setPublishedAt(topicEntity.getPublishedAt());
        topicJsonEntity.setSource(topicEntity.getSource());
        topicJsonEntity.setType(topicEntity.getType());
        topicJsonEntity.setUrl(topicEntity.getUrl());
        topicJsonEntity.setWho(topicEntity.getWho());
        List<String> imgList = new ArrayList<>();
        for (RealmString realmString : topicEntity.getImages()) {
            imgList.add(realmString.getValue());
        }
        topicJsonEntity.setImages(imgList);
        return topicJsonEntity;
    }

    @FromJson
    public TopicEntity fromJson(TopicJsonEntity topicJsonEntity) {
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setId(topicJsonEntity.getId());
        topicEntity.setCreateAt(topicJsonEntity.getCreateAt());
        topicEntity.setDesc(topicJsonEntity.getDesc());
        topicEntity.setPublishedAt(topicJsonEntity.getPublishedAt());
        topicEntity.setSource(topicJsonEntity.getSource());
        topicEntity.setType(topicJsonEntity.getType());
        topicEntity.setUrl(topicJsonEntity.getUrl());
        topicEntity.setWho(topicJsonEntity.getWho());
        if (topicJsonEntity.getImages() != null && topicJsonEntity.getImages().size() > 0) {
            RealmList<RealmString> images = new RealmList<>();
            for (String s : topicJsonEntity.getImages()) {
                images.add(new RealmString(s));
            }
            topicEntity.setImages(images);
        }
        return topicEntity;
    }


}
