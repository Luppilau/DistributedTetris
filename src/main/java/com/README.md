# Welcome to the Distributed Tetris project

Note: Tetrimino in the project is a tetramino in the report. This is due to an old typo. The correct word is tetramino.

Here is a package overview:

App: Here resides the files required to run the UI of the client side application
Server: Files to run the game-server 
Tetris: Files that represent the actual game in code
    - Controller: UI objects to make the game responsive and progress in due time
        -
    - Model: Classes that represent in-game objects
        - Generators: Files to generate new tetraminos
        - Opponent: Special classes to handle the opponent view
        - Tetriminos: Files to represent the falling blocks in game
        - TetrisModel.java: The actual game model
    - Net: Utilitiy package to send messages in tuple spaces
        - Updates: Different kinds of updates for the "opponent-view" on the client side
    - View: UI objects to display the actual game

