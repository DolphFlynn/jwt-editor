/*
Author : Dolph Flynn

Copyright 2025 Dolph Flynn

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

package com.blackberry.jwteditor.view.editor;

import com.blackberry.jwteditor.presenter.EditorPresenter;
import com.blackberry.jwteditor.utils.Utils;

import javax.swing.*;

class EditorViewAttackMenuFactory {
    private final EditorPresenter presenter;
    private final boolean isProVersion;

    EditorViewAttackMenuFactory(EditorPresenter presenter, boolean isProVersion) {
        this.presenter = presenter;
        this.isProVersion = isProVersion;
    }

    JPopupMenu buildAttackPopupMenu() {

        JPopupMenu popupMenuAttack = new JPopupMenu();
        JMenuItem menuItemAttackEmbedJWK = new JMenuItem(Utils.getResourceString("editor_view_button_attack_embed_jwk"));
        JMenuItem menuItemAttackSignNone = new JMenuItem(Utils.getResourceString("editor_view_button_attack_sign_none"));
        JMenuItem menuItemAttackKeyConfusion = new JMenuItem(Utils.getResourceString("editor_view_button_attack_key_confusion"));
        JMenuItem menuItemAttackSignEmptyKey = new JMenuItem(Utils.getResourceString("editor_view_button_attack_sign_empty_key"));
        JMenuItem menuItemAttackSignPsychicSignature = new JMenuItem(Utils.getResourceString("editor_view_button_attack_sign_psychic_signature"));
        JMenuItem menuItemAttackEmbedCollaboratorPayload = new JMenuItem(Utils.getResourceString("editor_view_button_attack_embed_collaborator_payload"));
        JMenuItem menuItemAttackWeakSymmetricKey = new JMenuItem(Utils.getResourceString("editor_view_button_attack_weak_symmetric_key"));

        menuItemAttackEmbedJWK.addActionListener(e -> presenter.onAttackEmbedJWKClicked());
        menuItemAttackKeyConfusion.addActionListener(e -> presenter.onAttackKeyConfusionClicked());
        menuItemAttackSignNone.addActionListener(e -> presenter.onAttackSignNoneClicked());
        menuItemAttackSignEmptyKey.addActionListener(e -> presenter.onAttackSignEmptyKeyClicked());
        menuItemAttackSignPsychicSignature.addActionListener(e -> presenter.onAttackPsychicSignatureClicked());
        menuItemAttackEmbedCollaboratorPayload.addActionListener(e -> presenter.onAttackEmbedCollaboratorPayloadClicked());
        menuItemAttackWeakSymmetricKey.addActionListener(e -> presenter.onAttackWeakHMACSecret());

        menuItemAttackEmbedCollaboratorPayload.setEnabled(isProVersion);

        popupMenuAttack.add(menuItemAttackEmbedJWK);
        popupMenuAttack.add(menuItemAttackSignNone);
        popupMenuAttack.add(menuItemAttackKeyConfusion);
        popupMenuAttack.add(menuItemAttackSignEmptyKey);
        popupMenuAttack.add(menuItemAttackSignPsychicSignature);
        popupMenuAttack.add(menuItemAttackEmbedCollaboratorPayload);
        popupMenuAttack.add(menuItemAttackWeakSymmetricKey);

        return popupMenuAttack;
    }
}
