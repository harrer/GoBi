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
    
    private double initError(DoubleMatrix2D p, DoubleMatrix2D q, DoubleMatrix2D centroid){
        double error = 0;
        Algebra alg = new Algebra();
        DoubleMatrix2D cP = alg.mult(centroid, p);
        DoubleMatrix2D cQ = alg.mult(centroid, q);
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
        svd.getS();
        svd.getU();
        svd.getV();
    }
}
