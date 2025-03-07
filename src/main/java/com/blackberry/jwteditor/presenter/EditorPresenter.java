/*
Author : Fraser Winterborn

Copyright 2021 BlackBerry Limited

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.blackberry.jwteditor.presenter;

import burp.api.montoya.collaborator.CollaboratorPayloadGenerator;
import burp.api.montoya.logging.Logging;
import com.blackberry.jwteditor.model.jose.*;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.KeyRing;
import com.blackberry.jwteditor.model.keys.KeysRepository;
import com.blackberry.jwteditor.model.tokens.Token;
import com.blackberry.jwteditor.model.tokens.TokenIdGenerator;
import com.blackberry.jwteditor.model.tokens.TokenRepository;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.dialog.MessageDialogFactory;
import com.blackberry.jwteditor.view.dialog.operations.*;
import com.blackberry.jwteditor.view.dialog.operations.SigningPanel.Mode;
import com.blackberry.jwteditor.view.editor.Attack;
import com.blackberry.jwteditor.view.editor.EditorMode;
import com.blackberry.jwteditor.view.editor.EditorView;
import com.blackberry.jwteditor.view.weak.WeakKeyAttackDialog;
import com.nimbusds.jose.util.Base64URL;
import org.json.JSONException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static com.blackberry.jwteditor.model.jose.ClaimsType.JSON;
import static com.blackberry.jwteditor.model.jose.ClaimsType.TEXT;
import static com.blackberry.jwteditor.model.jose.JWEFactory.jweFromParts;
import static com.blackberry.jwteditor.model.jose.JWSFactory.jwsFromParts;
import static com.blackberry.jwteditor.utils.Base64URLUtils.base64UrlEncodeJson;
import static com.blackberry.jwteditor.utils.JSONUtils.isJsonCompact;
import static com.blackberry.jwteditor.utils.JSONUtils.prettyPrintJSON;
import static com.blackberry.jwteditor.view.dialog.operations.SigningPanel.Mode.EMBED_JWK;
import static com.blackberry.jwteditor.view.dialog.operations.SigningPanel.Mode.NORMAL;

public class EditorPresenter {

    private final KeysRepository keysRepository;
    private final TokenRepository tokenRepository;
    private final TokenIdGenerator tokenIdGenerator;
    private final EditorView view;
    private final CollaboratorPayloadGenerator collaboratorPayloadGenerator;
    private final Logging logging;
    private final MessageDialogFactory messageDialogFactory;
    private final EditorModel model;
    private final LastSigningKeys lastSigningKeys;

    private boolean selectionChanging;

    public EditorPresenter(
            EditorView view,
            CollaboratorPayloadGenerator collaboratorPayloadGenerator,
            Logging logging,
            KeysRepository keysRepository,
            TokenRepository tokenRepository,
            TokenIdGenerator tokenIdGenerator) {
        this.view = view;
        this.collaboratorPayloadGenerator = collaboratorPayloadGenerator;
        this.logging = logging;
        this.keysRepository = keysRepository;
        this.tokenRepository = tokenRepository;
        this.tokenIdGenerator = tokenIdGenerator;
        this.model = new EditorModel();
        this.messageDialogFactory = new MessageDialogFactory(view.uiComponent());
        this.lastSigningKeys = new LastSigningKeys();
    }

    /**
     * Set the content of the editor tab by extracting and parsing JWE/JWSs from a block of text
     *
     * @param content text that may contain a serialized JWE/JWS
     */
    public void setMessage(String content) {
        model.setMessage(content);
        view.setJOSEObjects(model.getJOSEObjectStrings());
    }

    /**
     * Display a JWS in the editor
     *
     * @param jws the JWS to display
     */
    private void setJWS(JWS jws) {
        // Check if the header and payload survives compaction without changes (i.e. it was compact when deserialized)
        // If contain whitespace, don't try to pretty print, as the re-compacted version won't match the original

        Header header = jws.header();
        boolean isHeaderJsonCompact = header.isCompact();
        String jwsHeader = isHeaderJsonCompact ? header.decodeAndPrettyPrint() : header.decoded();

        view.setJWSHeader(jwsHeader);
        view.setJWSHeaderCompact(isHeaderJsonCompact);

        JWSClaims claim = jws.claims();

        switch (claim.type()) {
            case JSON -> {
                String payload = claim.decoded();
                boolean isPayloadJsonCompact = isJsonCompact(payload);
                String jwsPayload = isPayloadJsonCompact ? prettyPrintJSON(payload) : payload;

                view.setJWSPayloadCompact(isPayloadJsonCompact);
                view.setJWSPayload(jwsPayload, JSON);
            }

            case TEXT -> view.setJWSPayload(claim.decoded(), TEXT);

            default -> throw new IllegalStateException("Unsupported claim type: " + claim.type());
        }

        view.setJWSSignature(jws.signature().data());
    }

    /**
     * Convert the text/hex entry fields to a JWS
     *
     * @return the JWS built from the editor entry fields
     */
    private JWS getJWS() {
        // Get the header and payload text entry as base64. Compact the JSON if corresponding compact checkbox is ticked
        // Return the entry encoded as-is if this fails, or the corresponding compact checkbox is unticked

        Base64URL header = base64UrlEncodeJson(view.getJWSHeader(), view.getJWSHeaderCompact());
        Base64URL payload = base64UrlEncodeJson(view.getJWSPayload(), view.getJWSPayloadCompact());

        return jwsFromParts(header, payload, Base64URL.encode(view.getJWSSignature()));
    }

    /**
     * Display a JWE in the editor
     *
     * @param jwe the JWE to display
     */
    private void setJWE(JWE jwe) {
        // Check if the header survives compaction without changes (i.e. it was compact when deserialized)
        Header header = jwe.header();

        boolean isHeaderJsonCompact = header.isCompact();
        String jweHeader = isHeaderJsonCompact ? header.decodeAndPrettyPrint() : header.decoded();

        view.setJWEHeader(jweHeader);
        view.setJWEHeaderCompact(isHeaderJsonCompact);

        // Set the other JWE fields - these are all byte arrays
        view.setJWEEncryptedKey(jwe.getEncryptedKey());
        view.setJWECiphertext(jwe.getCiphertext());
        view.setJWEIV(jwe.getIV());
        view.setJWETag(jwe.getTag());
    }

    /**
     * Convert the text/hex entry fields to a JWE
     *
     * @return the JWE built from the editor entry fields
     */
    private JWE getJWE() {
        // Get the header text entry as base64. Compact the JSON if the compact checkbox is ticked
        // Return the entry encoded as-is if this fails, or the compact checkbox is unticked
        Base64URL header = base64UrlEncodeJson(view.getJWEHeader(), view.getJWEHeaderCompact());

        Base64URL encryptedKey = Base64URL.encode(view.getJWEEncryptedKey());
        Base64URL iv = Base64URL.encode(view.getJWEIV());
        Base64URL ciphertext = Base64URL.encode(view.getJWECiphertext());
        Base64URL tag = Base64URL.encode(view.getJWETag());

        return jweFromParts(header, encryptedKey, iv, ciphertext, tag);
    }

    public void perform(Attack attack) {
        switch (attack) {
            case EmbedJWK -> signingDialog(EMBED_JWK);
            case SignNone -> showDialogAndUpdateJWS(new NoneOperation());
            case KeyConfusion -> onAttackKeyConfusionClicked();
            case SignEmptyKey -> showDialogAndUpdateJWS(new EmptyKeySigningPanel());
            case SignPsychicSignature -> showDialogAndUpdateJWS(new PsychicSignaturePanel());
            case EmbedCollaboratorPayload -> showDialogAndUpdateJWS(new EmbedCollaboratorPayloadPanel(collaboratorPayloadGenerator));
            case WeakSymmetricKey -> onAttackWeakHMACSecret();
        }
    }

    private void onAttackKeyConfusionClicked() {
        // Get a list of verification capable public keys
        List<Key> attackKeys = keysRepository.getVerificationKeys().stream()
                .filter(key -> key.isPublic() && key.canConvertToPem())
                .toList();

        if (attackKeys.isEmpty()) {
            messageDialogFactory.showWarningDialog("error_title_no_signing_keys", "error_no_signing_keys");
            return;
        }

        showDialogAndUpdateJWS(new KeyConfusionAttackPanel(attackKeys, lastSigningKeys));
    }

    private void onAttackWeakHMACSecret() {
        JWS jws = getJWS();

        if (!jws.header().algorithm().startsWith("HS")) {
            messageDialogFactory.showWarningDialog("error_title_unable_to_attack_weak_symmetric", "error_unable_to_attack_weak_symmetric");
            return;
        }

        WeakKeyAttackDialog dialog = new WeakKeyAttackDialog(view.window(), logging, getJWS());
        dialog.display();
    }

    public void onSignClicked() {
        signingDialog(NORMAL);
    }

    private void signingDialog(Mode mode) {
        // Check there are signing keys in the keystore
        if (keysRepository.getSigningKeys().isEmpty()) {
            messageDialogFactory.showWarningDialog("error_title_no_signing_keys", "error_no_signing_keys");
            return;
        }

        showDialogAndUpdateJWS(new SigningPanel(keysRepository.getSigningKeys(), mode, lastSigningKeys));
    }

    private void showDialogAndUpdateJWS(Operation<JWS, JWS> operation) {
        OperationDialog<JWS, JWS> dialog = new OperationDialog<>(
                view.window(),
                logging,
                operation,
                getJWS()
        );

        dialog.display();

        JWS updatedJWS = dialog.getJWT();

        if (updatedJWS != null) {
            setJWS(updatedJWS);
        }
    }

    public void onVerifyClicked() {
        List<Key> keys = keysRepository.getVerificationKeys();

        // Check there are verification keys in the keystore
        if (keys.isEmpty()) {
            messageDialogFactory.showWarningDialog("error_title_no_verification_keys", "error_no_verification_keys");
            return;
        }

        KeyRing keyRing = new KeyRing(keys);
        Optional<Key> key = keyRing.findVerifyingKey(getJWS());

        String messageKey = key.isPresent() ? "editor_view_message_verified" : "editor_view_message_not_verified";
        Object[] args = key.map(value -> new String[]{value.getID()}).orElseGet(() -> new String[0]);

        messageDialogFactory.showWarningDialog("editor_view_message_title_verification", messageKey, args);
    }

    public void onEncryptClicked() {
        // Check there are encryption keys in the keystore
        if (keysRepository.getEncryptionKeys().isEmpty()) {
            messageDialogFactory.showWarningDialog("error_title_no_encryption_keys", "error_no_encryption_keys");
            return;
        }

        OperationDialog<JWE, JWS> encryptDialog = new OperationDialog<>(
                view.window(),
                logging,
                new EncryptPanel(keysRepository.getEncryptionKeys()),
                getJWS()
        );
        encryptDialog.display();

        // If a JWE was created by the dialog, replace the contents of the editor and change to JWE mode
        JWE jwe = encryptDialog.getJWT();

        if (jwe != null) {
            setJWE(jwe);
        }
    }

    /**
     * Handle click events from the Decrypt button
     */
    public void onDecryptClicked() {
        if (keysRepository.getDecryptionKeys().isEmpty()) {
            messageDialogFactory.showWarningDialog("error_title_no_decryption_keys", "error_no_decryption_keys");
            return;
        }

        // Attempt to decrypt the contents of the editor with all available keys
        try {
            List<Key> keys = keysRepository.getDecryptionKeys();
            Optional<JWS> jws = new KeyRing(keys).attemptDecryption(getJWE());

            // If decryption was successful, set the contents of the editor to the decrypted JWS and set the editor mode to JWS
            if (jws.isPresent()) {
                setJWS(jws.get());
            } else {
                messageDialogFactory.showWarningDialog("error_title_unable_to_decrypt", "error_decryption_all_keys_failed");
            }
        } catch (ParseException e) {
            messageDialogFactory.showWarningDialog("error_title_unable_to_decrypt", "error_decryption_invalid_header");
        }
    }

    /**
     * Handle click events from the Copy button
     */
    public void onCopyClicked() {
        Utils.copyToClipboard(view.getSerialized());
    }

    public void onSendToTokensClicked() {
        Token token = new Token(tokenIdGenerator.next(), view.getHost(), view.getPath(), getJWS());

        tokenRepository.add(token);
    }

    /**
     * Get the message set by setMessage with the changes made by the editor
     *
     * @return the altered message
     */
    public String getMessage() {
        return model.getMessage();
    }

    /**
     * Has the editor modified any of the JOSE Objects
     *
     * @return true if changes have been made in the editor
     */
    public boolean isModified() {
        return model.isModified();
    }

    /**
     * Callback called by the view whenever the dropdown selection is changed
     */
    public void onSelectionChanged() {
        // Set a selectionChanging to true, so componentChanged doesn't treat the change as a user event
        selectionChanging = true;

        // Get the JOSEObject pair corresponding to the selected dropdown entry index
        MutableJOSEObject mutableJoseObject = model.getJOSEObject(view.getSelectedJOSEObjectIndex());
        JOSEObject joseObject = mutableJoseObject.getModified();

        // Change to JWE/JWS mode based on the newly selected JOSEObject
        if (joseObject instanceof JWS) {
            setJWS((JWS) joseObject);
        } else {
            setJWE((JWE) joseObject);
        }

        // Allow user events in componentChanged again
        selectionChanging = false;
    }

    /**
     * Callback called by the view whenever the contents of a text or hex editor changes, or the compact checkbox is modified
     */
    public void componentChanged() {
        // Get the currently selected object
        MutableJOSEObject mutableJoseObject = model.getJOSEObject(view.getSelectedJOSEObjectIndex());

        //Serialize the text/hex entries to a JWS/JWE in compact form, depending on the editor mode
        JOSEObject joseObject = view.getMode() == EditorMode.JWS ? getJWS() : getJWE();
        //Update the JOSEObjectPair with the change
        mutableJoseObject.setModified(joseObject);
        //Highlight the serialized text as changed if it differs from the original, and the change wasn't triggered by onSelectionChanging
        view.setSerialized(joseObject.serialize(), mutableJoseObject.changed() && !selectionChanging);

        view.setInformation(mutableJoseObject.information());
    }

    /**
     * Handle click events from the JWE Header Format button
     */
    public void formatJWEHeader() {
        try {
            view.setJWEHeader(prettyPrintJSON(view.getJWEHeader()));
        } catch (JSONException e) {
            messageDialogFactory.showErrorDialog("error_title_unable_to_format_json", "error_format_json");
        }
    }

    /**
     * Handle click events from the JWS Header Format button
     */
    public void formatJWSHeader() {
        try {
            view.setJWSHeader(prettyPrintJSON(view.getJWSHeader()));
        } catch (JSONException e) {
            messageDialogFactory.showErrorDialog("error_title_unable_to_format_json", "error_format_json");
        }
    }

    /**
     * Handle click events from the JWS Payload Format button
     */
    public void formatJWSPayload() {
        try {
            view.setJWSPayload(prettyPrintJSON(view.getJWSPayload()), JSON);
        } catch (JSONException e) {
            messageDialogFactory.showErrorDialog("error_title_unable_to_format_json", "error_format_json");
        }
    }
}
