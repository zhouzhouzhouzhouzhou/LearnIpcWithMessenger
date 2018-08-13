package com.example.admin.learnipcwithmessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";


    public static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageConstants.MESSAGE_FROM_CLIENT:
                    Log.i(TAG, "我收到了一天信息哦: "+msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","我收到你发的消息了，我一会回复~");
                    Message replyMessage = Message.obtain(null,MessageConstants.MESSAGE_FROM_SERVICE);
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    public final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
