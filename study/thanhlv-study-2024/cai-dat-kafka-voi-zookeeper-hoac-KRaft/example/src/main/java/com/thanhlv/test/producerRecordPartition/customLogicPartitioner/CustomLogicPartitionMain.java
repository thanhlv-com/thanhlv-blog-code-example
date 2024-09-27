package com.thanhlv.test.producerRecordPartition.customLogicPartitioner;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class CustomLogicPartitionMain {
    @SneakyThrows
    public static void main(String[] args) throws IOException {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ALL");
        final var props = new Properties();
        props.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "java-producer-producerRecordPartition-KeyNotNull");
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29091,localhost:29092,localhost:29093,localhost:29094");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "5000");
        // ghi đề logic xác định partition. Data là class path đến class ClassCustomLogicPartitioner
        props.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, "org.apache.kafka.clients.producer.RoundRobinPartitioner");

        try (var producer = new KafkaProducer<Object, String>(props)) {
            final var messageProducerRecord = new ProducerRecord<>(
                    "topic-rep-1-partition-10",     //topic name
                    // 36 byte
                    UUID.randomUUID().toString()        // value
            );
            for (int i = 1; i <= 5000; i++) {
                producer.send(messageProducerRecord);
            }
        }
    }
}
