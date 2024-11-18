package com.example.listadetareas.data.providers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.listadetareas.data.Task
import com.example.listadetareas.utils.DataBase_Manager

class TaskDAO(val context: Context) {

    private lateinit var db = SQLiteDatabase
    fun opendb(){
        db = DataBase_Manager(context).writableDatabase
    }
    fun closeDb(){
        db.close()
    }

    fun insert(task: Task){

        opendb()

        val values = ContentValues().apply {
               put(Task.COLUMN_NAME, task.name)
               put(Task.COLUMN_DONE, task.done)
        }

        try {

           val updateRows =  db.update(Task.TABLE_NAME, values, "${Task.COLUMN_ID} = ${task.id}", null)

        } catch (e: Exception){

            Log.e("DB", e.stackTraceToString())

        } finally{

            closeDb()

        }
    }

    fun update(task:Task){


    }

    fun delete(task:Task){



    }

    fun findById(id: Long): Task {



    }

    fun findAll(): List<Task>{



    }


}