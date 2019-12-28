package enums;

public enum LiftStatus {
    Up(0), Down(1), Wait(2);
    
    private int index;
    
    LiftStatus(int i) {
        this.index = i;
    }
    
    public int getIndex() {
        return index;
    }
}
