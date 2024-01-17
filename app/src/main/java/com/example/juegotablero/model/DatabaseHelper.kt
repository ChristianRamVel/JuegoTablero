package com.example.juegotablero.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        // Nombre de la base de datos.
        private const val DATABASE_NAME = "tableroDatabase"

        // Versión de la base de datos, útil para manejar actualizaciones esquemáticas.
        private const val DATABASE_VERSION = 1

        // Nombre de las tablas
        private const val TABLE_PARTIDAS = "partidas"
        private const val TABLE_JUGADORES = "jugadores"

        // Nombres de las columnas de la tabla partidas.
        private const val KEY_ID = "id"
        private const val KEY_JUGADOR1ID = "jugador1_id"
        private const val KEY_JUGADOR2ID = "jugador2_id"
        private const val KEY_TURNO = "turno"
        private const val KEY_FECHA = "fecha"

        // Nombres de las columnas de la tabla jugadores.
        private const val KEY_JUGADOR_ID = "id"
        private const val KEY_JUGADOR_NOMBRE = "nombre"
        private const val KEY_JUGADOR_POSICION = "posicion"
        private const val KEY_JUGADOR_PAREJAS = "parejas"
        private const val KEY_JUGADOR_TEST = "test"
        private const val KEY_JUGADOR_ADIVINAPALABRA = "adivinapalabra"
        private const val KEY_JUGADOR_REPASO = "repaso"
        private const val KEY_JUGADOR_FINAL = "final"
    }

    // Método llamado cuando la base de datos se crea por primera vez
    override fun onCreate(db: SQLiteDatabase) {
        // Sentencia SQL para crear la tabla de partida
        val createPartidaTable = ("CREATE TABLE " + TABLE_PARTIDAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_JUGADOR1ID + " INTEGER,"
                + KEY_JUGADOR2ID + " INTEGER," + KEY_TURNO + " INTEGER," + KEY_FECHA + " TEXT" + ")")

        // Sentencia SQL para crear la tabla de jugadores
        val createJugadorTable = ("CREATE TABLE " + TABLE_JUGADORES + "("
                + KEY_JUGADOR_ID + " INTEGER PRIMARY KEY," + KEY_JUGADOR_NOMBRE + " TEXT,"
                + KEY_JUGADOR_POSICION + " INTEGER," + KEY_JUGADOR_PAREJAS
                + " INTEGER," + KEY_JUGADOR_TEST + " INTEGER," + KEY_JUGADOR_ADIVINAPALABRA
                + " INTEGER," + KEY_JUGADOR_REPASO + " INTEGER," + KEY_JUGADOR_FINAL + " INTEGER" + ")")

        // Ejecuta las sentencias SQL para crear las tablas
        db.execSQL(createPartidaTable)
        db.execSQL(createJugadorTable)
    }

    // Método llamado cuando se necesita actualizar la base de datos, por ejemplo, cuando se incrementa DATABASE_VERSION.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla existente y crea una nueva.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARTIDAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JUGADORES")
        onCreate(db)
    }

    fun obtenerPartidas() : ArrayList<Partida> {
        val partidasList = ArrayList<Partida>()
        val selectQuery = "SELECT * FROM $TABLE_PARTIDAS"

        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Ejecuta la consulta SQL.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            // Maneja la excepción en caso de error al ejecutar la consulta.
            db.execSQL(selectQuery)
            return ArrayList()
        }

        // Variables para almacenar los valores de las columnas.
        var id: Int
        var jugador1_id: Int
        var jugador2_id: Int
        var turno: Int
        var fecha: String

        // Itera a través del cursor para leer los datos de la base de datos.
        if (cursor.moveToFirst()) {
            do {
                // Obtiene los índices de las columnas.
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val jugador1_idIndex = cursor.getColumnIndex(KEY_JUGADOR1ID)
                val jugador2_idIndex = cursor.getColumnIndex(KEY_JUGADOR2ID)
                val turnoIndex = cursor.getColumnIndex(KEY_TURNO)
                val fechaIndex = cursor.getColumnIndex(KEY_FECHA)

                if (idIndex != -1 && jugador1_idIndex != -1 && jugador2_idIndex != -1 && turnoIndex != -1 && fechaIndex != -1) {
                    // Lee los valores y los añade a la lista de partidas.
                    id = cursor.getInt(idIndex)
                    jugador1_id = cursor.getInt(jugador1_idIndex)
                    jugador2_id = cursor.getInt(jugador2_idIndex)
                    turno = cursor.getInt(turnoIndex)
                    fecha = cursor.getString(fechaIndex)
                    partidasList.add(Partida(id, jugador1_id, jugador2_id, turno, fecha))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return partidasList
    }


    fun crearJugadores(jugador1: Jugador, jugador2: Jugador): Pair<Long, Long> {
        val db = this.writableDatabase
        val contentValuesJugador1 = ContentValues()
        contentValuesJugador1.put(KEY_JUGADOR_NOMBRE, jugador1.nombre)
        contentValuesJugador1.put(KEY_JUGADOR_POSICION, jugador1.posicion)
        contentValuesJugador1.put(KEY_JUGADOR_PAREJAS, jugador1.PParejas)
        contentValuesJugador1.put(KEY_JUGADOR_TEST, jugador1.PTest)
        contentValuesJugador1.put(KEY_JUGADOR_ADIVINAPALABRA, jugador1.PAdivinaPalabra)
        contentValuesJugador1.put(KEY_JUGADOR_REPASO, jugador1.PRepaso)
        contentValuesJugador1.put(KEY_JUGADOR_FINAL, jugador1.PFinal)
        val jugador1Id = db.insert(TABLE_JUGADORES, null, contentValuesJugador1)

        val contentValuesJugador2 = ContentValues()
        contentValuesJugador2.put(KEY_JUGADOR_NOMBRE, jugador2.nombre)
        contentValuesJugador2.put(KEY_JUGADOR_POSICION, jugador2.posicion)
        contentValuesJugador2.put(KEY_JUGADOR_PAREJAS, jugador2.PParejas)
        contentValuesJugador2.put(KEY_JUGADOR_TEST, jugador2.PTest)
        contentValuesJugador2.put(KEY_JUGADOR_ADIVINAPALABRA, jugador2.PAdivinaPalabra)
        contentValuesJugador2.put(KEY_JUGADOR_REPASO, jugador2.PRepaso)
        contentValuesJugador2.put(KEY_JUGADOR_FINAL, jugador2.PFinal)
        val jugador2Id = db.insert(TABLE_JUGADORES, null, contentValuesJugador2)

        db.close()

        return Pair(jugador1Id, jugador2Id)
    }

    fun crearPartida(jugador1_id: Int, jugador2_id: Int, turno: Int, fecha: String) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_JUGADOR1ID, jugador1_id)
        contentValues.put(KEY_JUGADOR2ID, jugador2_id)
        contentValues.put(KEY_TURNO, turno)
        contentValues.put(KEY_FECHA, fecha)
        val success = db.insert(TABLE_PARTIDAS, null, contentValues)
        db.close()
        return success
    }


    fun guardarPartida(partida: Partida) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_JUGADOR1ID, partida.jugador1ID)
        contentValues.put(KEY_JUGADOR2ID, partida.jugador2ID)
        contentValues.put(KEY_TURNO, partida.turno)
        contentValues.put(KEY_FECHA, partida.fecha)
        db.update(TABLE_PARTIDAS, contentValues, "$KEY_ID = ?", arrayOf(partida.id.toString()))
        db.close()
    }

    fun guardarJugadores(jugador1: Jugador, jugador2: Jugador) {
        val db = this.writableDatabase
        val contentValuesJugador1 = ContentValues()
        contentValuesJugador1.put(KEY_JUGADOR_NOMBRE, jugador1.nombre)
        contentValuesJugador1.put(KEY_JUGADOR_POSICION, jugador1.posicion)
        contentValuesJugador1.put(KEY_JUGADOR_PAREJAS, jugador1.PParejas)
        contentValuesJugador1.put(KEY_JUGADOR_TEST, jugador1.PTest)
        contentValuesJugador1.put(KEY_JUGADOR_ADIVINAPALABRA, jugador1.PAdivinaPalabra)
        contentValuesJugador1.put(KEY_JUGADOR_REPASO, jugador1.PRepaso)
        contentValuesJugador1.put(KEY_JUGADOR_FINAL, jugador1.PFinal)
        db.update(TABLE_JUGADORES, contentValuesJugador1, "$KEY_JUGADOR_ID = ?", arrayOf(jugador1.id.toString()))

        val contentValuesJugador2 = ContentValues()
        contentValuesJugador2.put(KEY_JUGADOR_NOMBRE, jugador2.nombre)
        contentValuesJugador2.put(KEY_JUGADOR_POSICION, jugador2.posicion)
        contentValuesJugador2.put(KEY_JUGADOR_PAREJAS, jugador2.PParejas)
        contentValuesJugador2.put(KEY_JUGADOR_TEST, jugador2.PTest)
        contentValuesJugador2.put(KEY_JUGADOR_ADIVINAPALABRA, jugador2.PAdivinaPalabra)
        contentValuesJugador2.put(KEY_JUGADOR_REPASO, jugador2.PRepaso)
        contentValuesJugador2.put(KEY_JUGADOR_FINAL, jugador2.PFinal)
        db.update(TABLE_JUGADORES, contentValuesJugador2, "$KEY_JUGADOR_ID = ?", arrayOf(jugador2.id.toString()))
        db.close()
    }


    fun cargarPartida(id: Int) : Partida {
        val selectQuery = "SELECT * FROM $TABLE_PARTIDAS WHERE $KEY_ID = $id"

        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Ejecuta la consulta SQL.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            // Maneja la excepción en caso de error al ejecutar la consulta.
            db.execSQL(selectQuery)
            return Partida(0, 0, 0, 0, "")
        }

        // Variables para almacenar los valores de las columnas.
        val id: Int
        val jugador1_id: Int
        val jugador2_id: Int
        val turno: Int
        val fecha: String

        // Itera a través del cursor para leer los datos de la base de datos.
        if (cursor.moveToFirst()) {
            // Obtiene los índices de las columnas.
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val jugador1_idIndex = cursor.getColumnIndex(KEY_JUGADOR1ID)
            val jugador2_idIndex = cursor.getColumnIndex(KEY_JUGADOR2ID)
            val turnoIndex = cursor.getColumnIndex(KEY_TURNO)
            val fechaIndex = cursor.getColumnIndex(KEY_FECHA)

            if (idIndex != -1 && jugador1_idIndex != -1 && jugador2_idIndex != -1 && turnoIndex != -1 && fechaIndex != -1) {
                // Lee los valores y los añade a la lista de partidas.
                id = cursor.getInt(idIndex)
                jugador1_id = cursor.getInt(jugador1_idIndex)
                jugador2_id = cursor.getInt(jugador2_idIndex)
                turno = cursor.getInt(turnoIndex)
                fecha = cursor.getString(fechaIndex)
                cursor.close()
                return Partida(id, jugador1_id, jugador2_id, turno, fecha)
            }
        }
        cursor.close()
        return Partida(0, 0, 0, 0, "")
    }

    fun cargarJugador(id: Int) : Jugador {
        val selectQuery = "SELECT * FROM $TABLE_JUGADORES WHERE $KEY_JUGADOR_ID = $id"

        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Ejecuta la consulta SQL.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            // Maneja la excepción en caso de error al ejecutar la consulta.
            db.execSQL(selectQuery)
            return Jugador(0, "", false, false, false, false, false, 0)
        }

        // Variables para almacenar los valores de las columnas.
        val id: Int
        val nombre: String
        val parejas: Boolean
        val test: Boolean
        val adivinapalabra: Boolean
        val repaso: Boolean
        val final: Boolean
        val posicion: Int

        // Itera a través del cursor para leer los datos de la base de datos.
        if (cursor.moveToFirst()) {
            // Obtiene los índices de las columnas.
            val idIndex = cursor.getColumnIndex(KEY_JUGADOR_ID)
            val nombreIndex = cursor.getColumnIndex(KEY_JUGADOR_NOMBRE)
            val parejasIndex = cursor.getColumnIndex(KEY_JUGADOR_PAREJAS)
            val testIndex = cursor.getColumnIndex(KEY_JUGADOR_TEST)
            val adivinapalabraIndex = cursor.getColumnIndex(KEY_JUGADOR_ADIVINAPALABRA)
            val repasoIndex = cursor.getColumnIndex(KEY_JUGADOR_REPASO)
            val finalIndex = cursor.getColumnIndex(KEY_JUGADOR_FINAL)
            val posicionIndex = cursor.getColumnIndex(KEY_JUGADOR_POSICION)

            if (idIndex != -1 && nombreIndex != -1 && parejasIndex != -1 && testIndex != -1 && adivinapalabraIndex != -1 && repasoIndex != -1 && finalIndex != -1 && posicionIndex != -1) {
                // Lee los valores y los añade a la lista de partidas.
                id = cursor.getInt(idIndex)
                nombre = cursor.getString(nombreIndex)
                parejas = cursor.getInt(parejasIndex) == 1
                test = cursor.getInt(testIndex) == 1
                adivinapalabra = cursor.getInt(adivinapalabraIndex) == 1
                repaso = cursor.getInt(repasoIndex) == 1
                final = cursor.getInt(finalIndex) == 1
                posicion = cursor.getInt(posicionIndex)
                cursor.close()
                return Jugador(id, nombre, parejas, test, adivinapalabra, repaso, final, posicion)
            }
        }

        cursor.close()
        return Jugador(0, "", false, false, false, false, false, 0)
        }

    fun obtenerIDUltimoJugador() : Int {
        val selectQuery = "SELECT * FROM $TABLE_JUGADORES ORDER BY $KEY_JUGADOR_ID DESC LIMIT 1"

        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Ejecuta la consulta SQL.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            // Maneja la excepción en caso de error al ejecutar la consulta.
            db.execSQL(selectQuery)
            return 0
        }

        // Variables para almacenar los valores de las columnas.
        val id: Int

        // Itera a través del cursor para leer los datos de la base de datos.
        if (cursor.moveToFirst()) {
            // Obtiene los índices de las columnas.
            val idIndex = cursor.getColumnIndex(KEY_JUGADOR_ID)

            if (idIndex != -1) {
                // Lee los valores y los añade a la lista de partidas.
                id = cursor.getInt(idIndex)
                cursor.close()
                return id
            }
        }

        cursor.close()
        return 0
    }


}


