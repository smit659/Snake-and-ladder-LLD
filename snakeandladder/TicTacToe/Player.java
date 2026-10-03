package snakeandladder.TicTacToe;

public class Player {
    // Bug fix #14: fields were package-private; encapsulate via existing getters/setters
    private int id;
    private String name;
    private Character symbol;
    Player(int id, String name, Character symbol){
        this.id=id;
        this.name=name;
        this.symbol=symbol;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Character getSymbol() {
        return symbol;
    }
    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    @Override 
    public String toString() {
        return "Player:"+id+" name:"+name;
    }

    public static Builder builder() {
        return new Builder();           
    }

    static class Builder{
        private int id;
        private String name;
        private Character symbol;
        
        public Builder setId(int id) {
            this.id = id;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setSymbol(Character symbol) {
            this.symbol = symbol;
            return this;
        }
        public Player build() {
            return new Player(id, name, symbol);
        }   
    }
}
        