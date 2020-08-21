package com.example.contentprovider

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.Contract
import java.lang.System.out
import java.security.Permission

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private var listOfContacts: MutableList<UserContact> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //requestingPermission
        requestPermission()

    }


    private fun requestPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            fetchContactDetails()

            //settingUp RecyclerView
            setupRecyclerView();
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),1)
        }
    }

    private fun setupRecyclerView(){
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ContactAdapter(listOfContacts)
    }

    //fetching contacts from users phone book
    private fun fetchContactDetails(){
        var name : String
        var phone : String
        var prefix : String
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                prefix = name.substring(0,1)

                listOfContacts.add(UserContact(prefix,name,phone))
            }
        }
        cursor?.close()
    }

    //Showing toast on request result Whether user permission granted or not
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                    permissionLayout.visibility = View.VISIBLE
                    btn_allowPermission.setOnClickListener {
                        requestPermission()
                    }
                } else {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                    permissionLayout.visibility = View.INVISIBLE
                    fetchContactDetails()
                    setupRecyclerView()
                }
            }
        }
    }
}