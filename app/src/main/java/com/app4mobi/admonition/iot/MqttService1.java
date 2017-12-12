package com.app4mobi.admonition.iot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/*
*
* MQTT Setting up in Android
* Handles Connection, Publish, Subscribe, Un-subscribe and Disconnect of MQTT server
* --- Look into following link to get an Idea of MQTT work flow ---
* Ref :- https://www.hivemq.com/blog/mqtt-client-library-enyclopedia-paho-android-service
*
* */
public class MqttService1 extends Service {
    private static final String TAG = "MQTT Service";
    private MqttAndroidClient client;
    private IMqttToken token;
    private MqttConnectOptions options;

    public MqttService1() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        doConnect();
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void doConnect() {
        String clientId = MqttClient.generateClientId();
        //<tcp>://<server>:<port> <==>  tcp://m14.cloudmqtt.com:15997
        String serverUrl = "tcp://m14.cloudmqtt.com:15997";//tcp://broker.hivemq.com:1883
        client = new MqttAndroidClient(this.getApplicationContext(), serverUrl, clientId);
        try {
/*Start - Connect with MQTT 3.1.1*/
            //options = new MqttConnectOptions();
            //options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            //IMqttToken token = client.connect(options);
/*Start - Connect with Username / Password*/
            //options.setUserName("USERNAME");
            //options.setPassword("PASSWORD".toCharArray());
            //IMqttToken token = client.connect(options);
            /*End - Connect with Username / Password*/
/*End - Connect with MQTT 3.1.1*/
            token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
            //            publishMsg();
            //
            //            subscribeMsg();
            //
            //            unSubscribeMsg();
            //
            //            disConnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void disConnect() {
         /*Start - Disconnect*/
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        /*End - Disconnect*/
    }

    private void unSubscribeMsg() {
        /*Start - Unsubscribe*/
        final String topic = "foo/bar";
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        /*End - Unsubscribe*/
    }

    private void subscribeMsg() {
        /*Start - Subscribe*/
        String topicSub = "foo/bar";
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topicSub, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
            /*End - Subscribe*/
    }

    private void publishMsg() {
         /*Start - Publish a retained message*/
        String topicSend = "foo/bar";
        String payload = "the payload";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(topicSend, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
            /*End - Publish a retained message*/
    }
}
