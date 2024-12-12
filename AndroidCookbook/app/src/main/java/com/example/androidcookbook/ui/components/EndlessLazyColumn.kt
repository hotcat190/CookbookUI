package com.example.androidcookbook.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

internal fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}

@Composable
internal fun <T> EndlessLazyColumn(
    items: List<T>,
    itemKey: (T) -> Any,
    loadMore: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable (T) -> Unit,
) {

    val reachedBottom: Boolean by remember { derivedStateOf { listState.reachedBottom() } }

    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom && items.size > 10) {
        if (reachedBottom && items.size > 10) loadMore()
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
    ) {
        items(
            items = items,
            key = { item: T -> itemKey(item) },
        ) { item ->
            itemContent(item)
        }
    }
}

@Composable
internal fun <T> EndlessLazyColumn(
    items: List<T>,
    itemKey: (T) -> Any,
    loadMore: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable (Int, T) -> Unit,
) {

    val reachedBottom: Boolean by remember { derivedStateOf { listState.reachedBottom() } }

    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom && items.size > 10) {
        if (reachedBottom && items.size > 10) loadMore()
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
    ) {
        itemsIndexed(
            items = items,
            key = { _, item: T -> itemKey(item) },
        ) { index, item ->
            itemContent(index, item)
        }
    }
}