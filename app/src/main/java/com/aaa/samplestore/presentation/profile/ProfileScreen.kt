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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaa.samplestore.R

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onWishListClick: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val currentUserId = viewModel.getCurrentUserId()
    val userName = viewModel.userNameState.value
    val userAddress = viewModel.userAddressState.value
    val userPhone = viewModel.userPhoneState.value

    LaunchedEffect(Unit) {
        viewModel.getUserProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        if (currentUserId == null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login_or_register_to_start_shopping),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.login))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.register))
                    }
                }
            }
        } else {
            Text(
                text = stringResource(R.string.name_value, userName),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.address_value, userAddress),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.phone_value, userPhone),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onWishListClick() }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.wishlist))
        }

        Spacer(modifier = Modifier.weight(1f))

        if (currentUserId != null) {
            // Logout Button
            Button(onClick = {
                viewModel.logout()
                onLogoutSuccess()
            }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}
