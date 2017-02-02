package com.rackspace.vdo.hazelcast.beans

import com.rackspace.vdo.hazelcast.HazelcastInstanceService
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by anil1693 on 2/1/17.
 */
class HazelcastMapFactoryBean<K, V> implements FactoryBean<Map<K, V>>, InitializingBean {
    /**
     * Hazelcast context containing hazelcast instance available to the application.
     */
    @Autowired
    HazelcastInstanceService hazelcastContext

    /**
     * Name of the hazelcast instance that should contain the map.
     */
    String hazelcastInstanceName

    /**
     * Name of the hazelcast map.
     */
    String hazelcastMapName

    @Override
    Map<K, V> getObject() throws Exception {
        return hazelcastContext.getInstance(hazelcastInstanceName).getMap(hazelcastMapName)
    }

    @Override
    Class<?> getObjectType() {
        return Map.class
    }

    @Override
    boolean isSingleton() {
        return true
    }

    @Override
    void afterPropertiesSet() throws Exception {
        if (!hazelcastMapName) {
            throw new IllegalArgumentException('name of the Hazelcast map may not be null or empty')
        }

        if (!hazelcastInstanceName) {
            throw new IllegalArgumentException('name of the hazelcast instance may not be null or empty')
        }
    }
}
