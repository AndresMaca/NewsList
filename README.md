# NewsList

- I attach the dagger 2 lifecycle at the ViewModel, I never do this but I want to experiment with new stuff, I saw that in a medium post with about 10k cheers, and it have sense to me in this kind of apps. I usually attach the dagger - lifecycle to the app lifecycle. Anyways both of them follows: 
![alt text](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
- It has protection against MITM attacks, but...
- There's a lot of security issues that can be discussed and for lack of time will not be implemented, IE: emulator detector, integrity detection (to avoid repackaging)

# ATENTION.
- The HackerNews API it's a little bit messed they send the ID of the object in diferent keys, and sometimes they send the same story multiples times. With this behavior when you get i.e 10k registers there're maybe only 300 unique Stories, a lot of them are repeated ones.
I'm printing the output of the response in the console so you can check the download are doing well, but its a MUST to delete this kind of log in a Real-production app.
