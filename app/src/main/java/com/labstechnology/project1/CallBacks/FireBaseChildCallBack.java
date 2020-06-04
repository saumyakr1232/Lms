package com.labstechnology.project1.CallBacks;

public abstract class
FireBaseChildCallBack {

    public abstract void onChildAdded(Object object);

    public abstract void onChildChanged(Object object);

    public abstract void onChildRemoved(Object object);

    public abstract void onChildMoved(Object object);

    public abstract void onCancelled(Object object);
}
