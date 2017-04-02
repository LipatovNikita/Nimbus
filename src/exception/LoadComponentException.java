package exception;

public class LoadComponentException extends Exception{
    private int load;

    public int getLoad() {
        return load;
    }
    public void setLoad(int load) {
        this.load = load;
    }

    public LoadComponentException(){
        super();
    }

    public LoadComponentException(String message){
        super(message);
    }

    public LoadComponentException(int load){
        super("Компонент не может быть создан, так как виртуальный сервер загружен на "+load
                +"%\nСоздайте компонент на другом виртуальном сервере или разгрузите данный сервер");
    }
}
