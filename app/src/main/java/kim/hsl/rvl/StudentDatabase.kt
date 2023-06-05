package kim.hsl.rvl

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Student::class], version = 4, exportSchema = true)
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
                database.execSQL("alter table student add column degree integer not null default 1")
            }
        }

        /**
         * 数据库版本 3 升级到 版本 4 的迁移类实例对象
         * 销毁重建策略
         */
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.i("Room_StudentDatabase", "数据库版本 3 升级到 版本 4")
                // 创新临时数据库
                database.execSQL(
                    "CREATE TABLE temp_student (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "name TEXT," +
                            "age INTEGER NOT NULL," +
                            "sex TEXT NOT NULL DEFAULT 'M'," +
                            "degree INTEGER NOT NULL DEFAULT 1)"
                )

                // 拷贝数据
                database.execSQL(
                    "INSERT INTO temp_student (name, age, degree)" +
                            "SELECT name, age, degree FROM student"
                )

                // 删除原始表
                database.execSQL("DROP TABLE student")

                // 将临时表命令为原表表明
                database.execSQL("ALTER TABLE temp_student RENAME TO student")
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
                        .addMigrations(MIGRATION_2_3)
                        .addMigrations(MIGRATION_3_4)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries() // Room 原则上不允许在主线程操作数据库
                                                  // 如果要在主线程操作数据库需要调用该函数
                        .build()
                }
            }
            return instance;
        }
    }
}