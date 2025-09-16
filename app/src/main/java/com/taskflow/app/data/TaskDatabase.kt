package com.taskflow.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Classe abstrata que define o banco de dados Room
 * Serve como ponto de acesso principal para os dados persistidos
 *
 * @param entities Lista de entidades (tabelas) no banco
 * @param version Versão do schema do banco (incrementar quando houver mudanças)
 * @param exportSchema Define se deve exportar schema para testes
 */
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Método abstrato que retorna o DAO
     * Room implementa automaticamente este método
     */
    abstract fun taskDao(): TaskDao

    companion object {
        /**
         * Instância singleton do banco de dados
         * @Volatile garante visibilidade entre threads
         */
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        /**
         * Obtém a instância do banco de dados
         * Implementa padrão Singleton com double-checked locking para thread-safety
         *
         * @param context Contexto da aplicação
         * @return Instância única do banco
         */
        fun getDatabase(context: Context): TaskDatabase {
            // Se já existe uma instância, retorna ela
            return INSTANCE ?: synchronized(this) {
                // Double-check dentro do bloco sincronizado
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database" // Nome do arquivo do banco
                )
                    // Permite queries na main thread apenas para desenvolvimento
                    // Em produção, sempre usar coroutines
                    .fallbackToDestructiveMigration() // Recria banco se versão mudou
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}