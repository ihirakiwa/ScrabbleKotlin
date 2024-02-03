package com.example.scrabble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrabble.CellType.*


@Composable
fun ScrabbleGrid(grid: Array<Array<CellType>>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(grid) { rowIndex, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                row.forEachIndexed { columnIndex, cellType ->
                    ScrabbleCell(cellType = cellType)
                }
            }
        }
    }
}

@Composable
fun ScrabbleCell(cellType: CellType) {
    Box(
        modifier = Modifier
            .size(21.5.dp)
            .background(cellType.color)
    ) {
        Text(
            text = cellType.name,
            color = Color.Black,
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScrabbleGrid() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ScrabbleGrid(GRID)
    }
}
