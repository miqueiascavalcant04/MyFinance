import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// ViewModel que gerencia as finanças
class FinanceViewModel : ViewModel() {
    // Dados de receitas, despesas e saldo
    var totalExpenses = mutableStateOf(0f)
    var totalIncomes = mutableStateOf(0f)

    // Funções para adicionar receitas e despesas
    fun addExpense(amount: Float) {
        totalExpenses.value += amount
    }

    fun addIncome(amount: Float) {
        totalIncomes.value += amount
    }

    // Função para calcular o saldo
    val balance: Float
        get() = totalIncomes.value - totalExpenses.value
}
