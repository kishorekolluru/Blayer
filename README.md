# blayer
Use VLC media player for your music? Sometimes you feel the need to frequently turn up and down
the volume, skip a song, play the previous song, repeat a song etc. With Blayer you can do that without switching 
to VLC window(except of course you have to be careful you don't unintentionally press some other
combination of keys to another application!(and that is why we use the ~(tilde) key as a common key(like ctrl) in a combination.
This is a small java wrapper for VLC media player that can handle global hotkey commands and pass 
them to VLC media player via its Remote Control interface. 

It is a runnable jar. It runs, asks you to select the music directory, then starts VLC media player for you and queues up the music. The Java program keeps running in the background listening for its cue combination of keys that have a special meaning 
for it. Press F9 for Blayer to quit. Tilde key + P(Play/Pause), + hyphen(-)(Vol down),+ equals(=)(Vol Up) etc. are some of the key combinations. you are welcome to contribute to this repo.

