package com.blackberry.jwteditor.view.dialog.operations;

import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.view.dialog.operations.SigningDialog.Mode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LastSigningKeys {
    private final Map<Mode, Key> lastKeyMap = new HashMap<>();

    Optional<Key> lastKeyFor(Mode mode) {
        return Optional.ofNullable(lastKeyMap.get(mode));
    }

    void recordKeyUse(Mode mode, Key key) {
        lastKeyMap.put(mode, key);
    }
}
