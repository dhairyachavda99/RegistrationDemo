package com.example.registrationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.database.Cursor

class Register : AppCompatActivity() {

    lateinit var edtFnm: EditText
    lateinit var edtLnm: EditText
    lateinit var edtMno: EditText
    lateinit var rgGender: RadioGroup
    lateinit var btnRegister: Button
    lateinit var listView: ListView

    lateinit var dbHelper: DBHelper

    var list = ArrayList<String>()
    lateinit var adapter: ArrayAdapter<String>

    var selectedId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtFnm = findViewById(R.id.edt_fnm)
        edtLnm = findViewById(R.id.edt_lnm)
        edtMno = findViewById(R.id.edt_mno)
        rgGender = findViewById(R.id.rg_gender)
        btnRegister = findViewById(R.id.btn_register)
        listView = findViewById(R.id.list_items)

        dbHelper = DBHelper(this)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter

        displayUsers()

        // INSERT OR UPDATE
        btnRegister.setOnClickListener {

            val fname = edtFnm.text.toString()
            val lname = edtLnm.text.toString()
            val mobile = edtMno.text.toString()

            val genderId = rgGender.checkedRadioButtonId

            if (genderId == -1) {
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rb = findViewById<RadioButton>(genderId)
            val gender = rb.text.toString()

            if (selectedId == -1) {

                dbHelper.insertUser(fname, lname, mobile, gender)
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()

            } else {

                dbHelper.updateUser(selectedId, fname, lname, mobile, gender)
                Toast.makeText(this, "User Updated", Toast.LENGTH_SHORT).show()

                selectedId = -1
                btnRegister.text = "Register"
            }

            clearFields()
            displayUsers()
        }

        // EDIT (CLICK ITEM)
        listView.setOnItemClickListener { _, _, position, _ ->

            val cursor: Cursor = dbHelper.getUsers()

            if (cursor.moveToPosition(position)) {

                selectedId = cursor.getInt(0)

                edtFnm.setText(cursor.getString(1))
                edtLnm.setText(cursor.getString(2))
                edtMno.setText(cursor.getString(3))

                val gender = cursor.getString(4)

                if (gender == "Male")
                    rgGender.check(R.id.rb_Male)
                else
                    rgGender.check(R.id.rb_Female)

                btnRegister.text = "Update"

                Toast.makeText(this, "Edit Mode Enabled", Toast.LENGTH_SHORT).show()
            }
        }

        // DELETE (LONG PRESS)
        listView.setOnItemLongClickListener { _, _, position, _ ->

            val cursor: Cursor = dbHelper.getUsers()

            if (cursor.moveToPosition(position)) {

                val id = cursor.getInt(0)

                dbHelper.deleteUser(id)

                Toast.makeText(this, "User Deleted", Toast.LENGTH_SHORT).show()

                displayUsers()
            }

            true
        }
    }

    fun displayUsers() {

        list.clear()

        val cursor: Cursor = dbHelper.getUsers()

        if (cursor.moveToFirst()) {

            do {

                val data =
                    cursor.getString(1) + " " +
                            cursor.getString(2) + " | " +
                            cursor.getString(3) + " | " +
                            cursor.getString(4)

                list.add(data)

            } while (cursor.moveToNext())
        }

        adapter.notifyDataSetChanged()
    }

    fun clearFields() {

        edtFnm.setText("")
        edtLnm.setText("")
        edtMno.setText("")
        rgGender.clearCheck()
    }
}
