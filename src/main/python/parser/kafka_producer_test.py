# https://towardsdatascience.com/connecting-the-dots-python-spark-and-kafka-19e6beba6404
#!/usr/bin/python3.7

import kafka
from parser import stemmer
from parser import reader
import json

kafka_cluster_url = "localhost:29092"
topic_name = "in_msg"

producer = kafka.KafkaProducer(bootstrap_servers=kafka_cluster_url, value_serializer=lambda v: json.dumps(v).encode('utf-8'), key_serializer=str.encode)

ind = 0
for vac in reader.read_vacancies_raw_data("./raw_data.dat"):
    ind += 1
    event_stream_key = 'identifier_' + str(ind)
    event_stream_value = vac
    print(event_stream_key + ":" + event_stream_value)
    producer.send(topic_name, key = event_stream_key, value = event_stream_value)
    producer.flush()
