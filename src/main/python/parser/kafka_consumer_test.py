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
#producer = KafkaProducer(bootstrap_servers=kafka_cluster_url, value_serializer=str.encode, key_serializer=str.encode)

for msg in consumer:
    postlines = msg.value.split("\n")
    # parse date, tags, city, country, salary
    (tags) = parser.parse(postlines, tags_dict, {}, {})
    # remove first line as date 
    print("message come: {0} with tags {1}".format(msg.value, str(tags)))
