package com.example.tugasreaddanwritefile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.io.File

class FileListAdapter(private val context: Context, private val files: List<File>) : BaseAdapter() {

    override fun getCount(): Int {
        return files.size
    }

    override fun getItem(position: Int): File? {
        return files[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.text = files[position].name
        return view
    }
}
