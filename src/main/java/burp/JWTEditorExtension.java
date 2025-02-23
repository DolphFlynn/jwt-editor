package burp;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.intruder.Intruder;
import burp.api.montoya.persistence.PersistedObject;
import burp.api.montoya.persistence.Preferences;
import burp.api.montoya.proxy.Proxy;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.utilities.ByteUtils;
import burp.config.BurpConfig;
import burp.config.BurpConfigPersistence;
import burp.intruder.JWSPayloadProcessor;
import burp.persistence.TokensIdGeneratorPersistence;
import burp.persistence.TokensModelPersistence;
import burp.proxy.ProxyConfig;
import burp.proxy.ProxyHttpMessageHandler;
import burp.proxy.ProxyWsMessageHandler;
import burp.scanner.JWSHeaderInsertionPointProvider;
import com.blackberry.jwteditor.model.keys.KeysModel;
import com.blackberry.jwteditor.model.persistence.BurpKeysModelPersistence;
import com.blackberry.jwteditor.model.persistence.KeysModelPersistence;
import com.blackberry.jwteditor.model.tokens.TokenIdGenerator;
import com.blackberry.jwteditor.model.tokens.TokensModel;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.SuiteView;
import com.blackberry.jwteditor.view.editor.HttpRequestEditorView;
import com.blackberry.jwteditor.view.editor.HttpResponseEditorView;
import com.blackberry.jwteditor.view.editor.InformationPanelFactory;
import com.blackberry.jwteditor.view.editor.WebSocketEditorView;
import com.blackberry.jwteditor.view.hexcodearea.HexCodeAreaFactory;
import com.blackberry.jwteditor.view.rsta.RstaFactory;

import java.awt.*;

import static burp.api.montoya.core.BurpSuiteEdition.COMMUNITY_EDITION;
import static burp.api.montoya.core.BurpSuiteEdition.PROFESSIONAL;
import static burp.api.montoya.ui.editor.extension.EditorMode.READ_ONLY;

@SuppressWarnings("unused")
public class JWTEditorExtension implements BurpExtension {

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName(Utils.getResourceString("tool_name"));

        Preferences preferences = api.persistence().preferences();
        KeysModelPersistence keysModelPersistence = new BurpKeysModelPersistence(preferences);
        KeysModel keysModel = keysModelPersistence.loadOrCreateNew();

        BurpConfigPersistence burpConfigPersistence = new BurpConfigPersistence(preferences);
        BurpConfig burpConfig = burpConfigPersistence.loadOrCreateNew();

        UserInterface userInterface = api.userInterface();
        Window suiteWindow = userInterface.swingUtils().suiteFrame();

        RstaFactory rstaFactory = new RstaFactory(userInterface, api.logging());

        boolean isProVersion = api.burpSuite().version().edition() == PROFESSIONAL;

        PersistedObject extensionData = isProVersion ? api.persistence().extensionData() : null;

        TokensModelPersistence tokensModelPersistence = new TokensModelPersistence(isProVersion, extensionData);
        TokensModel tokensModel = tokensModelPersistence.loadOrCreateNew();

        TokensIdGeneratorPersistence tokensIdGeneratorPersistence = new TokensIdGeneratorPersistence(isProVersion, extensionData);
        TokenIdGenerator tokenIdGenerator = tokensIdGeneratorPersistence.loadOrCreateNew();

        SuiteView suiteView = new SuiteView(
                suiteWindow,
                keysModelPersistence,
                keysModel,
                tokensModel,
                rstaFactory,
                burpConfig,
                userInterface,
                isProVersion
        );

        userInterface.registerSuiteTab(suiteView.getTabCaption(), suiteView.getUiComponent());

        HexCodeAreaFactory hexAreaCodeFactory = new HexCodeAreaFactory(api.logging(), api.userInterface());
        InformationPanelFactory informationPanelFactory = new InformationPanelFactory(api.userInterface(), api.logging());

        userInterface.registerHttpRequestEditorProvider(editorCreationContext ->
                new HttpRequestEditorView(
                        keysModel,
                        tokensModel,
                        tokenIdGenerator,
                        rstaFactory,
                        api.collaborator().defaultPayloadGenerator(),
                        hexAreaCodeFactory,
                        api.logging(),
                        informationPanelFactory,
                        editorCreationContext.editorMode() != READ_ONLY,
                        isProVersion
                )
        );

        userInterface.registerHttpResponseEditorProvider(editorCreationContext ->
                new HttpResponseEditorView(
                        keysModel,
                        tokensModel,
                        tokenIdGenerator,
                        rstaFactory,
                        api.collaborator().defaultPayloadGenerator(),
                        hexAreaCodeFactory,
                        api.logging(),
                        informationPanelFactory,
                        editorCreationContext.editorMode() != READ_ONLY,
                        isProVersion
                )
        );

        userInterface.registerWebSocketMessageEditorProvider(editorCreationContext ->
                new WebSocketEditorView(
                        keysModel,
                        tokensModel,
                        tokenIdGenerator,
                        rstaFactory,
                        api.collaborator().defaultPayloadGenerator(),
                        hexAreaCodeFactory,
                        api.logging(),
                        informationPanelFactory,
                        editorCreationContext.editorMode() != READ_ONLY,
                        isProVersion
                )
        );

        Proxy proxy = api.proxy();
        ProxyConfig proxyConfig = burpConfig.proxyConfig();
        ByteUtils byteUtils = api.utilities().byteUtils();

        ProxyHttpMessageHandler proxyHttpMessageHandler = new ProxyHttpMessageHandler(proxyConfig, byteUtils);
        proxy.registerRequestHandler(proxyHttpMessageHandler);
        proxy.registerResponseHandler(proxyHttpMessageHandler);

        ProxyWsMessageHandler proxyWsMessageHandler = new ProxyWsMessageHandler(proxyConfig, byteUtils);
        proxy.registerWebSocketCreationHandler(proxyWebSocketCreation ->
                proxyWebSocketCreation.proxyWebSocket().registerProxyMessageHandler(proxyWsMessageHandler)
        );

        Intruder intruder = api.intruder();
        intruder.registerPayloadProcessor(new JWSPayloadProcessor(burpConfig.intruderConfig(), api.logging(), keysModel));

        if (api.burpSuite().version().edition() != COMMUNITY_EDITION) {
            api.scanner().registerInsertionPointProvider(new JWSHeaderInsertionPointProvider(burpConfig.scannerConfig()));
        }

        api.extension().registerUnloadingHandler(() -> {
            burpConfigPersistence.save(burpConfig);
            tokensModelPersistence.save(tokensModel);
            tokensIdGeneratorPersistence.save(tokenIdGenerator);
        });
    }
}
