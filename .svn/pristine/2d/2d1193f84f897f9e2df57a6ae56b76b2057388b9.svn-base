package gui;

/**
 * Piece class, has an int array
 * 
 * @author Jakub
 * @version 12/8/16
 */
public class Piece {

	private int[][] pieceType = new int[5][5];

	private int[] pointOfReference = new int[2];

	/**
	 * Returns the int array for the piece
	 * 
	 * @return the int array for the piece
	 */
	public int[][] getPieceType() {
		return pieceType;
	}

	/**
	 * Sets the int array for the piece
	 * 
	 * @param pieceType
	 *            The int array for the piece
	 */
	public void setPieceType(int[][] pieceType) {
		this.pieceType = pieceType;
	}

	/**
	 * Sets the reference point
	 * 
	 * @param pointOfReference
	 *            A int array of length 2, 0 is y, 1 is x
	 */
	public void setPointOfReference(int[] pointOfReference) {
		this.pointOfReference[0] = pointOfReference[0];
		this.pointOfReference[1] = pointOfReference[1];
	}

	/**
	 * Gets the reference point
	 * 
	 * @return The reference point array
	 */
	public int[] getPointOfReference() {
		return pointOfReference;
	}

	/**
	 * Rotates the piece to the left, counter clockwise
	 */
	public void rotateLeft() {
		int[][] tempPiece = new int[5][5];

		for (int i = 0; i < pieceType.length; i++) {
			for (int j = 0; j < pieceType[i].length; j++) {
				tempPiece[4 - j][i] = pieceType[i][j];
			}
		}

		int temp1 = pointOfReference[0];
		int temp2 = pointOfReference[1];

		pointOfReference[1] = temp1;
		pointOfReference[0] = 4 - temp2;

		pieceType = tempPiece;

	}

	/**
	 * Rotates the piece to the right, clockwise
	 */
	public void rotateRight() {
		int[][] tempPiece = new int[5][5];

		for (int i = 0; i < pieceType.length; i++) {
			for (int j = 0; j < pieceType[i].length; j++) {
				tempPiece[j][4 - i] = pieceType[i][j];
			}
		}

		int temp1 = pointOfReference[0];
		int temp2 = pointOfReference[1];

		pointOfReference[1] = 4 - temp1;
		pointOfReference[0] = temp2;

		pieceType = tempPiece;
	}

	/**
	 * Flips the piece vertically
	 */
	public void flip() {
		int[][] tempPiece = new int[5][5];

		for (int i = 0; i < pieceType.length; i++) {
			for (int j = 0; j < pieceType[i].length; j++) {
				tempPiece[4 - i][j] = pieceType[i][j];
			}
		}

		int temp1 = pointOfReference[0];
		int temp2 = pointOfReference[1];

		pointOfReference[0] = 4 - temp1;
		pointOfReference[1] = temp2;

		pieceType = tempPiece;
	}

	/**
	 * Finds the left most part of the piece
	 * 
	 * @return
	 */
	public int findLeftMost() {

		int left = 5;
		for (int i = 0; i < pieceType.length; i++) {
			for (int j = 0; j < pieceType[i].length; j++) {
				if (pieceType[i][j] == 1 && j < left) {
					left = j;
				}
			}
		}
		return left;
	}

	/**
	 * Finds the right most part of the piece
	 * 
	 * @return
	 */
	public int findRightMost() {

		int right = -1;
		for (int i = 0; i < pieceType.length; i++) {
			for (int j = 0; j < pieceType[i].length; j++) {
				if (pieceType[i][j] == 1 && j > right) {
					right = j;
				}
			}
		}
		return right;
	}

	/**
	 * Finds the top most part of the piece
	 * 
	 * @return
	 */
	public int findTopMost() {

		int top = 5;
		for (int i = 0; i < pieceType.length; i++) {
			for (int j = 0; j < pieceType[i].length; j++) {
				if (pieceType[i][j] == 1 && i < top) {
					top = i;
				}
			}
		}
		return top;
	}

	/**
	 * Finds the bottom most part of the piece
	 * 
	 * @return
	 */
	public int findBottomMost() {

		int bottom = -1;
		for (int i = 0; i < pieceType.length; i++) {
			for (int j = 0; j < pieceType[i].length; j++) {
				if (pieceType[i][j] == 1 && i > bottom) {
					bottom = i;
				}
			}
		}
		return bottom;
	}

}
