package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
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
                    3 -> UserProfileScreen(userName)
                }
            }
        }
    }
}
@Composable
fun UserProfileScreen(userName: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil do Usuário", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Nome: ${userName ?: "Usuário"}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Aqui você pode adicionar mais informações sobre o usuário, como email, foto, etc.
        // Exemplo:
        // Text("E-mail: ${userEmail}")

        Spacer(modifier = Modifier.height(24.dp))

        // Exemplo de ação (logout, editar perfil, etc.)
        Text(
            "Editar Perfil",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier
                .clickable {
                    // Ação para editar o perfil
                }
                .padding(16.dp)
        )
    }
}
@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF2196F3), Color(0xFF00BCD4)) // Gradiente de roxo
    )
    BottomNavigation(
        backgroundColor = Color.Transparent, // Tornar o fundo transparente para o gradiente
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.background(gradient) // Aplica o gradiente
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
            label = { Text("Dash") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            label = { Text("Settings") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "User") }, // Ícone para o perfil de usuário
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) },
            label = { Text("Perfil") }
        )
    }
}
@Composable
fun HomePageScreen(userName: String?) {
    val financeViewModel: FinanceViewModel = viewModel()

    // Itens para a Grid
    val gridItems = listOf(
        "Histórico de Transações",
        "Relatório de Despesas",
        "Definir Orçamento",
        "Estatísticas",
        "Configurações"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Saudação ao usuário
        Text(
            "Bem-vindo, ${userName ?: "Usuário"}!",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Informações financeiras
        Text(
            "Última Receita: R$ %.2f".format(financeViewModel.totalIncomes),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Última Despesa: R$ %.2f".format(financeViewModel.totalExpenses),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Grid de opções
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Definindo 2 colunas
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(gridItems.size) { index ->  // Corrigido para usar o índice da lista
                val item = gridItems[index] // Acessando o item pelo índice
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            when (item) {
                                "Histórico de Transações" -> {
                                    // Ação para exibir o histórico de transações
                                }
                                "Relatório de Despesas" -> {
                                    // Ação para exibir o relatório de despesas
                                }
                                "Definir Orçamento" -> {
                                    // Ação para definir o orçamento mensal
                                }
                                "Estatísticas" -> {
                                    // Ação para exibir as estatísticas financeiras
                                }
                                "Configurações" -> {
                                    // Ação para acessar configurações do app
                                }
                            }
                        },
                    elevation = 8.dp,  // Aumentando a elevação do card para um destaque maior
                    shape = MaterialTheme.shapes.medium // Bordas arredondadas
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(8.dp) // Adicionando padding extra no box
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
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
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val totalExpenses = financeViewModel.expenses.values.flatten().sum()
        val totalIncomes = financeViewModel.incomes.values.flatten().sum()
        // Barra de progresso para despesas
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colorScheme.errorContainer
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total de Despesas: R$ %.2f".format(totalExpenses))
                LinearProgressIndicator(
                    progress = { if (totalExpenses + totalIncomes != 0f) totalExpenses / (totalExpenses + totalIncomes) else 0f },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Barra de progresso para receitas
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total de Receitas: R$ %.2f".format(totalIncomes))
                LinearProgressIndicator(
                    progress = { if (totalExpenses + totalIncomes != 0f) totalIncomes / (totalExpenses + totalIncomes) else 0f },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Adicionar receita ou despesa
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isIncome = false; showDialog = true },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Adicionar Despesa")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { isIncome = true; showDialog = true },
                modifier = Modifier.weight(1f).height(48.dp),
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
                            label = { Text("Digite o valor") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(text = errorMessage, color = Color.Red)
                        }
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
                        amount = ""
                        isCategorySelected = false
                        showDialog = false
                        errorMessage = ""
                    } else {
                        errorMessage = "Por favor, insira um valor válido e selecione uma categoria."
                    }
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isCategorySelected = false
                    showDialog = false
                    errorMessage = ""
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
        // Seção para tema
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

        // Seção para email
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

        // Campo para editar email
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

        Spacer(modifier = Modifier.height(20.dp))

        // Botão de logout
        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair", color = Color.White)
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