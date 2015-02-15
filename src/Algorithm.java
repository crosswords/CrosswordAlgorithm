import controlers.Generator;

/**
 * Created by Tomek on 2014-12-25.
 */
public class Algorithm {

        public static char[][] generate(String[] args) {
            char[][] matrix=null;
            int maxSizeVertical = Integer.parseInt(args[1]);
            int maxSizeHorizontal = Integer.parseInt(args[0]);

            Generator gen = new Generator(maxSizeHorizontal, maxSizeVertical);
            gen.generate();
            matrix = gen.getMatrix();
            for (int y = 0; y < maxSizeVertical; y++) {
                for (int x = 0; x < maxSizeHorizontal; x++) {
                    if (matrix[x][y] == '\u0000') System.out.print(' ');
                    else
                        System.out.print(matrix[x][y]);
                }

                System.out.println();
            }
            return matrix;
        }
}
