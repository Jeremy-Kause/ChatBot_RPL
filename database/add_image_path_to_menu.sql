ALTER TABLE menu
    ADD COLUMN image_path VARCHAR(255) DEFAULT NULL AFTER deskripsi;

UPDATE menu SET image_path = 'asset/menu/espresso.jpg' WHERE nama_menu = 'Espresso';
UPDATE menu SET image_path = 'asset/menu/americano.jpg' WHERE nama_menu = 'Americano';
UPDATE menu SET image_path = 'asset/menu/cappuccino.jpg' WHERE nama_menu = 'Cappuccino';
UPDATE menu SET image_path = 'asset/menu/caffe-latte.jpg' WHERE nama_menu = 'Caffe Latte';
UPDATE menu SET image_path = 'asset/menu/kopi-susu-gula-aren.jpg' WHERE nama_menu = 'Kopi Susu Gula Aren';
UPDATE menu SET image_path = 'asset/menu/v60-pour-over.jpg' WHERE nama_menu = 'V60 Pour Over';
UPDATE menu SET image_path = 'asset/menu/cold-brew.jpg' WHERE nama_menu = 'Cold Brew';
UPDATE menu SET image_path = 'asset/menu/affogato.jpg' WHERE nama_menu = 'Affogato';
UPDATE menu SET image_path = 'asset/menu/matcha-latte.jpg' WHERE nama_menu = 'Matcha Latte';
UPDATE menu SET image_path = 'asset/menu/cokelat-panas.jpg' WHERE nama_menu = 'Cokelat Panas';
UPDATE menu SET image_path = 'asset/menu/teh-tarik.jpg' WHERE nama_menu = 'Teh Tarik';
UPDATE menu SET image_path = 'asset/menu/lemon-tea.jpg' WHERE nama_menu = 'Lemon Tea';
UPDATE menu SET image_path = 'asset/menu/milo-dinosaur.jpg' WHERE nama_menu = 'Milo Dinosaur';
UPDATE menu SET image_path = 'asset/menu/strawberry-smoothie.jpg' WHERE nama_menu = 'Strawberry Smoothie';
UPDATE menu SET image_path = 'asset/menu/es-kopi-matcha.jpg' WHERE nama_menu = 'Es Kopi Matcha';
UPDATE menu SET image_path = 'asset/menu/mocha-latte.jpg' WHERE nama_menu = 'Mocha Latte';
UPDATE menu SET image_path = 'asset/menu/taro-espresso.jpg' WHERE nama_menu = 'Taro Espresso';
UPDATE menu SET image_path = 'asset/menu/hazelnut-latte.jpg' WHERE nama_menu = 'Hazelnut Latte';
UPDATE menu SET image_path = 'asset/menu/caramel-macchiato.jpg' WHERE nama_menu = 'Caramel Macchiato';
UPDATE menu SET image_path = 'asset/menu/croissant-butter.jpg' WHERE nama_menu = 'Croissant Butter';
UPDATE menu SET image_path = 'asset/menu/roti-bakar-cokelat.jpg' WHERE nama_menu = 'Roti Bakar Cokelat';
UPDATE menu SET image_path = 'asset/menu/french-fries.jpg' WHERE nama_menu = 'French Fries';
UPDATE menu SET image_path = 'asset/menu/pisang-goreng-crispy.jpg' WHERE nama_menu = 'Pisang Goreng Crispy';
UPDATE menu SET image_path = 'asset/menu/nachos-cheese.jpg' WHERE nama_menu = 'Nachos Cheese';
UPDATE menu SET image_path = 'asset/menu/sandwich-tuna.jpg' WHERE nama_menu = 'Sandwich Tuna';
UPDATE menu SET image_path = 'asset/menu/brownies.jpg' WHERE nama_menu = 'Brownies';
