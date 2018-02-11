# LoLJournal
LolJournal is an Android app to help players keep notes on Champion matchups to help them track their progress and improve. 

The goal is allowing players to create lists of champions for the lanes they play, and for each champion in that lane they can add matchups. They can then store notes related to that matchup to help them learn from past encounters. 

I've implemented an MVVM architecture using Google's Android Architectural Components. I also used Dagger, taking advantage of the new Dagger-Android practices for dependency injection and RxJava (& RxAndroid) to deal with threading non-observable queries from the Room Database.

I have future plans I'm hoping to implement for LolJournal, including:
* Storing user data online, accessible through a login
* Linking data from previous matches into the app, allowing users to tag notes to specific matches
* Storing highlights from replays 


