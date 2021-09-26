#!/usr/bin/python3.7
import csv
import stemmer
import reader
import sys

# parse (tech) tags, location (city, country), salary (range or number)
def parse(postlines, tags_dict, cities_dict, lands_dict):
    #print(postlines)
    stemmed_word_pairs = stemmer.stem_lines(postlines)
    tags = []
    for (original_word, stemmed_word) in stemmed_word_pairs:
        if stemmed_word in tags_dict:
            tags.append(original_word)
    return (tags)


def read_tags_dict_from_csv(source_file):
    tags_dict = {}
    with open(source_file) as f:
        csvreader = csv.reader(f, delimiter = ";", quotechar="'", quoting = csv.QUOTE_ALL)
        for row in csvreader:
            if row[0] not in tags_dict:
                tags_dict[row[0]] = []
            tags_dict[row[0]].append(row[1])

    return tags_dict

if __name__ == "__main__":
    tags_dict = read_tags_dict_from_csv("tech_tags_edited.csv")
    vacancy = reader.read_vacancy_raw_data("raw_data.dat", 0 if len(sys.argv) < 2 else int(sys.argv[1]))
    print(vacancy)
    print(parse(vacancy.split("\n"), tags_dict, {}, {}))
