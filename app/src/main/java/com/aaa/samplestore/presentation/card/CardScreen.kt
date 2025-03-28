package com.aaa.samplestore.presentation.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.presentation.components.HeaderView
import com.paypal.android.cardpayments.Card
import com.paypal.android.cardpayments.CardApproveOrderResult
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.cardpayments.threedsecure.SCA
import com.paypal.android.corepayments.Address
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment

@Composable
fun CardScreen(
    viewModel: CardViewModel = hiltViewModel(),
    orderId: String,
    onRequestApproveOrder: (CardRequest) -> Unit
) {
    viewModel.setOrderId(orderId)
    var cardNumber = viewModel.cardNumber.value
    var expirationMonth = viewModel.expirationMonth.value
    var expirationYear = viewModel.expirationYear.value
    var securityCode = viewModel.securityCode.value

    LaunchedEffect(viewModel.cardRequest.value) {
        viewModel.cardRequest.value?.let { request ->
            onRequestApproveOrder(request)
        }
    }


    Scaffold(
        topBar = {
            HeaderView(
                title = stringResource(R.string.card_details),
                shouldShowSearch = false,
                shouldShowTitle = true,
                shouldShowCart = false,
                shouldShowProfile = false,
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { viewModel.updateCardNumber(it) },
                    label = { Text(stringResource(R.string.card_number)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = expirationMonth,
                        onValueChange = { viewModel.updateExpirationMonth(it) },
                        label = { Text(stringResource(R.string.mm)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = expirationYear,
                        onValueChange = { viewModel.updateExpirationYear(it) },
                        label = { Text(stringResource(R.string.yyyy)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = securityCode,
                    onValueChange = { securityCode = it },
                    label = { Text(stringResource(R.string.cvv)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    onClick = {
                        viewModel.resetTestCard()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.reset_test_card))
                }

                Button(
                    onClick = {
                        viewModel.submitCardDetails()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
        }

    }


}
