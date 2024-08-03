package com.urdubolo.com.pk.Ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Model.ModelUserSignUpResponse
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.Util.UtilAnimation
import com.urdubolo.com.pk.databinding.ActivitySignUpBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ActivitySignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var apiInterFace: ApiInterFace

    private val REQUEST_IMAGE_PICK = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiInterFace = RetrofitClient.apiInterface

        binding.apply {
ivBackArrow.setOnClickListener()
{
    finish()
}
            btnSIgnIn.setOnClickListener()
            {
                startActivity(Intent(this@ActivitySignUp,ActivityLogin::class.java))
            }

            selectimage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_IMAGE_PICK)
            }

            btnSignUp.setOnClickListener {
                var utilAnimation=UtilAnimation(this@ActivitySignUp)
                utilAnimation.startLoadingAnimation()
                val usernameText = etname.text.toString()
                val emailText = etEmail.text.toString()
                val passwordText = etpassword.text.toString()

                if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || selectedImageUri == null) {
                    utilAnimation.endLoadingAnimation()
                    Toast.makeText(this@ActivitySignUp, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val username = RequestBody.create("text/plain".toMediaType(), usernameText)
                val email = RequestBody.create("text/plain".toMediaType(), emailText)
                val password = RequestBody.create("text/plain".toMediaType(), passwordText)

                val profileImageFile = uriToFile(selectedImageUri!!)
                val requestFile = RequestBody.create("image/jpeg".toMediaType(), profileImageFile)
                val profileImage = MultipartBody.Part.createFormData("profile_image", profileImageFile.name, requestFile)

                val call = apiInterFace.signUpUser(username, email, password, profileImage)

                call.enqueue(object : Callback<ModelUserSignUpResponse> {
                    override fun onResponse(call: Call<ModelUserSignUpResponse>, response: Response<ModelUserSignUpResponse>) {
                        utilAnimation.endLoadingAnimation()
                        if (response.isSuccessful) {


                            response.body()?.let {

                                   Toast.makeText(this@ActivitySignUp, "Account Created Successfull", Toast.LENGTH_SHORT)
                                       .show()
                                   startActivity(Intent(this@ActivitySignUp,ActivityLogin::class.java))
                                   finish()

                            }
                        } else {
                            Toast.makeText(this@ActivitySignUp, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ModelUserSignUpResponse>, t: Throwable) {
                        utilAnimation.endLoadingAnimation()
                        Toast.makeText(this@ActivitySignUp, "Failure", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            binding.img.setImageURI(data?.data)
            // Optionally, you can display the selected image using an ImageView
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(cacheDir, getFileName(uri))
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
        return file
    }

    private fun getFileName(uri: Uri): String {
        var name = ""
        val returnCursor = contentResolver.query(uri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }
}
