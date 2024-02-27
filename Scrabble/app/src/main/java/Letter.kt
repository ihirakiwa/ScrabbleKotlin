package com.example.scrabble

enum class Letter(val score: Int, val frequency: Int) {
    A( 1, 9),
    B( 3, 2),
    C( 3, 2),
    D( 2, 4),
    E( 1, 12),
    F( 4, 2),
    G( 2, 3),
    H( 4, 2),
    I( 1, 9),
    J( 8, 1),
    K( 5, 1),
    L( 1, 4),
    M( 3, 2),
    N( 1, 6),
    O( 1, 8),
    P( 3, 2),
    Q( 10, 1),
    R( 1, 6),
    S( 1, 4),
    T( 1, 6),
    U( 1, 4),
    V( 4, 2),
    W( 4, 2),
    X( 8, 1),
    Y( 4, 2),
    Z( 10, 1),
    BLANK(0,2);


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