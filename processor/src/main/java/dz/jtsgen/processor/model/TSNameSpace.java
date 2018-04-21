/*
 * Copyright (c) 2017 Dragan Zuvic
 *
 * This file is part of jtsgen.
 *
 * jtsgen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jtsgen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jtsgen.  If not, see http://www.gnu.org/licenses/
 *
 */

package dz.jtsgen.processor.model;

import dz.jtsgen.processor.util.StringUtils;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Tree structure of namespaces with Types
 */
public class TSNameSpace {
    private final String name;
    private final List<TSType> types;
    private final Map<String,TSNameSpace> children;


    private TSNameSpace(String name, List<TSType> types, Map<String, TSNameSpace> children) {
        this.name = name;
        this.types = types;
        this.children = children;
    }

    public TSNameSpace() {
        this("", new ArrayList<>(), new HashMap<>());
    }

    private TSNameSpace(String name) {
        this(name, new ArrayList<>(), new HashMap<>());
    }

    public TSNameSpace findOrCreate(String namespace) {
        requireNonNull(namespace, "namespace");

        if (this.name.equals(namespace)) return this;

        final String car = StringUtils.car(namespace);
        final Optional<String> cdr = StringUtils.cdr(namespace);

        final TSNameSpace child;
        if (this.children.containsKey(car)) {
            child = this.children.get(car);
        }
        else {
            child=new TSNameSpace(car);
            this.children.put(car,child);
        }
        return cdr.map(child::findOrCreate).orElse(child);
    }

    public void addType(TSType type) {
        this.types.add(type);
    }

    @Override
    public String toString() {
        return "TSNameSpace{" + "name='" + name + '\'' +
                ", types=" + types +
                ", children=" + children +
                '}';
    }


    public boolean isRoot() {
        return "".equals(this.name);
    }

    public String getName() {
        return name;
    }

    public List<TSType> getTypes() {
        return types;
    }

    public Set<String> getSubNamespaces() {
        return this.children.keySet();
    }

    public TSNameSpace getSubNameSapce(String thatName) {
        if (!this.children.containsKey(thatName)) throw new IllegalArgumentException("no namespace " + thatName + " in current node " + this.name);
        else return this.children.get(thatName);
    }

    // only for tests
    Map<String, TSNameSpace> getChildren() {
        return children;
    }
}
