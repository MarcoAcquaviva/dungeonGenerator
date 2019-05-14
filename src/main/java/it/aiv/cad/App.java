package it.aiv.cad;

import java.util.Arrays;
import java.util.Random;

public class App {

	// Excercise n5
	public static void main(String[] args) {

		// Set Dungeon parameter
		Dungeon dungeon = new Dungeon(100, 100, 1);
		// Init Dungeon
		dungeon.Init(10,2);
		// Build Dungeon
		dungeon.Build();
		// Refactoring Dungeon
		dungeon.Refactoring();
		// Print dungeon
		dungeon.Print();
	}
	
	
	/// Class Lesson
	
//	public static void main(String[] args) {
//		int[][] matrix = new int[100][100];
//		int[] rules = new int[] { 0, 0, 0, 1, 1, 1, 1, 0, 1, 1 };
//		int epoque = 1;
//
//		matrix = init(matrix);
//		printMatrix(matrix);
//
//		for (int i = 0; i < epoque; i++) {
//			matrix = MooreTeacher(matrix);
//			//matrix = Moore(matrix, rules);
//			// matrix = VonNeumann(matrix,rules);
//		}
//
//		printMatrix(matrix);
//	}

	private static void printMatrix(int[][] matrix) {

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0)
					System.out.print(" ");
				else
					System.out.print("l");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void print(int text) {
		System.out.print(text);
	}

	private static int[][] init(int[][] matrix) {
		int[][] tempMatrix = new int[matrix.length][matrix[0].length];
		Random rand = new Random();

		for (int i = 0; i < tempMatrix.length; i++) {
			for (int j = 0; j < tempMatrix[0].length; j++) {
				int n = rand.nextInt(2);
				if (i == 0 || j == 0 || i == matrix.length - 1 || j == matrix[0].length - 1)
					tempMatrix[i][j] = 1;
				else
					tempMatrix[i][j] = n;
			}
		}

		return tempMatrix;
	}

	private static int[][] Moore(int[][] matrix, int[] rules) {

		int[][] tempMatrix = new int[matrix.length][matrix.length];

		for (int i = 1; i < matrix.length - 1; i++) {
			for (int j = 1; j < matrix.length - 1; j++) {
				int up = matrix[i - 1][j];
				int down = matrix[i + 1][j];
				int left = matrix[i][j - 1];
				int right = matrix[i][j + 1];
				int curr = matrix[i][j];
				int upleft = matrix[i - 1][j - 1];
				int upright = matrix[i - 1][j + 1];
				int bottomleft = matrix[i + 1][j - 1];
				int bottomright = matrix[i + 1][j + 1];

				// Cellula autonoma
				int sum = up + down + left + right + curr + upleft + upright + bottomleft + bottomright;

				tempMatrix[i][j] = rules[sum];

			}
		}

		return tempMatrix;
	}

	private static int[][] VonNeumann(int[][] matrix, int[] rules) {

		int[][] tempMatrix = new int[matrix.length][matrix.length];

		for (int i = 1; i < matrix.length - 1; i++) {
			for (int j = 1; j < matrix.length - 1; j++) {
				int up = matrix[i - 1][j];
				int down = matrix[i + 1][j];
				int left = matrix[i][j - 1];
				int right = matrix[i][j + 1];
				int curr = matrix[i][j];

				// Cellula autonoma
				int sum = up + down + left + right + curr;

				tempMatrix[i][j] = rules[sum];

			}
		}

		return tempMatrix;
	}

	private static int[][] MooreTeacher(int[][] matrix) {
		// int[][] tempMatrix = new int[matrix.length][matrix.length];

		int[][] tempMatrix = Arrays.stream(matrix).map(r -> r.clone()).toArray(int[][]::new);

		for (int i = 1; i < tempMatrix.length - 1; i++) {
			for (int j = 1; j < tempMatrix[i].length - 1; j++) {
				int wallAround = countWallAround(matrix, i, j);
				int current = matrix[i][j];
				if (current == 1) {
					if (wallAround >= 4) {
						tempMatrix[i][j] = 1;
					} else if (wallAround < 2) {
						tempMatrix[i][j] = 0;
					} else
						tempMatrix[i][j] = 0;
				} else {
					if (wallAround >= 5) {
						tempMatrix[i][j] = 1;
					} else {
						tempMatrix[i][j] = 0;
					}
				}
			}

		}

		return tempMatrix;
	}

	private static int countWallAround(int[][] matrix, int row, int col) {

		int count = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i == row && j == col)
					continue;

				count += matrix[i][j];
			}
		}

		return count;
	}
}
