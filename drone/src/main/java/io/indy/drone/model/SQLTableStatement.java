/*
 * Copyright 2013 Inderjit Gill
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

package io.indy.drone.model;

import java.util.ArrayList;
import java.util.List;

// generates strings that will be passed into SQLite

public class SQLTableStatement {

    private String mTableName;

    private List<String> mCols;

    public SQLTableStatement(String tableName) {
        mTableName = tableName;
        mCols = new ArrayList<String>();
    }

    public SQLTableStatement text(String name) {
        mCols.add(name + " text");
        return this;
    }

    public SQLTableStatement text(String name, String params) {
        mCols.add(name + " text " + params);
        return this;
    }

    public SQLTableStatement integer(String name) {
        mCols.add(name + " integer");
        return this;
    }

    public SQLTableStatement integer(String name, String params) {
        mCols.add(name + " integer " + params);
        return this;
    }

    public SQLTableStatement real(String name) {
        mCols.add(name + " real");
        return this;
    }

    public SQLTableStatement real(String name, String params) {
        mCols.add(name + " real " + params);
        return this;
    }

    public SQLTableStatement timestamp(String name) {
        mCols.add(name + " timestamp");
        return this;
    }

    public SQLTableStatement timestamp(String name, String params) {
        mCols.add(name + " timestamp " + params);
        return this;
    }

    public String create() {
        String comp = "";
        for (String unit : mCols) {
            comp = comp + unit + ",";
        }
        comp = comp.substring(0, comp.length() - 1);
        return "create table " + mTableName + " (" + comp + ");";
    }

    public String drop() {
        return "drop table if exists " + mTableName;
    }
}
