package com.example.listadetareas.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.listadetareas.data.class_Tarea

class DataBase_Manager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1                        // Si cambias el esquema de la BBDD, debes incrementar la version de la BBDD
        const val DATABASE_NAME = "lista_de_tareas.db"

        private const val SQL_CREATE_TABLE =
            "create table ${class_Tarea.Companion.TABLE_NAME} (" +
                         "${class_Tarea.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "${class_Tarea.COLUMN_NAME} TEXT," +
                         "${class_Tarea.COLUMN_DONE} INTEGER)"

        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${class_Tarea.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_TABLE)
    }
}