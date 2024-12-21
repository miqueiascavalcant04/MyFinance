package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class RegisterActivity : ComponentActivity() {
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = UserDatabaseHelper(this) // Inicializa o banco de dados
        setContent {
            MyApplicationTheme {
                RegisterScreen(onRegisterSuccess = {
                    Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                })
            }
        }
    }
}

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dbHelper = UserDatabaseHelper(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cadastro",
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if (password == confirmPassword) {
                        if (!dbHelper.checkIfUserExists(email)) {
                            dbHelper.insertUser(name, email, password)
                            onRegisterSuccess()
                        } else {
                            Toast.makeText(
                                context,
                                "Usuário já cadastrado.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "As senhas não coincidem.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Preencha todos os campos.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text("Cadastrar", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Já tem uma conta? Faça login", color = MaterialTheme.colors.primary)
        }
    }
}
