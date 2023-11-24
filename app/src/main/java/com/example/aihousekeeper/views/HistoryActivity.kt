import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.aihousekeeper.R

class HistoryActivity : AppCompatActivity() {

    private lateinit var chatHistoryListView: ListView
    private lateinit var chatHistoryList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        chatHistoryListView = findViewById(R.id.chatHistoryListView)

        // Example chat history data
        chatHistoryList = ArrayList()


        // Create and set the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, chatHistoryList)
        chatHistoryListView.adapter = adapter
    }
}