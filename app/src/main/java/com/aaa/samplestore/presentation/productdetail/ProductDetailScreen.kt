package com.aaa.samplestore.presentation.productdetail

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aaa.samplestore.R
import com.aaa.samplestore.presentation.components.ErrorView
import com.aaa.samplestore.presentation.components.HeaderView
import com.aaa.samplestore.presentation.components.LoadingView
import com.aaa.samplestore.presentation.components.SaleBadge

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    productId: Int,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onBuyNowClick: () -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    val productState = viewModel.productState.value
    val productLikeState = viewModel.productLikeState.value
    val currentUserId = viewModel.currentUserId

    // Description
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    val maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3
    var numberOfOrders by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        viewModel.getProductById(productId)
        viewModel.getIsProductOnWishlist(productId)
    }

    if(productState.isLoading) {
        LoadingView()
    }

    if(productState.error != null) {
        ErrorView(productState.error)
    }

    if(productState.data != null) {
        val product = productState.data

        Scaffold(
            topBar = {
                HeaderView(
                    onSearch = {},
                    onProfileClick = onProfileClick,
                    onCartClick = onCartClick
                )
            },
            bottomBar = {
                Column (modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.quantity),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { if (numberOfOrders > 1) numberOfOrders-- }) {
                            Icon(
                                painterResource(R.drawable.baseline_remove_24),
                                contentDescription = stringResource(R.string.decrease)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = numberOfOrders.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { numberOfOrders++ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.increase)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {
                            viewModel.addToWishlist(product, currentUserId)
                        }) {
                            if(productLikeState.isLoading){
                                CircularProgressIndicator()
                            }

                            if(productLikeState.data == true){
                                Icon(
                                    painterResource(R.drawable.baseline_favorite_24),
                                    contentDescription = stringResource(R.string.wishlisted),
                                    tint = Color.Red
                                )
                            }else{
                                Icon(
                                    painterResource(R.drawable.baseline_favorite_border_24),
                                    contentDescription = stringResource(R.string.not_wishlisted),
                                    tint = Color.Red
                                )
                            }

                        }
                    }

                    Row {
                        Button(
                            onClick = {
                                viewModel.addToCart(product,currentUserId, numberOfOrders)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = stringResource(R.string.add_to_cart))
                        }

                        Spacer(modifier = Modifier.width(2.dp))

                        Button(
                            onClick = {
                                viewModel.addToCart(product,currentUserId, numberOfOrders)
                                onBuyNowClick()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = stringResource(R.string.buy_now))
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
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

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                            SaleBadge(product.discount)
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

                Text(
                    text = stringResource(R.string.brandWithValue, product.brand),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.categoryWithValue, product.category),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                product.color?.let {
                    Text(
                        text = stringResource(R.string.color_with_value, it),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }

            }
        }


    }


}
