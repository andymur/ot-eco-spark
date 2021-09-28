#!/usr/bin/python3.7
import csv
import sys

countries = {}
cities_by_country = {}
c_id = 0
city_id = 0

if __name__ == "__main__":

    format = 'sql' if len(sys.argv) < 2 else sys.argv[1]
    entity = 'country' if len(sys.argv) < 3 else sys.argv[2]

    with open("./worldcities.csv") as f:
        reader = csv.reader(f, delimiter=",")
        next(reader, None)
        for row in reader:
            code = row[6]
            country_name = row[4]
            city_name = row[0]
            if code not in countries:
                c_id += 1
                countries[code] = (c_id, code, row[4])
                cities_by_country[code] = []
            cities_by_country[code].append(city_name)
    

    if entity == 'country':
        for c_id, iso_code, name in list(countries.values()):
            if format == 'sql':
                print("INSERT INTO lands(id, iso_code, name) values ({0}, '{1}', '{2}');".format(c_id, iso_code, name))
            else:
                print("{0};{1};{2}".format(c_id, iso_code, name))
    else:
        for c_id, iso_code, name in list(countries.values()):
            for city_name in cities_by_country[iso_code]:
                city_id += 1
                city_name = city_name.replace("'", "")
                if format == 'sql':
                    print("INSERT INTO cities(id, land_id, name) VALUES ({0}, {1}, '{2}');".format(city_id, c_id, city_name))
                else:
                    print("{0};{1};{2}".format(c_id, c_id, city_name))

    #print(countries)
    #print(cities_by_country['RUS'])
