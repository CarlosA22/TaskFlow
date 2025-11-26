package com.taskflow.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Classe abstrata que define o banco de dados Room.
 * Serve como ponto de acesso principal para os dados persistidos.
 *
 * @param entities Lista de entidades (tabelas) que o banco de dados conterá.
 * @param version A versão do schema do banco de dados. Deve ser incrementada
 *                sempre que houver uma alteração no schema.
 * @param exportSchema Define se o schema do banco de dados deve ser exportado
 *                     para um arquivo JSON. Útil para testes e versionamento.
 */
@Database(
    entities = [Task::class, UserSession::class],
    version = 3,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Método abstrato que retorna o DAO para a entidade Task.
     * O Room implementa automaticamente este método.
     */
    abstract fun taskDao(): TaskDao

    /**
     * Método abstrato que retorna o DAO para a entidade UserSession.
     * O Room implementa automaticamente este método.
     */
    abstract fun userSessionDao(): UserSessionDao

    companion object {
        /**
         * A instância singleton do banco de dados.
         * O @Volatile garante que as escritas nesta propriedade sejam
         * imediatamente visíveis para outros threads.
         */
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        /**
         * Obtém a instância singleton do banco de dados, criando-a se necessário.
         * Esta implementação usa o padrão Singleton com double-checked locking
         * para garantir a segurança em ambientes multithread.
         *
         * @param context O contexto da aplicação.
         * @return A instância única do banco de dados.
         */
        fun getDatabase(context: Context): TaskDatabase {
            // Se a instância já existe, a retorna.
            return INSTANCE ?: synchronized(this) {
                // Dentro do bloco sincronizado, verifica novamente para evitar
                // a criação de múltiplas instâncias em threads diferentes.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database" // Nome do arquivo do banco de dados SQLite.
                )
                    // fallbackToDestructiveMigration destrói e recria o banco de dados
                    // quando a versão do schema é incrementada. Em um app de produção,
                    // seria necessário implementar uma estratégia de migração real (Migration).
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
