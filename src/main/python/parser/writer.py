#!/usr/bin/python3
import psycopg2

connection_string = "dbname=otus user=otus password=otus host=localhost"


def persist_post_data(posted_at, city_id, land_id, tags_ids, raw_text):
    with psycopg2.connect(connection_string) as conn:
        cur = conn.cursor()
        cur.execute("INSERT INTO posts(posted_at, city_id, land_id, tags, words) VALUES(%s, %s, %s, %s, %s)", (posted_at, city_id, land_id, tags_ids, raw_text))

if __name__ == "__main__":
    persist_post_data(202012, 1, None, "{1,2}", "hello");
