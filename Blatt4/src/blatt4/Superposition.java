package blatt4;

import cern.colt.matrix.*;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.SingularValueDecomposition;

/**
 *
 * @author harrert
 */
public class Superposition {
    
    private DoubleMatrix2D getCentroid(DoubleMatrix2D matrix){
        DoubleMatrix2D m = newMatrix();
        double x=0,y=0,z=0;
        int n = matrix.rows();
        for (int i = 0; i < n; i++) {
            x += matrix.get(i, 0);
            y += matrix.get(i, 1);
            z += matrix.get(i, 2);
        }
        m.assign(new double[][]{new double[]{x/n}, new double[]{y/n}, new double[]{z/n}});
        return m;
    }
    
    private void translate(DoubleMatrix2D matrix, double[] centroid){
        for (int i = 0; i < matrix.rows(); i++) {
            matrix.set(i, 0, matrix.get(i, 0)+centroid[0]);
            matrix.set(i, 1, matrix.get(i, 1)+centroid[1]);
            matrix.set(i, 2, matrix.get(i, 2)+centroid[2]);
        }
    }
    
    private DoubleMatrix2D newMatrix(){
        return new DoubleMatrix2D() {

            @Override
            public double getQuick(int i, int i1) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public DoubleMatrix2D like(int i, int i1) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public DoubleMatrix1D like1D(int i) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected DoubleMatrix1D like1D(int i, int i1, int i2) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setQuick(int i, int i1, double d) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected DoubleMatrix2D viewSelectionLike(int[] ints, int[] ints1) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
    
    private DoubleMatrix2D newMatrix(double[][] array){
        DoubleMatrix2D m = this.newMatrix();
        m.assign(array);
        return m;
    }
    
    private double initError(DoubleMatrix2D cP, DoubleMatrix2D cQ){
        double error = 0;
//        Algebra alg = new Algebra();
//        DoubleMatrix2D cP = alg.mult(centroid, p);
//        DoubleMatrix2D cQ = alg.mult(centroid, q);
        for (int i = 0; i < cP.rows(); i++) {
            error += Math.pow(cP.get(i, 0),2);
            error += Math.pow(cP.get(i, 1),2);
            error += Math.pow(cP.get(i, 2),2);
        }
        for (int i = 0; i < cQ.rows(); i++) {
            error += Math.pow(cQ.get(i, 0), 2);
            error += Math.pow(cQ.get(i, 1), 2);
            error += Math.pow(cQ.get(i, 2), 2);
        }
        return error;
    }
    
    private DoubleMatrix2D covarMatrix(DoubleMatrix2D p, DoubleMatrix2D q){
        Algebra alg = new Algebra();
        return alg.mult(alg.transpose(p), q);
    }
    
    private DoubleMatrix2D rotate(DoubleMatrix2D covarMatrix){
        SingularValueDecomposition svd = new SingularValueDecomposition(covarMatrix);
        Algebra alg = new Algebra();
        DoubleMatrix2D U = svd.getU();
        DoubleMatrix2D V = svd.getV();
        double d = alg.det(V) * alg.det(U);
        DoubleMatrix2D diagMatrix = newMatrix();
        diagMatrix.assign(new double[][]{new double[]{1,0,0}, new double[]{0,1,0}, new double[]{0,0,d}});
        U = alg.mult(U, diagMatrix);
        return alg.mult(U, alg.transpose(V));
    }
    
    private double RMSD(DoubleMatrix2D covarMatrix, double E0, double L){
        SingularValueDecomposition svd = new SingularValueDecomposition(covarMatrix);
        Algebra alg = new Algebra();
        DoubleMatrix2D S = svd.getS();
        DoubleMatrix2D U = svd.getU();
        DoubleMatrix2D V = svd.getV();
        double d = alg.det(V) * alg.det(U);
        DoubleMatrix2D diagMatrix = newMatrix();
        diagMatrix.assign(new double[][]{new double[]{1,0,0}, new double[]{0,1,0}, new double[]{0,0,d}});
        S = alg.mult(S, diagMatrix);
        double error = (S.get(0, 0) + S.get(1, 1) + S.get(2, 2));
        return Math.sqrt((Math.abs(E0 - (2*error)))/L);
    }
    
    private DoubleMatrix2D T(DoubleMatrix2D R, DoubleMatrix2D c_p, DoubleMatrix2D c_q){
        Algebra alg = new Algebra();
        DoubleMatrix2D T = alg.mult(R, c_q);
        T.assign(new double[][]{new double[]{T.get(0, 0)-c_p.get(0, 0)}, new double[]{T.get(0, 1)-c_p.get(0, 1)}, new double[]{T.get(0, 2)-c_p.get(0, 2)}});
        return T;
    }
    
    private DoubleMatrix2D move_Q_onto_P(DoubleMatrix2D Q, DoubleMatrix2D R, DoubleMatrix2D T){
        for (int i = 0; i < Q.rows(); i++) {
            for (int j = 0; j < 3; j++) {
                Q.set(i, j, Q.get(i, 0) * R.get(0, j) + Q.get(i, 1) * R.get(1, j) + Q.get(i, 2) * R.get(2, j) + T.get(0, j));
            }
        }
        return Q;
    }
}
