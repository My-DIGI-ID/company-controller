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

var username = "";
var password = "";

var db = connect("mongodb://" + username + ":" + password + "@localhost:27017/admin");

db = db.getSiblingDB('CompanyController');

db.createUser(
    {
        user: username,
        pwd: password,
        roles: [ { role: "readWrite", db: "CompanyController"} ],
        passwordDigestor: "server",
    }
)

db.createRole(
  {
    role : "readWriteSystem",
    privileges:
      [
        {
          resource:
            {
              db: "CompanyController",
              collection: "system.indexes"
            },
          actions:
            [ "changeStream", "collStats", "convertToCapped", "createCollection", "createIndex", "dbHash", "dbStats", "dropCollection", "dropIndex", "emptycapped", "find", "insert", "killCursors", "listCollections", "listIndexes", "planCacheRead", "remove", "renameCollectionSameDB", "update"
            ]
         }
       ],
    roles:[]})

db.grantRolesToUser(username, ['readWriteSystem'])
