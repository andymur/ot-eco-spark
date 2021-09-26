#!/bin/bash

virtualenv env && source ./env/bin/activate && pip install -r requirements.txt
export FLASK_APP=users.py && flask run
