package piece;

public class AbstractPiece implements IPiece {
    private boolean isBlack;
    AbstractPiece(boolean isBlack) {
        this.isBlack = isBlack;
    }
    public boolean getIsBlack() {
        return this.isBlack;
    }
}
