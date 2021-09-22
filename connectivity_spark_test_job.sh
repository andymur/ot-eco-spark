export PYSPARK_PYTHON=python3.7
export PYSPARK_DRIVER_PYTHON=python3.7
spark-submit --master spark://localhost:7077 connectivity_spark_test_job.py
