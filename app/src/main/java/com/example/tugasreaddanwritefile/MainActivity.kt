package com.example.tugasreaddanwritefile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSIONS = 1001
    private lateinit var listView: ListView
    private lateinit var fileListAdapter: FileListAdapter
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createButton: Button = findViewById(R.id.createButton)
        editText = findViewById(R.id.editText)
        listView = findViewById(R.id.fileRecyclerView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }

        createButton.setOnClickListener {
            val fileName = editText.text.toString()
            createFile(fileName)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedFile = fileListAdapter.getItem(position)
            val intent = Intent(this, FileDetailActivity::class.java)
            intent.putExtra("fileName", selectedFile?.name)
            startActivity(intent)
        }

        updateFileList()
    }

    override fun onResume() {
        super.onResume()
        updateFileList()
    }

    private fun checkPermissions(): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun createFile(fileName: String) {
        if (fileName.isBlank()){
            Toast.makeText(this, "Please enter a file name", Toast.LENGTH_SHORT).show()
            return
        }

        if (isExternalStorageWritable()) {
            val file = File(getExternalFilesDir(null), "$fileName.txt")
            FileOutputStream(file).use {
                it.write("File created: ${System.currentTimeMillis()}\n".toByteArray())
            }
            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show()
            editText.text.clear()  // Clear the EditText after creating the file
            updateFileList()
        } else {
            Toast.makeText(this, "External storage not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFileList() {
        val files = getExternalFilesDir(null)?.listFiles()
        fileListAdapter = FileListAdapter(this, files?.toList() ?: listOf())
        listView.adapter = fileListAdapter
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY
    }
}
