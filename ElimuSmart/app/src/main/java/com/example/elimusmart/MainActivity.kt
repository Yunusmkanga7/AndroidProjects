package com.example.elimusmart

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.elimusmart.api.ApiClient
import com.example.elimusmart.api.ApiService
import com.example.elimusmart.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Fungua view elements kutoka layout
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)
        roleSpinner = findViewById(R.id.role_spinner)
        loginBtn = findViewById(R.id.login_btn)

        // ✅ Ongeza roles kwenye spinner
        val roles = listOf("Student", "Teacher")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        loginBtn.setOnClickListener {
            login()
        }
    }


    private fun login() {
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val role = roleSpinner.selectedItem?.toString()?.lowercase() ?: ""

        // Validation ya inputs
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tafadhali jaza username na password", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(username, password, role)
        val api = ApiClient.retrofit.create(ApiService::class.java)

        api.login(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null) {
                    val loggedInUser = response.body()!!

                    Toast.makeText(applicationContext, "Karibu ${loggedInUser.username}", Toast.LENGTH_SHORT).show()

                    // Fungua dashboard kulingana na role
                    val intent = if (role == "teacher") {
                        Intent(this@MainActivity, TeacherDashboardActivity::class.java)
                    } else {
                        Intent(this@MainActivity, StudentDashboardActivity::class.java)
                    }


                    // Ondoa login kwenye back stack
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Login imeshindwa. Hakikisha taarifa zako ni sahihi.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "Hitilafu ya mtandao: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}