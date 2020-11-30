package entity;

public class Pair {
    private int p1Value;
    private int p2Value;

    public Pair(int p1Value, int p2Value){
        setPlayer1Value(p1Value);
        setPlayer2Value(p2Value);
    }

    public int getPlayer1Value(){return p1Value;}

    public int getPlayer2Value(){return p2Value;}

    public void setPlayer1Value(int p1Value){this.p1Value = p1Value;}

    public void setPlayer2Value(int p2Value){this.p2Value = p2Value;}
}
