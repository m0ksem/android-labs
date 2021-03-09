package com.example.nubip_md

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.github.chrisbanes.photoview.PhotoView
import java.io.File


class PhotoViewer : AppCompatActivity() {
    private var files: List<File> = arrayListOf()
    private var activeFileIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer)

        findViewById<Button>(R.id.photo_viewer_open_folder).setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)

            try {
                startActivityForResult(Intent.createChooser(intent, "Select a where to save file"), 0)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openFolder() {
        val photoView = findViewById<View>(R.id.photo_view) as PhotoView
//        photoView.setImageResource(R.drawable.image)
    }

    private val activeFile: File
        get() = this.files[this.activeFileIndex]

    private fun setActiveFile(index: Int) {
        this.activeFileIndex = index
        val photoView = findViewById<View>(R.id.photo_view) as PhotoView
        photoView.setImageURI(this.activeFile.toUri())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
                var uri = data?.data

                if (uri != null) {
                    val dir = DocumentFile.fromTreeUri(this, uri)?: return
                    files = dir.listFiles().map {
                        File(it.uri.path)
                    }.filter {
                        it.endsWith("png")
                    }

                    if (files.count() > 0) {
                        setActiveFile(0)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}