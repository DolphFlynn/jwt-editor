package com.blackberry.jwteditor.model.keys;

public interface KeysModelListener {
    void notifyKeyInserted(Key key);

    void notifyKeyDeleted(int rowIndex);

    void notifyKeyDeleted(Key key);

    class InertKeyModelListener implements KeysModelListener {
        @Override
        public void notifyKeyInserted(Key key) {
        }

        @Override
        public void notifyKeyDeleted(int rowIndex) {
        }

        @Override
        public void notifyKeyDeleted(Key key) {
        }
    }

    class SimpleKeyModelListener implements KeysModelListener {
        private final Runnable action;

        public SimpleKeyModelListener(Runnable action) {
            this.action = action;
        }

        @Override
        public void notifyKeyInserted(Key key) {
            action.run();
        }

        @Override
        public void notifyKeyDeleted(int rowIndex) {
            action.run();
        }

        @Override
        public void notifyKeyDeleted(Key key) {
            action.run();
        }
    }
}
