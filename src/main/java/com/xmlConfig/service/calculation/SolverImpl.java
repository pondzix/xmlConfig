package com.xmlConfig.service.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xmlConfig.domain.Parameter;
import com.xmlConfig.exception.SolutionNotFoundException;

public class SolverImpl implements Solver{

	@Override
	public double solve(Map<Parameter, Double> equations, Parameter param) throws SolutionNotFoundException{
		int N = equations.size();
		int M = 7;
		double[] alfa;
		int[] right = param.getUnit();
		double A[][]  = new double[N][N];
		double b[] = new double[N];
		List<int[]> params = new ArrayList<>();
		for(Parameter p: equations.keySet())
			params.add(p.getUnit());
		
		int counter = 0;
		for(int i = 0; i < M; i++){
			for(int[] p: params)
				if(p[i] < 0.001)
					counter++;
		
			if(counter == M && right[i] < 0.001)
				throw new SolutionNotFoundException();		
		}
		
		
		for(int i = 0, j = 0; i < N; i++, j = 0){
			for(int[] p: params)
				A[i][j++] = p[N - i - 1];
			
			b[i] = right[N - i - 1];	
		}
			
		alfa = lsolve(A, b);
		double sum = 0;
		for(int i = N, j = 0; i < M; i++, j = 0){
			for(int[] p: params)
				sum += alfa[j] * p[i];
			
			if((sum  - right[i] > 0.001))
				throw new SolutionNotFoundException();       						
		}
		sum = 1;
		int i = 0 ;
		for(double d: equations.values())
			sum *= Math.pow(d, alfa[i++]);
			
		return sum * param.getValue();
	}
	
    private double[] lsolve(double[][] A, double[] b) throws SolutionNotFoundException {
	 	double eps = 1e-3;
        int N  = b.length;

        for (int p = 0; p < N; p++) {
            int max = p;
            for (int i = p + 1; i < N; i++) 
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) 
                    max = i;
                
           
            
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            if (Math.abs(A[p][p]) <= eps) 
                throw new SolutionNotFoundException();       

            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) 
                    A[i][j] -= alpha * A[p][j];               
            }
        }

        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) 
                sum += A[i][j] * x[j];
            
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }
	
}
