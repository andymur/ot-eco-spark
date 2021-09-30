#!/usr/bin/python3
import psycopg2

connection_string = "dbname=otus user=otus password=otus host=localhost"


def post_exists(post_hash_code, conn):
    cur = conn.cursor()
    cur.execute("SELECT hash_code FROM posts WHERE hash_code = %s", (post_hash_code,))
    return cur.fetchone() is not None

def persist_post_data(hash_code, posted_at, city_id, land_id, tags_ids, raw_text):
    with psycopg2.connect(connection_string) as conn:
        if post_exists(hash_code, conn):
            print("post with hashcode {} exists, skipping...".format(hash_code))
        else:
            cur = conn.cursor()
            cur.execute("INSERT INTO posts(hash_code, posted_at, city_id, land_id, tags, words) VALUES(%s, %s, %s, %s, %s, %s)", 
                (hash_code, posted_at, city_id, land_id, tags_ids, raw_text))

if __name__ == "__main__":
    with psycopg2.connect(connection_string) as conn:
        print(post_exists('0a5966d4376562640ba08c12e41fb882', conn))
