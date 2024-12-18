cat data.csv | clickhouse-client --query="INSERT INTO sessions_local FORMAT CSV"
