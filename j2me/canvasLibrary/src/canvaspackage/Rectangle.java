package canvaspackage;

/**
 * This class should not be used by the user.
 * This was made in order to provide elements to the main classes of 
 * this package.
 */
public class Rectangle {
    private int x, y, height, width;
    
    public Rectangle(int x, int y, int w, int h){
        setXY(x,y); 
        setWidth(w); 
        setHeight(h);
    }
    
    public void setXY(int x, int y){
        this.x = x; 
        this.y = y;
    }
    
    public void setWidth(int w){
        this.width = w;
    }
    
    public void setHeight(int h){
        this.height = h;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeigth(){
        return this.height;
    }
    
    
    public Rectangle newRectangleWithOffsetInX(int a){
        return new Rectangle(x+a,y,width,height);
    }
    public Rectangle newRectangleWithOffsetInY(int a){
        return new Rectangle(x,y+a,width,height);
    }
    public Rectangle newRectangleWithOffsetInWidth(int a){
        return new Rectangle(x,y,width+a,height);
    }
    public Rectangle newRectangleWithOffsetInHeight(int a){
        return new Rectangle(x,y,width,height+a);
    }
    
    
    public Rectangle newRectangleWithThisX(int a){
        return new Rectangle(a,y,width,height);
    }
    public Rectangle newRectangleWithThisY(int a){
        return new Rectangle(x,a,width,height);
    }
    public Rectangle newRectangleWithThisWidth(int a){
        return new Rectangle(x,y,a,height);
    }
    public Rectangle newRectangleWithThisHeight(int a){
        return new Rectangle(x,y,width,a);
    }
    
}
