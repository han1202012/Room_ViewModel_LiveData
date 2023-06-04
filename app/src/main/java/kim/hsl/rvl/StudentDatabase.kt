package kim.hsl.rvl

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Student::class], version = 2, exportSchema = false)
abstract class StudentDatabase: RoomDatabase() {
    /**
     * 获取 数据库访问 对象
     * 这是必须要实现的函数
     */
    abstract fun studentDao(): StudentDao

    companion object {
        lateinit var instance: StudentDatabase

        /**
         * 数据库版本 1 升级到 版本 2 的迁移类实例对象
         */
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.i("Room_StudentDatabase", "数据库版本 1 升级到 版本 2")
                database.execSQL("alter table student add column sex integer not null default 1")
            }
        }

        /**
         * 数据库版本 2 升级到 版本 3 的迁移类实例对象
         */
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.i("Room_StudentDatabase", "数据库版本 2 升级到 版本 3")
                database.execSQL("alter table student add column address integer not null default 1")
            }
        }

        fun inst(context: Context): StudentDatabase {
            if (!::instance.isInitialized) {
                synchronized(StudentDatabase::class) {
                    // 创建数据库
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StudentDatabase::class.java,
                        "student_database.db")
                        .addMigrations(MIGRATION_1_2)
                        .allowMainThreadQueries() // Room 原则上不允许在主线程操作数据库
                                                  // 如果要在主线程操作数据库需要调用该函数
                        .build()
                }
            }
            return instance;
        }
    }
}