<h1 align="center">WhatsApp Clone for Android</h1>

---

<h2>Description</h2>
<p>
This project is a functional clone of WhatsApp, initially developed as part of the course 
<a href="https://www.udemy.com/course/curso-de-desenvolvimento-android-oreo/">"Desenvolvimento Android Completo - Crie 18 Apps"</a> 
by Jamilton Damasceno on Udemy.
</p>

---

<h2>Overview</h2>
<p>The application replicates the core functionalities of WhatsApp, allowing users to:</p>
<ul>
  <li>✅ Send text messages and images</li>
  <li>✅ Search for contacts to start conversations quickly</li>
  <li>✅ Create groups for collective chats</li>
</ul>

<p>
Initially, the project was developed in Java, but I am migrating the code to Kotlin. This transition will enable the future replacement of the XML interface with Jetpack Compose, making the code more modern, declarative, and efficient.
</p>
<p>
When the course was created, Kotlin was still under development, and Jetpack Compose likely did not exist. Therefore, I am updating the project to incorporate the latest best practices.
</p>

---

<h2>Next Steps</h2>
<p>Beyond adopting Jetpack Compose, the following improvements will be implemented:</p>
<ul>
  <li>✔ SOLID principles for a more modular and sustainable codebase</li>
  <li>✔ MVVM architecture for better separation of concerns and maintainability</li>
</ul>

---

<h2>Technologies Used</h2>
<ul>
  <li><strong>Kotlin</strong> (in progress)</li>
  <li><strong>Firebase</strong> (Storage, Firestore Database, and Authentication)</li>
  <li><strong>Jetpack Compose</strong> (planned for the future)</li>
  <li><strong>MVVM Architecture</strong> (planned for the future)</li>
</ul>
<p>
This project demonstrates my progress on the Android platform, applying modern concepts and best practices. Feel free to explore, contribute, or contact me for suggestions!
</p>

---

<h2>How to Run the Project</h2>
<ol>
  <li>Clone this repository to your local machine:</li>
  <pre><code>git clone https://github.com/calbertobarbosajr/WhatsApp-Clone.git</code></pre>
  <li>Open the project in Android Studio.</li>
  <li>Ensure you have the necessary dependencies installed.</li>
  <li>Run the app on an Android emulator or physical device.</li>
</ol>

<p>
The code is being developed using <strong>Android Studio Ladybug | 2024.2.1 Patch 3</strong>.
</p>

---

<h2>Dependencies</h2>
<p>Ensure you have the following dependencies in the <code>build.gradle</code> file of the app module:</p>

<pre><code>
// Firebase dependencies
implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
implementation("com.google.android.gms:play-services-auth:21.2.0")
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-database-ktx")
implementation("com.google.firebase:firebase-storage-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")

// Image loading library
implementation("io.coil-kt:coil-compose:2.5.0")

// Tab layout dependencies
// https://github.com/ogaclejapan/SmartTabLayout
implementation("com.ogaclejapan.smarttablayout:library:2.0.0@aar")
implementation("com.ogaclejapan.smarttablayout:utils-v4:2.0.0@aar")

// Circular image view
implementation("de.hdodenhof:circleimageview:2.2.0")

// Material search view
implementation("io.gitlab.alexto9090:materialsearchview:1.0.0")
</code></pre>

---

<h2>Notes</h2>
<p>
This app was developed as part of a course and may contain specific learning functionalities. Ensure to adjust the code as needed to meet your specific requirements.
</p>

---

<h2>Contributions</h2>
<p>Contributions are welcome! If you find ways to improve or expand the app, feel free to contribute by opening issues or sending pull requests.</p>

---

<h2>License</h2>
<p>This project is licensed under the <strong>MIT License</strong>. Refer to the <a href="https://www.mit.edu/~amini/LICENSE.md">LICENSE</a> file for more details.</p>
