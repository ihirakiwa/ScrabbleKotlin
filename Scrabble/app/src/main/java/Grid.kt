package com.example.scrabble


import androidx.compose.ui.graphics.Color
import com.example.scrabble.CellType.BL
import com.example.scrabble.CellType.LD
import com.example.scrabble.CellType.MD
import com.example.scrabble.CellType.ST
import com.example.scrabble.CellType.LT
import com.example.scrabble.CellType.MT

 enum class CellType(val color: Color) {
    MT(Color(180, 180, 180)), // Triple word
    LT(Color(46, 60, 166)), // Triple letter
    MD(Color(170, 22, 18)), // Double word
    LD(Color(98, 138, 169)), // Double letter
    BL(Color(40, 40, 40)), // Blank
    ST(Color(22, 142, 212)), // Start
}

val GRID = arrayOf(
   arrayOf(MT, BL, BL, LD, BL, BL, BL, MT, BL, BL, BL, LD, BL, BL, MT), ////////
   arrayOf(BL, MD, BL, BL, BL, LT, BL, BL, BL, LT, BL, BL, BL, MD, BL), ///////
   arrayOf(BL, BL, MD, BL, BL, BL, LD, BL, LD, BL, BL, BL, MD, BL, BL), //////
   arrayOf(LD, BL, BL, MD, BL, BL, BL, LD, BL, BL, BL, MD, BL, BL, LD), /////
   arrayOf(BL, BL, BL, BL, MD, BL, BL, BL, BL, BL, MD, BL, BL, BL, BL), ////
   arrayOf(BL, LT, BL, BL, BL, LT, BL, BL, BL, LT, BL, BL, BL, LT, BL), ///
   arrayOf(BL, BL, LD, BL, BL, BL, LD, BL, LD, BL, BL, BL, LD, BL, BL), //
   arrayOf(MT, BL, BL, LD, BL, BL, BL, ST, BL, BL, BL, LD, BL, BL, MT),
   arrayOf(BL, BL, LD, BL, BL, BL, LD, BL, LD, BL, BL, BL, LD, BL, BL), //
   arrayOf(BL, LT, BL, BL, BL, LT, BL, BL, BL, LT, BL, BL, BL, LT, BL), ///
   arrayOf(BL, BL, BL, BL, MD, BL, BL, BL, BL, BL, MD, BL, BL, BL, BL), ////
   arrayOf(LD, BL, BL, MD, BL, BL, BL, LD, BL, BL, BL, MD, BL, BL, LD), /////
   arrayOf(BL, BL, MD, BL, BL, BL, LD, BL, LD, BL, BL, BL, MD, BL, BL), //////
   arrayOf(BL, MD, BL, BL, BL, LT, BL, BL, BL, LT, BL, BL, BL, MD, BL), ///////
   arrayOf(MT, BL, BL, LD, BL, BL, BL, MT, BL, BL, BL, LD, BL, BL, MT), ////////
)