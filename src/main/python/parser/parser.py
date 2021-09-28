#!/usr/bin/python3.7
import csv
import stemmer
import reader
import sys

# parse posted_at, (tech) tags, location (city, country), salary (range or number)
def parse(postlines, tags_dict, cities_dict, lands_dict):
    stemmed_word_pairs = stemmer.stem_lines(postlines)
    tags = []
    city = None
    land = None
    for (original_word, stemmed_word) in stemmed_word_pairs:
        if stemmed_word in tags_dict:
            tags.append(original_word)
        if city is None and original_word.lower() in cities_dict:
            city = original_word
        if city is None and land is None and original_word.lower() in lands_dict:
            land = original_word
    return (tags, city, land)

def read_cities_from_csv(source_file):
    cities = []
    with open(source_file) as f:
        csvreader = csv.reader(f, delimiter = ";")
        for row in csvreader:
            cities.append(row[2].lower())
    return cities

def read_countries_from_csv(source_file):
    countries = []
    with open(source_file) as f:
        csvreader = csv.reader(f, delimiter = ";")
        for row in csvreader:
            countries.append(row[2].lower())
    return countries

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
    countries = read_countries_from_csv("countries.csv")
    cities = read_cities_from_csv("cities.csv")

    vacancy = reader.read_vacancy_raw_data("raw_data.dat", 0 if len(sys.argv) < 2 else int(sys.argv[1]))
    print(vacancy)
    print(parse(vacancy.split("\n"), tags_dict, cities, countries))
