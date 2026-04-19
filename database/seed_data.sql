-- =========================================================
-- SEED DATA untuk database 'demikopi'
-- Jalankan file ini di MySQL setelah tabel sudah dibuat.
-- =========================================================

USE demikopi;


-- =========================================================
-- 1. KATEGORI
-- =========================================================
INSERT INTO kategori (id_kategori, nama_kategori) VALUES
(1, 'Kopi'),
(2, 'Non-Kopi'),
(3, 'Mix'),
(4, 'Makanan');


-- =========================================================
-- 2. INFO KEDAI
-- =========================================================
INSERT INTO info_kedai (id_info, jam_operasional, lokasi, kontak) VALUES
('INFO001', 'Senin - Jumat: 08.00 - 22.00 | Sabtu - Minggu: 09.00 - 23.00', 'Jl. Kopi Nusantara No. 17, Kota Bandung, Jawa Barat 40132', '0812-3456-7890');


-- =========================================================
-- 3. FASILITAS
-- =========================================================
INSERT INTO fasilitas (id_fasilitas, nama_fasilitas, deskripsi_fasilitas) VALUES
('FAS001', 'WiFi Gratis', 'Tersedia WiFi berkecepatan tinggi di seluruh area kedai. SSID: DEMIKOPI_GUEST | Password: kopienak123'),
('FAS002', 'Ruang Baca', 'Area tenang dengan rak buku koleksi kedai. Cocok untuk membaca atau belajar dengan suasana hening.'),
('FAS003', 'Area Smoking', 'Area merokok outdoor yang terpisah dari ruangan utama, dilengkapi asbak dan ventilasi terbuka.'),
('FAS004', 'Area Kerja', 'Meja panjang dengan stop kontak di setiap kursi, cocok untuk WFA (Work From Anywhere) atau meeting kecil.'),
('FAS005', 'Area Terbuka', 'Taman outdoor dengan kursi santai dan lampu hias. Cocok untuk nongkrong sore dan malam hari.'),
('FAS006', 'Kamar Mandi', 'Tersedia 2 kamar mandi bersih (pria dan wanita) dengan fasilitas lengkap.'),
('FAS007', 'Musholla', 'Ruang ibadah kecil yang bersih dan nyaman, tersedia mukena dan sajadah.'),
('FAS008', 'Parkir', 'Area parkir luas untuk motor dan mobil, dijaga oleh petugas keamanan.');


-- =========================================================
-- 4. MENU
-- =========================================================

-- ---------------------------------------------------------
-- 4a. KATEGORI KOPI (id_kategori = 1)
-- ---------------------------------------------------------
INSERT INTO menu (id_kategori, nama_menu, profil_rasa, suhu_sajian, is_bestseller, harga, deskripsi, status_tersedia) VALUES
(1, 'Espresso',          'pahit, bold, intense',           'panas',        false, 18000, 'Espresso murni tanpa campuran. Shot tunggal dari biji Arabika pilihan, menghasilkan crema tebal dan rasa yang kuat.', true),
(1, 'Americano',         'pahit, ringan, clean',           'panas/dingin',  false, 20000, 'Espresso yang diencerkan dengan air panas. Rasa kopi yang lebih ringan dari espresso namun tetap otentik.', true),
(1, 'Cappuccino',        'creamy, sedikit pahit, lembut',  'panas',        true,  25000, 'Perpaduan espresso, steamed milk, dan foam susu tebal. Rasa seimbang antara kopi dan kelembutan susu.', true),
(1, 'Caffe Latte',       'creamy, manis susu, mild',       'panas/dingin', true,  25000, 'Espresso dengan banyak steamed milk dan sedikit foam. Cocok untuk yang suka kopi lembut dan tidak terlalu pahit.', true),
(1, 'Kopi Susu Gula Aren', 'manis, caramel, creamy',      'dingin',       true,  23000, 'Signature drink! Espresso dicampur susu segar dan gula aren asli. Manis alami dengan aroma karamel khas.', true),
(1, 'V60 Pour Over',     'fruity, floral, tea-like',       'panas',        false, 28000, 'Single origin coffee diseduh manual dengan metode V60. Menghasilkan kopi yang bersih dengan karakter rasa unik sesuai origin.', true),
(1, 'Cold Brew',         'smooth, cokelat, rendah asam',   'dingin',       false, 27000, 'Kopi yang diseduh dingin selama 18 jam. Menghasilkan rasa yang halus, rendah asam, dengan sentuhan cokelat.', true),
(1, 'Affogato',          'pahit-manis, creamy, dessert',   'panas/dingin', false, 30000, 'Satu scoop vanilla ice cream disiram espresso panas. Perpaduan sensasi panas-dingin yang unik.', true);

-- ---------------------------------------------------------
-- 4b. KATEGORI NON-KOPI (id_kategori = 2)
-- ---------------------------------------------------------
INSERT INTO menu (id_kategori, nama_menu, profil_rasa, suhu_sajian, is_bestseller, harga, deskripsi, status_tersedia) VALUES
(2, 'Matcha Latte',      'earthy, creamy, sedikit pahit',  'panas/dingin', true,  27000, 'Green tea matcha premium dari Jepang dicampur susu segar. Rasa earthy yang khas dengan kelembutan susu.', true),
(2, 'Cokelat Panas',     'manis, cokelat, rich',           'panas',        false, 23000, 'Minuman cokelat premium dengan dark chocolate asli. Kaya rasa dan cocok untuk pencinta cokelat.', true),
(2, 'Teh Tarik',         'manis, creamy, teh kental',      'panas',        false, 18000, 'Teh hitam yang ditarik berulang kali menghasilkan buih lembut. Manis dan hangat.', true),
(2, 'Lemon Tea',         'asam, segar, manis ringan',      'dingin',       false, 18000, 'Teh hitam dicampur perasan lemon segar dan madu. Segar dan cocok untuk cuaca panas.', true),
(2, 'Milo Dinosaur',     'manis, cokelat, crunchy',        'dingin',       true,  22000, 'Milo dingin kental dengan taburan bubuk Milo di atasnya. Favorit semua kalangan!', true),
(2, 'Strawberry Smoothie','manis, asam segar, fruity',     'dingin',       false, 25000, 'Buah strawberry segar di-blend dengan yogurt dan es. Segar, sehat, dan kaya vitamin.', true);

-- ---------------------------------------------------------
-- 4c. KATEGORI MIX (id_kategori = 3)
-- ---------------------------------------------------------
INSERT INTO menu (id_kategori, nama_menu, profil_rasa, suhu_sajian, is_bestseller, harga, deskripsi, status_tersedia) VALUES
(3, 'Es Kopi Matcha',    'pahit kopi, earthy matcha, creamy', 'dingin',    false, 30000, 'Perpaduan unik dua dunia: shot espresso bertemu matcha latte. Rasa kompleks untuk petualang rasa.', true),
(3, 'Mocha Latte',       'cokelat, kopi, manis',              'panas/dingin', true, 28000, 'Espresso dicampur cokelat dan susu. Cocok untuk yang ingin menikmati kopi dengan sentuhan manis cokelat.', true),
(3, 'Taro Espresso',     'creamy, ubi ungu, sedikit pahit',   'dingin',    false, 28000, 'Taro latte yang diberi tambahan shot espresso. Warna cantik ungu dengan rasa unik.', true),
(3, 'Hazelnut Latte',    'nutty, caramel, creamy',            'panas/dingin', false, 28000, 'Latte dengan sirup hazelnut premium. Aroma kacang yang hangat berpadu dengan espresso.', true),
(3, 'Caramel Macchiato', 'manis, caramel, creamy, kopi ringan', 'panas/dingin', true, 28000, 'Susu dan vanilla disiram espresso lalu di-drizzle saus karamel. Layered drink yang cantik dan lezat.', true);

-- ---------------------------------------------------------
-- 4d. KATEGORI MAKANAN (id_kategori = 4)
-- ---------------------------------------------------------
INSERT INTO menu (id_kategori, nama_menu, profil_rasa, suhu_sajian, is_bestseller, harga, deskripsi, status_tersedia) VALUES
(4, 'Croissant Butter',    'gurih, buttery, flaky',          'suhu ruang', true,  20000, 'Croissant klasik dengan lapisan pastry renyah dan isian butter. Pendamping sempurna untuk kopi.', true),
(4, 'Roti Bakar Cokelat',  'manis, cokelat, hangat',         'panas',      false, 15000, 'Roti tawar panggang dengan isian cokelat leleh. Comfort food yang simpel dan nikmat.', true),
(4, 'French Fries',        'gurih, asin, crispy',            'panas',      false, 18000, 'Kentang goreng renyah dengan pilihan saus: sambal, mayo, atau kecap. Snack teman ngobrol.', true),
(4, 'Pisang Goreng Crispy','manis, gurih, crispy',           'panas',      true,  15000, 'Pisang kepok digoreng dengan tepung crispy. Disajikan dengan topping keju, cokelat, atau gula halus.', true),
(4, 'Nachos Cheese',       'gurih, cheesy, sedikit pedas',   'suhu ruang', false, 22000, 'Tortilla chips renyah dengan saus keju cheddar dan topping jalapeno. Cocok untuk sharing.', true),
(4, 'Sandwich Tuna',       'gurih, creamy, segar',           'suhu ruang', false, 25000, 'Roti gandum isi tuna mayo, selada, tomat, dan timun. Menu sehat dan mengenyangkan.', true),
(4, 'Brownies',            'manis, cokelat pekat, fudgy',    'suhu ruang', true,  18000, 'Brownies dark chocolate buatan in-house yang fudgy dan lembut. Best seller pendamping kopi!', true);
