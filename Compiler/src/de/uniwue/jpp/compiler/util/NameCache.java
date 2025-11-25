package de.uniwue.jpp.compiler.util;

import java.net.CacheRequest;
import java.util.*;

public interface NameCache {
    public static NameCache create() {
        return new NameCache() {
            final List<String> list = new ArrayList<>();
            final Map<String,Integer> map = new HashMap<>();
            int i1 ;
            @Override
            public int cacheName(String name) {
                int i = list.indexOf(name);

                if (i != -1)
                    return i;
                list.add(name);

                /*if (map.containsKey(name))
                    return map.get(name);
                map.put(name,i1);
                return i1++;*/
                return list.size()-1;
            }
            @Override
            public String getName(int id) {
                return list.get(id);
            }
        };
    }

    public int cacheName(String name);
    public String getName(int id);

}
