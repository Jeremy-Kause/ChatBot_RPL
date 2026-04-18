# Database DEMIKOPI

Gunakan konfigurasi ini untuk mengakses database MySQL lokal project.

```text
DB_HOST=localhost
DB_PORT=3306
DB_NAME=demikopi
DB_USER=root
DB_PASSWORD=
```

Konfigurasi yang sama saat ini juga ada di:

```text
src/main/java/com/demikopi/dataAccess/DatabaseConfig.java
```

## Cara Membaca Database

Jika MySQL CLI tersedia:

```bash
mysql -h localhost -P 3306 -u root --protocol=tcp -D demikopi
```

Jika MySQL CLI tidak tersedia, gunakan JDBC driver dari Maven lokal:

```text
C:\Users\Jeremy\.m2\repository\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar
```

Contoh query awal yang aman:

```sql
SHOW TABLES;
SELECT * FROM admin LIMIT 5;
SELECT * FROM fasilitas LIMIT 5;
SELECT * FROM infokedai LIMIT 5;
SELECT * FROM kategori LIMIT 5;
SELECT * FROM menu LIMIT 5;
```

## Tabel yang Pernah Terbaca

```text
admin
fasilitas
infokedai
kategori
menu
```

