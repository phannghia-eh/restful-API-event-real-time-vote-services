package com.vng.talktv.Services;

import com.vng.talktv.Cache.CacheProviderContainer;
import org.infinispan.commons.api.BasicCache;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.*;

public class GenericCacheService<K, V> {
    protected BasicCache<K, V> cache;
    @EJB
    CacheProviderContainer cacheProviderContainer;

    @PostConstruct
    public void initCache() {
        setCache((BasicCache<K, V>) cacheProviderContainer.<String, Integer>getCache(Integer.class));
    }

    public void put(K key, V value) {
        if (key != null && value != null) {
            this.cache.put(key, value);
        }
    }

    public V get(K key) {
        if (key == null) {
            return null;
        }
        return this.cache.get(key);
    }

    public List<V> values() {
        Collection<V> all = this.cache.values();
        return new ArrayList<>(all);
    }

    public void remove(Object key) {
        if (key != null) {
            this.cache.remove(key);
        }
    }

    public void clearAll() {
        this.cache.clear();
    }

    public Set<K> keySet() {
        Set<K> keySet = this.cache.keySet();
        return new HashSet<>(keySet);
    }

    public BasicCache<K, V> getCache() {
        return cache;
    }

    public void setCache(BasicCache<K, V> cache) {
        this.cache = cache;
    }
}
