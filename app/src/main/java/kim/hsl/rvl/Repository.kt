package kim.hsl.rvl

import android.content.Context
import androidx.lifecycle.LiveData

class Repository {
    lateinit var dao: StudentDao

    constructor(context: Context) {
        var database = StudentDatabase.inst(context)
        this.dao = database.studentDao()
    }

    fun insert(student: Student) {
        this.dao.insert(student)
    }

    fun query(): LiveData<List<Student>> {
        return this.dao.query()
    }

    fun update(student: Student) {
        this.dao.update(student)
    }

    fun delete(id: Int) {
        var student = Student(id)
        this.dao.delete(student)
    }
}