Hangman
=======

Hangman is a native app for the Android platform. The app thinks of a word and the player tries to guess it by suggesting letters.


How to get it up and running
----------

- Clone the repository: git clone git@github.com:muzzi11/hangman.git
- Get Eclipse and the ADT plugin, see instructions here: http://developer.android.com/sdk/installing/installing-adt.html
- Open the SDKManager and install the SDK Platform, system images and Google APIs for Android 4.2.2(API17)
- Open Eclipse and choose a workspace folder that's different from the project folder
- New > Project> Android > Android Project from Existing Code

----------

Features
---------

- Immediately upon launch, gameplay must start (unless the app was simply backgrounded, in which case gameplay, if in progress prior to backgrounding, should resume).
- The app’s front side must display placeholders (e.g., hyphens) for yet-unguessed letters that make clear the word’s length.
- The app must inform the user graphically how many incorrect guesses he or she can still make before losing by showing the progress of a gallows being built up.
- The app must indicate to the user which letters he or she has guessed so far by highlighting them on the virtual keyboard.
- The user must be able to input guesses via an on-screen keyboard.
- The app must have a logo and one button that starts a new game.
- If the user guesses every letter in some word before running out of chances, he or she will be congratulated, and gameplay should end (i.e., the game should ignore any subsequent keyboard input). If the user fails to guess every letter in some word before running out of chances, he or she will be somehow consoled, and gameplay should end. The front side’s two buttons should continue to operate.
- A user must be able to configure three settings: the length of words to be guessed (the allowed range for which must be [1,n], where n is the length of the longest word in words.plist/xml); the maximum number of incorrect guesses allowed (the allowed range for which must be [1,26]); and the game mode(normal or evil).
- When settings are changed, they should only take effect for new games, not one already in progress, if any.
- The app must maintain a history of high scores that’s displayed anytime a game is won or lost. High scores are based on the unique amount of letters in the word and the number of correctly guessed letters divided by the total amount of letters guessed. The top 10 scores are displayed with the word guessed. The history of high scores should persist even when the app is backgrounded or force-quit.
- The app must have an additional game mode called evil. In this game mode the app dynamically chooses the word to be guessed as the game progresses. This dynamic choice will keep the amount of possible words to a maximum for every letter that's guessed.

Frameworks, language and libraries
---------

- Java
- AndEngine

Mock-up screens
---------

![Main screen](https://raw.github.com/muzzi11/hangman/master/Doc/mock-main.png)
![Highscores screen](https://raw.github.com/muzzi11/hangman/master/Doc/mock-highscores.png)
![Settings screen](https://raw.github.com/muzzi11/hangman/master/Doc/mock-settings.png)

Task division
---------
Pair programming for basic setup. Mustafa will program the required animation code while Jos will program the game-play side.
