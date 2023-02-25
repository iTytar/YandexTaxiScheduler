Build project:
>mvn clean install
>docker build -t taxi-service:latest .

Run project:
>docker-compose up -d

Grafana URL: http://localhost:3000
user: admin
password: admin
press Skip on change password

Configuration / Data sources -> Add data source -> Prometeus -> Set URL: 'http://host.docker.internal:9090'
Dashboards / Import dashboard -> select 'jvm-micrometer_rev9.json'
Dashboards / Import dashboard -> select 'micrometer-spring-throughput_rev2.json'

