
//Adam Wu 
import processing.core.*;
import processing.video.*;
import java.util.*;

import oscP5.*;
import netP5.*;
//importing the JMusic stuff
import jm.music.data.*;
import jm.JMC;
import jm.util.*;
import jm.midi.*;

import java.io.UnsupportedEncodingException;
import java.net.*;

//make sure this class name matches your file name, if not fix.
public class HelloWorldMidiMain extends PApplet {
	XiaoQiu qiu;
	MelodyPlayer player; // play a midi sequence
	MidiFileToNotes midiNotes; // read a midi file

	ProbabilityGenerator<Integer> pitchGenerator = new ProbabilityGenerator<Integer>();
	ProbabilityGenerator<Double> ryhthemGenerator = new ProbabilityGenerator<Double>();
	MarkovG<Integer> p2 = new MarkovG<Integer>();
	MarkovG<Double> r2 = new MarkovG<Double>();
	MarkovOrder<Integer> p3 = new MarkovOrder<Integer>();
	MarkovOrder<Double> r3 = new MarkovOrder<Double>();
	Tree<String> t1 = new Tree<String>();
	Tree<Integer> pt = new Tree<Integer>();// pitch
	Tree<Double> rt = new Tree<Double>();// for ryhthem
	boolean play = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("HelloWorldMidiMain"); // change this to match above class & file name

	}

	float insenity = 0;
	int line = 1000;
	int cellSize = 20;

	int cols, rows;

	Capture video;

	// setting the window size to 300x300
	public void settings() {
		size(800, 800);

	}

	XiaoQiu[] xqs = new XiaoQiu[2000];
	float toumingdu;

	// doing all the setup stuff
	public void setup() {

		OscP5 oscP5;

		oscP5 = new OscP5(this, 6012);
		oscP5.plug(this, "setAmp", "/amp");

		// returns a url
		String filePath = getPath("mid/pirate.mid");
		// playMidiFile(filePath);

		midiNotes = new MidiFileToNotes(filePath);
//		// which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
		midiNotes.setWhichLine(0);

		player = new MelodyPlayer(this, 100.0f);
		player.setup();
		player.setMelody(midiNotes.getPitchArray()); // instead of setting Melody, send it to ProbabilityGen?
		player.setRhythm(midiNotes.getRhythmArray());
		colorMode(HSB, 360, 100, 100);
		for (int i = 0; i < xqs.length; i++) {
			xqs[i] = new XiaoQiu(new PVector(random(0, width), random(0, height)), random(5, 10));
		}

	}

	public void draw() {

		player.play();

		toumingdu = map(insenity, 0, width, 255, 0);
		noStroke();
		fill(0, toumingdu);
		rect(0, 0, width, height);
		for (int i = 0; i < xqs.length; i++) {
			xqs[i].update();
			xqs[i].display();
			xqs[i].check();

		}
		fill(0, 40);
		fill(255);

		ellipse(random(width), random(height), 5, 5);
		float ax = 300;
		float ay = 300;
		for (int i = 0; i < 1000; i++) {
			float bx = ax + random(-insenity, insenity);
			float by = ay + random(-insenity, insenity);
			fill(255);
			line(ax, ay, bx, by);
			ax = bx;
			ay = by;
		}
	}

	public void setAmp(float amp) {
		insenity = amp * 10;
	}

	class XiaoQiu {
		PVector loc;
		float vx = 0, vy = 0;
		float r;
		float baocunr;
		float angle = 0;
		float cx, cy, c;
		boolean bianxiao = true;

		XiaoQiu(PVector location, float r) {
			loc = location;
			this.r = r;
			baocunr = r;
			cx = map(loc.x, 0, width, 0, 180);
			cy = map(loc.y, 0, height, 0, 180);
			c = cx + cy;

		}

		void update() {

			angle += 0.02 * noise((float) 0.001 * loc.x, (float) 0.001 * loc.y);
			vx = (float) (2 * Math.sin(angle) + insenity);
			vy = (float) (2 * Math.cos(angle));
			loc.x += vx;
			loc.y += vy;
			if (bianxiao)
				r -= 0.3;
			if (r <= 0)
				bianxiao = false;
			if (!bianxiao)
				r += 0.3;
			if (r >= baocunr)
				bianxiao = true;
			c += 5;

		}

		void display() {
			noStroke();
			fill(c % 360, 100, 100);
			ellipse(loc.x, loc.y, r, insenity);

		}

		void check() {

			if (loc.x < 0)
				loc.x = width;
			if (loc.x > width)
				loc.x = 0;
			if (loc.y < 0)
				loc.y = height;
			if (loc.y > height)
				loc.y = 0;
		}
	}

	public void blink() {
		float ax = (float) (width / 2.0);
		float ay = (float) (height / 2.0);
		for (int i = 0; i < line; i++) {
			float bx = ax + random(-insenity, insenity);
			float by = ay + random(-insenity, insenity);
			line(ax, ay, bx, by);
			ax = bx;
			ay = by;
		}
	}

	class system // fall down
	{
		float speed;
		ArrayList<Integer> points;

		system(float speed_) {
			speed = speed_;
			points = new ArrayList<Integer>();
		}

		void generate() {
			PVector position = new PVector(random(0, width), 0);
			PVector velocity = new PVector(0, random(5, 10));
			float rotation = random(0, 360);
			float aVel = random(-2, 5);
			float radius = random(10, 20);
			float ratio = (float) 0.618;
			flashingpoints s = new flashingpoints(position, velocity, rotation, aVel, radius, ratio);

		}

		public void emit() {
			if (speed >= 1) {
				for (int i = 0; i < speed; i++) {
					generate();
				}
			} else if (speed > 0) {
				if (random(1) < speed) {
					generate();
				}
			}
		}

		public void update() {
			ArrayList<Integer> flashingpoints = new ArrayList<Integer>();
			Iterator<Integer> ite = flashingpoints.iterator();
			while (ite.hasNext()) {
				Integer s = ite.next();

			}
		}

		void run() {
			emit();
			update();
		}
	}

	class flashingpoints {

		public flashingpoints(PVector position, PVector velocity, float rotation, float aVel, float radius,
				float ratio) {

		}

	}

	// this finds the absolute path of a file
	String getPath(String path) {

		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	void playMidiFile(String filename) {
		Score theScore = new Score("Temporary score");
		Read.midi(theScore, filename);
		Play.midi(theScore);
	}

	// Unit Test One
	public void UnitTest() {// unit test for tree
		String[] abracadabra = { "a", "b", "r", "a", "c", "a", "d", "a", "b", "r", "a" };
		ArrayList<String> tree1 = new ArrayList<String>(Arrays.asList(abracadabra));
		t1.setTokens(tree1);
		t1.train();
		System.out.println(String.format("abracadabra:  PST L=%d Pmin=%.2f R=1.5", t1.L, t1.pMin));
		t1.print();
		t1.clearT();
		String[] acadaacbda = { "a", "c", "a", "d", "a", "a", "c", "b", "d", "a" };
		ArrayList<String> tree2 = new ArrayList<String>(Arrays.asList(acadaacbda));
		t1.setTokens(tree2);
		t1.train();
		System.out.println("-----------------");
		System.out.println(String.format("acadaacbda:  PST L=%d Pmin=%.2f R=1.5", t1.L, t1.pMin));
		t1.print();
		t1.clearT();
		String[] abcccdaadcdaabcadad = { "a", "b", "c", "c", "c", "d", "a", "a", "d", "c", "d", "a", "a", "b", "c", "a",
				"d", "a", "d" };
		ArrayList<String> tree3 = new ArrayList<String>(Arrays.asList(abcccdaadcdaabcadad));
		t1.setTokens(tree3);
		t1.train();
		System.out.println(String.format("abcccdaadcdaabcadad:  PST L=%d Pmin=%.2f R=1.5", t1.L, t1.pMin));

		t1.print();
		pt.setTokens(midiNotes.getPitchArray());
		pt.train();
		System.out.println("----------------------------");
		// System.out.println(": PST L=3 Pmin=0.1 R=1.5");
		System.out.println(String.format("MaryHadALittleLamb:  PST L=%d Pmin=%.2f R=%d", t1.L, t1.pMin, t1.r));
		System.out.println("----------------------------");
		// System.out.println("PitchTree: ");
		pt.print();

		// System.out.println("RhythmTree: ");
		// rt.train();
		// rt.print();

	}

	public void keyPressed() {
		if (key == ' ') {
			player.reset();
			println("Melody started!");
		}
		if (key == '1') {
			UnitTest();
		}
		if (key == '2') {
			ArrayList<Integer> pitchInput = midiNotes.getPitchArray();

			ArrayList<Double> rhythmInput = midiNotes.getRhythmArray();

			p3.setTokens(pitchInput);
			rt.setTokens(rhythmInput);
			System.out.println("------Pitches Probability:-------");
			p3.train();
			p3.print(1);
			System.out.println("------------------------------");
			System.out.println("------Rhythm Probability:-------");
			pt.train();
			rt.print(1);
			System.out.println("------------------------------");
			r3.generate();
			p3.generate();
			System.out.println(pt.genarray());
			System.out.println(rt.genarray());
			player.setMelody(pt.genarray());
			player.setRhythm(rt.genarray());
		}

		if (key == 'P') {
			if (play == false) {
				play = true;
			} else {
				play = false;
			}
		}

	}

}
