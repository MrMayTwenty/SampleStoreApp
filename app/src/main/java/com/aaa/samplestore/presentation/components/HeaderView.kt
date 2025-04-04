package com.aaa.samplestore.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aaa.samplestore.R

@Composable
fun HeaderView(
    title: String = "",
    onSearch: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    shouldShowTitle: Boolean = false,
    shouldShowSearch: Boolean = false,
    shouldShowProfile: Boolean = false,
    shouldShowCart: Boolean = false
) {
    var searchQuery by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier
            .weight(1f)
            .height(60.dp)) {
            if (shouldShowSearch) {
                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearch(it)
                    },
                    placeholder = { Text(stringResource(R.string.search_textfield)) },
                )
            }
            if(shouldShowTitle){
                Text(
                    text = title,
                    modifier = Modifier.align(Alignment.CenterStart),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        if(shouldShowCart) {
            IconButton(onClick = onCartClick) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
            }
        }

        if(shouldShowProfile) {
            IconButton(onClick = onProfileClick) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Account")
            }
        }
    }
}