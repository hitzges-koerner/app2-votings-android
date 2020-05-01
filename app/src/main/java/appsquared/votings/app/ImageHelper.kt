package appsquared.votings.app

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.IOException


@Throws(IOException::class)
fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String): Bitmap {
    val ei = ExifInterface(image_absolute_path)
    val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)

        ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)

        ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)

        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, true, false)

        ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)

        else -> bitmap
    }
}

private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
    val matrix = Matrix()
    matrix.preScale(if (horizontal) (-1).toFloat() else 1F, if (vertical) (-1).toFloat() else 1F)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun scale(bitmap: Bitmap) : Bitmap {
    val ratio:Float = bitmap.width.toFloat() / bitmap.height.toFloat()

    val width = Resources.getSystem().displayMetrics.widthPixels
    val height = Math.round((width / ratio).toDouble())

    return Bitmap.createScaledBitmap(bitmap, width, height.toInt(), false)
}