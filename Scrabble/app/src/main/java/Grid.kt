package com.example.scrabble


import androidx.compose.ui.graphics.Color
import com.example.scrabble.CellType.BL
import com.example.scrabble.CellType.DL
import com.example.scrabble.CellType.DW
import com.example.scrabble.CellType.ST
import com.example.scrabble.CellType.TL
import com.example.scrabble.CellType.TW

 enum class CellType(val color: Color) {
    TW(Color(180, 180, 180)), // Triple word
    TL(Color(46, 60, 166)), // Triple letter
    DW(Color(170, 22, 18)), // Double word
    DL(Color(98, 138, 169)), // Double letter
    BL(Color(40, 40, 40)), // Blank
    ST(Color(22, 142, 212)), // Start
}

 val GRID = arrayOf(
    arrayOf(TW, BL, BL, DL, BL, BL, BL, TW, BL, BL, BL, DL, BL, BL, TW), ////////
    arrayOf(BL, DW, BL, BL, BL, TL, BL, BL, BL, TL, BL, BL, BL, DW, BL), ///////
    arrayOf(BL, BL, DW, BL, BL, BL, DL, BL, DL, BL, BL, BL, DW, BL, BL), //////
    arrayOf(DL, BL, BL, DW, BL, BL, BL, DL, BL, BL, BL, DW, BL, BL, DL), /////
    arrayOf(BL, BL, BL, BL, DW, BL, BL, BL, BL, BL, DW, BL, BL, BL, BL), ////
    arrayOf(BL, TL, BL, BL, BL, TL, BL, BL, BL, TL, BL, BL, BL, TL, BL), ///
    arrayOf(BL, BL, DL, BL, BL, BL, DL, BL, DL, BL, BL, BL, DL, BL, BL), //
    arrayOf(TW, BL, BL, DL, BL, BL, BL, ST, BL, BL, BL, DL, BL, BL, TW),
    arrayOf(BL, BL, DL, BL, BL, BL, DL, BL, DL, BL, BL, BL, DL, BL, BL), //
    arrayOf(BL, TL, BL, BL, BL, TL, BL, BL, BL, TL, BL, BL, BL, TL, BL), ///
    arrayOf(BL, BL, BL, BL, DW, BL, BL, BL, BL, BL, DW, BL, BL, BL, BL), ////
    arrayOf(DL, BL, BL, DW, BL, BL, BL, DL, BL, BL, BL, DW, BL, BL, DL), /////
    arrayOf(BL, BL, DW, BL, BL, BL, DL, BL, DL, BL, BL, BL, DW, BL, BL), //////
    arrayOf(BL, DW, BL, BL, BL, TL, BL, BL, BL, TL, BL, BL, BL, DW, BL), ///////
    arrayOf(TW, BL, BL, DL, BL, BL, BL, TW, BL, BL, BL, DL, BL, BL, TW), ////////
)