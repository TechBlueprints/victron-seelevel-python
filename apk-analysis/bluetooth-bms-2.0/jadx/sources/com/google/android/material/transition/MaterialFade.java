package com.google.android.material.transition;

import android.animation.Animator;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.animation.AnimationUtils;

/* loaded from: classes.dex */
public class MaterialFade extends MaterialVisibility<FadeProvider> {
    private static final long DEFAULT_DURATION_ENTER = 150;
    private static final long DEFAULT_DURATION_RETURN = 75;
    private static final float DEFAULT_FADE_END_THRESHOLD_ENTER = 0.3f;
    private static final float DEFAULT_START_SCALE = 0.8f;
    private boolean entering;

    @Override // com.google.android.material.transition.MaterialVisibility
    public /* bridge */ /* synthetic */ VisibilityAnimatorProvider getSecondaryAnimatorProvider() {
        return super.getSecondaryAnimatorProvider();
    }

    @Override // com.google.android.material.transition.MaterialVisibility, android.transition.Visibility
    public /* bridge */ /* synthetic */ Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return super.onAppear(viewGroup, view, transitionValues, transitionValues2);
    }

    @Override // com.google.android.material.transition.MaterialVisibility, android.transition.Visibility
    public /* bridge */ /* synthetic */ Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return super.onDisappear(viewGroup, view, transitionValues, transitionValues2);
    }

    @Override // com.google.android.material.transition.MaterialVisibility
    public /* bridge */ /* synthetic */ void setSecondaryAnimatorProvider(VisibilityAnimatorProvider visibilityAnimatorProvider) {
        super.setSecondaryAnimatorProvider(visibilityAnimatorProvider);
    }

    public static MaterialFade create() {
        return create(true);
    }

    public static MaterialFade create(boolean z) {
        return new MaterialFade(z);
    }

    private MaterialFade(boolean z) {
        this.entering = z;
        setDuration(z ? 150L : 75L);
        setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        initialize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.transition.MaterialVisibility
    public FadeProvider getDefaultPrimaryAnimatorProvider() {
        FadeProvider fadeProvider = new FadeProvider();
        if (this.entering) {
            fadeProvider.setIncomingEndThreshold(DEFAULT_FADE_END_THRESHOLD_ENTER);
        }
        return fadeProvider;
    }

    @Override // com.google.android.material.transition.MaterialVisibility
    VisibilityAnimatorProvider getDefaultSecondaryAnimatorProvider() {
        ScaleProvider scaleProvider = new ScaleProvider();
        scaleProvider.setScaleOnDisappear(false);
        scaleProvider.setIncomingStartScale(DEFAULT_START_SCALE);
        return scaleProvider;
    }
}
