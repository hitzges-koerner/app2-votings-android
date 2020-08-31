package appsquared.votings.app

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.preference.PreferenceManager
import com.yalantis.ucrop.UCrop
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_camera.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class CameraActivity : AppCompatActivity() {

    var disposable: Disposable? = null
    lateinit var user : Model.User

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var mType: Int = CAMERA
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 2
    val REQUEST_PICK_PHOTO = 3
    val RC_CROP_IMAGE = 101
    val TAG = "CAMERA"

    override fun onCreate(savedInstanceState: Bundle?) {

        mType = intent.getIntExtra("type", CAMERA)
        launchMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    private fun launchMode() {
        when(mType) {
            CAMERA -> {
                dispatchTakePictureIntent()
            }
            PICKER -> {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                try {
                    i.putExtra("return-data", true)
                    startActivityForResult(
                        Intent.createChooser(i, "Select Picture"), REQUEST_PICK_PHOTO)
                } catch (ex: ActivityNotFoundException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private val SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var uri: Uri

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        } else if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val file = File(mCurrentPhotoPath)
            if (Build.VERSION.SDK_INT >= 24)
                uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
            else
                uri = Uri.fromFile(file)

            var destinationFileName: String = SAMPLE_CROPPED_IMAGE_NAME
            destinationFileName += ".png";

            val destinationUri = Uri.fromFile(File(cacheDir, destinationFileName))

            UCrop.of(uri, destinationUri)
                .withAspectRatio(1F, 1F)
                .withMaxResultSize(400, 400)
                .start(this)

        } else if (requestCode == REQUEST_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            try {
                uri = data!!.data!!

                var destinationFileName: String = SAMPLE_CROPPED_IMAGE_NAME
                destinationFileName += ".png";

                val destinationUri = Uri.fromFile(File(cacheDir, destinationFileName))

                UCrop.of(uri, destinationUri)
                    .withAspectRatio(1F, 1F)
                    .withMaxResultSize(400, 400)
                    .start(this)

                //cropImageView.crop(data.data)

                /*
                cropImageView.crop(mImageCaptureUri)
                    .execute(object : CropCallback {
                        override fun onSuccess(cropped: Bitmap?) {
                            cropImageView.save(cropped)
                                .execute(saveUri, mSaveCallback)
                        }

                        override fun onError(e: Throwable?) {}
                    })
                    */

            } catch (e: Exception) {
                e.printStackTrace()
                finish()
            }

        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            if(data != null) {
                val resultUri = UCrop.getOutput(data)
                imageView.setImageURI(resultUri)

                sendAvatar(resultUri!!.toFile())
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!);
        } else finish()
    }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(
                contentResolver,
                selectedPhotoUri)
        }
    }

    private lateinit var photoFile: File

    fun dispatchTakePictureIntent() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                val photoURI = FileProvider.getUriForFile(this,
                    getString(R.string.authority),
                    photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 11)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(grantResults.contains(PackageManager.PERMISSION_DENIED)) {
            toastLong(getString(R.string.permission_camera_error))
            finish()
        } else {
            dispatchTakePictureIntent()
        }
    }

    private var mCurrentPhotoPath: String? = null

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("HHmmssSSS").format(Date())
        val timeStamp2 = SimpleDateFormat("yyyyMMdd").format(Date())
        val timeStampFinal = timeStamp2 + "T" + timeStamp

        val imageFileName = "JPEG_" + timeStampFinal + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private lateinit var mFileUpload: File

    private fun compressBitmapToFile(bm: Bitmap, thumb: Boolean): Boolean {

        var fOut: OutputStream? = null

        val timeStamp = SimpleDateFormat("HHmmssSSS").format(Date())
        val timeStamp2 = SimpleDateFormat("yyyyMMdd").format(Date())
        val timeStampFinal = timeStamp2 + "T" + timeStamp
        var fileName = ""

        /*
        if(thumb) fileName = timeStampFinal + "_THUMB.jpg"
        else fileName = timeStampFinal + "_full.jpg"
        */
        fileName = timeStampFinal + ".jpg"
        val photoUrl = File(filesDir, "")
        if (!photoUrl.exists()) photoUrl.mkdirs()
        mFileUpload = File(photoUrl, fileName)
        fOut = FileOutputStream(mFileUpload)

        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 60, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) { }
        return true
    }


    fun sendAvatar(imageFile: File) {

        val requestBody = imageFile.asRequestBody("image/png".toMediaTypeOrNull())

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.sendAvatar("Bearer $token",
            workspace!!,
            requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    pref.edit().putString(PreferenceNames.USER_AVATAR_URL, result).apply()

                    val returnIntent = Intent()
                    returnIntent.putExtra("imageUrl", result)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()

                }, { error ->
                    toast(getString(R.string.error_sending_photo))
                }
            )
    }



    fun generateThumbnail(bitmap: Bitmap) : File {
        val bitmapThumb : Bitmap = bitmap

        val directory = this.getDir("avatar", Context.MODE_PRIVATE)
        if(!directory.exists() && !directory.mkdirs()){
            Log.e("ImageSaver","Error creating directory " + directory);
        }

        val f = File(directory, "avatar")
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream();
        bitmapThumb.compress(Bitmap.CompressFormat.JPEG, 80 , bos)
        val bitmapData = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return f
    }

    companion object {
        val CAMERA = 0
        val PICKER = 1
    }

}