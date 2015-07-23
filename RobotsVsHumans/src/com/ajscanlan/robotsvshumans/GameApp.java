package com.ajscanlan.robotsvshumans;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameApp {

	ArrayList<Robot> robots = new ArrayList<Robot>();
	ArrayList<Human> humans = new ArrayList<Human>();
	ArrayList<String> firstNames = new ArrayList<String>();
	ArrayList<String> lastNames = new ArrayList<String>();
	ArrayList<String> roboNames = new ArrayList<String>();

	final int LIMIT = 1000;
	final float DECAY = 0.05F;
	private int totalHumanPower, totalRobotPower;

	public static void main(String[] args) {
		GameApp gl = new GameApp();
		int choice = gl.displayMenu();
		gl.populateLists();

		switch(choice){
		case 1:
			gl.runProgram();
			gl.displayResults();
			break;

		case 2:
			gl.battleRoyale();
		}

	}

	private void battleRoyale() {

		while(humans.size() > 0 && robots.size() > 0){

			System.out.println(robots.get(0) + " vs " + humans.get(0));

			if(humans.get(0).getPower() > robots.get(0).getPower()){
				robots.remove(0);
				humans.get(0).setPower(humans.get(0).getPower() - (humans.get(0).getPower() * DECAY));
				System.out.println("HUMAN WINNER");
			} else if(humans.get(0).getPower() == robots.get(0).getPower()){
				robots.remove(0);
				humans.remove(0);
				System.out.println("DRAW");
			} else if(humans.get(0).getPower() < robots.get(0).getPower()) {
				humans.remove(0);
				robots.get(0).setPower(robots.get(0).getPower() - (robots.get(0).getPower() * DECAY));
				System.out.println("ROBOT WINNER");
			}
		}

		System.out.println("Humans: " + humans.size());
		System.out.println("Robots: " + robots.size());

	}

	private int displayMenu() {
		System.out.println("1) 1 versus 1");
		System.out.println("2) Battle Royale");
		//System.out.println("Literally anything else) Quit");

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		return scan.nextInt();
	}

	private void displayResults() {
		System.out.println("Humans\n-----------");
		System.out.println("Games won: " + Human.getGamesWon());
		System.out.println("Games lost: " + Human.getGamesLost());
		System.out.println("Games drew: " + Human.getGamesDrew() + "\n\n");

		System.out.println("Robots\n-----------");
		System.out.println("Games won: " + Robot.getGamesWon());
		System.out.println("Games lost: " + Robot.getGamesLost());
		System.out.println("Games drew: " + Robot.getGamesDrew() + "\n\n");

		if(Human.getGamesWon() > Robot.gamesWon){
			System.out.println("HUMANS WIN, IT'S A FINE DAY FOR HUMANITY");
		} else if(Human.getGamesWon() == Robot.getGamesWon()){
			System.out.println("It was a draw, EVERYONE IS A LOSER");
		} else {
			System.out.println("ROBOTS WIN, ALL HAIL OUR ROBOT OVERLORDS");
		}

		System.out.println("Total Human power: " + totalHumanPower);
		System.out.println("Total Robot power: " + totalRobotPower);

	}

	private void populateLists() {
		Random randy = new Random();

		try {
			Scanner hL = new Scanner(new File("lastNames.txt"));
			Scanner hF = new Scanner(new File("firstNames.txt"));
			Scanner r = new Scanner(new File("robots.txt"));

			while (hL.hasNext()){
				lastNames.add(hL.next());
			}

			while (hF.hasNext()){
				firstNames.add(hF.next());
			}

			while(r.hasNext()){
				roboNames.add(r.next());
			}

			r.close();
			hL.close();
			hF.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for(int i = 0; i < LIMIT/2; ++i){
			String firstName = firstNames.get(randy.nextInt(firstNames.size()));
			String temp = firstName.substring(1, firstName.length()).toLowerCase();
			firstName = firstName.charAt(0) + temp;

			String lastName = lastNames.get(randy.nextInt(lastNames.size()));
			temp = lastName.substring(1, lastName.length()).toLowerCase();
			lastName = lastName.charAt(0) + temp;

			String name = firstName + " " + lastName;

			humans.add(new Human(name, randy.nextInt(101), i));
			robots.add(new Robot(roboNames.get(randy.nextInt(roboNames.size())) + " " + Robot.getModelType(), randy.nextInt(101), i + 500));
		}



	}

	private void runProgram() {

		for(int i = 0; i < LIMIT/2; ++i){
			float roboPower = robots.get(i).getPower();
			float humanPower = humans.get(i).getPower();

			System.out.println(humans.get(i) +" vs " + robots.get(i) + "\n");

			totalHumanPower += humanPower;
			totalRobotPower += roboPower;

			if(humanPower == roboPower){
				Human.incGames(Human.DRAW);
				Robot.incGames(Robot.DRAW);
			}else if(humanPower > roboPower){
				Human.incGames(Human.WIN);
				Robot.incGames(Robot.LOSS);
			}else {
				Human.incGames(Human.LOSS);
				Robot.incGames(Robot.WIN);
			}
		}

	}

}
