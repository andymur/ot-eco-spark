#!/usr/bin/python3.7
from kafka import KafkaConsumer
from kafka import KafkaProducer
import parser
import json

kafka_cluster_url = "localhost:29092"
topic_name = "books"
out_topic_name = "out_msg"

tags_dict = parser.read_tags_dict_from_csv("./tech_tags_edited.csv")

consumer = KafkaConsumer(topic_name, bootstrap_servers=[kafka_cluster_url], value_deserializer= json.loads)
producer = KafkaProducer(bootstrap_servers=kafka_cluster_url, value_serializer=str.encode, key_serializer=str.encode)

for msg in consumer:
    print(msg.value)
    postlines = msg.value.split("\n")
    (tags) = parser.parse(postlines, tags_dict, {}, {})
    # remove first line / parse as date
    event_stream_key = str(hash(msg.value))
    event_stream_value = str(tags)
    producer.send(out_topic_name, key = event_stream_key, value = event_stream_value)
    producer.flush()
