package com.aaa.samplestore.presentation.checkout

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aaa.samplestore.R
import androidx.core.net.toUri
import com.aaa.samplestore.presentation.components.HeaderView

@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onGetPayPalOrderSuccess: (String) -> Unit
) {
    val checkoutState = viewModel.checkoutFormState.value
    val cartState = viewModel.cartState.value
    val payPalOrderIdState = viewModel.payPalOrderIdState.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadCartItems()
    }

    LaunchedEffect(payPalOrderIdState) {
        if (payPalOrderIdState.data != null) {
            onGetPayPalOrderSuccess(payPalOrderIdState.data.orderId)
        }
    }

    Scaffold(
        topBar = {
            HeaderView(
                title = stringResource(R.string.checkout),
                shouldShowTitle = true
            )
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            stringResource(R.string.order_summary),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))


                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                        ) {
                            items(cartState.data ?: emptyList()) { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        item.title,
                                        modifier = Modifier.weight(0.7f),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("x${item.quantity}", modifier = Modifier.weight(0.2f))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        stringResource(
                                            R.string.currencyWithPrice,
                                            item.price * item.quantity
                                        ), modifier = Modifier.weight(0.3f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Total Price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.total), fontWeight = FontWeight.Bold)
                            Text("$${checkoutState.totalPrice}", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form Inputs
                OutlinedTextField(
                    value = checkoutState.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text(stringResource(R.string.full_name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = checkoutState.address,
                    onValueChange = { viewModel.onAddressChange(it) },
                    label = { Text(stringResource(R.string.address)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = checkoutState.contactNumber,
                    onValueChange = { viewModel.onContactChange(it) },
                    label = { Text(stringResource(R.string.contact_number)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.processPayPalPayment() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !payPalOrderIdState.isLoading
                ) {
                    if (payPalOrderIdState.isLoading) CircularProgressIndicator()
                    else Text(stringResource(R.string.pay_with_paypal))
                }

            }
        }

    }


}
