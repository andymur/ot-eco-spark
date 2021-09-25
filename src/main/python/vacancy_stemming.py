#!/usr/bin/python3

from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize

if __name__ == "__main__":
    lines = []
    with open('./vacancies_raw_content.txt') as f:
        for l in f:
            lines.append(l)

    ps = PorterStemmer()
    stemmed_words = []

    for line in lines:
        line_words = word_tokenize(line)
        for word in line_words:
            stemmed_words.append(ps.stem(word))

    print(stemmed_words)

