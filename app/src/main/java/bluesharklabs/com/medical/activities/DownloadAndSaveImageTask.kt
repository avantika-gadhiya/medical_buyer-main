package bluesharklabs.com.medical.activities

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import android.media.MediaScannerConnection
import androidx.core.content.FileProvider
import android.widget.Toast
import bluesharklabs.com.medical.BuildConfig
import java.util.*
import kotlin.collections.ArrayList


class DownloadAndSaveImageTask( var context: Context) : AsyncTask<String, Unit, Unit>() {
    private var mContext: WeakReference<Context> = WeakReference(context)

    override fun doInBackground(vararg params: String?) {
        val url = params[0]
        /* val requestOptions = RequestOptions().override(100)
             .downsample(DownsampleStrategy.CENTER_INSIDE)
             .skipMemoryCache(true)
             .diskCacheStrategy(DiskCacheStrategy.NONE)*/

        mContext.get()?.let {
            val bitmap = Glide.with(it)
                .asBitmap()
                .load(url)
                .submit()
                .get()

            try {
                val path =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/FBDownloader/"
                val dir = File(path)

                /* var file = File(it.filesDir, "Images")
                  if (!file.exists()) {
                      file.mkdir()
                  }
                 file = File(file, "img.jpg")*/
                val wrapper = ContextWrapper(context)

                // Initializing a new file
                // The bellow line return a directory in internal storage
                var file = wrapper.getDir("Pictures", MODE_PRIVATE)

                // Create a file to save the image
                file = File(file, "MEdical" + ".jpg")
                /*var file = it.getDir("Images", Context.MODE_PRIVATE)
                file = File(file, "img.jpg")*/


                /*        var gpath: String = Environment.getExternalStorageDirectory().absolutePath
                        var spath = "Download"
                        var fullpath = File(gpath + File.separator + spath)
                        Log.w("fullpath", "" + fullpath)
                        imageReaderNew(fullpath)*/
                val now = Date()
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
                val fileName = "$now.jpg"

                val folder =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "")
                folder.mkdirs()

                val imageFile = File(folder, fileName)
                imageFile.createNewFile()
                val outputStream = FileOutputStream(imageFile)
                val quality = 100

                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.flush()
                outputStream.close()

                Toast.makeText(context, "ScreenShot Captured", Toast.LENGTH_SHORT).show()
                var bmpUri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    imageFile
                )
                // Log.i("Seiggailion", "bmpUri==>"+bmpUri)
                MediaScannerConnection.scanFile(context,
                    arrayOf(imageFile.toString()), null,
                    MediaScannerConnection.OnScanCompletedListener { path, uri ->
                        Log.i("ExternalStorage", "Scanned $path:")
                        Log.i("ExternalStorage", "-> uri=$uri")
                    })
                /* val out = FileOutputStream(file)
                 Log.d("Seiggailion","BIMAP==>"+file.absolutePath)
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                 out.flush()
                 out.close()*/
                Log.i("Seiggailion", "Image saved.")
            } catch (e: Exception) {
                Log.i("Seiggailion", "Failed to save image.")
            }
        }
    }
    fun imageReaderNew(root: File) {
        val fileList: ArrayList<File> = ArrayList()
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.size > 0) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".jpeg")) {
                    // File absolute path
                    Log.e("downloadFilePath", currentFile.getAbsolutePath())
                    // File Name
                    Log.e("downloadFileName", currentFile.getName())
                    fileList.add(currentFile.absoluteFile)
                }
            }
            Log.w("fileList", "" + fileList.size)
        }
    }
}