package mp;
/*
@author  j.n.magee 20/11/96
*/

import java.util.*;

/* ********************mp.Port**************************** */
// The definition of channel assumes that there can be many sender
// but only one receiver

public class Port<M> extends Selectable{

    Vector<M> queue = new Vector<M>();

    public synchronized void send(M v) {
        queue.addElement(v);
        signal();
    }

    public synchronized M receive() throws InterruptedException {
        block();
        clearReady();
        M tmp = queue.elementAt(0);
        queue.removeElementAt(0);
        return(tmp);
    }
}

