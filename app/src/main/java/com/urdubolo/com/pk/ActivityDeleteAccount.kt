package com.urdubolo.com.pk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityDeleteAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        val etEmail = findViewById<EditText>(R.id.et2)
        val etUsername = findViewById<EditText>(R.id.et1)
        val btnSendEmail = findViewById<Button>(R.id.btn)

        btnSendEmail.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()

            sendEmail(email, username)
        }
    }


    private fun sendEmail(email: String, username: String) {
        val recipient = "huma01122@gmail.com"
        val subject = "Account Deletion  Request: $username"
        val message = "Email: $email\nUsername: $username"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822" // Ensures only email apps handle this intent
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(intent, "Choose an email client"))

        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "No email client found on your device", Toast.LENGTH_SHORT).show()
        }
    }
}
