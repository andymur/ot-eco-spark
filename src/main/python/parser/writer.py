#!/usr/bin/python3
import psycopg2

connection_string = "dbname=otus user=otus password=otus host=localhost"


def post_exists(post_hash_code, conn):
    return False

def persist_post_data(hash_code, posted_at, city_id, land_id, tags_ids, raw_text):
    with psycopg2.connect(connection_string) as conn:
        if post_exists(hash_code, conn):
            pass
        else:
            cur = conn.cursor()
            cur.execute("INSERT INTO posts(hash_code, posted_at, city_id, land_id, tags, words) VALUES(%s, %s, %s, %s, %s, %s)", 
                (hash_code, posted_at, city_id, land_id, tags_ids, raw_text))

if __name__ == "__main__":
    persist_post_data('615b2e14e6ca60a673d8205a5de45dc7', 202012, 1, None, "{'1','2'}", "hello");
