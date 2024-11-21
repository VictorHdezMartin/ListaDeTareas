package com.example.listadetareas.data.providers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.listadetareas.data.class_Tarea
import com.example.listadetareas.utils.DataBase_Manager

class Tareas_DAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun openDB(){
        db = DataBase_Manager(context).writableDatabase
    }

    private fun closeDB(){
        db.close()
    }

    fun insert(tarea: class_Tarea){

        val values = ContentValues().apply {
            put(class_Tarea.COLUMN_NAME, tarea.name)
            put(class_Tarea.COLUMN_DONE, tarea.done)
        }

        try {
           openDB()
           val id =  db.insert(class_Tarea.TABLE_NAME, null, values)

        } catch (e: Exception){
            Log.e("DB", e.stackTraceToString())

        } finally{
            closeDB()
        }
    }

    fun update(tarea: class_Tarea){

        val values = ContentValues().apply {
            put(class_Tarea.COLUMN_NAME, tarea.name)
            put(class_Tarea.COLUMN_DONE, tarea.done)
        }

        try {
            openDB()
            val updateRows =  db.update(class_Tarea.TABLE_NAME, values, "${class_Tarea.COLUMN_ID} = ${tarea.id}", null)
        } catch (e: Exception){
            Log.e("DB", e.stackTraceToString())
        } finally{
            closeDB()
        }
    }

    fun delete(tarea: class_Tarea){
        try {
            openDB()
            val deletedRows = db.delete(class_Tarea.TABLE_NAME, "${class_Tarea.COLUMN_ID} = ${tarea.id}", null)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            closeDB()
        }
    }

    fun findById(id: Long): class_Tarea? {
        val selectColumnas = arrayOf(class_Tarea.COLUMN_ID, class_Tarea.COLUMN_NAME, class_Tarea.COLUMN_DONE)

        try {
            openDB()
            val cursor = db.query(
                class_Tarea.TABLE_NAME,                     // The table to query
                selectColumnas,                             // The array of columns to return (pass null to get all)
                "${class_Tarea.COLUMN_ID} = $id",  // The columns for the WHERE clause
                null,                          //  The values for the WHERE clause
                null,                              //  don't group the rows
                null,                               //  don't filter by row groups
                null                               //  The sort order
            )

            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(class_Tarea.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(class_Tarea.COLUMN_NAME))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(class_Tarea.COLUMN_DONE)) != 0

                return class_Tarea(id, name, done)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            closeDB()
        }
        return null
    }

    fun findAll(): List<class_Tarea> {

        var list: MutableList<class_Tarea> = mutableListOf()
        val selectColumnas = arrayOf(class_Tarea.COLUMN_ID, class_Tarea.COLUMN_NAME, class_Tarea.COLUMN_DONE)

        try {
            openDB()
            val cursor = db.query(
                class_Tarea.TABLE_NAME,                    // The table to query
                selectColumnas,                            // The array of columns to return (pass null to get all)
                null,                             // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                              // don't group the rows
                null,                               // don't filter by row groups
                null                               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(class_Tarea.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(class_Tarea.COLUMN_NAME))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(class_Tarea.COLUMN_DONE)) != 0

                val task = class_Tarea(id, name, done)
                list.add(task)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            closeDB()
        }
        return list
    }
}