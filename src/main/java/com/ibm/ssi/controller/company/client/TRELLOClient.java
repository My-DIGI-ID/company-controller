/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.ssi.controller.company.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "TRELLOClient", url = "${ssibk.company.controller.trello.apiurl}")
public interface TRELLOClient {

    @RequestMapping(method = RequestMethod.POST, value = "/1/cards")
   void createCard(@RequestParam(value="key") String key, @RequestParam(value="token") String token, @RequestParam(value="idList") String idList, @RequestParam(value="name") String name, @RequestParam(value="idLabels") String idLabels);
}
