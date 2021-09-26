# -*- coding: utf-8 -*-
import json
from flask import request, Response
from flask import Flask
from flask_cors import CORS

app = Flask(__name__)
#to avoid CORS
CORS(app, resources={r"*": {"origins": "*"}})


tags = ["C#", "SQL", "HDFS", "C"]

countries = {
    "Germany": ["Frankfurt", "Berlin", "Hamburg"],
    "France": ["Paris", "Strasburg"],
    "Estonia": ["Tallinn"]
}

@app.route("/tags/", methods=['GET'])
def get_tags():
    content = {"payload": tags}
    response = Response(response=json.dumps(content), status=200, mimetype='application/json')
    return response

@app.route("/countries/", methods=['GET'])
def get_countries():
    content = {"payload": list(countries.keys())}
    response = Response(response=json.dumps(content), status=200, mimetype='application/json')
    return response

@app.route("/cities/<country>", methods=['GET'])
def get_cities(country):
    cities = countries.get(country)
    if cities is None:
        raise Exception('Country {0} not found!'.format(country))
    content = {"payload": cities}
    response = Response(response=json.dumps(content), status=200, mimetype='application/json')
    return response
