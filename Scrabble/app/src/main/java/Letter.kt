package com.example.scrabble

enum class Letter(val score: Int, val frequency: Int) {
    A( 1, 9),
    B( 3, 2),
    C( 3, 2),
    D( 2, 3),
    E( 1, 15),
    F( 4, 2),
    G( 2, 2),
    H( 4, 2),
    I( 1, 8),
    J( 8, 1),
    K( 10, 1),
    L( 1, 5),
    M( 2, 3),
    N( 1, 6),
    O( 1, 6),
    P( 3, 2),
    Q( 8, 1),
    R( 1, 6),
    S( 1, 6),
    T( 1, 6),
    U( 1, 6),
    V( 4, 2),
    W( 10, 1),
    X( 10, 1),
    Y( 10, 1),
    Z( 10, 1),
    BLANK(0,2);

    fun displayLetter(): String {
        return when (this) {
            BLANK -> "_"
            else -> this.name
        }
    }
}


private val stock = mutableListOf<Letter>()
fun stockClear(){
    stock.clear()
}
fun stockAdd(letter: Letter){
    stockClear()
    stock.add(letter)
}
fun getLastLetter(): Letter?{
    if (stock.isEmpty()) {
        return null
    }
    return stock[0]
}