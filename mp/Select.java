package mp;
/*
@author  j.n.magee 20/11/96
*/


import java.util.*;

/*********************mp.Select*****************************/
// implements choice
class Select {
    Vector<Selectable> list = new Vector<Selectable>(2);

    public void add(Selectable s) {
        list.addElement(s);
        s.setSelect(this);
    }

    private void clearAll() {
        for (Enumeration e = list.elements(); e.hasMoreElements();){
            ((Selectable)e.nextElement()).clearOpen();
        }
    }

    private void openAll() {
        for (Enumeration e = list.elements(); e.hasMoreElements();){
            Selectable s = (Selectable)e.nextElement();
            if (s.testGuard()) s.setOpen();
        }
    }

    private int testAll() {
        int i = 0;
        int j = 1;
        for (Enumeration e = list.elements(); e.hasMoreElements() && i==0; ++j){
            Selectable s = (Selectable)e.nextElement();
            if (s.testReady() && s.testGuard()) i=j;
        }
        return i;
    }

    public synchronized int choose() throws InterruptedException {
        int readyIndex = 0;
        while (readyIndex==0) {
            readyIndex=testAll();
            if (readyIndex==0) {
                openAll();
                wait();
                clearAll();
            }
        }
        return readyIndex;
    }
}




