#!/usr/bin/python3.7
import reader
import sys
import csv

starting_symbols = "abcdefghijklmnopqrstuvwxyz."

def map_stemmed_pairs_by_letters(word_pairs):
    result = {}
    for original_word, stemmed_word in word_pairs:
        if not len(original_word):
            continue
        key_symbol = original_word[0]
        if key_symbol not in result:
            result[key_symbol] = set()
        result[key_symbol].add((stemmed_word, original_word))
    return result

def create_rows(word_pairs):
    rows = []
    for stem, word in word_pairs:
        rows.append({'stem': stem, 'word': word})
    return rows

def print_rows(rows):
    for row in rows:
        print("'{0}';'{1}';true;true".format(row['stem'], row['word']))

def save_rows(output_file, rows):
    with open(output_file, 'w') as f:
        writer = csv.writer(f, delimiter = ";", quotechar="'", quoting = csv.QUOTE_ALL)
        for row in rows:
            writer.writerow([row['stem'], row['word'], 'true', 'true'])

if __name__ == "__main__":
    symbol = None if len(sys.argv) < 2 else sys.argv[1]
    file_name = symbol + '_tech_tags.csv' if symbol else 'tech_tags.csv'
    output_file = file_name if len(sys.argv) < 3 else sys.argv[2]

    word_pairs_map = map_stemmed_pairs_by_letters(reader.read_original_stemmed_pairs_set("./raw_data.dat"))
    if symbol:
        word_pairs = word_pairs_map[symbol]
    else:
        word_pairs = set()
        for starting_symbol in starting_symbols:
            word_pairs.update(word_pairs_map[starting_symbol])

    rows = create_rows(sorted(word_pairs))
    print(print_rows(rows))
    save_rows(output_file, rows)
