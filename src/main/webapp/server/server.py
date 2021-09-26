# -*- coding: utf-8 -*-
import json
import random
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
    return create_response(tags)

@app.route("/countries/", methods=['GET'])
def get_countries():
    return create_response(list(countries.keys()))

@app.route("/cities/<country>", methods=['GET'])
def get_cities(country):
    cities = countries.get(country)
    if cities is None:
        raise Exception('Country {0} not found!'.format(country))
    return create_response(cities)

@app.route("/jobstatistics/", methods=['POST'])
def search():
    job_stats = [{'year': 2020, 'month': 1, 'count': random.randint(10, 20)}, 
             {'year': 2020, 'month': 2, 'count': random.randint(10, 20)}, 
             {'year': 2020, 'month': 3, 'count': random.randint(10, 20)}, 
             {'year': 2020, 'month': 4, 'count': random.randint(10, 20)}, 
             {'year': 2020, 'month': 5, 'count': random.randint(10, 20)}, 
            ]
    print(type(request.json))
    print(request.json['fromPeriod'])
    print(request.json['toPeriod'])
    print(request.json['city'])
    print(request.json['country'])
    print(request.json['tags'])
    return create_response(job_stats)

def create_response(payload):
    content = {"payload": payload}
    return Response(response=json.dumps(content), status=200, mimetype='application/json')