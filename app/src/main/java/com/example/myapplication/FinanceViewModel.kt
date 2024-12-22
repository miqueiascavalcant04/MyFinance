import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.FinanceDatabaseHelper

class FinanceViewModel(private val dbHelper: FinanceDatabaseHelper) : ViewModel() {
    var userName by mutableStateOf("")
    var totalRevenue by mutableStateOf(0f)
    var totalExpenses by mutableStateOf(0f)

    fun loadUserData(email: String) {
        userName = dbHelper.getUserNameByEmail(email) ?: "Usuário não encontrado"
        val userId = dbHelper.getUserNameByEmail(email) // Método para obter ID do usuário pelo e-mail
        if (userId != null) {
            totalExpenses = dbHelper.getTotalExpenses(userId)
            totalRevenue = dbHelper.getTotalIncomes(userId)
        }
    }
}
