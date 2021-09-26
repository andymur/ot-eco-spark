#!/usr/bin/python3.7
import sys
import stemmer

vacancy_delimiter = "########## "

line_replacement_rules = [("\n#", " "), (" #", " "), ("@", ""), (".com", "")]


def read_vacancies_raw_data(source_file):
    vacancies_data = []
    current_vacancy_lines = []
    with open(source_file) as f:
        for line in f:
            if line.startswith(vacancy_delimiter):
                new_vacancy = True
                if current_vacancy_lines:
                    vacancies_data.append("".join(current_vacancy_lines))
                current_vacancy_lines = [stemmer.replace(line, [(vacancy_delimiter, "")])]
                continue
            if new_vacancy:
                new_vacancy = False
            current_vacancy_lines.append(stemmer.replace(line, line_replacement_rules))
    return vacancies_data

def read_vacancy_raw_data(source_file, index):
    return read_vacancies_raw_data(source_file)[index]

def read_words_set(source_file, adder):
    words = set()
    with open(source_file) as f:
        for line in f:
            words.update(stemmer.stem_line(line, adder))
    return words

def read_stemmed_words_set(source_file):
    return read_words_set(source_file, lambda w: stemmer.stem(w))

def read_original_stemmed_pairs_set(source_file):
    return read_words_set(source_file, lambda w: (w, stemmer.stem(w)))

if __name__ == "__main__":
    print(read_vacancy_raw_data("./raw_data.dat", int(sys.argv[1])))
