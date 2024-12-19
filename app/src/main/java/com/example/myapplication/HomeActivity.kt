package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à Home!", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
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
        Text(
            "Saldo: R$ 1000,00",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Sair")
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val dataStore = context.dataStore
    var userName by remember { mutableStateOf("João da Silva") }
    var showSnackbar by remember { mutableStateOf(false) }

    val darkModeState = getDarkMode(dataStore)
    val isDarkMode by darkModeState.collectAsState(initial = false)

    val coroutineScope = rememberCoroutineScope()
    val updateDarkMode: (Boolean) -> Unit = { newValue ->
        coroutineScope.launch {
            saveDarkMode(dataStore, newValue)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Configurações",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("Modo Escuro")
        Switch(
            checked = isDarkMode,
            onCheckedChange = { newValue -> updateDarkMode(newValue) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("Nome de Usuário")
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Digite seu nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { showSnackbar = true }) {
            Text("Salvar Configurações")
        }
        if (showSnackbar) {
            Snackbar(
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("Fechar")
                    }
                }
            ) {
                Text("Configurações salvas com sucesso!")
            }
        }
    }
}

@Composable
fun getDarkMode(dataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>): Flow<Boolean> {
    val darkModeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey("dark_mode")] ?: false
    }
    return darkModeFlow
}

suspend fun saveDarkMode(dataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>, value: Boolean) {
    dataStore.edit { preferences ->
        preferences[booleanPreferencesKey("dark_mode")] = value
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
