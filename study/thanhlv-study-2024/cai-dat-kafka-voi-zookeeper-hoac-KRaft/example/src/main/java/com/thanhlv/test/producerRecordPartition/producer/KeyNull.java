package com.thanhlv.test.producerRecordPartition.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class KeyNull {
    public static void main(String[] args) throws IOException {

        final var props = new Properties();
        props.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "java-producer-producerRecordPartition-KeyNotNull");
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:29093");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Khi tong so luong data gui = BATCH_SIZE_CONFIG thi se tinh toan lai partition moi
        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "10000");

        try (var producer = new KafkaProducer<Object, String>(props)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));) {

                while (true) {
                    log.info("Enter number random message: ");
                    String number = br.readLine().trim();
                    for (int i = 0; i < Integer.parseInt(number); i++) {
                        final var messageProducerRecord = new ProducerRecord<>(
                                "my-topic-2",     //topic name
                                UUID.randomUUID().toString()        // value
                        );
                        producer.send(messageProducerRecord);
                    }
                }
            }
        }

    }
}
