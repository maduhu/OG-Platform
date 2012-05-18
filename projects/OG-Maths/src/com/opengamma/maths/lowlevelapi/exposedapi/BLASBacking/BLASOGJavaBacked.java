/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.lowlevelapi.exposedapi.BLASBacking;

/**
 * 
 */
public class BLASOGJavaBacked extends BLASAbstractSuper implements BLASAPIInterface {

  @Override
  public void drotg(double a, double b, double c, double s) {
  }

  @Override
  public void drotmg(double dd1, double dd2, double dx1, double dy2, double[] dPARAM) {
  }

  @Override
  public void drot(int n, double[] x, int incx, double[] y, int incy, double c, double s) {
  }

  @Override
  public void drotm(int n, double[] x, int incx, double[] y, int incy, double[] dPARAM) {
  }

  @Override
  public void dswap(int n, double[] x, int incx, double[] y, int incy) {
  }

  @Override
  public void dscal(int n, double alpha, double[] x, int incx) {
  }

  @Override
  public void dcopy(int n, double[] x, int incx, double[] y, int incy) {
  }

  @Override
  public void daxpy(int n, double alpha, double[] x, int incx, double[] y, int incy) {
  }

  @Override
  public double ddot(int n, double[] x, int incx, double[] y, int incy) {
    return 0;
  }

  @Override
  public double dnrm2(int n, double[] x, int incx) {
    return 0;
  }

  @Override
  public double dasum(int n, double[] x, int incx) {
    return 0;
  }

  @Override
  public int idamax(int n, double[] x, int incx) {
    return 0;
  }

  @Override
  public void dgemv(char trans, int m, int n, double alpha, double[] aMatrix, int lda, double[] x, int incx, double beta, double[] y, int incy) {
  }

  @Override
  public void dgemm(char transa, char transb, int m, int n, int k, double alpha, double[] aMatrix, int lda, double[] bMatrix, int ldb, double beta, double[] cMatrix, int ldc) {
  }



}
