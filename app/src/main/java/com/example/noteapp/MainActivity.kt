package com.example.noteapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 29

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> {
                    // Respond to navigation item 1 click
                    findNavController(R.id.nav_host).navigate(R.id.notesListFragment2)
                    true
                }
                R.id.page_2 -> {
                    // Respond to navigation item 2 click
                    findNavController(R.id.nav_host).navigate(R.id.voiceNotesFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("Home", "Permission Granted")
                }else{
                    Log.d("Home", "Permission Failed")
                    Toast.makeText(this, "You must allow permission record audio to your mobile device.", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
            }
        }
    }

    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                    ),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                Log.d("Home", "Already granted access")
            }
        }
    }

}
