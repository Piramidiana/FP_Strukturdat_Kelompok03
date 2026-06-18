## _**Course Prerequisite Planner:**_ **Sistem Rekomendasi Urutan Pengambilan Mata Kuliah** 

## **Nama Anggota:** 

Arjunina Maqbulin Usman (5027251007) Dian Piramidiana Rachmatika (5027251031) Izzat Ilham Wahyudi (5027251062) Muhammad Atallah Mas`udi (5027251071) 

## **Deskripsi Masalah** 

Mahasiswa seringkali mengalami kesulitan dalam menyusun rencana studi yang optimal karena kompleksitas aturan prasyarat mata kuliah. Kesalahan dalam merencanakan urutan pengambilan dapat berdampak pada penundaan kelulusan, terutama jika terjadi konflik kurikulum di mana terbentuk siklus prasyarat yang tidak logis. Untuk mengatasi masalah ini, kami mengembangkan "Course Prerequisite Planner". Aplikasi ini dapat merekomendasikan urutan pengambilan mata kuliah yang aman, memetakan rantai prasyarat secara jelas, serta secara otomatis mendeteksi jika ada siklus prasyarat yang tidak valid di dalam sistem. 

## **Dataset** 

Sistem ini menggunakan dataset buatan sendiri yang memodelkan kurikulum. Untuk menjaga integritas data, dataset dipisah menjadi dua file: 

1. Node (Mata Kuliah) matakuliah.csv : Terdapat 25 node yang merepresentasikan mata kuliah. Data ini memiliki 6 atribut: Kode, Nama, SKS, Semester, Jurusan, dan Deskripsi. 

2. Edge (Relasi) prasyarat.csv : Terdapat 40 edge terarah yang merepresentasikan aturan prasyarat. 

## **Struktur Graph yang digunakan** 

Sistem ini mengimplementasikan Directed Graph menggunakan representasi Adjacency List. 

1. Node (Vertex): Merepresentasikan objek Mata Kuliah. 

2. Edge: Merepresentasikan hubungan prasyarat secara satu arah. Arah panah (edge) dibuat mengarah dari mata kuliah prasyarat menuju mata kuliah lanjutan (contoh: Algoritma ke Struktur Data). 

3. Alasan Pemilihan: Representasi adjacency list sangat cocok karena graf prasyarat kurikulum bersifat sparse (memiliki 25 node namun hanya sekitar 40 relasi). Struktur ini jauh lebih hemat alokasi memori dibandingkan adjacency matrix. 

## **Struktur Tree yang Digunakan** 

Sistem ini menggunakan AVL Tree, yaitu jenis Self-Balancing Binary Search Tree, untuk menyimpan dan mengelola keseluruhan data mata kuliah. 

1. Key: Kode mata kuliah (bertipe String, diurutkan secara leksikografis). 

2. Value: Objek Mata Kuliah (berisi nama, SKS, semester, dll). 

3. Alasan Pemilihan: Modifikasi dari rencana awal menggunakan B+ Tree ke AVL Tree dilakukan untuk efisiensi di level memori utama (in-memory). Mekanisme auto-balancing (rotasi Left-Left, Right-Right, Left-Right, Right-Left) pada AVL Tree menjamin kompleksitas pencarian, penyisipan, dan penghapusan selalu beroperasi stabil di O(log n). 

## **Algoritma yang digunakan** 

Sistem ini menggabungkan tiga algoritma graf untuk menjamin validitas akademik: 

1. Topological Sort: Digunakan pada Directed Acyclic Graph (DAG) untuk menentukan urutan pengambilan mata kuliah yang valid. Mata kuliah tanpa prasyarat (in-degree 0) akan dieksekusi pertama kali, disusul oleh mata kuliah tingkat lanjut. 

2. Cycle Detection (DFS-based): Berfungsi sebagai safety mechanism sebelum Topological Sort dijalankan. Algoritma menelusuri graf dan mendeteksi apakah ada siklus/lingkaran tak berujung (misal: Matkul A butuh Matkul B, namun Matkul B butuh Matkul A). 

3. Depth-First Search (DFS): Digunakan untuk melakukan traversal guna menampilkan seluruh rantai prasyarat (langsung maupun tidak langsung) dari suatu mata kuliah secara mendalam. 

## **Design Decision Log** 

|No|Keputusan|Alternatif|Alasan Memilih|Risiko/Kelemahan|
|---|---|---|---|---|
|1|AVL Tree|Trie/HasMap|Self-balancing binary<br>search tree menjamin<br>pencarian yang cepat<br>pada worst-case secara<br>terurut, cocok untuk<br>mencari dan|Membutuhkan operasi<br>rotasi tambahan saat proses<br>insert dan delete, sehingga<br>menambah sedikit<br>kompleksitas.|



||||menampilkan mata<br>kuliah.||
|---|---|---|---|---|
|2|Adjacency<br>List|Adjacency<br>Matrix|Graph prasyarat bersifat<br>sparse (hanya sekitar 40<br>relasi dari 25 node),<br>sehingga struktur ini jauh<br>lebih hemat memori.|Pengecekan apakah sebuah<br>edge spesifik dari A ke B<br>ada akan sedikit lebih<br>lambat. O(degree)|
|3|Topological<br>Sort<br>(Kahn's/DFS)|Pengurutan<br>manual|Algoritma ini secara<br>otomatis dapat<br>menghasilkan urutan<br>belajar yang valid<br>berdasarkan dependensi<br>prasyarat.|Algoritma akan gagal dan<br>tidak bisa memberikan hasil<br>jika graph memiliki siklus<br>(prasyarat melingkar).|
|4|Cycle<br>Detection<br>(DFS + visited<br>state)|Tidak dicek|Adanya prasyarat sirkular<br>adalah bug sistematis<br>kurikulum yang sangat<br>fatal dan wajib dideteksi<br>oleh sistem.|Menambah beban<br>(overhead) traversal graph<br>setiap kali pengguna<br>memasukkan prasyarat<br>baru.|
|5|DFS untuk<br>traversal<br>prasyarat|BFS|Sangat cocok dan efisien<br>untuk menelusuri<br>kedalaman rantai<br>prasyarat suatu mata<br>kuliah dari awal hingga<br>akhir.|Berisiko terkena stack<br>overflow jika jumlah mata<br>kuliah dan rantai<br>prasyaratnya terlalu<br>panjang/dalam.|



## **Analisis Kompleksitas** 

|Operasi|Struktur Algoritma|Kompleksitas|Alasan|
|---|---|---|---|
|Insert mata<br>kuliah|AVL Tree|O(log n)|Proses penyeimbangan otomatis<br>(rotations) memastikan tinggi tree<br>selalu seimbang sebesar nilai<br>logaritmik dari node.|



|Search mata<br>kuliah by code|AVL Tree|O(log n)|Traversal pencarian (binary search)<br>beroperasi memotong cabang dari<br>root hingga node target ditemukan.|
|---|---|---|---|
|Range Search<br>(missal:<br>semester 1-3)|AVL Tree|O(n)|Tanpa adanya linked list pada leaf<br>node (berbeda dengan B+ Tree),<br>pencarian filter di luar key utama<br>mengharuskan in-order traversal<br>untuk memeriksa seluruh node.|
|Traversal<br>Prasyarat|DFS|O(V+E)|Algoritma harus mengunjungi semua<br>vertex (node) dan edge dalam graph<br>prasyarat.|
|Urutan belajar|Topological Sort|O(V+E)|Setiap node dan edge diproses tepat<br>satu kali untuk menentukan urutan<br>yang valid.|
|Deteksi siklus|DFS + state<br>tracking|O(V+E)|Sama dengan DFS biasa, dengan<br>tambahan pengecekan status (state)<br>pada setiap node untuk mendeteksi<br>jalur melingkar.|
|Insert prasyarat<br>ke graph|Adjacency List|O(1)|Hanya menambahkan elemen baru ke<br>akhir memori dari struktur list yang<br>sudah ada.|



## **What if Analysis** 

1. Apa yang terjadi jika jumlah mata kuliah naik dari 25 menjadi 10.000? 

AVL Tree tetap sangat efisien karena beroperasi sebagai self-balancing binary search tree dengan kompleksitas pencarian O(log n). Kenaikan dari 25 ke 10.000 node hanya akan meningkatkan tinggi tree ke sekitar 14 level. Namun, penggunaan memori pada Adjacency List akan meningkat seiring bertambahnya relasi prasyarat. Algoritma Topological Sort dan DFS tetap berjalan dalam batas O(V+E), tetapi waktu eksekusinya akan meningkat secara linear sesuai dengan jumlah node dan edge. 

2. Apa yang terjadi jika ada prasyarat sirkular (siklus)? 

Contoh nyata dari dataset: jika ditambahkan relasi ET234103 (Algoritma) ke  ET234203 (Struktur Data) ke  ET234103, maka akan terbentuk siklus. Dalam kondisi ini, algoritma Topological Sort tidak bisa menghasilkan urutan belajar yang valid. Sistem harus menjalankan fitur Cycle Detection terlebih dahulu sebelum Topological Sort. Jika siklus ditemukan, sistem secara otomatis menampilkan peringatan dan membatalkan operasi pengurutan agar mahasiswa tidak mendapat rekomendasi yang keliru. 

## 3. Apa yang terjadi jika relasi prasyarat tertentu dihapus? 

Contoh: jika relasi ET234203 → ET234103 (Struktur Data membutuhkan Algoritma) dihapus, maka mata kuliah ET234203 bisa langsung diambil tanpa prasyarat apa pun. Urutan pada Topological Sort akan langsung berubah karena tingkat ketergantungannya berkurang. Sistem harus menjalankan ulang algoritma Topological Sort setelah operasi penghapusan edge agar dapat menghasilkan urutan rekomendasi baru yang valid. 

## 4. Apa yang terjadi jika ada data mata kuliah duplikat? 

Contoh: jika mata kuliah ET234103 di-insert dua kali ke dalam AVL Tree, operasi insert kedua akan diabaikan atau menimpa data yang lama, karena kode mata kuliah bertindak sebagai key unik di dalam tree. Pada struktur Graph, jika tidak ada pengecekan duplikasi saat menambah edge, bisa terbentuk relasi ganda antar node yang sama. Sistem harus selalu melakukan validasi kode unik sebelum proses insert dijalankan untuk menjaga integritas data. 

## 5. Apa yang terjadi jika input user tidak ditemukan di sistem? 

Contoh: user mencari kode "ET999999" yang tidak ada di dalam dataset. AVL Tree akan melakukan traversal dengan membandingkan nilai key hingga mencapai leaf node terakhir (null), lalu mengembalikan nilai null. Graph juga tidak bisa menjalankan DFS dari node yang tidak terdaftar. Sistem akan menangani edge case ini dengan menampilkan pesan error yang informatif, seperti "Mata kuliah dengan kode ET999999 tidak ditemukan", agar user menyadari bahwa data tidak ada dan dapat menginputkan ulang data yang benar. 

## 6. Apa yang terjadi jika graph tidak terhubung (ada mata kuliah tanpa relasi)? 

Contoh dari dataset: ET234104 (Hukum dan Etika TI), SM234101 (Kalkulus 1), dan ET234202 (Arsitektur Enterprise) tidak memiliki relasi prasyarat ke mata kuliah lain di semester berikutnya. Topological Sort tetap dapat berjalan dengan baik karena algoritma ini memproses semua komponen secara independen. Mata kuliah tanpa prasyarat (memiliki in-degree bernilai 0) akan diprioritaskan muncul di awal urutan rekomendasi. Untuk proses DFS, iterasi harus dijalankan ke semua node yang belum dikunjungi (unvisited) agar seluruh komponen graph, baik yang terhubung maupun tidak, dapat ter-cover sepenuhnya. 

## **Tracing Manual** 

1. Gambar Tracing AVL Tree (Kasus Rotasi Right-Right), sebelum dan sesudah rotasi 

2. Tracing Topological Sort, Gambar ini menunjukkan graf prasyarat sederhana dan antrean (queue) saat Algoritma Topological Sort memproses mata kuliah yang tidak memiliki prasyarat (In-Degree = 0). 

3. Tracing Cycle Detection (Deteksi Siklus), Gambar ini menunjukkan graf yang memiliki siklus terlarang (A ke B, B ke C, C kembali ke A). Garis merah gelap (maroon) menandakan titik di mana sistem mendeteksi siklus. 

## **Dokumentasi Hasil Program** 

Tampilan awal 

1. Add Course 

## 2. Delete Course 

## 3. Add Prerequisite 

## 4. Search Course 

## 5. Show Prerequisite Chain 

## 6. Show Dependent Course 

## 7. Rekomendasi _study plan_ 

## 8. Show All Course 

## **Kesimpulan** 

Berdasarkan perancangan, implementasi, dan analisis yang telah dilakukan pada aplikasi Course Prerequisite Planner, dapat ditarik beberapa kesimpulan utama: 

1. Efisiensi Manajemen Data Utama: Penggunaan struktur data AVL Tree terbukti memberikan performa yang optimal dan stabil. Sifat self-balancing dari tree ini memastikan bahwa setiap operasi manipulasi data tunggal (seperti insert, search, dan delete) selalu beroperasi pada kompleksitas waktu yang konsisten di O(log n), menghindari penurunan performa menjadi linear meskipun jumlah mata kuliah bertambah secara signifikan. 

2. Pemetaan Relasi yang Skalabel: Implementasi Directed Graph dengan representasi Adjacency List merupakan pilihan arsitektur yang sangat tepat. Karena sifat graf kurikulum akademik yang sparse (minim kepadatan relasi), struktur ini meminimalisir alokasi memori yang terbuang jika dibandingkan dengan penggunaan adjacency matrix. 

3. Validitas Rencana Studi: Algoritma Topological Sort berhasil memenuhi tujuan utama sistem dalam menyusun urutan linier pengambilan mata kuliah yang valid dan terbebas dari pelanggaran prasyarat, memastikan alur belajar mahasiswa berjalan secara logis. 

4. Keamanan Eksekusi (Fail-safe): Penambahan mekanisme Cycle Detection berbasis modifikasi algoritma DFS bertindak sebagai lapisan keamanan kritis (safety net). Fitur ini secara proaktif mencegah error sistematis dan menghentikan pembentukan circular prerequisite (prasyarat melingkar) yang dapat menghambat mahasiswa dalam menyelesaikan studinya.Secara keseluruhan, arsitektur sistem yang dibangun tidak hanya memecahkan permasalahan perencanaan studi secara fungsional, namun juga memiliki landasan teoretis yang kuat dan terukur untuk menangani skalabilitas data di masa depan. 

