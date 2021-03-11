package com.example.app;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;
import org.mozilla.geckoview.WebExtension;

public class MainActivity extends AppCompatActivity {

    private GeckoView geckoView;
    private static GeckoRuntime runtime;
    private GeckoSession geckoSession;
    private static WebExtension.Port mPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geckoView = findViewById(R.id.gecko_v);
        findViewById(R.id.test_evaluateJavascript).setOnClickListener(v -> {

            //test evaluateJavascript
            evaluateJavascript("document.body.style.width = '100px'");
            //            evaluateJavascript("document.cookie");
            // test js call native
//            evaluateJavascript("window.JSBridge.postMessage('from web html')");
        });
        if (runtime == null) {
            runtime = GeckoRuntime.create(this);
            runtime.getSettings().setRemoteDebuggingEnabled(true);
            installExtension();
        }
        geckoSession = new GeckoSession();
        geckoSession.open(runtime);
        geckoView.setSession(geckoSession);

        geckoSession.loadUri("https://mobile.vipkid.com.cn/");

    }

    void installExtension() {
        runtime.getWebExtensionController()
                .ensureBuiltIn("resource://android/assets/messaging/", "messaging@example.com")
                .accept(
                        extension -> {
                            Log.i("MessageDelegate", "Extension installed: " + extension);
                            extension.setMessageDelegate(mMessagingDelegate, "browser");
                        },
                        e -> Log.e("MessageDelegate", "Error registering WebExtension", e)
                );
    }

    private final WebExtension.MessageDelegate mMessagingDelegate = new WebExtension.MessageDelegate() {

        @Nullable
        @Override
        public void onConnect(@NonNull WebExtension.Port port) {
            Log.e("MessageDelegate", "onConnect");
            mPort = port;
            mPort.setDelegate(mPortDelegate);
        }
    };
    private final WebExtension.PortDelegate mPortDelegate = new WebExtension.PortDelegate() {
        @Override
        public void onPortMessage(final @NonNull Object message,
                                  final @NonNull WebExtension.Port port) {
            Log.e("MessageDelegate", "Received message from extension: "
                    + message);
            try {
                if (message instanceof JSONObject) {
                    Log.e("MessageDelegate", "Received JSONObject");
                    JSONObject jsonObject = (JSONObject) message;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnect(final @NonNull WebExtension.Port port) {
            Log.e("MessageDelegate:", "onDisconnect");
            if (port == mPort) {
                mPort = null;
            }
        }
    };

    public void evaluateJavascript(String javascriptString) {
        try {
            long id = System.currentTimeMillis();
            Log.e("evalJavascript:id:", id + "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "evalJavascript");
            jsonObject.put("data", javascriptString);
            jsonObject.put("id", id);
            Log.e("evalJavascript:", jsonObject.toString());
            mPort.postMessage(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}