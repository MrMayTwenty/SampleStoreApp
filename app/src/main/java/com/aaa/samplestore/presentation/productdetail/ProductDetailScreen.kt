package com.aaa.samplestore.presentation.productdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aaa.samplestore.presentation.components.ErrorView
import com.aaa.samplestore.presentation.components.LoadingView

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    productId: Int,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onBuyNowClick: () -> Unit
) {

    val productState = viewModel.productState.value

    // Description
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    val maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3
    var numberOfOrders by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        viewModel.getProductById(productId)
    }

    if(productState.isLoading) {
        LoadingView()
    }

    if(productState.error != null) {
        ErrorView(productState.error)
    }

    if(productState.data != null) {
        val product = productState.data
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Product Image
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Product Title
            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price & Discount
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                product.discount?.let {
                    if(it > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "-${product.discount}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))



            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable { isDescriptionExpanded = !isDescriptionExpanded }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Brand
            Text(
                text = "Brand: ${product.brand}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Category
            Text(
                text =  "Category: ${product.category}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Color
            product.color?.let {
                Text(
                    text = "Color: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { if (numberOfOrders > 1) numberOfOrders-- }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Decrease"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = numberOfOrders.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { numberOfOrders++ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart Button
            Button(
                onClick = {
                    viewModel.addToCart(product, numberOfOrders)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Add to Cart")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buy Now Button
            Button(
                onClick = {
                    viewModel.addToCart(product, numberOfOrders)
                    onBuyNowClick()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Buy Now")
            }
        }
    }


}
