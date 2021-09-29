#!/usr/bin/python3.7
from kafka import KafkaConsumer
from kafka import KafkaProducer
import parser
import json
import hashlib
import writer

kafka_cluster_url = "localhost:29092"
topic_name = "in_msg"

tags_dict = parser.read_tags_dict_from_csv("./tags.csv")
cities_dict = parser.read_cities_from_csv("./cities.csv")
countries_dict = parser.read_countries_from_csv("./countries.csv")

consumer = KafkaConsumer(topic_name, bootstrap_servers=[kafka_cluster_url], value_deserializer = json.loads)

def calc_md5_hash(raw_text):
    return hashlib.md5(raw_text.encode('utf-8')).hexdigest()

def stringify_tags(tags_list):
    return "{'" + "', '".join(tags_list) + "'}"

if __name__ == "__main__":
    for msg in consumer:
        postlines = msg.value.split("\n")
        raw_text = postlines[1:]
        md5sum = calc_md5_hash("\n".join(raw_text))

        # parse date, tags, city, country, salary
        (posted_at, tags, city_info, land_info) = parser.parse(postlines, tags_dict, cities_dict, countries_dict)
        city_id = None
        land_id = None
        if city_info:
            city_id = city_info[0]
            land_id = city_info[1]
        elif land_info:
            land_id = land_info[0]
        print("hash: {}, posted_at: {}, city_id: {}, land_id: {}, tags: {}".format(md5sum, posted_at, city_id, land_id, stringify_tags(tags)))
        writer.persist_post_data(str(md5sum), posted_at, city_id, land_id, stringify_tags(tags), "\n".join(raw_text))
