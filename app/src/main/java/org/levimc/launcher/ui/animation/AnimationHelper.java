package org.levimc.launcher.ui.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import org.levimc.launcher.R;
import org.levimc.launcher.databinding.ActivityMainBinding;

public class AnimationHelper {
    public static void prepareInitialStates(ActivityMainBinding binding) {
        binding.header.setVisibility(View.INVISIBLE);
        setViewAnimationState(binding.mainCard, 30f);
        setViewAnimationState(binding.modCard, 30f);
        setViewAnimationState(binding.aboutCard, 30f);
        binding.githubIcon.setAlpha(0f);
    }

    private static void setViewAnimationState(View view, float translationY) {
        view.setAlpha(0f);
        view.setTranslationY(translationY);
    }

    public static void runInitializationSequence(ActivityMainBinding binding) {
        binding.header.postDelayed(() -> startHeaderAnimation(binding.header), 300);
        binding.mainCard.postDelayed(() -> animateCard(binding.mainCard, 600, 1.2f, 0), 500);
        binding.modCard.postDelayed(() -> animateCard(binding.modCard, 400, 1f, 200), 700);
        binding.aboutCard.postDelayed(() -> animateCard(binding.aboutCard, 400, 1f, 400), 800);
        binding.githubIcon.postDelayed(() -> animateGithubIcon(binding.githubIcon), 1000);
    }

    private static void startHeaderAnimation(View header) {
        header.setVisibility(View.VISIBLE);
        header.startAnimation(AnimationUtils.loadAnimation(header.getContext(), R.anim.slide_in_top));
    }

    private static void animateCard(View view, int duration, float tension, long delay) {
        // Create fade and translate animations
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0f);
        
        // Create floating animation
        ValueAnimator floatAnim = ValueAnimator.ofFloat(0f, 1f);
        floatAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatAnim.setRepeatMode(ValueAnimator.REVERSE);
        floatAnim.setDuration(3000);
        floatAnim.setInterpolator(new LinearInterpolator());
        floatAnim.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            float translation = (float) (Math.sin(value * Math.PI * 2) * 10); // -10px to 10px
            view.setTranslationY(translation);
        });
        
        // Set up animation set
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeIn, translateY);
        set.setInterpolator(new OvershootInterpolator(tension));
        set.setDuration(duration);
        set.setStartDelay(delay);
        set.start();
        
        // Start floating animation after the initial animation
        view.postDelayed(floatAnim::start, duration + delay);
    }

    private static void animateGithubIcon(View githubIcon) {
        githubIcon.setAlpha(1f);
        
        // Floating animation
        ObjectAnimator floatAnim = ObjectAnimator.ofFloat(githubIcon, "translationY", 0f, -15f, 0f);
        floatAnim.setDuration(3000);
        floatAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatAnim.setInterpolator(new LinearInterpolator());
        floatAnim.start();
    }
}
