package kim.hsl.rvl

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ViewModel: AndroidViewModel {

    lateinit var repository: Repository

    constructor(application: Application) : super(application) {
        this.repository = Repository(application)
    }

    fun insert(student: Student) {
        this.repository.insert(student)
    }

    fun query(): LiveData<List<Student>> {
        return this.repository.query()
    }

    fun update(student: Student) {
        this.repository.update(student)
    }

    fun delete(id: Int) {
        this.repository.delete(id)
    }
}