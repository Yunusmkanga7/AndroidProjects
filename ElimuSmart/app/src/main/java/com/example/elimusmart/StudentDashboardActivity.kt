package com.example.elimusmart

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.elimusmart.api.ApiClient
import com.example.elimusmart.api.ApiService
import com.example.elimusmart.model.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentDashboardActivity : AppCompatActivity() {

    private lateinit var taskList: LinearLayout
    private lateinit var refreshBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

//        taskList = findViewById(R.id.task_list)

        refreshBtn.setOnClickListener {
            fetchTasks()
        }

        fetchTasks()
    }

    private fun fetchTasks() {
        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.getAllTasks().enqueue(object : Callback<List<Task>> {
            override fun onResponse(
                call: Call<List<com.example.elimusmart.model.Task>>,
                response: Response<List<com.example.elimusmart.model.Task>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    displayTasks(response.body()!!)
                } else {
                    Toast.makeText(applicationContext, "Hakuna kazi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Toast.makeText(applicationContext, "Tatizo la mtandao", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayTasks(tasks: List<Task>) {
        taskList.removeAllViews()
        for (task in tasks) {
            val textView = TextView(this)
            textView.text = "• ${task.title} (${task.deadline})\n${task.description}\nStatus: ${task.done}\n"
            taskList.addView(textView)

            if (task.done != "shown") {
                val doneBtn = Button(this)
                doneBtn.text = "Mark as Done"
                doneBtn.setOnClickListener {
                    task.id?.let { it1 -> markAsDone(it1) }
                }
                taskList.addView(doneBtn)
            }

            val divider = View(this)
            divider.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2
            )
            divider.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
            taskList.addView(divider)
        }
    }

    private fun markAsDone(taskId: Long) {
        val api = ApiClient.retrofit.create(ApiService::class.java)
        api.markTaskAsDone(taskId).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(applicationContext, "✅ Kazi imekamilishwa", Toast.LENGTH_SHORT).show()
                fetchTasks()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext, "❌ Imeshindikana", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
