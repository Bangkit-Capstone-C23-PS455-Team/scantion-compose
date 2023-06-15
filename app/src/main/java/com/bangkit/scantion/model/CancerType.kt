package com.bangkit.scantion.model

class CancerType(
    val displayName: String,
    val fullName: String,
    val desc: String,
    val symptom: String,
    val type: String,
    val note: String
) {
    companion object {
        val listKey = listOf("Benign", "Melanoma", "Normal")
        fun getData(): Map<String, CancerType> {
            return mapOf(
                "Benign" to
                        CancerType(
                            displayName = "Benign",
                            fullName = "Benign / tumor (non kanker)",
                            desc = "Benign mengacu pada kondisi atau pertumbuhan non-kanker. Tidak seperti tumor ganas (kanker), tumor Benign tidak menyerang jaringan disekitarnya atau menyebar ke bagian tubuh yang lain. Mereka cenderung tumbuh lambat dan tetap terlokalisasi di tempat asalnya.",
                            symptom = "1. Non-invasif: Tumor Benign tidak menyerang jaringan atau organ di sekitarnya. Mereka biasanya tumbuh dengan cara yang terkendali dan tidak menyebar ke area lain di tubuh.\n" +
                                    "\n" +
                                    "2. Perbatasan yang jelas: Tumor Benign sering memiliki batas yang jelas dan jelas, yang dapat membantu membedakannya dari tumor ganas.\n" +
                                    "\n" +
                                    "3. Pertumbuhan lambat: Tumor Benign cenderung tumbuh lebih lambat dibandingkan tumor ganas. Tingkat pertumbuhan mereka dapat bervariasi tergantung pada jenis dan lokasi tumor.\n" +
                                    "\n" +
                                    "4. Berkapsul: Beberapa tumor Benign berkapsul, artinya mereka dikelilingi oleh kapsul berserat yang memisahkannya dari jaringan di sekitarnya.\n" +
                                    "\n" +
                                    "5. Penampilan sel normal: Di bawah mikroskop, sel-sel pada tumor Benign umumnya menyerupai sel normal. Mereka tidak menunjukkan kelainan seluler yang signifikan atau pembelahan sel yang tidak normal.\n" +
                                    "\n" +
                                    "6. Tidak ada metastasis: Tumor Benign tidak memiliki kemampuan untuk bermetastasis atau menyebar ke bagian tubuh lain melalui aliran darah atau sistem limfatik.",
                            type = "1. Nevus (NV): Nevus mengacu pada tahi lalat Benign atau tanda lahir pada kulit. Meskipun sebagian besar nevi tidak berbahaya, jenis tertentu, seperti nevi atipikal atau displastik, memiliki risiko yang sedikit lebih tinggi untuk berkembang menjadi melanoma.\n" +
                                    "\n" +
                                    "2. Basal Cell Carcinoma (BCC): BCC adalah jenis kanker kulit yang paling umum, tetapi bukan merupakan bentuk melanoma. Ini biasanya berkembang di sel basal lapisan terluar kulit dan umumnya disebabkan oleh paparan sinar UV matahari yang terlalu lama.\n" +
                                    "\n" +
                                    "3. Keratosis aktinik (AKIEC): Keratosis aktinik, juga dikenal sebagai keratosis surya, adalah kondisi prakanker yang muncul sebagai bercak kasar dan bersisik pada area kulit yang terpapar sinar matahari. Meskipun AKIEC bukan melanoma, penting untuk memantau dan mengobati lesi ini karena dapat berkembang menjadi karsinoma sel skuamosa, sejenis kanker kulit.\n" +
                                    "\n" +
                                    "4. Benign Keratosis-Like Lesions (BKL): Istilah ini umumnya mencakup berbagai lesi kulit Benign yang menyerupai keratosis atau bercak kasar pada kulit. Contohnya mungkin termasuk keratosis seboroik atau keratoacanthoma. Lesi ini tidak bersifat kanker dan biasanya tidak berkembang menjadi melanoma.\n" +
                                    "\n" +
                                    "5. Dermatofibroma (DF): Dermatofibroma adalah pertumbuhan kulit Benign yang umum yang biasanya berkembang sebagai benjolan kecil dan keras pada kulit. Mereka sering muncul di kaki dan tidak berhubungan dengan melanoma.\n" +
                                    "\n" +
                                    "6. Lesi Vaskular (VASC): Lesi vaskular mencakup berbagai jenis kelainan pada pembuluh darah, seperti hemangioma, malformasi vena, atau tumor vaskular. Lesi ini bukan melanoma melainkan melibatkan pembuluh darah di dalam kulit.",
                            note = "Penting untuk dicatat bahwa meskipun tumor Benign umumnya tidak mengancam jiwa, pertumbuhan atau keberadaannya di lokasi tertentu masih dapat menyebabkan masalah kesehatan. Beberapa tumor Benign, tergantung pada ukuran atau lokasinya, dapat memberikan tekanan pada struktur di dekatnya, menyebabkan gejala atau komplikasi. Dalam kasus seperti itu, intervensi medis mungkin diperlukan untuk mengatasi tumor. Namun, pemantauan dan tindak lanjut secara teratur dapat direkomendasikan untuk memastikan tumor tetap Benign dan tidak mengalami perubahan signifikan dari waktu ke waktu. Dan SCANTION merekomendasikan semua pengguna yang memiliki Gejala Benign tersebut untuk pergi ke Rumah Sakit atau Dokter terdekat untuk mendapatkan hasil yang spesifik dan informasi rinci tentang hal itu."
                        ),
                "Melanoma" to
                        CancerType(
                            displayName = "Melanoma",
                            fullName = "Melanoma (Maglinant)",
                            desc = "Melanoma, juga disebut melanoma ganas, adalah kanker yang biasanya dimulai di kulit. Itu bisa dimulai di tahi lalat atau di kulit yang tampak normal. Melanoma adalah tumor yang dihasilkan oleh transformasi ganas melanosit.",
                            symptom = "1. Tahi Lalat Tidak Beraturan: Tahi lalat baru atau tahi lalat yang sudah ada yang mengalami perubahan ukuran, bentuk, warna, atau tekstur mungkin merupakan tanda peringatan. Melanoma sering memiliki batas yang tidak rata atau tidak beraturan dan mungkin lebih besar dari penghapus pensil.\n" +
                                    "\n" +
                                    "2. Asimetri: Melanoma biasanya asimetris, artinya separuh tahi lalat atau lesi tidak cocok dengan separuh lainnya.\n" +
                                    "\n" +
                                    "3. Warna Bervariasi: Melanoma mungkin memiliki banyak warna atau bayangan dalam lesi yang sama, termasuk area berwarna coklat, hitam, biru, merah, atau putih.\n" +
                                    "\n" +
                                    "4. Diameter: Melanoma biasanya berdiameter lebih besar dibandingkan dengan tahi lalat biasa. Mereka umumnya lebih besar dari 6 milimeter (seukuran penghapus pensil), tetapi bisa juga lebih kecil.\n" +
                                    "\n" +
                                    "5. Berkembang atau Berubah: Setiap perubahan dalam ukuran, bentuk, warna, ketinggian, atau karakteristik lain dari tahi lalat atau area berpigmen harus dievaluasi. Ini termasuk gatal, pendarahan, pengerasan kulit, atau ulserasi tahi lalat.\n" +
                                    "\n" +
                                    "6. Penyebaran: Melanoma dapat meluas melampaui batas tahi lalat atau lesi, tumbuh ke kulit di sekitarnya.\n" +
                                    "\n" +
                                    "7. Sensasi: Beberapa individu mungkin mengalami nyeri tekan, nyeri, atau gatal di area yang terkena.",
                            type = "",
                            note = "Penting untuk dicatat bahwa tidak semua melanoma menunjukkan gejala ini, dan beberapa melanoma mungkin kekurangan pigmentasi dan muncul sebagai lesi berwarna merah muda, merah, atau berwarna daging. Sangat penting untuk memeriksa kulit Anda secara teratur dan berkonsultasi dengan dokter kulit jika Anda melihat adanya perubahan atau kelainan pada tahi lalat atau kulit Anda. Deteksi dan pengobatan dini sangat meningkatkan kemungkinan hasil yang sukses dalam kasus melanoma. Jadi jika anda dideteksi menderita kanker kulit melanoma maka SCANTION menyarankan anda untuk berobat ke Dokter atau Rumah Sakit terdekat."
                        ),
                "Normal" to
                        CancerType(
                            displayName = "Normal",
                            fullName = "Kulit Normal",
                            desc = "1. Kulit normal memiliki area yang bersih dan tidak ada bekas luka sama sekali. Kulit normal tampak halus, kencang, dan warnanya konsisten. Itu tidak memiliki pertumbuhan yang tidak biasa, perubahan warna, atau batas yang tidak teratur.\n" +
                                    "\n" +
                                    "2. Kulit normal memiliki tingkat pigmentasi yang seimbang. Ini mungkin memiliki variasi warna kulit karena faktor-faktor seperti paparan sinar matahari, etnis, atau variasi alami, tetapi tidak memiliki area pigmen yang besar atau berbentuk tidak teratur yang mengindikasikan melanoma.\n" +
                                    "\n" +
                                    "3. Tahi lalat biasa terjadi pada kulit normal dan biasanya tidak berbahaya. Tahi lalat normal biasanya kecil, berbentuk bulat atau oval, dan memiliki warna yang konsisten. Mereka mungkin datar atau sedikit terangkat tetapi biasanya tidak lebih besar dari diameter penghapus pensil.",
                            symptom = "",
                            type = "",
                            note = ""
                        ),
            )
        }
    }
}