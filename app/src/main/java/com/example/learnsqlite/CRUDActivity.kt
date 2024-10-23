package com.example.learnsqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CRUDActivity : AppCompatActivity() {

    private var myDB: SQLiteDatabase? = null
    private val tableName = "Mahasiswa"
    private var data = ""
    private lateinit var dtMhs: TextView
    private lateinit var saveButton: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var editTextNIM: EditText
    private lateinit var editTextNama: EditText
    private lateinit var editTextAlamat: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crudactivity)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }2021

        dtMhs = findViewById(R.id.TextDataMahasiswa)
        editTextNIM = findViewById(R.id.editTextNIM)
        editTextNama = findViewById(R.id.editTextNama)
        editTextAlamat = findViewById(R.id.editTextAlamat)
        saveButton = findViewById(R.id.buttonSimpan)
        editButton = findViewById(R.id.buttonEdit)
        deleteButton = findViewById(R.id.buttonHapus)

        createDB()
        showData()

        saveButton.setOnClickListener {
            saveData()
        }
        editButton.setOnClickListener {
            editData()
        }
        deleteButton.setOnClickListener {
            deleteData()
        }
    }

    private fun createDB() {
        try {
            myDB = this.openOrCreateDatabase("DBMHS", MODE_PRIVATE, null)
            myDB!!.execSQL(
                "CREATE TABLE IF NOT EXISTS $tableName " +
                        "(NIM VARCHAR PRIMARY KEY, NAMA VARCHAR, ALAMAT VARCHAR);"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showData() {
        var cursor: Cursor? = null
        try {
            data = ""
            clearFields()
            cursor = myDB?.rawQuery("SELECT * FROM $tableName", null)

            cursor?.let {
                if (it.moveToFirst()) { // Check if cursor has data
                    val col1 = it.getColumnIndex("NIM")
                    val col2 = it.getColumnIndex("NAMA")
                    val col3 = it.getColumnIndex("ALAMAT")

                    do {
                        val nimMhs = it.getString(col1)
                        val nmMhs = it.getString(col2)
                        val almtMhs = it.getString(col3)
                        data += "$nimMhs | $nmMhs | $almtMhs\n"
                    } while (it.moveToNext())
                }
                dtMhs.text = data
            } ?: run {
                dtMhs.text = "No data available"
            }
        } catch (e: Exception) {
            dtMhs.text = "Error: ${e.message}"
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    private fun saveData() {
        try {
            myDB!!.execSQL(
                "Insert into $tableName Values ('${editTextNIM.text}'," +
                        "'${editTextNama.text}', '${editTextAlamat.text}');"
            )
            showData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Edit existing data in the database
    private fun editData() {
        try {
            myDB!!.execSQL(
                "Update $tableName Set NAMA = '${editTextNama.text}', " +
                        "ALAMAT = '${editTextAlamat.text}' Where NIM = '${editTextNIM.text}';"
            )
            showData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Delete data from the database
    private fun deleteData() {
        try {
            myDB!!.execSQL("Delete From $tableName where NIM = '${editTextNIM.text}';")
            showData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearFields() {
        editTextNIM.text.clear()
        editTextNama.text.clear()
        editTextAlamat.text.clear()
    }
}