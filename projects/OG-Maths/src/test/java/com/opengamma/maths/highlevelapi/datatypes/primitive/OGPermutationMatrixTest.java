/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.datatypes.primitive;

import java.util.Arrays;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import com.opengamma.maths.commonapi.exceptions.MathsExceptionIllegalArgument;
import com.opengamma.maths.commonapi.exceptions.MathsExceptionNullPointer;

/**
 * Tests the OGPermutation Array class
 */
public class OGPermutationMatrixTest {

  int[] data = new int[] {3, 1, 2, 4, 0 };
  int[][] fullData = new int[][] { {0, 0, 0, 1, 0 }, {0, 1, 0, 0, 0 }, {0, 0, 1, 0, 0 }, {0, 0, 0, 0, 1 }, {1, 0, 0, 0, 0 } };

  // sending in null ptr int[] constructor
  @Test(expectedExceptions = MathsExceptionNullPointer.class)
  public void testintPtrConstructorNullPtrTest() {
    int[] tmp = null;
    new OGPermutationMatrix(tmp);
  }

  // sending in out of range int[] constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testintPtrConstructorBadRangeTest() {
    int[] tmp = new int[] {1, 2, 3, 999 };
    new OGPermutationMatrix(tmp);
  }

  // sending in repeat index int[] constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testintPtrConstructorRepeatedIndexIsBadTest() {
    int[] tmp = new int[] {1, 1, 2, 3, 3 };
    new OGPermutationMatrix(tmp);
  }

  // sending in repeat index int[] constructor
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testintPtrConstructorNegIndexIsBadTest() {
    int[] tmp = new int[] {1, -1, 2, 3, 3 };
    new OGPermutationMatrix(tmp);
  }

  // sending in ok index int[] constructor
  @Test
  public void testintPtrConstructorDataOKTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    assertTrue(D.getClass() == OGPermutationMatrix.class);
    assertTrue(Arrays.equals(D.getIntData(), data));
    assertTrue(D.getNumberOfRows() == 5);
    assertTrue(D.getNumberOfColumns() == 5);
  }

  // test get rows
  @Test
  public void testGetRowsTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    assertTrue(D.getNumberOfRows() == 5);
  }

  // test get cols
  @Test
  public void testGetColumnsTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    assertTrue(D.getNumberOfColumns() == 5);
  }

  // test get data
  @Test
  public void testGetDataTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    assertTrue(Arrays.equals(D.getIntData(), data));
  }

  // test get entry bad index count
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetEntryBadIndexCountTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getEntry(1, 2, 3);
  }

  // test get entry bad row index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetEntryBadRowIndicesTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getEntry(23, 1);
  }

  // test get entry bad row index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetEntryBadColumnIndicesTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getEntry(1, 23);
  }

  // test get entry ok
  @Test
  public void testGetEntryOKIndicesTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    for (int i = 0; i < D.getNumberOfRows(); i++) {
      for (int j = 0; j < D.getNumberOfColumns(); j++) {
        assertTrue(D.getEntry(i, j) == fullData[i][j]);
      }
    }
  }

  // test get col neg index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetColNegIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getColumn(-1);
  }

  // test get col bad index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetColumnBadIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getColumn(23);
  }

  // test get col ok
  @Test
  public void testGetColumnOkIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    OGArray<? extends Number> col = D.getColumn(3);
    double[][] tmp = new double[][] { {1 }, {0 }, {0 }, {0 }, {0 } };
    OGMatrix answer = new OGMatrix(tmp);
    assertTrue(col.equals(answer));
  }
  
  
  // test get cols null index
  @Test(expectedExceptions = MathsExceptionNullPointer.class)
  public void testGetColumnsNullTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getColumns(null);
  }

  // test get cols neg index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetColumnsNegIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getColumns(-1);
  }

  // test get cols bad index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetColumnsBadIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getColumns(23);
  }

  // test get cols ok
  @Test
  public void testGetColumnsOkIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    OGArray<? extends Number> col = D.getColumns(2, 0);
    OGMatrix getCol = new OGMatrix(new double[][] { {0., 0. }, {0., 0. }, {1., 0. }, {0., 0. }, {0., 1. }});
    assertTrue(col.equals(getCol));
  }

  
  // test get row neg index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetRowNegIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getRow(-1);
  }

  // test get row bad index
  @Test(expectedExceptions = MathsExceptionIllegalArgument.class)
  public void testGetRowBadIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.getRow(23);
  }

  // test get row ok
  @Test
  public void testGetRowOkIndexTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    OGArray<? extends Number> row = D.getRow(1);
    double[][] tmp = new double[][] { {0 , 1 , 0 , 0 , 0 } };
    OGMatrix answer = new OGMatrix(tmp);
    assertTrue(row.equals(answer));
  }

  // test equals obj points to obj
  @Test
  public void testEqualsObjeqObj() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    assertTrue(D.equals(D));
  }

  // test equals obj not = null
  @Test
  public void testEqualsObjNull() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    assertFalse(D.equals(null));
  }

  // test equals obj class different
  @Test
  public void testEqualsObjDifferentClass() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    assertFalse(D.equals(new OGMatrix(1)));
  }

  // test equals obj class ok, diff cols
  @Test
  public void testEqualsObjDifferentCols() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    OGPermutationMatrix Diff = new OGPermutationMatrix(new int[] {0 });
    assertFalse(D.equals(Diff));
  }

  // test equals obj class ok, same cols same rows different data
  @Test
  public void testEqualsObjDifferentData() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    OGPermutationMatrix Diff = new OGPermutationMatrix(new int[] {0, 1, 2, 3, 4 });
    assertFalse(D.equals(Diff));
  }

  // test equals obj class ok, same cols same rows same data
  @Test
  public void testEqualsObjStructurallyIdentical() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    OGPermutationMatrix Diff = new OGPermutationMatrix(new int[] {3, 1, 2, 4, 0 });
    assertTrue(D.equals(Diff));
  }

  // test hash code
  @Test
  public void testHashCodeTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.hashCode();
  }

  // test toString code
  @Test
  public void testToStringTest() {
    OGPermutationMatrix D = new OGPermutationMatrix(data);
    D.toString();
  }

}
