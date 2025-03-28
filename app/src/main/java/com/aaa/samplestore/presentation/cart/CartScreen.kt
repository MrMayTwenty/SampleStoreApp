package com.aaa.samplestore.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aaa.samplestore.R
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.presentation.components.ErrorView
import com.aaa.samplestore.presentation.components.HeaderView
import com.aaa.samplestore.presentation.components.LoadingView

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBackClick: () -> Unit,
    onProceedToCheckoutClick: () -> Unit
) {
    val cartItems = viewModel.cartItem.value

    LaunchedEffect(Unit) {
        viewModel.getCartItems()
    }


    Scaffold (
        topBar = {
            HeaderView(
                title = stringResource(R.string.shopping_cart),
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
                if(cartItems.isLoading) {
                    LoadingView()
                }

                if(cartItems.error != null) {
                    ErrorView(cartItems.error)
                }

                if (cartItems.data.isNullOrEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartItems.data) { item ->
                            CartItemRow(item, viewModel)
                        }
                    }

                    OrderSummary(cartItems.data, onProceedToCheckoutClick)
                }
            }
        }

    }


}

@Composable
fun CartItemRow(item: CartItem, viewModel: CartViewModel) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = item.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title ?: "", fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Brand: ${item.brand}", style = MaterialTheme.typography.bodySmall)

//            item.color?.let {
//                Text("Color: $it", style = MaterialTheme.typography.bodySmall)
//            }

            Text("$${item.price}", style = MaterialTheme.typography.bodySmall)

        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { viewModel.updateQuantity(item.cartId!!, maxOf(1, item.quantity - 1)) }) {
                    val icon = painterResource(R.drawable.baseline_remove_24)
                    Icon(icon, contentDescription = "Decrease Quantity")
                }
                Text("${item.quantity}", fontWeight = FontWeight.Bold)
                IconButton(onClick = { viewModel.updateQuantity(item.cartId!!, item.quantity + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }
            IconButton(onClick = { viewModel.removeFromCart(item.cartId!!) }) {
                Icon(Icons.Default.Delete, contentDescription = "Remove Item", tint = Color.Red)
            }
        }
    }
}

@Composable
fun OrderSummary(cartItems: List<CartItem>, onCheckout: () -> Unit) {
    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.total_with_currency, totalPrice), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Button(
            onClick = onCheckout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.checkout))
        }
    }
}