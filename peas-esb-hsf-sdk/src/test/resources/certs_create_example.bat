D:
cd ca
openssl genrsa -out private/root.key.pem 2048
openssl req -x509 -new -key private/root.key.pem -out certs/root.crt -subj "/C=CN/ST=BJ/O=Sino/OU=Sino/CN=localhost"
openssl genrsa -out private/client.key.pem 2048
openssl req -new -key private/client.key.pem -out private/client.csr -subj "/C=CN/ST=BJ/L=BJ/O=Sino/OU=Sino/CN=Sino"
openssl x509 -req -in private/client.csr -CA certs/root.crt -CAkey private/root.key.pem -CAcreateserial -days 3650 -out certs/client.crt
openssl genrsa -out private/server.key.pem 2048
openssl req -new -key private/server.key.pem -out private/server.csr -subj "/C=CN/ST=BJ/L=BJ/O=Sino/OU=Sino/CN=localhost"
openssl x509 -req -in private/server.csr -CA certs/root.crt -CAkey private/root.key.pem -CAcreateserial -days 3650 -out certs/server.crt
openssl pkcs12 -export -in certs/client.crt -inkey private/client.key.pem -out certs/client.p12
openssl pkcs12 -export -in certs/server.crt -inkey private/server.key.pem -out certs/server.p12
keytool -importkeystore -srckeystore certs/client.p12 -destkeystore java/client.jks -srcstoretype pkcs12
keytool -importkeystore -srckeystore certs/server.p12 -destkeystore java/server.jks -srcstoretype pkcs12
keytool -importcert -keystore java/server.jks -file certs/root.crt
keytool -importcert -alias ca -file certs/root.crt -keystore java/clienttrust.jks
keytool -importcert -alias clientcert -file certs/client.crt -keystore java/clienttrust.jks
keytool -importcert -alias ca -file certs/root.crt -keystore java/servertrust.jks
keytool -importcert -alias servercert -file certs/server.crt -keystore java/servertrust.jks