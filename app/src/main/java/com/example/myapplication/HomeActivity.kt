package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HomeScreen(context = this)
            }
        }
    }
}

class FinanceViewModel : ViewModel() {
    var totalExpenses by mutableStateOf(1500f)
    var totalIncomes by mutableStateOf(3500f)
    var expenses = mutableStateMapOf<String, MutableList<Float>>()
    var incomes = mutableStateMapOf<String, MutableList<Float>>()

    fun addExpense(category: String, amount: Float) {
        expenses.getOrPut(category) { mutableListOf() }.add(amount)
        totalExpenses += amount
    }

    fun addIncome(category: String, amount: Float) {
        incomes.getOrPut(category) { mutableListOf() }.add(amount)
        totalIncomes += amount
    }
}

@Composable
fun HomeScreen(context: Context) {
    var selectedItem by remember { mutableStateOf(0) }
    val email = "usuario@example.com" // Substitua pelo e-mail real que você vai usar após login
    val userName by remember { mutableStateOf(getUserNameByEmail(context, email)) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedItem) { newSelection ->
                selectedItem = newSelection
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(targetState = selectedItem) { targetScreen ->
                when (targetScreen) {
                    0 -> HomePageScreen(userName)
                    1 -> DashboardScreen()
                    2 -> SettingsScreen(context)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            label = { Text("Home") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Dashboard, contentDescription = "Dashboard") },
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            label = { Text("Dashboard") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            label = { Text("Settings") }
        )
    }
}

@Composable
fun HomePageScreen(userName: String?) {
    val financeViewModel: FinanceViewModel = viewModel()

    val balance = financeViewModel.totalIncomes - financeViewModel.totalExpenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo, ${userName ?: "Usuário"}!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Resumo financeiro
        Text("Resumo das suas finanças", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Exibindo saldo total
        Text("Saldo Total: R$ %.2f".format(balance), style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Exibindo total de despesas
        Text("Total de Despesas: R$ %.2f".format(financeViewModel.totalExpenses))

        Spacer(modifier = Modifier.height(8.dp))

        // Exibindo total de receitas
        Text("Total de Receitas: R$ %.2f".format(financeViewModel.totalIncomes))

        Spacer(modifier = Modifier.height(24.dp))

        // Instruções ou ações rápidas, como visualizar o dashboard
        Button(
            onClick = { /* Ação para ir para o Dashboard */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Ver Dashboard")
        }
    }
}

@Composable
fun DashboardScreen() {
    val financeViewModel: FinanceViewModel = viewModel()

    val expenseCategories = listOf("Lazer", "Família", "Saúde", "Educação")
    val incomeCategories = listOf("Salário", "Freelance", "Investimentos", "Outro")

    var selectedCategory by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isCategorySelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        val totalExpenses = financeViewModel.expenses.values.flatten().sum()
        val totalIncomes = financeViewModel.incomes.values.flatten().sum()

        // Barra de progresso para despesas
        Text("Total de Despesas: R$ %.2f".format(totalExpenses))
        LinearProgressIndicator(
            progress = { totalExpenses / (totalExpenses + totalIncomes) },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de progresso para receitas
        Text("Total de Receitas: R$ %.2f".format(totalIncomes))
        LinearProgressIndicator(
            progress = { totalIncomes / (totalExpenses + totalIncomes) },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Adicionar receita ou despesa
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isIncome = false; showDialog = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Adicionar Despesa")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { isIncome = true; showDialog = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Adicionar Receita")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (isIncome) "Adicionar Receita" else "Adicionar Despesa") },
            text = {
                Column {
                    if (!isCategorySelected) {
                        if (isIncome) {
                            Text("Escolha uma categoria de receita:")
                            incomeCategories.forEach { category ->
                                Button(
                                    onClick = {
                                        selectedCategory = category
                                        isCategorySelected = true
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(category)
                                }
                            }
                        } else {
                            Text("Escolha uma categoria de despesa:")
                            expenseCategories.forEach { category ->
                                Button(
                                    onClick = {
                                        selectedCategory = category
                                        isCategorySelected = true
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(category)
                                }
                            }
                        }
                    } else {
                        TextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Digite o valor") }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val amountValue = amount.toFloatOrNull()
                    if (amountValue != null && amountValue > 0 && selectedCategory.isNotEmpty()) {
                        if (isIncome) {
                            financeViewModel.addIncome(selectedCategory, amountValue)
                        } else {
                            financeViewModel.addExpense(selectedCategory, amountValue)
                        }
                    }
                    amount = ""
                    isCategorySelected = false
                    showDialog = false
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isCategorySelected = false
                    showDialog = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun SettingsScreen(context: Context) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("usuario@example.com") } // Substitua pelo email do usuário logado
    var newEmail by remember { mutableStateOf("") }
    var isEditingEmail by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Configurações", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // Troca de tema
        Text("Tema:")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Claro")
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { isDarkTheme = it }
            )
            Text("Escuro")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // E-mail
        Text("E-mail:")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(email)
            IconButton(onClick = { isEditingEmail = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar E-mail")
            }
        }

        if (isEditingEmail) {
            OutlinedTextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("Novo E-mail") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    email = newEmail
                    isEditingEmail = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar E-mail")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen() {
    MaterialTheme {
        HomeScreen(context = LocalContext.current)
    }
}

fun getUserNameByEmail(context: Context, email: String): String {
    // Aqui você pode adicionar uma lógica para buscar o nome do usuário pelo e-mail
    return "João Silva" // Exemplo de nome retornado
}
