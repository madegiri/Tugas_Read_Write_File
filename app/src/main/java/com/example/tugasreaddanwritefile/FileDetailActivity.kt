package com.example.tugasreaddanwritefile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FileDetailActivity : AppCompatActivity() {

    private lateinit var fileName: String
    private lateinit var fileDetailTextView: TextView
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_detail)

        fileName = intent.getStringExtra("fileName") ?: ""
        fileDetailTextView = findViewById(R.id.fileDetailTextView)
        editText = findViewById(R.id.editText)

        val updateButton: Button = findViewById(R.id.updateButton)
        val deleteButton: Button = findViewById(R.id.deleteButton)

        displayFileDetails()

        updateButton.setOnClickListener {
            updateFile()
        }

        deleteButton.setOnClickListener {
            deleteFile()
        }
    }

    private fun displayFileDetails() {
        val file = File(getExternalFilesDir(null), fileName)
        if (file.exists()) {
            val content = FileInputStream(file).use {
                it.bufferedReader().use { reader ->
                    reader.readText()
                }
            }
            val lastModified = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(file.lastModified()))
            fileDetailTextView.text = "Name: ${file.name}\nLast Modified: $lastModified\n$content"
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFile() {
        val file = File(getExternalFilesDir(null), fileName)

        if (file.exists()) {
            val newFileName = editText.text.toString().trim()
            if (newFileName.isNotEmpty() && newFileName != fileName) {
                val newFile = File(file.parent, "$newFileName.txt")
                try {
                    val newContent = file.readText() + "\nUpdated: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}"
                    if (file.renameTo(newFile)) {
                        newFile.writeText(newContent)
                        fileName = newFileName
                        Toast.makeText(this, "File updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to rename file", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else if (newFileName.isEmpty()) {
                Toast.makeText(this, "Please enter a file name", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val newContent = file.readText() + "\nUpdated: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}"
                    file.writeText(newContent)
                    Toast.makeText(this, "File updated successfully", Toast.LENGTH_SHORT).show()
                    editText.isEnabled = false // Disable the EditText after updating
                    finish() // Go back to the previous activity after update
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun deleteFile() {
        val file = File(getExternalFilesDir(null), fileName)
        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(this, "File deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to delete file", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
        }
    }
}
