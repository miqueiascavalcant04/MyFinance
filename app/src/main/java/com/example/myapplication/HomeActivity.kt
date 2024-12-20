package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableStateOf(0) }

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
                    0 -> HomePageScreen()
                    1 -> DashboardScreen()
                    2 -> SettingsScreen()
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
fun HomePageScreen() {
    val expenseCategories = listOf("Lazer", "Família", "Saúde", "Educação")
    val incomeCategories = listOf("Salário", "Freelance", "Investimentos", "Outro")

    val expenses = remember { mutableStateMapOf<String, MutableList<Float>>() }
    val incomes = remember { mutableStateMapOf<String, MutableList<Float>>() }

    var selectedCategory by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) } // Flag para distinguir receitas de despesas
    var showDialog by remember { mutableStateOf(false) }
    var isCategorySelected by remember { mutableStateOf(false) } // Flag para indicar se a categoria foi selecionada

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à Home!", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Selecione uma categoria e insira o valor:")

        Spacer(modifier = Modifier.height(16.dp))

        // Exibição dos botões para despesas e receitas
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

        Spacer(modifier = Modifier.height(16.dp))
        Text("Despesas por Categoria:")
        expenseCategories.forEach { category ->
            val totalExpense = expenses[category]?.sum() ?: 0f
            Text("$category: R$ %.2f".format(totalExpense))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Receitas por Categoria:")
        incomeCategories.forEach { category ->
            val totalIncome = incomes[category]?.sum() ?: 0f
            Text("$category: R$ %.2f".format(totalIncome))
        }

        Spacer(modifier = Modifier.height(16.dp))
        val totalExpenses = expenses.values.flatten().sum()
        val totalIncomes = incomes.values.flatten().sum()
        Text("Total de Despesas: R$ %.2f".format(totalExpenses))
        Text("Total de Receitas: R$ %.2f".format(totalIncomes))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (isIncome) "Adicionar Receita" else "Adicionar Despesa") },
            text = {
                Column {
                    if (!isCategorySelected) {
                        if (isIncome) {
                            // Exibição de categorias de receitas
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
                            // Exibição de categorias de despesas
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
                        // Campo para inserir o valor
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
                            incomes.getOrPut(selectedCategory) { mutableListOf() }.add(amountValue)
                        } else {
                            expenses.getOrPut(selectedCategory) { mutableListOf() }.add(amountValue)
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
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bem-vindo ao Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Saldo: R$ 1000,00", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SettingsScreen() {
    Text("Configurações", style = MaterialTheme.typography.headlineMedium)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
