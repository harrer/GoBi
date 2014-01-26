package blatt4;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.SingularValueDecomposition;
import cern.jet.math.Functions;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author harrert
 */
public class Superposition {
    
    public Superposition(DoubleMatrix2D P, DoubleMatrix2D Q){
        DoubleMatrix1D centP = getCentroid(P);
        DoubleMatrix1D centQ = getCentroid(Q);
        DoubleMatrix2D cP = translate(P, centP);
        DoubleMatrix2D cQ = translate(Q, centQ);
//        System.out.println(initError(cP, cQ));
        DoubleMatrix2D covar = covarMatrix(cP, cQ);
        DoubleMatrix2D R = rotate(covar);
        DoubleMatrix1D t = T(R, centP, centQ);
        Q = move_Q_onto_P(Q, R, t);
        System.out.println(matrixToString(Q));
        System.out.println("read off RMSD: "+readOffRMSD(P, Q));
        System.out.println(RMSD(covar, initError(cP, cQ), P.rows()));
    }
    
    private DoubleMatrix1D getCentroid(DoubleMatrix2D matrix){
        DoubleMatrix1D m = new DenseDoubleMatrix1D(3);
        double x=0,y=0,z=0;
        int n = matrix.rows();
        for (int i = 0; i < n; i++) {
            x += matrix.get(i, 0);
            y += matrix.get(i, 1);
            z += matrix.get(i, 2);
        }
        m.assign(new double[]{x/n, y/n, z/n});
        return m;
    }
    
    private DoubleMatrix2D translate(DoubleMatrix2D matrix, DoubleMatrix1D centroid){
        DoubleMatrix2D out = new DenseDoubleMatrix2D(matrix.rows(), matrix.columns());
        for (int i = 0; i < matrix.rows(); i++) {
            out.set(i, 0, matrix.get(i, 0)+centroid.get(0));
            out.set(i, 1, matrix.get(i, 1)+centroid.get(1));
            out.set(i, 2, matrix.get(i, 2)+centroid.get(2));
        }
        return out;
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
        //return alg.mult(alg.transpose(p), q);
        return alg.mult(alg.transpose(p), q);
    }
    
    private DoubleMatrix2D rotate(DoubleMatrix2D covarMatrix){
        SingularValueDecomposition svd = new SingularValueDecomposition(covarMatrix);
        Algebra alg = new Algebra();
        DoubleMatrix2D U = svd.getU();
        DoubleMatrix2D V = svd.getV();
        double d = alg.det(V) * alg.det(U);
        DoubleMatrix2D diagMatrix = new DenseDoubleMatrix2D(new double[][]{new double[]{1,0,0}, new double[]{0,1,0}, new double[]{0,0,d}});
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
        DoubleMatrix2D diagMatrix = new DenseDoubleMatrix2D(new double[][]{new double[]{1,0,0}, new double[]{0,1,0}, new double[]{0,0,d}});
        S = alg.mult(S, diagMatrix);
        double error = (S.get(0, 0) + S.get(1, 1) + S.get(2, 2));
        return Math.sqrt((Math.abs(E0 - (2*error)))/L);
    }
    
    private DoubleMatrix1D T(DoubleMatrix2D R, DoubleMatrix1D c_p, DoubleMatrix1D c_q){
        DoubleMatrix1D t;
        Algebra alg = new Algebra();
        Functions f = Functions.functions;
        t = alg.mult(R, c_q);
        t.assign(f.neg);
        t.assign(c_p, f.plus);
//        DoubleMatrix1D T = alg.mult(R, c_q);
        t.assign(new double[]{t.get(0)-c_p.get(0), t.get(1)-c_p.get(1), t.get(2)-c_p.get(2)});
        return t;
    }
    
    private DoubleMatrix2D move_Q_onto_P(DoubleMatrix2D Q, DoubleMatrix2D R, DoubleMatrix1D T){
        int r = Q.rows();
        for (int i = 0; i < Q.rows(); i++) {
            for (int j = 0; j < 3; j++) {
                Q.set(i, j, Q.get(i, 0) * R.get(0, j) + Q.get(i, 1) * R.get(1, j) + Q.get(i, 2) * R.get(2, j) + T.get(j));
            }
        }
        return Q;
    }
    
    private double readOffRMSD(DoubleMatrix2D P, DoubleMatrix2D QontoP){
        double distance = 0;
        for (int i = 0; i < P.rows(); i++) {
            for (int j = 0; j < 3; j++) {
                distance += Math.pow(P.get(i, j) - QontoP.get(i, j), 2);
            }
        }
        return Math.sqrt(distance/P.rows());
    }
    
    public double gdt_ts(double l, DoubleMatrix2D covarMatrix, double E0, double L){
        return (P(1,covarMatrix,E0,L) + P(2,covarMatrix,E0,L) + P(3,covarMatrix,E0,L) + P(4,covarMatrix,E0,L))/(4*l);
    }
    
    private double P(int x, DoubleMatrix2D covarMatrix, double E0, double L){
        int c=0;
        if(RMSD(covarMatrix, E0, L) <= x) {
            c++;
        }
        return c;
    }
    
    public static void main(String[] args) throws IOException {
        DoubleMatrix2D m = new DenseDoubleMatrix2D(new double[][]{new double[]{1,2,3}, new double[]{4,5,6}, new double[]{7,8,9}});
        System.out.println(m.get(1, 0)); //Zeilen X Spalten
//        Superposition s = new Superposition();
//        String path = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/";
//        DoubleMatrix2D P = PDBParser.parseToMatrix("/Users/Tobias/Desktop/1c25000.pdb",true);//"1ewrA01.pdb"/home/h/harrert/Dokumente/GoBi/
//        DoubleMatrix2D Q = PDBParser.parseToMatrix("/Users/Tobias/Desktop/1a5t001.pdb",true);//path+"1exzB00.pdb"
        //System.out.println(s.readOffRMSD(P, Q));
    }
    
    private static String matrixToString(DoubleMatrix2D matrix){
        StringBuilder sb = new StringBuilder();
        DecimalFormat dec = new DecimalFormat("#0.000",new DecimalFormatSymbols(Locale.US));
        double[][] da = matrix.toArray();
        for (double[] ds : da) {
            for (double d : ds) {
                sb.append(dec.format(d)).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
