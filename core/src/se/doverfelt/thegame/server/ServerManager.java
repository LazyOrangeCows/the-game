package se.doverfelt.thegame.server;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;
import se.doverfelt.thegame.net.Server;

import java.net.SocketException;

/**
 * Created by rickard on 2016-09-30.
 */
public class ServerManager extends Thread {

    private Server server;

    public ServerManager(boolean isPublic) {
        server = new Server("0.0.0.0", 30916);
        if (isPublic) {
            PortMapping desiredMapping =
                    null;
            try {
                desiredMapping = new PortMapping(
                        30916,
                        server.getAddress(),
                        PortMapping.Protocol.TCP,
                        "The-Game"
                );
            } catch (SocketException e) {
                e.printStackTrace();
            }

            UpnpService upnpService =
                new UpnpServiceImpl(
                        new PortMappingListener(desiredMapping)
                );

            upnpService.getControlPoint().search();
        }
    }

    @Override
    public void run() {

    }
}
