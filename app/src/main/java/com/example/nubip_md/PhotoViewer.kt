package com.example.nubip_md

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.github.chrisbanes.photoview.PhotoView
import java.net.URLConnection


class PhotoViewer : AppCompatActivity() {
    private var files: List<Uri> = arrayListOf()
    private var activeFileIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer)

        val imageView = findViewById<Button>(R.id.photo_viewer_open_folder)

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)

            try {
                startActivityForResult(Intent.createChooser(intent, "Select a where to save file"), 0)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.prev_image_button).setOnClickListener {
            this.setActiveFile(this.activeFileIndex - 1)
        }

        findViewById<Button>(R.id.next_image_button).setOnClickListener {
            this.setActiveFile(this.activeFileIndex + 1)
        }

        var direction = 1
        val delay: Long = 3000
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                setActiveFile(activeFileIndex + direction)
                handler.postDelayed(this, delay)
                if (activeFileIndex <= 0) { direction = -direction }
                if (activeFileIndex >= files.count() - 1) { direction = -direction }
            }
        }

        var isPlaying = false

        findViewById<Button>(R.id.play_image_button).setOnClickListener {
            if (isPlaying) {
                handler.removeCallbacks(runnable);
            } else {
                handler.postDelayed(runnable, delay);
            }
            isPlaying = !isPlaying
        }
    }

    private val activeFile: Uri
        get() = this.files[this.activeFileIndex]

    private fun setActiveFile(index: Int) {
        if (index < 0) { return }
        if (index > this.files.count() - 1) { return }

        this.activeFileIndex = index
        val photoView = findViewById<View>(R.id.photo_view) as PhotoView
        photoView.setImageURI(this.activeFile)
    }

    private fun isImageFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
                var uri = data?.data

                if (uri != null) {
                    val dir = DocumentFile.fromTreeUri(this, uri)?: return
                    files = dir.listFiles()
                        .map { it.uri }
                        .filter { isImageFile(it.path) }

                    if (files.count() > 0) {
                        setActiveFile(0)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}