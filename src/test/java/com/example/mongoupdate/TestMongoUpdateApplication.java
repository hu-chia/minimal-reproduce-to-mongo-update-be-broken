package com.example.mongoupdate;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration.EnableWebFluxConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestMongoUpdateApplication {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    }

    public static void main(String[] args) {
        var app = SpringApplication.from(MongoUpdateApplication::main).with(TestMongoUpdateApplication.class).run(args);
        var mongo = app.getApplicationContext().getBean(ReactiveMongoTemplate.class);
        
        var update = new Update();
        update.set("child.mapmap.1012.2", 1);
        
        mongo.upsert(Query.query(Criteria.where("_id").is("12345")), update, ExampleModel.class).block();

        System.out.println(mongo.find(Query.query(Criteria.where("_id").is("12345")), ExampleModel.class).blockFirst());
    }

    static class ExampleModel {
        
        private Child child = new Child();

        public Child getChild() {
            return child;
        }

        public void setChild(Child child) {
            this.child = child;
        }

        @Override
        public String toString() {
            return "ExampleModel{" +
                "child=" + child +
                '}';
        }
    }
    
    static class Child {
        
        private Map<String, Map<String, String>> mapmap;

        public Map<String, Map<String, String>> getMapmap() {
            return mapmap;
        }

        public void setMapmap(Map<String, Map<String, String>> mapmap) {
            this.mapmap = mapmap;
        }

        @Override 
        public String toString() {
            return "Child{" +
                "mapmap=" + mapmap +
                '}';
        }
    }
}
