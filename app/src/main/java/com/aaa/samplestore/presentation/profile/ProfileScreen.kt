package com.aaa.samplestore.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onWishListClick: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val currentUserId = viewModel.getCurrentUserId()
    val selectedLanguage = viewModel.selectedLanguage.value
    val isDarkMode = viewModel.isDarkMode.value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        if (currentUserId == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Login or register to start shopping",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
                        Text("Register")
                    }
            }
            }
        } else {
//            userProfile?.let {
//                Text(text = "Name: ${it.name}", style = MaterialTheme.typography.headlineMedium)
//                Text(text = "Address: ${it.address}", style = MaterialTheme.typography.bodyMedium)
//            }
        }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onWishListClick },  modifier = Modifier.fillMaxWidth()) {
                Text("Wishlist")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Settings Section
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Settings", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Dark Mode Toggle
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dark Mode:")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(onClick = { viewModel.logout() }) {
                Text("Logout")
            }
        }
    }
