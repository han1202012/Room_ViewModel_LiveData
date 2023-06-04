package kim.hsl.rvl

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.room.ColumnInfo
import kim.hsl.rvl.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding.root)

        // 获取 ViewModel 视图模型对象
        var viewModel: ViewModel = ViewModelProvider(
            this,
            AndroidViewModelFactory(application)).get(ViewModel::class.java)

        // 为 ViewModel 中获取的 LiveData 数据设置 Observer 监听
        viewModel.query().observe(this, object: Observer<List<Student>> {
            override fun onChanged(t: List<Student>?) {
                Log.i("Room_MainActivity", "Observer#onChanged 回调, List<Student>: " + t)
            }
        })

        thread(start = true) {

            Thread.sleep(500)

            // 插入数据
            var s1 = Student("Tom", 18)
            var s2 = Student("Jerry", 16)

            viewModel.insert(s1)
            Log.i("Room_MainActivity", "插入数据 S1 : " + s1)

            Thread.sleep(500)

            viewModel.insert(s2)
            Log.i("Room_MainActivity", "插入数据 S2 : " + s2)

            Thread.sleep(500)

            s2 = Student(2, "Jack", 60)
            viewModel.update(s2)
            Log.i("Room_MainActivity", "更新数据 S2 : " + s2)

            Thread.sleep(500)

            // 删除数据
            viewModel.delete(1)
            Log.i("Room_MainActivity", "删除数据 id = 1")

            Thread.sleep(500)

            var students = viewModel.repository.dao.query()
            Log.i("Room_MainActivity", "主动查询 : LiveData : " + students + " , 实际数据 : " + students?.value)

            var students2 = viewModel.repository.dao.queryList()
            Log.i("Room_MainActivity", "主动查询2 : " + students2)


        }
    }
}