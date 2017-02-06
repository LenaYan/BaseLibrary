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

package com.ray.mvvm.lib.model.http.adapter;

import com.ray.mvvm.lib.model.model.GenericRespEntity;
import com.ray.mvvm.lib.model.model.VoidEntity;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class GenericRespEntityAdapter {

    @ToJson
    public GenericRespEntity<VoidEntity> toJson(GenericRespEntity genericRespEntity) {
        GenericRespEntity<VoidEntity> entity = new GenericRespEntity<>();
        entity.setCode(genericRespEntity.getCode());
        entity.setMessage(genericRespEntity.getMessage());
        return entity;
    }

    @FromJson
    public GenericRespEntity fromJson(GenericRespEntity<VoidEntity> genericRespEntity) {
        GenericRespEntity entity = new GenericRespEntity();
        entity.setCode(genericRespEntity.getCode());
        entity.setMessage(genericRespEntity.getMessage());
        return entity;
    }


}
