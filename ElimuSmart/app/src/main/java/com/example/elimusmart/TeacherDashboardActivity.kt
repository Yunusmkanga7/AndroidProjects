package com.example.elimusmart

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.elimusmart.model.Task
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class TeacherDashboardActivity : AppCompatActivity() {

    private lateinit var taskList: LinearLayout
    private lateinit var addButton: Button
    private lateinit var refreshButton: Button

    interface TaskApi {
        @GET("/api/tasks")
        fun getAllTasks(): Call<List<Task>>

        @POST("/api/tasks")
        fun addTask(@Body task: Task): Call<Task>

        @PUT("/api/tasks/{id}")
        fun updateTask(@Path("id") id: Long, @Body task: Task): Call<Void>

        @DELETE("/api/tasks/{id}")
        fun deleteTask(@Path("id") id: Long): Call<Void>
    }

    private val api: TaskApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)

        taskList = findViewById(R.id.task_list)
        addButton = findViewById(R.id.add_task_button)
        refreshButton = findViewById(R.id.refresh_button)

        addButton.setOnClickListener { showAddDialog() }
        refreshButton.setOnClickListener { loadTasks() }

        loadTasks()
    }

    private fun loadTasks() {
        taskList.removeAllViews()

        api.getAllTasks().enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach { task ->
                        val view = layoutInflater.inflate(R.layout.task_item, null)

                        view.findViewById<TextView>(R.id.task_title).text = task.title
                        view.findViewById<TextView>(R.id.task_description).text = task.description

                        view.findViewById<Button>(R.id.edit_button).setOnClickListener {
                            showEditDialog(task)
                        }

                        view.findViewById<Button>(R.id.delete_button).setOnClickListener {
                            api.deleteTask(task.id!!).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(this@TeacherDashboardActivity, "Deleted", Toast.LENGTH_SHORT).show()
                                    loadTasks()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@TeacherDashboardActivity, "Error", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }

                        taskList.addView(view)
                    }
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Toast.makeText(this@TeacherDashboardActivity, "Failed to load tasks", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAddDialog() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val titleInput = EditText(this)
        titleInput.hint = "Title"
        val descInput = EditText(this)
        descInput.hint = "Description"

        layout.addView(titleInput)
        layout.addView(descInput)

        AlertDialog.Builder(this)
            .setTitle("Ongeza Kazi")
            .setView(layout)
            .setPositiveButton("Tayari Umetuma") { _, _ ->
                val newTask = Task(null, titleInput.text.toString(), descInput.text.toString())
                api.addTask(newTask).enqueue(object : Callback<Task> {
                    override fun onResponse(call: Call<Task>, response: Response<Task>) {
                        loadTasks()
                    }

                    override fun onFailure(call: Call<Task>, t: Throwable) {
                        Toast.makeText(this@TeacherDashboardActivity, "Error adding task", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(task: Task) {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val titleInput = EditText(this)
        titleInput.setText(task.title)
        val descInput = EditText(this)
        descInput.setText(task.description)

        layout.addView(titleInput)
        layout.addView(descInput)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(layout)
            .setPositiveButton("Update") { _, _ ->
                val updatedTask = Task(task.id, titleInput.text.toString(), descInput.text.toString())
                api.updateTask(task.id!!, updatedTask).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        loadTasks()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@TeacherDashboardActivity, "Update Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
