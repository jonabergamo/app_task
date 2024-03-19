package com.example.a04_03_2024

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a04_03_2024.databinding.ActivityMainBinding
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val tasks = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        recyclerView = findViewById(R.id.recycler_view_tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Supondo que 'tasks' seja a sua lista de tasks
        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        binding.fab.setOnClickListener { view ->
            showTitlePopup(view.context) { titulo ->
                showDescriptionPopup(view.context) { descricao ->
                    showStatusPopup(view.context) { status ->
                        val task = Task(titulo, descricao, status)
                        tasks.add(task)
                        taskAdapter.notifyDataSetChanged() // Notifica o adapter sobre a mudança nos dados

                        Snackbar.make(view, "Task adicionada. Titulo: ${task.title}, Descrição: ${task.description}, Status: ${task.status}", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
            }
        }
    }


    fun showTitlePopup(context: Context, onTitleEntered: (String) -> Unit) {
        val popupTitulo = AlertDialog.Builder(context)
        val textTitulo = EditText(context)

        popupTitulo.setTitle("Título")
        popupTitulo.setView(textTitulo)
        popupTitulo.setPositiveButton("Ok") { _, _ ->
            onTitleEntered(textTitulo.text.toString())
        }
        popupTitulo.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        popupTitulo.show()
    }

    fun showDescriptionPopup(context: Context, onDescriptionEntered: (String) -> Unit) {
        val popupDescricao = AlertDialog.Builder(context)
        val textDescricao = EditText(context)

        popupDescricao.setTitle("Descrição")
        popupDescricao.setView(textDescricao)
        popupDescricao.setPositiveButton("Ok") { _, _ ->
            onDescriptionEntered(textDescricao.text.toString())
        }
        popupDescricao.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        popupDescricao.show()
    }

    fun showStatusPopup(context: Context, onStatusSelected: (String) -> Unit) {
        val statusItems = arrayOf("Pendente", "Concluída")
        AlertDialog.Builder(context).apply {
            setTitle("Escolha o status")
            setItems(statusItems) { _, which ->
                // Passa o status escolhido para o callback
                onStatusSelected(statusItems[which])
            }
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}