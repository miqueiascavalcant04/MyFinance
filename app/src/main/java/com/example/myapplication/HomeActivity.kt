package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.* // Usando Material3
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
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
        // Aplicando o padding correto ao conteúdo da tela
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Usando o padding fornecido pelo Scaffold
        ) {
            when (selectedItem) {
                0 -> HomePageScreen()        // Tela Home
                1 -> DashboardScreen()       // Tela Dashboard
                2 -> SettingsScreen()        // Tela de Configurações
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary, // Usando Material3 para cores
        contentColor = MaterialTheme.colorScheme.onPrimary // Usando Material3 para cores
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
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à Home!", style = MaterialTheme.typography.titleLarge) // Alterando para Material3
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
        Text(
            "Bem-vindo ao Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Exemplo de exibição de dados, como saldo ou outra informação
        Text(
            "Saldo: R$ 1000,00",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para sair
        Button(onClick = {
            // Lógica para logout ou fechar o app
        }) {
            Text("Sair")
        }
    }
}

@Composable
fun SettingsScreen() {
    // Gerenciar estado para configurações
    var isDarkMode by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("João da Silva") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Configurações",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Modo escuro
        Text("Modo Escuro")
        Switch(
            checked = isDarkMode,
            onCheckedChange = { isDarkMode = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Nome de usuário
        Text("Nome de Usuário")
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Digite seu nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botão para salvar configurações
        Button(onClick = {
            // Ação para salvar configurações
        }) {
            Text("Salvar Configurações")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
