#!/usr/bin/python3.7
from kafka import KafkaConsumer

kafka_cluster_url = "localhost:29092"
topic_name = "books"

consumer = KafkaConsumer(topic_name, bootstrap_servers=[kafka_cluster_url])
for msg in consumer:
    print(msg.value)
