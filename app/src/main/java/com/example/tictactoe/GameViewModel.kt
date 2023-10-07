package com.example.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class GameViewModel : ViewModel() {
    var state by mutableStateOf(GameState())
    private var selectWinCellValue = 0

    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.BoardTapped -> {
                addValueToBoard(action.cellNo)
            }

            UserAction.PlayAgainButtonClicked -> {
                gameReset()
            }
        }
    }


    private var startPlayer = state.startPlayer
    private fun gameReset() {
        boardItems.forEach { (i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        if (startPlayer == BoardCellValue.CIRCLE){
            startPlayer = BoardCellValue.CROSS
        }else{
            startPlayer = BoardCellValue.CIRCLE
        }

        if (startPlayer == BoardCellValue.CROSS){
            state = state.copy(
                hintText = "Player '0' turn",
                currentTurn = startPlayer,
                victoryType = VictoryType.NONE,
                hasWon = false
            )
            bot()
        }else{
            state = state.copy(
                hintText = "Player 'X' turn",
                currentTurn = startPlayer,
                victoryType = VictoryType.NONE,
                hasWon = false)

        }
    }
    private fun addValueToBoard(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE) {
            return
        }
        if (state.currentTurn == BoardCellValue.CIRCLE) {
            boardItems[cellNo] = BoardCellValue.CIRCLE
            state = if (checkForVictory(BoardCellValue.CIRCLE)) {
                state.copy(
                    hintText = "Player 'O' Won",
                    playerCircleCount = state.playerCircleCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state.copy(
                    hintText = "Player 'X' turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if (state.currentTurn == BoardCellValue.CROSS) {
            boardItems[cellNo] = BoardCellValue.CROSS
            state = if (checkForVictory(BoardCellValue.CROSS)) {
                state.copy(
                    hintText = "Computer Won",
                    playerCrossCount = state.playerCrossCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state.copy(
                    hintText = "Player 'O' turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
        if(state.currentTurn == BoardCellValue.CROSS &&!hasBoardFull()){
            bot()
        }
    }

    private fun checkWin(boardValue: BoardCellValue):Boolean{
        when {
            ((boardItems[2] == boardValue && boardItems[3] == boardValue)||
                    (boardItems[5] == boardValue && boardItems[9] == boardValue)||
                    (boardItems[4] == boardValue && boardItems[7] == boardValue)) && boardItems[1] == BoardCellValue.NONE -> {
                selectWinCellValue = 1
                return true
            }
            ((boardItems[1] == boardValue && boardItems[3] == boardValue)||
                    (boardItems[5] == boardValue && boardItems[8] == boardValue)) && boardItems[2] == BoardCellValue.NONE -> {
                selectWinCellValue = 2
                return true
            }
            ((boardItems[1] == boardValue && boardItems[2] == boardValue)||
                    (boardItems[5] == boardValue && boardItems[7] == boardValue)||
                    (boardItems[6] == boardValue && boardItems[9] == boardValue)) && boardItems[3] == BoardCellValue.NONE -> {
                selectWinCellValue = 3
                return true
            }
            ((boardItems[1] == boardValue && boardItems[7] == boardValue)||
                    (boardItems[5] == boardValue && boardItems[6] == boardValue)) && boardItems[4] == BoardCellValue.NONE -> {
                selectWinCellValue = 4
                return true
            }
            ((boardItems[2] == boardValue && boardItems[8] == boardValue)||
                    (boardItems[4] == boardValue && boardItems[6] == boardValue)||
                    (boardItems[1] == boardValue && boardItems[9] == boardValue)||
                    (boardItems[3] == boardValue && boardItems[7] == boardValue)) && boardItems[5] == BoardCellValue.NONE -> {
                selectWinCellValue = 5
                return true
            }
            ((boardItems[3] == boardValue && boardItems[9] == boardValue)||
                    (boardItems[4] == boardValue && boardItems[5] == boardValue)) && boardItems[6] == BoardCellValue.NONE -> {
                selectWinCellValue = 6
                return true
            }
            ((boardItems[1] == boardValue && boardItems[4] == boardValue)||
                    (boardItems[3] == boardValue && boardItems[5] == boardValue)||
                    (boardItems[8] == boardValue && boardItems[9] == boardValue)) && boardItems[7] == BoardCellValue.NONE -> {
                selectWinCellValue = 7
                return true
            }
            ((boardItems[7] == boardValue && boardItems[9] == boardValue)||
                    (boardItems[2] == boardValue && boardItems[5] == boardValue)) && boardItems[8] == BoardCellValue.NONE -> {
                selectWinCellValue = 8
                return true
            }
            ((boardItems[1] == boardValue && boardItems[5] == boardValue)||
                    (boardItems[3] == boardValue && boardItems[6] == boardValue)||
                    (boardItems[7] == boardValue && boardItems[8] == boardValue)) && boardItems[9] == BoardCellValue.NONE -> {
                selectWinCellValue = 9
                return true
            }
            else -> return false
        }
    }

    private fun block():Boolean{
        return checkWin(BoardCellValue.CIRCLE)
    }
    private fun checkMiddle():Boolean{
        return boardItems[5] == BoardCellValue.NONE
    }
    private fun randMove(){
        addValueToBoard(randNullSpace())
    }

    private fun randNullSpace():Int{
        val blankSpace = ArrayList<Int>()
        for (i in 1..9){
            if (boardItems[i] == BoardCellValue.NONE){
                blankSpace.add(i)
            }
        }
        return blankSpace.random()
    }

    private fun checkForVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue && boardItems[3] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }

            boardItems[4] == boardValue && boardItems[5] == boardValue && boardItems[6] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }

            boardItems[7] == boardValue && boardItems[8] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }

            boardItems[1] == boardValue && boardItems[4] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }

            boardItems[2] == boardValue && boardItems[5] == boardValue && boardItems[8] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }

            boardItems[3] == boardValue && boardItems[6] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }

            boardItems[1] == boardValue && boardItems[5] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }

            boardItems[3] == boardValue && boardItems[5] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }

            else -> return false
        }
    }

    fun hasBoardFull(): Boolean {
        return !boardItems.containsValue(BoardCellValue.NONE)
    }
    private  fun bot(){
        if (checkWin(BoardCellValue.CROSS)){
            addValueToBoard(selectWinCellValue)
        }
        else if(block()){
            addValueToBoard(selectWinCellValue)
        }
        else if(checkMiddle()){
            addValueToBoard(5)
        }
        else{
            randMove()
        }
    }
}

