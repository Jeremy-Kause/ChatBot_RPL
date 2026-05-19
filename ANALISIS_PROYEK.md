# Analisis Proyek Chatbot DEMIKOPI

Dokumen ini menjelaskan isi proyek, fitur yang sudah dikerjakan, alur kerja aplikasi, struktur database, serta catatan teknis yang perlu diperhatikan.

## Ringkasan Proyek

Chatbot DEMIKOPI adalah aplikasi desktop berbasis JavaFX untuk membantu pelanggan mendapatkan informasi menu, rekomendasi menu, jam buka, lokasi, dan fasilitas kedai. Aplikasi juga memiliki halaman admin untuk mengelola data menu, fasilitas, informasi kedai, dan akun admin.

Teknologi utama:

- Java 21
- JavaFX 21.0.6
- Maven
- MySQL/MariaDB
- JDBC MySQL Connector 8.0.33
- FXML dan CSS untuk tampilan JavaFX

## Apa Saja yang Sudah Dilakukan

1. Struktur project Maven sudah dibuat.
   Project memakai `pom.xml`, Maven Wrapper, JavaFX, JUnit, dan MySQL Connector.

2. Database MySQL sudah dirancang.
   Tersedia tabel `admin`, `fasilitas`, `infokedai`, `kategori`, dan `menu`.

3. Data awal sudah disediakan.
   File `database/demikopi.sql` berisi data admin, fasilitas, info kedai, kategori, dan 26 menu awal.

4. Data gambar menu sudah ditambahkan.
   Kolom `image_path` sudah ditambahkan ke tabel `menu`, lalu setiap menu diberi path gambar seperti `asset/menu/espresso.jpg`.

5. Layer akses database sudah dibuat.
   Package `dataAccess` menangani koneksi database dan query untuk admin, menu, kategori, fasilitas, serta info kedai.

6. Model data sudah dibuat.
   Package `model` berisi class sederhana untuk membawa data dari database ke sistem dan UI.

7. Chatbot user sudah dibuat.
   Chatbot dapat mengenali salam, pertanyaan menu, kategori, detail menu, rekomendasi, jam buka, lokasi, dan fasilitas.

8. Sistem rekomendasi sudah dibuat.
   Rekomendasi dapat mempertimbangkan kategori, profil rasa, suhu sajian, cuaca, dan status bestseller.

9. UI user sudah dibuat.
   Halaman user memiliki chat stream, bubble user/bot, quick action, input pesan, clear chat, gambar menu, dan kartu rekomendasi.

10. UI admin sudah dibuat.
    Admin memiliki login, dashboard ringkasan, manajemen menu, manajemen fasilitas, lokasi kedai, pengaturan akun, logout, dan tombol kembali ke chatbot.

11. Upload gambar menu sudah dibuat.
    Admin bisa klik atau drag-drop gambar pada halaman manajemen menu. Gambar disalin ke folder asset menu dan path disimpan ke database.

12. Fallback data sudah dibuat.
    Jika database gagal diakses, chatbot user masih bisa menjawab memakai data hardcoded dari `UserFallbackData`.

13. Styling JavaFX sudah dibuat.
    Tampilan admin dan user dipisah ke `admin.css` dan `user.css`.

14. Build output sudah pernah dihasilkan.
    Folder `target/` berisi file `.class` dan resource hasil kompilasi Maven.

## Alur Sistem

### Alur User Chatbot

1. User membuka aplikasi dari `Launcher`.
2. `Launcher` menjalankan `UserUI`.
3. `UserUI` membuka FXML `USER UI/dashboard.fxml`.
4. `UserDashboardController` menerima input user.
5. Input dikirim ke `ChatEngine`.
6. `ChatEngine` memakai `NLPService` untuk mendeteksi intent.
7. Berdasarkan intent, `ChatEngine` mengambil data dari DAO atau fallback.
8. Jawaban dibuat sebagai `ChatResponse`.
9. `UserDashboardController` menampilkan jawaban dalam bubble, format block, dan gambar jika ada.

Contoh intent yang dikenali:

- `SALAM`
- `TANYA_MENU`
- `TANYA_KATEGORI`
- `TANYA_REKOMENDASI`
- `TANYA_DETAIL_MENU`
- `TANYA_JAM_BUKA`
- `TANYA_LOKASI`
- `TANYA_FASILITAS`
- `TIDAK_DIKENAL`

### Alur Admin

1. Admin masuk dari tombol `Login Admin` di UI user atau langsung dari `AdminUI`.
2. `AdminLoginController` memvalidasi input username dan password.
3. `AdminAuth` membaca data admin lewat `AdminDAO`.
4. Jika login berhasil, data admin disimpan di `AdminSession`.
5. Admin diarahkan ke dashboard.
6. Halaman admin lain memakai `AdminNavigationController` untuk berpindah halaman.
7. CRUD menu, fasilitas, info kedai, dan password admin dilakukan melalui DAO dan database.

## Struktur Folder

```text
.
|-- .env.example
|-- .gitignore
|-- DATABASE.md
|-- ANALISIS_PROYEK.md
|-- pom.xml
|-- mvnw
|-- mvnw.cmd
|-- .mvn/
|-- database/
|-- src/
|-- target/
|-- .idea/
```

Penjelasan:

- `.env.example`: contoh konfigurasi database lokal.
- `.gitignore`: aturan file/folder yang tidak perlu masuk Git, seperti `target/`.
- `DATABASE.md`: panduan koneksi dan query awal database.
- `pom.xml`: konfigurasi Maven, dependency, compiler, dan plugin JavaFX.
- `mvnw` dan `mvnw.cmd`: Maven Wrapper untuk Linux/macOS dan Windows.
- `.mvn/wrapper`: konfigurasi Maven Wrapper.
- `database`: berisi SQL schema dan seed data.
- `src/main/java`: source code Java.
- `src/main/resources`: FXML, CSS, dan asset gambar.
- `target`: hasil build Maven, bukan source utama.
- `.idea`: konfigurasi IntelliJ IDEA lokal.

## Konfigurasi Project

### `pom.xml`

File ini mengatur:

- `groupId`: `com.demikopi`
- `artifactId`: `DEMIKOPU`
- nama project: `Chatbot DEMIKOPU`
- Java compiler source/target: 21
- dependency JavaFX controls dan FXML
- dependency JUnit untuk testing
- dependency MySQL Connector
- plugin `javafx-maven-plugin`
- main class JavaFX: `com.demikopi.ui.Launcher`

Catatan: module Java memakai nama `com.template.demikopu`, sedangkan package utama memakai `com.demikopi`. Ini masih bisa berjalan selama konfigurasi konsisten, tetapi nama module sebaiknya disamakan agar lebih rapi.

### `module-info.java`

Module Java membuka dan mengekspor package penting:

- membutuhkan `javafx.controls`
- membutuhkan `javafx.fxml`
- membutuhkan `java.sql`
- membutuhkan `mysql.connector.j`
- membuka `com.demikopi.uiHandler` untuk FXML
- mengekspor `com.demikopi.ui`
- mengekspor `com.demikopi.uiHandler`

## Database

Database bernama `demikopi`.

Konfigurasi lokal yang dipakai:

```text
DB_HOST=localhost
DB_PORT=3306
DB_NAME=demikopi
DB_USER=root
DB_PASSWORD=
```

### Tabel

| Tabel | Fungsi |
| --- | --- |
| `admin` | Menyimpan akun admin. |
| `fasilitas` | Menyimpan daftar fasilitas kedai. |
| `infokedai` | Menyimpan jam operasional, lokasi, dan kontak. |
| `kategori` | Menyimpan kategori menu. |
| `menu` | Menyimpan menu, harga, rasa, status, bestseller, dan gambar. |

### Data Awal

File `database/demikopi.sql` sudah mengisi:

- 4 admin: Delvin, Jeremy, Justin, Nathan
- 8 fasilitas
- 1 info kedai
- 4 kategori: Kopi, Non-Kopi, Mix, Makanan
- 26 menu

### Perubahan Gambar Menu

File `database/add_image_path_to_menu.sql` menambahkan:

```sql
ALTER TABLE menu
    ADD COLUMN image_path VARCHAR(255) DEFAULT NULL AFTER deskripsi;
```

Setelah itu setiap menu diberi path gambar. Contoh:

```sql
UPDATE menu SET image_path = 'asset/menu/espresso.jpg' WHERE nama_menu = 'Espresso';
```

## Source Java

### Entry Point dan Test Console

| File | Fungsi |
| --- | --- |
| `src/main/java/com/demikopi/ui/Launcher.java` | Entry point utama aplikasi JavaFX. Default-nya membuka UI user. |
| `src/main/java/com/demikopi/ui/UserUI.java` | Membuka dashboard chatbot user. |
| `src/main/java/com/demikopi/ui/AdminUI.java` | Membuka halaman login admin. |
| `src/main/java/com/demikopi/DemiKopi.java` | Test chatbot lewat console. |
| `src/main/java/com/demikopi/AdminTestFitur.java` | Test fitur admin CRUD lewat console. |

### Package `dataAccess`

| File | Fungsi |
| --- | --- |
| `DatabaseConfig.java` | Membuat koneksi JDBC ke MySQL dan memastikan koneksi tersedia. |
| `AdminDAO.java` | Mengambil data admin dan update password. |
| `MenuDAO.java` | Mengambil semua menu, filter menu, tambah, update, hapus, dan membaca kolom `image_path`. |
| `KategoriDAO.java` | Mengambil data kategori dan mencari kategori berdasarkan id/nama. |
| `FasilitasDAO.java` | CRUD fasilitas. |
| `InfoDAO.java` | Mengambil dan update info kedai. |

Catatan penting:

- Query utama sudah memakai `PreparedStatement`.
- `MenuDAO` dibuat kompatibel dengan database lama yang belum punya kolom `image_path`.
- Koneksi database masih hardcoded di `DatabaseConfig`.

### Package `model`

| File | Fungsi |
| --- | --- |
| `Admin.java` | Model akun admin. |
| `Menu.java` | Model menu, termasuk kategori, rasa, suhu, harga, gambar, bestseller, dan status tersedia. |
| `Kategori.java` | Model kategori. |
| `Fasilitas.java` | Model fasilitas. |
| `InfoKedai.java` | Model info kedai. |

Model ini sebagian besar berisi field, constructor, dan getter.

### Package `sistemAdmin`

| File | Fungsi |
| --- | --- |
| `AdminAuth.java` | Validasi login admin. |
| `AdminSession.java` | Menyimpan username dan nama admin yang sedang login. |
| `AdminController.java` | Penghubung antara UI admin dan DAO, termasuk validasi input. |

Fitur admin yang sudah didukung:

- login
- menyimpan sesi admin
- ambil semua menu
- tambah/update/hapus menu
- ambil/update info kedai
- ambil/tambah/update/hapus fasilitas
- ambil kategori
- ambil data admin
- ganti password admin

### Package `sistemUser`

| File | Fungsi |
| --- | --- |
| `ChatEngine.java` | Pusat routing percakapan chatbot. |
| `NLPService.java` | Deteksi intent dan ekstraksi keyword/preferensi. |
| `RecomendationHandler.java` | Ranking dan alasan rekomendasi menu. |
| `ChatResponse.java` | Struktur jawaban bot, termasuk block teks dan gambar. |
| `UserFallbackData.java` | Data cadangan jika database gagal. |

#### `NLPService`

NLP masih rule-based, yaitu memakai kamus kata kunci dan regex sederhana.

Yang dikenali:

- salam
- jam buka
- lokasi
- fasilitas
- detail menu
- rekomendasi
- bestseller
- rasa seperti manis, pahit, creamy, fruity, cokelat
- suhu seperti panas dan dingin
- konteks cuaca seperti panas, gerah, hujan, mendung
- kategori seperti kopi, non-kopi, makanan, minuman, mix

#### `ChatEngine`

`ChatEngine` melakukan:

- validasi input kosong
- deteksi intent
- pencarian menu langsung dari input
- membangun jawaban semua menu
- membangun jawaban menu per kategori
- membangun detail menu
- membangun rekomendasi
- membangun jawaban jam buka
- membangun jawaban lokasi
- membangun jawaban fasilitas
- fallback jika input tidak dikenal
- fallback data jika database gagal
- menghubungkan jawaban teks dengan gambar menu

#### `RecomendationHandler`

Sistem rekomendasi melakukan filter dan scoring berdasarkan:

- kategori
- rasa
- suhu sajian
- konteks cuaca
- status bestseller
- harga sebagai tie-breaker jika skor sama

Jika user tidak memberi preferensi, sistem akan memprioritaskan menu bestseller.

#### `ChatResponse`

`ChatResponse` dibuat agar UI tidak hanya menerima teks mentah. Jawaban bisa punya:

- teks biasa
- block terformat
- title
- section
- paragraph
- note
- detail row
- schedule row
- numbered item
- gambar tunggal
- beberapa gambar rekomendasi

Ini membuat tampilan chatbot lebih rapi di UI JavaFX.

## UI JavaFX

### UI User

FXML:

- `src/main/resources/com/demikopi/uiHandler/USER UI/dashboard.fxml`

Controller:

- `src/main/java/com/demikopi/uiHandler/UserDashboardController.java`

CSS:

- `src/main/resources/com/demikopi/uiHandler/user.css`

Fitur:

- header aplikasi
- status online
- tombol clear chat
- tombol login admin
- chat stream
- bubble user dan bot
- quick action: Menu, Rekomendasi, Jam Buka, Lokasi, Fasilitas
- input pesan
- tombol kirim
- render jawaban terformat dari `ChatResponse`
- render gambar detail menu
- render galeri kartu rekomendasi
- tombol `Lihat Detail` pada kartu rekomendasi

### UI Admin

FXML admin:

- `login.fxml`
- `admin-dashboard.fxml`
- `menu-management.fxml`
- `facility-management.fxml`
- `admin-lokasi.fxml`
- `settings-view.fxml`

Controller admin:

- `AdminLoginController.java`
- `AdminNavigationController.java`
- `AdminDashboardController.java`
- `MenuManagementController.java`
- `FacilityManagementController.java`
- `AdminLokasiController.java`
- `AdminSettingsController.java`

CSS:

- `src/main/resources/com/demikopi/uiHandler/admin.css`

#### Login Admin

File terkait:

- `AdminLoginController.java`
- `login.fxml`

Fitur:

- input username
- input password
- validasi input kosong
- validasi credential ke database
- membuka dashboard jika berhasil
- alert jika gagal

#### Dashboard Admin

File terkait:

- `AdminDashboardController.java`
- `admin-dashboard.fxml`

Fitur:

- total menu
- total menu tersedia
- total bestseller
- total fasilitas
- status database
- top menu
- top kategori
- status kelengkapan info kedai
- agenda admin
- refresh dashboard

#### Manajemen Menu

File terkait:

- `MenuManagementController.java`
- `menu-management.fxml`

Fitur:

- tabel menu
- thumbnail gambar menu
- pencarian menu
- filter semua/tersedia/habis/bestseller
- tambah menu
- edit menu
- hapus menu
- input nama, kategori, profil rasa, suhu sajian, harga, deskripsi
- checkbox bestseller
- checkbox tersedia
- drag-drop gambar
- file chooser gambar
- preview gambar
- penyimpanan gambar ke folder resource
- path gambar disimpan dalam format `asset/menu/nama-file.jpg`

#### Manajemen Fasilitas

File terkait:

- `FacilityManagementController.java`
- `facility-management.fxml`

Fitur:

- tabel fasilitas
- pencarian berdasarkan id, nama, atau deskripsi
- tambah fasilitas
- edit fasilitas
- hapus fasilitas
- refresh data

#### Lokasi Kedai

File terkait:

- `AdminLokasiController.java`
- `admin-lokasi.fxml`

Fitur:

- edit jam operasional
- edit lokasi
- edit kontak
- status kesiapan data chatbot
- indikator jam/lokasi/kontak sudah terisi atau belum
- muat ulang data dari database

#### Pengaturan Akun

File terkait:

- `AdminSettingsController.java`
- `settings-view.fxml`

Fitur:

- menampilkan admin yang sedang login
- membaca username dari `AdminSession`
- ganti password admin
- validasi password lama
- validasi password baru minimal 4 karakter
- validasi konfirmasi password

## Asset dan Styling

### Asset

Folder asset:

```text
src/main/resources/com/demikopi/uiHandler/asset/
```

Isi penting:

- `coffee-cup.png`: logo/avatar bot.
- `asset/menu/*.jpg`: gambar menu.
- `asset/menu/README.md`: aturan penamaan gambar menu.
- `asset/menu/CHECKLIST.md`: checklist foto menu.

Gambar menu yang sudah tersedia memakai format `.jpg`, sedangkan checklist lama masih memberi contoh `.png`. UI mendukung `.png`, `.jpg`, dan `.jpeg`.

### CSS

`admin.css` mengatur:

- sidebar admin
- halaman login
- card dashboard
- tabel
- form
- tombol primary, soft, danger
- status chip/pill
- drop zone gambar

`user.css` mengatur:

- header user
- bubble chat
- quick actions
- input pesan
- block jawaban bot
- detail row
- schedule row
- gambar menu
- kartu rekomendasi

## Cara Menjalankan

1. Pastikan MySQL/MariaDB berjalan.
2. Buat database bernama `demikopi`.
3. Import file:

```text
database/demikopi.sql
```

4. Pastikan konfigurasi di `DatabaseConfig.java` sesuai:

```java
String url = "jdbc:mysql://localhost:3306/demikopi";
String user = "root";
String pass = "";
```

5. Jalankan aplikasi JavaFX:

```powershell
.\mvnw.cmd javafx:run
```

Default aplikasi akan membuka UI user karena `Launcher` menjalankan `UserUI`.

## Akun Admin Default

Berdasarkan seed database:

| Username | Password | Nama |
| --- | --- | --- |
| Jeremy | admin1 | Jeremy Kause |
| Justin | admin2 | Justin William |
| Nathan | admin3 | Waraney Mambu |
| Delvin | admin4 | Delvin Laurens |

Catatan: password saat ini disimpan plain text di database.

## Status Git Saat Dianalisis

Remote repository:

```text
https://github.com/Jeremy-Kause/ChatBot_RPL.git
```

Commit terakhir lokal yang terbaca:

```text
4af7649 Edit dikit
```

Ada satu perubahan lokal sebelum dokumen ini dibuat:

```text
D  .idea/data_source_mapping.xml
```

Artinya file `.idea/data_source_mapping.xml` sedang berstatus terhapus di working tree.

## Catatan Teknis dan Potensi Perbaikan

1. Password admin masih plain text.
   Sebaiknya password di-hash, misalnya dengan BCrypt, sebelum disimpan ke database.

2. Konfigurasi database masih hardcoded.
   Sebaiknya `DatabaseConfig` membaca dari environment variable atau file konfigurasi.

3. Nama module belum konsisten.
   `module-info.java` memakai `com.template.demikopu`, sedangkan package utama memakai `com.demikopi`.

4. `.idea` berisi konfigurasi lokal.
   Beberapa file `.idea` seperti data source dan workspace biasanya tidak perlu ikut repository.

5. Folder `target` adalah hasil build.
   Folder ini tidak perlu dianalisis sebagai source dan sebaiknya tidak dicommit.

6. `AdminController.tambahFasilitas` mengecek `idFasilitas` wajib terisi, tetapi `FasilitasDAO.tambahFasilitas` tidak menyimpan id karena database memakai auto increment. Validasi ini bisa membuat tambah fasilitas dari controller gagal jika id kosong.

7. Nama class `RecomendationHandler` salah eja.
   Secara fungsi tidak masalah, tetapi nama yang lebih tepat adalah `RecommendationHandler`.

8. Belum terlihat folder test otomatis.
   Dependency JUnit sudah ada, tetapi belum ada test unit di `src/test`.

9. IntelliJ project memakai JDK 25, sedangkan Maven compiler memakai Java 21.
   Aplikasi tetap bisa dikompilasi dengan Java 21, tetapi konfigurasi IDE dan Maven sebaiknya disamakan.

10. `AdminTestFitur` masih berupa test manual console.
    File ini berguna untuk demo awal, tetapi untuk kualitas jangka panjang lebih baik ditambah test otomatis.

## Kesimpulan

Project ini sudah mencakup aplikasi chatbot JavaFX yang cukup lengkap: ada sisi user, sisi admin, database, CRUD, rekomendasi menu, gambar menu, dan tampilan yang dipisah dengan CSS. Bagian yang paling penting untuk pengembangan lanjutan adalah merapikan keamanan password, konfigurasi database, konsistensi module/package, dan menambahkan test otomatis.
