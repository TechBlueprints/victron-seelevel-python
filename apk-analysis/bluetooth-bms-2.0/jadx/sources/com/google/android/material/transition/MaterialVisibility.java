package com.google.android.material.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.transition.VisibilityAnimatorProvider;
import java.util.ArrayList;

/* loaded from: classes.dex */
abstract class MaterialVisibility<P extends VisibilityAnimatorProvider> extends Visibility {
    private P primaryAnimatorProvider;
    private VisibilityAnimatorProvider secondaryAnimatorProvider;

    abstract P getDefaultPrimaryAnimatorProvider();

    abstract VisibilityAnimatorProvider getDefaultSecondaryAnimatorProvider();

    MaterialVisibility() {
    }

    void initialize() {
        this.primaryAnimatorProvider = (P) getDefaultPrimaryAnimatorProvider();
        this.secondaryAnimatorProvider = getDefaultSecondaryAnimatorProvider();
    }

    public P getPrimaryAnimatorProvider() {
        if (this.primaryAnimatorProvider == null) {
            this.primaryAnimatorProvider = (P) getDefaultPrimaryAnimatorProvider();
        }
        return this.primaryAnimatorProvider;
    }

    public VisibilityAnimatorProvider getSecondaryAnimatorProvider() {
        return this.secondaryAnimatorProvider;
    }

    public void setSecondaryAnimatorProvider(VisibilityAnimatorProvider visibilityAnimatorProvider) {
        this.secondaryAnimatorProvider = visibilityAnimatorProvider;
    }

    @Override // android.transition.Visibility
    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return createAnimator(viewGroup, view, transitionValues, transitionValues2, true);
    }

    @Override // android.transition.Visibility
    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return createAnimator(viewGroup, view, transitionValues, transitionValues2, false);
    }

    private Animator createAnimator(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2, boolean z) {
        Animator animatorCreateDisappear;
        Animator animatorCreateDisappear2;
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        if (z) {
            animatorCreateDisappear = getPrimaryAnimatorProvider().createAppear(viewGroup, view, transitionValues, transitionValues2);
        } else {
            animatorCreateDisappear = getPrimaryAnimatorProvider().createDisappear(viewGroup, view, transitionValues, transitionValues2);
        }
        if (animatorCreateDisappear != null) {
            arrayList.add(animatorCreateDisappear);
        }
        VisibilityAnimatorProvider secondaryAnimatorProvider = getSecondaryAnimatorProvider();
        if (secondaryAnimatorProvider != null) {
            if (z) {
                animatorCreateDisappear2 = secondaryAnimatorProvider.createAppear(viewGroup, view, transitionValues, transitionValues2);
            } else {
                animatorCreateDisappear2 = secondaryAnimatorProvider.createDisappear(viewGroup, view, transitionValues, transitionValues2);
            }
            if (animatorCreateDisappear2 != null) {
                arrayList.add(animatorCreateDisappear2);
            }
        }
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        return animatorSet;
    }
}
