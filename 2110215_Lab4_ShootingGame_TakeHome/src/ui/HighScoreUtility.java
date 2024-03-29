/**
 * @author Jirapat Atiwattanachai (5731015621) 
 * @version 28 OCT 2015
 * Lab4 (1/2015) in 2110215 ProgMeth
 */
package ui;

import java.io.*;
import java.util.Arrays;

import javax.swing.JOptionPane;

//Fill import

public class HighScoreUtility {

	public static class HighScoreRecord implements Comparable<HighScoreRecord>{
		private String name = "";
		private int score = 0;
		
		public HighScoreRecord(String name, int score) {
			this.name = name;
			this.score = score;
		}
		
		/* 
		 * Parse the given string "record"
		 * record format is name:score
		 */
		public HighScoreRecord(String record) throws ScoreParsingException{
			int colon = record.indexOf(":");
			if(colon == -1) throw new ScoreParsingException(1);
			else {
				try {
					this.name = record.substring(0, colon);
					this.score = Integer.parseInt(record.substring(colon+1, record.length()));
				}
				catch(NumberFormatException e1){
					throw new ScoreParsingException(0);
				}
			}
			
		}
		
		private String getRecord(){
			return name.trim()+":"+score;
		}
		
		private static String[] defaultRecord(){
			return new String[]{"A:800","B:350","C:300",
					"D:250","E:200","F:100",
					"G:40","H:30","I:20","J:10"};
		}

		@Override
		public int compareTo(HighScoreRecord o) {
			if(this.score > o.score) return -1;
			return 1;
		}
	}
	
	private static HighScoreRecord[] highScoreRecord = null;

	private static String readFileName = "highscore";
	
	/*
	 * Display player's score and record if the player rank is 10 or higher.
	 */
	public static void recordHighScore(int score){
		if(!loadHighScore() || highScoreRecord == null){
			JOptionPane.showMessageDialog(null, "Error loading highscore record", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int index=highScoreRecord.length;
		for(int i=0; i<highScoreRecord.length; i++){
			if(score > highScoreRecord[i].score){
				index = i;
				break;
			}
		}
		if(index >= highScoreRecord.length){
			JOptionPane.showMessageDialog(null, "Game over\n" + "Your score is " + score, "Game over", JOptionPane.INFORMATION_MESSAGE);
		}else{
			for(int i=highScoreRecord.length-1; i>=index+1; i--){
				highScoreRecord[i] = highScoreRecord[i-1];
			}
			String name = JOptionPane.showInputDialog(null, "Congratulation, you are ranked " + (index + 1) + "\nPlease enter your name", "High score", JOptionPane.INFORMATION_MESSAGE);
			highScoreRecord[index] = new HighScoreRecord(name, score);
			try {
				
				BufferedWriter out = new BufferedWriter(new FileWriter("highscore"));
				String str = "";
				for(int i=0; i<highScoreRecord.length; i++) {
					str += highScoreRecord[i].getRecord() + "\n";
				}
				str = str.trim();
				out.write(getXORed(str));
				out.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Error saving high score record", 
						"Error", JOptionPane.ERROR_MESSAGE);
				highScoreRecord = null;
				return;
			}
		}
	}
	
	public static void displayTop10(){
		if(!loadHighScore() || highScoreRecord == null){
			JOptionPane.showMessageDialog(null, "Error loading highscore record", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String msg = "======= Top 10 players ======="+System.getProperty("line.separator");
		int rank = 1;
		for(HighScoreRecord record : highScoreRecord){
			msg += rank+" "+record.getRecord()+System.getProperty("line.separator");
			rank++;
		}
		JOptionPane.showMessageDialog(null, msg.trim(), "Top 10", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static boolean loadHighScore(){
		File f = new File(readFileName);
		//If no high score, create default
		if(!f.exists()){
			if(!createDefaultScoreFile()) return false;
		}
		//Read high score -- if fail: delete the file, create default one and read it again 
		if(!readAndParseScoreFile(f)){
			f.delete();
			if(!createDefaultScoreFile()) return false;
			return readAndParseScoreFile(f);
		}
		return true;
		
	}
	
	private static boolean readAndParseScoreFile(File f){
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line;
			highScoreRecord = new HighScoreRecord[10];
			String str = "";
			int c;
			while((c = in.read()) != -1){
				str += (char)c;
			}
			in.close();
			String[] records = getXORed(str).split("\n");
			for(int i=0; i<highScoreRecord.length; i++){
				try{
					highScoreRecord[i] = new HighScoreRecord(records[i]);
				}catch(ScoreParsingException e){
					System.err.println("Error parsing line "+(i+1)+", "+e.getMessage());
					highScoreRecord[i] = new HighScoreRecord("ERROR_RECORD", 0);
				}
			}
			Arrays.sort(highScoreRecord);
			return true;
		} catch (Exception e) {
			highScoreRecord = null;
		}
		return false;
	}
	
	private static boolean createDefaultScoreFile(){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("highscore"));
			String str = "";
			for(String s : HighScoreRecord.defaultRecord()){
				str += s+"\n";
			}
			str = str.trim();
			out.write(getXORed(str));
			out.close();
			return true;
		} catch (IOException e1) {
			highScoreRecord = null;
			return false;
		}
	}
	
	private static final byte[] key = "RmAAq2b5d8fjgu9dhher".getBytes();
	
	//This method does both encryption and decryption 
	private static String getXORed(String in){
		byte[] inData = in.getBytes();
		for(int i = 0; i < inData.length; i++) {
			inData[i]^=key[i%key.length];
		}
		return new String(inData);
	}

	public static void setReadFileName(String name){
		readFileName = name;
	}	
}
