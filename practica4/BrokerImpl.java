import java.rmi.Remote;
import java.rmi.RemoteException;

public class BrokerImpl implements Broker {


    private BrokerImpl() {}

    @Override
    public void declarar_cola(String queueName) throws RemoteException {
        
    }

    @Override
    public void publicar(String queueName, String msg) throws RemoteException {

    }

    @Override
    public void consumir(String queueName, Callback callback) throws RemoteException {

    }
}