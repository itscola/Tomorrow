package tomorrow.tomo.utils.irc;

public class ClientThread extends Thread{
    @Override
    public void run(){

        while (true){
            Client.handle();
        }
    }
}
