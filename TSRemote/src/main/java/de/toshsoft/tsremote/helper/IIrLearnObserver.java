package de.toshsoft.tsremote.helper;

import android.view.View;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public interface IIrLearnObserver {
    public void KeyLearned(int status, String remoteName, String keyName, View changedView);
}
