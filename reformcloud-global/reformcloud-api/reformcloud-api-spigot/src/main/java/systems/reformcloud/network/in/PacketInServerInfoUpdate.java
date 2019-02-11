/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudServerInfoUpdateEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInServerInfoUpdate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());
        final InternalCloudNetwork internalCloudNetwork = configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType());

        ReformCloudAPISpigot.getInstance().setInternalCloudNetwork(internalCloudNetwork);
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(internalCloudNetwork);

        if (serverInfo.getCloudProcess().getName().equals(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()))
            ReformCloudAPISpigot.getInstance().setServerInfo(serverInfo);

        SpigotBootstrap.getInstance().getServer().getPluginManager().callEvent(new CloudServerInfoUpdateEvent(serverInfo));
    }
}