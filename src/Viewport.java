final class Viewport
{
   public int row;
   public int col;
   public int numRows;
   public int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }

public void shift(Functions functions, int col, int row)
   {
      this.col = col;
      this.row = row;
   }

public boolean contains(Functions functions, Point p)
   {
      return p.y >= row && p.y < row + numRows &&
         p.x >= col && p.x < col + numCols;
   }

public Point worldToViewport(Functions functions, int col, int row)
   {
      return new Point(col - this.col, row - this.row);
   }

public Point viewportToWorld(Functions functions, int col, int row)
   {
      return new Point(col + this.col, row + this.row);
   }
}
