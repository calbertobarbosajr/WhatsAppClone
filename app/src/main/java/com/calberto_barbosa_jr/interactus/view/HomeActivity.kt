package com.calberto_barbosa_jr.interactus.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.calberto_barbosa_jr.interactus.R
import com.calberto_barbosa_jr.interactus.databinding.ActivityHomeBinding
import com.calberto_barbosa_jr.interactus.fragment.ContactsFragment
import com.calberto_barbosa_jr.interactus.fragment.ConversationsFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = binding.includeToolbar.toolbarMain
        toolbar.setTitle("Interactus")
        setSupportActionBar(toolbar)


        //Configurar abas
        val adapter = FragmentPagerItemAdapter(
            getSupportFragmentManager(),
            FragmentPagerItems.with(this)
                .add("Conversas", ConversationsFragment::class.java)
                .add("Contatos", ContactsFragment::class.java)
                .create()
        )
        val viewPager = findViewById<ViewPager?>(R.id.viewPager)
        viewPager.setAdapter(adapter)

        val viewPagerTab = findViewById<SmartTabLayout?>(R.id.viewPagerTab)
        viewPagerTab.setViewPager(viewPager)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = getMenuInflater()
        inflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menuExit -> {
                exit()
                finish()
            }

            R.id.SettingsMenu -> OpenSettings()
        }

        return super.onOptionsItemSelected(item)
    }

    fun exit() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun OpenSettings() {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}