package burp;

import com.blackberry.jwteditor.model.KeysModel;
import com.blackberry.jwteditor.model.config.ProxyConfig;
import com.blackberry.jwteditor.model.jose.JOSEObjectPair;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.persistence.ProxyConfigPersistence;
import com.blackberry.jwteditor.presenter.PresenterStore;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.BurpView;
import com.blackberry.jwteditor.view.EditorView;
import com.blackberry.jwteditor.view.RstaFactory;
import com.blackberry.jwteditor.view.RstaFactory.BurpThemeAwareRstaFactory;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

/**
 * Burp extension main class
 */
@SuppressWarnings("unused")
public class BurpExtender implements IBurpExtender, IMessageEditorTabFactory, IHttpListener {

    private IExtensionHelpers extensionHelpers;
    private PresenterStore presenters;
    private JFrame burp_frame;
    private RstaFactory rstaFactory;
    private ProxyConfig proxyConfig;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        presenters = new PresenterStore();

        // Try to load the keystore from the Burp project
        String json = callbacks.loadExtensionSetting("com.blackberry.jwteditor.keystore");

        // If this fails (empty), create a new keystore
        KeysModel keysModel;
        if(json != null) {
            try {
                keysModel = KeysModel.parse(json);
            } catch (ParseException e) {
                keysModel = new KeysModel();
            }
        }
        else {
            keysModel = new KeysModel();
        }

        for (Frame frame : Frame.getFrames()){
            if (frame.getName().equals("suiteFrame")) {
                burp_frame = (JFrame) frame;
            }
        }

        ProxyConfigPersistence proxyConfigPersistence = new ProxyConfigPersistence(callbacks);
        proxyConfig = proxyConfigPersistence.loadOrCreateNew();

        rstaFactory = new BurpThemeAwareRstaFactory(callbacks);

        // Create the tab
        BurpView burpView = new BurpView(
                burp_frame,
                presenters,
                callbacks,
                keysModel,
                rstaFactory,
                proxyConfigPersistence,
                proxyConfig
        );

        // Save the helpers for use in the HTTP processing callback
        extensionHelpers = callbacks.getHelpers();

        // Add the Keys tab and register the message editor tab and HTTP highlighter
        callbacks.setExtensionName(Utils.getResourceString("tool_name"));
        callbacks.addSuiteTab(burpView);
        callbacks.registerMessageEditorTabFactory(this);
        callbacks.registerHttpListener(this);
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        // Highlight any messages in HTTP History that contain JWE/JWSs
        if (toolFlag == IBurpExtenderCallbacks.TOOL_PROXY && proxyConfig.highlightJWT()) {
            // Get the request or response depending on the message type
            byte[] messageBytes = messageIsRequest ? messageInfo.getRequest() : messageInfo.getResponse();

            // Extract and count JWE/JWSs from the HTTP message
            int jwsCount = 0;
            int jweCount = 0;
            for(JOSEObjectPair joseObjectPair: Utils.extractJOSEObjects(extensionHelpers.bytesToString(messageBytes))){
                if(joseObjectPair.getModified() instanceof JWS){
                    jwsCount++;
                }
                else{
                    jweCount++;
                }
            }

            // If there are JWE or JWSs in the message, highlight the entry in HTTP History and set the count in the comment
            if(jweCount + jwsCount > 0){
                messageInfo.setHighlight(proxyConfig.highlightColor().burpColor);
                messageInfo.setComment(proxyConfig.comment(jwsCount, jweCount));
            }
        }
    }

    public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable) {
        // Create a new editor view when a HTTP message in Intercept/Repeater etc contains a JWE/JWS
        return new EditorView(burp_frame, presenters, extensionHelpers, rstaFactory, editable);
    }
}
