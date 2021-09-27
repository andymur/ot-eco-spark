#https://towardsdatascience.com/connecting-the-dots-python-spark-and-kafka-19e6beba6404
#!/usr/bin/python3.7

import random
import kafka

kafka_cluster_url = "localhost:29092"
topic_name = "books"

producer = kafka.KafkaProducer(bootstrap_servers=kafka_cluster_url, value_serializer=str.encode, key_serializer=str.encode)

for ind in range(1, 10):
    event_stream_key = 'identifier_' + str(ind)
    event_stream_value = 'bookval_' + str(random.randint(0, 100))
    print(event_stream_key + ":" + event_stream_value)
    producer.send(topic_name, key = event_stream_key, value = event_stream_value)
    producer.flush()
