package com.thanhlv.test.retention;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Demo {
    @SneakyThrows
    public static void main(String[] args) throws IOException {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ALL");
        final var props = new Properties();
        props.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "java-producer-producerRecordPartition-KeyNotNull");
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29091,localhost:29092,localhost:29093,localhost:29094");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (var producer = new KafkaProducer<String, String>(props)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));) {
                while (true) {
                    log.debug("Enter number random message: ");
                    String number = br.readLine().trim();
                    Long start = System.currentTimeMillis();
                    log.info("Start: {} ms",start);
                    final var messageProducerRecord = new ProducerRecord<>(
                            "topic-retention-demo",     //topic name
                            "lethanh2",
                            UUID.randomUUID().toString()         // value
                    );
                    Integer numberSend = Integer.parseInt(number);
                    CountDownLatch countDownLatch=new CountDownLatch(numberSend);
                    for (int i = 0; i < numberSend; i++) {
                        producer.send(new ProducerRecord<>(
                                "topic-retention-demo",     //topic name
                                "1",
                                UUID.randomUUID().toString()         // value
                        ), (metadata, exception) -> countDownLatch.countDown());
                    }
                    countDownLatch.await();
                    Long end = System.currentTimeMillis();
                    log.info("END: {} ms and end - start = {}",end,end - start);
                }
            }
        }
    }
}
