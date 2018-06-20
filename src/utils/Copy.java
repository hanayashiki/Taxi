package utils;

public class Copy {
    public static <T> void copy2d(T[][] mat, T[][] newMat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                newMat[i][j] = mat[i][j];
            }
        }
    }
}
