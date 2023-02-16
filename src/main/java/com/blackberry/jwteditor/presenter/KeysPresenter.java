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

import com.blackberry.jwteditor.exceptions.PemException;
import com.blackberry.jwteditor.model.KeysModel;
import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.PasswordKey;
import com.blackberry.jwteditor.model.persistence.KeysModelPersistence;
import com.blackberry.jwteditor.utils.JSONUtils;
import com.blackberry.jwteditor.utils.PEMUtils;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.KeysView;
import com.blackberry.jwteditor.view.dialog.keys.AsymmetricKeyDialogFactory;
import com.blackberry.jwteditor.view.dialog.keys.KeyDialog;
import com.blackberry.jwteditor.view.dialog.keys.PasswordDialog;
import com.blackberry.jwteditor.view.dialog.keys.SymmetricKeyDialog;
import com.blackberry.jwteditor.view.rsta.RstaFactory;
import com.nimbusds.jose.jwk.*;

import javax.swing.*;
import java.util.List;

/**
 * Presenter for the Keys tab
 */
public class KeysPresenter extends Presenter {

    private final KeysModel model;
    private final KeysView view;
    private final RstaFactory rstaFactory;
    private final PresenterStore presenters;
    private final KeysModelPersistence keysModelPersistence;
    private final AsymmetricKeyDialogFactory asymmetricKeyDialogFactory;

    /**
     * Create a new KeysPresenter
     *
     * @param view                 the KeysView to associate with the presenter
     * @param presenters           the shared list of all presenters
     * @param keysModelPersistence class used to persist keys model
     * @param keysModel            KeysModel to use (or null to create a new one)
     * @param rstaFactory          Factory to create RSyntaxTextArea
     */
    public KeysPresenter(KeysView view,
                         PresenterStore presenters,
                         KeysModelPersistence keysModelPersistence,
                         KeysModel keysModel,
                         RstaFactory rstaFactory) {
        this.view = view;
        this.rstaFactory = rstaFactory;
        this.presenters = presenters;
        this.keysModelPersistence = keysModelPersistence;
        this.model = keysModel;
        this.asymmetricKeyDialogFactory = new AsymmetricKeyDialogFactory(view.getParent(), presenters, rstaFactory);

        model.setPresenter(this);
        presenters.register(this);
        updateView();
    }

    /**
     * Handler for double-click events from the keys view
     */
    public void onTableKeysDoubleClick(){
        Key key = model.getKey(view.getSelectedRow());

        KeyDialog d;

        // Get the dialog type based on the key type
        if(key instanceof JWKKey) {
            JWK jwk = ((JWKKey) key).getJWK();
            if (jwk instanceof RSAKey rsaKey) {
                d = asymmetricKeyDialogFactory.rsaKeyDialog(rsaKey);
            } else if (jwk instanceof ECKey ecKey) {
                d = asymmetricKeyDialogFactory.ecKeyDialog(ecKey);
            } else if (jwk instanceof OctetKeyPair octetKeyPair) {
                d = asymmetricKeyDialogFactory.okpDialog(octetKeyPair);
            } else if (jwk instanceof OctetSequenceKey octetSequenceKey) {
                d = new SymmetricKeyDialog(view.getParent(), presenters, rstaFactory, octetSequenceKey);
            } else {
                return;
            }
        }
        else if(key instanceof PasswordKey){
            d = new PasswordDialog(view.getParent(), presenters, (PasswordKey) key);
        }
        else {
            return;
        }

        // Show the dialog
        d.pack();
        d.setLocationRelativeTo(SwingUtilities.getWindowAncestor(view.getParent()));
        d.setVisible(true);
        // Block here until dialog completes

        // If dialog returned a key, replace the key in the store with the new key
        Key newKey = d.getKey();
        if(newKey != null){
            model.deleteKey(key.getID());
            model.addKey(d.getKey());
        }
    }

    /**
     * Refresh the keys view based on the contents of the current model
     */
    private void updateView(){
        // Create a new table view model
        KeysView.KeysTableModel keysTableModel = new KeysView.KeysTableModel();

        // Add the relevant information about each key in the store
        for (Key key : model.keys()) {
            keysTableModel.addRow(
                    new Object[]{
                            key.getID(),
                            key.getDescription(),
                            key.isPublic(),
                            key.isPrivate(),
                            key.canSign(),
                            key.canVerify(),
                            key.canEncrypt(),
                            key.canDecrypt()
                    }
            );
        }

        // Change the view table model to the newly created one
        view.setTableModel(keysTableModel);
    }

    /**
     * Check if a key exists in the key model
     *
     * @param keyId id of key to check
     * @return true if the key exists in the model
     */
    public boolean keyExists(String keyId){
        return model.getKey(keyId) != null;
    }

    /**
     * Generic handler for new key dialogs
     * @param d the type of dialog to display
     */
    public void onButtonNewClicked(KeyDialog d) {
        // Display the dialog
        d.pack();
        d.setLocationRelativeTo(SwingUtilities.getWindowAncestor(view.getParent()));
        d.setVisible(true);
        // Block here until the dialog returns

        // If the dialog returned a key, add it to the model
        if(d.getKey() != null){
            model.addKey(d.getKey());
        }
    }

    /**
     * Handler for button clicks for new symmetric keys
     */
    public void onButtonNewSymmetricClick() {
        onButtonNewClicked(new SymmetricKeyDialog(view.getParent(), presenters, rstaFactory, null));
    }

    /**
     * Handler for button clicks for new RSA keys
     */
    public void onButtonNewRSAClick() {
        onButtonNewClicked(asymmetricKeyDialogFactory.rsaKeyDialog());
    }

    /**
     * Handler for button clicks for new EC keys
     */
    public void onButtonNewECClick() {
        onButtonNewClicked(asymmetricKeyDialogFactory.ecKeyDialog());
    }

    /**
     * Handler for button clicks for new OKPs
     */
    public void onButtonNewOKPClick() {
        onButtonNewClicked(asymmetricKeyDialogFactory.okpDialog());
    }

    /**
     * Handler for button clicks for new passwords
     */
    public void onButtonNewPasswordClick() {
        onButtonNewClicked(new PasswordDialog(view.getParent(), presenters));
    }

    /**
     * Callback called by the model when the model changes
     */
    public void onModelUpdated() {
        keysModelPersistence.save(model);

        // Refresh the view
        updateView();
    }

    /**
     * Can the key at a position in the model be copied as a JWK with private key
     *
     * @param row the index of the key to be copied from the position in the view
     * @return true if the key is a JWK with private key
     */
    public boolean canCopyJWK(int row) {
        Key key = model.getKey(row);
        return key.hasJWK() && key.isPrivate();
    }

    /**
     * Can the key at a position in the model be copied as a private key PEM
     *
     * @param row the index of the key to be copied from the position in the view
     * @return true if the key has a private key and can be formatted as a PEM
     */
    public boolean canCopyPEM(int row) {
        Key key = model.getKey(row);
        return key.hasPEM() && key.isPrivate();
    }

    /**
     * Can the key at a position in the model be copied as a public key JWK
     *
     * @param row the index of the key to be copied from the position in the view
     * @return true if the key has a public key and can be formatted as a JWK
     */
    public boolean canCopyPublicJWK(int row) {
        Key key = model.getKey(row);
        return key.hasJWK() && key.isPublic();
    }

    /**
     * Can the key at a position in the model be copied as a public key PEM
     *
     * @param row the index of the key to be copied from the position in the view
     * @return true if the key has a public key and can be formatted as a PEM
     */
    public boolean canCopyPublicPEM(int row) {
        Key key = model.getKey(row);
        return key.hasPEM() && key.isPublic();
    }

    /**
     * Can the key at a position in the model be copied as a password
     *
     * @param row the index of the key to be copied from the position in the view
     * @return true if the key is a password
     */
    public boolean canCopyPassword(int row) {
        Key key = model.getKey(row);
        return key instanceof PasswordKey;
    }

    /**
     * Handle click events on the row delete popup
     *
     * @param rows array of indices of the keys to be deleted from the position in the view
     */
    public void onPopupDelete(int[] rows) {
        model.deleteKeys(rows);
    }

    /**
     * Handle click events on the copy JWK popup menu entry
     *
     * @param row the index of the key from the position in the view
     */
    public void onPopupCopyJWK(int row) {
        JWKKey jwkKey = (JWKKey) model.getKey(row);
        JWK jwk = jwkKey.getJWK();
        Utils.copyToClipboard(JSONUtils.prettyPrintJSON(jwk.toJSONString()));
    }

    /**
     * Handle click events on the copy PEM popup menu entry
     *
     * @param row the index of the key from the position in the view
     */
    public void onPopupCopyPEM(int row) {
        JWKKey jwkKey = (JWKKey) model.getKey(row);
        JWK jwk = jwkKey.getJWK();
        try {
            Utils.copyToClipboard(PEMUtils.jwkToPem(jwk));
        } catch (PemException e) {
            throw new IllegalStateException("Shouldn't happen - call canCopyPEM first");
        }
    }

    /**
     * Handle click events on the copy public JWK popup menu entry
     *
     * @param row the index of the key from the position in the view
     */
    public void onPopupCopyPublicJWK(int row) {
        JWKKey jwkKey = (JWKKey) model.getKey(row);
        JWK jwk = jwkKey.getJWK().toPublicJWK();
        Utils.copyToClipboard(JSONUtils.prettyPrintJSON(jwk.toJSONString()));
    }

    /**
     * Handle click events on the copy public PEM popup menu entry
     *
     * @param row the index of the key from the position in the view
     */
    public void onPopupCopyPublicPEM(int row) {
        JWKKey jwkKey = (JWKKey) model.getKey(row);
        JWK jwk = jwkKey.getJWK().toPublicJWK();
        try {
            Utils.copyToClipboard(PEMUtils.jwkToPem(jwk));
        } catch (PemException e) {
            throw new IllegalStateException("Shouldn't happen - call canCopyPEM first");
        }
    }

    /**
     * Handle click events on the copy password popup menu entry
     *
     * @param row the index of the key from the position in the view
     */
    public void onPopupCopyPassword(int row) {
        PasswordKey passwordKey = (PasswordKey) model.getKey(row);
        Utils.copyToClipboard(passwordKey.getPassword());
    }

    /**
     * Get a list of signing keys from the model
     * @return list of keys that can be used for signing
     */
    public List<Key> getSigningKeys(){
        return model.getSigningKeys();
    }

    /**
     * Get a list of encryption keys from the model
     * @return list of keys that can be used for encryption
     */
    public List<Key> getEncryptionKeys(){
        return model.getEncryptionKeys();
    }

    /**
     * Get a list of decryption keys from the model
     * @return list of keys that can be used for decryption
     */
    public List<Key> getDecryptionKeys() { return model.getDecryptionKeys(); }

    /**
     * Get a list of verification keys from the model
     * @return list of keys that can be used for verification
     */
    public List<Key> getVerificationKeys() { return model.getVerificationKeys(); }
}
