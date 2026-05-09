-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 20 Apr 2026 pada 06.04
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `demikopi`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`username`, `password`, `nama_lengkap`) VALUES
('Delvin', 'admin4', 'Delvin Laurens'),
('Jeremy', 'admin1', 'Jeremy Kause'),
('Justin', 'admin2', 'Justin William'),
('Nathan', 'admin3', 'Waraney Mambu');

-- --------------------------------------------------------

--
-- Struktur dari tabel `fasilitas`
--

CREATE TABLE `fasilitas` (
  `id_fasilitas` int(11) NOT NULL,
  `nama_fasilitas` varchar(50) NOT NULL,
  `deskripsi_fasilitas` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `fasilitas`
--

INSERT INTO `fasilitas` (`id_fasilitas`, `nama_fasilitas`, `deskripsi_fasilitas`) VALUES
(1, 'WiFi Gratis', 'Tersedia WiFi berkecepatan tinggi di seluruh area kedai. SSID: DEMIKOPI_GUEST | Password: kopienak123'),
(2, 'Ruang Baca', 'Area tenang dengan rak buku koleksi kedai. Cocok untuk membaca atau belajar dengan suasana hening.'),
(3, 'Area Smoking', 'Area merokok outdoor yang terpisah dari ruangan utama, dilengkapi asbak dan ventilasi terbuka.'),
(4, 'Area Kerja', 'Meja panjang dengan stop kontak di setiap kursi, cocok untuk WFA (Work From Anywhere) atau meeting kecil.'),
(5, 'Area Terbuka', 'Taman outdoor dengan kursi santai dan lampu hias. Cocok untuk nongkrong sore dan malam hari.'),
(6, 'Kamar Mandi', 'Tersedia 2 kamar mandi bersih (pria dan wanita) dengan fasilitas lengkap.'),
(7, 'Musholla', 'Ruang ibadah kecil yang bersih dan nyaman, tersedia mukena dan sajadah.'),
(8, 'Parkir', 'Area parkir luas untuk motor dan mobil, dijaga oleh petugas keamanan.');

-- --------------------------------------------------------

--
-- Struktur dari tabel `infokedai`
--

CREATE TABLE `infokedai` (
  `id_info` int(11) NOT NULL,
  `jam_operasional` varchar(100) NOT NULL,
  `lokasi` text NOT NULL,
  `kontak` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `infokedai`
--

INSERT INTO `infokedai` (`id_info`, `jam_operasional`, `lokasi`, `kontak`) VALUES
(1, 'Senin - Jumat: 08.00 - 22.00 | Sabtu - Minggu: 09.00 - 23.00', 'Jl. Bima No. 17, Ngalaban, Sinduharjo, Kec.Ngaglik, Kabupaten Sleman, DIY  40132', '0812-3456-7890');

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` int(11) NOT NULL,
  `nama_kategori` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`) VALUES
(1, 'Kopi'),
(4, 'Makanan'),
(3, 'Mix'),
(2, 'Non-Kopi');

-- --------------------------------------------------------

--
-- Struktur dari tabel `menu`
--

CREATE TABLE `menu` (
  `id_menu` int(11) NOT NULL,
  `id_kategori` int(11) NOT NULL,
  `nama_menu` varchar(100) NOT NULL,
  `profil_rasa` varchar(50) DEFAULT NULL,
  `suhu_sajian` varchar(20) DEFAULT NULL,
  `is_bestseller` tinyint(1) DEFAULT 0,
  `harga` int(11) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `status_tersedia` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `menu`
--

INSERT INTO `menu` (`id_menu`, `id_kategori`, `nama_menu`, `profil_rasa`, `suhu_sajian`, `is_bestseller`, `harga`, `deskripsi`, `status_tersedia`) VALUES
(1, 1, 'Espresso', 'pahit, bold, intense', 'panas', 0, 18000, 'Espresso murni tanpa campuran. Shot tunggal dari biji Arabika pilihan, menghasilkan crema tebal dan rasa yang kuat.', 1),
(2, 1, 'Americano', 'pahit, ringan, clean', 'panas/dingin', 0, 20000, 'Espresso yang diencerkan dengan air panas. Rasa kopi yang lebih ringan dari espresso namun tetap otentik.', 1),
(3, 1, 'Cappuccino', 'creamy, sedikit pahit, lembut', 'panas', 1, 25000, 'Perpaduan espresso, steamed milk, dan foam susu tebal. Rasa seimbang antara kopi dan kelembutan susu.', 1),
(4, 1, 'Caffe Latte', 'creamy, manis susu, mild', 'panas/dingin', 1, 25000, 'Espresso dengan banyak steamed milk dan sedikit foam. Cocok untuk yang suka kopi lembut dan tidak terlalu pahit.', 1),
(5, 1, 'Kopi Susu Gula Aren', 'manis, caramel, creamy', 'dingin', 1, 23000, 'Signature drink! Espresso dicampur susu segar dan gula aren asli. Manis alami dengan aroma karamel khas.', 1),
(6, 1, 'V60 Pour Over', 'fruity, floral, tea-like', 'panas', 0, 28000, 'Single origin coffee diseduh manual dengan metode V60. Menghasilkan kopi yang bersih dengan karakter rasa unik sesuai origin.', 1),
(7, 1, 'Cold Brew', 'smooth, cokelat, rendah asam', 'dingin', 0, 27000, 'Kopi yang diseduh dingin selama 18 jam. Menghasilkan rasa yang halus, rendah asam, dengan sentuhan cokelat.', 1),
(8, 1, 'Affogato', 'pahit-manis, creamy, dessert', 'panas/dingin', 0, 30000, 'Satu scoop vanilla ice cream disiram espresso panas. Perpaduan sensasi panas-dingin yang unik.', 1),
(9, 2, 'Matcha Latte', 'earthy, creamy, sedikit pahit', 'panas/dingin', 1, 27000, 'Green tea matcha premium dari Jepang dicampur susu segar. Rasa earthy yang khas dengan kelembutan susu.', 1),
(10, 2, 'Cokelat Panas', 'manis, cokelat, rich', 'panas', 0, 23000, 'Minuman cokelat premium dengan dark chocolate asli. Kaya rasa dan cocok untuk pencinta cokelat.', 1),
(11, 2, 'Teh Tarik', 'manis, creamy, teh kental', 'panas', 0, 18000, 'Teh hitam yang ditarik berulang kali menghasilkan buih lembut. Manis dan hangat.', 1),
(12, 2, 'Lemon Tea', 'asam, segar, manis ringan', 'dingin', 0, 18000, 'Teh hitam dicampur perasan lemon segar dan madu. Segar dan cocok untuk cuaca panas.', 1),
(13, 2, 'Milo Dinosaur', 'manis, cokelat, crunchy', 'dingin', 1, 22000, 'Milo dingin kental dengan taburan bubuk Milo di atasnya. Favorit semua kalangan!', 1),
(14, 2, 'Strawberry Smoothie', 'manis, asam segar, fruity', 'dingin', 0, 25000, 'Buah strawberry segar di-blend dengan yogurt dan es. Segar, sehat, dan kaya vitamin.', 1),
(15, 3, 'Es Kopi Matcha', 'pahit kopi, earthy matcha, creamy', 'dingin', 0, 30000, 'Perpaduan unik dua dunia: shot espresso bertemu matcha latte. Rasa kompleks untuk petualang rasa.', 1),
(16, 3, 'Mocha Latte', 'cokelat, kopi, manis', 'panas/dingin', 1, 28000, 'Espresso dicampur cokelat dan susu. Cocok untuk yang ingin menikmati kopi dengan sentuhan manis cokelat.', 1),
(17, 3, 'Taro Espresso', 'creamy, ubi ungu, sedikit pahit', 'dingin', 0, 28000, 'Taro latte yang diberi tambahan shot espresso. Warna cantik ungu dengan rasa unik.', 1),
(18, 3, 'Hazelnut Latte', 'nutty, caramel, creamy', 'panas/dingin', 0, 28000, 'Latte dengan sirup hazelnut premium. Aroma kacang yang hangat berpadu dengan espresso.', 1),
(19, 3, 'Caramel Macchiato', 'manis, caramel, creamy, kopi ringan', 'panas/dingin', 1, 28000, 'Susu dan vanilla disiram espresso lalu di-drizzle saus karamel. Layered drink yang cantik dan lezat.', 1),
(20, 4, 'Croissant Butter', 'gurih, buttery, flaky', 'suhu ruang', 1, 20000, 'Croissant klasik dengan lapisan pastry renyah dan isian butter. Pendamping sempurna untuk kopi.', 1),
(21, 4, 'Roti Bakar Cokelat', 'manis, cokelat, hangat', 'panas', 0, 15000, 'Roti tawar panggang dengan isian cokelat leleh. Comfort food yang simpel dan nikmat.', 1),
(22, 4, 'French Fries', 'gurih, asin, crispy', 'panas', 0, 18000, 'Kentang goreng renyah dengan pilihan saus: sambal, mayo, atau kecap. Snack teman ngobrol.', 1),
(23, 4, 'Pisang Goreng Crispy', 'manis, gurih, crispy', 'panas', 1, 15000, 'Pisang kepok digoreng dengan tepung crispy. Disajikan dengan topping keju, cokelat, atau gula halus.', 1),
(24, 4, 'Nachos Cheese', 'gurih, cheesy, sedikit pedas', 'suhu ruang', 0, 22000, 'Tortilla chips renyah dengan saus keju cheddar dan topping jalapeno. Cocok untuk sharing.', 1),
(25, 4, 'Sandwich Tuna', 'gurih, creamy, segar', 'suhu ruang', 0, 25000, 'Roti gandum isi tuna mayo, selada, tomat, dan timun. Menu sehat dan mengenyangkan.', 1),
(26, 4, 'Brownies', 'manis, cokelat pekat, fudgy', 'suhu ruang', 1, 18000, 'Brownies dark chocolate buatan in-house yang fudgy dan lembut. Best seller pendamping kopi!', 1);

ALTER TABLE `menu`
  ADD COLUMN `image_path` varchar(255) DEFAULT NULL AFTER `deskripsi`;

UPDATE `menu` SET `image_path` = 'asset/menu/espresso.jpg' WHERE `nama_menu` = 'Espresso';
UPDATE `menu` SET `image_path` = 'asset/menu/americano.jpg' WHERE `nama_menu` = 'Americano';
UPDATE `menu` SET `image_path` = 'asset/menu/cappuccino.jpg' WHERE `nama_menu` = 'Cappuccino';
UPDATE `menu` SET `image_path` = 'asset/menu/caffe-latte.jpg' WHERE `nama_menu` = 'Caffe Latte';
UPDATE `menu` SET `image_path` = 'asset/menu/kopi-susu-gula-aren.jpg' WHERE `nama_menu` = 'Kopi Susu Gula Aren';
UPDATE `menu` SET `image_path` = 'asset/menu/v60-pour-over.jpg' WHERE `nama_menu` = 'V60 Pour Over';
UPDATE `menu` SET `image_path` = 'asset/menu/cold-brew.jpg' WHERE `nama_menu` = 'Cold Brew';
UPDATE `menu` SET `image_path` = 'asset/menu/affogato.jpg' WHERE `nama_menu` = 'Affogato';
UPDATE `menu` SET `image_path` = 'asset/menu/matcha-latte.jpg' WHERE `nama_menu` = 'Matcha Latte';
UPDATE `menu` SET `image_path` = 'asset/menu/cokelat-panas.jpg' WHERE `nama_menu` = 'Cokelat Panas';
UPDATE `menu` SET `image_path` = 'asset/menu/teh-tarik.jpg' WHERE `nama_menu` = 'Teh Tarik';
UPDATE `menu` SET `image_path` = 'asset/menu/lemon-tea.jpg' WHERE `nama_menu` = 'Lemon Tea';
UPDATE `menu` SET `image_path` = 'asset/menu/milo-dinosaur.jpg' WHERE `nama_menu` = 'Milo Dinosaur';
UPDATE `menu` SET `image_path` = 'asset/menu/strawberry-smoothie.jpg' WHERE `nama_menu` = 'Strawberry Smoothie';
UPDATE `menu` SET `image_path` = 'asset/menu/es-kopi-matcha.jpg' WHERE `nama_menu` = 'Es Kopi Matcha';
UPDATE `menu` SET `image_path` = 'asset/menu/mocha-latte.jpg' WHERE `nama_menu` = 'Mocha Latte';
UPDATE `menu` SET `image_path` = 'asset/menu/taro-espresso.jpg' WHERE `nama_menu` = 'Taro Espresso';
UPDATE `menu` SET `image_path` = 'asset/menu/hazelnut-latte.jpg' WHERE `nama_menu` = 'Hazelnut Latte';
UPDATE `menu` SET `image_path` = 'asset/menu/caramel-macchiato.jpg' WHERE `nama_menu` = 'Caramel Macchiato';
UPDATE `menu` SET `image_path` = 'asset/menu/croissant-butter.jpg' WHERE `nama_menu` = 'Croissant Butter';
UPDATE `menu` SET `image_path` = 'asset/menu/roti-bakar-cokelat.jpg' WHERE `nama_menu` = 'Roti Bakar Cokelat';
UPDATE `menu` SET `image_path` = 'asset/menu/french-fries.jpg' WHERE `nama_menu` = 'French Fries';
UPDATE `menu` SET `image_path` = 'asset/menu/pisang-goreng-crispy.jpg' WHERE `nama_menu` = 'Pisang Goreng Crispy';
UPDATE `menu` SET `image_path` = 'asset/menu/nachos-cheese.jpg' WHERE `nama_menu` = 'Nachos Cheese';
UPDATE `menu` SET `image_path` = 'asset/menu/sandwich-tuna.jpg' WHERE `nama_menu` = 'Sandwich Tuna';
UPDATE `menu` SET `image_path` = 'asset/menu/brownies.jpg' WHERE `nama_menu` = 'Brownies';

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`username`);

--
-- Indeks untuk tabel `fasilitas`
--
ALTER TABLE `fasilitas`
  ADD PRIMARY KEY (`id_fasilitas`);

--
-- Indeks untuk tabel `infokedai`
--
ALTER TABLE `infokedai`
  ADD PRIMARY KEY (`id_info`);

--
-- Indeks untuk tabel `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`),
  ADD UNIQUE KEY `nama_kategori` (`nama_kategori`);

--
-- Indeks untuk tabel `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id_menu`),
  ADD KEY `id_kategori` (`id_kategori`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `fasilitas`
--
ALTER TABLE `fasilitas`
  MODIFY `id_fasilitas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `infokedai`
--
ALTER TABLE `infokedai`
  MODIFY `id_info` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `menu`
--
ALTER TABLE `menu`
  MODIFY `id_menu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `menu`
--
ALTER TABLE `menu`
  ADD CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
