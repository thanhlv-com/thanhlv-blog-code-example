package com.thanhlv.test.clusternode;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        newThreadConsumer(createPropertiesByGroupId("1"),"app-1");
        newThreadConsumer(createPropertiesByGroupId("1"),"app-2");
        newThreadConsumer(createPropertiesByGroupId("1"),"app-3");
        newThreadConsumer(createPropertiesByGroupId("1"),"app-4");
        newThreadConsumer(createPropertiesByGroupId("1"),"app-5");
        while (true) {
            Thread.sleep(100);
        }
    }

    public static void newThreadConsumer(Properties props,String name) {
        new Thread(() -> {
            // create consumer
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

            consumer.subscribe(Arrays.asList("my-topic-2"));
            // poll for new data
            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("name: " + name + " , Group_id: " + props.get(ConsumerConfig.GROUP_ID_CONFIG) + " , Key: " + record.key() + ", Value: " + record.value() + ", Partition: " + record.partition() + ", Offset:" + record.offset());
                }
            }
        }).start();
    }

    public static Properties createPropertiesByGroupId(String groupId) {
        final var props = new Properties();
        props.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "java-Consumer");
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:29093");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return props;
    }
}
