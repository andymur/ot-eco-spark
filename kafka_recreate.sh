#!/bin/bash
./kafka-topics.sh --bootstrap-server localhost:29092 --delete --topic in_msg --if-exists
./kafka-topics.sh --bootstrap-server localhost:29092 --delete --topic out_msg --if-exists
./kafka-topics.sh --bootstrap-server localhost:29092 --create --topic in_msg
./kafka-topics.sh --bootstrap-server localhost:29092 --create --topic out_msg
