#!/usr/bin/python3.7
import nltk
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize

vacancy_delimiter = "Jobs abroad ["
line_replacement_rules = [(" #", " "), ("@", ""), (".com", "")]
word_replacement_rules = [(" #", " "), ("@", ""), (".com", ""), ("/", " "), ("-", " "), (",", " ")]

ps = PorterStemmer()

def replace(original_line, rules):
    result_line = original_line
    for rule in rules:
        result_line = result_line.replace(rule[0], rule[1])
    return result_line

def read_vacancies_raw_data(source_file):
    vacancies_data = []
    current_vacancy_lines = []
    with open(source_file) as f:
        for line in f:
            if line.startswith(vacancy_delimiter):
                new_vacancy = True
                if current_vacancy_lines:
                    vacancies_data.append("".join(current_vacancy_lines))
                    current_vacancy_lines = []
                continue
            if new_vacancy:
                new_vacancy = False
            current_vacancy_lines.append(replace(line, line_replacement_rules))
    return vacancies_data


def read_words(source_file, adder):
    words = set()
    with open(source_file) as f:
        for line in f:
            for word in replace(line, word_replacement_rules).lower().split(" "):
                words.add(adder(word))
    return words

def read_stemmed_words(source_file):
    return read_words(source_file, lambda w: ps.stem(w))


def read_vacancy_raw_data(source_file, index):
    return read_vacancies_raw_data(source_file)[index]

if __name__ == "__main__":
    words = read_stemmed_words("./raw_data.dat")
    for w in words:
        print(w)
    print(len(words))
