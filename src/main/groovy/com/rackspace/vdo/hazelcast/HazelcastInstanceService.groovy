package com.rackspace.vdo.hazelcast

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.rackspace.vdo.stdlib.logging.Logger
import com.rackspace.vdo.stdlib.logging.LoggerFactory

/**
 * Created by anil1693 on 2/1/17.
 */
class HazelcastInstanceService {

    Logger log = LoggerFactory.getLogger(getClass().name)

    /**
     * Creates a new Hazelcast instance from the given Map of configuration values.
     *
     * @param configuration
     * @return String
     */
    void createInstances(List<Map> configurations) {
        configurations.each  {instanceConfig ->
            Config hazelConfig = createConfig(instanceConfig)
            try {
                createInstance(hazelConfig)
            }
            catch(Exception e) {
                log.error("Not able to create hazelcast instance: ${instanceConfig.instanceName}, reason: ${e.message}", e)
            }
        }
    }

    /**
     * This method will be used to create the hazelcast config.
     *
     * @param instanceConfiguration
     * @return Config
     */
    Config createConfig(Map instanceConfiguration) throws Exception {
        Config hazelConfig = new Config()

        instanceConfiguration.keySet().each { instanceProperty ->
            if(hazelConfig.getMetaClass().hasProperty(hazelConfig, instanceProperty)) {
                hazelConfig.getMetaClass().setProperty(hazelConfig, instanceProperty, instanceConfiguration[instanceProperty])
            }
            else {
                throw new Exception("Property: ${instanceProperty} is not a valid property for Hazelcast Config class. Please check the configuration")
            }
        }

        return hazelConfig
    }

    /**
     * Creates a new Hazelcast instance from the given configuration.
     *
     * @param configuration
     * @return HazelcastInstance
     */
    HazelcastInstance createInstance(Config configuration) {
        return Hazelcast.newHazelcastInstance(configuration)
    }

    /**
     * Returns the Hazelcast instance associated with the given name.
     *
     * @param name Name of the Hazelcast instance.
     * @return Hazelcast instance associated with the given name.
     * @throws IllegalArgumentException when no instance exists for the given name.
     */
    HazelcastInstance getInstance(String name) throws IllegalArgumentException {
        HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(name)

        if (!instance) {
            throw new IllegalArgumentException("no Hazelcast instance with name \"$name\" exists")
        }
    }
}
