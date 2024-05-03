// MusicPlayerInterface.aidl
package uic.cs478.jaykumarkakkad.myapplication.commonInterface;

// Declare any non-default types here with import statements

interface MusicPlayerInterface {
    void playMusic(int id);
    void stopMusic();
    void resume();
    void pause();
}