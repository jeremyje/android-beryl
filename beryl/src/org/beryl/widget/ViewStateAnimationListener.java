package org.beryl.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/** Manages the state of a view on the start and end of an animation. */
public class ViewStateAnimationListener implements AnimationListener
{
	private final View _view;
	private int _endVisibility = View.VISIBLE;

	public ViewStateAnimationListener(final View v)
	{
		_view = v;
	}

	public void setVisibilityOnEnd(int visibility)
	{
		_endVisibility = visibility;
	}

	public void onAnimationEnd(Animation animation)
	{
		_view.setVisibility(_endVisibility);
	}

	public void onAnimationRepeat(Animation animation)
	{
		// Don't care about repeats.
	}

	public void onAnimationStart(Animation animation)
	{
		// I can't see a case where we want the view not to be visible during an animation.
		_view.setVisibility(View.VISIBLE);
	}

}
