package uniza.fri.majba.dis1.ui;

import uniza.fri.majba.dis1.simulation_core.Replication;
import uniza.fri.majba.dis1.ui.callback.ReplicationCallback;

public interface CallbackReplication extends Replication {
    void setCallback(ReplicationCallback callback);
}
