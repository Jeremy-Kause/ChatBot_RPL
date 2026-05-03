package com.demikopi.sistemUser;

import com.demikopi.model.Fasilitas;
import com.demikopi.model.InfoKedai;
import com.demikopi.model.Menu;

import java.util.List;

final class UserFallbackData {

    private UserFallbackData() {
    }

    static List<Menu> getMenu() {
        return List.of(
                new Menu(1, "Kopi", "Espresso", "pahit, bold, intense", "panas", false, 18000,
                        "Espresso murni dari biji Arabika pilihan dengan rasa kuat.", true),
                new Menu(2, "Kopi", "Americano", "pahit, ringan, clean", "panas/dingin", false, 20000,
                        "Espresso yang diencerkan dengan air untuk rasa kopi yang lebih ringan.", true),
                new Menu(3, "Kopi", "Cappuccino", "creamy, sedikit pahit, lembut", "panas", true, 25000,
                        "Perpaduan espresso, steamed milk, dan foam susu tebal.", true),
                new Menu(4, "Kopi", "Caffe Latte", "creamy, manis susu, mild", "panas/dingin", true, 25000,
                        "Espresso dengan banyak steamed milk, cocok untuk yang suka kopi lembut.", true),
                new Menu(5, "Kopi", "Kopi Susu Gula Aren", "manis, caramel, creamy", "dingin", true, 23000,
                        "Signature drink dengan espresso, susu segar, dan gula aren asli.", true),
                new Menu(6, "Kopi", "V60 Pour Over", "fruity, floral, tea-like", "panas", false, 28000,
                        "Single origin coffee seduh manual dengan karakter rasa bersih.", true),
                new Menu(7, "Kopi", "Cold Brew", "smooth, cokelat, rendah asam", "dingin", false, 27000,
                        "Kopi seduh dingin selama 18 jam dengan rasa halus dan rendah asam.", true),
                new Menu(8, "Kopi", "Affogato", "pahit-manis, creamy, dessert", "panas/dingin", false, 30000,
                        "Vanilla ice cream yang disiram espresso panas.", true),
                new Menu(9, "Non-Kopi", "Matcha Latte", "earthy, creamy, sedikit pahit", "panas/dingin", true, 27000,
                        "Matcha premium dengan susu segar dan rasa earthy yang khas.", true),
                new Menu(10, "Non-Kopi", "Cokelat Panas", "manis, cokelat, rich", "panas", false, 23000,
                        "Minuman cokelat premium yang kaya rasa.", true),
                new Menu(11, "Non-Kopi", "Teh Tarik", "manis, creamy, teh kental", "panas", false, 18000,
                        "Teh hitam creamy dengan buih lembut.", true),
                new Menu(12, "Non-Kopi", "Lemon Tea", "asam, segar, manis ringan", "dingin", false, 18000,
                        "Teh hitam dingin dengan lemon segar dan madu.", true),
                new Menu(13, "Non-Kopi", "Milo Dinosaur", "manis, cokelat, crunchy", "dingin", true, 22000,
                        "Milo dingin kental dengan taburan bubuk Milo.", true),
                new Menu(14, "Non-Kopi", "Strawberry Smoothie", "manis, asam segar, fruity", "dingin", false, 25000,
                        "Strawberry segar diblend dengan yogurt dan es.", true),
                new Menu(15, "Mix", "Es Kopi Matcha", "pahit kopi, earthy matcha, creamy", "dingin", false, 30000,
                        "Perpaduan espresso dan matcha latte.", true),
                new Menu(16, "Mix", "Mocha Latte", "cokelat, kopi, manis", "panas/dingin", true, 28000,
                        "Espresso dengan cokelat dan susu.", true),
                new Menu(17, "Mix", "Taro Espresso", "creamy, ubi ungu, sedikit pahit", "dingin", false, 28000,
                        "Taro latte dengan tambahan shot espresso.", true),
                new Menu(18, "Mix", "Hazelnut Latte", "nutty, caramel, creamy", "panas/dingin", false, 28000,
                        "Latte dengan sirup hazelnut premium.", true),
                new Menu(19, "Mix", "Caramel Macchiato", "manis, caramel, creamy, kopi ringan", "panas/dingin", true, 28000,
                        "Susu dan vanilla dengan espresso serta saus karamel.", true),
                new Menu(20, "Makanan", "Croissant Butter", "gurih, buttery, flaky", "suhu ruang", true, 20000,
                        "Croissant klasik renyah dengan lapisan butter.", true),
                new Menu(21, "Makanan", "Roti Bakar Cokelat", "manis, cokelat, hangat", "panas", false, 15000,
                        "Roti panggang dengan isian cokelat leleh.", true),
                new Menu(22, "Makanan", "French Fries", "gurih, asin, crispy", "panas", false, 18000,
                        "Kentang goreng renyah dengan pilihan saus.", true),
                new Menu(23, "Makanan", "Pisang Goreng Crispy", "manis, gurih, crispy", "panas", true, 15000,
                        "Pisang kepok goreng tepung crispy dengan topping.", true),
                new Menu(24, "Makanan", "Nachos Cheese", "gurih, cheesy, sedikit pedas", "suhu ruang", false, 22000,
                        "Tortilla chips dengan saus keju cheddar.", true),
                new Menu(25, "Makanan", "Sandwich Tuna", "gurih, creamy, segar", "suhu ruang", false, 25000,
                        "Roti gandum isi tuna mayo dan sayuran segar.", true),
                new Menu(26, "Makanan", "Brownies", "manis, cokelat pekat, fudgy", "suhu ruang", true, 18000,
                        "Brownies dark chocolate buatan kedai.", true)
        );
    }

    static InfoKedai getInfo() {
        return new InfoKedai(
                "1",
                "Senin - Jumat: 08.00 - 22.00 | Sabtu - Minggu: 09.00 - 23.00",
                "Jl. Bima No. 17, Ngalaban, Sinduharjo, Kec. Ngaglik, Kabupaten Sleman, DIY 40132",
                "0812-3456-7890"
        );
    }

    static List<Fasilitas> getFasilitas() {
        return List.of(
                new Fasilitas("1", "WiFi Gratis", "WiFi berkecepatan tinggi di seluruh area kedai."),
                new Fasilitas("2", "Ruang Baca", "Area tenang dengan rak buku untuk membaca atau belajar."),
                new Fasilitas("3", "Area Smoking", "Area merokok outdoor yang terpisah dari ruangan utama."),
                new Fasilitas("4", "Area Kerja", "Meja panjang dengan stop kontak untuk belajar atau bekerja."),
                new Fasilitas("5", "Area Terbuka", "Taman outdoor untuk nongkrong sore dan malam hari."),
                new Fasilitas("6", "Musholla", "Ruang ibadah kecil yang bersih dan nyaman."),
                new Fasilitas("7", "Parkir", "Area parkir untuk motor dan mobil.")
        );
    }
}
