#!/usr/bin/python3

from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize

word_replacement_rules = [("\n#", " "), (" #", " "), ("@", ""), (".com", ""),
                          ("/", " "), ("-", " "), (",", " "),   ("\n", " "),
                          ("(", " "), (")", " "), (";", " ")]

ps = PorterStemmer()

def stem(w):
    return ps.stem(w)

def stem_line(line, adder):
    words = set()
    # word_tokenize(replace(line, word_replacement_rules).lower())
    for word in replace(line, word_replacement_rules).lower().split(" "):
        words.add(adder(word))
    return words

def stem_lines(lines):
    words = set()
    for line in lines:
        words.update(stem_line(line, lambda w: (w, stem(w))))
    return words

def replace(original_line, rules):
    result_line = original_line
    for rule in rules:
        result_line = result_line.replace(rule[0], rule[1])
    return result_line

