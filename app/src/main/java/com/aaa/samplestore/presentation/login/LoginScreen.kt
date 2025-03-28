package com.aaa.samplestore.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaa.samplestore.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userState = viewModel.userState.value

    LaunchedEffect(viewModel.userState.value.data) {
        viewModel.userState.value.data?.let {
            user -> onLoginSuccess()
        }
    }

    Card(
        modifier = Modifier
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.login),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                label = { Text(stringResource(R.string.email_label)) },
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.password_label))},
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )


            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !userState.isLoading
            ) {
                if (userState.isLoading) CircularProgressIndicator()
                else Text(text = stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onRegisterClick, enabled = !userState.isLoading) {
                if (userState.isLoading) CircularProgressIndicator()
                else Text(text = stringResource(R.string.register))
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {}, onRegisterClick = {})
}
