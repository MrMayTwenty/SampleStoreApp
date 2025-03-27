package com.aaa.samplestore.presentation.wishlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.aaa.samplestore.R
import com.aaa.samplestore.domain.model.WishlistItem
import com.aaa.samplestore.presentation.components.HeaderView
import com.aaa.samplestore.presentation.profile.ProfileViewModel

@Composable
fun WishlistScreen(
    viewModel: WishlistViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit
) {
    val wishlistItems = viewModel.wishlistState.value

    LaunchedEffect(Unit) {
        viewModel.getWishlist()
    }

    Scaffold(
        topBar = {
            HeaderView(
                onSearch = {},
                onProfileClick = {},
                onCartClick = {},
                title = "Wishlist",
                shouldShowTitle = true
            )
        }
    ) { padding ->
        if (wishlistItems.data.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Your wishlist is empty.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(wishlistItems.data) { wishlistItem ->
                    WishlistItem(
                        wishlistItem = wishlistItem,
                        onRemove = { viewModel.removeProductFromWishlist(wishlistItem.wishlistItemId!!) },
                        onClick = { onProductClick(wishlistItem.wishlistItemId!!) }
                    )
                }
            }
        }
    }
}

@Composable
fun WishlistItem(
    wishlistItem: WishlistItem,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = wishlistItem.image,
                contentDescription = wishlistItem.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(wishlistItem.title, style = MaterialTheme.typography.bodyLarge)
                Text("$${wishlistItem.price}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }

            IconButton(onClick = onRemove) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.remove_from_wishlist), tint = Color.Red)
            }
        }
    }
}
