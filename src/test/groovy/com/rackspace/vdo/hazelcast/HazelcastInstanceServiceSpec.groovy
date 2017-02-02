package com.rackspace.vdo.hazelcast

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import spock.lang.Specification

/**
 * Created by anil1693 on 2/1/17.
 */
class HazelcastInstanceServiceSpec extends Specification {
    HazelcastInstanceService hazelcastInstanceService

    def setup() {
        hazelcastInstanceService = new HazelcastInstanceService()
    }

    void "Create successful hazelcast config"() {
        setup:
        Map instanceConfiguration = [
            instanceName: 'instance1',
            groupConfig: [
                name: 'group1',
                password: 'pwd1'
            ]
        ]

        when:
        Config hazelcastConfig = hazelcastInstanceService.createConfig(instanceConfiguration)

        then:
        hazelcastConfig
        hazelcastConfig.instanceName.equalsIgnoreCase("instance1")
        hazelcastConfig.getGroupConfig().name.equalsIgnoreCase("group1")
        hazelcastConfig.getGroupConfig().password.equalsIgnoreCase("pwd1")
    }

    void "Fail hazelcast config creation because of invalid config property name"() {
        setup:
        Map instanceConfiguration = [
            instanceName: 'instance1',
            groupInfo: [
                name: 'group1',
                password: 'pwd1'
            ]
        ]

        when:
        Exception testException
        try {
            hazelcastInstanceService.createConfig(instanceConfiguration)
        }
        catch(Exception e) {
            testException = e
        }

        then:
        testException
        testException.message.equalsIgnoreCase("Property: groupInfo is not a valid property for Hazelcast Config class. Please check the configuration")
    }

    void "Create successful hazelcast instances"() {
        setup:
        List<Map> instanceConfigurations = [
            [
                instanceName: 'instance1',
                groupConfig: [
                    name: 'group1',
                    password: 'pwd1'
                ]
            ]
        ]

        when:
        hazelcastInstanceService.createInstances(instanceConfigurations)
        HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName('instance1')

        then:
        instance
        instance.name.equalsIgnoreCase("instance1")
    }
}
