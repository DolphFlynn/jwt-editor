package burp;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.persistence.Preferences;
import burp.api.montoya.proxy.Proxy;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.utilities.ByteUtils;
import com.blackberry.jwteditor.model.KeysModel;
import com.blackberry.jwteditor.model.config.ProxyConfig;
import com.blackberry.jwteditor.model.persistence.BurpKeysModelPersistence;
import com.blackberry.jwteditor.model.persistence.KeysModelPersistence;
import com.blackberry.jwteditor.model.persistence.ProxyConfigPersistence;
import com.blackberry.jwteditor.presenter.PresenterStore;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.BurpView;
import com.blackberry.jwteditor.view.RequestEditorView;
import com.blackberry.jwteditor.view.ResponseEditorView;
import com.blackberry.jwteditor.view.RstaFactory;
import com.blackberry.jwteditor.view.RstaFactory.BurpThemeAwareRstaFactory;

import java.awt.*;

import static burp.api.montoya.ui.editor.extension.EditorMode.READ_ONLY;

/**
 * Burp extension main class
 */
@SuppressWarnings("unused")
public class JWTEditorExtension implements BurpExtension {
    private PresenterStore presenters;
    private Window suiteWindow;
    private RstaFactory rstaFactory;

    public void initialize(MontoyaApi api) {
        presenters = new PresenterStore();

        api.extension().setName(Utils.getResourceString("tool_name"));

        Preferences preferences = api.persistence().preferences();
        KeysModelPersistence keysModelPersistence = new BurpKeysModelPersistence(preferences);
        KeysModel keysModel = keysModelPersistence.loadOrCreateNew();

        ProxyConfigPersistence proxyConfigPersistence = new ProxyConfigPersistence(preferences);
        ProxyConfig proxyConfig = proxyConfigPersistence.loadOrCreateNew();

        UserInterface userInterface = api.userInterface();
        suiteWindow = userInterface.swingUtils().suiteFrame();

        rstaFactory = new BurpThemeAwareRstaFactory(userInterface, api.logging());

        BurpView burpView = new BurpView(
                suiteWindow,
                presenters,
                keysModelPersistence,
                keysModel,
                rstaFactory,
                proxyConfigPersistence,
                proxyConfig,
                userInterface
        );

        userInterface.registerSuiteTab(burpView.getTabCaption(), burpView.getUiComponent());

        userInterface.registerHttpRequestEditorProvider((httpRequestResponse, editorMode) ->
                new RequestEditorView(
                        suiteWindow,
                        presenters,
                        rstaFactory,
                        editorMode != READ_ONLY
                )
        );

        userInterface.registerHttpResponseEditorProvider((httpRequestResponse, editorMode) ->
                new ResponseEditorView(
                        suiteWindow,
                        presenters,
                        rstaFactory,
                        editorMode != READ_ONLY
                )
        );

        Proxy proxy = api.proxy();
        ByteUtils byteUtils = api.utilities().byteUtils();

        ProxyMessageHandler proxyMessageHandler = new ProxyMessageHandler(proxyConfig, byteUtils);
        proxy.registerRequestHandler(proxyMessageHandler);
        proxy.registerResponseHandler(proxyMessageHandler);
    }
}
