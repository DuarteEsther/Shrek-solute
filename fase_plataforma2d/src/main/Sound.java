package main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip; 
	
	public static final Sound musicBackground = new Sound("/srk.wav");
	public static final Sound dead = new Sound("/dead.wav");
	public static final Sound urro = new Sound("/urro.wav");
	
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e) {}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		}catch(Throwable e) {}
	}
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		}catch(Throwable e) {}
	}
}
