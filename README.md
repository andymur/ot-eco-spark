# OTUS Spark Ecosystem Course. Ninth Homework. Monitoring.

### Goal

After completion of this homework you will be able to configure monitoring for your application with such tools as InfluxDB, Grafana, Prometheus etc

### Assignment

Create monitoring with dashboard(s) for your application.

### Solution

I used artificial java based application which creates a stream of spot prices for currency couples.
InfluxDB used to store measurements and Grafana used to show them on a dashboard.

All deployed in to the Yandex Cloud.

### How to check (compile, build & run, etc)

Prerequisites: for normal work you need to have InfluxDB running (by default on same host). To check dashboard Grafana should be running as well.

You can launch those services with the following commands:

```docker run -p 8086:8086 -v influxdb:/var/lib/influxdb influxdb:1.8```
```docker run -d -p 3000:3000 grafana/grafana```

After checkout build the application with ```mvn clean install```.

You can run it with ```java -jar MeterSystemRunner.jar```. JAR file can be found in the target directory.

Main entry point located here: ```com.andymur.pg.influxdb.MeterSystemRunner```

Grafana dashboard json can be found here: ```src/main/resources/grafana/rate-dashboard.json```

Solution is running on the yandex cloud machine. 
You can check the dashboard using this link: http://178.154.201.92:3000/d/nMz6tcknz/ratedashboard

Creds are: ```admin``` : ```admin123```

#### Checkout

Solution stored in the git repo. In order to checkout simply do this:

```git clone -b ninth_how_work_hive https://github.com/andymur/ot-eco-spark.git``` or check the link ```https://github.com/andymur/ot-eco-spark/tree/ninth_how_work_monitoring```
