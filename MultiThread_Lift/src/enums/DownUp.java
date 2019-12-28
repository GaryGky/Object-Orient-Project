package enums;

public enum DownUp {
    Up(0), Down(1);
    private int index;
    
    DownUp(int i) {
        this.index = i;
    }
    
    public int getIndex() {
        return index;
    }
}
