package com.bangkit.scantion.model

import com.bangkit.scantion.R

class News(
    val title: String,
    val thumb: Int,
    val listBody: List<Pair<Int?, String?>>
) {
    companion object {
        fun getData(): List<News> {
            return listOf(
                News(
                    title = "Jenis-jenis Kanker Kulit",
                    thumb = R.drawable.img_news_jenis_jenis_kanker_kulit,
                    listBody = listOf(
                        Pair(
                            first = R.drawable.img_news_11_karsinoma_sel_basal,
                            second = "1. Karsinoma Sel Basal \n" +
                                "\n" +
                                "Jenis kanker kulit yang pertama adalah karsinoma sel basal. Jenis kanker kulit ini merupakan yang paling umum dan lebih sering dijumpai dibandingkan dengan jenis lainnya. Pertumbuhan sel kanker dari karsinoma sel basal cenderung lambat, dan pada umumnya tidak menyebar ke bagian tubuh lain. Meski berbahaya, jenis kanker kulit ini dapat disembuhkan secara total jika terdeteksi dan diobati sejak dini. "),
                        Pair(
                            first = R.drawable.img_news_12_karsionoma_sel_skuamosa,
                            second = "2. Karsinoma Sel Skuamosa \n" +
                                    "\n" +
                                    "Jenis kanker kulit yang kedua adalah karsinoma sel skuamosa. Berbeda dengan karsinoma sel basal, jenis kanker kulit ini dapat menyebar ke lapisan kulit yang lebih dalam serta ke bagian tubuh lain, namun juga dapat diobati dan disembuhkan secara total jika terdeteksi dini. "
                        ),
                        Pair(
                            first = R.drawable.img_news_12_karsionoma_sel_skuamosa ,
                            second = "3. Melanoma \n" +
                                    "\n" +
                                    "Nah, jenis kanker kulit yang terakhir adalah melanoma. Jenis kanker kulit ini tergolong langka, namun juga mematikan. Kanker kulit ini dapat muncul ketika sel melanosit, pemberi pigmen warna pada kulit, tumbuh secara abnormal yang lambat laun dapat menjadi kanker. \n" +
                                    "\n" +
                                    "Well, itu dia ketiga jenis kanker kulit yang biasa ditemui. Untuk mendeteksinya secara dini, yuk kenali 8 gejala kanker kulit yang seringkali tidak kita sadari! "
                        )
                    )
                ),
                News(
                    title = "Gejala Kanker Kulit",
                    thumb = R.drawable.img_news_gejala_kanker_yang_wajib_diketahui,
                    listBody = listOf(
                        Pair(
                            first = null,
                            second = "Sama seperti penyakit lainnya, kanker kulit yang diderita oleh seseorang tentunya akan terjadi dengan munculnya gejala-gejala tertentu. Gejala ini seringkali tidak disadari, hingga sel kanker menyebar dan tumbuh lebih besar di kulit. Apa saja gejalanya? \n" +
                                    "\n" +
                                    "1. Muncul Bercak pada Kulit \n" +
                                    "\n" +
                                    "Gejala kanker kulit yang pertama adalah munculnya bercak kemerahan pada kulit. Bercak ini juga seringkali membuat kulit lebih kering dan bersisik, serta diiringi dengan rasa gatal yang dapat membuat kulit mengelupas atau berdarah jika digaruk. "
                        ),
                        Pair(
                            first = R.drawable.img_news_20_bercak_kulit,
                            second = "2. Timbul Benjolan pada Kulit\n" +
                                    "\n" +
                                    "Benjolan berkilau berwarna terang, merah muda, merah, atau putih bisa menjadi ciri-ciri dari kanker kulit. Pada beberapa kasus, benjolan juga bisa muncul berwarna gelap seperti tahi lalat. Seringkali, benjolan ini disalah artikan sebagai tahi lalat biasa. Untuk membedakannya, benjolan yang disebabkan oleh kanker pada umumnya memiliki ukuran yang tidak normal dan terus membesar. \n" +
                                    "\n" +
                                    "3. Nyeri Sendi \n" +
                                    "\n" +
                                    "Tidak hanya pada bagian kulit, beberapa kasus pada kanker kulit juga bisa menyebar ke area paru-paru, liver, hati, hingga tulang. Nah, jika sudah menyebar ke area tulang, biasanya persendian akan terasa nyeri di area lutut dan pinggang. "
                        ),
                        Pair(
                            first = R.drawable.img_news_21_kulit_bersisik,
                            second = "4. Kulit Bersisik \n" +
                                    "\n" +
                                    "Tekstur kulit terasa kasar hingga seperti bersisik juga bisa menjadi salah satu gejala kanker kulit, lho. Biasanya, kulit yang bersisik tersebar luas di berbagai area kulit seperti lengan, kaki, hingga punggung. \n" +
                                    "\n" +
                                    "5. Muncul Luka Kecil dan Kasar \n" +
                                    "\n" +
                                    "Kulit mengalami luka kecil yang kasar dan terasa bersisik juga bisa menjadi salah satu gejala kanker kulit. Umumnya, luka akan muncul pada kulit di area yang sering terpapar oleh matahari tanpa adanya perlindungan UVA dan UVB. \n" +
                                    "\n" +
                                    "6. Kulit Gatal dan Nyeri \n" +
                                    "\n" +
                                    "Rasa gatal dan nyeri yang muncul sekaligus di permukaan kulit merupakan salah satu gejala awal kanker kulit yang bisa dirasakan. Rasa gatal ini bisa menjadi sebagai tanda bahwa sel kanker sedan menyerang sel kulit tubuh yang sehat, dan muncul di area kulit yang tampak kasar juga bersisik. \n" +
                                    "\n" +
                                    "7. Perubahan Warna Kulit \n" +
                                    "\n" +
                                    "Perubahan warna kulit yang drastis menjadi merah, hitam, atau kebiruan pada area kulit tertentu menjadi salah satu gejala kanker kulit yang perlu kamu perhatikan. Hal ini terjadi karena pertumbuhan melanosit yang berlebihan sehingga memicu perubahan warna kulit dan tumbuhnya sel kanker pada kulit. \n" +
                                    "\n" +
                                    "8. Luka yang tak Kunjung Sembuh \n" +
                                    "\n" +
                                    "Gejala terakhir dari kanker kulit yang harus diwaspadai adalah munculnya luka terbuka yang tak kunjung sembuh. Biasanya, luka ini memiliki pinggiran yang berkerak dan mengeluarkan cairan seperti nanah. \n" +
                                    "\n" +
                                    "Itu dia beberapa gejala kanker kulit yang bisa saja muncul tanpa disadari. Tidak perlu panik, jika mengalami salah dua atau lebih dari beberapa gejala di atas, kamu bisa segera periksa ke dokter terdekat untuk mengetahui diagnosa serta penanganan yang tepat, ya! "
                        )
                    )
                ),
                News(
                    title = "Penyebab Kanker Kulit",
                    thumb = R.drawable.img_news_penyebab_kanker_kulit,
                    listBody = listOf(
                        Pair(
                            first = R.drawable.img_news_30_penyebab_kanker_kulit,
                            second = "Penyebab Kanker Kulit\n" +
                                    "\n" +
                                    "Lalu, apa saja yang bisa menyebabkan kanker kulit terjadi? Secara garis besar, kanker kulit disebabkan oleh terjadinya perubahan atau mutasi genetik pada sel kulit. Kondisi ini dapat dipicu oleh dua faktor, yaitu faktor internal dan eksternal. \n" +
                                    "Faktor Internal\n" +
                                    "\n" +
                                    "1. Riwayat kanker kulit pada keluarga: seseorang yang pernah mengalami kanker kulit memiliki risiko yang cukup tinggi mengalami masalah kesehatan tersebut kembali. Risiko ini juga bisa meningkat ketika ada anggota keluarga yang memiliki riwayat penyakit yang sama. \n" +
                                    "\n" +
                                    "2. Kulit berwarna terang: memang, kanker bisa saja terjadi pada siapapun tanpa memandang warna kulit. Namun, tahukah kamu bahwa pemilik kulit yang lebih terang memiliki risiko lebih besar mengalami kanker kulit? Hal ini disebabkan jumlah melanin pada kulit yang lebih rendah, sehingga perlindungan terhadap paparan UV yang dimiliki menjadi lebih rendah. \n" +
                                    "\n" +
                                    "3. Imunitas tubuh lemah: individu yang memiliki imunitas tubuh memiliki risiko yang lebih besar mengalami kanker kulit. Misalnya, pengidap HIV/AIDS atau kelompok orang yang mengonsumsi obat jenis imunosupresan. \n" +
                                    "Faktor Eksternal\n" +
                                    "\n" +
                                    "1. Paparan sinar UV: tahukah kamu? Selain menjadi penyebab utama terjadinya penuaan dini, paparan sinar UV dari matahari juga bisa meningkatkan risiko seseorang mengalami kanker kulit, lho. Hal ini disebabkan oleh kemampuan sinar UVA yang mampu menembus hingga ke lapisan terdalam kulit dan mengubah mutasi DNA pada kulit. Tanda awal terjadinya kanker kulit yang disebabkan oleh sinar UV adalah terjadinya hiperpigmentasi pada kulit, kulit kering dan bersisik, serta muncul luka terbuka yang tak kunjung sembuh. \n" +
                                    "\n" +
                                    "2. Paparan radiasi: seseorang yang melakukan pengobatan dengan terapi radiasi seperti penderita eksim atopik atau jerawat juga memiliki risiko tinggi terserang kanker kulit jenis karsinoma sel basal. \n" +
                                    "\n" +
                                    "3. Paparan senyawa atau bahan kimia: Beberapa bahan kimia yang bersifat karsinogenik seperti arsenik diyakini dapat memicu kanker kulit oleh beberapa pakar kesehatan. Jadi sebaiknya, hindari atau gunakan sistem pengamanan yang sudah sesuai standar ketika kamu harus bekerja dengan berbagai paparan senyawa ini, ya! "
                        )
                    )
                ),
                News(
                    title = "Mencegah Kanker Kulit",
                    thumb = R.drawable.img_news_mencegah_kanker_kulit,
                    listBody = listOf(
                        Pair(
                            first = R.drawable.img_news_40_sunscreen_setiap_hari,
                            second = "Meskipun banyak faktor yang dapat menyebabkan kanker kulit, kamu bisa kok mencegah hal ini terjadi dengan berbagai langkah yang tepat. Apa saja? \n" +
                                    "\n" +
                                    "1. Gunakan Sunscreen Setiap Hari \n" +
                                    "\n" +
                                    "Salah satu penyebab terjadinya kanker kulit adalah kulit yang terus menerus terpapar sinar UV dari matahari tanpa adanya perlindungan. Nah, untuk menghindari risiko kanker kulit, hal utama yang perlu kamu lakukan adalah menggunakan sunscreen dengan perlindungan UVA dan UVB setiap hari. Jangan lupa, lakukan juga reapply setiap dua jam sekali untuk mempertahankan perlindungan dari sunscreen yang kamu gunakan tetap kuat, ya! \n" +
                                    "\n" +
                                    "2. Hindari Penggunaan Tanning Bed \n" +
                                    "\n" +
                                    "Penggunaan tanning bed atau alat yang dapat menggelapkan kulit memang tidak terlalu umum di Indonesia. Namun sebaiknya, tetap hindari penggunaan alat ini karena menggunakan tanning bed membuat kulit terpapar oleh sinar UV yang cukup kuat. \n" +
                                    "\n" +
                                    "3. Perhatikan Obat yang Dikonsumsi \n" +
                                    "\n" +
                                    "Menggunakan obat tertentu bisa menyebabkan efek samping pada kulit, salah satunya adalah antibiotik. Agar lebih aman, sebaiknya kamu konsultasikan pada dokter terlebih dahulu ketika hendak mengonsumsi obat tertentu, ya! \n" +
                                    "\n" +
                                    "4. Periksa Kulit Secara Berkala \n" +
                                    "\n" +
                                    "Kamu bisa melakukan pemeriksaan kulit secara rutin dan mengkonsultasikan kondisi kesehatan kamu pada dokter. Jadi, jangan sampai menunggu muncul beberapa gejala. \n" +
                                    "\n" +
                                    "Meskipun kanker kulit merupakan penyakit yang cukup berbahaya, tapi selama kamu mengetahui gejala serta metode pencegahan yang tepat, kamu bisa kok terhindar dari risiko penyakit kanker kulit ini. Jadi, jangan lupa gunakan sunscreen setiap hari dan lakukan metode pencegahan lainnya sesuai panduan di atas, ya! "
                        )
                    )
                )
            )
        }
    }

}