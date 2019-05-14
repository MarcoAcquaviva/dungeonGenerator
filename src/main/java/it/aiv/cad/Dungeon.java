package it.aiv.cad;

import java.util.Arrays;
import java.util.Random;

public class Dungeon {

	private int _row, _col, _epoque;
	int[][] _matrix;
	private int maxVamue;

	public Dungeon(int row, int col, int epoque) {
		_matrix = new int[row][col];
		_row = row;
		_col = col;
		_epoque = epoque;

	}

	public void Init(int seed, int blankLines) {
		Random rand = new Random();
		rand.setSeed(seed);
		for (int i = 0; i < _matrix.length; i++) {
			for (int j = 0; j < _matrix[i].length; j++) {
				if (i == 0 || j == 0 || i == _matrix.length - 1 || j == _matrix[0].length - 1)
					_matrix[i][j] = 1;

				else
					_matrix[i][j] = rand.nextInt(2);

				if (i >= (_matrix.length / 2 - (blankLines))
						&& i <= (_matrix.length / 2 + (blankLines)))
					if (blankLines % 2 != 0) {
						if (i >= (_matrix.length / 2 - (blankLines / 2))
								&& i <= (_matrix.length / 2 + (blankLines / 2))) {
							_matrix[i][j] = i;
						}
					} else if (i >= (_matrix.length / 2 - (blankLines / 2))
							&& i < (_matrix.length / 2 + (blankLines / 2))) {
						_matrix[i][j] = i;
					}
			}
		}
	}

	public int[][] Moore() {
		int[][] tempMatrix = Arrays.stream(_matrix).map(r -> r.clone()).toArray(int[][]::new);

		for (int i = 1; i < tempMatrix.length - 1; i++) {
			for (int j = 1; j < tempMatrix[i].length - 1; j++) {
				int wallAround = countWallAround(_matrix, i, j);
				int current = _matrix[i][j];
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

	public void Refactoring() {
		int[][] tempMatrix = Arrays.stream(_matrix).map(r -> r.clone()).toArray(int[][]::new);

		int indexer = 2;

		// Set new matrix
		for (int i = 1; i < tempMatrix.length - 1; i++) {
			for (int j = 1; j < tempMatrix[0].length - 1; j++) {
				int value = tempMatrix[i][j];
				if (value == 1)
					continue;

				int[] numbs = getNeighborsValue(tempMatrix, i, j);

				int counter = getNeighborsSum(tempMatrix, i, j);

				if (counter == 4) {
					tempMatrix[i][j] = 1;

				} else {
					int maxValue = getMaxValue(numbs);
					if (maxValue == 1) {
						setNeightbors(tempMatrix, i, j, indexer);
						tempMatrix[i][j] = indexer++;
					} else {
						tempMatrix[i][j] = maxValue;
					}
				}

			}
		}

		// Create array for count which has more same value
		int[] counterValue = new int[indexer + 1];

		// Count value into array
		for (int i = 0; i < tempMatrix.length; i++) {
			for (int j = 0; j < tempMatrix[0].length; j++) {
				int value = tempMatrix[i][j];
				counterValue[value]++;
			}
		}

		// Find Index with higher number of cells
		int maxIndexer = getMaxValueIndex(counterValue, 1);

		// Set all the remaining as wall
		for (int i = 0; i < tempMatrix.length; i++) {
			for (int j = 0; j < tempMatrix[0].length; j++) {
				int value = tempMatrix[i][j];
				if (value != maxIndexer)
					tempMatrix[i][j] = 1;
			}
		}

		// Set Matrix
		_matrix = tempMatrix;
	}

	private int[] getNeighborsValue(int[][] matrix, int row, int col) {
		int top = matrix[row - 1][col];
		int bot = matrix[row + 1][col];
		int left = matrix[row][col - 1];
		int right = matrix[row][col + 1];

		int[] values = { top, bot, left, right };
		return values;
	}

	private int getNeighborsSum(int[][] matrix, int row, int col) {
		int top = matrix[row - 1][col];
		int bot = matrix[row + 1][col];
		int left = matrix[row][col - 1];
		int right = matrix[row][col + 1];

		int counter = top + bot + left + right;
		return counter;
	}

	private void setNeightbors(int[][] matrix, int row, int col, int newValue) {
		int top = matrix[row - 1][col];
		int bot = matrix[row + 1][col];
		int left = matrix[row][col - 1];
		int right = matrix[row][col + 1];

		if (top == 0) {
			matrix[row - 1][col] = newValue;
			setNeightbors(matrix, row - 1, col, newValue);
		}
		if (bot == 0) {
			matrix[row + 1][col] = newValue;
			setNeightbors(matrix, row + 1, col, newValue);
		}
		if (left == 0) {
			matrix[row][col - 1] = newValue;
			setNeightbors(matrix, row, col - 1, newValue);
		}
		if (right == 0) {
			matrix[row][col + 1] = newValue;
			setNeightbors(matrix, row, col + 1, newValue);
		}
	}

	private int getMaxValue(int[] numbers) {
		int maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	private int getMaxValueIndex(int[] numbers, int skipIndex) {
		int maxValue = numbers[0];
		int maxIndex = 0;
		for (int i = 0; i < numbers.length; i++) {
			if (i == skipIndex)
				continue;
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	public void Build() {
		for (int i = 0; i < _epoque; i++) {
			_matrix = Moore();
		}
	}

	public void Print() {
		for (int i = 0; i < _matrix.length; i++) {
			for (int j = 0; j < _matrix[0].length; j++) {
				if (_matrix[i][j] == 1)
					System.out.print("1");
				else {
					System.out.print(" ");

				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public void Print(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 1)
					System.out.print(" ");
				else
					System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
